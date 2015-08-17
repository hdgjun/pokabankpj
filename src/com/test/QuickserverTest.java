/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test;

import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.quickserver.net.AppException;
import org.quickserver.net.server.QuickServer;

/**
 *
 * @author Administrator
 */
public class QuickserverTest {

    public static String VER = "1.0";

    public static void main(String s[]) {
        String cmdHandle = "com.test.TestClientCommandHandler";
        String objHandle = "dateserver.ObjectHandler";
        String auth = null;

        QuickServer myServer = new QuickServer(cmdHandle);
        myServer.setClientAuthenticationHandler(auth);
        myServer.setPort(8125);
        myServer.setName("Date Server v " + VER);
        myServer.setClientObjectHandler(objHandle);

        //ony blocking mode is supported for exchanging Object
        myServer.getBasicConfig().getServerMode().setBlocking(true);
        myServer.getBasicConfig().getAdvancedSettings().setDebugNonBlockingMode(true);
        myServer.getBasicConfig().setCommunicationLogging(true);

        //setup logger to log to file
        Logger logger = null;
        FileHandler txtLog = null;
        File log = new File("./log/");
        if (!log.canRead()) {
            log.mkdir();
        }
        try {
            logger = Logger.getLogger("");
            logger.setLevel(Level.FINEST);

            logger = Logger.getLogger("dateserver");
            logger.setLevel(Level.FINEST);

            txtLog = new FileHandler("log/DateServer.txt");
            txtLog.setLevel(Level.FINEST);
            txtLog.setFormatter(new org.quickserver.util.logging.SimpleTextFormatter());
            logger.addHandler(txtLog);

            myServer.setAppLogger(logger); //imp

            //myServer.setConsoleLoggingToMicro();
            myServer.setConsoleLoggingFormatter(
                    "org.quickserver.util.logging.SimpleTextFormatter");
            myServer.setConsoleLoggingLevel(Level.WARNING);
        } catch (Exception e) {
            System.err.println("Could not create xmlLog FileHandler : " + e);
        }
        //end of logger code

        try {
            myServer.startServer();

            myServer.getQSAdminServer().setShellEnable(true);
            myServer.startQSAdminServer();
        } catch (AppException e) {
            System.out.println("Error in server : " + e);
            e.printStackTrace();
        }
    }
}
