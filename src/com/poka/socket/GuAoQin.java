/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poka.socket;

import com.poka.entity.PokaFsn;
import com.poka.entity.PokaFsnBody;
import com.poka.util.LogManager;
import com.poka.util.StringUtil;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class GuAoQin implements Runnable, BaseHandle {

    static final Logger logger = LogManager.getLogger(GuAoQin.class);
    private Socket connection;
    private String path;

    private final int CMD_TIME = 0x41;
    private final int CMD_PORT_MODE = 0x42;
    private final int CMD_START = 0x51;
    private final int CMD_STOP = 0x52;
    private final int CMD_DATA = 0x30;
    private final int CMD_DATA_NO_PI = 0x31;

    @Override
    public void run() {
        InputStream input = null;
        OutputStream output = null;
        int iRet;
        String sTem;
        try {
            input = connection.getInputStream();
            output = connection.getOutputStream();
            while (true) {
                byte[] end = new byte[4];
                byte[] data1 = new byte[10];
                iRet = input.read(data1, 0, 10);
                if (10 != iRet) {
                    break;
                }
                byte[] cmd_byte = new byte[2];
                iRet = input.read(cmd_byte, 0, 2);
                if (10 != iRet) {
                    break;
                }
                int cmd_int = StringUtil.byteToInt16(cmd_byte, 0);
                switch (cmd_int) {
                    case CMD_TIME: {
                        input.read(end, 0, 4);
                        output.write(cmd_byte);
                        output.write(new java.text.SimpleDateFormat("yyyyMMddhhnnss").format(new Date()).getBytes("utf-8"));
                    }
                    break;
                    case CMD_PORT_MODE: {
                        byte[] portNum = new byte[16];
                        input.read(portNum, 0, 16);
                        input.read(end, 0, 4);
                        output.write(cmd_byte);
                        output.write(new byte[]{0, 0});
                    }
                    break;
                    case CMD_START: {
                        input.read(end, 0, 4);
                        output.write(cmd_byte);
                    }
                    break;
                    case CMD_STOP: {
                        input.read(end, 0, 4);
                        output.write(cmd_byte);
                    }
                    break;
                    case CMD_DATA:
                    case CMD_DATA_NO_PI: {
                        PokaFsn temFsn = new PokaFsn();
                        if (!temFsn.readBaseFsnFile(input, cmd_byte[0])) {
                            output.write(cmd_byte);
                            output.write(new byte[]{-1,-1});
                            break;
                        }
                         input.read(end, 0, 4);
                         output.write(new byte[]{0, 0});
                         String date = new java.text.SimpleDateFormat("yyyyMMddhhnnss").format(new Date());
                         String fsnPath1 = path+File.separator+date+"_"+"1"+".FSN";
                         String fsnPath2 = path+File.separator+date+"_"+"2"+".FSN";
                         PokaFsn liutong = new PokaFsn();
                         PokaFsn cang = new PokaFsn();
                         for(PokaFsnBody item:temFsn.getbList()){
                             byte[] cashPort = new byte[2];
                             StringUtil.stringToByte2(item.getReservel(), cashPort, 0);
                             if(3 == cashPort[1]){
                                 cang.add(item);
                             }else{
                                 liutong.add(item);
                             }
                         }
                         liutong.writeBaseFsnFile(fsnPath1);
                         cang.writeBaseFsnFile(fsnPath2);
                    }
                    break;
                }
                break;
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    @Override
    public void init(Socket connection, String path) {
        this.setConnection(connection);
        this.setPath(path);
    }

    /**
     * @return the connection
     */
    public Socket getConnection() {
        return connection;
    }

    /**
     * @param connection the connection to set
     */
    public void setConnection(Socket connection) {
        this.connection = connection;
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

}
