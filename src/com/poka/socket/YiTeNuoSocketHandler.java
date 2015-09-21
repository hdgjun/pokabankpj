/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poka.socket;

import static com.poka.socket.GuAoSocketHandler.lock;
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
public class YiTeNuoSocketHandler extends AbstractSocketHandle {

    static final Logger logger = LogManager.getLogger(YiTeNuoSocketHandler.class);
    private SocketHandle socketHandle;
    private PokaFsn fsn;
    private FsnComProperty property;
    private byte[] startWord = new byte[4];
    private byte[] fsnCount = new byte[2];
    private byte[] validNum = new byte[4];
    private byte[] cmd = new byte[2];
    private byte[] machinaSno = new byte[48];
    private byte[] checkSum = new byte[2];
    private byte[] endWord = new byte[4];

    public YiTeNuoSocketHandler(SocketHandle socketHandle, FsnComProperty property) {
        this.socketHandle = socketHandle;
        this.property = property;

    }

    @Override
    public void receData() {
        InputStream input = null;
        OutputStream output = null;
        String ip = null;
        try {
            Socket client = this.socketHandle.getSocket();
            output = client.getOutputStream();
            input = client.getInputStream();
            byte[] head = new byte[4];
            String sTime = null;
     

            byte[] res = {0x00, 0x00};
            byte[] reErr = {0x01, 0x01};
       

            ip = client.getInetAddress().getHostAddress();
            
            int off = 0;
            while (StaticVar.monTcpListen) {
                int bRt = PokaFsn.readInputStreamWithTimeout(input, startWord, 5000, off);
                if (bRt == -1) {
                    break;
                } else if (bRt == 0) {
                    continue;
                }
                sTime = StringUtil.byteToHexString(startWord, 0, startWord.length);

                if (!sTime.equalsIgnoreCase("55AA55AA")) {                   
                    output.write(reErr);
                    break;
                }
         
                if (!PokaFsn.readData(fsnCount, fsnCount.length, input)) { 
                    output.write(reErr);
                    break;
                }
         

                if (!PokaFsn.readData(validNum, validNum.length, input)) {
                    output.write(reErr);
                    break;
                }

                if (!PokaFsn.readData(cmd, cmd.length, input)) {                
                    output.write(reErr);
                    break;
                }

                if (!PokaFsn.readData(machinaSno, machinaSno.length, input)) {
                    output.write(reErr);
                    break;
                }
          
                PokaFsn temFsn = new PokaFsn();
                if (!temFsn.readBaseFsnFile(input, cmd[0])) {
                    output.write(reErr);
                    break;
                }
              
                if (!PokaFsn.readData(checkSum, checkSum.length, input)) {
                    output.write(reErr);
                    break;
                }
 
                if (!PokaFsn.readData(endWord, endWord.length, input)) {
                    output.write(reErr);
                    break;
                }

                output.write(res);
 
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
                        String newPokaName =  this.getFsnName(this.property.getBankNo(),this.property.getDotNo());
                    
                        String newPokaPath = this.property.getPath() + File.separator + UploadFtp.tem + File.separator + newPokaName;

                        fsn.writePokaFsnFile(newPokaPath);
                        UploadFtp.uploadFsnFile(newPokaPath, newPokaName);
                        fsn.getFhead().setCount(0);
                        fsn.getbList().clear();
                        fsn = null;
                        //UploadFtp.oneFileUploadFtp(newPokaName, UploadFtp.fsnbak);
                    }
                }
                
                break;
            }

            this.socketHandle.getSocket().close();
        } catch (IOException ex) {
            showMsg(PanelMsgEntity.connectMSGType, "ip:" + ip + " 机具已断开连接！", null, ip, PanelMsgEntity.closeState);
            logger.log(Level.INFO, null,ex);
        }
    }

    public boolean readData(byte[] disDa, int dataLen, InputStream input) {
        try {
            int count = dataLen;
            int lo = 0;
            int len;
            //    while ((len = input.read(disDa, lo, count - lo)) > 0) {
            while (true) {
                len = input.available();
                input.read(disDa, lo, count - lo);
                lo += len;
                if (lo == count) {
                    break;
                }
            }
            if (lo != count) {
                return false;
            }
        } catch (IOException ex) {
            return false;
        }
        return true;
    }

   

    private void showMsg(int busyType, String cmdMsg, String dataMsg, String ip, int state) {
        if (this.property.getDealPanel() != null) {
            if (!StaticVar.monTcpListen && PanelMsgEntity.monMSGType == busyType) {
                return;
            }
            PanelMsgEntity pMsg = new PanelMsgEntity(busyType, cmdMsg, dataMsg, ip, state,PanelMsgEntity.serverType);
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

}
