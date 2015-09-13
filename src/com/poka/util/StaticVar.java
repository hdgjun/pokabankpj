/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.poka.util;

import java.util.Map;

/**
 *全局变量帮助类
 * @author chenbo
 */

public class StaticVar {
    public static String loginUser = "";   //登录用户
    public static String loginName = "";
    public static String checkUser ="";   //审核用户
    public static Map<String,String> cfgMap;  //ftp信息Map
    public static String bankId = "";
    public static String agencyNo = "";
    public static String showSoftName = "";
    public static boolean monTcpListen = false;
    public static String isTimer = "0";  //0定时
    public static String noTimer = "1";  //1不定时
    public static String isZip = "0";  //0压缩
    public static String noZip = "1";  //1不压缩
    public static int guAoBodyOldLen = 88;  //古鳌旧协议主体长度为88个字节
    public static int guAoBodyNewLen = 96;  //古鳌新协议主体长度为96个字节
    
    
}
