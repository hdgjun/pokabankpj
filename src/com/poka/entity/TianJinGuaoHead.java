/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poka.entity;

import com.poka.util.StringUtil;

/**
 *
 * @author Administrator
 */
public class TianJinGuaoHead {

    private String implVer = "";
    private char hasImg = 0;
    private int recordCount = 0;
    private byte[] dataBody = new byte[12];

    public void init() {
        int loca = 0;
        this.setImplVer(StringUtil.byteToString2(getDataBody(), loca, 3));
        loca += 3;
        this.setHasImg((char) this.getDataBody()[loca++]);
        String tem = StringUtil.byteToString2(getDataBody(), loca, 8);
        this.setRecordCount(Integer.parseInt(tem));
    }

    public void reload() {
        int loca = 0;
        StringUtil.charToByte2(this.implVer.toCharArray(), 3, loca, this.dataBody);
        loca += 3;
        this.dataBody[loca++] = (byte) this.hasImg;
        String tem = "" + this.recordCount;
        fill0(8 - tem.length(), loca, this.dataBody);
        loca += 8 - tem.length();
        StringUtil.charToByte2(tem.toCharArray(), tem.length(), loca, this.dataBody);
    }

    public void fill0(int len, int start, byte[] by) {
        for (int i = 0; i < len; i++) {
            by[start + i] = '0';
        }

    }

    /**
     * @return the implVer
     */
    public String getImplVer() {
        return implVer;
    }

    /**
     * @param implVer the implVer to set
     */
    public void setImplVer(String implVer) {
        if (implVer.length() > 3) {
            implVer = implVer.substring(0, 3);
        }
        this.implVer = implVer;
    }

    /**
     * @return the hasImg
     */
    public char getHasImg() {
        return hasImg;
    }

    /**
     * @param hasImg the hasImg to set
     */
    public void setHasImg(char hasImg) {
        if (hasImg != '0' && hasImg != '1') {
            hasImg = '0';
        }
        this.hasImg = hasImg;
    }

    /**
     * @return the recordCount
     */
    public int getRecordCount() {
        return recordCount;
    }

    /**
     * @param recordCount the recordCount to set
     */
    public void setRecordCount(int recordCount) {
        if (recordCount > 99999999) {
            recordCount = 99999999;
        }
        this.recordCount = recordCount;
    }

    /**
     * @return the dataBody
     */
    public byte[] getDataBody() {
        return dataBody;
    }

}
