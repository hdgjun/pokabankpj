/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poka.util;

import com.poka.app.frame.PokaMainFrame;
import com.poka.dao.impl.BaseDao;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author poka
 */
public class BundleDeal {

    private String bunId;
    private String opedate;
    private String caozuoyuan;
    private String qukuanyuan;
    private String bankId;
    private String netId;
    private String guitaiId;
    private String runNum;
    private static BaseDao<Map> b = null;

    /**
     * 生成全部id
     */
    public String productAllId(String bankId, String netId, String runNum) {
        //ATMId编码生成规则
        StringBuffer sb = new StringBuffer("A");
        sb.append(bankId);
        sb.append(netId);
        sb.append(runNum);
        int len = sb.length();
        while (len < 24) {
            sb.append("E");
            len = sb.length();
        }
        return sb.toString();

    }

    public String productCxId(String runNum) {
        //钞箱ID二维码编码格式（11位）
        StringBuffer myCxId = new StringBuffer("C");
        myCxId.append(runNum);
        int myCxLen = myCxId.length();
        while (myCxLen < 24) {
            myCxId.append("E");
            myCxLen = myCxId.length();
        }
        return myCxId.toString();

    }

    public static String productbunId(String boxId) {
        //CT捆ID二维码编码格式（24位）
        StringBuffer myBundleId = new StringBuffer("K");
        myBundleId.append(boxId);
        Date mydate = BundleDeal.getDBTime();
        myBundleId.append(new SimpleDateFormat("HHmmssS").format(mydate));
        int myBundleIdLen = myBundleId.length();
        while (myBundleIdLen < 24) {
            myBundleId.append("E");
            myBundleIdLen = myBundleId.length();
        }
        return myBundleId.toString();
    }

    //BK捆ID二维码编码格式（24位）
    public static String productBKbunId(String userId, String juanbie, int type) {
        Date date = BundleDeal.getDBTime();
        String mydate = new SimpleDateFormat("yyyyMMddHHmmsss").format(date);
        int ty = 4;
        if (type == 1) {
            ty = 2;
        }
        String a = mydate.substring(2, mydate.length());
        String kunID = userId + juanbie + ty + a;
        if (kunID.length() < 24) {
            kunID += "E";
        }
        return kunID;
    }

    /**
     * 获取数据库当前时间
     *
     * @return
     */
    public static Date getDBTime() {
        Date date = new Date();  //合并在一起
        return date;
    }

    public String productBoxId(String boxId) {
        //把ID二维编码格式（24位）   
        StringBuffer myBaId = new StringBuffer("B");
        myBaId.append(boxId);
        myBaId.append(boxId);
        Date mydate = BundleDeal.getDBTime();
        myBaId.append(new SimpleDateFormat("HHmmssS").format(mydate));
        int myBaIdLen = myBaId.length();
        while (myBaIdLen < 24) {
            myBaId.append("E");
            myBaIdLen = myBaId.length();
        }
        return myBaId.toString();
    }

    /**
     * 生成CT文件
     */
    public static void writeCTFile(List<BundleDeal> bundList, String path) {
        try {
            File file = new File(path);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
            BufferedWriter bw = new BufferedWriter(osw);
            int count = bundList.size();
            //bw.write(count+"\n");
            for (BundleDeal bun : bundList) {
                bw.write(bun.getBunId() + "\n");
                bw.write(bun.getOpedate() + "\n");
                bw.write(bun.getGuitaiId() + "\n");;
            }
            //注意关闭的先后顺序，先打开的后关闭，后打开的先关闭
            bw.close();
            osw.close();
            fos.close();
        } catch (IOException ex) {
            Logger.getLogger(PokaMainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the opedate
     */
    public String getOpedate() {
        return opedate;
    }

    /**
     * @param opedate the opedate to set
     */
    public void setOpedate(String opedate) {
        this.opedate = opedate;
    }

    /**
     * @return the caozuoyuan
     */
    public String getCaozuoyuan() {
        return caozuoyuan;
    }

    /**
     * @param caozuoyuan the caozuoyuan to set
     */
    public void setCaozuoyuan(String caozuoyuan) {
        this.caozuoyuan = caozuoyuan;
    }

    /**
     * @return the qukuanyuan
     */
    public String getQukuanyuan() {
        return qukuanyuan;
    }

    /**
     * @param qukuanyuan the qukuanyuan to set
     */
    public void setQukuanyuan(String qukuanyuan) {
        this.qukuanyuan = qukuanyuan;
    }

    /**
     * @return the bankId
     */
    public String getBankId() {
        return bankId;
    }

    /**
     * @param bankId the bankId to set
     */
    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    /**
     * @return the netId
     */
    public String getNetId() {
        return netId;
    }

    /**
     * @param netId the netId to set
     */
    public void setNetId(String netId) {
        this.netId = netId;
    }

    /**
     * @return the guitaiId
     */
    public String getGuitaiId() {
        return guitaiId;
    }

    /**
     * @param guitaiId the guitaiId to set
     */
    public void setGuitaiId(String guitaiId) {
        this.guitaiId = guitaiId;
    }

    /**
     * @return the runNum
     */
    public String getRunNum() {
        return runNum;
    }

    /**
     * @param runNum the runNum to set
     */
    public void setRunNum(String runNum) {
        this.runNum = runNum;
    }

    /**
     * @return the bunId
     */
    public String getBunId() {
        return bunId;
    }

    /**
     * @param bunId the bunId to set
     */
    public void setBunId(String bunId) {
        this.bunId = bunId;
    }

}
