package com.poka.entity;

public class PokaFsnBody extends FsnBody {

    private String bundleId;
    private String userId1;
    private String userId2;
    private String bagId;
    private String atmId;
    private String userId3;
    private String userId4;
    private byte flag = 0x0000;

    private char[] pokaData;

    public PokaFsnBody() {
        super();
        pokaData = new char[105];
        this.bundleId = "";
        this.userId1 = "";
        this.userId2 = "";
        this.userId3 = "";
        this.userId4 = "";
        this.atmId = "";
        this.bagId = "";
        this.flag = 0x0000;
    }

    public void initPokaFsn() {
        super.init();
        String s = new String(pokaData);
        int loca = 0;
        this.bundleId = s.substring(loca, loca + 24);
        loca += 24;
        this.userId1 = s.substring(loca, loca + 8);
        loca += 8;
        this.userId2 = s.substring(loca, loca + 8);
        loca += 8;
        this.bagId = s.substring(loca, loca + 24);
        loca += 24;
        this.atmId = s.substring(loca, loca + 24);
        loca += 24;
        this.userId3 = s.substring(loca, loca + 8);
        loca += 8;
        this.userId4 = s.substring(loca, loca + 8);
        loca += 8;
        this.flag = (byte) (s.substring(loca, loca + 1).toCharArray()[0] - '0');
    }

    public void reloadPokaFsn() {
        super.reload();
        this.pokaData = new char[105];
        int loca = 0;
        if (this.bundleId != null && this.bundleId.toCharArray().length > 0) {
            System.arraycopy(this.bundleId.toCharArray(), 0, this.pokaData, loca,
                    this.bundleId.toCharArray().length);
        }
        loca += 24;
        if (this.userId1 != null && this.userId1.toCharArray().length > 0) {
            System.arraycopy(this.userId1.toCharArray(), 0, this.pokaData, loca, this.userId1.toCharArray().length);
        }
        loca += 8;
        if (this.userId2 != null && this.userId2.toCharArray().length > 0) {
            System.arraycopy(this.userId2.toCharArray(), 0, this.pokaData, loca, this.userId2.toCharArray().length);
        }
        loca += 8;
        if (this.bagId != null && this.bagId.toCharArray().length > 0) {
            System.arraycopy(this.bagId.toCharArray(), 0, this.pokaData, loca, this.bagId.toCharArray().length);
        }
        loca += 24;
        if (this.atmId != null && this.atmId.toCharArray().length > 0) {
            System.arraycopy(this.atmId.toCharArray(), 0, this.pokaData, loca, this.atmId.toCharArray().length);
        }
        loca += 24;
        if (this.userId3 != null && this.userId3.toCharArray().length > 0) {
            System.arraycopy(this.userId3.toCharArray(), 0, this.pokaData, loca, this.userId3.toCharArray().length);
        }
        loca += 8;
        if (this.userId4 != null && this.userId4.toCharArray().length > 0) {
            System.arraycopy(this.userId4.toCharArray(), 0, this.pokaData, loca, this.userId4.toCharArray().length);
        }
        loca += 8;
        this.pokaData[loca] = (char) flag;
    }

    public void reloadPokaFsn(String buId) {
        super.reload();
        this.pokaData = new char[105];
        int loca = 0;
        String temBun = null;
        if (this.bundleId.length() <= 0) {
            temBun = buId;
        } else {
            temBun = this.bundleId;
        }
        System.arraycopy(temBun.toCharArray(), 0, this.pokaData, loca,
                temBun.toCharArray().length);
        loca += 24;
        System.arraycopy(this.userId1.toCharArray(), 0, this.pokaData, loca, this.userId1.toCharArray().length);
        loca += 8;
        System.arraycopy(this.userId2.toCharArray(), 0, this.pokaData, loca, this.userId2.toCharArray().length);
        loca += 8;
        System.arraycopy(this.bagId.toCharArray(), 0, this.pokaData, loca, this.bagId.toCharArray().length);
        loca += 24;
        System.arraycopy(this.atmId.toCharArray(), 0, this.pokaData, loca, this.atmId.toCharArray().length);
        loca += 24;
        System.arraycopy(this.userId3.toCharArray(), 0, this.pokaData, loca, this.userId3.toCharArray().length);
        loca += 8;
        System.arraycopy(this.userId4.toCharArray(), 0, this.pokaData, loca, this.userId4.toCharArray().length);
        loca += 8;
        this.pokaData[loca] = (char) flag;
    }

    @Override
    public String toString() {
        return super.toString() + " \nbundleId:" + this.bundleId + " 打把员:"
                + this.userId1 + " 复核员:" + this.userId2 + " bagId:"
                + this.bagId + " atmId:" + this.atmId + " 加钞员:" + this.userId3
                + " 监督员:" + this.userId4 + " flag:" + this.flag;
    }

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    public String getUserId1() {
        return userId1;
    }

    public void setUserId1(String userId1) {
        this.userId1 = userId1;
    }

    public String getUserId2() {
        return userId2;
    }

    public void setUserId2(String userId2) {
        this.userId2 = userId2;
    }

    public String getBagId() {
        return bagId;
    }

    public void setBagId(String bagId) {
        this.bagId = bagId;
    }

    public String getAtmId() {
        return atmId;
    }

    public void setAtmId(String atmId) {
        this.atmId = atmId;
    }

    public String getUserId3() {
        return userId3;
    }

    public void setUserId3(String userId3) {
        this.userId3 = userId3;
    }

    public String getUserId4() {
        return userId4;
    }

    public void setUserId4(String userId4) {
        this.userId4 = userId4;
    }

    public byte getFlag() {
        return flag;
    }

    public void setFlag(byte flag) {
        this.flag = flag;
    }

    public char[] getPokaData() {
        return pokaData;
    }

    public void setPokaData(char[] pokaData) {
        this.pokaData = pokaData;
    }

}
