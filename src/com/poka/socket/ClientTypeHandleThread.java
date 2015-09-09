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
import com.poka.entity.TianJinDatFile;
import com.poka.entity.TianJinGuaoBody;
import com.poka.entity.TianJinGuaoCmd;
import com.poka.entity.TianJinGuaoMsg;
import com.poka.printer.socket.SocketClient;
import com.poka.util.BundleDeal;
import com.poka.util.LogManager;
import com.poka.util.MsgThread;
import com.poka.util.StaticVar;
import com.poka.util.UploadFtp;
import com.poka.util.XmlSax;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.SwingUtilities;

/**
 *
 * @author Administrator
 */
public class ClientTypeHandleThread implements Runnable {

    static final Logger logger = LogManager.getLogger(ClientTypeHandleThread.class);
    //  private int id;
    private FsnComProperty property;
    private String time;
  

  
    public void setProperty(FsnComProperty property) {
        this.property = property;
    }

//        public void setId(int id) {
//            this.id = id;
//        }
    public void run() {
        String ip = this.property.getIp();
        int port = this.property.getPort();
        TianJinGuaoCmd cmd = new TianJinGuaoCmd();
   
        SocketClient client = new SocketClient(ip, port);
        
        if (!client.connectServer()) {
             client.disConnect();
            return;
        }
     
        
        
        if (time == null) {
            time = (new java.text.SimpleDateFormat("yyyyMMddHHmmssSSS")).format(BundleDeal.getDBTime());
        }
        TianJinGuaoMsg msg = cmd.getDataFromGuao(client.getInStream(), client.getOutStream(), null);
        System.out.println("client:"+client.getSocket().isConnected());
        if (msg.getResult() == -1) {
           showMsg(PanelMsgEntity.connectMSGType, null, null, ip, PanelMsgEntity.closeState);
            return;
        } else if (msg.getResult() == 1) {
                //  threadStat[id] = false;
            //   guAoShowMessage(new PanelMsgEntity("ip为:" + ip + " 的机具没有要上传的数据！", null));
            showMsg(PanelMsgEntity.connectMSGType, null, null, ip, PanelMsgEntity.connectState);
            return;
        }
       
        client.disConnect();
        showMsg(PanelMsgEntity.connectMSGType, null, null, ip, PanelMsgEntity.connectState);
        PokaFsn fsn = new PokaFsn();
        TianJinDatFile tjData = msg.getFileData();
//        try {
//            tjData.writeDatFile("D:\\tem\\aa.txt");
//        } catch (IOException ex) {
//            Logger.getLogger(DataSendJPanel.class.getName()).log(Level.SEVERE, null, ex);
//        }
        int limit = this.property.getLimit();
     
        for (TianJinGuaoBody bd : tjData.getbList()) {
            if (bd.getsNO().trim().length() <= 0) {
                continue;
            }
            Pattern pattern = Pattern.compile("^[0-9A-Z]*");
//                Pattern pattern = Pattern.compile("[`~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]");
            Matcher matcher = pattern.matcher(bd.getsNO().trim());
            boolean b = matcher.matches();
            if (!b) {
                continue;
            }
            if (removeRepeat(bd.getsNO().trim(), limit)) {
                PokaFsnBody body = new PokaFsnBody();
                OperationUser usr = null;
                usr = this.property.getXmlCfg().getUser1AndUser2(ip, FsnComProperty.guaoBusType);

                body.setUserId1(usr.getUser1());

                body.setUserId2(usr.getUser2());

                body.setFlag((byte) this.property.getMoType());

                body.setDate(bd.getColTime().substring(0, 8));
                body.setTime(bd.getColTime().substring(8, 10) + ":" + bd.getColTime().substring(10, 12) + ":" + bd.getColTime().substring(12, 14));

                switch (bd.getvType()) {
                    case '0':
                        body.setTfFlag("1");
                        break;
                    case '1':
                        body.setTfFlag("0");
                        break;
                    case '2':
                        body.setTfFlag("2");
                        break;
                    case '3':
                        body.setTfFlag("0");
                        break;
                }

                body.setMoneyFlag(bd.getMoneyFlag());

                switch (bd.getVer()) {
                    case "1990":
                        body.setVer(0);
                        break;
                    case "1999":
                        body.setVer(1);
                        break;
                    case "2005":
                        body.setVer(2);
                        break;
                    default:
                        body.setVer(9999);

                }

                body.setValuta(Integer.parseInt(bd.getValuta()));
                body.setCharNum(bd.getsNO().trim().length());
                body.setsNo(bd.getsNO().trim());
                body.setMacinSno(bd.getMacinSno());
                body.getImageSNo().setImData(bd.getImage());
                body.getImageSNo().init();

                fsn.add(body);
                
                showMsg(PanelMsgEntity.monMSGType, null, bd.getsNO().trim(), ip, -1);
                //   guAoShowMessage(new PanelMsgEntity(null, bd.getsNO().trim()));
            } else {
                showMsg(PanelMsgEntity.monMSGType, null, bd.getsNO().trim() + "(重复)", ip, -1);
                //  guAoShowMessage(new PanelMsgEntity(null, bd.getsNO().trim() + "(重复)"));
            }
            System.out.println(bd.getsNO().trim());
        }
        
        if (fsn.getbList().size() > 0) {
            try {
                String newPokaName = getFsnName();
                String newPokaPath = this.property.getPath() + File.separator + UploadFtp.tem + File.separator + newPokaName;

                fsn.writePokaFsnFile(newPokaPath);
                fsn.getFhead().setCount(0);
                fsn.getbList().clear();

                UploadFtp.oneFileUploadFtp(newPokaName, UploadFtp.fsnbak);
            } catch (IOException ex) {
                logger.log(Level.INFO, null,ex);
            }
        }
        //     threadStat[id] = false;
    }
    
     public static synchronized String getFsnName() {
        //StaticVar.bankId, StaticVar.agencyNo, StaticVar.cfgMap.get(argPro.localAddr)
        String bankNo = StaticVar.bankId;
        if (null == bankNo) {
            bankNo = "*";
        }
        String dotNo = StaticVar.agencyNo;
        if (null == dotNo) {
            dotNo = "*";
        }
        String date = (new java.text.SimpleDateFormat("yyyyMMddHHmmssSSS")).format(new Date());
        return bankNo + "_" + dotNo + "_XXXXXXXXXXXXXX_XXXXXXX" + date + ".FSN";
    }

    public synchronized boolean removeRepeat(String mon, int limit) {
        return XmlSax.getInstance().isRepeatMon(mon, limit);
    }
    
    private void showMsg(int busyType, String cmdMsg, String dataMsg, String ip, int state) {
        if (this.property.getDealPanel() != null) {
            
            PanelMsgEntity pMsg = new PanelMsgEntity(busyType, cmdMsg, dataMsg, ip, state,PanelMsgEntity.clientType);
            MsgThread mt = new MsgThread();
            mt.setDealPanel(this.property.getDealPanel());
            mt.setpMsg(pMsg);
            SwingUtilities.invokeLater(mt);
        }
    }

}
