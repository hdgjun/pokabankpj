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
public class MachinesCfg {

    private String machineType = null;
    private int machineNum;
    private String ip = null;
    private String user1 = null;
    private String user2 = null;
    private int type;

    /**
     * @return the machineType
     */
    public String getMachineType() {
        return machineType;
    }

    /**
     * @param machineType the machineType to set
     */
    public void setMachineType(String machineType) {
        this.machineType = machineType;
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
     * @return the user1
     */
    public String getUser1() {
        return user1;
    }

    /**
     * @param user1 the user1 to set
     */
    public void setUser1(String user1) {
        this.user1 = user1;
    }

    /**
     * @return the user2
     */
    public String getUser2() {
        return user2;
    }

    /**
     * @param user2 the user2 to set
     */
    public void setUser2(String user2) {
        this.user2 = user2;
    }

    /**
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * @return the machineNum
     */
    public int getMachineNum() {
        return machineNum;
    }

    /**
     * @param machineNum the machineNum to set
     */
    public void setMachineNum(int machineNum) {
        this.machineNum = machineNum;
    }
    
}
