/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.test;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;

public class TestFram {
 public static JFrame loginFrame;
 public static JFrame mainfFrame;
 
 public static void main(String[] args) {
  loginFrame=new JFrame("登陆");
  loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  loginFrame.setSize(300, 200);
  JButton loginButton=new JButton("登陆");
  loginFrame.getContentPane().add(loginButton,BorderLayout.NORTH);
  loginFrame.setVisible(true);
  
  loginButton.addActionListener(new ActionListener() {   
   public void actionPerformed(ActionEvent e) {
    mainfFrame=new JFrame("程序主界面");
    mainfFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainfFrame.setSize(400, 300);
    mainfFrame.setVisible(true);//设置新窗口可见
    loginFrame.dispose();//销毁登陆窗口
   }
  });
  
 }

}
