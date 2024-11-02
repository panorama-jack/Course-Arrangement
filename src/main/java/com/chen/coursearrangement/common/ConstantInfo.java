package com.chen.coursearrangement.common;


public interface ConstantInfo {

    //系统相关
    String CODE_200 = "200";  //成功
    String CODE_400 = "400";  // 参数错误
    String CODE_401 = "401";  // 权限不足
    String CODE_402 = "402";  // 还未登录
    String CODE_404 = "404";  // 相关信息不存在
    String CODE_500 = "500";  // 系统错误
    String CODE_600 = "600";  // 其他业务异常


    String DICT_TYPE_ICON = "icon";


    String ROLE_ADMINISTRATOR = "ROLE_ADMINISTRATOR";
    String ROLE_ADMIN = "ROLE_ADMIN";
    String ROLE_TEACHER = "ROLE_TEACHER";
    String ROLE_STUDENT = "ROLE_STUDENT";



     //遗传算法相关
     // 是否固定上课时间1位
     String IS_FIX = "isFix";

     // 年级编号2位
     String GRADE_NO = "grade_no";

     // 班级编号8位
     String CLASS_NO = "class_no";

     // 教师编号5位
     String TEACHER_NO = "teacher_no";

     // 课程编号6位
     String COURSE_NO = "course_no";

     // 课程属性1位
     String COURSE_ATTR = "courseAttr";

     // 上课教室类型2位
     String CLASSROOM_TYPE = "classroomType";

     // 教室编号5位
     String CLASSROOM_NO = "classroom_no";

     // 上课时间2位
     String CLASS_TIME = "class_time";

     // 开课学期
     String TERM = "term";

     // 默认课程的编码
     String DEFAULT_COURSE_TIME = "00";
    
     // 体育课
     String PHYSICAL_COURSE = "4";
    
     // 设置遗传代数
     int GENERATION = 80;

    // 种群大小
    int POPULATION_SIZE = 300;

    // 定义交叉概率常量
    double CROSSOVER_RATE = 0.9; // 假设交叉概率为0.9

    // 定义变异概率常量
    double MUTATION_RATE = 0.01; // 假设变异概率为0.01
    
}
