package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.pojo.Emp;
import com.itheima.pojo.EmpQueryParam;
import com.itheima.pojo.PageResult;

import java.util.List;

public interface EmpService extends IService<Emp> {

    /**
     * 分页查询员工信息
     * @param param 查询参数
     * @return  分页查询结果
     */
    PageResult<Emp> getPageResult(EmpQueryParam param);

    /**
     * 新增员工信息
     * @param emp 员工信息
     */
    void saveEmp(Emp emp);

    /**
     * 根据id查询员工信息
     * @param id 员工id
     * @return 员工信息
     */
    Emp getEmpById(Integer id);

    /**
     * 修改员工信息
     * @param emp 员工信息
     */
    void updateEmp(Emp emp);

    /**
     * 根据id删除员工和员工工作经历信息
     * @param ids 员工id
     */
    void deleteEmpsByIds(List<Integer> ids);
}
