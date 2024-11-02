package com.chen.coursearrangement.controller;


import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chen.coursearrangement.common.ConstantInfo;
import com.chen.coursearrangement.common.Result;
import com.chen.coursearrangement.entity.CourseInfo;
import com.chen.coursearrangement.entity.dto.CourseInfoDTO;
import com.chen.coursearrangement.entity.dto.UserDTO;
import com.chen.coursearrangement.service.ICourseInfoService;
import com.chen.coursearrangement.service.IUserService;
import com.chen.coursearrangement.utils.CourseExcelListener;
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
 * @since 2024-01-17
 */
@RestController
@RequestMapping("/courseInfo")
public class CourseInfoController {

    @Resource
    private ICourseInfoService courseInfoService;

    @Resource
    private IUserService userService;

    //新增或者更新
    @PostMapping
    public Result save(@RequestBody CourseInfo courseInfo) {
        UserDTO userInfo = userService.getUserRoleAndCollege(StpUtil.getLoginIdAsString());
        String role = userInfo.getRole();
        String college = userInfo.getCollege();
        // 系统管理员有权限操作所有课程，或者管理员有权限操作本学院课程
        if (ConstantInfo.ROLE_ADMINISTRATOR.equals(role) || (ConstantInfo.ROLE_ADMIN.equals(role) && college.equals(courseInfo.getCollegeNo()))) {
            courseInfoService.saveOrUpdate(courseInfo);
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
        CourseInfo courseInfo = courseInfoService.getById(id);
        if (courseInfo == null) {
            return Result.error(ConstantInfo.CODE_404, "课程不存在");
        }
        if (ConstantInfo.ROLE_ADMINISTRATOR.equals(role) || (ConstantInfo.ROLE_ADMIN.equals(role) && college.equals(courseInfo.getCollegeNo()))) {
            // 系统管理员有权限操作所有课程，或者管理员有权限操作本学院课程
            courseInfoService.removeById(id);
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
            // 系统管理员有权限操作所有课程，管理员有权限操作本学院课程
            courseInfoService.removeByIds(ids);
            return Result.success();
        }else if(ConstantInfo.ROLE_ADMIN.equals(role)){
            for (int id:ids) {
                CourseInfo courseInfo = courseInfoService.getById(id);
                if(courseInfo!=null&&college.equals(courseInfo.getCollegeNo())){
                    courseInfoService.removeById(id);
                }
            }
            return Result.success();
        } else {
            // 其他角色无权限
            return Result.error(ConstantInfo.CODE_401, "权限不足");
        }
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String college,
                           @RequestParam(defaultValue = "") String courseAttribute,
                           @RequestParam(defaultValue = "") String courseName) {
        UserDTO userInfo = userService.getUserRoleAndCollege(StpUtil.getLoginIdAsString());
        String role = userInfo.getRole();
        QueryWrapper<CourseInfo> queryWrapper = new QueryWrapper<>();
        // 对管理员进行权限控制
        if (ConstantInfo.ROLE_ADMIN.equals(role)) {
            college = userInfo.getCollege();
        }
        // 精确查询学院
        if (!StringUtils.isEmpty(college)) {
            queryWrapper.eq("college_no", college);
        }
        // 精确查询课程属性
        if (!StringUtils.isEmpty(courseAttribute)) {
            queryWrapper.eq("course_attribute", courseAttribute);
        }
        // 模糊查询课程名称
        if (!StringUtils.isEmpty(courseName)) {
            queryWrapper.like("course_name", courseName);
        }

        queryWrapper.orderByAsc("id");

        // 分页查询并返回结果
        return Result.success(courseInfoService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    //查询课程和教师信息，为课程计划的数据输入提供便利
    @GetMapping("/course")
    public Result findCourse() {
        return Result.success(courseInfoService.findCourse());
    }

    //生成课程编号
    @PostMapping("/generateCourseNo")
    public Result generateCourseNo(@RequestBody Map<String, Object> requestData) {
        String college = String.valueOf(requestData.get("college"));
        String courseAttribute = String.valueOf(requestData.get("courseAttribute"));
        String courseNoPrefix = college + courseAttribute;
        //通过统计同一学院，同一课程属于的课程数目，得到课程编号后三位
        int lastThreeDigits = courseInfoService.lastThreeDigits(college, courseAttribute);
        String courseNo = courseNoPrefix + String.format("%03d", lastThreeDigits + 1);
        return Result.success("课程编号生成成功！",courseNo);
    }

    //数据导出
    @GetMapping("/export")
    public void export(HttpServletResponse response,
                       @RequestParam(defaultValue = "") String college,
                         @RequestParam(defaultValue = "") String courseAttribute,
                         @RequestParam(defaultValue = "") String courseName) throws IOException {
        UserDTO userInfo = userService.getUserRoleAndCollege(StpUtil.getLoginIdAsString());
        String role = userInfo.getRole();
        // 对管理员进行权限控制
        if (ConstantInfo.ROLE_ADMIN.equals(role)) {
            // 如果是 ROLE_ADMIN，使用当前用户的学院信息
            college = userInfo.getCollege();
        }
    try {
        // 从数据库查询出所有的数据
        List<CourseInfo> list = courseInfoService.export(college, courseAttribute, courseName);

        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");

        // 生成导出文件名
        String fileName = generateExportFileName("课程信息");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        response.setHeader("Access-Control-Expose-Headers","Content-Disposition");
            // 使用 EasyExcel 导出数据
            EasyExcel.write(response.getOutputStream(), CourseInfoDTO.class).sheet("课程信息").doWrite(list);

        } catch (IOException e) {
            // 处理导出异常
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

    @RequestMapping("/import")
    @ResponseBody
    public Result importExcel(MultipartFile file) {
        // 参数验证
        if (file == null || file.isEmpty()) {
            return Result.error(ConstantInfo.CODE_400, "上传的文件不能为空");
        }

        try {
            CourseExcelListener listener = new CourseExcelListener(courseInfoService);
            EasyExcel.read(file.getInputStream(), CourseInfoDTO.class, listener).sheet().doRead();
            // 获取成功导入的数据条数
            int importedCount = listener.getImportedCount();
            return Result.success("导入成功，共导入 " + importedCount + " 条数据");
        } catch (Exception e) {
            // 处理导入异常
            return Result.error(ConstantInfo.CODE_500, "导入失败：" + e.getMessage());
        }
    }
}
