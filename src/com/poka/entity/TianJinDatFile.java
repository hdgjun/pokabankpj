/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poka.entity;

import com.poka.util.LogManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class TianJinDatFile {

    static final Logger logger = LogManager.getLogger(TianJinDatFile.class);
    private String fileName = "";
    private TianJinGuaoHead fileHead;
    private List<TianJinGuaoBody> bList;

    public TianJinDatFile() {
        this.bList = new ArrayList<TianJinGuaoBody>();
        this.fileHead = new TianJinGuaoHead();
    }

    public boolean readFile(InputStream input, int type) {
        try {
            byte[] tem = fileHead.getDataBody();
            int len = input.read(tem);
            if (len < 12) {
                System.out.println("read file error!");
                return false;
            }
            fileHead.init();
            bList.clear();

            System.out.println("fileHead.getRecordCount():" + fileHead.getRecordCount());
            TianJinGuaoBody temBody;
            for (int i = 0; i < fileHead.getRecordCount(); i++) {
                if (FsnComProperty.zhongchao2015Metype_c == type) {
                    temBody = new TianJinGuaoBody(TianJinGuaoBody.newSize);
                } else {
                    temBody = new TianJinGuaoBody(TianJinGuaoBody.oldSize);
                }
                if (!readData(temBody.getDataBody(), temBody.getDataBody().length, input)) {
                    return false;
                }
                if (!readData(temBody.getImage(), 1544, input)) {
                    return false;
                }
                temBody.init();
                bList.add(temBody);
//                if (FsnComProperty.zhongchao2015Metype_c == type) {
//                    byte[] b = new byte[136];
//                    TianJinDatFile.readData(b, 136, input);
//                }
            }

            return true;
        } catch (IOException ex) {
            logger.log(Level.INFO, null, ex);
            return false;
        }
    }

    public static boolean readData(byte[] disDa, int dataLen, InputStream input) {
        try {
            int count = dataLen;
            int lo = 0;
            int len;
            while ((len = input.read(disDa, lo, count - lo)) > 0) {
                lo += len;
                if (lo == count) {
                    break;
                }
            }
//            System.out.println("lo = "+lo);
            if (lo != count) {
                return false;
            }
        } catch (IOException ex) {
            logger.log(Level.INFO, null, ex);
            return false;
        }
        return true;
    }

    public boolean writeDatFile(String fPath) throws IOException {
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
        this.fileHead.reload();
        FileOutputStream output = new FileOutputStream(f);
        output.write(this.fileHead.getDataBody());
        for (TianJinGuaoBody bo : this.bList) {
            bo.reload();
            output.write(bo.getDataBody());
            output.write(bo.getImage());
        }
        output.close();
        return true;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the fileHead
     */
    public TianJinGuaoHead getFileHead() {
        return fileHead;
    }

    /**
     * @return the bList
     */
    public List<TianJinGuaoBody> getbList() {
        return bList;
    }

}
