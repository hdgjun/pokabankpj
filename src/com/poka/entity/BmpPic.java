/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poka.entity;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class BmpPic {

    //BMP文件头（14字节）
    private final String bfType = "BM";//位图文件类型，必须为'B'‘M'两个字母（0-1字节）
    private int bfSize;//位图文件的大小，以字节为单位(2-5字节)
    private final int bfReserved1 = 0;//位图文件保留字，必须为0（6-7字节）
    private final int bfReserved2 = 0;//位图文件保留字，必须为0（8-9字节）
    private int bfOffBits;//位图数据的其实位置，以相对于位图(10-13字节)

    //位图信息头（40字节）
    private final int size = 40;//本结构所占用字节数（14-17字节）
    private int imageWith;//位图的宽度，以像素为单位（18-21字节）
    private int imageHeith;//位图的高度，以像素点为单位（22-25字节）
    private final int planes = 1;//目标设备的级别，必须为1(26-27字节)
    private int nbiBitCount;//每个像素所需要的位数，必须是1（双色）、4（16色），5（256色）或24（真彩色）之一（28-29字节）
    private int biCompression;//位图压缩类型，必须是0（不压缩）、1（BI_RLE8 压缩类型）或2（BI_RLE4压缩类型）之一（30-33字节）
    private int nSizeImage;//位图的大小，以字节为单位（34-37字节）
    private int biXPelsPerMeter;//位图水平分辨率，每米像素数（38-41字节）
    private int biYPelsPerMeter;//位图垂直分辨率，每米像素数（42-45字节）
    private int biClrUsed;//位图实际使用的颜色表中的颜色数（46-49字节）
    private int biClrImportant;//位图显示过程中的重要颜色数（50-53字节)

    //颜色表中RGBQUAD结构数据的个数有biBitCount来确定:
    //当biBitCount=1,4,8时，分别有2,16,256个表项;
    //当biBitCount=24时，没有颜色表项。
    private RGBQUAD[] rgbTable;//颜色表 

    private byte[] bmpData;

    public BmpPic(int imageWith, int imageHeith, byte[] data) {
        this.nbiBitCount = 1;
        this.imageWith = imageWith;
        this.imageHeith = imageHeith;
        this.biClrImportant = 0;
        this.rgbTable = new RGBQUAD[]{new RGBQUAD(255, 255, 255), new RGBQUAD(0, 0, 0)};
        this.nSizeImage = data.length;
        this.biXPelsPerMeter = 60;
        this.biYPelsPerMeter = 60;
        this.biClrUsed = 0;
        this.biClrImportant = 0;
        //this.bfSize = 14 + 40 + 8 + data.length;
        this.bfSize = 14 + 40 + 8 + data.length;
        this.bfOffBits = 14 + 40 + 8;
        this.bmpData = new byte[this.bfSize];
       
        byte[] by  = new byte[data.length];//= Arrays.copyOf((data,data.length);
        System.arraycopy(data,0,by,0,by.length);
        init(rotate(imageWith,imageHeith, by));
    }

    public BmpPic(int imageWith, int imageHeith, List<byte[]> byList, int nSizeImage) {
//        int with;
//        int heith;
//
//        this.nbiBitCount = 1;
//        this.imageWith = nSizeImage / imageHeith;
//        this.imageHeith = imageHeith;
//        this.biClrImportant = 0;
//        this.rgbTable = new RGBQUAD[]{new RGBQUAD(255, 255, 255), new RGBQUAD(0, 0, 0)};
//        this.nSizeImage = nSizeImage;
//        this.biXPelsPerMeter = 60;
//        this.biYPelsPerMeter = 60;
//        this.biClrUsed = 0;
//        this.biClrImportant = 0;
//        //this.bfSize = 14 + 40 + 8 + data.length;
//        this.bfSize = 14 + 40 + 8 + nSizeImage;
//        this.bfOffBits = 14 + 40 + 8;
//        this.bmpData = new byte[this.bfSize];

      
     //   init(rotate(int we, int he, byte[] mydata));
    }

    public BmpPic(int nbiBitCount, int imageWith, int imageHeith, int biCompression, int nSizeImage, int biXPelsPerMeter, int biYPelsPerMeter, int biClrUsed, int biClrImportant, RGBQUAD[] rgbTable, byte[] data) {
        this.nbiBitCount = nbiBitCount;
        this.imageWith = imageWith;
        this.imageHeith = imageHeith;
        this.biClrImportant = biCompression;
        this.rgbTable = rgbTable;
        this.nSizeImage = nSizeImage;
        this.biXPelsPerMeter = biXPelsPerMeter;
        this.biYPelsPerMeter = biYPelsPerMeter;
        this.biClrUsed = biClrUsed;
        this.biClrImportant = biClrImportant;
    }

    private void init(byte[] data) {
        //BMP文件头（14字节）
        char[] c = this.bfType.toCharArray();
        bmpData[0] = (byte) c[0]; //bfType （0-1字节）
        bmpData[1] = (byte) c[1];

        bmpData[2] = (byte) (this.bfSize & 0x000000FF);   // bfSize (2-5字节)
        bmpData[3] = (byte) ((this.bfSize >> 8) & 0x000000FF);
        bmpData[4] = (byte) ((this.bfSize >> 16) & 0x000000FF);
        bmpData[5] = (byte) ((this.bfSize >> 24) & 0x000000FF);

        bmpData[6] = 0;   // bfReserved1 (6-7字节）
        bmpData[7] = 0;

        bmpData[8] = 0;  //bfReserved2  (8-9字节)
        bmpData[9] = 0;

        bmpData[10] = (byte) (this.bfOffBits & 0x000000FF);   // bfOffBits (10-13字节)
        bmpData[11] = (byte) ((this.bfOffBits >> 8) & 0x000000FF);
        bmpData[12] = (byte) ((this.bfOffBits >> 16) & 0x000000FF);
        bmpData[13] = (byte) ((this.bfOffBits >> 24) & 0x000000FF);

        //位图信息头（40字节）
        bmpData[14] = (byte) (this.size & 0x000000FF);   // size (14-17字节)
        bmpData[15] = (byte) ((this.size >> 8) & 0x000000FF);
        bmpData[16] = (byte) ((this.size >> 16) & 0x000000FF);
        bmpData[17] = (byte) ((this.size >> 24) & 0x000000FF);

        bmpData[18] = (byte) (this.imageWith & 0x000000FF);   // imageWith (18-21字节)
        bmpData[19] = (byte) ((this.imageWith >> 8) & 0x000000FF);
        bmpData[20] = (byte) ((this.imageWith >> 16) & 0x000000FF);
        bmpData[21] = (byte) ((this.imageWith >> 24) & 0x000000FF);

        bmpData[22] = (byte) (this.imageHeith & 0x000000FF);   // imageHeith (22-25字节)
        bmpData[23] = (byte) ((this.imageHeith >> 8) & 0x000000FF);
        bmpData[24] = (byte) ((this.imageHeith >> 16) & 0x000000FF);
        bmpData[25] = (byte) ((this.imageHeith >> 24) & 0x000000FF);

        bmpData[26] = (byte) (this.planes & 0x000000FF);   // planes (26-27字节)
        bmpData[27] = (byte) ((this.planes >> 8) & 0x000000FF);

        bmpData[28] = (byte) (this.nbiBitCount & 0x000000FF);   // nbiBitCount (28-29字节)
        bmpData[29] = (byte) ((this.nbiBitCount >> 8) & 0x000000FF);

        bmpData[30] = (byte) (this.biCompression & 0x000000FF);   // biCompression (30-33字节)
        bmpData[31] = (byte) ((this.biCompression >> 8) & 0x000000FF);
        bmpData[32] = (byte) ((this.biCompression >> 16) & 0x000000FF);
        bmpData[33] = (byte) ((this.biCompression >> 24) & 0x000000FF);

        bmpData[34] = (byte) (this.nSizeImage & 0x000000FF);   // nSizeImage (34-37字节)
        bmpData[35] = (byte) ((this.nSizeImage >> 8) & 0x000000FF);
        bmpData[36] = (byte) ((this.nSizeImage >> 16) & 0x000000FF);
        bmpData[37] = (byte) ((this.nSizeImage >> 24) & 0x000000FF);

        bmpData[38] = (byte) (this.biXPelsPerMeter & 0x000000FF);   // biXPelsPerMeter (38-41字节)
        bmpData[39] = (byte) ((this.biXPelsPerMeter >> 8) & 0x000000FF);
        bmpData[40] = (byte) ((this.biXPelsPerMeter >> 16) & 0x000000FF);
        bmpData[41] = (byte) ((this.biXPelsPerMeter >> 24) & 0x000000FF);

        bmpData[42] = (byte) (this.biYPelsPerMeter & 0x000000FF);   // biXPelsPerMeter (42-45字节)
        bmpData[43] = (byte) ((this.biYPelsPerMeter >> 8) & 0x000000FF);
        bmpData[44] = (byte) ((this.biYPelsPerMeter >> 16) & 0x000000FF);
        bmpData[45] = (byte) ((this.biYPelsPerMeter >> 24) & 0x000000FF);

        bmpData[46] = (byte) (this.biClrUsed & 0x000000FF);   // biXPelsPerMeter (46-49字节)
        bmpData[47] = (byte) ((this.biClrUsed >> 8) & 0x000000FF);
        bmpData[48] = (byte) ((this.biClrUsed >> 16) & 0x000000FF);
        bmpData[49] = (byte) ((this.biClrUsed >> 24) & 0x000000FF);

        bmpData[50] = (byte) (this.biClrImportant & 0x000000FF);   // biXPelsPerMeter (50-53字节)
        bmpData[51] = (byte) ((this.biClrImportant >> 8) & 0x000000FF);
        bmpData[52] = (byte) ((this.biClrImportant >> 16) & 0x000000FF);
        bmpData[53] = (byte) ((this.biClrImportant >> 24) & 0x000000FF);

        int location = 54;
        for (RGBQUAD rgb : this.rgbTable) {
            bmpData[location++] = (byte) rgb.getRgbBlue();
            bmpData[location++] = (byte) rgb.getRgbGreen();
            bmpData[location++] = (byte) rgb.getRgbRed();
            bmpData[location++] = (byte) 0;
        } //0 4 8
        //  System.out.println("data.length="+data.length);
        for (int i = 0; i < data.length; i += 4) {
            // System.out.println("i="+i);
            bmpData[location + i] = data[i + 3];
            bmpData[location + i + 1] = data[i + 2];
            bmpData[location + i + 2] = data[i + 1];
            bmpData[location + i + 3] = data[i];
        }
    }
//32*32     32*32*10

    public byte[] rotate(int we, int he, byte[] mydata) {

        byte[] first = new byte[we * he * 8];
        int j, k, t = -1;
        int temp = we/8;
        for (int i = 0; i < he; i++) {
            for (j = 0; j < temp; j++) {
                for (k = 0; k < 8; k++) {
                    first[++t] = (byte) ((mydata[i * temp + j] >> k) & 0x01);
                }
            }
        }

        int cw = 0;
        byte[] second = new byte[we * he * 8];
        t = -1;
        for (int i = 0; i < he; i++) {
            for (j = 0; j < he; j++) {
                if (cw == 1) {
                    second[++t] = first[he * j + he - i - 1];
                } else {
                    // second[(he - 1 - j) * he + he - 1 - i] = first[i * he + (he - 1 - j)];
                    second[++t] = first[he * (he - j - 1) + i];
                }
            }
        }

        byte[] third = new byte[we * he * 8];
        t = -1;
        for (int i = 0; i < he; i++) {
            for (j = 0; j < he; j++) {
                third[++t] = second[i * he + j];
            }
        }

        byte[] forth = new byte[we * he];
        //   int u = -1;
        int m = -1;
        int tt = 0;
        for (int i = 0; i < he * we; i++) {
            int sum = 0;
            for (k = 0; k < 8; k++) {
                tt = 1 << k;
                sum = (sum & 0x000000FF) + (tt * third[++m]);

            }
            forth[i] = (byte) sum;
        }
        for(int i = 0;i<we;i++){
            for(j = 0;j<he/8;j++){
                mydata[i*he/8+j] = forth[i*he/8+j];
            }
        }
        return mydata;
    }
//    public byte[][] rotate(int we, int he, byte[] mydata, int loca) {
//
//        byte[][] myTem = new byte[we][he];
//        byte[] first = new byte[we * he * 8];
//        int j, k, t = -1;
//        int temp = we / 8;
//        for (int i = 0; i < he; i++) {
//            for (j = 0; j < temp; j++) {
//                for (k = 0; k < 8; k++) {
//                    first[++t] = (byte) ((mydata[i * temp + j] >> k) & 0x01);
//                }
//            }
//        }
//
//        int cw = 0;
//        byte[] second = new byte[we * he * 8];
//        t = -1;
//        for (int i = 0; i < he; i++) {
//            for (j = 0; j < he; j++) {
//                if (cw == 1) {
//                    second[++t] = first[he * j + he - i - 1];
//                } else {
//                    // second[(he - 1 - j) * he + he - 1 - i] = first[i * he + (he - 1 - j)];
//                    second[++t] = first[he * (he - j - 1) + i];
//                }
//            }
//        }
//
//        byte[] third = new byte[we * he * 8];
//        t = -1;
//        for (int i = 0; i < he; i++) {
//            for (j = 0; j < he; j++) {
//                third[++t] = second[i * he + j];
//            }
//        }
//
//     //   byte[] forth = new byte[we * he];
//        //   int u = -1;
//        int m = -1;
//        int tt = 0;
//        int sum = 0;
//        for (int i = 0; i < we; i++) {
//            for (j = 0; j < he; j++) {
//                sum = 0;
//                for (k = 0; k < 8; k++) {
//                    tt = 1 << k;
//                    sum = (sum & 0x000000FF) + (tt * third[++m]);
//
//                }
//                myTem[i][j] = (byte) sum;
//            }
//        }
//        return myTem;
//    }

    /**
     * @return the bfType
     */
    public String getBfType() {
        return bfType;
    }

    /**
     * @return the bfSize
     */
    public int getBfSize() {
        return bfSize;
    }

    /**
     * @param bfSize the bfSize to set
     */
    public void setBfSize(int bfSize) {
        this.bfSize = bfSize;
    }

    /**
     * @return the bfReserved1
     */
    public int getBfReserved1() {
        return bfReserved1;
    }

    /**
     * @return the bfReserved2
     */
    public int getBfReserved2() {
        return bfReserved2;
    }

    /**
     * @return the bfOffBits
     */
    public int getBfOffBits() {
        return bfOffBits;
    }

    /**
     * @param bfOffBits the bfOffBits to set
     */
    public void setBfOffBits(int bfOffBits) {
        this.bfOffBits = bfOffBits;
    }

    /**
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * @return the imageWith
     */
    public int getImageWith() {
        return imageWith;
    }

    /**
     * @param imageWith the imageWith to set
     */
    public void setImageWith(int imageWith) {
        this.imageWith = imageWith;
    }

    /**
     * @return the imageHeith
     */
    public int getImageHeith() {
        return imageHeith;
    }

    /**
     * @param imageHeith the imageHeith to set
     */
    public void setImageHeith(int imageHeith) {
        this.imageHeith = imageHeith;
    }

    /**
     * @return the planes
     */
    public int getPlanes() {
        return planes;
    }

    /**
     * @return the nbiBitCount
     */
    public int getNbiBitCount() {
        return nbiBitCount;
    }

    /**
     * @param nbiBitCount the nbiBitCount to set
     */
    public void setNbiBitCount(int nbiBitCount) {
        this.nbiBitCount = nbiBitCount;
    }

    /**
     * @return the biCompression
     */
    public int getBiCompression() {
        return biCompression;
    }

    /**
     * @param biCompression the biCompression to set
     */
    public void setBiCompression(int biCompression) {
        this.biCompression = biCompression;
    }

    /**
     * @return the nSizeImage
     */
    public int getnSizeImage() {
        return nSizeImage;
    }

    /**
     * @param nSizeImage the nSizeImage to set
     */
    public void setnSizeImage(int nSizeImage) {
        this.nSizeImage = nSizeImage;
    }

    /**
     * @return the biXPelsPerMeter
     */
    public int getBiXPelsPerMeter() {
        return biXPelsPerMeter;
    }

    /**
     * @param biXPelsPerMeter the biXPelsPerMeter to set
     */
    public void setBiXPelsPerMeter(int biXPelsPerMeter) {
        this.biXPelsPerMeter = biXPelsPerMeter;
    }

    /**
     * @return the biYPelsPerMeter
     */
    public int getBiYPelsPerMeter() {
        return biYPelsPerMeter;
    }

    /**
     * @param biYPelsPerMeter the biYPelsPerMeter to set
     */
    public void setBiYPelsPerMeter(int biYPelsPerMeter) {
        this.biYPelsPerMeter = biYPelsPerMeter;
    }

    /**
     * @return the biClrUsed
     */
    public int getBiClrUsed() {
        return biClrUsed;
    }

    /**
     * @param biClrUsed the biClrUsed to set
     */
    public void setBiClrUsed(int biClrUsed) {
        this.biClrUsed = biClrUsed;
    }

    /**
     * @return the biClrImportant
     */
    public int getBiClrImportant() {
        return biClrImportant;
    }

    /**
     * @param biClrImportant the biClrImportant to set
     */
    public void setBiClrImportant(int biClrImportant) {
        this.biClrImportant = biClrImportant;
    }

    /**
     * @return the rgbTable
     */
    public RGBQUAD[] getRgbTable() {
        return rgbTable;
    }

    /**
     * @param rgbTable the rgbTable to set
     */
    public void setRgbTable(RGBQUAD[] rgbTable) {
        this.rgbTable = rgbTable;
    }

    /**
     * @return the bmpData
     */
    public byte[] getBmpData() {
        return bmpData;
    }

    /**
     * @param bmpData the bmpData to set
     */
    public void setBmpData(byte[] bmpData) {
        this.bmpData = bmpData;
    }

}
