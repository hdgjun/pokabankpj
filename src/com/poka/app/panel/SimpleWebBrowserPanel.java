/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.poka.app.panel;

import java.awt.BorderLayout;  
import javax.swing.JFrame;  
import javax.swing.JPanel;  
import javax.swing.SwingUtilities;  
  
import chrriis.common.UIUtils;  
import chrriis.dj.nativeswing.swtimpl.NativeInterface;  
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;  
import java.awt.Dimension;
import java.awt.Toolkit;
  
/** 
 * @author Christopher Deckers 
 */  
public class SimpleWebBrowserPanel extends JPanel {  
  private final JWebBrowser webBrowser;
  public SimpleWebBrowserPanel() {  
    super(new BorderLayout());  
    Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包    
    Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸   
    JPanel webBrowserPanel = new JPanel(new BorderLayout());  
   // webBrowserPanel.setBorder(BorderFactory.createTitledBorder("Native Web Browser component"));  
    webBrowser = new JWebBrowser();  
   // webBrowser.navigate("http://www.baidu.com");  
    webBrowserPanel.add(webBrowser, BorderLayout.CENTER);  
    add(webBrowserPanel, BorderLayout.CENTER);  
    webBrowser.setMenuBarVisible(false);
    this.setSize(screenSize);
    NativeInterface.open();  
    // Create an additional bar allowing to show/hide the menu bar of the web browser.  
//    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 4));  
//    JCheckBox menuBarCheckBox = new JCheckBox("Menu Bar", webBrowser.isMenuBarVisible());  
//    menuBarCheckBox.addItemListener(new ItemListener() {  
//      public void itemStateChanged(ItemEvent e) {  
//        webBrowser.setMenuBarVisible(e.getStateChange() == ItemEvent.SELECTED);  
//      }  
//    });  
//    buttonPanel.add(menuBarCheckBox);  
//    add(buttonPanel, BorderLayout.SOUTH);  
  }  
  
  /* Standard main method to try that test as a standalone application. */  
  public static void main(String[] args) {  
    UIUtils.setPreferredLookAndFeel();  
    NativeInterface.open();  
    SwingUtilities.invokeLater(new Runnable() {  
      public void run() {  
        JFrame frame = new JFrame();  
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        frame.getContentPane().add(new SimpleWebBrowserPanel(), BorderLayout.CENTER);  
        frame.setSize(800, 600);  
        frame.setLocationByPlatform(true);  
        frame.setVisible(true);  
      }  
    });  
   // NativeInterface.runEventPump();  
  }  

    /**
     * @return the webBrowser
     */
    public JWebBrowser getWebBrowser() {
        return webBrowser;
    }
  
}  