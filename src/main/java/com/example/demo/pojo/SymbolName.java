package com.example.demo.pojo;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;


/**
 * @FileName: SymbolName
 * @Author: Haifeng Tong
 * @Date: 2021/4/2410:43 下午
 * @Description:
 * @History: 2021/4/24
 */
@Entity
@Table(name = "symbol_name")
@Data
public class SymbolName{
    @Id
    private String symbol;
    @Column(unique=true,nullable=false)
    private String name;
    private String description;
    private Date upDate;
}
