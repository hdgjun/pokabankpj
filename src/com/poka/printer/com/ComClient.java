/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.poka.printer.com;

import com.poka.printer.PostekPple;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Administrator
 */
public class ComClient {
    private  CommPortIdentifier portID;
    private String comName ;
    private SerialPort port;
    private InputStream inStream;
    private OutputStream outStream;
    private PrintWriter outWriter;
    private BufferedReader inReader;
   
    
    public ComClient(String comName) throws  PortInUseException, IOException, UnsupportedCommOperationException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchPortException{  
//        CommDriver commDriver = (CommDriver) Class.forName("com.sun.comm.Win32Driver").newInstance(); 
//        commDriver.initialize();
        this.comName  = comName;
        System.out.println("comname = "+this.comName);
        this.portID = CommPortIdentifier.getPortIdentifier(this.comName);
        this.port = (SerialPort)portID.open("ComClient", 100000000);
        this.port.setSerialPortParams(9600, 8,1, 0);
        this.outStream = this.port.getOutputStream();
        this.outWriter = new PrintWriter(this.outStream);
        this.inStream = this.port.getInputStream();
    }
    
    public static Enumeration getSerialPorts(){
        return  CommPortIdentifier.getPortIdentifiers();
    }
  
    
    public void close() throws IOException{
        if(this.outStream != null)
            this.outStream.close();
        
        if(this.outWriter != null)
            this.outWriter.close();
        
        if(this.port != null)
            this.port.close();
    }

    public void writeString(String s){
        this.outWriter.write(s);
        this.outWriter.flush();
    }
    public void writeGB2312(String s) throws UnsupportedEncodingException, IOException{
        this.outStream.write(s.getBytes("GB2312"));
        this.outStream.flush();
    }
    public void write(String s) throws UnsupportedEncodingException, IOException{
        this.outStream.write(s.getBytes());
        this.outStream.flush();
    }
    public void write(byte[] s) throws UnsupportedEncodingException, IOException{
        this.outStream.write(s);
        this.outStream.flush();
    }
    
    public void writeChsrs(char[] chs) throws IOException{
        byte[] by = new byte[chs.length];
        int i = 0;
        for(char c : chs){
            by[i++] = (byte)(c&0x00ff);
        }
        this.outStream.write(by);
        this.outStream.flush();
    }
    /**
     * @return the comName
     */
    public String getComName() {
        return comName;
    }

    /**
     * @param comName the comName to set
     */
    public void setComName(String comName) {
        this.comName = comName;
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
     * @return the outStream
     */
    public OutputStream getOutStream() {
        return outStream;
    }

    /**
     * @param outStream the outStream to set
     */
    public void setOutStream(OutputStream outStream) {
        this.outStream = outStream;
    }

    /**
     * @return the outWriter
     */
    public PrintWriter getOutWriter() {
        return outWriter;
    }

    /**
     * @param outWriter the outWriter to set
     */
    public void setOutWriter(PrintWriter outWriter) {
        this.outWriter = outWriter;
    }

    /**
     * @return the inReader
     */
    public BufferedReader getInReader() {
        return inReader;
    }

    /**
     * @param inReader the inReader to set
     */
    public void setInReader(BufferedReader inReader) {
        this.inReader = inReader;
    }
    
    
     public static void main(String[] args) {
        try {
            ComClient client  = new ComClient("COM1");
         
            String tem = null;
            tem = PostekPple.cleanPictureBuffer();
            client.writeString(tem);
            
             tem = PostekPple.printLine(37, 11, 0.4, 30);
            client.writeString(tem);
            
            tem = PostekPple.printSLine(37, 11, 0.4, 56,26);
            client.writeString(tem);
            
            tem = PostekPple.printSLine(56, 26, 0.4, 37,41);
            client.writeString(tem);
            
            tem = PostekPple.pringText(11 + 13 + 2 + 15 + 5 + 3 -2, 5 + 6 + 5.5 , 1, '3', 4, 4, 'N', "100");
            client.write(tem);
            client.write("50");
            client.write("\"\r\n");
            
            tem = PostekPple.printLabel(1);
            client.writeString(tem);
            
            client.close();
          //  System.out.println("disconnect");
        }  catch (PortInUseException ex) {
            Logger.getLogger(ComClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ComClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedCommOperationException ex) {
            Logger.getLogger(ComClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ComClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(ComClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ComClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPortException ex) {
            Logger.getLogger(ComClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
