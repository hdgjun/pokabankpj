/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.test;

import com.poka.dao.impl.BaseDao;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class AccessTest {
     public static void main(String[] args) {
           File f = new File("F:\\2.txt");
           f.renameTo(new File("F:\\1.txt"));
     }
}
