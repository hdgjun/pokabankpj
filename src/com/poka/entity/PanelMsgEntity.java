/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.poka.entity;

/**
 *
 * @author Administrator
 */
public class PanelMsgEntity { 
    private int busyType;
    private int linkType;  //服务器类型或客户端类型
    
    private String cmdMsg;
    private String dataMsg; 
    private String ip;
    private int state;

    public final static int clientType = 0;
    public final static int serverType = 1;
    
    public final static int connectMSGType = 0;
    public final static int monMSGType = 1;
    public final static int cmdType = 2;
    public final static int addCmdType = 3;
    public final static int addMonType = 4;
    
    public final static int closeState = 0;
    public final static int connectState = 1;
    public final static int rceState = 2;
    
    public final static String[] stateString = new String[]{
      "未连接","已连接" ,"接收数据" 
    };
    
    public PanelMsgEntity(int busyType,String cmdMsg,String dataMsg,String ip,int state,int linkType){
        this.busyType = busyType;
        this.cmdMsg = cmdMsg;
        this.dataMsg = dataMsg; 
        this.ip = ip;
        this.state = state;
        this.linkType = linkType;
    }
    /**
     * @return the cmdMsg
     */
    public String getCmdMsg() {
        return cmdMsg;
    }

    /**
     * @param cmdMsg the cmdMsg to set
     */
    public void setCmdMsg(String cmdMsg) {
        this.cmdMsg = cmdMsg;
    }

    /**
     * @return the dataMsg
     */
    public String getDataMsg() {
        return dataMsg;
    }

    /**
     * @param dataMsg the dataMsg to set
     */
    public void setDataMsg(String dataMsg) {
        this.dataMsg = dataMsg;
    }

    /**
     * @return the busyType
     */
    public int getBusyType() {
        return busyType;
    }

    /**
     * @param busyType the busyType to set
     */
    public void setBusyType(int busyType) {
        this.busyType = busyType;
    }

    /**
     * @return the state
     */
    public int getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(int state) {
        this.state = state;
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
     * @return the linkType
     */
    public int getLinkType() {
        return linkType;
    }

    /**
     * @param linkType the linkType to set
     */
    public void setLinkType(int linkType) {
        this.linkType = linkType;
    }
    
}
