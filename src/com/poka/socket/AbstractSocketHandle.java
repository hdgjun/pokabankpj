/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.poka.socket;

import java.util.Date;

/**
 *
 * @author Administrator
 */
public abstract class AbstractSocketHandle {
    public abstract void receData();
    
    public synchronized String getFsnName(String BankNo,String DotNo) {
        String bankNo = BankNo;
        if (null == bankNo) {
            bankNo = "*";
        }
        String dotNo = DotNo;
        if (null == dotNo) {
            dotNo = "*";
        }
        String date = (new java.text.SimpleDateFormat("yyyyMMddHHmmssSSS")).format(new Date());
        return bankNo + "_" + dotNo + "_XXXXXXXXXXXXXX_XXXXXXX" + date + ".FSN";
    }
}
