package com.poka.entity;

import com.poka.util.StringUtil;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FsnBody {

    private String date = "";//yyyymmdd
    private String time = "";//hh:mimi:ss
    private String tfFlag = "";
    private byte[] errorCode;
    private String moneyFlag = "";
    private int ver;
    private int valuta;
    private int charNum;
    private String sNo = "";
    private String macinSno = "";
    private String reservel = "";
    private ImageSno imageSNo;
    private byte[] fbData;

    public void init() {
        int loca = 0;
        this.date = calculaDate(fbData, loca, 2);
        loca += 2;
        this.time = calculaTime(fbData, loca, 2);
        loca += 2;
        this.tfFlag = StringUtil.byteToIntString(fbData, loca, 2);
        loca += 2;
        //this.errorCode = StringUtil.byteToIntString(fbData, loca, 6);
        //this.errorCode[0] = (char) ((((char) fbData[loca]) << 8) + fbData[loca + 1]);
        this.errorCode[0] = this.fbData[loca];
        loca += 2;
        this.errorCode[1] = this.fbData[loca];
        loca += 2;
        this.errorCode[2] = this.fbData[loca];
        loca += 2;
        this.moneyFlag = StringUtil.byteToString(fbData, loca, 8);
        loca += 8;
        this.ver = StringUtil.byteToInt(fbData, loca, 2);
        loca += 2;
        this.valuta = StringUtil.byteToInt(fbData, loca, 2);
        loca += 2;
        this.charNum = StringUtil.byteToInt(fbData, loca, 2);
        loca += 2;
        this.sNo = StringUtil.byteToString(fbData, loca, 2 * 12);
        Pattern pattern = Pattern.compile("[0-9A-Za-z]{10}");
        Matcher matcher = pattern.matcher(this.sNo.trim());
        boolean b = matcher.matches();
        if (!b) {
            this.sNo = "**********";
        }
        loca += 2 * 12;
        this.macinSno = StringUtil.byteToString(fbData, loca, 2 * 24);
        loca += 2 * 24;
        this.reservel = StringUtil.byteToString(fbData, loca, 2);
        loca += 2;
        this.imageSNo.init();
    }

    public void reload() {
        this.fbData = new byte[100];
        int i;
        int loca = 0;
        stringDateToByte(this.fbData, loca, 2, this.date);
        loca += 2;
        stringTimeToByte(this.fbData, loca, 2, this.time);
        loca += 2;
        StringUtil.stringToByte(this.tfFlag, this.fbData, loca);
        loca += 2;
        this.fbData[loca] = this.errorCode[0];
        loca += 2;
        this.fbData[loca] = this.errorCode[1];
        loca += 2;
        this.fbData[loca] = this.errorCode[2];
        loca += 2;
        StringUtil.stringToByte2(this.moneyFlag, this.fbData, loca);
        loca += 8;
        StringUtil.intToByte(this.fbData, loca, 4, this.ver);
        loca += 2;
        StringUtil.intToByte(this.fbData, loca, 4, this.valuta);
        loca += 2;
        StringUtil.intToByte(this.fbData, loca, 4, this.charNum);
        loca += 2;
        StringUtil.stringToByte2(this.sNo, this.fbData, loca);
        loca += 24;
        StringUtil.stringToByte2(this.macinSno, this.fbData, loca);
        loca += 48;
        StringUtil.stringToByte2(this.reservel, this.fbData, loca);
        loca += 2;
        this.imageSNo.reload();
    }

    public void stringDateToByte(byte[] by, int start, int bLen, String s) {

        if (s == null) {
            return;
        }
        int nSt = 0;
        try {
            nSt = Integer.parseInt(s);
        } catch (NumberFormatException e) {
        }
        byte year = (byte) ((nSt / 10000) - 1980);
        byte mom = (byte) ((nSt / 100) % 100);
        byte date1 = (byte) (nSt % 100);

        by[start] = (byte) ((date1 & 0x1F) | ((mom << 5) & 0xE0));
        by[start + 1] = (byte) (((mom >> 3) & 0x01) | ((year << 1) & 0xFE));
    }

    public void stringTimeToByte(byte[] by, int start, int bLen, String s) { //12:23:32
        if (s == null) {
            return;
        }
        String[] sAr = s.split(":");
        if (sAr.length < 3) {
            return;
        }
        byte hh = (byte) Integer.parseInt(sAr[0]);
        byte mi = (byte) Integer.parseInt(sAr[1]);
        byte ss = (byte) Integer.parseInt(sAr[2]);

        by[start] = (byte) (((ss >> 1) & 0x1F) | ((mi << 5) & 0xE0));
        by[start + 1] = (byte) (((mi >> 3) & 0x07) | ((hh << 3) & 0xF8));
    }

    public String calculaDate(byte[] by, int start, int bLen) {
        String s = "";
        int year;
        int mom;
        int date;
        year = (int) ((by[start + 1] & 0xFE) >> 1) + 1980;
        s += year;
        mom = (int) (((by[start] & 0xE0) >> 5) + ((by[start + 1] & 0x01) << 3));
        if (mom < 10) {
            s += 0;
        }
        s += mom;
        date = (int) (by[start] & 0x1F);
        if (date < 10) {
            s += 0;
        }
        s += date;

        return s;
    }

    public String calculaTime(byte[] by, int start, int bLen) {
        String s = "";
        int hour;
        int mi;
        int sec;

        hour = (int) ((by[start + 1] & 0xF8) >> 3);
        if (hour < 10) {
            s += 0;
        }
        s += hour;
        s += ':';
        mi = (int) (((by[start] & 0xE0) >> 5) + ((by[start + 1] & 0x07) << 3));
        if (mi < 10) {
            s += 0;
        }
        s += mi;
        s += ':';
        sec = (int) ((by[start] & 0x1F) << 1);
        if (sec < 10) {
            s += 0;
        }
        s += sec;

        return s;
    }

    public FsnBody() {
        this.fbData = new byte[100];
        this.errorCode = new byte[3];
        this.imageSNo = new ImageSno();
    }

    public byte[] getFbData() {
        return fbData;
    }

    public void setFbData(byte[] fbData) {
        this.fbData = fbData;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTfFlag() {
        return tfFlag;
    }

    public void setTfFlag(String tfFlag) {
        this.tfFlag = tfFlag;
    }

    public byte[] getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(byte[] errorCode) {
        this.errorCode = errorCode;
    }

    public String getMoneyFlag() {
        return moneyFlag;
    }

    public void setMoneyFlag(String moneyFlag) {
        this.moneyFlag = moneyFlag;
    }

    public int getVer() {
        return ver;
    }

    public void setVer(int ver) {
        this.ver = ver;
    }

    public int getValuta() {
        return valuta;
    }

    public void setValuta(int valuta) {
        this.valuta = valuta;
    }

    public int getCharNum() {
        return charNum;
    }

    public void setCharNum(int charNum) {
        this.charNum = charNum;
    }

    public String getsNo() {
        return sNo;
    }

    public void setsNo(String sNo) {
        this.sNo = sNo;
    }

    public String getMacinSno() {
        return macinSno;
    }

    public void setMacinSno(String macinSno) {
        this.macinSno = macinSno;
    }

    public String getReservel() {
        return reservel;
    }

    public void setReservel(String reservel) {
        this.reservel = reservel;
    }

    public ImageSno getImageSNo() {
        return imageSNo;
    }

    public void setImageSNo(ImageSno imageSNo) {
        this.imageSNo = imageSNo;
    }

    @Override
    public String toString() {
        return "date:" + date + " time:" + time + " tfFlag:" + tfFlag
                + " errorCode:" + errorCode + " moneyFlag:" + moneyFlag
                + " ver:" + ver + " valuta:" + valuta + " charNum:" + charNum
                + " sNo:" + sNo + " macinSno:" + macinSno + " reservel:"
                + reservel + "imageSNo :" + imageSNo.toString();
    }

//    public static void main(String[] args){
//    Pattern pattern = Pattern.compile("[0-9A-Za-z]{10}");
//        Matcher matcher = pattern.matcher("'111110111");
//        boolean b = matcher.matches();
//        if (b) {
//           System.out.println("match");
//        }else{
//            System.out.println("not match!");
//        }
//     }
}
