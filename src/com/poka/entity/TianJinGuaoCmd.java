/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poka.entity;

import com.poka.util.LogManager;
import com.poka.util.StringUtil;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class TianJinGuaoCmd {
    static final Logger logger = LogManager.getLogger(TianJinGuaoCmd.class);
    private final byte[] cmdCheckData = new byte[] {(byte)0x00,(byte)0x00,(byte)0x00,(byte)0xF0};
  //  private final byte[] cmdCheckData = new byte[] {(byte)0xF0,(byte)0x00,(byte)0x00,(byte)0x00};
    
    private final byte[] cmdReadData =  new byte[] {(byte)0x01,(byte)0x00,(byte)0x00,(byte)0xF0};
    private final byte[] cmdReadDataAfterTime =  new byte[] {(byte)0x02,(byte)0x00,(byte)0x00,(byte)0xF0};
    private final byte[] cmdSetTime =  new byte[] {(byte)0x03,(byte)0x00,(byte)0x00,(byte)0xF0};
    private final byte[] cmdSetUser =  new byte[] {(byte)0x04,(byte)0x00,(byte)0x00,(byte)0xF0};
    private final byte[] cmdSetMechina = new byte[] {(byte)0x05,(byte)0x00,(byte)0x00,(byte)0xF0};
    private final byte[] cmdSetBank =  new byte[] {(byte)0x06,(byte)0x00,(byte)0x00,(byte)0xF0};
    private final byte[] cmdReturn =  new byte[] {(byte)0x07,(byte)0x00,(byte)0x00,(byte)0xF0};
    
  //  private final byte[] cmdHead = new byte[] {(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00};
    private final byte[] cmdHead = new byte[] {(byte)0x00,(byte)0x55,(byte)0xAA,(byte)0xFF};
    private final byte[] cmdReady =  new byte[] {(byte)0xAA,(byte)0xAA,(byte)0xAA,(byte)0xAA};
    private final byte[] cmdErr =  new byte[] {(byte)0x55,(byte)0x55,(byte)0x55,(byte)0x55};

    private final byte[] cmdNoData =  new byte[] {(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00};
    private final byte[] cmdHasData =  new byte[] {(byte)0x01,(byte)0x00,(byte)0x00,(byte)0x00};

    public TianJinGuaoMsg getDataFromGuao(InputStream input, OutputStream output, String time) {
        TianJinGuaoMsg msg = new TianJinGuaoMsg();
        try {
            TianJinDatFile DataFile = msg.getFileData();
            msg.setResult(-1);
            if (time == null) {
                //上位机查询设备是否有数据要上传
                output.write(combine(this.cmdHead,this.cmdCheckData,intToBytes(0x00000000),intToBytes(0x00000004)));  //0x00000040改成了04
            } else {//特定时间
                output.write(combine(this.cmdHead,this.cmdReadDataAfterTime,intToBytes(0x0000000E),intToBytes(0x00000004)));
            }

            byte[] result = new byte[4];
            int len;
            
           
            if (!TianJinDatFile.readData(result, 4, input)) {
                msg.setResult(-1);
                msg.setErrMsg("read response err 1!");
                return msg;
            }
            String tem = StringUtil.byteToHexString2(result, 0, 4);
            System.out.println(tem);
            if (!tem.equals("AAAAAAAA")) {
                msg.setResult(-1);
                msg.setErrMsg("the Counting machine is not ready!");
                return msg;
            }

            if (time != null) {
                output.write(time.getBytes());
            }
            byte[] lData = new byte[64];

           // len = input.read(lData);
            if (!TianJinDatFile.readData(lData, 64, input)) {
                msg.setResult(-1);
                msg.setErrMsg("readdata err 2!");
                return msg;
            }
            len = StringUtil.byteToInt(lData, 0, 4);
            if (len == 0) {
                msg.setResult(1);
                msg.setErrMsg("this is no data in the Counting machine");
                return msg;
            }
            int dataLen;
            dataLen = StringUtil.byteToInt(lData, 4, 4);
            DataFile.setFileName(StringUtil.byteToString2(lData, 8, 56));

            //从设备端缓冲区读取特定长度数据
//            output.write(this.cmdHead);
//            output.write(this.cmdReadData);
//            output.write(intToBytes(0x00000000));
//            output.write(intToBytes(dataLen));
output.write(combine(this.cmdHead,this.cmdReadData,intToBytes(0x00000000),intToBytes(dataLen)));
          //  len = input.read(result);
            if (!TianJinDatFile.readData(result, 4, input)) {
                msg.setResult(-1);
                msg.setErrMsg("read response err 3!");
                this.respon(input, output, this.cmdNoData);
                return msg;
            }
            tem = StringUtil.byteToHexString2(result, 0, 4);
            if (!tem.equals("AAAAAAAA")) {
                msg.setResult(-1);
                msg.setErrMsg("Read data err 4 "+tem);
                this.respon(input, output, this.cmdNoData);
                return msg;
            }

            boolean re = DataFile.readFile(input);
            if (!re) {
                msg.setResult(-1);
                msg.setErrMsg("get file data err!");
                this.respon(input, output, this.cmdNoData);
                return msg;
            }
//            System.out.println("getRecordCount:"+DataFile.getFileHead().getRecordCount());
            this.respon(input, output, this.cmdHasData);

        } catch (IOException ex) {
            msg.setResult(-1);
            msg.setErrMsg(ex.getMessage());
            logger.log(Level.INFO, null,ex);
            return msg;
        }
        msg.setResult(0);
        return msg;
    }
    public byte[] combine(byte[]b1,byte[] b2,byte[] b3,byte[] b4){
        byte[] re = new byte[16];
        int lo = 0;
        re[lo++] = b1[0];
        re[lo++] = b1[1];
        re[lo++] = b1[2];
        re[lo++] = b1[3];
        re[lo++] = b2[0];
        re[lo++] = b2[1];
        re[lo++] = b2[2];
        re[lo++] = b2[3];
        re[lo++] = b3[0];
        re[lo++] = b3[1];
        re[lo++] = b3[2];
        re[lo++] = b3[3];
        re[lo++] = b4[0];
        re[lo++] = b4[1];
        re[lo++] = b4[2];
        re[lo++] = b4[3];
        return re;
    }
    public byte[] intToBytes(int data){
        byte[] by = new byte[4];
        by[0] = (byte)(data&0x000000FF);
        by[1] = (byte)((data&0x0000FF00)>>8);
        by[2] = (byte)((data&0x00FF0000)>>16);
        by[3] = (byte)((data&0xFF000000)>>24);
        return by;
    }
    public void respon(InputStream input, OutputStream output, byte[] result) {
        try {
           
            output.write(combine(this.cmdHead,this.cmdReturn,intToBytes(0x00000004),intToBytes(0x00000000)));

            byte[] tem = new byte[4];
            input.read(tem);
            output.write(result);

        } catch (IOException ex) {
           logger.log(Level.INFO, null,ex);
        }
    }

}
