/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.poka.socket;

import com.poka.util.LogManager;
import java.net.Socket;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class LiaolinJulongSocketHandleImpl implements SocketHandle{
    
    private Socket socket;
    
    LiaolinJulongSocketHandleImpl(Socket socket){
        this.socket = socket;
    }
    
    @Override
    public Socket getSocket() {
        return socket;
    }
    
}
