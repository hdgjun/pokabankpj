package com.poka.util;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.File;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author chenbo
 */
public class HibernateSqlServerUtil {
    private static final ThreadLocal<Session> threadLocal = new ThreadLocal<Session>();
    private static MyAnnotationConfiguration configuration = new MyAnnotationConfiguration();
    private static SessionFactory sessionFactory;
    //private static String configFile = CONFIG_FILE_LOCATION;
    static {
        try {                     
            String path = System.getProperty("user.dir");         
            configuration.configure(new File(path+"\\hibernate.cfg.sqlserver.xml"));           
            sessionFactory = configuration.buildSessionFactory();
        } catch (Exception e) {
            System.err.println("%%%% Error Creating SessionFactory %%%%");
            e.printStackTrace();
        }
    }    
    public static void rebuildSessionFactory(){
          try {
            // 此处是用annotation来做的，而且文件的路径必须改为这种方式，如果直接传字符串作为路径，在怎么都不会找到配置文件
            String path = System.getProperty("user.dir");
            configuration.configure(new File(path+"\\hibernate.cfg.sqlserver.xml"));
            sessionFactory = configuration.buildSessionFactory();
        } catch (Exception e) {
            System.err.println("%%%% Error Creating SessionFactory %%%%");
            e.printStackTrace();
        }
    }
     public static SessionFactory getSessionFactory() {     
        return sessionFactory;
    }

    /**
     * return hibernate configuration
     */
    public static MyAnnotationConfiguration getConfiguration() {
        return configuration;
    }

    //文件发生改变后，调用此方法，就可以更改配置

    public static void reConnect() {
        HibernateSqlServerUtil.getConfiguration().reset();
        HibernateSqlServerUtil.getSessionFactory().getCurrentSession().close();
        //HibernateUtil.getSessionFactory().getSession().close();
        HibernateSqlServerUtil.getSessionFactory().close();
        rebuildSessionFactory();
    }
}

