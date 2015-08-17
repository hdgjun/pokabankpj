package com.poka.entity;

import com.poka.util.StringUtil;

public class ImageSno {

    private int num;
    private int height;
    private int width;
    private int Resever2;
    private ImageSnoData sN0[];
    private byte[] imData;

    public ImageSno() {
        this.sN0 = new ImageSnoData[12];
        for (int i = 0; i < 12; i++) {
            this.sN0[i] = new ImageSnoData();
        }
        this.imData = new byte[1544];
    }

    public void init() {
        int loca = 0;
        this.num = (char) StringUtil.byteToInt(imData, loca, 2);
        loca += 2;
        this.height = (char) StringUtil.byteToInt(imData, loca, 2);
        loca += 2;
        this.width = (char) StringUtil.byteToInt(imData, loca, 2);
        loca += 2;
        this.Resever2 = (char) StringUtil.byteToInt(imData, loca, 2);
        loca += 2;

        for (ImageSnoData c : this.sN0) {
             byte[] da = c.getData();
            for (int i = 0; i < 128; i++) {
                da[i] = imData[loca];
                loca += 1;
            }
        }
    }

    public void reload() {
         byte[] tem = new byte[8];
         int loca = 0;
         StringUtil.charToByte(imData, loca, 2, (char)this.num);
         loca+=2;
         StringUtil.charToByte(imData, loca, 2, (char)this.height);
         loca+=2;
         StringUtil.charToByte(imData, loca, 2, (char)this.width);
         loca+=2;
         StringUtil.charToByte(imData, loca, 2, (char)this.Resever2);
         loca+=2;
         for (ImageSnoData c : this.sN0) {
             byte[] da = c.getData();
            for (int i = 0; i < 128; i++) {
                imData[loca] = da[i]; 
                loca += 1;
            }
        }
    }

    public byte[] getImData() {
        return imData;
    }

    public void setImData(byte[] imData) {
        this.imData = imData;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return  width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getResever2() {
        return Resever2;
    }

    public void setResever2(int resever2) {
        Resever2 = resever2;
    }

    public ImageSnoData[] getsN0() {
        return sN0;
    }

    public void setsN0(ImageSnoData[] sN0) {
        this.sN0 = sN0;
    }

    @Override
    public String toString() {
        String s = "{ num:" + (int) this.num + " ,width:" + (int) this.width + " ,height:" + (int) this.height + " ,sN0:\n{\n";

        for (ImageSnoData i : sN0) {
            s += i.toString();
        }
        s += "}";
        //s+="\n imData:{"+StringUtil.byteToHexString(imData,8,1544-8);
        return s;
    }

   
}
