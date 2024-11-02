package com.chen.coursearrangement.utils;

import com.chen.coursearrangement.common.ConstantInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;

@Slf4j
public class CourseArrangementUtil {
    /**
     * 用于切割获得编码的染色体中需要的属性
     *
     * @param aim
     * @param source
     * @return
     * 接受两个参数：aim（目标属性）和 source（源字符串）。
     * 通过 switch 语句根据目标属性（aim）的值选择不同的情况。
     * 每个情况对应一种编码属性，通过 source 的 substring 方法截取相应位置的子串。
     * 返回截取得到的子串，表示目标属性对应的值。
     * 如果 aim 的值不匹配任何情况，返回空字符串 ""。
     * 根据给定的目标属性（如年级编号、班级编号等），从源字符串中提取相应的子串。在上下文中，这被用于从编码中提取特定信息，以便在其他地方使用。
     */
    public static String cutGene(String aim, String source) {
        switch (aim) {
            case ConstantInfo.IS_FIX:
                return source.substring(0, 1);  //固定时间 1
            case ConstantInfo.GRADE_NO:
                return source.substring(1, 3);  //年级编号 2
            case ConstantInfo.CLASS_NO:
                return source.substring(3, 11);  //班级编号 8
            case ConstantInfo.TEACHER_NO:
                return source.substring(11, 16);  //讲师编号 5
            case ConstantInfo.COURSE_NO:
                return source.substring(16, 22);  //课程编号 6
            case ConstantInfo.COURSE_ATTR:
                return source.substring(22, 23);  //课程属性 1
            case ConstantInfo.CLASSROOM_TYPE:
                return source.substring(23, 25);  //教室类型 2
            case ConstantInfo.CLASS_TIME:
                return source.substring(25, 27);  //上课时间 2
            case ConstantInfo.CLASSROOM_NO:
                return source.substring(27, 32);  // 教室编号 5
            default:
                return "";
        }
    }

    /**
     * 随机生成上课时间的静态方法
     * @return
     */
    public static String randomTime() {
        //课程上课范围，1表示星期一第一节课，按照每天的课进行编号
        int min = 1;
        int max = 25;

        Random random = new Random();
        int randomTime = random.nextInt(max - min + 1) + min;
        // 格式化生成的时间为两位数字（例如，如果小于10，则添加前导零）
        String time = String.format("%02d", randomTime);
        return time;
    }


    /**
     * 计算主要课程的期望值
     * @param classTime 课程的上课时间片
     * @return 主要课程的期望值
     * <p>
     * 方法接受一个参数 classTime，表示课程的上课时间片。
     * 通过判断 classTime是否在不同期望值的时间片数组中，来确定主要课程的期望值。
     * 如果 classTime 在特定的时间片数组中，则返回相应的期望值。
     */

    //安排优先级：第一节（早上第一节）>第二节>第三节（下午第一节）>第四节>第五节（晚上第一节）
    private static double judgingMainExpect(String classTime) {
        // 第一节课（早上第一节）
        String[] firstExpectRange = {"01", "06", "11", "16", "21"};
        // 第二节课
        String[] secondExpectRange = {"02", "07", "12", "17", "22"};
        // 第三节课（下午第一节）
        String[] thirdExpectRange = {"03", "08", "13", "18", "23"};
        // 第四节课
        String[] fourthExpectRange = {"04", "09", "14", "19", "24"};
        // 第五节课（晚上第一节）
        // String[] fifthExpectRange = {"05", "10", "15", "20", "25"};

        if (ArrayUtils.contains(firstExpectRange, classTime)) {
            return 1.0;
        } else if (ArrayUtils.contains(secondExpectRange, classTime)) {
            return 0.8;
        } else if (ArrayUtils.contains(thirdExpectRange, classTime)) {
            return 0.6;
        } else if (ArrayUtils.contains(fourthExpectRange, classTime)) {
            return 0.4;
        } else {
            return 0.2;
        }
    }


    /**
     * 计算体育课的期望值
     *
     * @param classTime 体育课的上课时间片
     * @return 体育课的期望值
     * <p>
     * 方法接受一个参数 classTime，表示体育课的上课时间片。
     * 通过判断 classTime是否在不同期望值的时间片数组中，来确定体育课的期望值。
     * 如果 classTime在特定的时间片数组中，则返回相应的期望值。
     */
    //体育课   晚上不排课   周五下午不排课
    //排课优先级：第四节>第二节>第三节
    private static double judgingPhysicalExpect(String classTime) {
        String[] highExpectRange = {"04", "09", "14", "19"};
        String[] mediumExpectRange = {"02", "07", "12", "17", "22"};
        String[] lowExpectRange = {"03", "08", "13", "18"};
        // String[] zeroExpectRange = {"01", "05", "06", "10", "11", "15", "16", "20", "21", "23", "24", "25"};
        if (ArrayUtils.contains(highExpectRange, classTime)) {
            return 1;
        } else if (ArrayUtils.contains(mediumExpectRange, classTime)) {
            return 0.8;
        } else if (ArrayUtils.contains(lowExpectRange, classTime)) {
            return 0.4;
        } else {
            return 0;
        }
    }


    /**
     * 判断同一课程多次上课时间差在哪个区间，并返回对应的期望值
     * @param timeDifference 两次课的时间差
     * @return 对应的期望值
     */
    private static double judgingDiscreteValuesExpect(int timeDifference) {
        int[] highExpectRange = {8, 9, 10};
        int[] mediumExpectRange = {6, 7, 11, 12, 13, 14};
        int[] lowExpectRange = {5, 15, 16, 17, 18};
        int[] minimalExpectRange = {3, 4, 19, 20, 21, 22};
        // int[] zeroExpectRange = {1, 2, 23, 24, 25};
        if (ArrayUtils.contains(highExpectRange, timeDifference)) {
            return 1;
        } else if (ArrayUtils.contains(mediumExpectRange, timeDifference)) {
            return 0.8;
        } else if (ArrayUtils.contains(lowExpectRange, timeDifference)) {
            return 0.6;
        } else if (ArrayUtils.contains(minimalExpectRange, timeDifference)) {
            return 0.4;
        } else {
            return 0.2;
        }
    }


    /**
     * 计算一个班同一门课程多次上课时间的期望值
     * @param individualList
     * @return
     */
    private static double calculateDiscreteExpect(List<String> individualList) {
        double discreteCourse=0;       //离散程度期望值
        int discreteCourseCount=0;     //多次上课的课程数
        Map<String, Map<String, List<String>>> classCourseTimeMap = new HashMap<>();

        // 遍历每个班级的课程信息
        for (String gene : individualList) {
            String classNo = CourseArrangementUtil.cutGene(ConstantInfo.CLASS_NO, gene);
            String courseNo = CourseArrangementUtil.cutGene(ConstantInfo.COURSE_NO, gene);
            String classTime = CourseArrangementUtil.cutGene(ConstantInfo.CLASS_TIME, gene);

            // 如果班级已存在于映射中，则获取其对应的课程时间映射，否则创建新的课程时间映射
            Map<String, List<String>> courseTimeMap = classCourseTimeMap.computeIfAbsent(classNo, k -> new HashMap<>());

            // 如果课程已存在于课程时间映射中，则获取其对应的上课时间列表，否则创建新的上课时间列表
            List<String> timeList = courseTimeMap.computeIfAbsent(courseNo, k -> new ArrayList<>());

            // 将上课时间加入到课程时间列表中
            timeList.add(classTime);
        }

        // 对每个班级的课程时间列表进行排序
        for (Map<String, List<String>> courseTimeMap : classCourseTimeMap.values()) {
            for (List<String> timeList : courseTimeMap.values()) {
                Collections.sort(timeList);
                if (timeList.size() > 1) {
                    for (int i = 0; i < timeList.size() - 1; ++i) {
                        //计算同一门课程上课的时间差
                        int temp = Integer.parseInt(timeList.get(++i)) - Integer.parseInt(timeList.get(i - 1));
                        //计算多门课程期望值
                        discreteCourse = discreteCourse + judgingDiscreteValuesExpect(temp);
                        //计数
                        discreteCourseCount++;
                    }
                }
            }
        }
        //返回平均值
        return discreteCourse/discreteCourseCount;
    }

    /**
     * 计算一个班的课程分布期望值
     * @param individualList
     * @return
     */
    private static double calculateAverageDistributionExpect(List<String> individualList) {
        Map<String, List<String>> individualMap = new HashMap<>();

        // 将基因编码按班级分组
        for (String gene : individualList) {
            String classNo = CourseArrangementUtil.cutGene(ConstantInfo.CLASS_NO, gene);
            individualMap.computeIfAbsent(classNo, k -> new ArrayList<>()).add(gene);
        }

        double totalFitness = 0;
        int numClasses = individualMap.size(); // 班级数量

        // 计算每个班级的期望值并累加
        for (List<String> geneList : individualMap.values()) {
            int totalCourses = geneList.size(); // 班级内总课程数量
            int[] courseCountPerTimeSlot = new int[5]; // 每个时间段上的课程数量

            // 统计每个时间段上的课程数量
            for (String gene : geneList) {
                String classTime = CourseArrangementUtil.cutGene(ConstantInfo.CLASS_TIME, gene);
                int timeSlot = Integer.parseInt(classTime);
                int day = (timeSlot - 1) / 5;
                courseCountPerTimeSlot[day]++;
            }
            // 计算平均课程数量
            int averageCourseCount = totalCourses / 5;

            // 计算课程数量的标准差
            double sumSquaredDeviations = 0;
            for (int count : courseCountPerTimeSlot) {
                double deviation = count - averageCourseCount;
                sumSquaredDeviations += deviation * deviation;
            }
            double standardDeviation = Math.sqrt(sumSquaredDeviations / 5);

            // 计算适应度并累加
            double fitness = 1 / (1 + standardDeviation);
            totalFitness += fitness;
        }
        // 返回平均适应度
        return totalFitness / numClasses;
    }

    /**
     * 计算种群中个体的适应度
     * @param individualList
     * @return
     */
    public static double calculateFitness(List<String> individualList) {
        double K1 = 0.4;   //节次优化所占权重
        double K2 = 0.2;   //特殊课程所占权重
        double K3 = 0.15;  //同一课程上课时间离散程度所占权重
        double K4 = 0.15;  //不同课程上课时间离散程度所占权重

        double mainCourse=0;
        double specialCourse=0;
        int specialCourseCount=0;
        double F; // 总适应度值

        //计算每一个个体的适应度
        for (String gene : individualList) {
            // 获得课程属性
            String courseAttr = cutGene(ConstantInfo.COURSE_ATTR, gene);
            // 获得该课程的开课时间
            String classTime = cutGene(ConstantInfo.CLASS_TIME, gene);
            // 根据课程属性设置期望值
            if (courseAttr.equals(ConstantInfo.PHYSICAL_COURSE)) {
                specialCourse = specialCourse  + judgingPhysicalExpect(classTime);
                specialCourseCount++;
            } else {
                mainCourse = mainCourse + judgingMainExpect(classTime);
            }
        }

        int size=individualList.size();    //课程总数
        double F1 = mainCourse/(size-specialCourseCount); // 主要课程节次优化期望总值
        double F2 = specialCourse/specialCourseCount;      //特殊课程节次优化期望总值
        double F3 = calculateDiscreteExpect(individualList);  //同一班级相同课程离散程度期望总值
        double F4= calculateAverageDistributionExpect(individualList);  //同一班级不同课程离散程度期望总值
        // 总适应度
        F = K1 * F1 + K2 * F2 + K3 * F3 +K4 * F4;
        return F; // 个体的适应度值
    }
}
