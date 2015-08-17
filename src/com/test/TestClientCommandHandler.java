/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.test;

import java.io.IOException;
import java.net.SocketTimeoutException;
import org.quickserver.net.server.ClientCommandHandler;
import org.quickserver.net.server.ClientHandler;

/**
 *
 * @author Administrator
 */
public class TestClientCommandHandler implements ClientCommandHandler {

    @Override
    public void handleCommand(ClientHandler ch, String string) throws SocketTimeoutException, IOException {
       
    }
    
}
