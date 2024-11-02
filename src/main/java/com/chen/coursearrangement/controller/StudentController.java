package com.chen.coursearrangement.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chen.coursearrangement.common.ConstantInfo;
import com.chen.coursearrangement.common.Result;
import com.chen.coursearrangement.entity.Student;
import com.chen.coursearrangement.entity.dto.StudentDTO;
import com.chen.coursearrangement.entity.dto.UserDTO;
import com.chen.coursearrangement.service.IStudentService;
import com.chen.coursearrangement.service.IUserService;
import com.chen.coursearrangement.utils.StudentExcelListener;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 *
 * @author Jack Chen
 * @since 2023-05-12
 */
@RestController
@RequestMapping("/student")
public class StudentController {

    @Resource
    private IStudentService studentService;

    @Resource
    private IUserService userService;

    //新增或者更新
    @PostMapping
    public Result save(@RequestBody Student student) {
        UserDTO userInfo = userService.getUserRoleAndCollege(StpUtil.getLoginIdAsString());
        String role = userInfo.getRole();
        String college = userInfo.getCollege();
        if (ConstantInfo.ROLE_ADMINISTRATOR.equals(role) || (ConstantInfo.ROLE_ADMIN.equals(role) && college.equals(student.getCollegeNo()))) {
            // 系统管理员有权限操作所有学生，或者管理员有权限操作本学院学生

            saveStudentData(student);
            return Result.success();
        } else {
            // 其他角色无权限
            return Result.error(ConstantInfo.CODE_401, "权限不足");
        }
    }

    private void saveStudentData(Student student) {
        try {
            // 若为新增学生，则添加用户数据
            if (student.getId() == null) {
                studentService.addStudentUser(student);
                //更新对应班级人数
                studentService.updateClassStudentNumber(student.getClassNo(),1);
            }
            // 保存或更新学生信息
            studentService.saveOrUpdate(student);
        } catch (Exception e) {
            // 异常处理，返回错误信息
            throw new RuntimeException("保存学生数据失败：" + e.getMessage());
        }
    }

    //删除学生
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        UserDTO userInfo = userService.getUserRoleAndCollege(StpUtil.getLoginIdAsString());
        String role = userInfo.getRole();
        String college = userInfo.getCollege();

        Student student = studentService.getById(id);
        if (student == null) {
            return Result.error(ConstantInfo.CODE_404, "学生不存在");
        }
        if (ConstantInfo.ROLE_ADMINISTRATOR.equals(role) || (ConstantInfo.ROLE_ADMIN.equals(role) && college.equals(student.getCollegeNo()))) {
            // 系统管理员有权限操作所有学生，或者管理员有权限操作本学院学生
            deleteStudentData(id);
            //修改对应班级人数
            studentService.updateClassStudentNumber(student.getClassNo(),-1);
            return Result.success();
        } else {
            // 其他角色无权限
            return Result.error(ConstantInfo.CODE_401, "权限不足");
        }
    }

    private void deleteStudentData(Integer id) {
        studentService.deleteStudentUser(id);
        studentService.removeById(id);
    }


@PostMapping("/del/batch")
public Result deleteBatch(@RequestBody List<Integer> ids) { //批量删除
    UserDTO userInfo = userService.getUserRoleAndCollege(StpUtil.getLoginIdAsString());
    String role = userInfo.getRole();
    String college = userInfo.getCollege();

    if (ConstantInfo.ROLE_ADMINISTRATOR.equals(role)) {
        // 系统管理员有权限操作所有学生
        if (!ids.isEmpty()) {
            for (int id : ids) {
                Student student = studentService.getById(id);
                studentService.deleteStudentUser(id);     //先删用户，后删学生
                //修改对应班级人数
                studentService.updateClassStudentNumber(student.getClassNo(),-1);
            }
            studentService.removeByIds(ids);
        }
        return Result.success();
    } else if (ConstantInfo.ROLE_ADMIN.equals(role)) {
        // 管理员有权限操作本学院学生
        if (!ids.isEmpty()) {
            for (int id : ids) {
                Student student = studentService.getById(id);
                if (student != null && college.equals(student.getCollegeNo())) {
                    studentService.deleteStudentUser(id);
                    studentService.removeById(id);
                    //修改对应班级人数
                    studentService.updateClassStudentNumber(student.getClassNo(),-1);
                }
            }
        }
        return Result.success();
    } else {
        // 其他角色无权限
        return Result.error(ConstantInfo.CODE_401, "权限不足");
    }
}


//进行编辑时查询数据
@GetMapping("/{id}")
public Result findOne(@PathVariable Integer id) {
    UserDTO userInfo = userService.getUserRoleAndCollege(StpUtil.getLoginIdAsString());
    String role = userInfo.getRole();
    String college = userInfo.getCollege();

    Student student = studentService.getById(id);
    if (student == null) {
        return Result.error(ConstantInfo.CODE_404, "学生不存在");
    }
    if (ConstantInfo.ROLE_ADMINISTRATOR.equals(role) ||(ConstantInfo.ROLE_ADMIN.equals(role) && college.equals(student.getCollegeNo()))) {
        // 系统管理员有权限操作所有学生，或者管理员有权限操作本学院学生
        return Result.success(student);
    } else {
        // 其他角色无权限
        return Result.error(ConstantInfo.CODE_401, "权限不足");
    }
}

    @GetMapping("/getClassNoByStudentNo/{studentNo}")
    public Result getClassNoByStudentNo(@PathVariable String studentNo) {
        UserDTO userInfo = userService.getUserRoleAndCollege(StpUtil.getLoginIdAsString());
        String role = userInfo.getRole();
        if (ConstantInfo.ROLE_STUDENT.equals(role) ) {
            QueryWrapper queryWrapper=new QueryWrapper();
            queryWrapper.eq("student_no",studentNo);
            Student student = studentService.getOne(queryWrapper);
            if (student != null) {
                String classNo = student.getClassNo();
                return Result.success("查询成功",classNo);
            }
            return Result.error(ConstantInfo.CODE_404, "班级信息查询失败");
        } else {
            // 其他角色无权限
            return Result.error(ConstantInfo.CODE_401, "权限不足");
        }
    }

//查询所有数据
@GetMapping("/page")
public Result findPage(
        @RequestParam Integer pageNum, @RequestParam Integer pageSize,
        @RequestParam(defaultValue = "") String college,
        @RequestParam(defaultValue = "") String grade,
        @RequestParam(defaultValue = "") String classNo,
        @RequestParam(defaultValue = "") String studentNo,
        @RequestParam(defaultValue = "") String studentName,
        @RequestParam(defaultValue = "") String term){
    //如果是 ROLE_ADMINISTRATOR，则无论 college 是什么都能访问所有数据。如果是 ROLE_ADMIN，则只能访问当前用户所在学院的数据。
    // 获取当前用户的角色信息
    UserDTO userInfo = userService.getUserRoleAndCollege(StpUtil.getLoginIdAsString());
    String role = userInfo.getRole();
    // 判断权限
    if (ConstantInfo.ROLE_ADMINISTRATOR.equals(role)) {
        // 如果是系统管理员，使用当前用户的学院信息
        return Result.success(studentService.findPage(new Page<>(pageNum, pageSize), college, grade, classNo, studentNo, studentName));

    } else if (ConstantInfo.ROLE_ADMIN.equals(role)) {
        // 如果是管理员，只能访问当前用户所在学院的数据
        college = userInfo.getCollege();
        return Result.success(studentService.findPage(new Page<>(pageNum, pageSize), college, grade, classNo, studentNo, studentName));
    } else if (ConstantInfo.ROLE_TEACHER.equals(role)) {
        return Result.success(studentService.findPageByTeacher(new Page<>(pageNum, pageSize),term,StpUtil.getLoginIdAsString(), classNo,studentNo, studentName));
    } else {
        // 其他角色没有权限
        return Result.error(ConstantInfo.CODE_401, "权限不足");
    }
}

//生成学号
@PostMapping("/generateStudentNo")
public Result generateStudentNo(@RequestBody Map<String, Object> requestData) {
    // 获取 form 中的参数
    String classNo = String.valueOf(requestData.get("classNo"));

    // 查询数据库中学号最后两位最大的数
    int maxLastTwoDigits = studentService.getMaxLastTwoDigits(classNo);

    // 生成新的学号
    String studentNo = classNo + String.format("%02d", maxLastTwoDigits + 1);

    // 返回生成的学号到前端
    return Result.success("学号生成成功！",studentNo);
}

    @GetMapping("/export")
    public void export(HttpServletResponse response,
                       @RequestParam(defaultValue = "") String college,
                       @RequestParam(defaultValue = "") String grade,
                       @RequestParam(defaultValue = "") String classNo,
                       @RequestParam(defaultValue = "") String studentNo,
                       @RequestParam(defaultValue = "") String studentName,
                       @RequestParam(defaultValue = "") String term) throws IOException {
        UserDTO userInfo = userService.getUserRoleAndCollege(StpUtil.getLoginIdAsString());
        String role = userInfo.getRole();
        if (ConstantInfo.ROLE_ADMIN.equals(role)) {
            // 管理员只能导出本学院的数据
            college = userInfo.getCollege();
        }
        try {
            List<StudentDTO> list;
            // 根据角色调用不同的导出方法
            if (ConstantInfo.ROLE_TEACHER.equals(role)) {
                list=studentService.exportByTeacher(term,StpUtil.getLoginIdAsString(), classNo,studentNo, studentName);
            } else {
                // 从数据库查询出所有的数据
                list= studentService.export(college, grade, classNo, studentNo, studentName);
            }

            // 生成导出文件名
            String fileName = generateExportFileName("学生信息");
            // 设置响应头信息
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            // 设置响应头中的Content-disposition，指定导出文件名，并使用UTF-8编码
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
            response.setHeader("Access-Control-Expose-Headers","Content-Disposition");

            // 使用EasyExcel进行数据导出
            EasyExcel.write(response.getOutputStream(), StudentDTO.class).sheet("学生信息").doWrite(list);

        } catch (Exception e) {
            // 异常处理，抛出异常
            throw new RuntimeException("导出数据失败：" + e.getMessage());
        }
    }


    /**
     * 生成导出文件名
     */
    private String generateExportFileName(String prefix) throws UnsupportedEncodingException {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());   //年月日时分秒
        String fileName = prefix + "_" + timestamp;
        // 使用URLEncoder编码文件名，防止中文乱码
        return  URLEncoder.encode(fileName, "UTF-8");
    }


    @RequestMapping("/import")
    @ResponseBody
    public Result importExcel(/*@RequestParam("excelFile")*/ MultipartFile file){
        // 参数验证
        if (file == null || file.isEmpty()) {
            return Result.error(ConstantInfo.CODE_400, "上传的文件不能为空");
        }
        try {
            // 使用 EasyExcel 读取 Excel 数据，并交给 StudentExcelListener 处理
            StudentExcelListener listener = new StudentExcelListener(studentService);
            EasyExcel.read(file.getInputStream(), StudentDTO.class,listener).sheet().doRead();
            // 获取成功导入的数据条数
            int importedCount = listener.getImportedCount();
            return Result.success("导入成功，共导入 " + importedCount + " 条数据");
        } catch (Exception e) {
            // 异常处理，返回错误信息
            return Result.error(ConstantInfo.CODE_500, "导入失败：" + e.getMessage());
        }
    }

}

//数据导出
/*@GetMapping("/export")
public Result export(HttpServletResponse response,
                     @RequestParam(defaultValue = "") String college,
                     @RequestParam(defaultValue = "") String grade,
                     @RequestParam(defaultValue = "") String classNo,
                     @RequestParam(defaultValue = "") String studentNo,
                     @RequestParam(defaultValue = "") String studentName) throws IOException {

    UserDTO userInfo = TokenUtils.getCurrentUserRoleAndCollege();
    if (userInfo == null) {
        return Result.error(ConstantInfo.CODE_404, "用户信息获取失败");
    }
    String role = userInfo.getRole();

    if(ConstantInfo.ROLE_ADMIN.equals(role)){
        //对管理员进行权限控制
        college=userInfo.getCollege();
    }
    // 从数据库查询出所有的数据
    List<StudentDTO> list = studentService.export(college, grade, classNo, studentNo, studentName);
    // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    response.setCharacterEncoding("utf-8");
    // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
    String fileName = URLEncoder.encode("学生信息", "UTF-8").replaceAll("\\+", "%20");
    response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
    EasyExcel.write(response.getOutputStream(), StudentDTO.class).sheet("学生信息").doWrite(list);
    return Result.success();
}*/


//导入Excel
/*
@RequestMapping("/import")
@ResponseBody
public Result importExcel(*//*@RequestParam(value = "excelFile")*//* MultipartFile file) throws IOException{
    EasyExcel.read(file.getInputStream(), StudentDTO.class, new StudentExcelListener(studentService)).sheet().doRead();
    return Result.success();
}*/
