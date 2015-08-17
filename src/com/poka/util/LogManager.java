/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poka.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author
 *
 */
public class LogManager {

    static String path;
    static FileHandler fh;

    // 初始化LogManager
    static {

        // 读取配置文件
      //  ClassLoader cl = LogManager.class.getClassLoader();
        InputStream inputStream = null;
    //    if (cl != null) {
            try {
                inputStream =new FileInputStream(new File(System.getProperty("user.dir")+"\\logging.properties"));//cl.getResourceAsStream(System.getProperty("user.dir")+"\\logging.properties");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(LogManager.class.getName()).log(Level.SEVERE, null, ex);
            }
       // } else {
       //     inputStream = ClassLoader
       //             .getSystemResourceAsStream(System.getProperty("user.dir")+"\\logging.properties");
       // }
        java.util.logging.LogManager logManager = java.util.logging.LogManager
                .getLogManager();
        try {

            // 重新初始化日志属性并重新读取日志配置。
            logManager.readConfiguration(inputStream);

            StringBuilder logPath = new StringBuilder();
            String tem = logManager.getProperty("java.util.logging.FileHandler.pattern");
            logPath.append(System.getProperty("user.dir"));
            logPath.append(File.separator);
            logPath.append("log");
            logPath.append(File.separator);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            logPath.append(sdf.format(new Date()));
            logPath.append(File.separator);
            logPath.append(tem);
            logPath.append(".log");
            path = logPath.toString();

            if (path != null) {
                UploadFtp.newDir(path);
            }

            try {
                fh = new FileHandler(path, true);

            } catch (IOException ex) {
                Logger.getLogger(LogManager.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(LogManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SecurityException e) {
            System.err.println(e);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    /**
     * 获取日志对象
     *
     * @param clazz
     * @return
     */
    public static Logger getLogger(Class clazz) {

        Logger logger = Logger.getLogger(clazz.getName());
        logger.addHandler(fh);
        return logger;
    }
}
