package com.poka.entity;

import com.poka.util.StringUtil;

public class FsnHead {

    private byte[] headBody;
    private char[] headStart;
    private char[] headString;
    private int count;
    private char[] headEnd;

    //0x2e有图 0x2d 无图
    public FsnHead() {
        this.headStart = new char[]{20, 10, 7, 26};
        this.headString = new char[]{0, 1, 0x2E, 'S', 'N', 'o'};
        this.headEnd = new char[]{0, 1, 2, 3};
        this.headBody = new byte[32];
        this.count = 0;
    }

    public FsnHead(int count) {
        this.headStart = new char[]{20, 10, 7, 26};
        this.headString = new char[]{0, 1, 0x2E, 'S', 'N', 'o'};
        this.count = count;
        this.headEnd = new char[]{0, 1, 2, 3};
        this.headBody = new byte[32];
    }

    public byte[] getFsnHead() {
        byte[] head = new byte[32];
        int location = 0;
        for (char s : headStart) {
            StringUtil.charToByte(head, location, 2, s);
            location += 2;
        }
        for (char s : headString) {
            StringUtil.charToByte(head, location, 2, s);
            location += 2;
        }
        StringUtil.intToByte(head, location, 4, count);
        location += 4;
        for (char s : headEnd) {
            StringUtil.charToByte(head, location, 2, s);
            location += 2;
        }
        return head;
    }

    public byte[] getHeadBody() {
        return this.headBody;
    }

    public void setHeadBody(byte[] headBody) {
        int loca = 0;
        this.headBody = headBody;
        StringUtil.byteToChar(this.headStart, this.headStart.length, loca, this.headBody);
        loca += this.headStart.length * 2;
        StringUtil.byteToChar(this.headString, this.headString.length, loca, this.headBody);
        loca += this.headString.length * 2;
        this.count = StringUtil.byteToInt(this.headBody, loca, 4);
        //System.out.println("count="+count);
        loca += 4;
        StringUtil.byteToChar(this.headEnd, this.headEnd.length, loca, this.headBody);
    }

    public char[] getHeadStart() {
        return headStart;
    }

    public void setHeadStart(char[] headStart) {
        this.headStart = headStart;
    }

    public char[] getHeadString() {
        return headString;
    }

    public void setHeadString(char[] headString) {
        this.headString = headString;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public char[] getHeadEnd() {
        return headEnd;
    }

    public void setHeadEnd(char[] headEnd) {
        this.headEnd = headEnd;
    }

}
