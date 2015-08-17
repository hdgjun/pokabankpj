package com.poka.entity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BaseFsn {
	protected FsnHead fhead;
	protected List<FsnBody> bList;

	public BaseFsn() {
		this.fhead = new FsnHead();
		this.bList = new ArrayList<FsnBody>();
	}
	public BaseFsn(int count) {
		this.fhead = new FsnHead(count);
		this.bList = new ArrayList<FsnBody>();
	}

	public boolean readFile(String fPath) throws IOException {
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
			for (int i = 0; i < fhead.getCount(); i++) {
				FsnBody temBody = new FsnBody();
				tem = temBody.getFbData();
				len = input.read(tem);
				tem = temBody.getImageSNo().getImData();
				len = input.read(tem);
				temBody.init();
				bList.add(temBody);
			}
			input.close();
			return true;
		}
		System.out.println("file  is no exists");
		return false;
	}

	public boolean writeFile(String fPath) throws IOException {
		File f = new File(fPath);
		if(!f.exists()){
			File pf = f.getParentFile();
			if (!pf.exists())
				pf.mkdirs();
			if(f.createNewFile()){
				FileOutputStream output = new FileOutputStream(f);
				output.write(this.fhead.getFsnHead());
				for(FsnBody bo : this.bList){
					bo.reload();
					output.write(bo.getFbData());
					output.write(bo.getImageSNo().getImData());
				}
				
				output.close();
			}
		}
		return true;
	}

	public FsnHead getFhead() {
		return fhead;
	}

	public void setFhead(FsnHead fhead) {
		this.fhead = fhead;
	}

	public List<FsnBody> getbList() {
		return bList;
	}

	public void setbList(List<FsnBody> bList) {
		this.bList = bList;
		this.fhead.setCount(this.bList.size());
	}
}
