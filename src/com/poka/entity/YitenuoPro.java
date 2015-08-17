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
public class YitenuoPro  {
    byte[] startWord = new byte[4]; 
    byte[] fsnCount = new byte[2];
    byte[] validNum = new byte[4];
    byte[] cmd = new byte[2];
    byte[] machinaSno = new byte[48];
    PokaFsn fsn = new PokaFsn();
    byte[] checkSum = new byte[2];
    byte[] endWord = new byte[4];
    
    
}
