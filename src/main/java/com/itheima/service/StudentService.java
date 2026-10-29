package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.pojo.PageResult;
import com.itheima.pojo.Student;
import com.itheima.pojo.StudentQueryParam;

public interface StudentService extends IService<Student> {

    /**
     * 分页查询学生信息
     * @param param 查询参数
     * @return  查询到的学生信息
     */
    PageResult<Student> getPageResult(StudentQueryParam param);

    /**
     * 扣分
     * @param id 学生id
     * @param score 扣分
     */
    void violation(Integer id, Integer score);
}
