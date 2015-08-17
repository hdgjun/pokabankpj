/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test;

import com.poka.entity.PokaFsn;
import com.poka.util.StringUtil;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class test {
private List<String> myList = new ArrayList<String>();
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket;
            serverSocket = new ServerSocket(2222);
            Socket incomingConnection = null;
            while (true) {
                incomingConnection = serverSocket.accept();
                System.out.println("设备已经连接上，准备接收数据：");
                MyThread12 tt = new MyThread12();
                tt.setIncomingConnection(incomingConnection);
                tt.start();
            }
        } catch (IOException ex) {
            System.out.println("dg");
        }
       
    }
}

class MyThread12 extends Thread {

    Socket incomingConnection = null;

    public Socket getIncomingConnection() {
        return this.incomingConnection;
    }

    public void setIncomingConnection(Socket incomingConnection) {
        this.incomingConnection = incomingConnection;
    }

    public void run() {
        InputStream input = null;
        OutputStream output = null;
        try {

            input = incomingConnection.getInputStream();
            output = incomingConnection.getOutputStream();
            byte[] head = new byte[19];
            int len;
            PokaFsn.readData(head, 19, input);
           // len = input.read(head);
            System.out.println(StringUtil.byteToHexString(head, 0, 19));
            System.out.println(StringUtil.byteToHexString2(head, 0, 19));
           output.write(1);
            incomingConnection.close();
        } catch (IOException ex) {
            Logger.getLogger(MyThread1.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
