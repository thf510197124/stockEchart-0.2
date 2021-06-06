package com.example.demo.service;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @FileName: SymbolNameServiceTest
 * @Author: Haifeng Tong
 * @Date: 2021/4/259:39 上午
 * @Description:
 * @History: 2021/4/25
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class SymbolNameServiceTest {

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void setsMrepository() {
    }

    @Test
    public void updateSymbolName() {

    }

    @Test
    public void addSymbolName() {
        SymbolNameService symbolNameService = new SymbolNameService();
        symbolNameService.addSymbolName("002707","众信旅游","");
    }

    @Test
    public void findSymbolName() {

    }
}