/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.poka.socket;

import java.net.Socket;

/**
 *
 * @author Administrator
 */
public class XingDaSocketHandleImpl implements SocketHandle{
    private Socket socket;

    XingDaSocketHandleImpl(Socket socket){
        this.socket = socket;
    }
    /**
     * @return the socket
     */
    @Override
    public Socket getSocket() {
        return socket;
    }
}
