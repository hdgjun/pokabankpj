/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poka.socket;

import com.poka.entity.FsnComProperty;
import com.poka.entity.OperationUser;
import com.poka.entity.PanelMsgEntity;
import com.poka.entity.PokaFsn;
import com.poka.entity.PokaFsnBody;
import com.poka.util.LogManager;
import com.poka.util.MsgThread;
import com.poka.util.StaticVar;
import com.poka.util.StringUtil;
import com.poka.util.UploadFtp;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/**
 *
 * @author Administrator
 */
public class LiaolinJulongSocketHandler extends AbstractSocketHandle {
    static final Logger logger = LogManager.getLogger(LiaolinJulongSocketHandler.class);
    //   private PokaFsn fsn;
    private final byte start_cmd = (byte) 0xA1;
    private final byte stop_cmd = (byte) 0xA2;
    private final int startWord = 0x40404A4C;

    private final int payRe = 0x0001;
    private final int getRe = 0x0002;
    private final int payBusyRe = 0x0003;
    private final int getBusyRe = 0x0004;
    private final int atmRe = 0x0005;
    private final int numberRe = 0x0006;
    private final int mechinaGetRe = 0x0007;
    private final int requestRe = 0x0000;

    private final int sendSucess = 0x00F0;
    private final int formatErr = 0x00E0;
    private final int sysBusy = 0x00E1;
    private final int tooLarge = 0x00E2;
    private final int missReq = 0x00E3;
    private final int req2many = 0x00E4;

    //  private final byte 
    private SocketHandle socketHandle;
    private PokaFsn fsn;
    private FsnComProperty property;
    private String fsnFileName = "";
    private String temFileName = "";
    private String fsnFilePath = "";
    private String temFilePath = "";
    //  private PokaFsn fsnBase;

    public LiaolinJulongSocketHandler(SocketHandle socketHandle, FsnComProperty property) {
        this.socketHandle = socketHandle;
        this.property = property;
        fsn = new PokaFsn();
    }

    @Override
    public void receData() {
        InputStream input = null;
        OutputStream output = null;
        String ip = null;
        try {
            input = this.socketHandle.getSocket().getInputStream();
            output = this.socketHandle.getSocket().getOutputStream();
            ip = this.socketHandle.getSocket().getInetAddress().getHostAddress();
            int iRet;
            byte[] head = new byte[12];
            boolean flag = false;
                        
            while (StaticVar.monTcpListen) {
                if (flag) {
                    break;
                }
                boolean bRt = PokaFsn.readData(head, 12, input);
                if(!bRt){
                    showMsg(PanelMsgEntity.connectMSGType, "ip:" + ip + " 机具已断开连接！", null, ip, PanelMsgEntity.closeState);
                    break;
                }
               // System.out.println(StringUtil.byteToHexString(head, 0, 12));
                switch (head[10]) {
                    case start_cmd: {
                        byte[] aBody = new byte[42];
                        PokaFsn.readData(aBody, 42, input);
                        this.replay(output, requestRe, sendSucess, aBody);
                        break;
                    }

                    case stop_cmd: {
                        byte[] sBody = new byte[32];
                        PokaFsn.readData(sBody, 32, input);
                        // flag = true;
 
                        PokaFsn tem = new PokaFsn();
                        tem.readBaseFsnFile(this.temFilePath);
                        fsn = new PokaFsn();
                        for (PokaFsnBody body : tem.getbList()) {
                            String recTime = null;
                            boolean iFlag = removeRepeat(body.getsNo());
                            if (iFlag) {
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

                                }

                                fsn.add(body);

                                showMsg(PanelMsgEntity.monMSGType, recTime, body.getsNo(), ip, -1);

                            } else {
                             showMsg(PanelMsgEntity.monMSGType, body.getDate()+" "+body.getTime(), body.getsNo() + "(重复)", ip, -1);
                            }
                        }

                        fsn.writePokaFsnFile(this.fsnFilePath);
                        tem.getbList().clear();

                        fsn.getFhead().setCount(0);
                        fsn.getbList().clear();
                        File upFile = new File(this.temFileName);
                        upFile.renameTo(new File(this.property.getPath() + File.separator + UploadFtp.basebak + File.separator + this.temFileName));
                        upFile.delete();
                        UploadFtp.oneFileUploadFtp(this.fsnFileName, UploadFtp.fsnbak);
                        break;
                    }
                    default: {
                        byte[] mBody = new byte[72];
                        PokaFsn.readData(mBody, 72, input);
                        int packageIndex = mBody[48] * 255 + mBody[49];
                        if (packageIndex == 0) {
                            this.fsnFileName =  this.getFsnName();
                            this.fsnFilePath = this.property.getPath() + File.separator + UploadFtp.tem + File.separator + this.fsnFileName;
                            this.temFileName = this.fsnFileName + ".JULONG";
                            this.temFilePath = this.fsnFilePath + ".JULONG";
                        }
                        RandomAccessFile randomFile = new RandomAccessFile(this.temFilePath, "rw");
                        // 文件长度，字节数
                        long fileLength = randomFile.length();
                        //将写文件指针移到文件尾。
                        randomFile.seek(fileLength);

                        int count = StringUtil.byteToInt(head, 4, 4) - 84 - 2;
                        int len;
                        byte[] disDa = new byte[1024];
                        int lo = 0;
                        int curLen = count>1024?1024:count;
                        while ((len = input.read(disDa, 0, curLen)) > 0) {
                            lo += len;
                            randomFile.write(disDa, 0, len);
                            if (lo == count) {
                                System.out.println("lo="+lo);
                                break;
                            }
                            curLen = count-lo>1024?1024:count-lo;
                        }
                        
                        randomFile.close();
                        byte[] checkSum = new byte[2];
                        PokaFsn.readData(checkSum, 2, input);
                        this.replay(output, head[11], sendSucess, mBody);
                    }
                }
            }
            showMsg(PanelMsgEntity.connectMSGType, "ip:" + ip + " 机具已断开连接！", null, ip, PanelMsgEntity.closeState);
        } catch (IOException ex) {
             showMsg(PanelMsgEntity.connectMSGType, "ip:" + ip + " 机具已断开连接！", null, ip, PanelMsgEntity.closeState);
        }
    }

    public synchronized String getFsnName() {
        String bankNo = this.property.getBankNo();
        if (null == bankNo) {
            bankNo = "*";
        }
        String dotNo = this.property.getDotNo();
        if (null == dotNo) {
            dotNo = "*";
        }
        String date = (new java.text.SimpleDateFormat("yyyyMMddHHmmssSSS")).format(new Date());
        return bankNo + "_" + dotNo + "_XXXXXXXXXXXXXX_XXXXXXX" + date + ".FSN";
    }

    public boolean removeRepeat(String mon) {
        if (this.property.getBusType() == FsnComProperty.comBusType) {
            return this.property.getXmlCfg().isRepeatMon(mon, property.getLimit());
        } else {
            return this.property.getXmlCfg().isRepeatMonATMAdd(mon, property.getLimit());
        }
    }

   private void showMsg(int busyType, String cmdMsg, String dataMsg, String ip, int state) {
        if (StaticVar.monTcpListen &&this.property.getDealPanel() != null) {         
            PanelMsgEntity pMsg =  new PanelMsgEntity(busyType, cmdMsg, dataMsg, ip, state,PanelMsgEntity.serverType);
            MsgThread mt = new MsgThread();
            mt.setDealPanel(this.property.getDealPanel());
            mt.setpMsg(pMsg);
            SwingUtilities.invokeLater(mt);
        }
    }


    public void replay(OutputStream output, int requestCmd, int retCode, byte[] data) {
        try {
            byte[] reData = new byte[46];
            int loca = 0;
            StringUtil.intToByte(reData, loca, loca, startWord);
            loca += 4;
            StringUtil.intToByte(reData, loca, loca, 46);
            loca += 4;
            reData[8] = 15;
            loca += 2;
            reData[10] = (byte) (requestCmd & 0x000000FF);
            loca += 2;
            reData[12] = (byte) (retCode & 0x000000FF);
            loca += 2;
            for (int i = 0; i < 28; i++) {
                reData[loca + i] = data[i];
            }
            //System.err.println("reply["+StringUtil.byteToHexString2(reData, 0, reData.length)+"]");
            output.write(reData);
        } catch (IOException ex) {
            logger.log(Level.INFO, null,ex);
        }
        
    }
}
