/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poka.entity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class BfFile {

    private List<BfBody> list;

    public BfFile() {
        list = new ArrayList<BfBody>();
    }

    public boolean readBfFile(String fPath) throws IOException {
        File f = new File(fPath);
        if (f.exists()) {
            FileInputStream input = new FileInputStream(f);
            list.clear();
            int len = 0;
            BfBody tem = new BfBody();
            while ((len = input.read(tem.getBodyData())) > 0) {
                tem.init();
                list.add(tem);
                tem = new BfBody();
            }
            input.close();
            return true;
        }
        return true;
    }

    public boolean writeBfFile(String fPath) throws IOException {
        File f = new File(fPath);
        if (!f.exists()) {
            File pf = f.getParentFile();
            if (!pf.exists()) {
                pf.mkdirs();
            }
            if (!f.createNewFile()) {
                System.out.println("Create new file error!");
                return false;
            }
        } else {
            f.delete();
        }
        FileOutputStream output = new FileOutputStream(f);
        for (BfBody bo : this.list) {
            bo.reload();
            output.write(bo.getBodyData());
        }
        output.flush();
        output.close();
        return true;
    }

    public static String createBfFileName(String bankId, String dotId, String boxId) {
        String tem = "XXXXXXXXXXXXXXX";
        String bank = bankId.length() > 4 ? bankId.substring(0, 4) : bankId;
        if (bank.length() < 4) {
            bank = tem.substring(0, 4 - bank.length()) + bank;
        }
        String dot = dotId.length() > 4 ? dotId.substring(0, 4) : dotId;
        if (dot.length() < 4) {
            dot = tem.substring(0, 4 - dot.length()) + dot;
        }
        String box = boxId.length() > 14 ? boxId.substring(0, 14) : boxId;
        if (box.length() < 14) {
            box = tem.substring(0, 14 - box.length()) + box;
        }
        
        return bank + "_" + dot + "_" + box + "_XXXXXXX" + (new java.text.SimpleDateFormat("yyyyMMddHHmmssSSS")).format(new Date()) + ".BF";
    }
    
    public boolean repeat(String atm,String kun){
        for(BfBody tem :this.list){
            if(tem.getAtmId().trim().equals(atm.trim())&&tem.getBundleId().trim().equals(kun.trim())||
                    tem.getMonBoxId().trim().equals(atm.trim())&&tem.getBundleId().trim().equals(kun.trim())||
                    tem.getAtmId().trim().equals(atm.trim())&&tem.getMonBoxId().trim().equals(kun.trim())){
                return false;
            }
        }
        return true;
    }
    public void clear(){
        this.list.clear();
    }
    public void deleteAt(int item){
        this.list.remove(item);
    }
    public void add(BfBody item){
        this.list.add(item);
    }

    /**
     * @return the list
     */
    public List<BfBody> getList() {
        return list;
    }

    /**
     * @param list the list to set
     */
    public void setList(List<BfBody> list) {
        this.list = list;
    }

    public int getCount() {
        return this.list.size();
    }
}
