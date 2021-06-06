package com.example.demo.repository;


import com.example.demo.pojo.SymbolName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @FileName: SymbolNameRepository
 * @Author: Haifeng Tong
 * @Date: 2021/4/2411:17 下午
 * @Description:
 * @History: 2021/4/24
 */
@Repository
public interface SymbolNameRepository extends JpaRepository<SymbolName,String>, JpaSpecificationExecutor<SymbolName> {
    @Modifying
    @Query("update SymbolName set name=:name where symbol = :symbol")
    @Transactional
    public void updateSymbolName(String symbol,String name);
    @Modifying
    @Query("update SymbolName set name=:name,description=:description where symbol=:symbol")
    @Transactional
    public void updateSymbolName(String symbol,String name,String description);
    public SymbolName findBySymbol(String symbol);
    //public void finaSymbolStartingWith(String subSymbol);
    public SymbolName findByName(String name);
    public List<SymbolName> findAll();
}
