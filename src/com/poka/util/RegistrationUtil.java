/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poka.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Administrator
 */
public class RegistrationUtil {

    public static String getMachineCode() {
        String src = getCPUSerial() + getSerialNumber("C");

        return   src.replaceAll("-", "").toUpperCase().trim();
    }

    public static String getRegistration() {
        String szSrc = getMachineCode();
        return getRegistration(szSrc.toUpperCase().trim());
    }

    public static String getRegistration(String szSrc) {
        try {
            Security.addProvider(new com.sun.crypto.provider.SunJCE());
            final byte[] keyBytes = {(byte) 0x1A, (byte) 0x22, (byte) 0x4F, (byte) 0x58, (byte) 0x88, (byte) 0x10, (byte) 0x40,
                (byte) 0x38, (byte) 0x28, (byte) 0x25, (byte) 0x79, (byte) 0x51, (byte) 0xCB, (byte) 0xDD, (byte) 0x55,
                (byte) 0x66, (byte) 0x77, (byte) 0x29, (byte) 0x74, (byte) 0x98, (byte) 0x30, (byte) 0x40, (byte) 0x36, (byte) 0xE2
            };    //24字节的密钥
            String tem = new String(szSrc.getBytes(), "utf-8");
            byte[] encoded = encryptMode(keyBytes, tem.getBytes("utf-8"));

            StringBuilder bf = new StringBuilder();
            for (byte by : encoded) {
                if (by > 122) {
                    by = (byte) (by % 26 + 97);
                } else if (by > 90 && by < 97) {
                    by = (byte) (by - 20);
                } else if (by > 57 && by < 65) {
                    by = (byte) (by + 10);
                } else if (by < 48 && by >= 0) {
                    by = (byte) (by % 26 + 65);
                } else if (by < 0) {
                    by = (byte) (by % 26 + 90);
                }
                bf.append((char) by);
            }
            return bf.toString().toUpperCase();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(RegistrationUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
    private static final String Algorithm = "DESede"; //定义 加密算法,可用 DES,DESede,Blowfish
    //keybyte为加密密钥，长度为24字节
    //src为被加密的数据缓冲区（源）

    public static String getEncode() {
        final byte[] keyBytes = {(byte) 0x1A, (byte) 0x22, (byte) 0x4F, (byte) 0x58, (byte) 0x88, (byte) 0x10, (byte) 0x40,
            (byte) 0x38, (byte) 0x28, (byte) 0x25, (byte) 0x79, (byte) 0x51, (byte) 0xCB, (byte) 0xDD, (byte) 0x55,
            (byte) 0x66, (byte) 0x77, (byte) 0x29, (byte) 0x74, (byte) 0x98, (byte) 0x30, (byte) 0x40, (byte) 0x36, (byte) 0xE2
        };

        SecretKey deskey = new SecretKeySpec(keyBytes, Algorithm);
        return "format:" + deskey.getFormat() + "  Encoded" + deskey.getEncoded();
    }

    public static byte[] encryptMode(byte[] keybyte, byte[] src) {
        try {
            //生成密钥
            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);

            //加密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    //keybyte为加密密钥，长度为24字节
    //src为加密后的缓冲区
    public static byte[] decryptMode(byte[] keybyte, byte[] src) {
        try {
            //生成密钥
            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
            //解密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    //转换成十六进制字符串
    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";

        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
            if (n < b.length - 1) {
                hs = hs + ":";
            }
        }
        return hs.toUpperCase();
    }

    public static String getSerialNumber(String drive) {
        String result = "";
        try {
            File file = File.createTempFile("realhowto", ".vbs");
            file.deleteOnExit();
            FileWriter fw = new java.io.FileWriter(file);

            String vbs = "Set objFSO = CreateObject(\"Scripting.FileSystemObject\")\n"
                    + "Set colDrives = objFSO.Drives\n"
                    + "Set objDrive = colDrives.item(\"" + drive + "\")\n"
                    + "Wscript.Echo objDrive.SerialNumber";  
            fw.write(vbs);
            fw.close();
    
            Process p = Runtime.getRuntime().exec(System.getProperty("user.dir")+"\\cscript //NoLogo " + file.getPath());
            BufferedReader input
                    = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                result += line;
            }
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.trim();
    }

    public static String getCPUSerial() {
        String result = "";
        try {
            File file = File.createTempFile("tmp", ".vbs");
            file.deleteOnExit();
            FileWriter fw = new java.io.FileWriter(file);
            String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
                    + "Set colItems = objWMIService.ExecQuery _ \n"
                    + " (\"Select * from Win32_Processor\") \n"
                    + "For Each objItem in colItems \n" + " Wscript.Echo objItem.ProcessorId \n"
                    + " exit for   ' do the first cpu only! \n" + "Next \n";

            fw.write(vbs);
            fw.close();
            Process p = Runtime.getRuntime().exec(System.getProperty("user.dir")+"\\cscript //NoLogo " + file.getPath());
            BufferedReader input = new BufferedReader(new InputStreamReader(
                    p.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                result += line;
            }
            input.close();
            file.delete();
        } catch (IOException e) {
        }
        if (result.trim().length() < 1 ||  null == result) {
            result = "无 CPU_ID被读取 ";
        }
        return result.trim();
    }
}
