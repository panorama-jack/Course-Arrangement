package com.chen.coursearrangement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.coursearrangement.common.ConstantInfo;
import com.chen.coursearrangement.entity.Classroom;
import com.chen.coursearrangement.entity.CourseInfo;
import com.chen.coursearrangement.entity.CoursePlan;
import com.chen.coursearrangement.entity.Schedule;
import com.chen.coursearrangement.entity.dto.EchartsDTO;
import com.chen.coursearrangement.mapper.*;
import com.chen.coursearrangement.service.ICoursePlanService;
import com.chen.coursearrangement.utils.CourseArrangementUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Jack Chen
 * @since 2024-01-19
 */
@Service
@Slf4j
public class CoursePlanServiceImpl extends ServiceImpl<CoursePlanMapper, CoursePlan> implements ICoursePlanService {
    @Resource
    private CoursePlanMapper coursePlanMapper;
    @Resource
    private ClassroomMapper classroomMapper;
    @Resource
    private ClassInfoMapper classInfoMapper;
    @Resource
    private ScheduleMapper scheduleMapper;
    @Resource
    private CourseInfoMapper courseInfoMapper;

    // 不固定上课时间 0
    private final String UNFIXED_TIME = "unFixedTime";
    // 固定上课时间  1
    private final String IS_FIX_TIME = "isFixedTime";


    @Override
    public List<EchartsDTO> queryCoursePlanProcess(String term, String gradeNo) {
        return coursePlanMapper.queryCoursePlanProcess(term,gradeNo);
    }


    @Transactional
    @Override
    public String courseScheduling(String term) {
        try {
            log.info("开始排课,时间：" + System.currentTimeMillis());
            long start = System.currentTimeMillis();
            // 1、根据学期获得开课任务
            QueryWrapper<CoursePlan> wrapper = new QueryWrapper<CoursePlan>().eq("term", term);
            List<CoursePlan> coursePlanList = coursePlanMapper.selectList(wrapper);
            // 判断是否有开课任务
            if (CollectionUtils.isEmpty(coursePlanList)) {
                return "排课失败，查询不到排课任务！";
            }
            // 2、将课程计划的各项信息编码成染色体，分为固定时间与不固定时间
            Map<String, List<String>> geneList = coding(coursePlanList);
            // 3、初始化种群
            Map<String, List<String>> initializePopulation = initializePopulation(geneList);
            // 4、执行进化操作
            Map<String, List<String>> evolutionPopulation = geneticEvolution(initializePopulation);
            // 5、选出适应度最高的个体
            List<String> bestIndividual = findBestIndividual(evolutionPopulation);
            //上面进行时间分配，下面分配教室
            // 6、分配教室并做教室冲突检测
            List<String> resultList = finalResult(bestIndividual);
            // 7、解码最终的染色体获取其中的基因信息
            List<Schedule> scheduleList = decoding(resultList);
            // 8、写入schedule上课计划表
            scheduleMapper.deleteScheduleByTerm(term);  // 先删除原来的排课信息
            for (Schedule schedule : scheduleList) {
                scheduleMapper.insertSchedule(schedule.getGradeNo(), schedule.getClassNo(), schedule.getCourseNo(),
                        schedule.getTeacherNo(), schedule.getClassroomNo(), schedule.getCourseTime(), term);
            }
            log.info("完成排课,耗时：" + (System.currentTimeMillis() - start));
            return "排课成功！耗时：" + (System.currentTimeMillis() - start) + "毫秒";
        } catch (Exception e) {
            log.error("the error message is:" + "   " + e.getMessage());
            e.printStackTrace();
            return "排课失败，出现异常!";
        }
    }

    //初始化种群
    private Map<String, List<String>> initializePopulation(Map<String, List<String>> geneList) {
        Map<String, List<String>> population = new HashMap<>();
        for (int i = 0; i < ConstantInfo.POPULATION_SIZE; i++) {
            // 产生一个个体
            List<String> individual = codingTime(geneList);  //随机产生时间
            //第一次消除冲突 ，为之后的进化作准备
            List<String> noConflictList = conflictResolution(individual);
            //将个体加入到种群中
            population.put("Individual" + i, noConflictList);
        }
        return population;
    }

       //轮盘赌方法选择个体
/*    private Map<String, List<String>> selection(Map<String, List<String>> population) {
        Map<String, Double> fitnessMap = new HashMap<>();
        double totalFitness = 0.0;
        double maxFitness =0.0;
        // 计算每个个体的适应度值并计算总适应度值
        for (String individualId : population.keySet()) {
            List<String> individual = population.get(individualId);
            double fitness = CourseArrangementUtil.calculateFitness(individual);
            fitnessMap.put(individualId, fitness);
            totalFitness += fitness;
        }
        // 这个过程中，适应度值高的个体对应的累加值会更快地达到随机数rand，从而被选中的概率更大。
        // 因为累加值是逐渐增加的，而适应度值高的个体对应的累加值增加得更快，所以它们被选中的概率更高。这就是轮盘赌法中适应度值高的个体被选中概率更大的原理。
        // 根据适应度值进行轮盘赌选择
        Map<String, List<String>> selectedPopulation = new HashMap<>();
        Random random = new Random();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            double rand = random.nextDouble() * totalFitness;
            double sum = 0.0;
            for (String individualId : fitnessMap.keySet()) {
                sum += fitnessMap.get(individualId);
                if (sum >= rand) {
                    selectedPopulation.put("Individual" + i, population.get(individualId));
                    break;
                }
            }
        }
        return selectedPopulation;
    }
    */

    //锦标赛法选择
    private Map<String, List<String>> tournamentSelection(Map<String, List<String>> population) {
        Map<String, List<String>> selectedPopulation = new HashMap<>();
        Random random = new Random();
        Map<String, Double> fitnessMap = new HashMap<>();

        // 计算每个个体的适应度值
        for (String individualId : population.keySet()) {
            List<String> individual = population.get(individualId);
            double fitness = CourseArrangementUtil.calculateFitness(individual);
            fitnessMap.put(individualId, fitness);
        }

        // 竞标赛的参数：锦标赛的个体数量和锦标赛的轮数
        int tournamentSize = 3; // 锦标赛的个体数量
        int numTournaments = ConstantInfo.POPULATION_SIZE; // 锦标赛的轮数

        // 进行多轮锦标赛选择
        for (int i = 0; i < numTournaments; i++) {
            // 选择本轮的竞标赛个体
            List<String> tournamentIndividuals = new ArrayList<>();
            for (int j = 0; j < tournamentSize; j++) {
                String randomIndividualId = (String) population.keySet().toArray()[random.nextInt(population.size())];
                tournamentIndividuals.add(randomIndividualId);
            }

            // 选出本轮的锦标赛胜者
            String selectedIndividualId = tournamentIndividuals.get(0);
            double maxFitness = fitnessMap.get(selectedIndividualId);
            for (int k = 1; k < tournamentSize; k++) {
                String individualId = tournamentIndividuals.get(k);
                double fitness = fitnessMap.get(individualId);
                if (fitness > maxFitness) {
                    maxFitness = fitness;
                    selectedIndividualId = individualId;
                }
            }

            // 将胜者添加到选中个体的集合中  由于键名不能重复，所以就会选择优良个体，淘汰不好个体  但并不是都是最好的被选中，而是部分中的最好着被选中
            selectedPopulation.put(selectedIndividualId, population.get(selectedIndividualId));
        }
        return selectedPopulation;
    }

    //交叉操作
    private Map<String, List<String>> crossover(Map<String, List<String>> population) {
        Random random = new Random();
        int populationSize = population.size();
        int numIndividualsToCross = populationSize / 2;  // 交叉操作的个体数量为种群数量的一半

        // 创建一个新的种群用于存储交叉后的个体
        Map<String, List<String>> crossedPopulation = new HashMap<>(population);

        // 遍历进行交叉操作的个体对
        for (int i = 0; i < numIndividualsToCross; i++) {
            // 使用交叉率判断是否进行交叉操作
            if (random.nextDouble() < ConstantInfo.CROSSOVER_RATE) {
                // 随机选择两个不同的个体进行交叉
                String[] individualIds = population.keySet().toArray(new String[0]);   // 种群中个体的键名列表
                // 随机选择两个个体
                String individualId1 = individualIds[random.nextInt(populationSize)];
                String individualId2 = individualIds[random.nextInt(populationSize)];
                List<String> parent1 = population.get(individualId1);
                List<String> parent2 = population.get(individualId2);

                // 采用单点交叉方式，交叉相同课程编码的上课时间，由于每个个体中相同课程的索引相同，所以直接交换染色体
                // 随机选择交叉的染色体索引
                int chromosomeIndex = random.nextInt(Math.min(parent1.size(), parent2.size()));

                // 创建两个新的个体列表用于存储交叉后的个体
                List<String> crossedParent1 = new ArrayList<>(parent1);
                List<String> crossedParent2 = new ArrayList<>(parent2);

                // 交换两个个体的染色体
                String temp = crossedParent1.get(chromosomeIndex);
                crossedParent1.set(chromosomeIndex, crossedParent2.get(chromosomeIndex));
                crossedParent2.set(chromosomeIndex, temp);
                //交叉操作也有可能将两个适应性高的个体替换为了适应性低的个体，所以，我们将交叉前后的个体都保存下来，在下次种群更新时再进行选择。
                // 将交叉后的个体添加到新种群中
                crossedPopulation.put("CrossedIndividual" + i * 2, crossedParent1);
                crossedPopulation.put("CrossedIndividual" + (i * 2 + 1), crossedParent2);
            }
        }
        return crossedPopulation;
    }

    //变异操作
    private Map<String, List<String>> mutate(Map<String, List<String>> population) {
        Random random = new Random();
        Map<String, List<String>> mutatedPopulation = new HashMap<>();

        // 计算需要变异的个体数量
        /*通常在遗传算法进行的早期要设置较高的变异概率，这样可以快速搜索到新的性状，后期为了收敛速度，设置较小的变异概率。  由于进化后期，种群数量下降，所以需要进行变异操作的个体就少了
        为了避免进入局部最优，在实际应用中通常需要降低高适应性个体的变异概率，提高低适应性个体的变异概率*/
        int numToMutate = (int) (population.size() * ConstantInfo.MUTATION_RATE);

        // 创建一个新的种群对象，用于存储变异后的个体
        for (Map.Entry<String, List<String>> entry : population.entrySet()) {
            String individualId = entry.getKey();
            List<String> individual = new ArrayList<>(entry.getValue()); // 复制原始个体的基因列表

            // 对个体进行变异操作
            for (int i = 0; i < numToMutate; i++) {
                int size = individual.size();
                int temp = random.nextInt(size);
                String gene = individual.get(temp);
                if (CourseArrangementUtil.cutGene(ConstantInfo.IS_FIX, gene).equals("0")) {  //对上课时间不固定的进行变异操作
                    // 随机给它一个上课时间
                    String newCourseTime = CourseArrangementUtil.randomTime();
                    // 变异操作
                    gene = gene.substring(0, 25) + newCourseTime;
                    // 替换原来的基因
                    individual.set(temp, gene);
                }
            }
            // 将个体添加到新的种群中，无论是否进行了变异操作
            mutatedPopulation.put(individualId, individual);
        }
        return mutatedPopulation;
    }


    //遗传进化
    private Map<String, List<String>> geneticEvolution(Map<String, List<String>> noConflictPopulation) {
        // 遗传代数
        int generation = ConstantInfo.GENERATION;
        for (int i=0;i<=generation;i++) {
            //选择    锦标赛选择法
            Map<String, List<String>>  selectPopulation =tournamentSelection(noConflictPopulation);
            //若选择出的种群大小为1直接退出
            if(selectPopulation.size()==1)
                break;
            //交叉
            Map<String, List<String>> crossover = crossover(selectPopulation);
            //变异
            Map<String, List<String>> mutate = mutate(crossover);
            // 遍历种群中的每一个个体，对其进行冲突解决
            for (Map.Entry<String, List<String>> entry : mutate.entrySet()) {
                String individualId = entry.getKey();
                List<String> individual = entry.getValue();
                // 解决冲突
                List<String> noConflictList = conflictResolution(individual);
                // 将无冲突的个体放回种群中
                mutate.put(individualId, noConflictList);
            }
            //赋值回去，进行循环操作
            noConflictPopulation=mutate;

            double totalFintness = 0;
            for (String individualId : noConflictPopulation.keySet()) {
                List<String> individual = noConflictPopulation.get(individualId);
                double fitness = CourseArrangementUtil.calculateFitness(individual);
                // 记录最大适应度的个体的基因列表
                totalFintness+=fitness;
            }
            double avgFitness=totalFintness/noConflictPopulation.size();
            System.out.println("第"+i+"轮进化"+"平均适应度"+avgFitness);
        }
        return noConflictPopulation;
    }

    //选出适应度最高的个体
    private List<String> findBestIndividual(Map<String, List<String>> population) {
        double maxFitness = 0;
        List<String> bestIndividual = null;
        if (population.size() == 1) {
            // 如果种群中只有一个个体，直接返回该个体
            for (List<String> individual : population.values()) {
                bestIndividual = new ArrayList<>(individual); // 复制基因列表
                break;
            }
        } else {
            // 遍历种群中的每一个个体，计算适应度并找出最大适应度的个体
            for (String individualId : population.keySet()) {
                List<String> individual = population.get(individualId);
                double fitness = CourseArrangementUtil.calculateFitness(individual);
                // 记录最大适应度的个体的基因列表

                if (fitness > maxFitness) {
                    maxFitness = fitness;
                    bestIndividual = new ArrayList<>(individual); // 复制基因列表

                }
            }

        }
        System.out.println("每轮进化的最佳适应度："+maxFitness);
        return bestIndividual;
    }


    /**
     * 编码规则:
     * 固定时间：1
     * 年级编号：2
     * 班级编号：8
     * 讲师编号：5
     * 课程编号：6
     * 课程属性：1     期望计算，进行优化
     * 教室类型：2     教室选择
     * 上面共25位
     * <p>
     * 上课时间：2
     * 教室编号：5
     * 共32位
     * 编码规则为：是否固定+年级编号+班级编号+教师编号+课程编号+课程属性+教室类型+上课时间，  教室之后才进行安排，初始不含教室编号
     * 其中如果不固定上课时间默认填充为"00"
     *
     * @param coursePlanList
     * @return
     * <p>
     * 根据课程计划生成染色体的编码过程，
     * 通过 coding方法，将课程计划信息编码为染色体，使用 Map<String, List<String>>作为返回值，其中键表示上课时间是否固定，值是对应的课程信息编码列表。
     * 编码规则：每个基因编码有32位。规则为：是否固定 + 年级编号 + 班级编号 + 讲师编号 + 课程编号 + 课程属性 +教室类型+上课时间。其中，如果不固定上课时间，默认填充为"00"。
     * 编码过程：对每门课程进行遍历，分别处理固定和不固定的上课时间方式。
     * 对于不固定时间，根据每周上课的节数生成对应数量的基因编码，使用默认的上课时间（"00"）。
     * 对于固定时间，根据每周上课的节数和设定的固定时间，生成对应数量的基因编码。
     * 课程信息获取：通过调用 courseInfoMapper 获取每门课程的讲师编号和课程属性。
     * 返回值：geneListMap包含两个键值对，分别对应不固定时间和固定时间的基因列表。
     */
    private Map<String, List<String>> coding(List<CoursePlan> coursePlanList) {
        Map<String, List<String>> geneListMap = new HashMap<>();
        // 不固定0
        List<String> unFixedTimeGeneList = new ArrayList<>();
        // 固定1
        List<String> fixedTimeGeneList = new ArrayList<>();

        for (CoursePlan coursePlan : coursePlanList) {
            // 从数据库中查询课程信息
            CourseInfo courseInfo = courseInfoMapper.selectOne(new QueryWrapper<CourseInfo>()
                    .select("teacher_no", "course_attribute", "classroom_type")
                    .eq("course_no", coursePlan.getCourseNo()));

            if (courseInfo != null) {
                String teacherNo = courseInfo.getTeacherNo();
                String courseAttribute = courseInfo.getCourseAttribute();
                String classroomType = courseInfo.getClassroomType();
                // 不固定上课时间
                if ("0".equals(coursePlan.getIsFix())) {
                    //得到每周上课的节数，设定2学时为一节课
                    int size = coursePlan.getWeekTime() / 2;
                    for (int i = 0; i < size; i++) {    //每周要上几节课就生成几个编码
                        // 编码:不固定时间的课程
                        String gene = "0" + coursePlan.getGradeNo() + coursePlan.getClassNo()
                                + teacherNo + coursePlan.getCourseNo() + courseAttribute + classroomType
                                + ConstantInfo.DEFAULT_COURSE_TIME;
                        unFixedTimeGeneList.add(gene);
                    }
                }

                // 固定上课时间
                if ("1".equals(coursePlan.getIsFix())) {
                    int size = coursePlan.getWeekTime() / 2;
                    for (int i = 0; i < size; i++) {
                        // 获得设定的固定时间、
                        //.substring(i * 2, (i + 1) * 2)：对上一步返回的字符串执行子串提取操作。i * 2 是起始索引，(i + 1) * 2 是结束索引（不包括结束索引的字符）。这样就提取了字符串中从索引 i * 2 到 (i + 1) * 2 - 1 的子串。
                        //这通常用于从表示时间的字符串中提取特定时间段的信息。例如，如果i为0，那么这将提取字符串的前两个字符，如果i为1，那么将提取字符串的第三和第四个字符，依此类推。这可以在循环中使用，逐步提取课程的不同时间段。
                        //即为：若一门课的周学时为2，只需输入一个时间，如：01，若周学时为4，则需要输入两个学时，如0506    以此类推
                        String courseTime = coursePlan.getCourseTime().substring(i * 2, (i + 1) * 2);
                        String gene = "1" + coursePlan.getGradeNo() + coursePlan.getClassNo()
                                + teacherNo + coursePlan.getCourseNo() + courseAttribute + classroomType
                                + courseTime;
                        fixedTimeGeneList.add(gene);
                    }
                }
            }
        }
        //将两种上课时间的列表放入map中
        geneListMap.put(UNFIXED_TIME, unFixedTimeGeneList);
        geneListMap.put(IS_FIX_TIME, fixedTimeGeneList);
        // 进行返回
        return geneListMap;
    }

    /**
     * 给初始基因编码随机分配时间(不固定上课时间的课程)
     * @param geneList 固定时间与不固定时间的编码集合
     * @return
     * 分离固定时间和不固定时间的基因列表：
     * 通过 geneList.get(IS_FIX_TIME) 获取固定时间的基因列表。
     * 通过 geneList.get(UNFIXED_TIME) 获取不固定时间的基因列表。
     * 遍历不固定时间的基因列表：
     * 对于每个不固定时间的基因编码，调用 CourseArrangementUtil.randomTime(gene, resultGeneList) 来分配一个随机的上课时间。
     * 将原基因编码的时间部分替换为随机分配的时间，得到分配好随机上课时间的基因编码。
     * 结果集合：将所有固定上课时间的基因编码集合isFixedTimeGeneList加入最终的结果集合 resultGeneList。将分配好上课时间的不固定时间的基因编码集合加入 resultGeneList。
     * 返回值：返回的是一个包含所有固定和不固定上课时间的基因编码的列表。
     *
     */
    private List<String> codingTime(Map<String, List<String>> geneList) {
        List<String> resultGeneList = new ArrayList<>();
        List<String> isFixedTimeGeneList = geneList.get(IS_FIX_TIME);
        List<String> unFixedTimeGeneList = geneList.get(UNFIXED_TIME);
        // 将固定上课时间的课程基因编码集合全部加入集合
        resultGeneList.addAll(isFixedTimeGeneList);
        // 没有固定时间的课程
        for (String gene : unFixedTimeGeneList) {
            // 获得一个随机时间
            String courseTime = CourseArrangementUtil.randomTime();
            // 得到分配好随机上课时间的基因编码
            gene = gene.substring(0, 25) + courseTime;
            // 分配好上课时间的编码集合
            resultGeneList.add(gene);
        }
        return resultGeneList;
    }


    /**
     * 开始给进化完的基因编码分配教室，即在原来的编码中加上教室编号5
     * @param individualList
     * @return 通过遍历不分班级的基因编码，为每个基因编码分配教室，最终得到完整的基因编码列表，形成最终的染色体。
     */
    private List<String> finalResult(List<String> individualList) {
        //存储已经分配教室的基因列表
        List<String> resultList = new ArrayList<>();

        //查询教室列表
        List<Classroom> classroomList = classroomMapper.selectList(null);

        for (String gene : individualList) {
            // 获得班级编号
            String classNo = CourseArrangementUtil.cutGene(ConstantInfo.CLASS_NO, gene);
            // 得到该班级的学生人数
            int studentNum = classInfoMapper.selectStuNum(classNo);
            // 分配教室
            String classroomNo = chooseClassroom(studentNum, gene, classroomList, resultList);
            // 基因编码中加入教室编码，至此所有基因信息编码完成，得到染色体
            gene = gene + classroomNo;
            // 将最终的编码加入集合中
            resultList.add(gene);
        }
        // 完整的基因编码，即分配有教室的
        return resultList;
    }


    /**
     * 给不同课程的基因编码随机选择一个教室
     *
     * @param studentNum    开课的班级的学生人数
     * @param gene          需要安排教室的基因编码
     * @param classroomList 教室列表
     * @param resultList
     * @return
     * 随机选择教室：使用随机数从给定的 classroomList中选择一个教室。
     * 判断是否满足条件：调用 judgeClassroom 方法判断选择的教室是否满足条件。
     * 如果所选教室满足条件，则返回该教室的编号；否则，递归调用自身重新选择教室，直到找到符合条件的教室。
     * 这个方法的核心思想是通过随机选择教室，并在每次选择后判断是否满足条件。如果不满足条件，就继续随机选择，直到找到符合条件的教室。
     */
    private String chooseClassroom(int studentNum, String gene, List<Classroom> classroomList, List<String> resultList) {
        Random random = new Random();
        // 用于随机选取教室
        int temp = random.nextInt(classroomList.size());
        // 随机教室
        Classroom classroom = classroomList.get(temp);
        // 判断是否满足条件
        if (judgeClassroom(studentNum, gene, classroom, resultList)) {
            // 该教室满足条件
            return classroom.getClassroomNo();
        } else {
            // 不满足，继续找教室    递归查找
            return chooseClassroom(studentNum, gene, classroomList, resultList);
        }
    }

    private Boolean judgeClassroom(int studentNum, String gene, Classroom classroom, List<String> resultList) {
        // 通过判断教室的类型
        if (CourseArrangementUtil.cutGene(ConstantInfo.CLASSROOM_TYPE, gene).equals(classroom.getClassroomType())) {
            // 判断人数
            if (classroom.getCapacity() >= studentNum) {
                // 判断该教室上课时间是否重复
                return isFree(gene, resultList, classroom);
            } else {
                return false;
            }
        } else {
            //类型不匹配
            return false;
        }
    }

    /**
     * 判断同一时间同一个教室是否有多个班级使用
     *
     * @param gene
     * @param resultList
     * @param classroom
     * @return 是空闲true 不空闲false
     * 判断同一时间是否有多个班级在同一个教室上课。
     * 如果 resultList 为空，说明还没有教室被分配，直接返回 true，表示教室是空闲的。
     * 如果 resultList 不为空，遍历已分配的基因编码（染色体），判断是否有其他班级在同一时间使用了相同的教室。
     * 对于已分配的基因编码，提取其中的教室编号和上课时间，与当前基因编码中的教室编号和上课时间进行比较。
     * 如果找到相同的教室编号，并且上课时间也相同，说明有冲突，返回 false。
     * 如果没有找到相同的教室编号或者找到了相同的教室编号但上课时间不相同，继续检查下一个已分配的基因编码。
     * 如果遍历完所有已分配的基因编码，都没有发现冲突，返回 true，表示教室是空闲的。
     * 确保同一时间同一个教室只被一个班级使用，避免教室时间冲突。
     */
    private Boolean isFree(String gene, List<String> resultList, Classroom classroom) {
        // 如果resultList为空说明还没有教室被分配,直接返回true
        if (resultList.size() == 0) {
            return true;
        } else {
            for (String resultGene : resultList) {
                // 如果当前教室在之前分配了，判断时间是否有冲突
                if (CourseArrangementUtil.cutGene(ConstantInfo.CLASSROOM_NO, resultGene).equals(classroom.getClassroomNo())) {
                    // 判断时间是否一样，一样则表示有冲突
                    if (CourseArrangementUtil.cutGene(ConstantInfo.CLASS_TIME, gene).equals(CourseArrangementUtil.cutGene(ConstantInfo.CLASS_TIME, resultGene))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * 冲突消除,若同一个班同一时间上多门课或同一个讲师同一时间上多门课
     * 解决：重新分配一个时间，直到所有的基因编码中不再存在上课时间冲突为止
     * 因素：讲师-课程-时间-教室
     *
     * @param resultGeneList 所有个体集合
     * @return
     * 使用两层循环遍历所有基因编码：
     * 外层循环（i）遍历所有基因编码，获取基因编码的讲师编号、上课时间、班级编号。
     * 内层循环（j）从外层循环的下一个位置开始，遍历剩余的基因编码，获取当前循环基因编码的讲师编号、上课时间、班级编号。
     * 在内层循环中进行冲突检测：如果两个基因编码的上课时间相同，并且班级编号相同或讲师编号相同，表示发生了冲突。
     * 如果发生了冲突：
     * 输出提示信息，使用 CourseArrangementUtil.randomTime方法生成一个新的上课时间。
     * 循环结束后，输出冲突发生的次数。
     * 通过修改课程计划的上课时间，解决了同一个班同一时间上多门课或同一个讲师同一时间上多门课的问题，直到没有冲突为止。
     */
    private List<String> conflictResolution(List<String> resultGeneList) {
        int conflictTimes = 0;
        List<String> newResultGeneList = new ArrayList<>(resultGeneList); // 创建一个新的列表来存储替换后的基因序列
        //标签（label），用于在循环中实现跳转。
        eitx:
        for (int i = 0; i < newResultGeneList.size(); i++) {
            //得到集合中每一条基因编码
            String gene = newResultGeneList.get(i);
            String isFix = CourseArrangementUtil.cutGene(ConstantInfo.IS_FIX, gene);
            String teacherNo = CourseArrangementUtil.cutGene(ConstantInfo.TEACHER_NO, gene);
            String courseTime = CourseArrangementUtil.cutGene(ConstantInfo.CLASS_TIME, gene);
            String classNo = CourseArrangementUtil.cutGene(ConstantInfo.CLASS_NO, gene);

            for (int j = i + 1; j < newResultGeneList.size(); j++) {
                // 再找剩余的基因编码对比
                String tempGene = newResultGeneList.get(j);
                String tempIsFix = CourseArrangementUtil.cutGene(ConstantInfo.IS_FIX, tempGene);
                String tempTeacherNo = CourseArrangementUtil.cutGene(ConstantInfo.TEACHER_NO, tempGene);
                String tempCourseTime = CourseArrangementUtil.cutGene(ConstantInfo.CLASS_TIME, tempGene);
                String tempClassNo = CourseArrangementUtil.cutGene(ConstantInfo.CLASS_NO, tempGene);

                if (courseTime.equals(tempCourseTime)) {  //当上课时间相同                          //即同一时刻，一个班只能上一门课，一个老师，只能给一个班上课
                    if (classNo.equals(tempClassNo) || teacherNo.equals(tempTeacherNo)) {     //出现相同班级或者教师，则产生冲突
                        //System.out.println("出现冲突情况");
                        conflictTimes++;
                        String newCourseTime = CourseArrangementUtil.randomTime();

                        String newGene;

                        if(tempIsFix.equals("1")){
                            newGene = gene.substring(0, 25) + newCourseTime;       //若j对应的编码上课时间固定或者i，j的上课时间都固定  修改i对应的编码
                            newResultGeneList.set(i, newGene);    // 替换原基因序列中的基因
                        }else{
                            newGene = tempGene.substring(0, 25) + newCourseTime;   //若i对应的上课时间固定或者二者时间都不固定  修改j对应的编码
                            newResultGeneList.set(j, newGene);    // 替换原基因序列中的基因
                        }
                        i=-1;
                        continue eitx;
                    }
                }
            }
        }

        //System.out.println("冲突发生次数:" + conflictTimes);
        return newResultGeneList;
    }


    /**
     * 解码染色体中的基因，按照之前的编码解码
     * @param resultList 全部上课计划实体
     * @return
     * 对基因编码的解码操作，将编码还原成上课计划实体（Schedule）的列表。
     * 解码的规则如下：
     * 是否固定（1位）
     * 年级编号（2位）
     * 班级编号（8位）
     * 讲师编号（5位）
     * 课程编号（6位）
     * 课程属性（1位）
     * 教室类型（2位）
     * 上课时间（2位）
     * 教室编号（5位）
     * 共32位
     * 编码规则为：是否固定+年级编号+班级编号+教师编号+课程编号+课程属性+教室类型+上课时间+教室编号(遗传算法执行完最后再分配教室,并判断是否存在冲突)
     * 使用 CourseArrangementUtil.cutGene方法根据编码规则提取各个字段的值。
     * 创建 Schedule 对象，并设置年级、班级、课程、讲师、教室、上课时间等属性。
     * 将每个解码后的 Schedule 对象添加到 scheduleList 中。
     * 返回解码后的 scheduleList。
     * 这段代码的作用是将基因编码还原为可读的上课计划实体，方便后续操作和展示。
     */
    private List<Schedule> decoding(List<String> resultList) {    //解码操作
        List<Schedule> scheduleList = new ArrayList<>();
        for (String gene : resultList) {
            Schedule schedule = new Schedule();
            // 年级
            schedule.setGradeNo(CourseArrangementUtil.cutGene(ConstantInfo.GRADE_NO, gene));
            // 班级
            schedule.setClassNo(CourseArrangementUtil.cutGene(ConstantInfo.CLASS_NO, gene));
            // 课程
            schedule.setCourseNo(CourseArrangementUtil.cutGene(ConstantInfo.COURSE_NO, gene));
            // 讲师
            schedule.setTeacherNo(CourseArrangementUtil.cutGene(ConstantInfo.TEACHER_NO, gene));
            // 教室
            schedule.setClassroomNo(CourseArrangementUtil.cutGene(ConstantInfo.CLASSROOM_NO, gene));
            // 上课时间
            schedule.setCourseTime(CourseArrangementUtil.cutGene(ConstantInfo.CLASS_TIME, gene));
            scheduleList.add(schedule);
        }
        return scheduleList;
    }
}

