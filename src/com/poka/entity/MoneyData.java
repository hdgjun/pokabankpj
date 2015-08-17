/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.poka.entity;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author poka
 */
@Entity
public class MoneyData implements Serializable{    
    private BigInteger id;
    private Date coltime;
    private String mon;
    private String monType;
    private String monVal;
    private String monVer;
    private String trueFlag;
    private String quanLity;
    private String operatOrid;
    private Date insertDatetime; 
    private String machineId;
       
    public String getMon() {
            return mon;
    }
    public void setMon(String mon) {
            this.mon = mon;
    }
    public String getMonType() {
            return monType;
    }
    public void setMonType(String monType) {
            this.monType = monType;
    }
    public String getMonVal() {
            return monVal;
    }
    public void setMonVal(String monVal) {
            this.monVal = monVal;
    }
    public String getMonVer() {
            return monVer;
    }
    public void setMonVer(String monVer) {
            this.monVer = monVer;
    }
    public String getTrueFlag() {
            return trueFlag;
    }
    public void setTrueFlag(String trueFlag) {
            this.trueFlag = trueFlag;
    }
    public String getQuanLity() {
            return quanLity;
    }
    public void setQuanLity(String quanLity) {
            this.quanLity = quanLity;
    }
    public String getOperatOrid() {
            return operatOrid;
    }
    public void setOperatOrid(String operatOrid) {
            this.operatOrid = operatOrid;
    }

    public Date getColtime() {
            return coltime;
    }
    public void setColtime(Date coltime) {
            this.coltime = coltime;
    }
    public Date getInsertDatetime() {
            return insertDatetime;
    }
    public void setInsertDatetime(Date insertDatetime) {
            this.insertDatetime = insertDatetime;
    }
    public MoneyData() {
            super();
    }

    /**
     * @return the id
     */
     @Id    
    public BigInteger getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(BigInteger id) {
        this.id = id;
    }
    
    public MoneyData(Date coltime,BigInteger id,Date insert,String mon,String monType,String monVal,String monVer,String operatOrid,String quanLity,String trueFlag,String machineId){
        this.coltime = coltime;
        this.id = id;
        this.insertDatetime = insert;
        this.mon = mon;
        this.monType = monType;
        this.monVal = monVal;
        this.monVer = monVer;
        this.operatOrid = operatOrid;
        this.quanLity = quanLity;
        this.trueFlag = trueFlag;    
        this.machineId = machineId;
    }

    /**
     * @return the machineId
     */
    public String getMachineId() {
        return machineId;
    }

    /**
     * @param machineId the machineId to set
     */
    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }
    
}
