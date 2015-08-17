package com.poka.util;

public class StringUtil {

    public static int byteToInt(byte[] by, int start, int bLen) {
        int res = 0;
        int i;
        for (i = start + bLen - 1; i >= start; i--) {
            res = (res << 4) + (int) ((by[i] >> 4) & 0x0f);
            res = (res << 4) + (int) (by[i] & 0x0f);
        }
        return res;
    }

    public static void byteToChar(char[] cVa, int len, int start, byte[] bVal) {
        for (int i = 0; i < len; i++) {
            cVa[i] = (char) bVal[start + i * 2 + 1];
            cVa[i] = (char) ((cVa[i] << 8) + bVal[start + i * 2]);
        }
    }

    public static void byteToChar2(char[] cVa, int len, int start, byte[] bVal) {
        for (int i = 0; i < len; i++) {
            cVa[i] = (char) bVal[start + i];
        }
    }

    public static byte[] CharToByte2(char[] cVa, int len, int start) {
        byte[] by = new byte[len];
        for (int i = 0; i < len; i++) {
            by[i] = (byte) cVa[start + i];
        }
        return by;
    }
       public static byte[] charToByte2(char[] cVa, int len, int start) {
        byte[] by = new byte[len];
        for (int i = 0; i < len; i++) {
            by[i] = (byte) cVa[start + i];
        }
        return by;
    }
    
    public static byte[] charToByte2(char[] cVa, int len, int start,byte[] by) {
        for (int i = 0; i < len; i++) {
            by[i+start] = (byte) cVa[i];
        }
        return by;
    }


    public static void stringToByte(String s, byte[] by, int loca) {
        char[] tem = s.toCharArray();
        for (char c : tem) {
            by[loca++] = (byte) (c & 0x0f);
            by[loca++] = (byte) ((c >> 8) & 0x0f);
        }
    }

    public static void stringToByte2(String s, byte[] by, int loca) {
        if (s.length() <= 0) {
            return;
        }
        char[] tem = s.toCharArray();
        for (char c : tem) {
            charToByte(by, loca, 0, c);
            loca += 2;
        }
    }

    public static void charToByte(byte[] b, int start, int end, char nValue) {
        b[start] = (byte) (nValue & 0x000000ff);
        b[start + 1] = (byte) ((nValue & 0x0000ff00) >> 8);
    }

    public static String byteToHexString(byte[] by, int start, int bLen) {
        StringBuilder bf = new StringBuilder();
        for (int i = start; i < start + bLen; i++) {
            byte c = by[i];
            bf.append((char) (((c >> 4) & 0x0f) > 9 ? (((c >> 4) & 0x0f) % 10 + 'A')
                    : ((c >> 4) & 0x0f) + '0'));
            bf.append((char) ((c & 0x0f) > 9 ? (char) ((c & 0x0f) % 10 + 'A')
                    : (c & 0x0f) + '0'));
        }
        return bf.toString();
    }

    public static String byteToIntString(byte[] by, int start, int bLen) {
        StringBuilder bf = new StringBuilder();
        for (int i = 0; i < bLen / 2; i++) {
            bf.append(byteToInt(by, start, 2));
            start += 2;
        }
        return bf.toString();
    }

    public static String byteToString(byte[] by, int start, int bLen) {
        StringBuilder bf = new StringBuilder();
        for (int i = 0; i < bLen / 2; i++) {
            char c = (char) by[start + i * 2 + 1];
            c = (char) ((char) (c << 8) + (char) by[start + i * 2]);
            if (c != 0) {
                bf.append((char) (c));
            }
        }
        return bf.toString();
    }

    public static void intToByte(byte[] b, int start, int end, int nValue) {
        b[start] = (byte) (nValue & 0x000000ff);
        b[start + 1] = (byte) ((nValue & 0x0000ff00) >> 8);
        b[start + 2] = (byte) ((nValue & 0x00ff0000) >> 16);
        b[start + 3] = (byte) ((nValue & 0xff000000) >> 24);
    }
    
    public static byte[] intToByte(int nValue) {
        byte[] b = new byte[4];
        b[0] = (byte) (nValue & 0x000000ff);
        b[1] = (byte) ((nValue & 0x0000ff00) >> 8);
        b[2] = (byte) ((nValue & 0x00ff0000) >> 16);
        b[3] = (byte) ((nValue & 0xff000000) >> 24);
        return b;
    }
    
    public static byte[] intToByte16(int nValue) {
        byte[] b = new byte[2];
        b[0] = (byte) (nValue & 0x000000ff);
        b[1] = (byte) ((nValue & 0x0000ff00) >> 8);
        return b;
    }

    public static int byteToInt16(byte[] by, int start) {
        int res = 0;
        int i;
        i = start;
        byte c = by[i];
        byte d = by[i + 1];

        res = (res << 4) + (int) ((d >> 4) & 0x0f);
        res = (res << 4) + (int) (d & 0x0f);

        res = (res << 4) + (int) ((c >> 4) & 0x0f);
        res = (res << 4) + (int) (c & 0x0f);

        return res;
    }
    
    public static int byteToInt32(byte[] by, int start) {
        int res = 0;
        int i;
        i = start;
        byte c = by[i];
        byte d = by[i + 1];
        byte e = by[i + 2];
        byte f = by[i + 3];

        res = (res << 4) + (int) ((f >> 4) & 0x0f);
        res = (res << 4) + (int) (f & 0x0f);

        res = (res << 4) + (int) ((e >> 4) & 0x0f);
        res = (res << 4) + (int) (e & 0x0f);

        res = (res << 4) + (int) ((d >> 4) & 0x0f);
        res = (res << 4) + (int) (d & 0x0f);

        res = (res << 4) + (int) ((c >> 4) & 0x0f);
        res = (res << 4) + (int) (c & 0x0f);

        return res;
    }

    public static String byteToString2(byte[] by, int start, int bLen) {
        StringBuilder bf = new StringBuilder();
        for (int i = start; i < start + bLen; i++) {
            bf.append((char) by[i]);
        }
        return bf.toString();
    }

    public static String byteToHexString2(byte[] by, int start, int bLen) {
        StringBuilder bf = new StringBuilder();
        for (int i = start; i < start + bLen - 3; i += 4) {

            byte c = by[i];
            byte d = by[i + 1];
            byte e = by[i + 2];
            byte f = by[i + 3];

            bf.append((char) (((f >> 4) & 0x0f) > 9 ? (((f >> 4) & 0x0f) % 10 + 'A')
                    : ((f >> 4) & 0x0f) + '0'));
            bf.append((char) ((f & 0x0f) > 9 ? (char) ((f & 0x0f) % 10 + 'A')
                    : (f & 0x0f) + '0'));

            bf.append((char) (((e >> 4) & 0x0f) > 9 ? (((e >> 4) & 0x0f) % 10 + 'A')
                    : ((e >> 4) & 0x0f) + '0'));
            bf.append((char) ((e & 0x0f) > 9 ? (char) ((e & 0x0f) % 10 + 'A')
                    : (e & 0x0f) + '0'));

            bf.append((char) (((d >> 4) & 0x0f) > 9 ? (((d >> 4) & 0x0f) % 10 + 'A')
                    : ((d >> 4) & 0x0f) + '0'));
            bf.append((char) ((d & 0x0f) > 9 ? (char) ((d & 0x0f) % 10 + 'A')
                    : (d & 0x0f) + '0'));

            bf.append((char) (((c >> 4) & 0x0f) > 9 ? (((c >> 4) & 0x0f) % 10 + 'A')
                    : ((c >> 4) & 0x0f) + '0'));
            bf.append((char) ((c & 0x0f) > 9 ? (char) ((c & 0x0f) % 10 + 'A')
                    : (c & 0x0f) + '0'));

        }
        return bf.toString();
    }

}
