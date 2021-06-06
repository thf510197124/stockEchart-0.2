package com.example.demo.service;

import com.example.demo.pojo.SymbolName;
import com.example.demo.repository.SymbolNameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @FileName: SymbolNameService
 * @Author: Haifeng Tong
 * @Date: 2021/4/2411:18 下午
 * @Description:
 * @History: 2021/4/24
 */
@Service
public class SymbolNameService {

    private SymbolNameRepository sMrepository;
    @Autowired
    public void setsMrepository(SymbolNameRepository sMrepository) {
        this.sMrepository = sMrepository;
    }
    @Transactional
    public SymbolName updateSymbolName(String symbol,String name,String description){
        if (name == null || name.length() < 3){
            System.out.println("-----------------错误的股票名称，不能更新---------------");
            return null;
        }
        if(description.length() >0){
            sMrepository.updateSymbolName(symbol,name,description);
            return sMrepository.findBySymbol(symbol);
        }else{
            sMrepository.updateSymbolName(symbol,name);
            return sMrepository.findBySymbol(symbol);
        }
    }
    @Transactional
    public SymbolName addSymbolName(String symbol,String name,String description){
        if (name == null || name.length() < 3){
            System.out.println("-----------------错误的股票名称，不能添加---------------");
            return null;
        }
        SymbolName symbolName = new SymbolName();
        symbolName.setSymbol(symbol);
        symbolName.setName(name);
        symbolName.setDescription(description);
        return sMrepository.save(symbolName);
    }
    @Transactional
    public SymbolName save(SymbolName symbol){
        return sMrepository.save(symbol);
    }
    public SymbolName findSymbolName(String symbol){
        return sMrepository.findBySymbol(symbol);
    }
    public SymbolName updateOrAdd(String symbol,String name,String description){
        if (name == null || name.length() < 3){
            System.out.println("-----------------错误的股票名称，不能添加---------------");
            return null;
        }
        if (findSymbolName(symbol) == null){
            return addSymbolName(symbol,name,description);
        }else{
            return updateSymbolName(symbol,name,description);
        }
    }
    public SymbolName findByName(String name){
        return sMrepository.findByName(name);
    }

    public List<SymbolName> findAll() {
        return sMrepository.findAll();
    }
}
