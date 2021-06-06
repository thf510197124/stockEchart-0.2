package com.example.czsc.dataSystem.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.example.czsc.utils.Utils.println;

/**
 * @FileName: DataSourceConnection
 * @Author: Haifeng Tong
 * @Date: 2021/5/117:07 下午
 * @Description:
 * @History: 2021/5/11
 */
public class DataSourceConnection {
    /**
     * 全部的数据都读成字符串的数组
     * @param sql
     * @return
     */
    public static List<String[]> getResultSet(String sql){
        List<String[]> result = new ArrayList<>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql:///finance?characterEncoding=UTF-8&useUnicode=true&&useSSL=false&serverTimezone=UTC";
            String username = "root";
            String password = "8023myself";
            Connection conn = DriverManager.getConnection(url,username,password);
            PreparedStatement st = conn.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            int size = rs.getMetaData().getColumnCount();
            println("结果集有" + rs.getFetchSize());
            while (rs.next()){
                String[] r = new String[size];
                for (int i = 0;i<size ;i++){
                    r[i] = rs.getString(i+1);
                }
                result.add(r);
            }
            rs.close();
            st.close();
            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

}
