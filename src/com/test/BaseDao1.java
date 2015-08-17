/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author chenbo
 */
public class BaseDao1 {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/test";  
                                
    private static final String UserName = "root";    //用户名称  
    private static final String PassWord = "root";  //用户密码  
    private static Connection conn = null;  
    /**
     * 获取连接
     * @return Connection 
     */
    public static Connection getConnection(){  
        try {  
            Class.forName("com.mysql.jdbc.Driver");  //加载驱动程序  
            conn = DriverManager.getConnection(URL, UserName, PassWord);    //创建数据库连接  
        } catch (SQLException | ClassNotFoundException e) { 
            e.printStackTrace();
        }  
        return conn;  
    }  
    
    
    
    /** 
     * 执行增删改 
     * @param sql 
     * @param args 
     * @return boolean 
     */  
    public static boolean executeSql(String sql,Object[] args)  
    {  
        boolean sign=false;         
        PreparedStatement state=null;  
          
        try {  
            conn=getConnection();
            state=conn.prepareStatement(sql);  
            if(args!=null && args.length>0)  
            {  
                for (int i = 0; i < args.length; i++) {  
                    state.setObject(i+1, args[i]);  
                }  
            }  
            int rows=state.executeUpdate();  
            if(rows>0)  
                sign=true;  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
        finally  
        {  
            closeAll(null, state, conn);  
        }  
        return sign;  
    }         
      
    /** 
     * 执行查询 
     * @param sql 
     * @param args 
     * @return javax.servlet.jsp.jstl.sql.Result; 
     */  
    public static ResultSet executeQuery(String sql,Object[] args)  
    {  
        //JSTL中提供的类,因此要导入JSTL的两个jar包  
        ResultSet result=null;      
        PreparedStatement state=null;  
        ResultSet rs=null;  
          
          
        try {  
            conn=getConnection();
            state=conn.prepareStatement(sql);  
            if(args!=null && args.length>0)  
            {
                for (int i = 0; i < args.length; i++) {
                    state.setObject(i+1, args[i]);
                }  
            }  
            rs= state.executeQuery();  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
        finally  
        {  
            closeAll(rs, state, conn);  
        }  
        return result;  
    }  
      
    /** 
     * 关闭资源 
     * @param rs 
     * @param state 
     * @param conn 
     */  
    public static void closeAll(ResultSet rs,Statement state,Connection conn)  
    {  
        try {  
            if(rs!=null)  
                rs.close();  
            if(state!=null)  
                state.close();  
            if(conn!=null && conn.isClosed())  
                conn.close();  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
    }  
    
    
    
    
    
    
    
}

