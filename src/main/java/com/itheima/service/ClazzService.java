package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.pojo.Clazz;
import com.itheima.pojo.PageResult;

import java.time.LocalDate;

public interface ClazzService extends IService<Clazz> {

    /**
     * 分页条件查询
     * @param name 班级名称
     * @param begin 结课时间开始
     * @param end   结课时间结束
     * @param page  当前页码
     * @param pageSize  每页显示记录数
     * @return  结果
     */
    PageResult<Clazz> getPageResult(String name, LocalDate begin, LocalDate end, int page, int pageSize);
}
