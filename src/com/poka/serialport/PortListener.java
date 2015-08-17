/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poka.serialport;

import com.poka.util.LogManager;
import com.poka.util.StringUtil;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.TooManyListenersException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextField;

/**
 *
 * @author poka
 */
public class PortListener implements SerialPortEventListener {

    static final Logger logger = LogManager.getLogger(PortListener.class);
    private CommPortIdentifier portID;
    private String comName;
    private SerialPort port;
    private InputStream inStream;
    private OutputStream outStream;
    private PrintWriter outWriter;
    private BufferedReader inReader;
    private String rightAddr = "";

    private JTextField curTextField;

    private static boolean rcvFlag = false;
    private static int curLen = 0;
    private static byte[] rcvData = new byte[10000];
    private static int dataLen = 0;

    public PortListener(String comName, String rightAddr) throws PortInUseException, IOException, UnsupportedCommOperationException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchPortException {
        try {
//            CommDriver commDriver = (CommDriver) Class.forName("com.sun.comm.Win32Driver").newInstance();
//            commDriver.initialize();
            this.comName = comName;
            this.rightAddr = rightAddr;
            System.out.println("comname = " + this.comName);
            this.portID = CommPortIdentifier.getPortIdentifier(this.comName);
            this.port = (SerialPort) portID.open("ComClient", 100000000);
            this.port.setSerialPortParams(9600, 8, 1, 0);
            this.port.addEventListener(this);
            this.port.notifyOnDataAvailable(true);
            this.inStream = this.port.getInputStream();
            this.outStream = this.port.getOutputStream();
        } catch (TooManyListenersException ex) {
          logger.log(Level.INFO, null,ex);
        }
    }

    public void close() {
        try {
            if (this.outStream != null) {
                this.outStream.close();
            }

            if (this.outWriter != null) {
                this.outWriter.close();
            }

            if (this.port != null) {
                this.port.close();
            }
        } catch (IOException ex) {
          logger.log(Level.INFO, null,ex);
        }
    }

    /**
     * 监听事件
     *
     * @param evt
     */
    @Override
    public void serialEvent(SerialPortEvent evt) {
        System.out.println("evt.getEventType():" + evt.getEventType());
        switch (evt.getEventType()) {
            case SerialPortEvent.CTS:
                System.out.println("CTS event occured.");
                break;
            case SerialPortEvent.CD:
                System.out.println("CD event occured.");
                break;
            case SerialPortEvent.BI:
                System.out.println("BI event occured.");
                break;
            case SerialPortEvent.DSR:
                System.out.println("DSR event occured.");
                break;
            case SerialPortEvent.FE:
                System.out.println("FE event occured.");
                break;
            case SerialPortEvent.OE:
                System.out.println("OE event occured.");
                break;
            case SerialPortEvent.PE:
                System.out.println("PE event occured.");
                break;
            case SerialPortEvent.RI:
                System.out.println("RI event occured.");
                break;
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                System.out.println("OUTPUT_BUFFER_EMPTY event occured.");
                break;
            case SerialPortEvent.DATA_AVAILABLE:

                try {
                    this.inStream.read(PortListener.rcvData, PortListener.curLen, 1);
                    if (!PortListener.rcvFlag) {
                        if (PortListener.curLen == 0) {
                            if (PortListener.rcvData[0] == 0x55) {
                                PortListener.curLen++;
                            }
                        } else if (PortListener.curLen == 1) {
                            if (PortListener.rcvData[1] == 0x7A) {
                                PortListener.curLen++;
                            } else {
                                PortListener.curLen = 0;
                            }
                        } else {
                            PortListener.curLen++;
                            PortListener.rcvFlag = true;
                            PortListener.dataLen = (int) PortListener.rcvData[2];
                        }

                    } else {
                        PortListener.curLen++;
                        if (PortListener.curLen == (PortListener.dataLen + 2)) {
                            System.out.println(StringUtil.byteToHexString(rcvData, 0, curLen));

                            DealWithData(rcvData);

                            PortListener.curLen = 0;
                            PortListener.rcvFlag = false;
                            PortListener.dataLen = 0;
                        }
                    }
                } catch (IOException ex) {
                   logger.log(Level.INFO, null,ex);
                }

                break;
        }
    }

    public void DealWithData(byte[] data) {
        if (VoilateData(data)) {
            try {
                //            this.outStream
                int codeLen = (int) data[12];
                String code = StringUtil.byteToString2(data, 13, codeLen);
                System.out.println(code);
//              this.curTextField.setText(code);
                Robot robot = new Robot();
                for (int i = 13; i < PortListener.curLen - 2; i++) {
                    robot.keyPress(data[i]);
                    robot.keyRelease(data[i]);
                }
                robot.keyPress(KeyEvent.VK_ENTER);
                robot.keyRelease(KeyEvent.VK_ENTER);

                byte tem;
                tem = data[4];
                data[4] = data[6];
                data[6] = tem;
                tem = data[5];
                data[5] = data[7];
                data[7] = tem;

                data[11] = 0;
                data[12] = 1;
                data[13] = 0x41;
                data[14] = 0;
                data[10] = 4;

                data[2] = 15;
                byte[] crc = getCrc(data, 2, 15);
                data[15] = crc[0];
                data[16] = crc[1];

                try {
                    System.out.println(StringUtil.byteToHexString(data, 0, 17));
                    this.outStream.write(data, 0, 17);
                } catch (IOException ex) {
                   logger.log(Level.INFO, null,ex);
                }
            } catch (AWTException ex) {
                logger.log(Level.INFO, null,ex);
            }
        } else {
            return;
        }
    }

    public byte[] getCrc(byte[] data, int start, int len) {
        int tems = 0;
        int temp = 0;
        for (int i = start; i < len; i++) {
            temp += data[i];
            tems = tems + (temp % 0x10000);
        }
        byte[] crc = new byte[2];
        crc[0] = (byte) (tems & 0x000000FF);
        crc[1] = (byte) ((tems >> 8) & 0x000000FF);
        return crc;
    }

    public boolean VoilateData(byte[] data) {
        int tems = 0;
        int temp = 0;
        int addr = 0;
        addr = ((int) data[5]) * 256 + data[4];
        
        int addTem = Integer.parseInt(rightAddr);
        if(addr != addTem){
            return false;
        }
        
        for (int i = 2; i < PortListener.dataLen; i++) {
            temp += data[i];
            tems = tems + (temp % 0x10000);
        }
        byte[] crc = new byte[2];
        crc[0] = (byte) (tems & 0x000000FF);
        crc[1] = (byte) ((tems >> 8) & 0x000000FF);

        if (data[PortListener.curLen - 2] == crc[0] && data[PortListener.curLen - 1] == crc[1]) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return the inStream
     */
    public InputStream getInStream() {
        return inStream;
    }

    /**
     * @param inStream the inStream to set
     */
    public void setInStream(InputStream inStream) {
        this.inStream = inStream;
    }

    /**
     * @return the curTextField
     */
    public JTextField getCurTextField() {
        return curTextField;
    }

    /**
     * @param curTextField the curTextField to set
     */
    public void setCurTextField(JTextField curTextField) {
        this.curTextField = curTextField;
    }

    /**
     * @return the rightAddr
     */
    public String getRightAddr() {
        return rightAddr;
    }

    /**
     * @param rightAddr the rightAddr to set
     */
    public void setRightAddr(String rightAddr) {
        this.rightAddr = rightAddr;
    }

}
