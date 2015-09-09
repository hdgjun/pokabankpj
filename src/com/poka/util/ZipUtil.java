package com.poka.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


public class ZipUtil {
     public static void main(String[] args) throws IOException {
    
            compress("D:\\window7/0204_0000_P90113080011T1_000000825A21503231550279.FSN","D:\\window7/222.zip");  
//     unzip();
     }
        static String Parent="D:\\fsn/chenbo/"; //输出路径（文件夹目录）
	static final int BUFFER = 8192;


	public static void compress(String srcPathName,String pathName) {
                File zipFile = new File(pathName);
		File file = new File(srcPathName);
		if (!file.exists())
                    return;			
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
			CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream,
					new CRC32());
			                 ZipOutputStream out = new ZipOutputStream(cos);
			String basedir = "";
			compress(file, out, basedir);
			out.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void compress(File file, ZipOutputStream out, String basedir) {
		/* 判断是目录还是文件 */
		if (file.isDirectory()) {
//			System.out.println("压缩：" + basedir + file.getName());
			compressDirectory(file, out, basedir);
		} else {
//			System.out.println("压缩：" + basedir + file.getName());
			compressFile(file, out, basedir);
		}
	}

	/** 压缩一个目录 */
	public static void compressDirectory(File dir, ZipOutputStream out, String basedir) {
		if (!dir.exists())
			return;

		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			/* 递归 */
			compress(files[i], out, basedir + dir.getName() + "/");
		}
	}

	/** 压缩一个文件 */
	public static void compressFile(File file, ZipOutputStream out, String basedir) {
		if (!file.exists()) {
			return;
		}
		try {
			BufferedInputStream bis = new BufferedInputStream(
					new FileInputStream(file));
			                 ZipEntry entry = new ZipEntry(basedir + file.getName());
			out.putNextEntry(entry);
			int count;
			byte data[] = new byte[BUFFER];
			while ((count = bis.read(data, 0, BUFFER)) != -1) {
				out.write(data, 0, count);
			}
			bis.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
        
        //解压
        public static boolean unzip(File resourceZipFile){
             System.out.println(resourceZipFile);
            long startTime=System.currentTimeMillis();
            try {
                    ZipInputStream Zin=new ZipInputStream(new FileInputStream(
                   resourceZipFile));//输入源zip路径
                    BufferedInputStream Bin=new BufferedInputStream(Zin);

                    File Fout=null;
                    ZipEntry entry;
                    try {
                            while((entry = Zin.getNextEntry())!=null && !entry.isDirectory()){
                                    Fout=new File(Parent,entry.getName());
                                    if(!Fout.exists()){
                                            (new File(Fout.getParent())).mkdirs();
                                    }
                                    FileOutputStream out=new FileOutputStream(Fout);
                                                                   BufferedOutputStream Bout=new BufferedOutputStream(out);
                                    int b;
                                    while((b=Bin.read())!=-1){
                                            Bout.write(b);
                                    }
                                    Bout.close();
                                    out.close();                                   
                            }
//                             System.out.println(Fout+"解压成功");	
                            Bin.close();
                            Zin.close();
                    } catch (IOException e) {

                            e.printStackTrace();
                            return false;
                    }
            } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return false;
            }
            long endTime=System.currentTimeMillis();
            System.out.println("耗费时间： "+(endTime-startTime)+" ms");
            return true;
	}

        
}