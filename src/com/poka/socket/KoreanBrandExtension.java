/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poka.socket;

import com.poka.util.LogManager;
import com.poka.util.StaticVar;
import java.io.IOException;
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
public class KoreanBrandExtension {

    static final Logger logger = LogManager.getLogger(KoreanBrandExtension.class);
    private int listenPort;
    private ServerSocket serverSocket;
    private boolean stopFlag;
    private ExecutorService executorService;//线程池
    private final int POOL_SIZE = 10;//单个CPU线程池大小
    private String path;
    private String handle = "";


    public KoreanBrandExtension(){
         this.stopFlag = false;
    }
    public KoreanBrandExtension(int aListenPort,String path) {
        this.listenPort = aListenPort;
        this.stopFlag = false;
        this.path = path;
    }

    private void acceptConnections() {

        try {
            serverSocket = new ServerSocket(getListenPort());
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * POOL_SIZE);

        Socket incomingConnection = null;
        while (true) {
            try {
                incomingConnection = serverSocket.accept();
                if(this.stopFlag){
                    if(incomingConnection != null){
                        incomingConnection.close();
                    }
                    break;
                }
                
                Class<?> runable = Class.forName(handle);
                BaseHandle thread = (BaseHandle)runable.newInstance();
                thread.init(incomingConnection, path);
                executorService.execute((Runnable)thread);
            } catch (IOException | ClassNotFoundException |InstantiationException | IllegalAccessException  ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
    }

    public void stop() {
        StaticVar.monTcpListen = false;
        if (serverSocket != null) {
            if (!serverSocket.isClosed()) {
                try {
                    this.serverSocket.close();
                } catch (IOException ex) {
                    logger.log(Level.INFO, null, ex);
                }
            }
        }
    }

    public void startAccept() {
        acceptConnections();
    }

    /**
     * @return the listenPort
     */
    public int getListenPort() {
        return listenPort;
    }

    /**
     * @param listenPort the listenPort to set
     */
    public void setListenPort(int listenPort) {
        this.listenPort = listenPort;
    }

    /**
     * @return the serverSocket
     */
    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    /**
     * @param serverSocket the serverSocket to set
     */
    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return the handle
     */
    public String getHandle() {
        return handle;
}

    /**
     * @param handle the handle to set
     */
    public void setHandle(String handle) {
        this.handle = handle;
    }

}
