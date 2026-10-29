package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.mapper.ClazzMapper;
import com.itheima.pojo.Clazz;
import com.itheima.pojo.Emp;
import com.itheima.pojo.PageResult;
import com.itheima.service.ClazzService;
import com.itheima.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClazzServiceImpl extends ServiceImpl<ClazzMapper, Clazz> implements ClazzService {
    @Autowired
    private EmpService empService;

    /**
     * 分页条件查询
     *
     * @param name     班级名称
     * @param begin    结课时间开始
     * @param end      结课时间结束
     * @param page     当前页码
     * @param pageSize 每页显示记录数
     * @return 结果
     */
    @Override
    public PageResult<Clazz> getPageResult(String name, LocalDate begin, LocalDate end, int page, int pageSize) {
        Page<Clazz> p = new Page<>(page, pageSize);
        // 根据最近修改时间降序排序
        p.addOrder(OrderItem.desc("update_time"));

        p = lambdaQuery().like(name != null && !name.isEmpty(), Clazz::getName, name)
                .gt(begin != null, Clazz::getEndDate, begin)
                .lt(end != null, Clazz::getEndDate, end)
                .page(p);
        List<Clazz> clazzList = p.getRecords();

        List<Clazz> records = clazzList.stream().map(clazz -> {
            // 获取当前班级的教师名称
            Integer masterId = clazz.getMasterId();
            if (masterId != null) {
                Emp emp = empService.getById(masterId);
                clazz.setMasterName(emp.getName());
            }
            // 获取当前班级的状态
            LocalDate now = LocalDate.now();
            if (now.isBefore(clazz.getBeginDate())) {
               clazz.setStatus("未开班");
            }
            if (now.isAfter(clazz.getEndDate())) {
                clazz.setStatus("已结课");
            }
            if (now.isAfter(clazz.getBeginDate()) && now.isBefore(clazz.getEndDate())) {
                clazz.setStatus("在读");
            }
            return clazz;
        }).collect(Collectors.toList());

        return new PageResult<>(p.getTotal(), records);
    }
}
