/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poka.util;

import com.poka.app.frame.PokaMainFrame;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author poka
 */
public class IOUtil {

    /**
     * 根据参数生成BK文件
     *
     * @param file
     * @param path
     */
    public static void main(String[] arg) {
        //getBaID("sdfdfdf");
    }

    public static void deleteDir(File dir) {
        File[] files = dir.listFiles();

        for (File file : files) {
            if (file.isDirectory()) {
                deleteDir(file);
            } else {
                file.delete();
            }
        }
    }

    public static void writeBKFile(String[] arrs, String path) {
        try {
            File file = new File(path);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
            BufferedWriter bw = new BufferedWriter(osw);
            for (String arr : arrs) {
                bw.write(arr + "\n");
            }
            //注意关闭的先后顺序，先打开的后关闭，后打开的先关闭
            bw.close();
            osw.close();
            fos.close();
        } catch (IOException ex) {
            Logger.getLogger(PokaMainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 生成把ID
     */
    public static String getBaID(String jiju) {
        Date date = BundleDeal.getDBTime();
        String mydate = new SimpleDateFormat("MMddHHmmsss").format(date);
        String mouth = String.valueOf(date.getMonth() + 1);
        String replaceWord = "";
        if (mouth.equals("10")) {
            replaceWord = "A";
            mydate = mydate.replaceFirst(mouth, replaceWord);
        } else if (mouth.equals("11")) {
            replaceWord = "B";
            mydate = mydate.replaceFirst(mouth, replaceWord);
            System.out.println(mydate);
        } else if (mouth.equals("12")) {
            replaceWord = "C";
            mydate = mydate.replaceFirst(mouth, replaceWord);
        }

        String baID = "";
        char panduan = mydate.charAt(0);
        if (panduan == '0') {
            mydate = mydate.substring(1);
        }
        baID = "XXXXXXXXXXXXXX" + mydate;   //把Id
        while (baID.length() < 24) {
            baID += "E";
        }
        return baID;
    }

    /**
     * 生成把ID
     */
    public static String getBaID() {
        Date date = BundleDeal.getDBTime();
        String mydate = new SimpleDateFormat("MMddHHmmss").format(date);
        String mouth = String.valueOf(date.getMonth() + 1);
        String replaceWord = "";
        if (mouth.equals("10")) {
            replaceWord = "A";
            mydate = mydate.replaceFirst(mouth, replaceWord);
        } else if (mouth.equals("11")) {
            replaceWord = "B";
            mydate = mydate.replaceFirst(mouth, replaceWord);
            System.out.println(mydate);
        } else if (mouth.equals("12")) {
            replaceWord = "C";
            mydate = mydate.replaceFirst(mouth, replaceWord);
        }

        String baID = "";
        char panduan = mydate.charAt(0);
        if (panduan == '0') {
            mydate = mydate.substring(1);
        }
        baID = "XXXXXXXXXXXXXX" + mydate;   //把Id
        while (baID.length() < 23) {
            baID += "E";
        }
        return baID;
    }
    /**
     * 广电清分机后台文件夹数据登陆后删除
     */
//    public static void deleteGuangdianFile(){
//        String path = StaticVar.cfgMap.get(argPro.localAddr) + File.separator +  UploadFtp.guangdianfile;
//            File guangdianDir = new File(path);
//            if(guangdianDir.exists()){
//                if(guangdianDir.isDirectory()){
//                    File[] files = guangdianDir.listFiles();
//                    for (File file : files) {
//                        file.delete();
//                    }
//                }else{
//                    guangdianDir.mkdirs();
//                }
//            }else{
//                guangdianDir.mkdirs();
//            }
//    }
}
