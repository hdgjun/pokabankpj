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
public class TianJinGuaoBody {

    private String colTime = "";
    private String sNO = "";
    private String ver = "";
    private String moneyFlag = "";
    private String valuta = "";
    private char vType = 0;
    private char mType = 0;
    private String macinSno = "";
    private String user = "";
    private String dots = "";
    private String bank = "";
    private String reservel = "";

    private byte[] dataBody;
    private byte[] image = new byte[1544];
    
    public TianJinGuaoBody(int len){
        dataBody = new byte[len];
    }
    
    public void init() {
        int loc = 0;
        this.colTime = StringUtil.byteToString2(dataBody, loc, 14);
        loc += 14;
        this.sNO = StringUtil.byteToString2(dataBody, loc, 12);
        loc += 12;
        this.ver = StringUtil.byteToString2(dataBody, loc, 4);
        loc += 4;
        this.moneyFlag = StringUtil.byteToString2(dataBody, loc, 3);
        loc += 3;
        this.valuta = StringUtil.byteToString2(dataBody, loc, 6);
        loc += 6;
        this.vType = (char) dataBody[loc++];
        this.mType = (char) dataBody[loc++];
        this.macinSno = StringUtil.byteToString2(dataBody, loc, 24);
        loc += 24;
        this.user = StringUtil.byteToString2(dataBody, loc, 7);
        loc += 7;
        this.dots = StringUtil.byteToString2(dataBody, loc, 6);
        loc += 6;
        this.bank = StringUtil.byteToString2(dataBody, loc, 6);
        loc += 6;
        this.reservel = StringUtil.byteToString2(dataBody, loc, 4);
    }
    public void initNew() {
        int loc = 0;
        this.colTime = StringUtil.byteToString2(dataBody, loc, 14);
        loc += 14;
        this.sNO = StringUtil.byteToString2(dataBody, loc, 12);
        loc += 12;
        this.ver = StringUtil.byteToString2(dataBody, loc, 4);
        loc += 4;
        this.moneyFlag = StringUtil.byteToString2(dataBody, loc, 3);
        loc += 3;
        this.valuta = StringUtil.byteToString2(dataBody, loc, 6);
        loc += 6;
        this.vType = (char) dataBody[loc++];
        this.mType = (char) dataBody[loc++];
        this.macinSno = StringUtil.byteToString2(dataBody, loc, 24);
        loc += 24;
        this.user = StringUtil.byteToString2(dataBody, loc, 7);
        loc += 7;
        this.dots = StringUtil.byteToString2(dataBody, loc, 11);
        loc += 11;
        this.bank = StringUtil.byteToString2(dataBody, loc, 11);
        loc += 11;
        this.reservel = StringUtil.byteToString2(dataBody, loc, 2);
    }
    public void reload() {
        int loca = 0;
        StringUtil.charToByte2(this.colTime.toCharArray(), 14, loca, this.dataBody);
        loca += 14;
        StringUtil.charToByte2(this.sNO.toCharArray(), 12, loca, this.dataBody);
        loca += 12;
        StringUtil.charToByte2(this.ver.toCharArray(), 4, loca, this.dataBody);
        loca += 4;
        StringUtil.charToByte2(this.moneyFlag.toCharArray(), 3, loca, this.dataBody);
        loca += 3;
        StringUtil.charToByte2(this.valuta.toCharArray(), 6, loca, this.dataBody);
        loca += 6;
        
        this.dataBody[loca++] = (byte) this.vType;
        this.dataBody[loca++] = (byte) this.mType;
        
        StringUtil.charToByte2(this.macinSno.toCharArray(), 24, loca, this.dataBody);
        loca += 24;
        StringUtil.charToByte2(this.user.toCharArray(), 7, loca, this.dataBody);
        loca += 7;
        StringUtil.charToByte2(this.dots.toCharArray(), 6, loca, this.dataBody);
        loca += 6;
        StringUtil.charToByte2(this.bank.toCharArray(), 6, loca, this.dataBody);
        loca += 6;
        StringUtil.charToByte2(this.reservel.toCharArray(), 4, loca, this.dataBody);
        loca += 4;
        
    }
    
    public void reloadNew() {
        int loca = 0;
        StringUtil.charToByte2(this.colTime.toCharArray(), 14, loca, this.dataBody);
        loca += 14;
        StringUtil.charToByte2(this.sNO.toCharArray(), 12, loca, this.dataBody);
        loca += 12;
        StringUtil.charToByte2(this.ver.toCharArray(), 4, loca, this.dataBody);
        loca += 4;
        StringUtil.charToByte2(this.moneyFlag.toCharArray(), 3, loca, this.dataBody);
        loca += 3;
        StringUtil.charToByte2(this.valuta.toCharArray(), 6, loca, this.dataBody);
        loca += 6;
        
        this.dataBody[loca++] = (byte) this.vType;
        this.dataBody[loca++] = (byte) this.mType;
        
        StringUtil.charToByte2(this.macinSno.toCharArray(), 24, loca, this.dataBody);
        loca += 24;
        StringUtil.charToByte2(this.user.toCharArray(), 7, loca, this.dataBody);
        loca += 7;
        StringUtil.charToByte2(this.dots.toCharArray(), 11, loca, this.dataBody);
        loca += 11;
        StringUtil.charToByte2(this.bank.toCharArray(), 11, loca, this.dataBody);
        loca += 11;
        StringUtil.charToByte2(this.reservel.toCharArray(), 2, loca, this.dataBody);
        loca += 2;
        
    }

    /**
     * @return the colTime
     */
    public String getColTime() {
        return colTime;
    }

    /**
     * @param colTime the colTime to set
     */
    public void setColTime(String colTime) {
        if (colTime.length() > 14) {
            colTime = colTime.substring(0, 14);
        }
        this.colTime = colTime;
    }

    /**
     * @return the sNO
     */
    public String getsNO() {
        return sNO;
    }

    /**
     * @param sNO the sNO to set
     */
    public void setsNO(String sNO) {
        if (sNO.length() > 12) {
            sNO = sNO.substring(0, 12);
        }
        this.sNO = sNO;
    }

    /**
     * @return the ver
     */
    public String getVer() {
        return ver;
    }

    /**
     * @param ver the ver to set
     */
    public void setVer(String ver) {
        if (ver.length() > 4) {
            ver = ver.substring(0, 4);
        }
        this.ver = ver;
    }

    /**
     * @return the moneyFlag
     */
    public String getMoneyFlag() {
        return moneyFlag;
    }

    /**
     * @param moneyFlag the moneyFlag to set
     */
    public void setMoneyFlag(String moneyFlag) {
        if (moneyFlag.length() > 3) {
            moneyFlag = moneyFlag.substring(0, 3);
        }
        this.moneyFlag = moneyFlag;
    }

    /**
     * @return the valuta
     */
    public String getValuta() {
        return valuta;
    }

    /**
     * @param valuta the valuta to set
     */
    public void setValuta(String valuta) {
        if (valuta.length() > 6) {
            valuta = valuta.substring(0, 6);
        }
        this.valuta = valuta;
    }

    /**
     * @return the vType
     */
    public char getvType() {
        return vType;
    }

    /**
     * @param vType the vType to set
     */
    public void setvType(char vType) {
        this.vType = vType;
    }

    /**
     * @return the mType
     */
    public char getmType() {
        return mType;
    }

    /**
     * @param mType the mType to set
     */
    public void setmType(char mType) {
        this.mType = mType;
    }

    /**
     * @return the macinSno
     */
    public String getMacinSno() {
        return macinSno;
    }

    /**
     * @param macinSno the macinSno to set
     */
    public void setMacinSno(String macinSno) {
        if (macinSno.length() > 24) {
            macinSno = macinSno.substring(0, 24);
        }
        this.macinSno = macinSno;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        if (user.length() > 7) {
            user = user.substring(0, 7);
        }
        this.user = user;
    }

    /**
     * @return the dots
     */
    public String getDots() {
        return dots;
    }

    /**
     * @param dots the dots to set
     */
    public void setDots(String dots) {
        if (dots.length() > 6) {
            dots = dots.substring(0, 6);
        }
        this.dots = dots;
    }

    /**
     * @return the bank
     */
    public String getBank() {
        return bank;
    }

    /**
     * @param bank the bank to set
     */
    public void setBank(String bank) {
        if (bank.length() > 6) {
            bank = bank.substring(0, 6);
        }
        this.bank = bank;
    }

    /**
     * @return the reservel
     */
    public String getReservel() {
        return reservel;
    }

    /**
     * @param reservel the reservel to set
     */
    public void setReservel(String reservel) {
        if (reservel.length() > 4) {
            reservel = reservel.substring(0, 4);
        }
        this.reservel = reservel;
    }

    /**
     * @return the dataBody
     */
    public byte[] getDataBody() {
        return dataBody;
    }

    /**
     * @return the image
     */
    public byte[] getImage() {
        return image;
    }

}
