package com.poka.util;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.File;
import java.util.Properties;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author chenbo
 */
public class HibernateUtil {

    //private static URL CONFIG_FILE_LOCATION = HibernateUtil.class.getResource("/hibernate.cfg.xml");
    private static final ThreadLocal<Session> threadLocal = new ThreadLocal<Session>();
    private static MyAnnotationConfiguration configuration = new MyAnnotationConfiguration();
    private static SessionFactory sessionFactory;
    //private static String configFile = CONFIG_FILE_LOCATION;
    static {
        try {
            
            // 此处是用annotation来做的，而且文件的路径必须改为这种方式，如果直接传字符串作为路径，在怎么都不会找到配置文件
            String path = System.getProperty("user.dir");
            //configuration.configure(HibernateUtil.class.getResource("/hibernate.cfg.xml"));
           // configuration.configure(HibernateUtil.class.getResource("/config/hibernate.cfg.xml"));
            configuration.configure(new File(path+"\\hibernate.cfg.xml"));
            Properties extraProperties = new Properties();
        //    System.out.println( "jdbc:odbc:DRIVER={Microsoft Access Driver(*.mdb, *.accdb)};DBQ="+path.replaceAll("\\\\", "/")+"/mon.accdb ");
         //   System.out.println( "jdbc:odbc:driver={Microsoft Access Driver(*.mdb, *.accdb)};DBQ="+path+"\\mon.accdb ");
            extraProperties.setProperty("hibernate.connection.url", "jdbc:sqlite:file:///"+path.replaceAll("\\\\", "/")+"/mon.db");
          //  extraProperties.setProperty("hibernate.connection.url", "jdbc:odbc:DRIVER={Microsoft Access Driver(*.mdb, *.accdb)};DBQ="+path.replaceAll("\\\\", "/")+"/mon.accdb ");
            
           //extraProperties.setProperty("hibernate.connection.url", "jdbc:odbc:pokarmb");
            
            configuration.addProperties(extraProperties);
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
            configuration.configure(new File(path+"\\hibernate.cfg.xml"));
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
     *
     */
    public static MyAnnotationConfiguration getConfiguration() {
        return configuration;
    }

    //文件发生改变后，调用此方法，就可以更改配置

    public static void reConnect() {
        HibernateUtil.getConfiguration().reset();
//        if(HibernateUtil.getSessionFactory()!=null&&HibernateUtil.getSessionFactory().getCurrentSession()!=null){
        HibernateUtil.getSessionFactory().getCurrentSession().close();
//        }
        //HibernateUtil.getSessionFactory().getSession().close();
//        if( HibernateUtil.getSessionFactory()!=null){  
            HibernateUtil.getSessionFactory().close();
//        }
      
        rebuildSessionFactory();
    }

}
//    private static SessionFactory buildSessionFactory() {
//
//        try {
//          Configuration cfg = new Configuration().configure();		
//          return cfg.buildSessionFactory();
//        }
//
//        catch (Throwable ex) {
//            // Make sure you log the exception, as it might be swallowed
//            System.err.println("Initial SessionFactory creation failed." + ex);
//
//            throw new ExceptionInInitializerError(ex);
//        }
//
//    }
//    private static String CONFIG_FILE_LOCATION = System.getProperty("user.dir")
//   + "\\config\\hibernate.cfg.xml";
// private static final ThreadLocal<Session> threadLocal = new ThreadLocal<Session>();
// private static MyAnnotationConfiguration configuration = new MyAnnotationConfiguration();
// private static SessionFactory sessionFactory;
// private static String configFile = CONFIG_FILE_LOCATION;
//
// static {
//  try {
//   // 此处是用annotation来做的，而且文件的路径必须改为这种方式，如果直接传字符串作为路径，在怎么都不会找到配置文件
//   configuration.configure(new File(configFile));
//   
//   sessionFactory = configuration.buildSessionFactory();
//  } catch (Exception e) {
//   System.err.println("%%%% Error Creating SessionFactory %%%%");
//   e.printStackTrace();
//  }

//   public static void flashSessionFaction(){
//        Configuration cfg = new Configuration().configure();		
//        sessionFactory.close();
//        sessionFactory = null;
//        
//        sessionFactory = cfg.buildSessionFactory();
//   }
//
//    public static SessionFactory getSessionFactory() {     
//        return sessionFactory;
//    }

