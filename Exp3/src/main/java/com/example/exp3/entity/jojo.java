package com.example.exp3.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor  //有参构造
@NoArgsConstructor   //无参构造
public class jojo {
    private String id;
    private String name;
    private String introduction;
    private Integer sale;
    private Integer number;

}
