/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poka.socket;

/**
 *
 * @author Administrator
 */
public class SocketHandleFactory {

    private AbstractSocketHandle abstractSocketHandle;

    private static SocketHandleFactory socketHandleFactory;

    private SocketHandleFactory(AbstractSocketHandle abstractSocketHandle) {

        this.abstractSocketHandle = abstractSocketHandle;

    }

    public static SocketHandleFactory getInstance(
            AbstractSocketHandle abstractSocketHandle) {

        return socketHandleFactory = new SocketHandleFactory(
                abstractSocketHandle);

    }

    public void receData() throws Exception {
        abstractSocketHandle.receData();
    }
}
