/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.poka.util;

import java.util.HashMap;
import java.util.Map;

/**
 *全局变量帮助类
 * @author chenbo
 */

public class StaticVar {
    public static String loginUser = "";   //登录用户
    public static String loginName = "";
    public static String checkUser ="";   //审核用户
    public final static Map<String,String> cfgMap = new HashMap<String, String>();  //ftp信息Map
    public static String bankId = "";
    public static String agencyNo = "";
    public static String showSoftName = "";
    public static boolean monTcpListen = false;
}
