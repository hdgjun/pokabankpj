/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.poka.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author chenbo;
 * 系统配置信息
 */
@Entity
@Table(name ="ARG")
public class Arg implements Serializable{
    private int aId;
    private String akey;
    private String avalue;

    /**
     * @return the akey
     */
     @Id
     @Column(name ="akey",unique = true)
     @GeneratedValue(strategy = GenerationType.AUTO)
    public String getAkey() {
        return akey;
    }

    /**
     * @param akey the akey to set
     */
    public void setAkey(String akey) {
        this.akey = akey;
    }

    /**
     * @return the avalue
     */
    @Column(name ="avalue",unique = false)
    public String getAvalue() {
        return avalue;
    }

    
    /**
     * @param avalue the avalue to set
     */
    public void setAvalue(String avalue) {
        this.avalue = avalue;
    }
    
    public Arg(){
    
    }
    
    public Arg(String aKey,String aValue){
        this.akey = aKey;
        this.avalue = aValue;
    }

    /**
     * @return the aId
     */
    
    @Transient
    public int getaId() {
        return aId;
    }

    /**
     * @param aId the aId to set
     */
    public void setaId(int aId) {
        this.aId = aId;
    }
}