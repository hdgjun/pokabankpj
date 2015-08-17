/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poka.util;

import com.poka.printer.PostekPple;
import com.poka.printer.com.ComClient;
import com.poka.util.Converter;
import com.poka.util.LogManager;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class PostekUtil {
    static final Logger logger = LogManager.getLogger(PostekUtil.class);

    public static void main(String args[]) {
        PostekUtil post = new PostekUtil();
        post.printGX100X50FQ("COM19", "123456789012345678901234", "中国工商银行");
        //   post.printGX100X50("COM19", "2014-10-24", "测试员一", "50", "测试员二", "123456789012345678901234", "中国工商银行", 10);
    }

    public void printWZ(String com, String fq_date, String fq_repeat, String fq_value, String fq_fengkun, String fq_num, String bankName, double kunNum) {
        int bankNameLen = bankName.length();
        int item = (12 - bankNameLen - 2) * 5;
        if (item > 0) {
            item = item / 2;
        }

        try {
            ComClient client = new ComClient(com);

            String tem = null;
            tem = PostekPple.cleanPictureBuffer();
            client.writeString(tem);

            tem = "H15\r\n";
            client.writeString(tem);
            //边框
            tem = PostekPple.printRectangle(2, 5, 0.4, 73, 140 + 5);
            client.writeString(tem);
            tem = PostekPple.printRectangle(11 + 11 + 3, 5 + 6, 0.4, 11 + 13 + 8 + 3, 2 + 6 + 32);
            client.writeString(tem);
            //线条
            tem = PostekPple.printLine(11 + 2, 5, 0.4, 140);
            client.writeString(tem);
            tem = PostekPple.printLine(43 + 11 + 2 + 3, 5, 0.4, 140);
            client.writeString(tem);
            tem = PostekPple.printLine(2, 22, 11, 0.4);
            client.writeString(tem);
            tem = PostekPple.printLine(2, 22 + 25 + 5, 11, 0.4);
            client.writeString(tem);
            tem = PostekPple.printLine(2, 22 + 25 + 22, 11, 0.4);
            client.writeString(tem);
            tem = PostekPple.printLine(2, 22 + 25 + 22 + 25 + 5, 11, 0.4);
            client.writeString(tem);
            tem = PostekPple.printLine(2, 22 + 25 + 22 + 25 + 22, 11, 0.4);
            client.writeString(tem);
            //encoding
            //日期 
            tem = PostekPple.pringText(2 + 3 + 6, 5 + 3, 1, '4', 2, 1, 'N', "");
            client.write(tem);
            client.writeGB2312("日  期");
            client.write("\"\r\n");

            tem = PostekPple.pringText(2 + 3 + 5, 5 + 22, 1, '4', 2, 1, 'N', "");
            client.write(tem);
            client.write(fq_date);
            client.write("\"\r\n");

            //封捆员
            tem = PostekPple.pringText(2 + 3 + 6, 5 + 22 + 25 + 3, 1, '4', 2, 1, 'N', "封捆员");
            client.write(tem);
            client.writeGB2312("封捆员");
            client.write("\"\r\n");

            tem = PostekPple.pringText(2 + 3 + 6, 5 + 22 + 25 + 22, 1, '4', 2, 1, 'N', "陈国标");
            client.write(tem);
            client.writeGB2312(fq_fengkun);
            client.write("\"\r\n");

            //复核员
            tem = PostekPple.pringText(2 + 3 + 6, 5 + 22 + 25 + 22 + 25 + 3, 1, '4', 2, 1, 'N', "复核员");
            client.write(tem);
            client.writeGB2312("复核员");
            client.write("\"\r\n");
            tem = PostekPple.pringText(2 + 3 + 6, 5 + 22 + 25 + 22 + 25 + 22, 1, '4', 2, 1, 'N', "陈国标");
            client.write(tem);
            client.writeGB2312(fq_repeat);
            client.write("\"\r\n");
            int shitY = 4;
            //判断币值
            String value = "";
            int ySh = 5;
            if (fq_value.equals("100")) {
                //人民币壹佰元卷 
                shitY = 0;
                tem = PostekPple.pringText(11 + 13 + 2 + 2 + 2 + 3, 5 + 6 + 3, 1, '2', 2, 1, 'N', "人民币壹佰元券");
                client.write(tem);
                client.writeGB2312("人民币壹佰元券");
                client.write("\"\r\n");
                value = Converter.digitUppercase(kunNum * 100 * 100);
                ySh = 0;
            } else if (fq_value.equals("50")) {
                //人民币五十元卷 
                tem = PostekPple.pringText(11 + 13 + 2 + 2 + 2 + 3, 5 + 6 + 3, 1, '2', 2, 1, 'N', "人民币伍拾元券");
                client.write(tem);
                client.writeGB2312("人民币伍拾元券");
                client.write("\"\r\n");
                value = Converter.digitUppercase(kunNum * 50 * 100);
            } else if (fq_value.equals("20")) {
                //人民币二十元卷 
                tem = PostekPple.pringText(11 + 13 + 2 + 2 + 2 + 3, 5 + 6 + 3, 1, '2', 2, 1, 'N', "人民币贰拾元券");
                client.write(tem);
                client.writeGB2312("人民币贰拾元券");
                client.write("\"\r\n");
                value = Converter.digitUppercase(kunNum * 20 * 100);
            } else {
                //人民币十元卷 
                tem = PostekPple.pringText(11 + 13 + 2 + 2 + 2 + 3, 5 + 6 + 3, 1, '2', 2, 1, 'N', "人民币拾元券");
                client.write(tem);
                client.writeGB2312("人民币拾元券");
                client.write("\"\r\n");
                value = Converter.digitUppercase(kunNum * 10 * 100);
            }

            //壹拾万园整
            tem = PostekPple.pringText(2 + 11 + 13 + 15 + 5 + 5 + 3, 5 + 45 + ySh, 1, '4', 3, 3, 'N', "壹拾万圆整");
            client.write(tem);
            client.writeGB2312(value);
            client.write("\"\r\n");

            //中国工商银行鄂州分行封签
            tem = PostekPple.pringText(2 + 11 + 43 + 2 + 10 + 3, 5 + 50 - 15 + 4 + item, 1, '4', 3, 2, 'N', "中国工商银行鄂州分行封签");
            client.write(tem);
            client.writeGB2312(bankName + "封签");
            client.write("\"\r\n");
//            
            //编号
            tem = PostekPple.pringText(2 + 11 + 5 + 4 + 3, 5 + 22 + 17 + 3 - 2, 1, '2', 1, 1, 'N', "编号：");
            client.write(tem);
            client.writeGB2312("编号: ");
            client.write("\"\r\n");
            tem = PostekPple.pringText(2 + 11 + 5 + 4 + 3, 5 + 22 + 17 + 11, 1, '4', 1, 1, 'N', "");
            client.write(tem);
            client.write(fq_num);
            client.write("\"\r\n");

            //一维码
            tem = PostekPple.printOneDimensionalCode(2 + 11 + 13 + 12 + 3, 5 + 22 + 18, 1, "1", 0.4, 1, 15, 'N', fq_num);
            client.writeString(tem);
    
//            
            //二维码 b664,250,QR,0,0,o0,r5,m2,g0,s0
            tem = PostekPple.printTwoDimensionalCode(2 + 11 + 7 + 3, 5 + 22 + 25 + 22 + 25 + 20, "QR", 15, 15, "o0", "r9", "m2", "g0", "s1", fq_num);
            client.writeString(tem);

            int xSh = 0;
            //椭圆
            if (fq_num.charAt(10) == '2') {
                tem = PostekPple.printPicture(11 + 13 + 2 + 6 + 3, 5 + 6 + 2 + 1, 20, 192);
                client.writeString(tem);
                client.write(PostekPple.btuoyuan);
            } else {
                xSh = -3;
                //三角
                tem = PostekPple.printLine(37, 11, 1, 30);
                client.writeString(tem);

                tem = PostekPple.printSLine(37, 11, 1, 56, 26);
                client.writeString(tem);

                tem = PostekPple.printSLine(56, 26, 1, 37, 41);
                client.writeString(tem);

            }

            //   tem = PostekPple.pringText(11 + 13 + 2 + 15 + 5 + 3 -2, 5 + 6 + 5.5 , 1, '3', 4, 4, 'N', "100");
            //面值
            tem = PostekPple.pringText(11 + 13 + 2 + 15 + 5 + 3 + xSh, 5 + 6 + 5.5 + shitY, 1, '3', 4, 4, 'N', "100");
            client.write(tem);
            client.write(fq_value);
            client.write("\"\r\n");

            tem = PostekPple.printLabel(1);
            client.writeString(tem);

            client.close();
        } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException | PortInUseException | UnsupportedCommOperationException | NoSuchPortException ex) {
           logger.log(Level.INFO, null,ex);
        }
    }

    public void printGX100X50(String com, String fq_date, String fq_repeat, String fq_value, String fq_fengkun, String fq_num, String bankName, double kunNum) {
        int bankNameLen = bankName.length();
        int item = (12 - bankNameLen - 2) * 5;
        if (item > 0) {
            item = item / 2;
        }

        try {
            ComClient client = new ComClient(com);

            String tem = null;
            tem = PostekPple.cleanPictureBuffer();
            client.writeString(tem);

            tem = "H15\r\n";
            client.writeString(tem);
            //边框
//            tem = PostekPple.printRectangle(0, 0, 0.4, 50, 100);
//            client.writeString(tem);

            double xShit = -5;
            tem = PostekPple.printLine(0 + xShit, 10, 82, 0.4);
            client.writeString(tem);

            int shit = 0;
            tem = PostekPple.pringText(0 + xShit, 11, shit, '4', 1, 1, 'N', "人民币:");
            client.write(tem);
            client.writeGB2312("人民币:");
            client.write("\"\r\n");

            tem = PostekPple.pringText(50 + xShit, 11, shit, '4', 2, 1, 'N', "第五版(纸)");
            client.write(tem);
            client.writeGB2312("第五版(纸)");
            client.write("\"\r\n");

            //日期 
            tem = PostekPple.pringText(0 + xShit, 26, shit, '4', 1, 1, 'N', "");
            client.write(tem);
            client.writeGB2312("日  期:");
            client.write("\"\r\n");

            tem = PostekPple.pringText(14 + xShit, 26, shit, '4', 1, 1, 'N', "");
            client.write(tem);
            client.write(fq_date);
            client.write("\"\r\n");

            //封捆员
            tem = PostekPple.pringText(0 + xShit, 21, shit, '4', 1, 1, 'N', "封 包:");
            client.write(tem);
            client.writeGB2312("封  包:");
            client.write("\"\r\n");
            tem = PostekPple.pringText(14 + xShit, 21, shit, '4', 2, 1, 'N', "封包员");
            client.write(tem);
            client.writeGB2312(fq_fengkun);
            client.write("\"\r\n");

            //复核员
            tem = PostekPple.pringText(0 + xShit, 16, shit, '4', 1, 1, 'N', "复 核:");
            client.write(tem);
            client.writeGB2312("复  核:");
            client.write("\"\r\n");
            tem = PostekPple.pringText(14 + xShit, 16, shit, '4', 2, 1, 'N', "复核员");
            client.write(tem);
            client.writeGB2312(fq_repeat);
            client.write("\"\r\n");

            int shitY = 4;
            //判断币值
            String value = "";
            if (fq_value.equals("100")) {
                //人民币壹佰元卷 
                value = Converter.digitUppercase(kunNum * 100 * 100);
                shitY = 0;
            } else if (fq_value.equals("50")) {
                //人民币五十元卷 
                value = Converter.digitUppercase(kunNum * 50 * 100);
            } else if (fq_value.equals("20")) {
                //人民币二十元卷 
                value = Converter.digitUppercase(kunNum * 20 * 100);
            } else {
                //人民币十元卷 
                value = Converter.digitUppercase(kunNum * 10 * 100);
            }

            //壹拾万园整
            tem = PostekPple.pringText(14 + xShit, 11, shit, '4', 2, 1, 'N', "壹拾万圆整");
            client.write(tem);
            client.writeGB2312(value);
            client.write("\"\r\n");

            //中国工商银行鄂州分行封签
            tem = PostekPple.pringText(10 + item + xShit, 3, shit, '4', 2, 2, 'N', "中国工商银行鄂州分行封签");
            client.write(tem);
            client.writeGB2312(bankName);
            client.write("\"\r\n");

            //编号
            if (fq_num.length() > 4) {
                tem = PostekPple.pringText(10 + xShit, 45, shit, '4', 1, 1, 'N', "");
                client.write(tem);
                client.write(fq_num.substring(0, fq_num.length() - 4));
                client.write("\"\r\n");

                tem = PostekPple.pringText(50 + xShit, 45, shit, '4', 2, 1, 'N', "");
                client.write(tem);
                client.write(fq_num.substring(fq_num.length() - 4, fq_num.length()));
                client.write("\"\r\n");
            } else {
                tem = PostekPple.pringText(5 + xShit, 20, shit, '4', 1, 1, 'N', "");
                client.write(tem);
                client.write(fq_num);
                client.write("\"\r\n");
            }
            //一维码
            tem = PostekPple.printOneDimensionalCode(10 + xShit, 33, shit, "1", 0.4, 0.5, 11, 'N', fq_num);
            client.writeString(tem);

            int xSh = 0;
            //椭圆
            if (fq_num.charAt(10) == '2') {
//            if (true) {
                tem = PostekPple.printPicture(50 + xShit, 14, 24, 150);
                client.writeString(tem);
                client.write(PostekPple.btuoyuan2);
            } else {
                xSh = 1;
                //三角
                tem = PostekPple.printLine(45 + xShit, 30, 32, 0.5);
                client.writeString(tem);

                tem = PostekPple.printSLine(45 + xShit, 30, 0.5, 61 + xShit, 14);
                client.writeString(tem);

                tem = PostekPple.printSLine(61 + xShit, 14, 0.5, 77 + xShit, 30);
                client.writeString(tem);

            }

            //面值
            tem = PostekPple.pringText(52 + shitY + xShit, 20 + xSh, shit, '3', 4, 4, 'N', "100");
            client.write(tem);
            client.write(fq_value);
            client.write("\"\r\n");

            tem = PostekPple.printLabel(1);
            client.writeString(tem);

            client.close();
        } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException | PortInUseException | UnsupportedCommOperationException | NoSuchPortException ex) {
            logger.log(Level.INFO, null,ex);
        }
    }

    public void printFQ(String com, String fq_num, String bankName) {
        int bankNameLen = bankName.length();
        int item = (12 - bankNameLen - 2) * 5;
        if (item > 0) {
            item = item / 2;
        }

        try {
            ComClient client = new ComClient(com);

            String tem = null;
            tem = PostekPple.cleanPictureBuffer();
            client.writeString(tem);
            
            tem = "H15\r\n";
            client.writeString(tem);
            
            //边框
//            tem = PostekPple.printRectangle(2, 5, 0.4, 73, 140 + 5);
//            client.writeString(tem);

            //线条
//            tem = PostekPple.printLine(11 + 2, 5, 0.4, 140);
//            client.writeString(tem);
//            tem = PostekPple.printLine(43 + 11 + 2 + 3, 5, 0.4, 140);
//            client.writeString(tem);

            //中国工商银行鄂州分行封签
            tem = PostekPple.pringText(2 + 11 + 43 + 2 + 10 + 3, 5 + 50 - 15 + 4 + item, 1, '4', 3, 2, 'N', "中国工商银行鄂州分行封签");
            client.write(tem);
            client.writeGB2312(bankName + "封签");
            client.write("\"\r\n");

            //一维码
            tem = PostekPple.printOneDimensionalCode(2 + 11 + 13 + 12 + 3, 5 + 22, 1, "1", 0.4, 0.8, 15, 'B', fq_num);
            // tem = PostekPple.printOneDimensionalCode(2 + 11 + 13 + 12 + 3, 5 + 22 + 17, 1, "1C", 0.4, 0.8, 15, 'N', fq_num);
            client.writeString(tem);
//            
            //二维码 b664,250,QR,0,0,o0,r5,m2,g0,s0
            tem = PostekPple.printTwoDimensionalCode(2 + 11 + 7 + 3, 5 + 22 + 25 + 22 + 25 + 13, "QR", 15, 15, "o0", "r9", "m2", "g0", "s1", fq_num);
            client.writeString(tem);

            tem = PostekPple.printLabel(1);
            client.writeString(tem);

            client.close();
        } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException | PortInUseException | UnsupportedCommOperationException | NoSuchPortException ex) {
           logger.log(Level.INFO, null,ex);
        }
    }

    public void printGX100X50FQ(String com, String fq_num, String bankName) {
        int bankNameLen = bankName.length();
        int item = (12 - bankNameLen - 2) * 5;
        if (item > 0) {
            item = item / 2;
        }

        try {
            ComClient client = new ComClient(com);

            String tem = null;
            tem = PostekPple.cleanPictureBuffer();
            client.writeString(tem);
            
            tem = "H15\r\n";
            client.writeString(tem);

            //中国工商银行鄂州分行封签
            tem = PostekPple.pringText(5 + item, 3, 0, '4', 2, 2, 'N', "中国工商银行鄂州分行封签");
            client.write(tem);
            client.writeGB2312(bankName);
            client.write("\"\r\n");

            //一维码
            tem = PostekPple.printOneDimensionalCode(10, 10, 0, "1", 0.4, 0.5, 11, 'B', fq_num);
            client.writeString(tem);
//            
            //二维码 b664,250,QR,0,0,o0,r5,m2,g0,s0
            tem = PostekPple.printTwoDimensionalCode(30, 25, "QR", 15, 15, "o0", "r9", "m2", "g0", "s1", fq_num);
            client.writeString(tem);

            tem = PostekPple.printLabel(1);
            client.writeString(tem);

            client.close();
        } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException | PortInUseException | UnsupportedCommOperationException | NoSuchPortException ex) {
           logger.log(Level.INFO, null,ex);
        }
    }
}
