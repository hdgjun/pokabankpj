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
import com.poka.util.UploadFtp;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/**
 *
 * @author Administrator
 */
public class GuAoSocketHandler extends AbstractSocketHandle {

    static final Logger logger = LogManager.getLogger(GuAoSocketHandler.class);

    static Object lock = new Object();

    private SocketHandle socketHandle;
    private PokaFsn fsn;
    private FsnComProperty property;

    public final String G1Cmd = "G001";
    public final String G2Cmd = "G002";
    public final String G3Cmd = "G003";
    public final String G4Cmd = "G004";

    //  private PokaFsn fsnBase;
    public GuAoSocketHandler(SocketHandle socketHandle, FsnComProperty property) {
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
            Socket client = this.socketHandle.getSocket();

            input = client.getInputStream();
            output = client.getOutputStream();

            output.write("0000".getBytes());
            ip = client.getInetAddress().getHostAddress();

            byte cmd[] = new byte[4];
            byte len[] = new byte[2];
            while (StaticVar.monTcpListen) {
                if (client.isConnected()) {
                    PokaFsn.readData(cmd, 4, input);
                } else {
                    break;
                }
                String scmd = new String(cmd);

                PokaFsn.readData(len, 2, input);

                int iLen = len[1] * 256 + len[0];

                byte data[] = new byte[iLen];

                PokaFsn.readData(data, iLen, input);

                switch (scmd) {
                    case G1Cmd: {
                        output.write("0000".getBytes());
                        break;
                    }
                    case G2Cmd: {
                        output.write("0000".getBytes());
//               

//                    }
//                    case G3Cmd: {
                        PokaFsn.readData(cmd, 4, input);
                        PokaFsn.readData(len, 2, input);
                        byte td;
                        while (true) {
                            td = (byte) input.read();
                            if (td == 0x6E) {
                                break;
                            }
                        }

                        PokaFsn temFsn = new PokaFsn();
                        if (!temFsn.readBaseFsnFile(input, (byte) 0x30)) {
                            output.write("1000".getBytes());
                            break;
                        }
                        output.write("0000".getBytes());

                        PokaFsn.readData(cmd, 4, input);
                        PokaFsn.readData(len, 2, input);
                        iLen = len[1] * 256 + len[0];

                        byte data1[] = new byte[iLen];

                        PokaFsn.readData(data1, iLen, input);
                        
                        output.write("0000".getBytes());
                        int i;
                        if (fsn == null) {
                            fsn = new PokaFsn();
                        }
                        List<PokaFsnBody> bList = temFsn.getbList();
                        synchronized (lock) {
                            for (i = 0; i < temFsn.getFhead().getCount(); i++) {
                                PokaFsnBody body = bList.get(i);
                                String strNum = body.getsNo().trim();
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

                                        //body.set
                                        body.setBagId(this.property.getBoxId() != null ? this.property.getBoxId() : "");

                                        body.setFlag((byte) 3);

                                        recTime = (new java.text.SimpleDateFormat("yyyyMMddHHmmssSSS")).format(new Date());

                                    }

                                    fsn.add(body);

                                    showMsg(PanelMsgEntity.monMSGType, recTime, strNum, ip, -1);

                                } else {

                                    if (this.property.getBusType() == FsnComProperty.atmAddBusType) {

                                        recTime = (new java.text.SimpleDateFormat("yyyyMMddHHmmssSSS")).format(new Date());

                                    }
                                    showMsg(PanelMsgEntity.monMSGType, recTime, strNum + "(重复)", ip, -1);

                                }
                            }

                            bList.clear();

                            if (fsn.getbList().size() > 0) {
                                String newPokaName = getFsnName();
                                System.out.println(newPokaName);
                                String newPokaPath = this.property.getPath() + File.separator + UploadFtp.tem + File.separator + newPokaName;

                                fsn.writePokaFsnFile(newPokaPath);
                                fsn.getFhead().setCount(0);
                                fsn.getbList().clear();
                                fsn = null;
                                UploadFtp.oneFileUploadFtp(newPokaName, UploadFtp.fsnbak);
                            }
                        }
                        break;
                    }
                    default: {
                        break;
                    }
                }
                break;
            }
        } catch (IOException ex) {

        } finally {

            if (socketHandle.getSocket() != null) {
                try {
                    socketHandle.getSocket().close();
                } catch (IOException ex) {
                    Logger.getLogger(GuAoSocketHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
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

}
