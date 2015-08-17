/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poka.app.panel;

import com.poka.printer.com.ComClient;
import com.poka.util.PostekUtil;
import com.poka.util.BundleDeal;
import com.poka.util.XmlSax;
import gnu.io.CommPortIdentifier;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class ReprintJPanel extends javax.swing.JPanel {

    /**
     * Creates new form ReprintJPanel
     */
    public ReprintJPanel() {
        initComponents();
        this.msgLabel.setForeground(Color.red);
        this.xml = XmlSax.getInstance();
        itemFlag = false;
        CommPortIdentifier portId = null;
        Enumeration portIdentifier = ComClient.getSerialPorts();
        while (portIdentifier.hasMoreElements()) {
            portId = (CommPortIdentifier) portIdentifier.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                this.comNumberComboBox.addItem(portId.getName());
            }
        }
        String sItem = this.xml.getLastCom().trim();
        this.comNumberComboBox.setEditable(false);
        if (sItem != null) {
            this.comNumberComboBox.setSelectedItem(sItem);
        }
        String paper = this.xml.getLastPaperSize().trim();
        this.lastPaperSizeComboBox.setEditable(false);
        if (paper != null) {
            this.lastPaperSizeComboBox.setSelectedItem(paper);
        }
        itemFlag = true;

    }
    private XmlSax xml;
    private boolean itemFlag = false;

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        comNumberComboBox = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        printButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        labelTypeComboBox = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        fkTextField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        fhTextField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        rmbComboBox = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        kunTextField = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        lastPaperSizeComboBox = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        labelTextField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        bankTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        msgLabel = new javax.swing.JLabel();

        setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        setMaximumSize(new java.awt.Dimension(467, 251));

        comNumberComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comNumberComboBoxItemStateChanged(evt);
            }
        });

        jLabel1.setText("端    口：");

        printButton.setText("打印");
        printButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                printButtonMouseClicked(evt);
            }
        });

        jLabel3.setText("标签类型：");

        labelTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "ATM标签", "钞箱标签", "捆标签" }));
        labelTypeComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                labelTypeComboBoxItemStateChanged(evt);
            }
        });

        jLabel5.setText("封 捆 员:");

        jLabel6.setText("复 核 员:");

        fhTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fhTextFieldActionPerformed(evt);
            }
        });

        jLabel7.setText("币    值:");

        rmbComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "100元", "50元", "20元", "10元" }));

        jLabel8.setText("每捆把数:");

        com.poka.util.LimitDocument doc = new com.poka.util.LimitDocument(5);
        doc.setAllowChar("0123456789");
        kunTextField.setDocument(doc);
        kunTextField.setText("10");

        jLabel9.setText("封签尺寸:");

        lastPaperSizeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "155*77", "100*50" }));
        lastPaperSizeComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                lastPaperSizeComboBoxItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(comNumberComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lastPaperSizeComboBox, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rmbComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(fkTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(9, 9, 9)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(kunTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fhTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(printButton)
                .addGap(20, 20, 20))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comNumberComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel5)
                    .addComponent(fkTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(printButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(labelTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(fhTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(rmbComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8)
                        .addComponent(kunTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(lastPaperSizeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jLabel2.setText("银行名称：");

        bankTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bankTextFieldActionPerformed(evt);
            }
        });

        jLabel4.setText("标 签 号：");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(msgLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
                    .addComponent(bankTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
                    .addComponent(labelTextField))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(bankTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(msgLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void bankTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bankTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bankTextFieldActionPerformed

    private void printButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_printButtonMouseClicked
        String comNum = this.comNumberComboBox.getSelectedItem().toString();
        int labelType = this.labelTypeComboBox.getSelectedIndex();
        String bankName = this.bankTextField.getText().trim();
        double kunNum = Double.parseDouble(this.kunTextField.getText().trim());
        if (bankName == null || bankName.length() <= 0) {
            JOptionPane.showMessageDialog(null, "请输入银行名称！");
            this.bankTextField.selectAll();
            return;
        }
        String labelText = this.labelTextField.getText().toUpperCase().trim();
        if (labelText == null || labelText.length() <= 0) {
            JOptionPane.showMessageDialog(null, "请输入标签号！");
            this.labelTextField.selectAll();
            return;
        }
        PostekUtil post = new PostekUtil();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = BundleDeal.getDBTime();
        String printDate = sdf.format(date);

        int selectSize = this.lastPaperSizeComboBox.getSelectedIndex();
        String rmb = (String) this.rmbComboBox.getSelectedItem();
        rmb = rmb.substring(0, rmb.length() - 1);
        switch (labelType) {
            case 0:
                if (!labelText.startsWith("A")) {
                    JOptionPane.showMessageDialog(null, "请输入正确的A开头18位ATM标签号！");
                    this.labelTextField.selectAll();
                    return;
                } else if (labelText.trim().length() != 18) {
                    JOptionPane.showMessageDialog(null, "请输入正确的A开头18位ATM标签号！");
                    this.labelTextField.selectAll();
                    return;
                } else {
                    if (0 == selectSize) {
                        post.printFQ(comNum, labelText, bankName + "ATM机");
                    } else {
                        post.printGX100X50FQ(comNum, labelText, bankName + "ATM机");
                    }

                }
                break;
            case 1:
                if (!labelText.startsWith("C")) {
                    JOptionPane.showMessageDialog(null, "请输入正确的C开头11位钞箱标签号！");
                    this.labelTextField.selectAll();
                    return;
                } else if (labelText.trim().length() != 11) {
                    JOptionPane.showMessageDialog(null, "请输入正确的C开头11位钞箱标签号！");
                    this.labelTextField.selectAll();
                    return;
                } else {
                    if (0 == selectSize) {
                        post.printFQ(comNum, labelText, bankName + "ATM机钞箱");
                    } else {
                        post.printGX100X50FQ(comNum, labelText, bankName + "ATM机钞箱");
                    }
                }
                break;
            case 2:
                String inventory = this.fkTextField.getText().trim();
                if (inventory == null || inventory.length() <= 0) {
                    JOptionPane.showMessageDialog(null, "请输入封捆员！");
                    this.fkTextField.selectAll();
                    return;
                }
                String auditer = this.fhTextField.getText().trim();
                if (auditer == null || auditer.length() <= 0) {
                    JOptionPane.showMessageDialog(null, "请输入复核员！");
                    this.fhTextField.selectAll();
                    return;
                }
                String rxt = "\\b(\\d{8}[59]{1}[abcdeABCDE]{1}[12345]{1}\\d{13})\\b";
                Pattern pattern = Pattern.compile(rxt);
                Matcher matcher = pattern.matcher(labelText);
                boolean b = matcher.matches();
                if (!b) {
                    JOptionPane.showMessageDialog(null, "请输入正确的24为捆标签！");
                    this.labelTextField.selectAll();
                    return;
                }
                if (0 == selectSize) {
                    post.printWZ(comNum, printDate, auditer, rmb, inventory, labelText, bankName, kunNum);
                } else {
                    post.printGX100X50(comNum, printDate, auditer, rmb, inventory, labelText, bankName, kunNum);
                }
                break;
            default:
                break;
        }
    }//GEN-LAST:event_printButtonMouseClicked

    private void fhTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fhTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fhTextFieldActionPerformed

    private void labelTypeComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_labelTypeComboBoxItemStateChanged
        int index = labelTypeComboBox.getSelectedIndex();
        switch (index) {
            case 0: {
                this.msgLabel.setText("*18位A开头的ATM标签");
                break;
            }
            case 1: {
                this.msgLabel.setText("*11位C开头的钞箱标签");
                break;
            }
            case 2: {
                this.msgLabel.setText("*24位捆标签");
                break;
            }
        }
    }//GEN-LAST:event_labelTypeComboBoxItemStateChanged

    private void comNumberComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comNumberComboBoxItemStateChanged
        if (itemFlag) {
            String com = (String) this.comNumberComboBox.getSelectedItem();
            this.xml.setLastCom(com);
        }
    }//GEN-LAST:event_comNumberComboBoxItemStateChanged

    private void lastPaperSizeComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_lastPaperSizeComboBoxItemStateChanged
        if (itemFlag) {
            String paper = (String) this.lastPaperSizeComboBox.getSelectedItem();
            this.xml.setLastPaperSize(paper);
        }
    }//GEN-LAST:event_lastPaperSizeComboBoxItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField bankTextField;
    private javax.swing.JComboBox comNumberComboBox;
    private javax.swing.JTextField fhTextField;
    private javax.swing.JTextField fkTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField kunTextField;
    private javax.swing.JTextField labelTextField;
    private javax.swing.JComboBox labelTypeComboBox;
    private javax.swing.JComboBox lastPaperSizeComboBox;
    private javax.swing.JLabel msgLabel;
    private javax.swing.JButton printButton;
    private javax.swing.JComboBox rmbComboBox;
    // End of variables declaration//GEN-END:variables
}
