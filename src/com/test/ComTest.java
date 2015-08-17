/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.test;

import gnu.io.CommPortIdentifier;
import java.util.Enumeration;


/**
 *
 * @author Administrator
 */
public class ComTest {
    public static void main(String args[]){
        Enumeration	ports;
        CommPortIdentifier	portId;
        ports = CommPortIdentifier.getPortIdentifiers();
        if (ports == null)
        {
            System.out.println("No comm ports found!");
            
            return;
        }
        System.out.println("ports.hasMoreElements:"+ports.hasMoreElements());
        while (ports.hasMoreElements())
        {
            /*
            *  Get the specific port
            */
            
            portId = (CommPortIdentifier)
                    ports.nextElement();
            
            System.out.println("portId:"+portId.getName());
        }
    }
}
