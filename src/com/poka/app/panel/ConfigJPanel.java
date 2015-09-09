/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poka.app.panel;

import com.poka.app.frame.PokaMainFrame;
import com.poka.entity.Arg;
import com.poka.util.PokaSftp;
import com.poka.util.StaticVar;
import com.poka.util.XmlSax;
import com.poka.util.argPro;
import com.poka.util.UploadFtp;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author poka
 */
public class ConfigJPanel extends javax.swing.JPanel implements KeyListener {

    /**
     * Creates new form ConfigJPanel
     */
    public ConfigJPanel() {

        initComponents();
    
    
        
        this.loginLabel.setVisible(false);
        this.loginLoadjComboBox.setVisible(false);
        localAddrTextField.setEditable(false);

        this.ftpAddrTextField.addKeyListener(this);
        this.ftpUserTextField.addKeyListener(this);
        this.ftpPwdTextField.addKeyListener(this);
        this.localAddrTextField.addKeyListener(this);
        this.remoteAddrTextField.addKeyListener(this);
        this.loginLoadjComboBox.addKeyListener(this);
        this.softNameTextField.addKeyListener(this);
        this.changeButton.addKeyListener(this);
        sftpPortTextField.addKeyListener(this);
        isZipjComboBox.setModel(new javax.swing.DefaultComboBoxModel(zipArry));  //设置压缩不压缩
        timerComboBox1.setModel(new javax.swing.DefaultComboBoxModel(timerArry));  //设置定时不定时
        //this.FSNFileLoadjComboBox.addItem("自动执行");
        //this.FSNFileLoadjComboBox.addItem("手动执行");
        //String fileLoadModel = StaticVar.cfgMap.get("fileLoadModel");    
        //this.FSNFileLoadjComboBox.setSelectedIndex(Integer.parseInt(fileLoadModel));
//        String sql = "from Arg";
//        List<Arg> argList = b.find(sql);
        List<Arg> argList = xmlSax.getArgList();
        for (Arg a : argList) {
            String akey = a.getAkey();
            if (akey.equals(argPro.ftp)) {
                this.ftpAddrTextField.setText(a.getAvalue());
            } else if (akey.equals(argPro.user)) {
                this.ftpUserTextField.setText(a.getAvalue());
            } else if (akey.equals(argPro.pwd)) {
                this.ftpPwdTextField.setText(a.getAvalue());
            } else if (akey.equals(argPro.localAddr)) {
                this.localAddrTextField.setText(a.getAvalue());
            } else if (akey.equals(argPro.remoteAddr)) {
                this.remoteAddrTextField.setText(a.getAvalue());
            } else if (akey.equals(argPro.sendModel)) {

            } else if (akey.equals(argPro.updatePath)) {
                this.updateTextField.setText(a.getAvalue());
            } else if (akey.equals(argPro.webPtah)) {
                this.webjTextField.setText(a.getAvalue());
            }else if(akey.equals(argPro.port)){
                this.sftpPortTextField.setText(a.getAvalue());
            }else if (akey.equals(argPro.selfTimer)) {
                String[] timerStr = a.getAvalue().split("-");
                this.jSpinnerHour.setValue(Integer.parseInt(timerStr[0]));
                this.jSpinnerMinute.setValue(Integer.parseInt(timerStr[1]));
            }else if(akey.equals(argPro.isZip)){
                if(a.getAvalue().equals("0"))
                    this.isZipjComboBox.setSelectedIndex(0);
                else
                    this.isZipjComboBox.setSelectedIndex(1);
            }else if(akey.equals(argPro.isTimer)){
                if(a.getAvalue().equals("0")){
                    this.timerComboBox1.setSelectedIndex(0);
                 
                }
                else{
                    this.timerComboBox1.setSelectedIndex(1);               
                }
                System.out.println(this.timerComboBox1.getSelectedIndex());
            }
            
            if(this.sftpPortTextField.getText().isEmpty()){
                this.sftpPortTextField.setText("22");
            }
            if(remoteAddrTextField.getText().isEmpty()){
                remoteAddrTextField.setText("/fsn/");
            }
            if(updateTextField.getText().isEmpty()){
                updateTextField.setText("/home/poka/update/");
            }
            this.bankIdjTextField.setText(xmlSax.getBankNum());
            this.dotsjTextField.setText(xmlSax.getNetworkNum());
            this.softNameTextField.setText(StaticVar.showSoftName.trim());
            this.ftpAddrTextField.requestFocus();
        }
    }
    String[] zipArry = new String[] { "压缩", "不压缩" };
    String[] timerArry = new String[] { "定时", "实 时" };
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        scrollbar1 = new java.awt.Scrollbar();
        jPanel1 = new javax.swing.JPanel();
        ftpLabel = new javax.swing.JLabel();
        ftpAddrTextField = new javax.swing.JTextField();
        ftpUserLabel = new javax.swing.JLabel();
        ftpUserTextField = new javax.swing.JTextField();
        ftpPwdLabel = new javax.swing.JLabel();
        ftpPwdTextField = new javax.swing.JPasswordField();
        showPromptjLabel = new javax.swing.JLabel();
        ftpLabel2 = new javax.swing.JLabel();
        softNameTextField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        sftpPortTextField = new javax.swing.JTextField();
        changeButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        localAddrLabel = new javax.swing.JLabel();
        remoteAddrLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        updateTextField = new javax.swing.JTextField();
        remoteAddrTextField = new javax.swing.JTextField();
        localAddrTextField = new javax.swing.JTextField();
        lookPathjButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        bankIdjTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        dotsjTextField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        webjTextField = new javax.swing.JTextField();
        loginLabel = new javax.swing.JLabel();
        loginLoadjComboBox = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        ftpLabel4 = new javax.swing.JLabel();
        timerComboBox1 = new javax.swing.JComboBox();
        timerjPanel = new javax.swing.JPanel();
        SpinnerModel spinnerModel =
        new SpinnerNumberModel(17, //initial value
            0, //min
            23, //max
            1);//step
        jSpinnerHour = new javax.swing.JSpinner(spinnerModel);
        jLabel6 = new javax.swing.JLabel();
        SpinnerModel spinnerminuteModel =
        new SpinnerNumberModel(0, //initial value
            0, //min
            59, //max
            1);//step
        jSpinnerMinute = new javax.swing.JSpinner(spinnerminuteModel);
        jLabel7 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        isZipjComboBox = new javax.swing.JComboBox();

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList1);

        setBorder(javax.swing.BorderFactory.createTitledBorder("系统参数配置"));
        setMaximumSize(new java.awt.Dimension(800, 410));
        setMinimumSize(new java.awt.Dimension(800, 410));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setMaximumSize(new java.awt.Dimension(516, 379));
        jPanel1.setMinimumSize(new java.awt.Dimension(516, 379));

        ftpLabel.setText("SFTP 服  务 器 :");

        com.poka.util.LimitDocument lit1 = new com.poka.util.LimitDocument(15);
        lit1.setAllowChar("0123456789.");
        ftpAddrTextField.setDocument(lit1);

        ftpUserLabel.setText("SFTP 账     户 :");

        ftpPwdLabel.setText("SFTP 账户 密码 :");

        showPromptjLabel.setForeground(new java.awt.Color(255, 0, 51));

        ftpLabel2.setText("软  件  名  称 :");

        jLabel5.setText("SFTP 端     口 :");

        com.poka.util.LimitDocument litPort = new com.poka.util.LimitDocument(6);
        litPort.setAllowChar("0123456789.");
        sftpPortTextField.setDocument(litPort);
        sftpPortTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sftpPortTextFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(ftpUserLabel)
                        .addGap(20, 20, 20)
                        .addComponent(ftpUserTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(ftpLabel)
                                    .addGap(20, 20, 20))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(ftpLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(ftpPwdLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(ftpPwdTextField)
                            .addComponent(softNameTextField, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(sftpPortTextField, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(ftpAddrTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE))))
                .addGap(152, 152, 152)
                .addComponent(showPromptjLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(ftpLabel))
                    .addComponent(ftpAddrTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(showPromptjLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sftpPortTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ftpUserTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(ftpUserLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ftpPwdLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ftpPwdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(softNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ftpLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 17, 354, 210));

        changeButton.setText("修改");
        changeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeButtonActionPerformed(evt);
            }
        });
        add(changeButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 320, 86, 33));

        localAddrLabel.setText("本地 文件 路径 :");

        remoteAddrLabel.setText("前置机文件路径 :");

        jLabel1.setText("软件 更新 路径 :");

        lookPathjButton.setText("浏览");
        lookPathjButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lookPathjButtonMouseClicked(evt);
            }
        });

        jLabel3.setText(" 银 行 号 :");

        bankIdjTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bankIdjTextFieldActionPerformed(evt);
            }
        });

        jLabel4.setText(" 网 点 号 :");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(localAddrLabel)
                        .addGap(20, 20, 20)
                        .addComponent(localAddrTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(lookPathjButton))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(remoteAddrLabel)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(updateTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                            .addComponent(remoteAddrTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                            .addComponent(bankIdjTextField)
                            .addComponent(dotsjTextField))))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(localAddrLabel))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(localAddrTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lookPathjButton))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(remoteAddrLabel))
                    .addComponent(remoteAddrTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(updateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bankIdjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dotsjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(368, 17, -1, 200));

        jLabel2.setText("web 查询 网址 :");
        add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 270, 98, 20));
        add(webjTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 270, 530, 20));

        loginLabel.setText(" 登  陆  模  式 :");
        add(loginLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 350, 106, -1));

        loginLoadjComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "登陆一次", "每次登陆" }));
        loginLoadjComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                loginLoadjComboBoxItemStateChanged(evt);
            }
        });
        add(loginLoadjComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 360, 164, -1));

        ftpLabel4.setText("传 输  设  定 :");

        timerComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                timerComboBox1ItemStateChanged(evt);
            }
        });

        jSpinnerHour.setRequestFocusEnabled(false);

        jLabel6.setText("时");

        jLabel7.setText("分");

        javax.swing.GroupLayout timerjPanelLayout = new javax.swing.GroupLayout(timerjPanel);
        timerjPanel.setLayout(timerjPanelLayout);
        timerjPanelLayout.setHorizontalGroup(
            timerjPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(timerjPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSpinnerHour, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSpinnerMinute, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addContainerGap())
        );
        timerjPanelLayout.setVerticalGroup(
            timerjPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, timerjPanelLayout.createSequentialGroup()
                .addGroup(timerjPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSpinnerHour, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jSpinnerMinute, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ftpLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(timerComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(timerjPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(31, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(timerjPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(ftpLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(timerComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 8, Short.MAX_VALUE))
        );

        add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, 360, 30));

        jLabel8.setText("是 否 压 缩:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel8)
                .addGap(26, 26, 26)
                .addComponent(isZipjComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(114, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(isZipjComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 220, 310, 40));
    }// </editor-fold>//GEN-END:initComponents

    private void loginLoadjComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_loginLoadjComboBoxItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            fileModelSelectIndex = this.loginLoadjComboBox.getSelectedIndex();
            isChangeFileModel = true;
        }
    }//GEN-LAST:event_loginLoadjComboBoxItemStateChanged

    private void changeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeButtonActionPerformed

        List<Arg> argList = new ArrayList<Arg>();
        String addr = this.ftpAddrTextField.getText();

        String rxt = "\\b(([01]?\\d?\\d|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d?\\d|2[0-4]\\d|25[0-5])\\b";

        Pattern pattern = Pattern.compile(rxt);
        Matcher matcher = pattern.matcher(addr);
        boolean fl = matcher.matches();
        if (!fl) {
            showWord("请输入正确的ip地址!");
            this.ftpAddrTextField.selectAll();
            return;
        }
        if (addr.equals("")) {
            showWord("SFTP服务器为空!");
            this.ftpAddrTextField.requestFocus();
            return;
        }
        String port = this.sftpPortTextField.getText().trim();
        if(port.equals("")){
            port = "22";
        }
        String user = this.ftpUserTextField.getText().trim();
        if (user.equals("")) {
            showWord("SFTP用户为空!");
            this.ftpUserTextField.requestFocus();
            return;
        }

        String pwd = this.ftpPwdTextField.getText().trim();
        if (pwd.equals("")) {
            showWord("SFTP密码为空");  
            this.ftpPwdTextField.requestFocus();
            return;
        }
        String localAddr = this.localAddrTextField.getText().trim();
        if (localAddr.equals("")) {
            showWord("本地扫描路径为空");  
            this.localAddrTextField.requestFocus();
            return;
        }
        String remoteAddr = this.remoteAddrTextField.getText().trim();
        if (remoteAddr.equals("")) {
            showWord("前置机文件路径为空");
            this.remoteAddrTextField.requestFocus();
            return;
        }

        String updateAdd = this.updateTextField.getText().trim();
        if (updateAdd.equals("")) {
            showWord("更新路径为空");
            this.updateTextField.requestFocus();
            return;
        }

        String softName = this.softNameTextField.getText();
        if (softName.equals("")) {
            showWord("软件名称为空");
            this.softNameTextField.requestFocus();
            return;
        }

        String webPath = this.webjTextField.getText();
        if (softName.equals("")) {
             showWord("web查询网址为空");
            this.webjTextField.requestFocus();
            return;
        }

        String bankId = this.bankIdjTextField.getText();
        if (softName.equals("")) {
            showWord("银行号为空");
            this.bankIdjTextField.requestFocus();
            return;
        }
        String dots = this.dotsjTextField.getText();
        if (softName.equals("")) {
            showWord("网点号为空");
            this.dotsjTextField.requestFocus();
            return;
        }
        int hour = (int)jSpinnerHour.getValue();
        int minute = (int)jSpinnerMinute.getValue();
        StaticVar.bankId = bankId;
        StaticVar.agencyNo = dots;
        xmlSax.setBankNum(bankId);
        xmlSax.setNetworkNum(dots);

        xmlSax.changeBankNumXmlAtt();
        Arg arg = new Arg(argPro.ftp, addr);
        argList.add(arg);
        Arg arg1 = new Arg(argPro.user, user);
        argList.add(arg1);
        Arg arg2 = new Arg(argPro.pwd, pwd);
        argList.add(arg2);
        Arg arg3 = new Arg(argPro.localAddr, localAddr);
        //       System.out.print("------------------" + localAddr);
        argList.add(arg3);
        Arg arg4 = new Arg(argPro.remoteAddr, remoteAddr);
        argList.add(arg4);
        Arg arg5 = new Arg(argPro.updatePath, updateAdd);
        argList.add(arg5);
        Arg arg6 = new Arg(argPro.webPtah, webPath);
        argList.add(arg6);

        Arg arg7 = new Arg(argPro.port, port);
        argList.add(arg7);
        Arg arg8 = new Arg(argPro.selfTimer, hour+"-"+minute );
        argList.add(arg8);
        String indexStr = String.valueOf(this.timerComboBox1.getSelectedIndex());
        Arg arg9 = new Arg(argPro.isTimer, indexStr );
        argList.add(arg9);
        indexStr = String.valueOf(this.isZipjComboBox.getSelectedIndex());
         Arg arg10 = new Arg(argPro.isZip, indexStr );
        argList.add(arg10);
//        if(isChangeFileModel){   //文件上次模式改变
//            Arg arg5 = new Arg(argPro.fileLoadModel,String.valueOf(fileModelSelectIndex));
//            argList.add(arg5); 
//        }
        //BaseDao<Arg> b = new BaseDao<Arg>();
        //  String sql = "update Arg set avalue=:val where akey=:ke";
        for (Arg ar : argList) {
            this.xmlSax.updateArg(ar);
        }
        this.xmlSax.setSoftTitle(softName);
        int item = this.loginLoadjComboBox.getSelectedIndex();
        if (item == 0) {
            this.xmlSax.writeNeedLogin("0");
            this.pokaMain.setNeedLogin(true);
        } else {
            this.xmlSax.writeNeedLogin("1");
            this.pokaMain.setNeedLogin(false);
        }
        this.pokaMain.oneLogin = false;

        JOptionPane.showMessageDialog(null, "修改成功!");

        UploadFtp.newDir(localAddr + File.separator + UploadFtp.tem + File.separator + "TEM");

        StaticVar.cfgMap = new HashMap<String, String>();
        for (Arg a : argList) {
            StaticVar.cfgMap.put(a.getAkey(), a.getAvalue());
        }
        if(StaticVar.cfgMap.get(argPro.isTimer).equals(StaticVar.isTimer)){
            timer(StaticVar.cfgMap.get(argPro.selfTimer));       
        }else{
            if(timerTask != null){
                timerTask.cancel();
            }
        }
        UploadFtp.createDir(StaticVar.cfgMap.get(argPro.localAddr));
    }//GEN-LAST:event_changeButtonActionPerformed
     public static void timer(String hourAndMimuti) { 
        String[] timeArray = hourAndMimuti.split("-");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(timeArray[0])); // 控制时  
        calendar.set(Calendar.MINUTE, Integer.valueOf(timeArray[1]));       // 控制分  
        calendar.set(Calendar.SECOND, 0);       // 控制秒  
        Date time = calendar.getTime();         // 得出执行任务的时间,此处为今天的12：00：00     
        if(timerTask != null){
            timerTask.cancel();
        }
        timerTask = new TimerTask() {  
            public void run() {  
                System.out.println("-------设定要指定任务--------");  
                PokaSftp.uploadFile();
            }
        };
        timer.scheduleAtFixedRate(timerTask, time, 1000 * 60 * 60 * 24);// 这里设定将延时每天固定执行  
//        timer.cancel();
    }
    private void showWord(String word){
        JOptionPane.showMessageDialog(null, word);
    }
    JFileChooser jfc = new JFileChooser();
    private void lookPathjButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lookPathjButtonMouseClicked

        File f = new File(this.localAddrTextField.getText().trim());
        if (f.exists()) {
            jfc.setCurrentDirectory(f);
        }
        jfc.setDialogTitle("选择本地扫描路径");
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = jfc.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String path = jfc.getSelectedFile().getPath();
            localAddrTextField.setText(path);
        }

    }//GEN-LAST:event_lookPathjButtonMouseClicked

    private void bankIdjTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bankIdjTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bankIdjTextFieldActionPerformed

    private void sftpPortTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sftpPortTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sftpPortTextFieldActionPerformed

    private void timerComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_timerComboBox1ItemStateChanged
        // TODO add your handling code here:
        if (ItemEvent.SELECTED == evt.getStateChange()) {
            int item = this.timerComboBox1.getSelectedIndex();
            if(item == 0){
                this.timerjPanel.setVisible(true);
            }
            else{
                this.timerjPanel.setVisible(false);
            }
        }

    }//GEN-LAST:event_timerComboBox1ItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField bankIdjTextField;
    private javax.swing.JButton changeButton;
    private javax.swing.JTextField dotsjTextField;
    private javax.swing.JTextField ftpAddrTextField;
    private javax.swing.JLabel ftpLabel;
    private javax.swing.JLabel ftpLabel2;
    private javax.swing.JLabel ftpLabel4;
    private javax.swing.JLabel ftpPwdLabel;
    private javax.swing.JPasswordField ftpPwdTextField;
    private javax.swing.JLabel ftpUserLabel;
    private javax.swing.JTextField ftpUserTextField;
    private javax.swing.JComboBox isZipjComboBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner jSpinnerHour;
    private javax.swing.JSpinner jSpinnerMinute;
    private javax.swing.JLabel localAddrLabel;
    private javax.swing.JTextField localAddrTextField;
    private javax.swing.JLabel loginLabel;
    public javax.swing.JComboBox loginLoadjComboBox;
    private javax.swing.JButton lookPathjButton;
    private javax.swing.JLabel remoteAddrLabel;
    private javax.swing.JTextField remoteAddrTextField;
    private java.awt.Scrollbar scrollbar1;
    private javax.swing.JTextField sftpPortTextField;
    private javax.swing.JLabel showPromptjLabel;
    private javax.swing.JTextField softNameTextField;
    private javax.swing.JComboBox timerComboBox1;
    private javax.swing.JPanel timerjPanel;
    private javax.swing.JTextField updateTextField;
    private javax.swing.JTextField webjTextField;
    // End of variables declaration//GEN-END:variables
   // private BaseDao<Arg> b = new BaseDao<Arg>();
    private boolean isChangeFileModel = false;
    private int fileModelSelectIndex = 0;
    private PokaMainFrame pokaMain;
    private XmlSax xmlSax = XmlSax.getInstance();
    public static Timer timer = new Timer();
    public static TimerTask timerTask;
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (e.getSource() == this.ftpAddrTextField) {
                this.ftpUserTextField.selectAll();
                this.ftpUserTextField.requestFocus();
            } else if (e.getSource() == this.ftpUserTextField) {
                this.ftpPwdTextField.selectAll();
                this.ftpPwdTextField.requestFocus();
            } else if (e.getSource() == this.ftpPwdTextField) {
                this.localAddrTextField.selectAll();
                this.localAddrTextField.requestFocus();
            } else if (e.getSource() == this.localAddrTextField) {
                this.remoteAddrTextField.selectAll();
                this.remoteAddrTextField.requestFocus();
            } else if (e.getSource() == this.remoteAddrTextField) {
                this.softNameTextField.selectAll();
                this.softNameTextField.requestFocus();
            } else if (e.getSource() == this.loginLoadjComboBox) {
                this.changeButton.requestFocus();
            } else if (e.getSource() == this.softNameTextField) {
                this.loginLoadjComboBox.requestFocus();
            } else if (e.getSource() == this.changeButton) {
                this.changeButton.doClick();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            if (e.getSource() == this.ftpAddrTextField) {
                this.ftpUserTextField.selectAll();
                this.ftpUserTextField.requestFocus();
            } else if (e.getSource() == this.ftpUserTextField) {
                this.ftpPwdTextField.selectAll();
                this.ftpPwdTextField.requestFocus();
            } else if (e.getSource() == this.ftpPwdTextField) {
                this.localAddrTextField.selectAll();
                this.localAddrTextField.requestFocus();
            } else if (e.getSource() == this.localAddrTextField) {
                this.remoteAddrTextField.selectAll();
                this.remoteAddrTextField.requestFocus();
            } else if (e.getSource() == this.remoteAddrTextField) {
                this.softNameTextField.selectAll();
                this.softNameTextField.requestFocus();
//                this.loginLoadjComboBox.requestFocus();
            } else if (e.getSource() == this.loginLoadjComboBox) {
                this.loginLoadjComboBox.showPopup();
            } else if (e.getSource() == this.softNameTextField) {
                this.loginLoadjComboBox.requestFocus();
            }// else if (e.getSource() == this.changeButton) {
//               // this.resetButton.requestFocus();
//            } else if (e.getSource() == this.resetButton) {
//                this.ftpAddrTextField.selectAll();
//                this.ftpAddrTextField.requestFocus();
//            }
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            if (e.getSource() == this.ftpAddrTextField) {
                // this.resetButton.requestFocus();
            } else if (e.getSource() == this.ftpUserTextField) {
                this.ftpAddrTextField.selectAll();
                this.ftpAddrTextField.requestFocus();
            } else if (e.getSource() == this.ftpPwdTextField) {
                this.ftpUserTextField.selectAll();
                this.ftpUserTextField.requestFocus();
            } else if (e.getSource() == this.localAddrTextField) {
                this.ftpPwdTextField.selectAll();
                this.ftpPwdTextField.requestFocus();
            } else if (e.getSource() == this.remoteAddrTextField) {
                this.localAddrTextField.selectAll();
                this.localAddrTextField.requestFocus();
            } else if (e.getSource() == this.loginLoadjComboBox) {
                this.loginLoadjComboBox.showPopup();
            } else if (e.getSource() == this.softNameTextField) {
//                 this.loginLoadjComboBox.requestFocus();
                this.remoteAddrTextField.selectAll();
                this.remoteAddrTextField.requestFocus();
            } else if (e.getSource() == this.changeButton) {
                this.loginLoadjComboBox.requestFocus();
            } //else if (e.getSource() == this.resetButton) {
//                this.changeButton.requestFocus();
//            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    /**
     * @return the pokaMain
     */
    public PokaMainFrame getPokaMain() {
        return pokaMain;
    }

    /**
     * @param pokaMain the pokaMain to set
     */
    public void setPokaMain(PokaMainFrame pokaMain) {
        this.pokaMain = pokaMain;
    }
}
