package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.mapper.EmpMapper;
import com.itheima.pojo.*;
import com.itheima.service.DeptService;
import com.itheima.service.EmpExprService;
import com.itheima.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmpServiceImpl extends ServiceImpl<EmpMapper, Emp> implements EmpService {
    @Autowired
    private DeptService deptService;

    @Autowired
    private EmpExprService exprService;

    /**
     * 分页查询员工信息
     *
     * @param param 查询参数
     * @return 分页查询结果
     */
    @Override
    public PageResult<Emp> getPageResult(EmpQueryParam param) {
        Page<Emp> page = new Page<>(param.getPage(), param.getPageSize());
        // 根据最近修改时间降序排序
        page.addOrder(OrderItem.desc("update_time"));

        /*LambdaQueryWrapper<Emp> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(param.getName() != null && !param.getName().isEmpty(), Emp::getName, param.getName())
                .eq(param.getGender() != null, Emp::getGender, param.getGender())
                .between(param.getBegin() != null && param.getEnd() != null, Emp::getEntryDate, param.getBegin(), param.getEnd());

        page = page(page, queryWrapper);*/

        page = lambdaQuery().like(param.getName() != null && !param.getName().isEmpty(), Emp::getName, param.getName())
                .eq(param.getGender() != null, Emp::getGender, param.getGender())
                .between(param.getBegin() != null && param.getEnd() != null, Emp::getEntryDate, param.getBegin(), param.getEnd())
                .page(page);

        List<Emp> emps = page.getRecords();

        // 遍历员工集合，获取到员工的部门id集合
        List<Integer> deptIds = emps.stream().map(emp -> emp.getDeptId()).collect(Collectors.toList());

        if (!deptIds.isEmpty()) {
            // 根据部门id集合查询部门信息
            List<Dept> depts = deptService.listByIds(deptIds);
            // 遍历员工集合，查找员工对应的部门名称
            emps.forEach(emp -> {
                depts.forEach(dept -> {
                    if (emp.getDeptId() != null && emp.getDeptId().equals(dept.getId())) {
                        emp.setDeptName(dept.getName());
                    }
                });
            });
        }

        return new PageResult<>(page.getTotal(), emps);
    }

    /**
     * 新增员工信息
     *
     * @param emp 员工信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveEmp(Emp emp) {
        // 补全基础属性
        emp.setCreateTime(LocalDateTime.now());
        emp.setUpdateTime(LocalDateTime.now());

        // 保存员工基本信息
        save(emp);

        // 获取到员工的工作经历集合
        List<EmpExpr> exprList = emp.getExprList();

        if (exprList != null && !exprList.isEmpty()) {
            exprList.forEach(expr -> {
                expr.setEmpId(emp.getId());
            });
            // 批量保存员工工作经历信息
            exprService.saveBatch(exprList);
        }
    }

    /**
     * 根据id查询员工信息
     *
     * @param id 员工id
     * @return 员工信息
     */
    @Override
    public Emp getEmpById(Integer id) {
        Emp emp = getById(id);

        // 根据员工id查询员工工作经历信息
        emp.setExprList(exprService.list(new LambdaQueryWrapper<EmpExpr>().eq(EmpExpr::getEmpId, id)));
        return emp;
    }

    /**
     * 修改员工信息
     *
     * @param emp 员工信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEmp(Emp emp) {
        // 保存员工基本信息
        emp.setUpdateTime(LocalDateTime.now());
        updateById(emp);

        // 删除员工工作经历信息
        exprService.remove(new LambdaQueryWrapper<EmpExpr>().eq(EmpExpr::getEmpId, emp.getId()));

        // 批量保存新的工作经历信息
        List<EmpExpr> exprList = emp.getExprList();

        if (exprList != null && !exprList.isEmpty()) {
            exprList.forEach(expr -> {
                expr.setEmpId(emp.getId());
            });
            // 批量保存员工工作经历信息
            exprService.saveBatch(exprList);
        }
    }

    /**
     * 根据id删除员工和员工工作经历信息
     *
     * @param ids 员工id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteEmpsByIds(List<Integer> ids) {
        // 批量删除员工基本信息
        removeByIds(ids);

        // 批量删除员工工作经历信息
        exprService.remove(Wrappers.<EmpExpr>lambdaQuery().in(EmpExpr::getEmpId, ids));
    }
}
