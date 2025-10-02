package com.example.exp3.controller;

import ch.qos.logback.classic.Logger;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.exp3.entity.jojo;
import com.example.exp3.mapper.JOJOMAPPER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController  //后端数据流返回前端
public class JOJOController {

    @Autowired
    JOJOMAPPER jojoMapper;
    private Logger logger;

    @RequestMapping("/insert")  //增
    public String insert(String id, String name, String introduction, Integer sale, Integer number){
        // 参数非空校验
        if (id == null || name == null || introduction == null || sale == null || number == null) {
            return "添加失败";
        }

        // 参数有效性校验
        if (id.trim().isEmpty() || name.trim().isEmpty() || number < 0) {
            return "添加失败";
        }

        try {
            // 使用参数化查询防止SQL注入
            int result = jojoMapper.insert(new jojo(id, name, introduction, sale, number));
            return result > 0 ? "添加成功" : "添加失败";
            } catch (Exception e) {
                // 异常处理
                return "添加失败";
        }
    }


    @RequestMapping("/delete")  //删
    public String delete(String id){
        try {
            // 参数校验
            if (id == null || id.trim().isEmpty()) {
                return "删除失败：ID不能为空";
            }

            // 执行删除操作
            int result = jojoMapper.deleteById(id);
            return result > 0 ? "删除成功" : "删除失败";
            } catch (Exception e) {
                // 异常处理
                return "删除失败：系统异常";
        }
    }


    @RequestMapping("/update")  //改
    public String update(String id, String name, String introduction, Integer sale, Integer number){
        try {
            // 参数校验
            if (id == null || id.trim().isEmpty()) {
                return "修改失败";
            }

            // 数值范围校验
            if (sale != null && (sale < 0 || sale > Integer.MAX_VALUE)) {
                return "修改失败";
            }
            if (number != null && (number < 0 || number > Integer.MAX_VALUE)) {
                return "修改失败";
            }

            // 执行更新操作
            int result = jojoMapper.updateById(new jojo(id, name, introduction, sale, number));
            return result > 0 ? "修改成功" : "修改失败";
        } catch (Exception e) {
            // 异常处理
            return "修改失败";
        }
    }


    @RequestMapping("/select1")  //查
    public List<jojo> select1(){
        try {
            return jojoMapper.selectList(null);
        } catch (Exception e) {
            // 记录异常日志
            throw new RuntimeException("查询数据失败", e);
        }
    }


    @RequestMapping("/selectById")  //根据ID查询
    public jojo selectById(String id){
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID参数不能为空");
        }
        try {
            return jojoMapper.selectById(id);
        } catch (Exception e) {
            // 记录日志
            throw new RuntimeException("查询数据时发生错误", e);
        }
    }


    @RequestMapping("/selectByName")  //根据名称模糊查询
    public List<jojo> selectByName(String name){
        try {
            if (name == null || name.trim().isEmpty()) {
                return new ArrayList<>();
            }
            // 使用参数化查询防止SQL注入
            return jojoMapper.selectList(
                    new QueryWrapper<jojo>().like("name", name)
            );
        } catch (Exception e) {
            // 记录异常日志
            throw new RuntimeException("查询失败", e);
        }
    }


    @RequestMapping("/selectPage")  //分页查询
    public IPage<jojo> selectPage(int pageNum, int pageSize){
        // 参数校验
        if (pageNum <= 0) {
            pageNum = 1;
        }
        if (pageSize <= 0 || pageSize > 100) {
            pageSize = 10;
        }

        try {
            Page<jojo> page = new Page<>(pageNum, pageSize);
            return jojoMapper.selectPage(page, null);
        } catch (Exception e) {
            // 记录异常日志
            throw new RuntimeException("分页查询失败", e);
        }
    }


        @RequestMapping("/count")  //统计数量
    public long count(){
        try {
            return jojoMapper.selectCount(null);
        } catch (Exception e) {
            // 记录异常日志
            logger.error("统计数量时发生数据库异常", e);
            throw new RuntimeException("统计失败，请稍后重试");
        }
    }


    @RequestMapping("/countBySale")  //统计销售数量
    public long countBySale(Integer sale){
        if (sale == null) {
            throw new IllegalArgumentException("sale参数不能为空");
        }
        if (sale < 0) {
            throw new IllegalArgumentException("sale参数不能为负数");
        }
        return jojoMapper.selectCount(
                new QueryWrapper<jojo>().ge("sale", sale)
        );
    }


    @RequestMapping("/updateNumber")  //修改数量
    public String updateNumber(String id, Integer number){
        // 参数校验
        if (id == null || id.trim().isEmpty()) {
            return "ID不能为空";
        }
        if (number == null) {
            return "数量不能为空";
        }

        try {
            jojo j = jojoMapper.selectById(id);
            if (j != null) {
                j.setNumber(number);
                return jojoMapper.updateById(j) > 0 ? "数量修改成功" : "数量修改失败";
            }
            return "未找到该数据";
        } catch (Exception e) {
            // 异常处理
            return "系统异常，请稍后重试";
        }
    }


    @RequestMapping("/insertBatch")
    @Transactional
    public String insertBatch(@RequestBody List<jojo> list){
        if (list == null || list.isEmpty()) {
            return "批量添加成功";
        }

        try {
            jojoMapper.insertBatch(list);
            return "批量添加成功";
        } catch (Exception e) {
            throw new RuntimeException("批量插入失败：" + e.getMessage(), e);
        }
    }


    @RequestMapping("/deleteBatch")  //批量删除
    public String deleteBatch(@RequestBody List<String> ids){
        // 参数非空校验
        if (ids == null) {
            return "批量删除失败";
        }

        // 空列表校验
        if (ids.isEmpty()) {
            return "批量删除成功";
        }

        try {
            // 对ID进行基本的有效性检查（防止SQL注入）
            for (String id : ids) {
                if (id == null || id.trim().isEmpty()) {
                    return "批量删除失败";
                }
            }

            return jojoMapper.deleteBatchIds(ids) > 0 ? "批量删除成功" : "批量删除失败";
        } catch (Exception e) {
            // 异常处理
            return "批量删除失败";
        }
    }


    @RequestMapping("/selectOrderBySale")  //按销量排序
    public List<jojo> selectOrderBySale(){
        try {
            return jojoMapper.selectList(
                    new QueryWrapper<jojo>().orderByDesc("sale")
            );
        } catch (Exception e) {
            // 记录异常日志
            System.err.println("查询销量排序数据失败: " + e.getMessage());
            throw e; // 重新抛出异常，让上层处理
        }
    }


    @RequestMapping("/lowStock")  //库存低于5报警
    public List<jojo> lowStock(){
        try {
            final int LOW_STOCK_THRESHOLD = 5;
            return jojoMapper.selectList(
                    new QueryWrapper<jojo>().lt("number", LOW_STOCK_THRESHOLD)
            );
        } catch (Exception e) {
            // 记录异常日志
            throw new RuntimeException("查询低库存商品时发生错误", e);
        }
    }

}
