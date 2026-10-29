package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.mapper.StudentMapper;
import com.itheima.pojo.Clazz;
import com.itheima.pojo.PageResult;
import com.itheima.pojo.Student;
import com.itheima.pojo.StudentQueryParam;
import com.itheima.service.ClazzService;
import com.itheima.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {
    @Autowired
    private ClazzService clazzService;

    /**
     * 分页查询学生信息
     *
     * @param param 查询参数
     * @return 查询到的学生信息
     */
    @Override
    public PageResult<Student> getPageResult(StudentQueryParam param) {
        Page<Student> page = new Page<>(param.getPage(), param.getPageSize());
        // 根据最近修改时间降序排序
        page.addOrder(OrderItem.desc("update_time"));

        page = lambdaQuery().like(param.getName() != null && !param.getName().isEmpty(), Student::getName, param.getName())
                .eq(param.getDegree() != null, Student::getDegree, param.getDegree())
                .eq(param.getClazzId() != null, Student::getClazzId, param.getClazzId())
                .page(page);

        List<Student> emps = page.getRecords();

        List<Integer> clazzIds = emps.stream().map(emp -> emp.getClazzId()).collect(Collectors.toList());
        if (!clazzIds.isEmpty()) {
            List<Clazz> clazzList = clazzService.listByIds(clazzIds).stream().collect(Collectors.toList());
            emps.forEach(emp -> {
                clazzList.forEach(clazz -> {
                    if (emp.getClazzId() != null && emp.getClazzId().equals(clazz.getId())) {
                        emp.setClazzName(clazz.getName());
                    }
                });
            });
        }

        return new PageResult<>(page.getTotal(), emps);
    }

    /**
     * 扣分
     *
     * @param id    学生id
     * @param score 扣分
     */
    @Override
    public void violation(Integer id, Integer score) {
        UpdateWrapper<Student> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id)
                .setSql("violation_score = violation_score + " + score)
                .setSql("violation_count = violation_count + 1");
        update(updateWrapper);
    }
}
