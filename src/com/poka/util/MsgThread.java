/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.poka.util;

import com.poka.app.impl.PanelMessageI;
import com.poka.entity.PanelMsgEntity;

/**
 *
 * @author Administrator
 */
public class MsgThread implements Runnable {

    private PanelMsgEntity pMsg;
    private PanelMessageI dealPanel;
    private int type;
    
   
    @Override
    public void run() {
        this.dealPanel.showMessage(pMsg);
    }

    /**
     * @return the pMsg
     */
    public PanelMsgEntity getpMsg() {
        return pMsg;
    }

    /**
     * @param pMsg the pMsg to set
     */
    public void setpMsg(PanelMsgEntity pMsg) {
        this.pMsg = pMsg;
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
    
}
