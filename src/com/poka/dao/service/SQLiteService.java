/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poka.dao.service;

import com.poka.dao.impl.BaseDao;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class SQLiteService {

    private static BaseDao<Map> db;

    static {
        db = new BaseDao<>();
    }
    
    public static void clearMon() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        
        Map<String, Object> myArg = new HashMap<String, Object>();
        myArg.put("delTime",sdf.format(date));
       
        String sql = "delete from mondata where delTime <= :delTime";
        db.executeSql(sql, myArg);
    }

    public static boolean isRepeatMon(int type, String mon, int limit) {
        boolean flag = true;
        Map<String, Object> myArg = new HashMap<String, Object>();
        myArg.put("type", "" + type);
        myArg.put("mon", mon);
        String sql = "select count(*) co from mondata where type=:type and mon=:mon";
        System.out.println("sql="+sql);
        List<Map> result = null;
        try {
            result = db.findBySql(new String(sql.getBytes("utf-8")), myArg);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(SQLiteService.class.getName()).log(Level.SEVERE, null, ex);
        }
        int re = Integer.parseInt(result.get(0).get("co").toString().trim());
        System.out.println("re="+re);
        if (re > 0) {
            flag = false;
        } else {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.MINUTE, limit);
            Date dt1 = cal.getTime();
            String reStr = sdf.format(dt1);
            sql = "insert into mondata(type,mon,delTime) values(:type,:mon,:delTime) ";
            myArg.put("delTime", reStr);
            db.executeSql(sql, myArg);
        }
        return flag;
    }
}
