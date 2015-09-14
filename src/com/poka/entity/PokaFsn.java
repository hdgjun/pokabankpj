package com.poka.entity;

import static com.poka.app.panel.ConfigJPanel.timer;
import com.poka.util.LogManager;
import com.poka.util.StaticVar;
import com.poka.util.StringUtil;
import com.poka.util.UploadFtp;
import static com.poka.util.ZipUtil.compress;
import com.poka.util.argPro;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PokaFsn {
    static final Logger logger = LogManager.getLogger(PokaFsn.class);
    protected FsnHead fhead;
    protected List<PokaFsnBody> bList;
    private List<String> bundleIdList;
    private int bundleIdCount;
    private String fileName = "";
    private int curCount = 0;

    private String bankNo = "";
    private String dotNo = "";
    private String path = "";

    public PokaFsn() {
        this.fhead = new FsnHead();
        this.bList = new ArrayList<PokaFsnBody>();
        this.bundleIdList = new ArrayList<String>();
        this.bundleIdCount = 0;
    }

    public PokaFsn(int count) {
        this.fhead = new FsnHead(count);
        this.bList = new ArrayList<PokaFsnBody>();
        this.bundleIdList = new ArrayList<String>();
        this.bundleIdCount = 0;
    }

    public synchronized String getFsnName() {
        String bankNO = this.bankNo;
        if (null == bankNO) {
            bankNO = "*";
        }
        String dotNO = this.dotNo;
        if (null == dotNO) {
            dotNO = "*";
        }
        String date = (new java.text.SimpleDateFormat("yyyyMMddHHmmssSSS")).format(new Date());
        return bankNO + "_" + dotNO + "_XXXXXXXXXXXXXX_XXXXXXX" + date + ".FSN";
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

            if (lo != count) {
                return false;
            }
        } catch (IOException ex) {
            return false;
        }
        return true;
    }

    public boolean readPokaFsnFile(String fPath) throws IOException {
        File f = new File(fPath);
        if (f.exists()) {
            FileInputStream input = new FileInputStream(f);
            byte[] tem = fhead.getHeadBody();
            int len = input.read(tem);
            if (len < 32) {
                System.out.println("read file error!");
                return false;
            }
            fhead.setHeadBody(tem);
            bList.clear();
            getBundleIdList().clear();
            bundleIdCount = 0;
            for (int i = 0; i < fhead.getCount(); i++) {
                PokaFsnBody temBody = new PokaFsnBody();
                tem = temBody.getFbData();
                len = input.read(tem);
                tem = temBody.getImageSNo().getImData();
                len = input.read(tem);
                char[] temC = temBody.getPokaData();
                byte[] temB = new byte[temC.length];
                len = input.read(temB);
                StringUtil.byteToChar2(temC, temC.length, 0, temB);
                temBody.initPokaFsn();
                bList.add(temBody);
                if (0 == i % 100) {
                    this.getBundleIdList().add(temBody.getBundleId());
                    this.bundleIdCount++;
                }
            }
            input.close();
            return true;
        }
        return true;
    }



    public boolean readBaseFsnFile(String fPath) throws IOException {
        File f = new File(fPath);
        if (f.exists()) {
            FileInputStream input = new FileInputStream(f);
            byte[] tem = fhead.getHeadBody();
            int len = input.read(tem);
            if (len < 32) {
                System.out.println("read file error!");
                return false;
            }
            fhead.setHeadBody(tem);
            bList.clear();
            getBundleIdList().clear();
            bundleIdCount = 0;
            for (int i = 0; i < fhead.getCount(); i++) {
                PokaFsnBody temBody = new PokaFsnBody();
                tem = temBody.getFbData();
                len = input.read(tem);
                tem = temBody.getImageSNo().getImData();
                len = input.read(tem);
                /*
                 * char[] temC = temBody.getPokaData(); byte[] temB = new
                 * byte[temC.length]; len = input.read(temB);
                 * StringUtil.byteToChar2(temC, temC.length, 0, temB);
                 */
                temBody.init();
                bList.add(temBody);
            }
            input.close();
            return true;
        }
        return true;
    }

    public boolean readBaseFsnFile(InputStream input, byte cmd) {
        byte[] tem = fhead.getHeadBody();
        if (!PokaFsn.readData(tem, 32, input)) {
            System.out.println("read file error!");
            return false;
        }
        fhead.setHeadBody(tem);
        bList.clear();
        getBundleIdList().clear();
        bundleIdCount = 0;
        for (int i = 0; i < fhead.getCount(); i++) {
            PokaFsnBody temBody = new PokaFsnBody();
            tem = temBody.getFbData();
            PokaFsn.readData(tem, tem.length, input);
            //if (cmd == 0x30) {
                tem = temBody.getImageSNo().getImData();
                PokaFsn.readData(tem, tem.length, input);
          //  }
            temBody.init();
            bList.add(temBody);
            System.out.println(temBody.getsNo());
        }
        return true;
    }
    public static PokaFsnBody readBaseFsnFileNoHead(InputStream input) {
       byte[] tem = null;
        PokaFsnBody temBody = new PokaFsnBody();
        tem = temBody.getFbData();
        PokaFsn.readData(tem, tem.length, input);        
        tem = temBody.getImageSNo().getImData();
        PokaFsn.readData(tem, tem.length, input);          
        temBody.init();       
       return temBody;
    }

    public boolean writePokaFsnFile(String fPath) throws IOException {
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
        output.write(this.fhead.getFsnHead());
        int i = 0;
        for (PokaFsnBody bo : this.bList) {
            if (this.getBundleIdList().size() > 0) {
                bo.reloadPokaFsn(this.getBundleIdList().get(i / 100));
                i++;
            } else {
                bo.reloadPokaFsn();
            }
            output.write(bo.getFbData());
            output.write(bo.getImageSNo().getImData());
            output.write(StringUtil.CharToByte2(bo.getPokaData(),
                    bo.getPokaData().length, 0));
        }
        output.close();
        return true;
    }

    public boolean writeBaseFsnFile(String fPath) throws IOException {
       
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
        output.write(this.fhead.getFsnHead());
        for (PokaFsnBody bo : this.bList) {
            bo.reload();
            output.write(bo.getFbData());
            output.write(bo.getImageSNo().getImData());
            /*
             * output.write(StringUtil.CharToByte2(bo.getPokaData(),
             * bo.getPokaData().length, 0));
             */
        }
        output.close();
        return true;
    }

    public static int readInputStreamWithTimeout(InputStream is, byte[] b, int timeoutMillis,int offset)
            throws IOException {
        int bufferOffset = offset;
        long maxTimeMillis = System.currentTimeMillis() + timeoutMillis;
        while (System.currentTimeMillis() < maxTimeMillis && bufferOffset < b.length) {
            int readLength = java.lang.Math.min(is.available(), b.length - bufferOffset);
            // can alternatively use bufferedReader, guarded by isReady():
            int readResult = is.read(b, bufferOffset, readLength);
            if (readResult == -1) {
                break;
            }
            bufferOffset += readResult;
        }
        return bufferOffset;
    }
    public void deleteFile(){
        File f = new File(this.path + File.separator + this.fileName);
        if(f.exists()){
            f.delete();
        }
        if(this.bList.size()>0){
            this.bList.clear();
        }
    }
    public void addAndWrite(PokaFsnBody item, int count) {
        if (item != null) {
            this.bList.add(item);
            this.fhead.setCount(this.fhead.getCount() + 1);
        }

        if (this.fileName == null || this.fileName.length() <= 0) {
            this.fileName = this.getFsnName();
        }
        if (this.bList.size() >= 50 || this.fhead.getCount() == count) {
            try {
                RandomAccessFile randomFile = new RandomAccessFile(this.path + File.separator + this.fileName, "rw");

                randomFile.seek(0);
                randomFile.write(this.fhead.getFsnHead());
                // 文件长度，字节数
                long fileLength = randomFile.length();
                //将写文件指针移到文件尾。
                randomFile.seek(fileLength);
                int i = 0;
                for (PokaFsnBody bo : this.bList) {
                    bo.reloadPokaFsn();
                    randomFile.write(bo.getFbData());
                    randomFile.write(bo.getImageSNo().getImData());
                    randomFile.write(StringUtil.CharToByte2(bo.getPokaData(),
                            bo.getPokaData().length, 0));
                }
                this.bList.clear();
                randomFile.close();
                if (this.fhead.getCount() == count) {
	                String resourcePath = this.path+File.separator+this.fileName;
	                UploadFtp.uploadFsnFile(resourcePath, this.fileName);
	                this.fileName = "";
	                this.fhead.setCount(0);
                }
            } catch (IOException ex) {
               logger.log(Level.INFO, null,ex);
            }
        }
    }

    public void add(PokaFsnBody item) {
        this.bList.add(item);
        this.fhead.setCount(this.fhead.getCount() + 1);
    }

    public void addBundleId(String item) {
        this.bundleIdList.add(item);
    }

    public FsnHead getFhead() {
        return fhead;
    }

    public void setFhead(FsnHead fhead) {
        this.fhead = fhead;
    }

    public List<PokaFsnBody> getbList() {
        return bList;
    }

    public void setbList(List<PokaFsnBody> bList) {
        this.bList = bList;
    }

    /**
     * @return the bundleIdList
     */
    public List<String> getBundleIdList() {
        return bundleIdList;
    }

    /**
     * @return the bundleIdCount
     */
    public int getBundleIdCount() {
        return bundleIdCount;
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
     * @return the curCount
     */
    public int getCurCount() {
        return curCount;
    }

    /**
     * @param curCount the curCount to set
     */
    public void setCurCount(int curCount) {
        this.curCount = curCount;
    }

    /**
     * @return the bankNo
     */
    public String getBankNo() {
        return bankNo;
    }

    /**
     * @param bankNo the bankNo to set
     */
    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    /**
     * @return the dotNo
     */
    public String getDotNo() {
        return dotNo;
    }

    /**
     * @param dotNo the dotNo to set
     */
    public void setDotNo(String dotNo) {
        this.dotNo = dotNo;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }
    
    public void clear(){
        this.bList.clear();
        this.getFhead().setCount(0);
    }
}
