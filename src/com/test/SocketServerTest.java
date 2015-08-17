/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test;

import com.poka.socket.AbstractSocketHandle;
import com.poka.socket.LiaolinJulongSocketHandleImpl;
import com.poka.socket.LiaolinJulongSocketHandler;
import com.poka.socket.SocketHandle;
import com.poka.util.StringUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class SocketServerTest {

    private int port = 2222;
    private ServerSocket serverSocket;
    private ExecutorService executorService;//线程池
    private final int POOL_SIZE = 10;//单个CPU线程池大小

    public SocketServerTest() {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException ex) {
            Logger.getLogger(SocketServerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Runtime的availableProcessor()方法返回当前系统的CPU数目.
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * POOL_SIZE);
        System.out.println("服务器启动");
    }

    public void stop() {
        executorService.shutdownNow();

        if (this.serverSocket != null) {
            try {
                this.serverSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(SocketServerTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void service() {
        while (true) {
            Socket socket = null;
            try {
                //接收客户连接,只要客户进行了连接,就会触发accept();从而建立连接
                socket = serverSocket.accept();
                executorService.execute(new Handler(socket));

            } catch (Exception e) {
                System.out.println("ServerSocket closed");
                break;

            }
        }
    }
  //  SocketServerTest ser =  new SocketServerTest();

    public static void main(String[] args) throws IOException {
        SocketServerTest ser = new SocketServerTest();
        ser.service();
        System.out.println("~~~~~~~~~~~~~");
        try {
            Thread.sleep(60000);
        } catch (InterruptedException ex) {
            Logger.getLogger(SocketServerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        ser.stop();
    }

}

class Handler implements Runnable {

    private Socket socket;

    public Handler(Socket socket) {
        this.socket = socket;
    }

    private PrintWriter getWriter(Socket socket) throws IOException {
        OutputStream socketOut = socket.getOutputStream();
        return new PrintWriter(socketOut, true);
    }

    private BufferedReader getReader(Socket socket) throws IOException {
        InputStream socketIn = socket.getInputStream();
        return new BufferedReader(new InputStreamReader(socketIn));
    }

    public String echo(String msg) {
        return "echo:" + msg;
    }

    public void run() {
        try {
            //        try {
//            System.out.println("New connection accepted "+socket.getInetAddress()+":"+socket.getPort());
//            BufferedReader br=getReader(socket);
//            PrintWriter pw=getWriter(socket);
//            String msg=null;
//            while((msg=br.readLine())!=null){
//                System.out.println(msg);
////                pw.println(echo(msg));
////                if(msg.equals("bye"))
////                    break;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }finally{
//            try {
//                if(socket!=null)
//                    socket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        AbstractSocketHandle xDhd = null;
//         SocketHandle sh;
//        sh = new LiaolinJulongSocketHandleImpl(this.socket);
//                    xDhd = new LiaolinJulongSocketHandler(sh, null);
            
            InputStream socketIn = socket.getInputStream();
            int len = 0;
            byte[] data = new byte[1024];
            while(true){
                int a = socketIn.available();
                len = socketIn.read(data, 0, a);
              //  len = readInputStreamWithTimeout(socketIn,data,1000);
                if(len == 0){
                    System.out.println("time out");
                    continue;
                }else if(len == -1){
                    break;
                }
                System.out.println("111"+StringUtil.byteToHexString2(data, 0, len));
            }
        } catch (IOException ex) {
            Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static int readInputStreamWithTimeout(InputStream is, byte[] b, int timeoutMillis)
            throws IOException {
        int bufferOffset = 0;
        long maxTimeMillis = System.currentTimeMillis() + timeoutMillis;
        while (System.currentTimeMillis() < maxTimeMillis && bufferOffset < b.length) {
            int readLength = java.lang.Math.min(is.available(), b.length - bufferOffset);
            // can alternatively use bufferedReader, guarded by isReady():
            int readResult = is.read(b, bufferOffset, readLength);
            if (readResult == -1) {
                break;
            }
            bufferOffset += readResult;
        }
        return bufferOffset;
    }
}
