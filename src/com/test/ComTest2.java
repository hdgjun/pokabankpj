/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.test;

import java.math.BigInteger;

/**
 *
 * @author Administrator
 */
public class ComTest2 {
  
    public static void main( String[] args ){
      BigInteger a=BigInteger.valueOf(1);
       
       BigInteger a1=BigInteger.valueOf(2);
        BigInteger a2=BigInteger.valueOf(0);
        
        System.out.println(a.compareTo(a1)+" "+a2.compareTo(a2)+" "+a1.compareTo(a2));
    }
}
