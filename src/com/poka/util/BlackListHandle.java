/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poka.util;

import java.util.ArrayList;
import java.util.List;


/**
 *冠字号黑名单处理
 * @author Administrator
 */
public class BlackListHandle {
    
    public static List<String> blackList = new ArrayList<String>();   //可疑币列表
    public static String rex = "";
    /**
     * 检查清分设备上传的冠字号是否为可疑币
     * @param sno 冠字号码
     * @return 
     */   
    public static boolean checkSno(String sno){
        if(sno == null || sno.equals(""))return false;
        for(String str : blackList){
            boolean isFlag = str.contains("*");
            if(isFlag){
                
            }else{
                if(str.equals(sno)){                        
                    return true;
                }
            }
            
        }                
        
        return false;
    }
    
    public static String checkSpecilSno(String blackSno){
        StringBuffer rxt = new StringBuffer("");
        for(char ch : blackSno.toCharArray()){
            if(ch == '*'){
                rxt.append("[0-9a-zA-z]");
            }else{
                rxt.append(ch);
            }
        }
        return null;
    } 
       
}
