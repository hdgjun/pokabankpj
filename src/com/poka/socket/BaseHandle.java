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
public interface BaseHandle {
    public void init(Socket connection, String path);
}
