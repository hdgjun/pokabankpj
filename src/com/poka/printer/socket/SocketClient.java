/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.poka.printer.socket;

import com.poka.util.LogManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class SocketClient {
    static final Logger logger = LogManager.getLogger(SocketClient.class);
    private String ip;
    private int port;
    private Socket socket;
    
    private InputStream inStream;
    private OutputStream outStream;
    
    private PrintWriter outWriter;
    private BufferedReader inReader;
    
    public SocketClient(String ip,int port){
        this.ip = ip;
        this.port = port;
    }

    public boolean connectServer(){
        try {
            //   InetAddress addre = new InetAddress(this.ip);
            this.socket = new Socket(this.ip,this.port);
            this.inStream = this.socket.getInputStream();
            this.outStream = this.socket.getOutputStream();
            
            this.outWriter = new PrintWriter(this.outStream);
            this.inReader = new BufferedReader(new InputStreamReader(this.inStream));
            return true;
        } catch (IOException ex) {
         logger.log(Level.INFO, null,ex);
            return false;
        }    
    }
    
    public void disConnect(){
        try {
            if(this.inStream != null)
                this.inStream.close();
            
            if(this.outStream != null)
                this.outStream.close();
            
            if(this.inReader != null)
                this.inReader.close();
            
            if(this.outWriter !=null)
                this.outWriter.close();
            
            if(this.socket != null)
            this.socket.close();
            
        } catch (IOException ex) {
           logger.log(Level.INFO, null,ex);
        }
    }
    
    public void writeString(String data){
         this.outWriter.write(data);
         this.outWriter.flush();
    }
    /**
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip the ip to set
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @return the socket
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * @param socket the socket to set
     */
    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    /**
     * @return the inStream
     */
    public InputStream getInStream() {
        return inStream;
    }

    /**
     * @param inStream the inStream to set
     */
    public void setInStream(InputStream inStream) {
        this.inStream = inStream;
    }

    /**
     * @return the outStream
     */
    public OutputStream getOutStream() {
        return outStream;
    }

    /**
     * @param outStream the outStream to set
     */
    public void setOutStream(OutputStream outStream) {
        this.outStream = outStream;
    }

    /**
     * @return the outWriter
     */
    public PrintWriter getOutWriter() {
        return outWriter;
    }

    /**
     * @param outWriter the outWriter to set
     */
    public void setOutWriter(PrintWriter outWriter) {
        this.outWriter = outWriter;
    }

    /**
     * @return the inReader
     */
    public BufferedReader getInReader() {
        return inReader;
    }

    /**
     * @param inReader the inReader to set
     */
    public void setInReader(BufferedReader inReader) {
        this.inReader = inReader;
    }
    
    public static void main(String[] args) throws InterruptedException {
//        SocketClient client  = new SocketClient("127.0.0.1",8889);
//        client.connectServer();
//        String tem = null;
//        tem = PostekPple.cleanPictureBuffer();
//        System.out.println("commad:"+tem);
//        client.writeString(tem);
//        
//        //边框
//        tem = PostekPple.printRectangle(60, 60,12 , 844, 1762);
//        client.writeString(tem);
//        
//        tem = PostekPple.printRectangle(118+118+60, 60+60, 12, 118+118+100,60+60+500 );
//        client.writeString(tem);
//        
//        //线条
//        tem = PostekPple.printLine(118+60, 60, 12, 1702);
//        client.writeString(tem);
//        tem = PostekPple.printLine(666+60, 60, 12, 1702);
//        client.writeString(tem);
//
//        tem = PostekPple.printLine(60, 200+60,118 , 12);
//        client.writeString(tem);
//        tem = PostekPple.printLine(60, 200+367+60,118 , 12);
//        client.writeString(tem);
//        tem = PostekPple.printLine(60, 200+367+200+60,118 , 12);
//        client.writeString(tem);
//        tem = PostekPple.printLine(60, 200+367+200+367+60,118 , 12);
//        client.writeString(tem);
//        tem = PostekPple.printLine(60, 200+367+200+367+200+60,118 , 12);
//        client.writeString(tem);
//        
//        //日期
//        tem = PostekPple.pringText(60+5, 60+5, 1, '6', 1, 1,'N', "日 期");
//        client.writeString(tem);
//        tem = PostekPple.pringText(60+5, 60+200+5, 1, '2', 1, 1,'N', "2014-08-14");
//        client.writeString(tem);
//        
//        //封捆员
//        tem = PostekPple.pringText(60+5, 60+200+367+5, 1, '6', 1, 1,'N', "封捆员");
//        client.writeString(tem);
//        tem = PostekPple.pringText(60+5, 60+200+367+200+5, 1, '6', 1, 1,'N', "陈国标");
//        client.writeString(tem);
//        
//        //复核员
//        tem = PostekPple.pringText(60+5, 60+200+367+200+367+5, 1, '6', 1, 1,'N', "复核员");
//        client.writeString(tem);
//        tem = PostekPple.pringText(60+5, 60+200+367+200+367+200+5, 1, '6', 1, 1,'N', "陈国标");
//        client.writeString(tem);
//        
//        //人民币壹佰元卷 tem = PostekPple.printRectangle(118+118+60, 60+60, 12, 118+118+100,60+60+500 );
//        tem = PostekPple.pringText(118+118+60+5, 60+60+10, 1, '6', 1, 1,'N', "人民币壹佰元卷");
//        client.writeString(tem);
//        
//        //壹拾万园整
//        tem = PostekPple.pringText(118+118+60, 60+200+300, 1, '6', 3, 3,'N', "壹拾万园整");
//        client.writeString(tem);
//        
//        //中国工商银行鄂州分行封签
//        tem = PostekPple.pringText(666+5+60, 60+200+250+50, 1, '6', 2, 2,'N', "中国工商银行鄂州分行封签");
//        client.writeString(tem);
//        
//        //编号
//        tem = PostekPple.pringText(118+60+10, 60+200+250, 1, '6', 1, 1,'N', "编号：");
//        client.writeString(tem);
//        tem = PostekPple.pringText(118+60+10, 60+200+250+24+24+24, 1, '6', 1, 1,'N', "123456789123456789");
//        client.writeString(tem);
//        
//        //一维码
//        tem = PostekPple.printOneDimensionalCode(118+60+10, 60+200+250, 1, "K", 3, 5, 118*2, 'N', "123456789123456789");
//        client.writeString(tem);
//        
//        //二维码
//        tem = PostekPple.printTwoDimensionalCode(118+60+10, 200+367+200+367+150, "QR", 0, 0, "o0", "r5", "m0", "g0", "s0", tem);
//        client.writeString(tem);
//        
//        tem = PostekPple.printLabel(1);
//        client.writeString(tem);
//        
//        client.disConnect();
//        System.out.println("disconnect");
         SocketClient client  = new SocketClient("192.168.1.137",2222);
         client.connectServer();
         byte[] by = new byte[]{(byte)0x5A,(byte)0xA5,(byte)0x55,(byte)0xAA,
             0,0,0,0,
             0,0,0,0,
             0,0,0,0};
         int i = 0;
         while(true){
             try {
                 
                 client.outStream.write(by);
               
                // System.out.println("~~~~~~");
                 Thread.sleep(10000);
                 
                 i++;
                 if(i>100)break;
             } catch (IOException ex) {
                logger.log(Level.INFO, null,ex);
             }
         }
         client.disConnect();
        
    }
}
