/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poka.app.impl;

import com.poka.entity.PanelMsgEntity;

/**
 *
 * @author Administrator
 */
public interface PanelMessageI {

    /**
     *
     * @param msg
     */
    public void showMessage(PanelMsgEntity msg);

    public void controlButton(int rows);

    public void flashMach();

    public void flashMachClient();
    
}
