/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poka.app.frame;

import com.poka.util.LoginResult;
import com.poka.util.LoginUtil;
import com.poka.util.StaticVar;
import com.poka.util.XmlSax;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author poka
 */
public class TwoLoginFrame extends javax.swing.JFrame implements KeyListener {

    /**
     * Creates new form TwoLoginFrame
     */
    private static TwoLoginFrame twoLogin = null;

    public static TwoLoginFrame gettwoLoginFrame() {
        if (twoLogin == null) {
            twoLogin = new TwoLoginFrame();
        }
        return twoLogin;
    }

    public TwoLoginFrame() {
        this.getParent();
        initComponents();
        this.userA.addKeyListener(this);
        this.pwdA.addKeyListener(this);
        this.userB.addKeyListener(this);
        this.pwdB.addKeyListener(this);
        this.loadingButton.addKeyListener(this);
        this.reset.addKeyListener(this);
        //禁止最大化
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        loadingButton = new javax.swing.JButton();
        reset = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        userA = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        prompt1 = new javax.swing.JLabel();
        prompt2 = new javax.swing.JLabel();
        prompt5 = new javax.swing.JLabel();
        pwdA = new javax.swing.JPasswordField();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        userB = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        prompt3 = new javax.swing.JLabel();
        prompt4 = new javax.swing.JLabel();
        prompt6 = new javax.swing.JLabel();
        pwdB = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("用户登录");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                windowclose(evt);
            }
        });

        loadingButton.setText("登录");
        loadingButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadingButtonActionPerformed(evt);
            }
        });

        reset.setText("重置");
        reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("操作员登录"));

        jLabel4.setText("用户名:");

        jLabel5.setText("密  码:");

        prompt1.setForeground(new java.awt.Color(255, 51, 51));

        prompt2.setForeground(new java.awt.Color(255, 51, 51));

        prompt5.setForeground(new java.awt.Color(255, 0, 0));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(2, 2, 2)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(pwdA)
                    .addComponent(prompt5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(userA, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(prompt1, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(prompt2, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(prompt5, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(userA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4))
                    .addComponent(prompt1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(pwdA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(prompt2, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(31, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(" 复核员登录"));

        jLabel6.setText("用户名:");

        jLabel7.setText("密  码:");

        prompt3.setForeground(new java.awt.Color(255, 51, 51));

        prompt4.setForeground(new java.awt.Color(255, 51, 51));

        prompt6.setForeground(new java.awt.Color(255, 0, 51));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(68, 68, 68)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(prompt6, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(pwdB)
                            .addComponent(userB, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(prompt4, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(prompt3, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(prompt6, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(prompt3, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(userB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(prompt4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7)
                        .addComponent(pwdB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(33, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 32, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(32, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(loadingButton)
                .addGap(79, 79, 79)
                .addComponent(reset)
                .addGap(123, 123, 123))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(loadingButton)
                    .addComponent(reset))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void windowclose(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_windowclose
        this.pokaMain.setEnabled(true);
        this.pokaMain.requestFocus();
        this.pokaMain.toFront();
    }//GEN-LAST:event_windowclose

    private XmlSax xmlsax = XmlSax.getInstance();
    private void loadingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadingButtonActionPerformed
        this.prompt1.setText("");
        this.prompt2.setText("");
        this.prompt3.setText("");
        this.prompt4.setText("");
        this.prompt5.setText("");
        this.prompt6.setText("");
        String user1 = this.getUserA().getText();
        String pwd1 = this.getPwdA().getText();
        String user2 = this.getUserB().getText();
        String pwd2 = this.getPwdB().getText();
        if (user1.equals("")) {
            this.prompt1.setText("用户名为空");
            return;
        }
        if (pwd1.equals("")) {
            this.prompt2.setText("密码为空");
            return;
        }
        if (user2.equals("")) {
            this.prompt3.setText("用户名为空");
            return;
        }
        if (pwd2.equals("")) {
            this.prompt4.setText("密码为空");
            return;
        }
        
        if(user2.equals(user1)){
            this.prompt3.setText("不能使用同一用户登录");
            return;
        }
        
        LoginResult res = LoginUtil.login(xmlsax.getIp(), xmlsax.getPort(), user1, pwd1);
        LoginResult res1;
        if (res.isFlag()) {
            res1 = LoginUtil.login(xmlsax.getIp(), xmlsax.getPort(), user2, pwd2);
           
            if (res1.isFlag()) {
                StaticVar.checkUser = res1.getLoginCode();
                //StaticVar.TwoLoginFrameIdent = "1";
                this.pokaMain.setLoginUser(user1);
                this.pokaMain.setCheckUser(user2);
                this.pokaMain.setLoginUserName(res.getLoginName());
                this.pokaMain.setCheckUserName(res1.getLoginName());
                this.pokaMain.login = true;
                this.pokaMain.oneLogin = true;
                this.pokaMain.setEnabled(true);
                this.pokaMain.requestFocus();
                this.pokaMain.toFront();
                this.pokaMain.addPanel((JPanel) this.pokaMain.getCurPanel());
                this.pokaMain.getCurPanel().initLogin();
                this.setVisible(false);
                this.dispose();
            } else {
                this.prompt6.setText("用户名或密码错误");
                this.userB.selectAll();
                this.userB.requestFocus();
            }
        } else {
            this.prompt5.setText("用户名或密码错误");
            this.userA.selectAll();
            this.userA.requestFocus();
        }
    }//GEN-LAST:event_loadingButtonActionPerformed

    private void resetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetActionPerformed

        this.getUserA().setText("");
        this.getPwdA().setText("");
        this.getUserB().setText("");
        this.getPwdB().setText("");
        this.userA.requestFocus();
    }//GEN-LAST:event_resetActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TwoLoginFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TwoLoginFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton loadingButton;
    private javax.swing.JLabel prompt1;
    private javax.swing.JLabel prompt2;
    private javax.swing.JLabel prompt3;
    private javax.swing.JLabel prompt4;
    private javax.swing.JLabel prompt5;
    private javax.swing.JLabel prompt6;
    private javax.swing.JPasswordField pwdA;
    private javax.swing.JPasswordField pwdB;
    private javax.swing.JButton reset;
    private javax.swing.JTextField userA;
    private javax.swing.JTextField userB;
    // End of variables declaration//GEN-END:variables

    private PokaMainFrame pokaMain;

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

    /**
     * @return the pwdA
     */
    public javax.swing.JTextField getPwdA() {
        return pwdA;
    }

    /**
     * @return the pwdB
     */
    public javax.swing.JTextField getPwdB() {
        return pwdB;
    }

    /**
     * @return the userA
     */
    public javax.swing.JTextField getUserA() {
        return userA;
    }

    /**
     * @return the userB
     */
    public javax.swing.JTextField getUserB() {
        return userB;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (e.getSource() == this.userA) {
                this.pwdA.selectAll();
                this.pwdA.requestFocus();
            } else if (e.getSource() == this.pwdA) {
                this.userB.selectAll();
                this.userB.requestFocus();
            } else if (e.getSource() == this.userB) {
                this.pwdB.selectAll();
                this.pwdB.requestFocus();
            } else if (e.getSource() == this.pwdB) {
                this.loadingButton.requestFocus();
            } else if (e.getSource() == this.loadingButton) {
               // this.loadingButton.doClick();
            } else if (e.getSource() == this.reset) {
               // this.reset.doClick();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            if (e.getSource() == this.userA) {
                this.reset.requestFocus();
            } else if (e.getSource() == this.pwdA) {
                this.userA.selectAll();
                this.userA.requestFocus();
            } else if (e.getSource() == this.userB) {
                this.pwdA.selectAll();
                this.pwdA.requestFocus();
            } else if (e.getSource() == this.pwdB) {
                this.userB.selectAll();
                this.userB.requestFocus();
            } else if (e.getSource() == this.loadingButton) {
                this.pwdB.selectAll();
                this.pwdB.requestFocus();
            } else if (e.getSource() == this.reset) {
                this.loadingButton.requestFocus();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            if (e.getSource() == this.userA) {
                this.pwdA.selectAll();
                this.pwdA.requestFocus();
            } else if (e.getSource() == this.pwdA) {
                this.userB.selectAll();
                this.userB.requestFocus();
            } else if (e.getSource() == this.userB) {
                this.pwdB.selectAll();
                this.pwdB.requestFocus();
            } else if (e.getSource() == this.pwdB) {
                this.loadingButton.requestFocus();
            } else if (e.getSource() == this.loadingButton) {
                this.reset.requestFocus();
            } else if (e.getSource() == this.reset) {
                this.userA.selectAll();
                this.userA.requestFocus();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {

            if (e.getSource() == this.loadingButton) {
                this.reset.requestFocus();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (e.getSource() == this.reset) {
                this.loadingButton.requestFocus();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}