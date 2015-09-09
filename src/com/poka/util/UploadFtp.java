/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poka.util;

import static com.jcraft.jsch.ChannelSftp.RESUME;
import com.poka.app.util.FileUploadMonitor;
import static com.poka.util.ZipUtil.compress;
import java.io.File;
import java.util.Date;
import java.util.List;

/**
 *
 * @author poka
 */
public class UploadFtp {

    public static final String fsnbak = "POKABak";
    public static final String bfbak = "BFBak";
    public static final String bkbak = "BKBak";
    public static final String ctbak = "CTBak";
    public static final String tem = "TEM";
    public static final String reUpload = "REUpLoad";
    public static final String basebak = "BaseBak";

    public static final String qfFile = "QFFile";//清分机加钞文件落地目录
    public static final String addQFFile = "JULONGFILE";//清分机加钞文件落地目录

    public void fileUploadFtp() {

    }

    public static void createDir(String path) {
        if (path != null) {
            UploadFtp.newDir(path + File.separator + fsnbak + File.separator + "TEM");
            UploadFtp.newDir(path + File.separator + bfbak + File.separator + "TEM");
            UploadFtp.newDir(path + File.separator + bkbak + File.separator + "TEM");
            UploadFtp.newDir(path + File.separator + ctbak + File.separator + "TEM");
            UploadFtp.newDir(path + File.separator + tem + File.separator + "TEM");
            UploadFtp.newDir(path + File.separator + reUpload + File.separator + "TEM");
            UploadFtp.newDir(path + File.separator + basebak + File.separator + "TEM");
            UploadFtp.newDir(path + File.separator + qfFile + File.separator + "TEM");
            UploadFtp.newDir(path + File.separator + addQFFile + File.separator + "TEM");
        }
    }

    public static void oneFileUploadFtp(String fileName, String bakDir) {
        String basePath = StaticVar.cfgMap.get(argPro.localAddr);
        String localFilePath = basePath + File.separator + UploadFtp.tem;
        String remotePath = StaticVar.cfgMap.get(argPro.remoteAddr);
        String bakPath = basePath + File.separator + bakDir;
        String reUploadPath = basePath + File.separator + UploadFtp.reUpload;
        String filePath = localFilePath + File.separator + fileName;

        String date = new java.text.SimpleDateFormat("yyyyMMdd").format(new Date());
        File upFile = newDir(filePath);

        //上传到前置机
        PokaSftp sftp = new PokaSftp();
        sftp.connect(StaticVar.cfgMap.get(argPro.ftp), Integer.valueOf(StaticVar.cfgMap.get(argPro.port)), StaticVar.cfgMap.get(argPro.user), StaticVar.cfgMap.get(argPro.pwd), 6000);
        if (sftp.getSftp() == null) {
            String tempFilePath = reUploadPath + File.separator + fileName;
            File tempFile = newDir(tempFilePath);
            if (upFile.renameTo(tempFile)) {
                upFile.delete();
            }
            return;
        }

        boolean flag = false;
        String temPokaName = fileName + ".TEM";
        flag = sftp.upload(remotePath, temPokaName, upFile, RESUME);
        if (flag) {//上传成功，备份poka fsn文件
            flag = sftp.rename(temPokaName, fileName, remotePath);
            // boolean setWritable = .setWritable(true);
            if (flag) {
                File tem = newDir(bakPath + File.separator + date + File.separator + fileName);
//                if (!tem.getParentFile().exists()) {
//                    tem.getParentFile().mkdirs();
//                }
                if (tem.exists()) {
                    tem = new File(bakPath + File.separator + date + File.separator + fileName + "." + (new java.text.SimpleDateFormat("yyyyMMddHHmmssSSS")).format(BundleDeal.getDBTime()));
                }
                if (upFile.renameTo(tem)) {//poka fsn文件移到备份目录
                    upFile.delete();
                }
            } else {
                String tempFilePath = reUploadPath + File.separator + fileName;
                File tempFile = newDir(tempFilePath);
                if (upFile.renameTo(tempFile)) {
                    upFile.delete();
                }
            }
        } else {//上传失败，将文件存入reupload目录下
            String tempFilePath = reUploadPath + File.separator + fileName;

            File tempFile = newDir(tempFilePath);
            if (upFile.renameTo(tempFile)) {
                upFile.delete();
            }
        }
        sftp.disConnect();

    }

    public static File newDir(String path) {
        File file = new File(path);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        return file;
    }

    public static void oneFileUploadFtp(String fileName, String bakDir, FileUploadMonitor fileUploadMonitor) {

        String basePath = StaticVar.cfgMap.get(argPro.localAddr);
        String localFilePath = basePath + File.separator + UploadFtp.tem;
        String remotePath = StaticVar.cfgMap.get(argPro.remoteAddr);
        String bakPath = basePath + File.separator + bakDir;
        String reUploadPath = basePath + File.separator + UploadFtp.reUpload;
        String filePath = localFilePath + File.separator + fileName;

        String date = new java.text.SimpleDateFormat("yyyyMMdd").format(new Date());

        File upFile = newDir(filePath);

        //上传到前置机
        PokaSftp sftp = new PokaSftp();
        sftp.connect(StaticVar.cfgMap.get(argPro.ftp), Integer.valueOf(StaticVar.cfgMap.get(argPro.port)), StaticVar.cfgMap.get(argPro.user), StaticVar.cfgMap.get(argPro.pwd), 6000);
        if (sftp.getSftp() == null) {
            String tempFilePath = reUploadPath + File.separator + fileName;
            File tempFile = newDir(tempFilePath);
            if (upFile.renameTo(tempFile)) {
                upFile.delete();
            }
            return;
        }

        //  sftp.upload(remotePath, upFile, (FileUploadMonitor) fileUploadMonitor, RESUME);
        boolean flag = false;
        String temPokaName = fileName + ".TEM";
        fileUploadMonitor.setValue(0);
        fileUploadMonitor.setMaximum((int) upFile.length());
        ((FileUploadMonitor) fileUploadMonitor).setFileSize(upFile.length());
        flag = sftp.upload(remotePath, temPokaName, upFile, fileUploadMonitor, RESUME);
        if (flag) {//上传成功，备份poka fsn文件
            flag = sftp.rename(temPokaName, fileName, remotePath);
            // boolean setWritable = upFile.setWritable(true);
            if (flag) {
                File tem = newDir(bakPath + File.separator + date + File.separator + fileName);
                if (tem.exists()) {
                    tem = new File(bakPath + File.separator + date + File.separator + fileName + "." + (new java.text.SimpleDateFormat("yyyyMMddHHmmssSSS")).format(BundleDeal.getDBTime()));
                }
                if (upFile.renameTo(tem)) {//poka fsn文件移到备份目录
                    upFile.delete();
                }
            } else {
                String tempFilePath = reUploadPath + File.separator + fileName;
                File tempFile = newDir(tempFilePath);
                if (upFile.renameTo(tempFile)) {
                    upFile.delete();
                }
            }
        } else {//上传失败，将文件存入tem目录下
            String tempFilePath = reUploadPath + File.separator + fileName;
            File tempFile = newDir(tempFilePath);
            if (upFile.renameTo(tempFile)) {
                upFile.delete();
            }
        }
        sftp.disConnect();

    }

    public static boolean oneFileUploadFtp(String fileName, String bakDir, FileUploadMonitor fileUploadMonitor, PokaSftp sftp) {

        String basePath = StaticVar.cfgMap.get(argPro.localAddr);
        String localFilePath = basePath + File.separator + UploadFtp.tem;
        String remotePath = StaticVar.cfgMap.get(argPro.remoteAddr);
        String bakPath = basePath + File.separator + bakDir;
        String reUploadPath = basePath + File.separator + UploadFtp.reUpload;
        String filePath = localFilePath + File.separator + fileName;

        String date = new java.text.SimpleDateFormat("yyyyMMdd").format(new Date());
        //上传到前置机       
        File upFile = newDir(filePath);
        //  sftp.upload(remotePath, upFile, (FileUploadMonitor) fileUploadMonitor, RESUME);
        boolean flag = false;
        String temPokaName = fileName + ".TEM";
        fileUploadMonitor.setValue(0);
        fileUploadMonitor.setMaximum((int) upFile.length());
        ((FileUploadMonitor) fileUploadMonitor).setFileSize(upFile.length());
        flag = sftp.upload(remotePath, temPokaName, upFile, fileUploadMonitor, RESUME);
        if (flag) {//上传成功，备份poka fsn文件
            flag = sftp.rename(temPokaName, fileName, remotePath);
            // boolean setWritable = upFile.setWritable(true);
            if (flag) {
                File tem = newDir(bakPath + File.separator + date + File.separator + fileName);
                if (tem.exists()) {
                    tem = new File(bakPath + File.separator + date + File.separator + fileName + "." + (new java.text.SimpleDateFormat("yyyyMMddHHmmssSSS")).format(BundleDeal.getDBTime()));
                }
                if (upFile.renameTo(tem)) {//poka fsn文件移到备份目录
                    upFile.delete();
                }
            } else {
                String tempFilePath = reUploadPath + File.separator + fileName;
                File tempFile = newDir(tempFilePath);
                if (upFile.renameTo(tempFile)) {
                    upFile.delete();
                }
                return false;
            }
        } else {//上传失败，将文件存入tem目录下
            String tempFilePath = reUploadPath + File.separator + fileName;
            File tempFile = newDir(tempFilePath);
            if (upFile.renameTo(tempFile)) {
                upFile.delete();
            }
            return false;
        }
        return true;
    }

    public static boolean againFileUploadFtp(String fileName, String bakDir, FileUploadMonitor fileUploadMonitor, PokaSftp sftp) {

        String basePath = StaticVar.cfgMap.get(argPro.localAddr);
        String localFilePath = basePath + File.separator + UploadFtp.tem;
        String remotePath = StaticVar.cfgMap.get(argPro.remoteAddr);
        String bakPath = basePath + File.separator + bakDir;
        String reUploadPath = basePath + File.separator + UploadFtp.reUpload;
        String filePath = reUploadPath + File.separator + fileName;

        String date = new java.text.SimpleDateFormat("yyyyMMdd").format(new Date());

        //上传到前置机       
        File upFile = newDir(filePath);
        //  sftp.upload(remotePath, upFile, (FileUploadMonitor) fileUploadMonitor, RESUME);
        boolean flag = false;
        String temPokaName = fileName + ".TEM";
        fileUploadMonitor.setValue(0);
        fileUploadMonitor.setMaximum((int) upFile.length());
        ((FileUploadMonitor) fileUploadMonitor).setFileSize(upFile.length());
        flag = sftp.upload(remotePath, temPokaName, upFile, fileUploadMonitor, RESUME);
        if (flag) {//上传成功，备份poka fsn文件
            flag = sftp.rename(temPokaName, fileName, remotePath);
            // boolean setWritable = upFile.setWritable(true);
            if (flag) {
                File tem = newDir(bakPath + File.separator + date + File.separator + fileName);
                if (tem.exists()) {
                    tem = new File(bakPath + File.separator + date + File.separator + fileName + "." + (new java.text.SimpleDateFormat("yyyyMMddHHmmssSSS")).format(BundleDeal.getDBTime()));
                }
                if (upFile.renameTo(tem)) {//poka fsn文件移到备份目录
                    upFile.delete();
                }
            } else {

                return false;
            }
        } else {//上传失败，将文件存入tem目录下
            return false;
        }
        return true;
    }

    public static boolean againFileUploadFtp(String fileName, String bakDir, PokaSftp sftp) {

        String basePath = StaticVar.cfgMap.get(argPro.localAddr);
        String localFilePath = basePath + File.separator + UploadFtp.tem;
        String remotePath = StaticVar.cfgMap.get(argPro.remoteAddr);
        String bakPath = basePath + File.separator + bakDir;
        String reUploadPath = basePath + File.separator + UploadFtp.reUpload;
        String filePath = reUploadPath + File.separator + fileName;

        String date = new java.text.SimpleDateFormat("yyyyMMdd").format(new Date());

        //上传到前置机       
        File upFile = newDir(filePath);
        //  sftp.upload(remotePath, upFile, (FileUploadMonitor) fileUploadMonitor, RESUME);
        boolean flag = false;
        String temPokaName = fileName + ".TEM";
        flag = sftp.upload(remotePath, temPokaName, upFile, RESUME);
        if (flag) {//上传成功，备份poka fsn文件
            flag = sftp.rename(temPokaName, fileName, remotePath);
            // boolean setWritable = upFile.setWritable(true);
            if (flag) {
                File tem = newDir(bakPath + File.separator + date + File.separator + fileName);
                if (tem.exists()) {
                    tem = new File(bakPath + File.separator + date + File.separator + fileName + "." + (new java.text.SimpleDateFormat("yyyyMMddHHmmssSSS")).format(BundleDeal.getDBTime()));
                }
                if (upFile.renameTo(tem)) {//poka fsn文件移到备份目录
                    upFile.delete();
                }
            } else {

                return false;
            }
        } else {//上传失败，将文件存入tem目录下
            return false;
        }
        return true;
    }
    
    public static void manyFileUploadFtp(List<String> fileList, String bakDir) {
        String basePath = StaticVar.cfgMap.get(argPro.localAddr);
        String localFilePath = basePath + File.separator + UploadFtp.tem;
        String remotePath = StaticVar.cfgMap.get(argPro.remoteAddr);
        String bakPath = basePath + File.separator + bakDir;
        String reUploadPath = basePath + File.separator + UploadFtp.reUpload;
        File upFile = null;

        String date = new java.text.SimpleDateFormat("yyyyMMdd").format(new Date());

        PokaSftp sftp = new PokaSftp();
        sftp.connect(StaticVar.cfgMap.get(argPro.ftp), Integer.valueOf(StaticVar.cfgMap.get(argPro.port)), StaticVar.cfgMap.get(argPro.user), StaticVar.cfgMap.get(argPro.pwd), 6000);
        if (sftp.getSftp() == null) {
            for (String fileName : fileList) {
                upFile = newDir(localFilePath + File.separator + fileName);
                String tempFilePath = reUploadPath + File.separator + fileName;
                File tempFile = newDir(tempFilePath);
                if (upFile.renameTo(tempFile)) {
                    upFile.delete();
                }
            }
            return;
        }
        for (String fileName : fileList) {
            String filePath = localFilePath + File.separator + fileName;
            //上传到前置机
            upFile = newDir(filePath);
            //  sftp.upload(remotePath, upFile, (FileUploadMonitor) fileUploadMonitor, RESUME);
            boolean flag = false;
            String temPokaName = fileName + ".TEM";
            flag = sftp.upload(remotePath, temPokaName, upFile, RESUME);
            if (flag) {//上传成功，备份poka fsn文件
                flag = sftp.rename(temPokaName, fileName, remotePath);
                // boolean setWritable = upFile.setWritable(true);
                if (flag) {
                    File tem = newDir(bakPath + File.separator + date + File.separator + fileName);
                    if (tem.exists()) {
                        tem = new File(bakPath + File.separator + date + File.separator + fileName + "." + (new java.text.SimpleDateFormat("yyyyMMddHHmmssSSS")).format(BundleDeal.getDBTime()));
                    }
                    if (upFile.renameTo(tem)) {//poka fsn文件移到备份目录
                        upFile.delete();
                    }
                } else {
                    String tempFilePath = reUploadPath + File.separator + fileName;
                    File tempFile = newDir(tempFilePath);
                    if (upFile.renameTo(tempFile)) {
                        upFile.delete();
                    }
                }
            } else {//上传失败，将文件存入tem目录下
                String tempFilePath = reUploadPath + File.separator + fileName;
                File tempFile = newDir(tempFilePath);
                if (upFile.renameTo(tempFile)) {
                    upFile.delete();
                }
            }
        }
        sftp.disConnect();
    }

    public static void manyFileUploadFtp(List<String> fileList, String bakDir, FileUploadMonitor fileUploadMonitor) {
        String basePath = StaticVar.cfgMap.get(argPro.localAddr);
        String localFilePath = basePath + File.separator + UploadFtp.tem;
        String remotePath = StaticVar.cfgMap.get(argPro.remoteAddr);
        String bakPath = basePath + File.separator + bakDir;
        String reUploadPath = basePath + File.separator + UploadFtp.reUpload;

        String date = new java.text.SimpleDateFormat("yyyyMMdd").format(new Date());

        File upFile = null;
        PokaSftp sftp = new PokaSftp();
        sftp.connect(StaticVar.cfgMap.get(argPro.ftp), Integer.valueOf(StaticVar.cfgMap.get(argPro.port)), StaticVar.cfgMap.get(argPro.user), StaticVar.cfgMap.get(argPro.pwd), 6000);
        if (sftp.getSftp() == null) {
            for (String fileName : fileList) {
                upFile = newDir(localFilePath + File.separator + fileName);
                String tempFilePath = reUploadPath + File.separator + fileName;
                File tempFile = newDir(tempFilePath);
                if (upFile.renameTo(tempFile)) {
                    upFile.delete();
                }
            }
            return;
        }

        for (String fileName : fileList) {
            String filePath = localFilePath + File.separator + fileName;
            //上传到前置机

            upFile = newDir(filePath);
            //  sftp.upload(remotePath, upFile, (FileUploadMonitor) fileUploadMonitor, RESUME);
            boolean flag = false;
            String temPokaName = fileName + ".TEM";
            fileUploadMonitor.setValue(0);
            fileUploadMonitor.setMaximum((int) upFile.length());
            ((FileUploadMonitor) fileUploadMonitor).setFileSize(upFile.length());
            flag = sftp.upload(remotePath, temPokaName, upFile, fileUploadMonitor, RESUME);
            if (flag) {//上传成功，备份poka fsn文件
                flag = sftp.rename(temPokaName, fileName, remotePath);
                // boolean setWritable = upFile.setWritable(true);
                if (flag) {
                    File tem = newDir(bakPath + File.separator + date + File.separator + fileName);
                    if (tem.exists()) {
                        tem = new File(bakPath + File.separator + date + File.separator + fileName + "." + (new java.text.SimpleDateFormat("yyyyMMddHHmmssSSS")).format(BundleDeal.getDBTime())
                        );
                    }
                    if (upFile.renameTo(tem)) {//poka fsn文件移到备份目录
                        upFile.delete();
                    }
                } else {
                    String tempFilePath = reUploadPath + File.separator + fileName;
                    File tempFile = newDir(tempFilePath);
                    if (upFile.renameTo(tempFile)) {
                        upFile.delete();
                    }
                }
            } else {//上传失败，将文件存入tem目录下
                String tempFilePath = reUploadPath + File.separator + fileName;
                File tempFile = newDir(tempFilePath);
                if (upFile.renameTo(tempFile)) {
                    upFile.delete();
                }
            }
        }
        sftp.disConnect();
    }
    
    /**
     * 上传FSN文件
     * @param resourcePath
     * @param fileName 
     */
    public static void uploadFsnFile(String resourcePath,String fileName){       
        if(StaticVar.cfgMap.get(argPro.isZip).equals(StaticVar.isZip) && StaticVar.cfgMap.get(argPro.isTimer).equals(StaticVar.isTimer)){  
            //0定时、0压缩  生成fsn之后，压缩文件到reupload文件夹中，压缩之后就不管了
            String fileNameNoFsn = fileName.substring(0, fileName.length()-4);  //文件名去除.fsn
            String zipPath = StaticVar.cfgMap.get(argPro.localAddr)+File.separator+ UploadFtp.reUpload + File.separator+fileNameNoFsn+".zip";
            compress(resourcePath,zipPath);
            File delFile = new File(resourcePath);
            if(delFile.exists()){
                delFile.delete();
            }
        }else if(StaticVar.cfgMap.get(argPro.isZip).equals(StaticVar.noZip) && StaticVar.cfgMap.get(argPro.isTimer).equals(StaticVar.isTimer)){
           //0定时，1不压缩 生成fsn文件后，把fsncopy到reupload文件夹中
            File file = new File(resourcePath);
            if(file.exists()){
                String zipPath = StaticVar.cfgMap.get(argPro.localAddr)+File.separator+ UploadFtp.reUpload + File.separator+fileName;
                File dest = new File(zipPath);
                if(file.renameTo(dest)){
//                    file.delete();
                }
            }
        }else if(StaticVar.cfgMap.get(argPro.isZip).equals(StaticVar.isZip) && StaticVar.cfgMap.get(argPro.isTimer).equals(StaticVar.noTimer)){            
           //1实时、0压缩 生成fsn文件后，压缩文件到tem目录，再上传 
            String fileNameNoFsn = fileName.substring(0, fileName.length()-4);  //文件名去除.fsn
            String zipPath = StaticVar.cfgMap.get(argPro.localAddr)+File.separator+ UploadFtp.tem + File.separator+fileNameNoFsn+".zip";
            compress(resourcePath,zipPath);
            File delFile = new File(resourcePath);
            if(delFile.exists()){
                delFile.delete();
            }
            oneFileUploadFtp(fileNameNoFsn+".zip", UploadFtp.fsnbak);
        }else if(StaticVar.cfgMap.get(argPro.isZip).equals(StaticVar.noZip) && StaticVar.cfgMap.get(argPro.isTimer).equals(StaticVar.noTimer)){
            //实时、不压缩 生成fsn文件后，直接上传
            oneFileUploadFtp(fileName, UploadFtp.fsnbak);
        }
    }
    
    /**
     * 上传BK文件
     * @param resourcePath
     * @param fileName 
     */
    public static void uploadBkFile(String resourcePath,String fileName){       
        if(StaticVar.cfgMap.get(argPro.isTimer).equals(StaticVar.isTimer)){
            //0定时、生成文件到reupload文件夹中，就不管了
            File file = new File(resourcePath);
            if(file.exists()){
                String zipPath = StaticVar.cfgMap.get(argPro.localAddr)+File.separator+ UploadFtp.reUpload + File.separator+fileName;
                File dest = new File(zipPath);
                if(file.renameTo(dest)){
//                    file.delete();
                }
            }
        }else if(StaticVar.cfgMap.get(argPro.isTimer).equals(StaticVar.noTimer)){
            //实时 生成bk文件后，直接上传
            oneFileUploadFtp(fileName, UploadFtp.bkbak);
        }        
    }
        
}
