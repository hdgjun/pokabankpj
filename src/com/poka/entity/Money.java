/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poka.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author poka
 */
@Entity
@Table(name = "MONDATA")
public class Money implements Serializable {

    private Integer id;
    private String type;
    private String mon;
    private String delTime;

    public Money() {

    }

    @Id()
    @Column(name = "ID")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the type
     */
    @Column(name = "TYPE")
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the mon
     */
    @Column(name = "MON")
    public String getMon() {
        return mon;
    }

    /**
     * @param mon the mon to set
     */
    public void setMon(String mon) {
        this.mon = mon;
    }

    /**
     * @return the delTime
     */
    @Column(name = "DELTIME")
    public String getDelTime() {
        return delTime;
    }

    /**
     * @param delTime the delTime to set
     */
    public void setDelTime(String delTime) {
        this.delTime = delTime;
    }

}
