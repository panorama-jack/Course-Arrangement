package com.chen.coursearrangement.controller;


import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chen.coursearrangement.common.ConstantInfo;
import com.chen.coursearrangement.common.Result;
import com.chen.coursearrangement.entity.Teacher;
import com.chen.coursearrangement.entity.dto.TeacherDTO;
import com.chen.coursearrangement.entity.dto.UserDTO;
import com.chen.coursearrangement.service.ITeacherService;
import com.chen.coursearrangement.service.IUserService;
import com.chen.coursearrangement.utils.TeacherExcelListener;
import org.springframework.transaction.annotation.Transactional;
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
 * @since 2024-01-15
 */
@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Resource
    private ITeacherService teacherService;

    @Resource
    private IUserService userService;
    //新增或者更新
    @PostMapping
    @Transactional
    public Result save(@RequestBody Teacher teacher) {
        UserDTO userInfo = userService.getUserRoleAndCollege(StpUtil.getLoginIdAsString());
        String role = userInfo.getRole();
        String college = userInfo.getCollege();

        if (ConstantInfo.ROLE_ADMINISTRATOR.equals(role) || (ConstantInfo.ROLE_ADMIN.equals(role) && college.equals(teacher.getCollegeNo()))) {
            // 系统管理员有权限操作所有教师，或者管理员有权限操作本学院教师
            teacherService.saveOrUpdate(teacher);
            // 若为新增教师，则添加用户数据
            if (teacher.getId() == null) {
                teacherService.addTeacherUser(teacher);
            }
            return Result.success();
        } else {
            // 其他角色无权限
            return Result.error(ConstantInfo.CODE_401, "权限不足");
        }
    }

    //删除
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        UserDTO userInfo = userService.getUserRoleAndCollege(StpUtil.getLoginIdAsString());
        String role = userInfo.getRole();
        String college = userInfo.getCollege();

        Teacher teacher = teacherService.getById(id);
        if (teacher == null) {
            return Result.error(ConstantInfo.CODE_404, "教师不存在");
        }
        if (ConstantInfo.ROLE_ADMINISTRATOR.equals(role) || (ConstantInfo.ROLE_ADMIN.equals(role) && college.equals(teacher.getCollegeNo()))) {
            // 系统管理员有权限操作所有教师，或者管理员有权限操作本学院教师
            teacherService.removeById(id);
            teacherService.deleteTeacherUser(id);
            return Result.success();
        } else {
            // 其他角色无权限
            return Result.error(ConstantInfo.CODE_401, "权限不足");
        }
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        UserDTO userInfo = userService.getUserRoleAndCollege(StpUtil.getLoginIdAsString());
        String role = userInfo.getRole();
        String college = userInfo.getCollege();

        if (ConstantInfo.ROLE_ADMINISTRATOR.equals(role)) {
            // 系统管理员有权限操作所有教师
            deleteTeacherBatch(ids);
            return Result.success();
        } else if (ConstantInfo.ROLE_ADMIN.equals(role)) {
            // 管理员有权限操作本学院教师
            deleteTeacherBatchInCollege(ids, college);
            return Result.success();
        } else {
            // 其他角色无权限
            return Result.error(ConstantInfo.CODE_401, "权限不足");
        }
    }

    private void deleteTeacherBatch(List<Integer> ids) {
        if (!ids.isEmpty()) {
            teacherService.removeByIds(ids);
            for (int id : ids) {
                teacherService.deleteTeacherUser(id);
            }
        }
    }

    private void deleteTeacherBatchInCollege(List<Integer> ids, String college) {
        if (!ids.isEmpty()) {
            for (int id : ids) {
                Teacher teacher = teacherService.getById(id);
                if (teacher != null && college.equals(teacher.getCollegeNo())) {
                    teacherService.removeById(id);
                    teacherService.deleteTeacherUser(id);
                }
            }
        }
    }


    @GetMapping
    public Result findAll() {
        UserDTO userInfo = userService.getUserRoleAndCollege(StpUtil.getLoginIdAsString());
        String role = userInfo.getRole();
        String college = userInfo.getCollege();
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();

        if (ConstantInfo.ROLE_ADMINISTRATOR.equals(role)) {
            // 系统管理员可以查看所有教师信息
            // 不添加学院条件，查询所有
        } else if (ConstantInfo.ROLE_ADMIN.equals(role)) {
            // 管理员只能查看本学院教师信息
            queryWrapper.eq("college_no", college);
        } else {
            // 其他角色无权限查看所有教师信息
            return Result.error(ConstantInfo.CODE_401, "权限不足");
        }
        return Result.success(teacherService.list(queryWrapper));
    }


    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        UserDTO userInfo = userService.getUserRoleAndCollege(StpUtil.getLoginIdAsString());
         String role = userInfo.getRole();
        String college = userInfo.getCollege();
        Teacher teacher = teacherService.getById(id);
        if (teacher == null) {
            return Result.error(ConstantInfo.CODE_404, "教师不存在");
        }

        if (ConstantInfo.ROLE_ADMINISTRATOR.equals(role) || (ConstantInfo.ROLE_ADMIN.equals(role) && college.equals(teacher.getCollegeNo()))) {
            // 系统管理员有权限操作所有教师，或者管理员有权限操作本学院教师
            return Result.success(teacher);
        } else {
            // 其他角色无权限
            return Result.error(ConstantInfo.CODE_401, "权限不足");
        }
    }

    @GetMapping("/selectByTeacherNo/{teacherNo}")
    public Result selectByTeacherNo(@PathVariable String teacherNo) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teacher_no", teacherNo).select("teacher_name");
        Teacher teacher = teacherService.getOne(queryWrapper);
        if (teacher != null) {
            return Result.success("查找成功",teacher.getTeacherName());
        } else {
            return Result.error("未找到相关记录");
        }
    }

    @GetMapping("/selectByCollege/{college}")
    public Result selectByCollege(@PathVariable String college) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("college_no", college);
        return Result.success(teacherService.list(queryWrapper));
    }

    @PostMapping("/generateTeacherNo")
    public Result generateTeacherNo(@RequestBody Map<String, Object> requestData) {

        // 获取 form 中的参数
        String college = String.valueOf(requestData.get("college"));

        // 查询数据库中工号最后三位最大的数
        int maxLastTwoDigits = teacherService.getMaxLastThreeDigits(college);

        // 生成新的学号
        String teacherNo = college + String.format("%03d", maxLastTwoDigits + 1);

        // 返回生成的工号到前端
        return Result.success("工号生成成功！",teacherNo);
    }


    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String teacherNo,
                           @RequestParam(defaultValue = "") String teacherName,
                           @RequestParam(defaultValue = "") String college) {
        UserDTO userInfo = userService.getUserRoleAndCollege(StpUtil.getLoginIdAsString());

        String role = userInfo.getRole();
        // 对管理员进行权限控制
        if (ConstantInfo.ROLE_ADMIN.equals(role)) {
            // 如果是 ROLE_ADMIN，使用当前用户的学院信息
            college = userInfo.getCollege();
        }
        return Result.success(teacherService.findPage(new Page<>(pageNum, pageSize), teacherNo, teacherName, college));
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response,
                         @RequestParam(defaultValue = "") String college,
                         @RequestParam(defaultValue = "") String teacherNo,
                         @RequestParam(defaultValue = "") String teacherName) throws IOException {
            UserDTO userInfo = userService.getUserRoleAndCollege(StpUtil.getLoginIdAsString());
            String role = userInfo.getRole();
            // 对管理员进行权限控制
            if (ConstantInfo.ROLE_ADMIN.equals(role)) {
                // 如果是 ROLE_ADMIN，使用当前用户的学院信息
                college = userInfo.getCollege();
            }

        try {  // 从数据库查询出所有的数据
            List<TeacherDTO> list = teacherService.export(college, teacherNo, teacherName);

            // 设置响应头信息
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");

            // 生成导出文件名
            String fileName = generateExportFileName("教师信息");

            // 设置响应头中的Content-disposition，指定导出文件名
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            response.setHeader("Access-Control-Expose-Headers","Content-Disposition");
            // 使用EasyExcel进行数据导出
            EasyExcel.write(response.getOutputStream(), TeacherDTO.class).sheet("教师信息").doWrite(list);

        } catch (Exception e) {
            // 异常处理，抛出异常
            throw new RuntimeException("导出数据失败：" + e.getMessage());
        }
    }
    /**
     * 生成导出文件名
     */
    private String generateExportFileName(String prefix) throws UnsupportedEncodingException {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String fileName = prefix + "_" + timestamp;
        // 使用URLEncoder编码文件名，防止中文乱码
        return URLEncoder.encode(fileName, "UTF-8");
    }

    //导入Excel
    @RequestMapping("/import")
    @ResponseBody
    public Result importExcel(/*@RequestParam("excelFile")*/ MultipartFile file) {
        // 参数验证
        if (file == null || file.isEmpty()) {
            return Result.error(ConstantInfo.CODE_400, "上传的文件不能为空");
        }
        try {
           TeacherExcelListener listener=new TeacherExcelListener(teacherService);
            // 使用 EasyExcel 读取 Excel 数据，并交给 TeacherExcelListener 处理
            // 获取成功导入的数据条数
            int importedCount = listener.getImportedCount();
            return Result.success("导入成功，共导入 " + importedCount + " 条数据");
        } catch (Exception e) {
            // 异常处理，返回错误信息
            return Result.error(ConstantInfo.CODE_500, "导入失败：" + e.getMessage());
        }
    }

}
