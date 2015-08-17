/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poka.util;

import com.poka.entity.PokaFsn;
import com.test.SocketClient;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class LoginUtil {
    
    public static LoginResult login(String ip, String port, String user, String passwd) {
        LoginResult res = new LoginResult();
        res.setFlag(false);
        SocketClient client = new SocketClient(ip.trim(), Integer.parseInt(port.trim()));
        if(!client.connectServer()){
            res.setMsg("连接前置机超时!");
            return res;
        }
        
        
        res.setLoginCode(user);
        if (passwd.length() != 6) {
            res.setMsg("密码错误!");
            return res;
        }
        String tem = user + passwd;
        byte[] send_head = new byte[]{0x55, 0x7A, 0x55, 0x7A, 0x41};
        byte[] send_data = byteMerger(send_head, tem.getBytes());
        byte[] get_data = new byte[17];
       // boolean flag = false;
        try {
            client.getOutStream().write(send_data);

            PokaFsn.readData(get_data, 17, client.getInStream());

            if (get_data[1] == 1) {
                res.setFlag(true);
            }
            res.setLoginName(new String(get_data,2,15,"utf-8"));
            client.disConnect();

        } catch (IOException ex) {
            res.setMsg("通讯错误!");
            res.setFlag(false);
            return res;
        }
        return res;
    }

    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
        byte[] byte_3 = new byte[byte_1.length + byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }
}
