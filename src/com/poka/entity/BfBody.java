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
public class BfBody {

    private String bundleId;
    private String atmId;
    private String monBoxId;
    private String userId3;
    private String userId4;
    private String addMoneyDateTime;

    private byte[] bodyData;

    public BfBody() {
        this.bundleId = "";
        this.atmId = "";
        this.monBoxId = "";
        this.userId3 = "";
        this.userId4 = "";
        this.addMoneyDateTime = "";
        this.bodyData = new byte[88+6];
    }

    public void init() {
        int loca = 0;
        this.bundleId = StringUtil.byteToString2(this.getBodyData(), loca, 24);
        loca += 25;
        this.atmId = StringUtil.byteToString2(this.getBodyData(), loca, 18);
        loca += 19;
        this.monBoxId = StringUtil.byteToString2(this.getBodyData(), loca, 11);
        loca += 12;
        this.userId3 = StringUtil.byteToString2(this.getBodyData(), loca, 8);
        loca += 9;
        this.userId4 = StringUtil.byteToString2(this.getBodyData(), loca, 8);
        loca += 9;
        this.addMoneyDateTime = StringUtil.byteToString2(this.getBodyData(), loca, 19);
        loca += 20;

        if(this.bundleId.equalsIgnoreCase("XXXXXXXXXXXXXXXXXXXXXXXX")){
            this.bundleId = "";
        }
        if(this.atmId.equalsIgnoreCase("XXXXXXXXXXXXXXXXXX")){
            this.atmId = "";
        }
        if(this.monBoxId.equalsIgnoreCase("XXXXXXXXXXX")){
            this.monBoxId = "";
        }
    }

    public void reload() {
        int loca = 0;
        int len = this.bundleId.length()>24?24:this.bundleId.length();
        StringUtil.charToByte2(this.bundleId.toCharArray(), len, loca, this.getBodyData());
        fillX(24 - len, loca + len, this.getBodyData());
        loca += 25;
        len = this.atmId.length()>18?18:this.atmId.length();
        StringUtil.charToByte2(this.atmId.toCharArray(), len, loca, this.getBodyData());
        fillX(18 - len, loca + len, this.getBodyData());
        loca += 19;
        len = this.monBoxId.length()>11?11:this.monBoxId.length();
        StringUtil.charToByte2(this.monBoxId.toCharArray(), len, loca, this.getBodyData());
        fillX(11 - len, loca + len, this.getBodyData());
        loca += 12;
        len = this.userId3.length()>8?8:this.userId3.length();
        StringUtil.charToByte2(this.userId3.toCharArray(),len, loca, this.getBodyData());
        fillX(8 - len, loca + len, this.getBodyData());
        loca += 9;
        len = this.userId4.length()>8?8:this.userId4.length();
        StringUtil.charToByte2(this.userId4.toCharArray(),len, loca, this.getBodyData());
        fillX(8 - len, loca + len, this.getBodyData());
        loca += 9;
        len = this.addMoneyDateTime.length()>19?19:this.addMoneyDateTime.length();
        StringUtil.charToByte2(this.addMoneyDateTime.toCharArray(), len, loca, this.getBodyData());
        fillX(19 - len, loca + len, this.getBodyData());
    }

    public void fillX(int len, int start, byte[] by) {
        for (int i = 0; i < len; i++) {
            by[start + i] = 'X';
        }
        by[start + len] = '\n';
    }

    /**
     * @return the bundleId
     */
    public String getBundleId() {
        return bundleId;
    }

    /**
     * @param bundleId the bundleId to set
     */
    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    /**
     * @return the atmId
     */
    public String getAtmId() {
        return atmId;
    }

    /**
     * @param atmId the atmId to set
     */
    public void setAtmId(String atmId) {
        this.atmId = atmId;
    }

    /**
     * @return the monBoxId
     */
    public String getMonBoxId() {
        return monBoxId;
    }

    /**
     * @param monBoxId the monBoxId to set
     */
    public void setMonBoxId(String monBoxId) {
        this.monBoxId = monBoxId;
    }

    /**
     * @return the userId3
     */
    public String getUserId3() {
        return userId3;
    }

    /**
     * @param userId3 the userId3 to set
     */
    public void setUserId3(String userId3) {
        this.userId3 = userId3;
    }

    /**
     * @return the userId4
     */
    public String getUserId4() {
        return userId4;
    }

    /**
     * @param userId4 the userId4 to set
     */
    public void setUserId4(String userId4) {
        this.userId4 = userId4;
    }

    /**
     * @return the addMoneyDateTime
     */
    public String getAddMoneyDateTime() {
        return addMoneyDateTime;
    }

    /**
     * @param addMoneyDateTime the addMoneyDateTime to set
     */
    public void setAddMoneyDateTime(String addMoneyDateTime) {
        this.addMoneyDateTime = addMoneyDateTime;
    }

    /**
     * @return the bodyData
     */
    public byte[] getBodyData() {
        return bodyData;
    }

    /**
     * @param bodyData the bodyData to set
     */
    public void setBodyData(byte[] bodyData) {
        this.bodyData = bodyData;
    }
    

}
