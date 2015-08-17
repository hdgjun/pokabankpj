/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poka.entity;

/**
 *
 * @author Administrator
 */
public class TianJinGuaoMsg {

    private int result = 0;
    private String errMsg = "";
    private TianJinDatFile fileData = new TianJinDatFile();

    /**
     * @return the result
     */
    public int getResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(int result) {
        this.result = result;
    }

    /**
     * @return the errMsg
     */
    public String getErrMsg() {
        return errMsg;
    }

    /**
     * @param errMsg the errMsg to set
     */
    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    /**
     * @return the fileData
     */
    public TianJinDatFile getFileData() {
        return fileData;
    }

    /**
     * @param fileData the fileData to set
     */
    public void setFileData(TianJinDatFile fileData) {
        this.fileData = fileData;
    }
    
    
}
