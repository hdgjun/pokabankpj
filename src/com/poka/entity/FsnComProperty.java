/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poka.entity;

import com.poka.app.impl.PanelMessageI;
import com.poka.util.XmlSax;

/**
 *
 * @author Administrator
 */
public class FsnComProperty {

    private String bankNo;
    private String dotNo;
    private String path;
    private String boxId;
    private String atmId;
    private int limit;
    private int count;
    private PanelMessageI dealPanel;
    private XmlSax xmlCfg;
    private int mechinaType = 0;
    private int busType = 0;
    private OperationUser usr;
    private String ip;
    private int port;

    private int moType = 0;

    public static final int zhongchaoMeType = 0;
    public static final int yiTeNuoMeType = 1;
    public static final int renJieMeType = 2;
    public static final int xinDaMeType = 3;
    public static final int liaoNinJulongType = 4;
    public static final int guaoMeType = 5;
    public static final int weiRongType = 6;
    
    public static final int zhongchaoMeType_c = 0;
    public static final int zhongchao2015Metype_c = 1;
    public static final int guaoMetype_c = 2;

    public static final int comBusType = 0;  //服务端模式
    public static final int atmAddBusType = 1;//加钞
    public static final int guaoBusType = 2;//客户端模式

    public FsnComProperty(PanelMessageI dealPanel) {
        this.bankNo = "0299";
        this.dotNo = "0299";
        this.path = "D:\\fsn";
        this.boxId = "P90113100217T1";
        this.count = 1000;
        this.dealPanel = dealPanel;
    }

    public FsnComProperty(String bankNo, String dotNo, String path, String boxId, PanelMessageI dealPanel) {
        this.bankNo = bankNo;
        this.dotNo = dotNo;
        this.path = path;
        this.boxId = boxId;
        this.count = 1000;
        this.dealPanel = dealPanel;
    }

    public FsnComProperty(String bankNo, String dotNo, String path, int count) {
        this.bankNo = bankNo;
        this.dotNo = dotNo;
        this.path = path;
        this.count = count;
    }

    /**
     * @return the bankNo
     */
    public String getBankNo() {
        return bankNo;
    }

    /**
     * @param bankNo the bankNo to set
     */
    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    /**
     * @return the dotNo
     */
    public String getDotNo() {
        return dotNo;
    }

    /**
     * @param dotNo the dotNo to set
     */
    public void setDotNo(String dotNo) {
        this.dotNo = dotNo;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * @return the dealPanel
     */
    public PanelMessageI getDealPanel() {
        return dealPanel;
    }

    /**
     * @param dealPanel the dealPanel to set
     */
    public void setDealPanel(PanelMessageI dealPanel) {
        this.dealPanel = dealPanel;
    }

    /**
     * @return the boxId
     */
    public String getBoxId() {
        return boxId;
    }

    /**
     * @param boxId the boxId to set
     */
    public void setBoxId(String boxId) {
        this.boxId = boxId;
    }

    /**
     * @return the xmlCfg
     */
    public XmlSax getXmlCfg() {
        return xmlCfg;
    }

    /**
     * @param xmlCfg the xmlCfg to set
     */
    public void setXmlCfg(XmlSax xmlCfg) {
        this.xmlCfg = xmlCfg;
    }

    /**
     * @return the mechinaType
     */
    public int getMechinaType() {
        return mechinaType;
    }

    /**
     * @param mechinaType the mechinaType to set
     */
    public void setMechinaType(int mechinaType) {
        this.mechinaType = mechinaType;
    }

    /**
     * @return the busType
     */
    public int getBusType() {
        return busType;
    }

    /**
     * @param busType the busType to set
     */
    public void setBusType(int busType) {
        this.busType = busType;
    }

    /**
     * @return the usr
     */
    public OperationUser getUsr() {
        return usr;
    }

    /**
     * @param usr the usr to set
     */
    public void setUsr(OperationUser usr) {
        this.usr = usr;
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
     * @return the limit
     */
    public int getLimit() {
        return limit;
    }

    /**
     * @param limit the limit to set
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * @return the moType
     */
    public int getMoType() {
        return moType;
    }

    /**
     * @param moType the moType to set
     */
    public void setMoType(int moType) {
        this.moType = moType;
    }

    /**
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip the ip to set
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }

}
