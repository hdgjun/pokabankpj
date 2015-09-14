package com.poka.util;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.poka.app.util.FileUploadMonitor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PokaSftp {

    public static final int SFTP_DEFAULT_PORT = 22;

    private ChannelSftp sftp;

    private Session sshSession;

    /**
     * 连接sftp服务器
     *
     * @param host 主机
     * @param port 端口
     * @param username 用户名
     * @param password 密码
     * @param timeout 超时
     * @return
     */
    public ChannelSftp connect(String host, int port, String username,
            String password, int timeout) {
        // ChannelSftp sftp = null;
        try {
            JSch jsch = new JSch();
            jsch.getSession(username, host, port);
            this.sshSession = jsch.getSession(username, host, port);
       
            sshSession.setPassword(password);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.setTimeout(timeout);
            sshSession.connect();
            Channel channel = sshSession.openChannel("sftp");
            channel.connect();
            this.sftp = (ChannelSftp) channel;
        } catch (Exception e) {
            this.sftp = null;
            return null;
        }
        return this.sftp;
    }

    public void disConnect() {
        if (this.sftp != null) {
            this.sftp.disconnect();
        }
        if (this.sshSession != null) {
            this.sshSession.disconnect();
        }
    }

    public boolean rename(String oldName, String newName, String path) {
        try {
            this.sftp.cd(path);
            this.sftp.rename(oldName, newName);
            return true;
        } catch (SftpException ex) {
            Logger.getLogger(PokaSftp.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * 上传文件
     *
     * @param directory 上传的目录
     * @param uploadFile 要上传的文件
     * @param sftp
     */
    public void upload(String directory, String uploadFile, FileUploadMonitor fMonitor, int mode) {
        FileInputStream input = null;
        try {
            try {
                this.sftp.cd(directory);
            } catch (SftpException e1) {
                createDir(directory, this.sftp);
                this.sftp.cd(directory);
            }
            File file = new File(uploadFile);
            input = new FileInputStream(file);
            this.sftp.put(input, file.getName(), fMonitor, mode);
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ex) {
                    Logger.getLogger(PokaSftp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void upload(String directory, File uploadFile, FileUploadMonitor fMonitor, int mode) {
        FileInputStream input = null;
        try {
            try {
                this.sftp.cd(directory);
            } catch (SftpException e1) {
                createDir(directory, this.sftp);
                this.sftp.cd(directory);
            }
            input = new FileInputStream(uploadFile);
            this.sftp.put(input, uploadFile.getName(), fMonitor, mode);
            input.close();
        } catch (Exception e) {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ex) {
                    Logger.getLogger(PokaSftp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public boolean upload(String directory, String desName, File uploadFile, FileUploadMonitor fMonitor, int mode) {
        FileInputStream input = null;
        try {
            try {
                this.sftp.cd(directory);
            } catch (SftpException e1) {
                createDir(directory, this.sftp);
                this.sftp.cd(directory);
            }
            input = new FileInputStream(uploadFile);
            this.sftp.put(input, desName, fMonitor, mode);
            input.close();
            return true;
        } catch (Exception e) {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ex) {
                    Logger.getLogger(PokaSftp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return false;
        }
    }

    public boolean upload(String directory, String desName, File uploadFile,  int mode) {
        FileInputStream input = null;
        try {
            try {
                this.sftp.cd(directory);
            } catch (SftpException e1) {
                createDir(directory, this.sftp);
                this.sftp.cd(directory);
            }
            input = new FileInputStream(uploadFile);
            this.sftp.put(input, desName, mode);
            input.close();
            return true;
        } catch (Exception e) {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ex) {
                    Logger.getLogger(PokaSftp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return false;
        }
    }
    
    public void upload(String directory, File uploadFile, int mode) {
        FileInputStream input = null;
        try {
            try {
                this.sftp.cd(directory);
            } catch (SftpException e1) {
                createDir(directory, this.sftp);
                this.sftp.cd(directory);
            }
            input = new FileInputStream(uploadFile);
            this.sftp.put(input, uploadFile.getName(), mode);
            input.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ex) {
                    Logger.getLogger(PokaSftp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * 下载文件
     *
     * @param directory 下载目录
     * @param downloadFile 下载的文件
     * @param saveFile 存在本地的路径
     * @param sftp
     */
    public void download(String directory, String downloadFile, String saveFile) {
        try {
            this.sftp.cd(directory);
            File file = createLDir(saveFile);
            this.sftp.get(downloadFile, new FileOutputStream(file));
        } catch (Exception e) {
           
        }
    }

    /**
     * 删除文件
     *
     * @param directory 要删除文件所在目录
     * @param deleteFile 要删除的文件
     * @param sftp
     */
    public void delete(String directory, String deleteFile) {
        try {
            this.sftp.cd(directory);
            this.sftp.rm(deleteFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 列出目录下的文件
     *
     * @param directory 要列出的目录
     * @param sftp
     * @return
     * @throws SftpException
     */
    public Vector listFiles(String directory) throws SftpException {
        return this.sftp.ls(directory);
    }

    private File createLDir(String filepath) {
        File f = new File(filepath);
        if (!f.exists()) {
            File pf = f.getParentFile();
            if (!pf.exists()) {
                pf.mkdirs();
            }
        }
        return f;
    }

    /**
     * create Directory
     *
     * @param filepath
     * @param sftp
     */
    private void createDir(String filepath, ChannelSftp sftp) {
        boolean bcreated = false;
        boolean bparent = false;
        File file = new File(filepath);
        String ppath = file.getParent().replaceAll("\\\\", "/");
        try {
            sftp.cd(ppath);
            bparent = true;
        } catch (SftpException e1) {
            e1.printStackTrace();
            bparent = false;
        }
        try {
            if (bparent) {
                try {
                    sftp.cd(filepath);
                    bcreated = true;
                } catch (Exception e) {
                    bcreated = false;
                }
                if (!bcreated) {
                    sftp.mkdir(filepath);
                    bcreated = true;
                }
                return;
            } else {
                createDir(ppath, sftp);
                sftp.cd(ppath);
                sftp.mkdir(filepath);
            }
        } catch (SftpException e) {
            System.out.println("mkdir failed :" + filepath);
            e.printStackTrace();
        }

        try {
            sftp.cd(filepath);
        } catch (SftpException e) {
            e.printStackTrace();
            System.out.println("can not cd into :" + filepath);
        }

    }
    //上传reupload中的文件
    public static void uploadFile(){       
            File dir = new File(((String) StaticVar.cfgMap.get(argPro.localAddr)) + File.separator + UploadFtp.reUpload);
            File[] files = dir.listFiles();
            if (files == null) {
                return;
            }
            PokaSftp sftp = new PokaSftp();
            boolean flag = false;
            sftp.connect(StaticVar.cfgMap.get(argPro.ftp), PokaSftp.SFTP_DEFAULT_PORT, StaticVar.cfgMap.get(argPro.user), StaticVar.cfgMap.get(argPro.pwd), 6000);
            if (sftp.getSftp() == null) {
                return;
            }
            String fileType = "";
            for (File f : files) {
                String file = f.getName().trim();
                if (file.endsWith(".FSN") || file.endsWith(".FSN.zip")) {
                    fileType = UploadFtp.fsnbak;                  
                } else if (file.endsWith(".BK")) {
                    fileType = UploadFtp.bkbak;
                } else if (file.endsWith(".CT")) {
                    fileType = UploadFtp.ctbak;
                } else if (file.endsWith(".BF")) {
                    fileType = UploadFtp.bfbak;
                }
                flag = UploadFtp.againFileUploadFtp(file, fileType, sftp);
                if (flag) {//上传成功，备份poka fsn文件

                } else {//上传失败，不处理poka fsn文件

                }
            }
            sftp.disConnect();
    }
    public ChannelSftp getSftp() {
        return sftp;
    }

    public void setSftp(ChannelSftp sftp) {
        this.sftp = sftp;
    }

    public Session getSshSession() {
        return sshSession;
    }

    public void setSshSession(Session sshSession) {
        this.sshSession = sshSession;
    }

}
