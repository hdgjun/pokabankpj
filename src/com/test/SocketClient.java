/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test;

import com.poka.entity.PokaFsn;
import com.poka.entity.PokaFsnBody;
import com.poka.util.StringUtil;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class SocketClient {

    private String ip;
    private int port;
    private Socket socket;

    private InputStream inStream;
    private OutputStream outStream;

    private PrintWriter outWriter;
    private BufferedReader inReader;

    public SocketClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public boolean connectServer() {
        try {
            //   InetAddress addre = new InetAddress(this.ip);
            this.socket = new Socket();

            SocketAddress address = new InetSocketAddress(this.ip, this.port);

            this.socket.connect(address, 10000);

            this.inStream = this.socket.getInputStream();
            this.outStream = this.socket.getOutputStream();

            this.outWriter = new PrintWriter(this.outStream);
            this.inReader = new BufferedReader(new InputStreamReader(this.inStream));
            return true;
        } catch (IOException ex) {
            //   Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public void disConnect() {
        try {
            if (this.inStream != null) {
                this.inStream.close();
            }

            if (this.outStream != null) {
                this.outStream.close();
            }

            if (this.inReader != null) {
                this.inReader.close();
            }

            if (this.outWriter != null) {
                this.outWriter.close();
            }

            if (this.socket != null) {
                this.socket.close();
            }

        } catch (IOException ex) {
            Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void writeString(String data) {
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

    public static void main(String[] args) {

        byte[] head = new byte[60];
        byte[] tail = new byte[6];
        byte[] bf = new byte[1024];
        Arrays.fill(head, (byte) 3);
        Arrays.fill(tail, (byte) 3);
        File dir = new File("D:\\fsn\\JULONGFILE");
        int len;
        File[] files = dir.listFiles();
        DataOutputStream dos = null;
        FileInputStream fis = null;
        byte[] sendBytes = null;
        int length = 0;
        double sumL = 0;
        for (File f : files) {
            try {
                System.out.println("path ="+f.getAbsolutePath().toString());
                SocketClient client = new SocketClient("127.0.0.1", 2222);
                client.connectServer();
               
                OutputStream socketOut = client.outStream;
                dos = new DataOutputStream(client.outStream);
                fis = new FileInputStream(f);
                socketOut.write(head, 0, 60);
                long l = f.length();
                sendBytes = new byte[1024];
                while ((length = fis.read(sendBytes, 0, sendBytes.length)) > 0) {
                    sumL += length;
                    dos.write(sendBytes, 0, length);
                    dos.flush();
                }

                socketOut.write(tail, 0, 6);
                byte[] red = new byte[2];
                client.inStream.read(red);
                //  Thread.sleep(1000000);
            } catch (IOException ex) {
                ex.printStackTrace();
                Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public byte[] combine(byte[] b1, byte[] b2) {
        byte[] re = new byte[19];
        int lo = 0;
        re[lo++] = b1[0];
        re[lo++] = b1[1];
        re[lo++] = b1[2];
        re[lo++] = b1[3];
        re[lo++] = b1[4];
        re[lo++] = b2[0];
        re[lo++] = b2[1];
        re[lo++] = b2[2];
        re[lo++] = b2[3];
        re[lo++] = b2[4];
        re[lo++] = b2[5];
        re[lo++] = b2[6];
        re[lo++] = b2[7];
        re[lo++] = b2[8];
        re[lo++] = b2[9];
        re[lo++] = b2[10];
        re[lo++] = b2[11];
        re[lo++] = b2[12];
        re[lo++] = b2[13];

        return re;
    }

    public static byte[] intToBytes(int data) {
        byte[] by = new byte[4];
        by[0] = (byte) (data & 0x000000FF);
        by[1] = (byte) ((data & 0x0000FF00) >> 8);
        by[2] = (byte) ((data & 0x00FF0000) >> 16);
        by[3] = (byte) ((data & 0xFF000000) >> 24);
        return by;

    }
}

class MyThread1 extends Thread {

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
            byte[] head = new byte[1024];
            int len;
//            byte[] data={(byte)0x00,(byte)0x55,(byte)0xAA,(byte)0xFF,
//                         (byte)0x00,(byte)0x00,(byte)0x00,(byte)0xF0,
//                         (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
//                         (byte)0x04,(byte)0x00,(byte)0x00,(byte)0x00};
//             byte[] data={(byte)0xFF,(byte)0xAA,(byte)0x55,(byte)0x00,
//                         (byte)0xF0,(byte)0x00,(byte)0x00,(byte)0x03,
//                         (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x0E,
//                         (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x0E};
//             output.write(data);

            String cmd = "G001";

            String data = "20141101203045";

            output.write(cmd.getBytes());
            output.write(4);
            output.write("0000".getBytes());

            output.write(cmd.getBytes());
            output.write(14);
            output.write(data.getBytes());

            len = input.read(head);
            System.out.println("len = " + len);
            System.out.println(StringUtil.byteToHexString2(head, 0, len));
            len = input.read(head);
            System.out.println("len = " + len);
            System.out.println(StringUtil.byteToHexString2(head, 0, len));
//            while (true) {
//                len = input.read(head);
//                System.out.println("len = " + len);
//                System.out.println(StringUtil.byteToHexString2(head, 0,len));
//                if(len<0)
//                    break;
//            }
        } catch (IOException ex) {
            Logger.getLogger(MyThread1.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
