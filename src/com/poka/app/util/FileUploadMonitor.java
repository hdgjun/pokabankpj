/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.poka.app.util;

import com.jcraft.jsch.SftpProgressMonitor;
import javax.swing.JProgressBar;
import javax.swing.Timer;

/**
 *
 * @author Administrator
 */
public class FileUploadMonitor extends  JProgressBar implements SftpProgressMonitor{

    private long progressInterval = 5 * 1000; // 默认间隔时间为5秒
    
    private boolean isEnd = false; // 记录传输是否结束
    
    private long transfered; // 记录已传输的数据总大小
    
    private long fileSize; // 记录文件总大小
    
    private Timer timer; // 定时器对象
    
    private boolean isScheduled = false; // 记录是否已启动timer记时器
    
    public FileUploadMonitor() {
    }
    public FileUploadMonitor(long fileSize) {
        this.fileSize = fileSize;
    }
    
    @Override
    public void init(int i, String string, String string1, long l) {
       this.setValue(0);
       this.setMaximum((int) this.fileSize);
       this.setMinimum(0);
       this.transfered = 0;
       setEnd(false);
    }

//    private void sendProgressMessage(long transfered) {
//        if (fileSize != 0) {
//            double d = ((double)transfered * 100)/(double)fileSize;
//            DecimalFormat df = new DecimalFormat( "#.##"); 
//            System.out.println("Sending progress message: " + df.format(d) + "%");
//        } else {
//            System.out.println("Sending progress message: " + transfered);
//        }
//    }
    
    @Override
    public boolean count(long count) {
        
        if (isEnd()) return false;
        add(count);
        if(this.fileSize == this.getValue()){
            end();
        }
        return true;
    }

    @Override
    public void end() {
       setEnd(true);
       System.out.println("transfering end.");
    }
    
    private synchronized void add(long count) {
        transfered = transfered + count;
        this.setValue((int) transfered);
    }
    
    private synchronized long getTransfered() {
        return transfered;
    }
    
    public synchronized void setTransfered(long transfered) {
        this.transfered = transfered;
    }
    
    private synchronized void setEnd(boolean isEnd) {
        this.isEnd = isEnd;
    }
    
    private synchronized boolean isEnd() {
        return isEnd;
    }

    /**
     * @param fileSize the fileSize to set
     */
    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
    
}
