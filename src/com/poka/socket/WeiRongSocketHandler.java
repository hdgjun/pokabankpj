/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poka.socket;

import com.poka.entity.FsnComProperty;
import com.poka.entity.ImageSno;
import com.poka.entity.ImageSnoData;
import com.poka.entity.OperationUser;
import com.poka.entity.PanelMsgEntity;
import com.poka.entity.PokaFsn;
import com.poka.entity.PokaFsnBody;
import static com.poka.socket.XingDaSocketHandler.lock;
import com.poka.util.LogManager;
import com.poka.util.MsgThread;
import com.poka.util.StaticVar;
import com.poka.util.StringUtil;
import com.poka.util.UploadFtp;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.SwingUtilities;

/**
 *
 * @author Administrator
 */
public class WeiRongSocketHandler extends AbstractSocketHandle {

    static final Logger logger = LogManager.getLogger(WeiRongSocketHandler.class);
    private SocketHandle socketHandle;
    private PokaFsn fsn;
    private FsnComProperty property;

    public final int DEV_INFO = 0x00000020;
    public final int USER_INFO = 0x00000022;
    public final int DEVICE_SETTING = 0x00000100;
    public final int START_COUNTING = 0x00000110;
    public final int STOP_COUNTING = 0x0000011f;
    public final int BILL_INFO1 = 0x00000111;
    public final int BILL_INFO2 = 0x00000112;
    public final int BILL_INFO3 = 0x00000113;

    public WeiRongSocketHandler(SocketHandle socketHandle, FsnComProperty property) {
        this.socketHandle = socketHandle;
        this.property = property;
        fsn = new PokaFsn();
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

            WeiRongCmd temHandle = new WeiRongCmd();
            while (StaticVar.monTcpListen) {
                if (client.isConnected()) {
                    temHandle.AnalysisData(input);
                } else {
                    break;
                }

                switch (temHandle.cmd) {
                    case DEV_INFO: {
                         logger.log(Level.INFO,"DEV_INFO");
                        break;
                    }
                    case USER_INFO: {
                        logger.log(Level.INFO,"USER_INFO");
                        break;
                    }
                    case DEVICE_SETTING: {
                        logger.log(Level.INFO,"DEVICE_SETTING");
                        break;
                    }
                    case START_COUNTING: {
                        logger.log(Level.INFO,"START_COUNTING");
                        break;
                    }
                    case STOP_COUNTING: {
                        logger.log(Level.INFO,"STOP_COUNTING");
                        break;
                    }
                    case BILL_INFO1: {
                        logger.log(Level.INFO,"BILL_INFO1");
                        break;
                    }
                    case BILL_INFO2: {
                        logger.log(Level.INFO,"BILL_INFO2");
                        PokaFsnBody body = new PokaFsnBody();
                        ImageSno imageSNo = body.getImageSNo();
                        byte[] monData = temHandle.getData();
                        int lo = 2;
                        int iDate = StringUtil.byteToInt32(monData, lo);
                        lo += 4;
                        int year = (iDate >> 16);
                        int mon = ((iDate >> 8) & 0x000000ff);
                        int de = (iDate & 0x000000ff);

                        body.setDate("" + year + (mon > 9 ? "" : 0) + mon + (de > 9 ? "" : 0) + de);
                        int iTime = StringUtil.byteToInt32(monData, lo);
                        lo += 4;
                        int hh = iTime >> 24;
                        int mm = (iTime >> 16) & 0x000000ff;
                        int ss = (iTime >> 8) & 0x000000ff;
                        body.setTime("" + (hh > 9 ? "" : 0) + hh + ":" + (mm > 9 ? "" : 0) + mm + ":" + (ss > 9 ? "" : 0) + ss);

                        int tf = monData[lo++];
                        String sTf = "";
                        switch (tf) {
                            case 0:
                                sTf = "1";
                                break;
                            case 1:
                                sTf = "2";
                                break;
                            case 2:
                                sTf = "3";
                                break;
                            default:
                                sTf = "0";
                                break;
                        }
                        body.setTfFlag(sTf);
                        body.getErrorCode()[0] = monData[lo++];
                        body.getErrorCode()[1] = monData[lo++];
                        body.getErrorCode()[2] = monData[lo++];

                        String moneyFlag = "" + (char) monData[lo++] + (char) monData[lo++] + (char) monData[lo++] + (char) monData[lo++];
                        body.setMoneyFlag(moneyFlag);
                        body.setVer(StringUtil.byteToInt16(monData, lo));
                        lo += 4;
                        body.setValuta(StringUtil.byteToInt16(monData, lo));
                        lo += 4;
                        String sNo = "";
                        for (int i = 0; i < 12; i++) {
                            sNo += (char) monData[lo++];
                        }
                        Pattern pattern = Pattern.compile("[0-9A-Za-z]{10}");
                        Matcher matcher = pattern.matcher(sNo.trim());
                        boolean b = matcher.matches();
                        if (!b) {
                            sNo = "**********";
                        }
                        body.setsNo(sNo);
                        String macinSno = "";
                        for (int i = 0; i < 24; i++) {
                            macinSno += (char) monData[lo++];
                        }
                        body.setMacinSno(macinSno);
                        lo += 8;

                        imageSNo.setNum(StringUtil.byteToInt16(monData, lo));
                        lo += 2;
                        imageSNo.setHeight(StringUtil.byteToInt16(monData, lo));
                        lo += 2;
                        imageSNo.setWidth(StringUtil.byteToInt16(monData, lo));
                        lo += 2;
                        imageSNo.setResever2(StringUtil.byteToInt16(monData, lo));
                        lo += 2;
                        for (ImageSnoData c : imageSNo.getsN0()) {
                            byte[] da = c.getData();
                            for (int i = 0; i < 128; i++) {
                                da[i] = monData[lo++];
                            }
                        }

                        synchronized (lock) {
                            String recTime = null;
                            boolean iRet = removeRepeat(body.getsNo());
                            if (iRet) {

                                OperationUser usr = null;

                                if (this.property.getBusType() == FsnComProperty.comBusType) {

                                    usr = this.property.getXmlCfg().getUser1AndUser2(ip, FsnComProperty.comBusType);

                                    body.setUserId1(usr.getUser1());

                                    body.setUserId2(usr.getUser2());

                                    body.setFlag((byte) this.property.getMoType());

                                } else if (this.property.getBusType() == FsnComProperty.atmAddBusType) {

                                    usr = this.property.getXmlCfg().getUser1AndUser2(ip, FsnComProperty.atmAddBusType);

                                    body.setUserId3(usr != null ? usr.getUser1() : "");

                                    body.setUserId4(usr != null ? usr.getUser2() : "");

                                    body.setAtmId(this.property.getAtmId() != null ? this.property.getAtmId() : "");

                                    body.setBagId(this.property.getBoxId() != null ? this.property.getBoxId() : "");

                                    body.setFlag((byte) 3);

                                    recTime = date + " " + time;

                                }
                                fsn.addAndWrite(body, this.property.getCount());
                                showMsg(PanelMsgEntity.monMSGType, recTime, sNo, ip, -1);

                            } else {
                                if (this.property.getBusType() == FsnComProperty.atmAddBusType) {

                                    recTime = date + " " + time;

                                }
                                showMsg(PanelMsgEntity.monMSGType, recTime, sNo + "(重复)", ip, -1);
                            }
                        }
                        break;
                    }
                    case BILL_INFO3: {
                        logger.log(Level.INFO,"BILL_INFO3");
                        break;
                    }

                }

            }
        } catch (IOException ex) {

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
            if (!StaticVar.monTcpListen && PanelMsgEntity.monMSGType == busyType) {
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

    public class WeiRongCmd {

        private String start;
        private int cmd;
        private int msgLen;
        private byte[] data;
        private int checksum;
        private String end;

        public void AnalysisData(InputStream input) {
            byte[] head = new byte[8];
            PokaFsn.readData(head, 8, input);
            this.start = "" + (char) head[0] + (char) head[1];
            this.cmd = head[2];
            this.cmd = (this.cmd << 8) + head[3];
            this.msgLen = StringUtil.byteToInt32(head, 4);
            this.data = new byte[this.msgLen];
            PokaFsn.readData(this.data, this.msgLen, input);
            byte[] ends = new byte[4];
            PokaFsn.readData(ends, 4, input);
        }

        /**
         * @return the start
         */
        public String getStart() {
            return start;
        }

        /**
         * @param start the start to set
         */
        public void setStart(String start) {
            this.start = start;
        }

        /**
         * @return the cmd
         */
        public int getCmd() {
            return cmd;
        }

        /**
         * @param cmd the cmd to set
         */
        public void setCmd(int cmd) {
            this.cmd = cmd;
        }

        /**
         * @return the msgLen
         */
        public int getMsgLen() {
            return msgLen;
        }

        /**
         * @param msgLen the msgLen to set
         */
        public void setMsgLen(int msgLen) {
            this.msgLen = msgLen;
        }

        /**
         * @return the data
         */
        public byte[] getData() {
            return data;
        }

        /**
         * @param data the data to set
         */
        public void setData(byte[] data) {
            this.data = data;
        }

        /**
         * @return the checksum
         */
        public int getChecksum() {
            return checksum;
        }

        /**
         * @param checksum the checksum to set
         */
        public void setChecksum(int checksum) {
            this.checksum = checksum;
        }

        /**
         * @return the end
         */
        public String getEnd() {
            return end;
        }

        /**
         * @param end the end to set
         */
        public void setEnd(String end) {
            this.end = end;
        }

    }

}
