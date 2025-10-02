package com.example.exp3.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;  //Mybatis中封装的
import com.example.exp3.entity.jojo;

import java.util.List;


public interface JOJOMAPPER extends BaseMapper<jojo> {
    void insertBatch(List<jojo> list);
}
