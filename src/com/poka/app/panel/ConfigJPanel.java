/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poka.app.panel;

import com.poka.app.frame.PokaMainFrame;
import com.poka.entity.Arg;
import com.poka.util.StaticVar;
import com.poka.util.XmlSax;
import com.poka.util.argPro;
import com.poka.util.UploadFtp;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

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

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList1);

        setBorder(javax.swing.BorderFactory.createTitledBorder("系统参数配置"));
        setMaximumSize(new java.awt.Dimension(755, 310));
        setMinimumSize(new java.awt.Dimension(755, 310));
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
        add(changeButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(325, 259, 86, 33));

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
        add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 230, 98, 20));
        add(webjTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 230, 530, 20));

        loginLabel.setText(" 登  陆  模  式 :");
        add(loginLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 330, 106, -1));

        loginLoadjComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "登陆一次", "每次登陆" }));
        loginLoadjComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                loginLoadjComboBoxItemStateChanged(evt);
            }
        });
        add(loginLoadjComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 320, 164, -1));
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
            this.showPromptjLabel.setText("请输入正确的ip地址!");
            this.ftpAddrTextField.selectAll();
            return;
        }
        if (addr.equals("")) {
            this.showPromptjLabel.setText("SFTP服务器为空");
            this.ftpAddrTextField.requestFocus();
            return;
        }
        String port = this.sftpPortTextField.getText().trim();
        if(port.equals("")){
            port = "22";
        }
        String user = this.ftpUserTextField.getText().trim();
        if (user.equals("")) {
            this.showPromptjLabel.setText("SFTP用户为空");
            this.ftpUserTextField.requestFocus();
            return;
        }

        String pwd = this.ftpPwdTextField.getText().trim();
        if (pwd.equals("")) {
            this.showPromptjLabel.setText("SFTP密码为空");
            this.ftpPwdTextField.requestFocus();
            return;
        }
        String localAddr = this.localAddrTextField.getText().trim();
        if (localAddr.equals("")) {
            this.showPromptjLabel.setText("本地扫描路径为空");
            this.localAddrTextField.requestFocus();
            return;
        }
        String remoteAddr = this.remoteAddrTextField.getText().trim();
        if (remoteAddr.equals("")) {
            this.showPromptjLabel.setText("前置机文件路径为空");
            this.remoteAddrTextField.requestFocus();
            return;
        }

        String updateAdd = this.updateTextField.getText().trim();
        if (updateAdd.equals("")) {
            this.showPromptjLabel.setText("软件名称为空");
            this.updateTextField.requestFocus();
            return;
        }

        String softName = this.softNameTextField.getText();
        if (softName.equals("")) {
            this.showPromptjLabel.setText("软件名称为空");
            this.softNameTextField.requestFocus();
            return;
        }

        String webPath = this.webjTextField.getText();
        if (softName.equals("")) {
            this.showPromptjLabel.setText("web查询网址为空");
            this.webjTextField.requestFocus();
            return;
        }

        String bankId = this.bankIdjTextField.getText();
        if (softName.equals("")) {
            this.showPromptjLabel.setText("银行号为空");
            this.bankIdjTextField.requestFocus();
            return;
        }
        String dots = this.dotsjTextField.getText();
        if (softName.equals("")) {
            this.showPromptjLabel.setText("网点号为空");
            this.dotsjTextField.requestFocus();
            return;
        }
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

         UploadFtp.createDir(StaticVar.cfgMap.get(argPro.localAddr));
    }//GEN-LAST:event_changeButtonActionPerformed
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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField bankIdjTextField;
    private javax.swing.JButton changeButton;
    private javax.swing.JTextField dotsjTextField;
    private javax.swing.JTextField ftpAddrTextField;
    private javax.swing.JLabel ftpLabel;
    private javax.swing.JLabel ftpLabel2;
    private javax.swing.JLabel ftpPwdLabel;
    private javax.swing.JPasswordField ftpPwdTextField;
    private javax.swing.JLabel ftpUserLabel;
    private javax.swing.JTextField ftpUserTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
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
    private javax.swing.JTextField updateTextField;
    private javax.swing.JTextField webjTextField;
    // End of variables declaration//GEN-END:variables
   // private BaseDao<Arg> b = new BaseDao<Arg>();
    private boolean isChangeFileModel = false;
    private int fileModelSelectIndex = 0;
    private PokaMainFrame pokaMain;
    private XmlSax xmlSax = XmlSax.getInstance();

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