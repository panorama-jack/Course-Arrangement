package com.chen.coursearrangement.utils;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.chen.coursearrangement.entity.dto.StudentDTO;
import com.chen.coursearrangement.service.IStudentService;

import java.util.ArrayList;
import java.util.List;

// 有个很重要的点 ExcelListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
public class StudentExcelListener extends AnalysisEventListener<StudentDTO> {

    private int importedCount = 0;

    private List<StudentDTO> list = new ArrayList<>();
    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 100;

    /**
     * 假设这个是一个DAO，当然有业务逻辑这个也可以是一个service。当然如果不用存储这个对象没用。
     */
    private IStudentService studentService;

    /**
     * 如果使用了spring,请使用这个构造方法。每次创建Listener的时候需要把spring管理的类传进来
     */
    public StudentExcelListener(IStudentService studentService) {

        this.studentService = studentService;
    }


    /**
     * 这个每一条数据解析都会来调用
     */
    @Override
    public void invoke(StudentDTO student, AnalysisContext analysisContext) {
        System.out.println("解析到一条数据:========================" + student.toString());
        // 数据存储到datas，供批量处理，或后续自己业务逻辑处理。

        list.add(student);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (list.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理datas
            list.clear();
        }

    }

    /**
     * 所有数据解析完成了 都会来调用
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        saveData();//确保所有数据都能入库
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        System.out.println("==============================" + list.size() + "条数据，开始存储到数据库");
        importedCount=studentService.imp(list);
    }

    public int getImportedCount() {
        return importedCount;
    }
}