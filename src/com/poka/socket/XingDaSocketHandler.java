/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poka.socket;

import com.poka.entity.FsnComProperty;
import com.poka.entity.ImageSno;
import com.poka.entity.OperationUser;
import com.poka.entity.PanelMsgEntity;
import com.poka.entity.PokaFsn;
import com.poka.entity.PokaFsnBody;
import com.poka.util.IOUtil;
import com.poka.util.LogManager;
import com.poka.util.MsgThread;
import com.poka.util.StaticVar;
import com.poka.util.StringUtil;
import com.poka.util.UploadFtp;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/**
 *
 * @author Administrator
 */
public class XingDaSocketHandler extends AbstractSocketHandle {

    static final Logger logger = LogManager.getLogger(XingDaSocketHandler.class);
    public static final int nullCmd = 0x00000000;
    public static final int dataCmd = 0x00000010;
    public static final int errCmd = 0x00000020;
    public static final int beginCmd = 0x00000030;
    public static final int endCmd = 0x00000040;

    static Object lock = new Object();

    private final String[] types = {"CNY", "USD", "EUR", "HKD", "JPY", "GBP"};

    private  SocketHandle socketHandle;
    private PokaFsn fsn;
    private  FsnComProperty property;
    //  private PokaFsn fsnBase;

    public XingDaSocketHandler(SocketHandle socketHandle, FsnComProperty property) {
        this.socketHandle = socketHandle;
        this.property = property;
        fsn = new PokaFsn();
    }

    byte get_iflag(int n) {
        switch (n) {
            case 0x0:
            case 0x4:
                return 0;
            case 0x1:
            case 0x2:
                return 1;
            case 0x3:
                return 2;
            default:
                return 0;

        }
    }

    int get_iver(int n) {
        switch (n) {
            case 0x0:
                return 2;
            case 0x1:
                return 1;
            case 0x2:
                return 0;
            default:
                return n;
        }
    }

    int get_value(int n) {
        switch (n) {
            case 0x00:
                return 100;
            case 0x01:
                return 50;
            case 0x02:
                return 20;
            case 0x03:
                return 10;
            case 0x04:
                return 5;
            case 0x05:
                return 2;
            case 0x06:
                return 1;
            default:
                return 0;
        }
    }

    public int extend(byte ex1, byte ex2) {
        return ((extend1(ex1, ex2) << 16) & 0xFFFF0000) | (extend1(ex1, ex2) & 0x0000FFFF);
    }

    public int extend1(byte ex1, byte ex2) {
        int i = ((int) ex1) & 0x000000FF;
        return (((i << 8) & 0x0000FF00) | (ex2 & 0x00FF)) & 0x0000FFFF;
    }

    public int change(int x) {
        x = ((x & 0x0000FF00) << 8) | ((x >> 8) & 0x0000FF00) | (x & 0xFF0000FF);
        x = ((x & 0x00F000F0) << 4) | ((x >> 4) & 0x00F000F0) | (x & 0xF00FF00F);
        x = ((x & 0x0C0C0C0C) << 2) | ((x >> 2) & 0x0C0C0C0C) | (x & 0xC3C3C3C3);
        x = ((x & 0x22222222) << 1) | ((x >> 1) & 0x22222222) | (x & 0x99999999);
        return x;
    }

    public void intsToBytes(byte[] des, int[] src) {
        if (des.length < src.length * 4) {
            des = new byte[src.length * 4];
        }
        int loca = 0;
        for (int i : src) {
            des[loca++] = (byte) (i & 0x0000ff);
            des[loca++] = (byte) ((i >> 8) & 0x0000ff);
            des[loca++] = (byte) ((i >> 16) & 0x0000ff);
            des[loca++] = (byte) ((i >> 24) & 0x0000ff);
        }
    }

    @Override
    public void receData() {
        InputStream input = null;
        String ip = null;
        try {
            Socket client = this.socketHandle.getSocket();
            input = client.getInputStream();

            fsn.setBankNo(this.property.getBankNo());
            fsn.setDotNo(this.property.getDotNo());
            fsn.setPath(this.property.getPath() + File.separator + UploadFtp.tem);

            byte[] head = new byte[16];
            String sTime = null;
            String sComName = null;
            int date = 0;
            String time = "";
  
            ip = client.getInetAddress().getHostAddress();

            while (StaticVar.monTcpListen) {
                if (client.isConnected()) {
                    PokaFsn.readData(head, 16, input);
                } else {
                    break;
                }
                sTime = StringUtil.byteToHexString2(head, 0, head.length);
                //System.out.println(sTime);

                XingDaHead temHead = new XingDaHead(head);

                byte[] temD = null;
                if (temHead.iDataLen > 0) {
                    if(temHead.iDataLen>1024*1024*100){
                      //  System.out.println("datalen = "+temHead.iDataLen);
                        continue;
                    }
                    temD = new byte[temHead.iDataLen];
                    input.read(temD);
                }
                switch (temHead.iCmd) {
                    case nullCmd: {
                        break;
                    }
                    case dataCmd: {
                       // System.out.println("!!!!!!!!!!!!!!!!!!!!!!port:"+StaticVar.cfgMap.get(argPro.port));
                        if(sComName == null){
                        //    System.out.println("没有机器码！！！"+temHead.iDataLen);
                            continue;
                        }
                        PokaFsnBody body = new PokaFsnBody();
                        ImageSno imageSNo = body.getImageSNo();
                        body.setDate("" + date);
                        
                        if(time==null||time.length()<=0){
                            Date date1 = new Date();
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                            time = sdf.format(date1);
                        }
                        body.setTime(time);
                        body.setTfFlag("" + get_iflag((temD[61] >> 4) & 0xf));
                        byte[] ch = new byte[3];
                        ch[0] = temD[61];
                        ch[1] = temD[62];
                        body.setErrorCode(ch);
                        int type_num = temD[0];
                        if (type_num > 5) {
                            body.setMoneyFlag("XXX");
                        } else {
                            body.setMoneyFlag(types[type_num]);
                        }

                        body.setVer(get_iver(temD[2]));
                        body.setValuta(get_value(temD[3]));
                        body.setCharNum(temD[20]);

                        char[] chNum = new char[15];
                        StringUtil.byteToChar2(chNum, 12, 24, temD);
                        String strNum = new String(chNum).trim();
                        body.setsNo(strNum);
                        body.setMacinSno(sComName);
                        
                        body.setBundleId("");

                        imageSNo.setNum(temD[20]);
                        imageSNo.setHeight(32);
                        imageSNo.setWidth(32);

                        for (int i = 0; i < 11; i++) {
                            int[] after = new int[32];
                            for (int j = 0; j < 12; j++) {
//                                after[4 + (j << 1)] = change(extend(temD[76 + i * 24 + (j << 1)], temD[76 + i * 24 + ((j << 1) + 1)]));
//                                after[5 + (j << 1)] = after[ 4 + (j << 1)];
                                after[4 + (j << 1)] = change(extend(temD[76 + i * 24 + ((j << 1)) + 1], temD[76 + i * 24 + (j << 1)]));
                                after[5 + (j << 1)] = after[4 + (j << 1)];
                            }
                            intsToBytes(imageSNo.getsN0()[i].getData(), after);
                        }
                        synchronized (lock) {
                            String recTime = null;
                            boolean iRet = removeRepeat(body.getsNo());
                            if (iRet) {

                                OperationUser usr = null;

                                if (this.property.getBusType() == FsnComProperty.comBusType) {

                                    usr = this.property.getXmlCfg().getUser1AndUser2(ip, FsnComProperty.comBusType);

                                    body.setAtmId("");
                                    body.setBagId("");
                                    body.setUserId1(usr.getUser1());

                                    body.setUserId2(usr.getUser2());
                                    body.setUserId3("");
                                    body.setUserId4("");

                                    body.setFlag((byte) this.property.getMoType());

                                } else if (this.property.getBusType() == FsnComProperty.atmAddBusType) {

                                    usr = this.property.getXmlCfg().getUser1AndUser2(ip, FsnComProperty.atmAddBusType);

                                    body.setUserId1("");

                                    body.setUserId2("");
                                    
                                    body.setUserId3(usr != null ? usr.getUser1() : "");

                                    body.setUserId4(usr != null ? usr.getUser2() : "");

                                    body.setAtmId(this.property.getAtmId() != null ? this.property.getAtmId() : "");

                                    body.setBagId(this.property.getBoxId() != null ? this.property.getBoxId() : "");

                                    body.setFlag((byte) 3);

                                    recTime = date + " " + time;

                                }
                                fsn.addAndWrite(body, this.property.getCount());
                                showMsg(PanelMsgEntity.monMSGType, recTime, strNum, ip, -1);

                            } else {
                                if (this.property.getBusType() == FsnComProperty.atmAddBusType) {

                                    recTime = date + " " + time;

                                }
                                showMsg(PanelMsgEntity.monMSGType, recTime, strNum + "(重复)", ip, -1);
                            }
                        }
                        break;
                    }
                    case errCmd: {
                        break;
                    }
                    case beginCmd: {
                        int s = (int) (((temD[1] >> 4) & 0x0F) * 10 + (temD[1] & 0x0F));
                        int mi = (int) (((temD[2] >> 4) & 0x0F) * 10 + (temD[2] & 0x0F));
                        int h = (int) (((temD[3] >> 4) & 0x0F) * 10 + (temD[3] & 0x0F));
                        int d = (int) (((temD[4] >> 4) & 0x0F) * 10 + (temD[4] & 0x0F));
                        int m = (int) (((temD[6] >> 4) & 0x0F) * 10 + (temD[6] & 0x0F));
                        int y = (int) (((temD[7] >> 4) & 0x0F) * 10 + (temD[7] & 0x0F));
                  
                        date = (2000 + y) * 10000 + m * 100 + d;
                        time = (h < 10 ? 0 + "" + h : h) + ":" + (mi < 10 ? 0 + "" + mi : mi) + ":" + (s < 10 ? 0 + "" + s : s);
                        sComName = "BOC14/POKA/" + StringUtil.byteToString2(temD, 32, 4).trim() + StringUtil.byteToString2(temD, 37, 1).trim() + StringUtil.byteToString2(temD, 39, 8).trim();
                        break;
                    }
                    case endCmd: {
                        date = 0;
                        time = null;
                        sComName = null;
                        break;
                    }
                }

            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } finally {
            
            try {
                showMsg(PanelMsgEntity.connectMSGType, "ip:" + ip + " 机具已断开连接！", null, ip, PanelMsgEntity.closeState);
                if (fsn.getFhead().getCount() > 0) {
                    fsn.addAndWrite(null, fsn.getFhead().getCount());
                }
                if (socketHandle.getSocket() != null) {
                    socketHandle.getSocket().close();
                }
            } catch (IOException ex) {
                logger.log(Level.INFO, null, ex);
            }
        }

    }

    private void showMsg(int busyType, String cmdMsg, String dataMsg, String ip, int state) {
        if (this.property.getDealPanel() != null) {
            if (!StaticVar.monTcpListen && busyType == PanelMsgEntity.monMSGType) {
                return;
            }
            PanelMsgEntity pMsg = new PanelMsgEntity(busyType, cmdMsg, dataMsg, ip, state, PanelMsgEntity.serverType);
            MsgThread mt = new MsgThread();
            mt.setDealPanel(this.property.getDealPanel());
            mt.setpMsg(pMsg);
            SwingUtilities.invokeLater(mt);
        }
    }

   

    public boolean removeRepeat(String mon) {
        if (this.property.getBusType() == FsnComProperty.comBusType) {
            return this.property.getXmlCfg().isRepeatMon(mon, property.getLimit());
        } else {
            return this.property.getXmlCfg().isRepeatMonATMAdd(mon, property.getLimit());
        }
    }

    public class XingDaHead {

        private int iHead;
        private int iCmd;
        private int iSerial;
        private int iDataLen;

        public XingDaHead(byte[] by) {
            int i = 0;
            if (by.length >= 16) {
                this.iHead = StringUtil.byteToInt32(by, i);
                i += 4;
                this.iCmd = StringUtil.byteToInt32(by, i);
                i += 4;
                this.iSerial = StringUtil.byteToInt32(by, i);
                i += 4;
                this.iDataLen = StringUtil.byteToInt32(by, i);
            }
        }

        /**
         * @return the iHead
         */
        public int getiHead() {
            return iHead;
        }

        /**
         * @param iHead the iHead to set
         */
        public void setiHead(int iHead) {
            this.iHead = iHead;
        }

        /**
         * @return the iCmd
         */
        public int getiCmd() {
            return iCmd;
        }

        /**
         * @param iCmd the iCmd to set
         */
        public void setiCmd(int iCmd) {
            this.iCmd = iCmd;
        }

        /**
         * @return the iSerial
         */
        public int getiSerial() {
            return iSerial;
        }

        /**
         * @param iSerial the iSerial to set
         */
        public void setiSerial(int iSerial) {
            this.iSerial = iSerial;
        }

        /**
         * @return the iDataLen
         */
        public int getiDataLen() {
            return iDataLen;
        }

        /**
         * @param iDataLen the iDataLen to set
         */
        public void setiDataLen(int iDataLen) {
            this.iDataLen = iDataLen;
        }

    }

}
