/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.poka.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.log4j.Logger;

/**
 *
 * @author poka
 */
public class DBConnTest {
    private static Logger logger = Logger.getLogger(DBConnTest.class);
    private static Connection conn;
    private static String sqlserverdriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static String mysqldriver = "com.mysql.jdbc.Driver";
    /**
     * 测试sqlserver数据库连接
     * @param username
     * @param password
     * @param dbname
     * @param ip
     * @param port
     * @return 
     */
    public static boolean getSqlServerConnection(String username,String password,String dbname,String ip,String port){
    System.out.println("sqlserver连接中...");
    try {
     Class.forName(sqlserverdriver);
     String url = getSqlServerUrl(dbname,ip,port);
     conn = DriverManager.getConnection(url, username, password);
     System.out.println("成功连接");
    } catch (ClassNotFoundException e) {
     // TODO Auto-generated catch block             
        logger.error("sqlserverdriver解析失败", e);
        e.printStackTrace();
     return false;
    } catch (SQLException e) {
     // TODO Auto-generated catch block        
        logger.error("连接失败", e);
        e.printStackTrace();     
     return false;
    }
    return true;
 }
    /**
     * 测试mysql数据库连接
     * @param username
     * @param password
     * @param dbname
     * @param ip
     * @param port
     * @return 
     */
    public static boolean getMySqlConnection(String username,String password,String dbname,String ip,String port){
    System.out.println("mysql连接中...");
    try {
     Class.forName(mysqldriver);
     String url = getMySqlUrl(dbname,ip,port);
     conn = DriverManager.getConnection(url, username, password);
     System.out.println("成功连接");
    } catch (ClassNotFoundException e) {
     // TODO Auto-generated catch block           
        logger.error("mysqldriver解析失败", e);
        e.printStackTrace();
     return false;
    } catch (Exception e) {
     // TODO Auto-generated catch block
        logger.error("连接失败", e);
        e.printStackTrace();     
     return false;
    }
    return true;
 }
    /**
     * 生成sqlserver数据库URL
     * @param dbname
     * @param ip
     * @param port
     * @return 
     */
  public static String getSqlServerUrl(String dbname,String ip,String port) {
       return "jdbc:sqlserver://" + ip + ":" + port + ";DatabaseName=" + dbname;      
  }
  
  /**
     * 生成mysql数据库URL
     * @param dbname
     * @param ip
     * @param port
     * @return 
     */
  public static String getMySqlUrl(String dbname,String ip,String port) {            
       return "jdbc:mysql://" + ip + ":" + port + "/" + dbname;      
  }
    
}
