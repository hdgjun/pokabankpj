/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poka.socket;

import com.poka.util.LogManager;
import com.poka.util.StringUtil;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class KoreanBrandExtensionHandler implements Runnable {

    static final Logger logger = LogManager.getLogger(KoreanBrandExtensionHandler.class);
    
    private Socket connection;
    private String path;

    public KoreanBrandExtensionHandler(Socket connection, String path) {
        this.connection = connection;
        this.path = path;
    }

    public void handleConnection() {
        Request request = new Request();
        request.receiveData(connection, path);
    }

    @Override
    public void run() {
        handleConnection();
    }

    class Request {

        private byte uploadType;
        private byte endFlag;
        private byte compressFlag;
        private int filelen;
        private int fileoffset;
        private int operatorIdLen;
        private String operatorId;
        private int filenameLen;
        private String filename;
        private int dataLen;

        public void receiveData(Socket connection, String path) {
            InputStream input = null;
            OutputStream output = null;
            int iRet;
            Respose resp = new Respose();
            try {
                input = connection.getInputStream();
                output = connection.getOutputStream();
                while (true) {
                    byte[] data1 = new byte[4];
                    iRet = input.read(data1, 0, 3);
                    setUploadType(data1[0]);
                    setEndFlag(data1[1]);
                    setCompressFlag(data1[3]);
                    
                    logger.info("uploadType:"+this.getUploadType()+" \nendflag:"+this.getEndFlag()+"\ncompressflag:"+this.getCompressFlag());
                    
                    iRet = input.read(data1);
                    setFilelen(StringUtil.byteToInt32(data1, 0));
                    logger.info("fileLen:"+this.getFilelen());
                    
                    iRet = input.read(data1);
                    setFileoffset(StringUtil.byteToInt32(data1, 0));
                    logger.info("fileoffset:"+this.getFileoffset());
                    
                    iRet = input.read(data1, 0, 2);
                    setOperatorIdLen(StringUtil.byteToInt16(data1, 0));
                    logger.info("operatorIdLen:"+this.getOperatorIdLen());
                    
                    byte[] opId = new byte[getOperatorIdLen()];
                    iRet = input.read(opId);
                    setOperatorId(new String(opId, "utf-8"));
                    
                    logger.info("operatorid:"+this.getOperatorId());

                    iRet = input.read(data1, 0, 2);
                    setFilenameLen(StringUtil.byteToInt16(data1, 0));

                    logger.info("filenameLen:"+this.getFilenameLen());
                    
                    
                    byte[] fName = new byte[getFilenameLen()];
                    iRet = input.read(fName);
                    setFilename(new String(fName, "utf-8"));
                    
                    logger.info("filename:"+this.getFilename());

                    iRet = input.read(data1, 0, 2);
                    setDataLen(StringUtil.byteToInt16(data1, 0));

                    RandomAccessFile randomFile = new RandomAccessFile(path + File.separator + this.getFilename() + ".tem", "rw");
                    // 文件长度，字节数
                    long fileLength = randomFile.length();
                    //将写文件指针移到文件尾。
                    randomFile.seek(fileLength);
                    byte[] data2 = new byte[this.getDataLen()];
                    input.read(data2);
                    randomFile.write(data2);
                    randomFile.close();

                    resp.setRetcode(0);
                    resp.setFileoffset((int) (fileLength + this.getDataLen()));
                    resp.setFilenameLen(filenameLen);
                    resp.setFilename(filename);
                    resp.sendMessage(output);

                    if (0x00 != endFlag) {
                        File f = new File(path + File.separator + this.getFilename() + ".tem");
                        f.renameTo(new File(path + File.separator + this.getFilename()));
                        break;
                    }

                }

            } catch (IOException ex) {
                Logger.getLogger(KoreanBrandExtensionHandler.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (IOException ex) {
                        Logger.getLogger(KoreanBrandExtensionHandler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        /**
         * @return the uploadType
         */
        public byte getUploadType() {
            return uploadType;
        }

        /**
         * @param uploadType the uploadType to set
         */
        public void setUploadType(byte uploadType) {
            this.uploadType = uploadType;
        }

        /**
         * @return the endFlag
         */
        public byte getEndFlag() {
            return endFlag;
        }

        /**
         * @param endFlag the endFlag to set
         */
        public void setEndFlag(byte endFlag) {
            this.endFlag = endFlag;
        }

        /**
         * @return the compressFlag
         */
        public byte getCompressFlag() {
            return compressFlag;
        }

        /**
         * @param compressFlag the compressFlag to set
         */
        public void setCompressFlag(byte compressFlag) {
            this.compressFlag = compressFlag;
        }

        /**
         * @return the filelen
         */
        public int getFilelen() {
            return filelen;
        }

        /**
         * @param filelen the filelen to set
         */
        public void setFilelen(int filelen) {
            this.filelen = filelen;
        }

        /**
         * @return the fileoffset
         */
        public int getFileoffset() {
            return fileoffset;
        }

        /**
         * @param fileoffset the fileoffset to set
         */
        public void setFileoffset(int fileoffset) {
            this.fileoffset = fileoffset;
        }

        /**
         * @return the operatorIdLen
         */
        public int getOperatorIdLen() {
            return operatorIdLen;
        }

        /**
         * @param operatorIdLen the operatorIdLen to set
         */
        public void setOperatorIdLen(int operatorIdLen) {
            this.operatorIdLen = operatorIdLen;
        }

        /**
         * @return the operatorId
         */
        public String getOperatorId() {
            return operatorId;
        }

        /**
         * @param operatorId the operatorId to set
         */
        public void setOperatorId(String operatorId) {
            this.operatorId = operatorId;
        }

        /**
         * @return the filenameLen
         */
        public int getFilenameLen() {
            return filenameLen;
        }

        /**
         * @param filenameLen the filenameLen to set
         */
        public void setFilenameLen(int filenameLen) {
            this.filenameLen = filenameLen;
        }

        /**
         * @return the filename
         */
        public String getFilename() {
            return filename;
        }

        /**
         * @param filename the filename to set
         */
        public void setFilename(String filename) {
            this.filename = filename;
        }

        /**
         * @return the dataLen
         */
        public int getDataLen() {
            return dataLen;
        }

        /**
         * @param dataLen the dataLen to set
         */
        public void setDataLen(int dataLen) {
            this.dataLen = dataLen;
        }

    }

    class Respose {

        private int retcode;
        private int fileoffset;
        private int filenameLen;
        private String filename = "";

        public Respose() {

        }

        public Respose(int retcode, int fileoffset, int filenameLen, String filename) {
            this.retcode = retcode;
            this.fileoffset = fileoffset;
            this.filenameLen = filenameLen;
            this.filename = filename;
        }

        public void sendMessage(OutputStream output) throws IOException {
            output.write(StringUtil.intToByte(retcode));
            output.write(StringUtil.intToByte(fileoffset));
            output.write(StringUtil.intToByte16(filenameLen));
            output.write(filename.getBytes("utf-8"));
        }

        /**
         * @return the retcode
         */
        public int getRetcode() {
            return retcode;
        }

        /**
         * @param retcode the retcode to set
         */
        public void setRetcode(int retcode) {
            this.retcode = retcode;
        }

        /**
         * @return the fileoffset
         */
        public int getFileoffset() {
            return fileoffset;
        }

        /**
         * @param fileoffset the fileoffset to set
         */
        public void setFileoffset(int fileoffset) {
            this.fileoffset = fileoffset;
        }

        /**
         * @return the filenameLen
         */
        public int getFilenameLen() {
            return filenameLen;
        }

        /**
         * @param filenameLen the filenameLen to set
         */
        public void setFilenameLen(int filenameLen) {
            this.filenameLen = filenameLen;
        }

        /**
         * @return the filename
         */
        public String getFilename() {
            return filename;
        }

        /**
         * @param filename the filename to set
         */
        public void setFilename(String filename) {
            this.filename = filename;
        }

    }

}
