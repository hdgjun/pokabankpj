/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poka.app.frame;

import com.poka.app.impl.PanelMessageI;
import com.poka.entity.FsnComProperty;
import com.poka.entity.MachinesCfg;
import com.poka.util.LogManager;
import com.poka.util.XmlSax;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Administrator
 */
public class MechinaConfigJFrame extends javax.swing.JFrame implements KeyListener {

     static final Logger logger = LogManager.getLogger(MechinaConfigJFrame.class);
    /**
     * Creates new form MechinaConfigJFrame
     */
    public MechinaConfigJFrame(int type) {
        initComponents();
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.ipTextField.addKeyListener(this);
        this.typeComboBox.addKeyListener(this);
        this.user1TextField.addKeyListener(this);
        this.user2TextField.addKeyListener(this);
     //   this.saveButton.addKeyListener(this);
        this.addButton.addKeyListener(this);
        this.ipTextField.selectAll();
        this.ipTextField.requestFocus();

        // this.setAlwaysOnTop(true);
        Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包    
        Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸   
        int screenWidth = screenSize.width; // 获取屏幕的宽   
        int screenHeight = screenSize.height; // 获取屏幕的高    
        //   System.out.print(screenWidth);
        this.setBounds((screenWidth - this.getWidth()) / 2, (screenHeight - this.getHeight()) / 2, this.getWidth(), this.getHeight());

        tableModle = new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "机具类型", "IP地址", "操作员", "复核员"
                }
        ) {
            Class[] types = new Class[]{
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        };
        this.cfgType = type;
        if (this.cfgType == FsnComProperty.guaoBusType) {
            this.typeComboBox.setModel(new javax.swing.DefaultComboBoxModel(spMechinasKeHu));
        } else {
            this.typeComboBox.setModel(new javax.swing.DefaultComboBoxModel(spMechinas));
        }
        List<MachinesCfg> cfg;
        if (this.cfgType == FsnComProperty.comBusType) {
            cfg = xml.getMachines();
        } else if (this.cfgType == FsnComProperty.atmAddBusType) {
            cfg = xml.getAddMachines();
        } else {
            cfg = xml.getGuaoMachines();
        }
        String[] newRow = new String[tableModle.getColumnCount()];
        for (MachinesCfg tem : cfg) {
            if (tem.getType() == this.cfgType) {
                newRow[0] = tem.getMachineType();
                newRow[1] = tem.getIp();
                newRow[2] = tem.getUser1();
                newRow[3] = tem.getUser2();
                tableModle.addRow(newRow);
            }
        }
        
        this.showTable.setModel(tableModle);
        this.showTable.invalidate();
        popupmenu = new JPopupMenu();
        delete = new JMenuItem("删除");

        delete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                Thread tt = new Thread(new Runnable() {
                    public void run() {
                        DefaultTableModel pokaModel = (DefaultTableModel) showTable.getModel();
                        int index = showTable.getSelectedRow();
                        if (index >= 0 && index < pokaModel.getRowCount()) {
                            MachinesCfg tem = new MachinesCfg();
                            tem.setMachineType((String) pokaModel.getValueAt(index, 0));
                            tem.setIp((String) pokaModel.getValueAt(index, 1));
                            tem.setUser1((String) pokaModel.getValueAt(index, 2));
                            tem.setUser2((String) pokaModel.getValueAt(index, 3));
                            tem.setType(cfgType);
                            xml.deleteMachineInfo(tem);
                            pokaModel.removeRow(index);
                            showTable.invalidate();
                        } else {
                            JOptionPane.showMessageDialog(null, "请选择一条要删除的数据！");
                        }
                    }
                ;
                });
                  tt.start();
            }
        });

        popupmenu.add(delete);
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
        showTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        ipTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        typeComboBox = new javax.swing.JComboBox();
        addButton = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        user1TextField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        user2TextField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        showTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(showTable);

        jLabel1.setText("已配置机具：");

        jLabel2.setText("ip地址：");

        com.poka.util.LimitDocument lit1 = new com.poka.util.LimitDocument(15);
        lit1.setAllowChar("0123456789.");
        ipTextField.setDocument(lit1);
        ipTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ipTextFieldActionPerformed(evt);
            }
        });

        jLabel3.setText("机具类型：");

        addButton.setText("添加");
        addButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addButtonMouseClicked(evt);
            }
        });

        jLabel4.setText("操作员：");

        com.poka.util.LimitDocument lit2= new com.poka.util.LimitDocument(8);
        lit2.setAllowChar("0123456789.");
        user1TextField.setDocument(lit2);

        jLabel5.setText("复核员：");

        com.poka.util.LimitDocument lit3= new com.poka.util.LimitDocument(8);
        lit3.setAllowChar("0123456789.");
        user2TextField.setDocument(lit3);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2)
                            .addComponent(ipTextField)
                            .addComponent(typeComboBox, 0, 150, Short.MAX_VALUE)
                            .addComponent(user1TextField)
                            .addComponent(user2TextField)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ipTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(typeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(user1TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addComponent(user2TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(51, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ipTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ipTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ipTextFieldActionPerformed

    private void showTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_showTableMouseClicked
        if (evt.getButton() == MouseEvent.BUTTON3) {
            popupmenu.show(evt.getComponent(), evt.getX(), evt.getY());
        }
        if(evt.getButton() == MouseEvent.BUTTON1){
           DefaultTableModel pokaModel = (DefaultTableModel) showTable.getModel();
                        int index = showTable.getSelectedRow();
                        if (index >= 0 && index < pokaModel.getRowCount()) {
                            ipTextField.setText((String) pokaModel.getValueAt(index, 1));
                            user1TextField.setText((String) pokaModel.getValueAt(index, 2));
                            typeComboBox.setSelectedIndex(getMechinaType((String) pokaModel.getValueAt(index, 0),this.cfgType));
                            user2TextField.setText((String) pokaModel.getValueAt(index, 3));                     
                        }
        }
      
        
                
      
    }//GEN-LAST:event_showTableMouseClicked
    private int getMechinaType(String me,int type) {
        int i = 0;
        if(FsnComProperty.comBusType == type ){
        for (String s : spMechinas) {
          if (s.trim().equals(me.trim())) {
                return i;
            } else {
                i++;
            }
        }
        }
        
        if(FsnComProperty.guaoBusType == type ){
        for (String s : spMechinasKeHu) {
          if (s.trim().equals(me.trim())) {
                return i;
            } else {
                i++;
            }
        }
        }
        return -1;
    }
    
    String[] spMechinas = new String[]{"中钞信达点钞机", "伊特诺点钞机", "仁杰点钞机", "新大点钞机", "辽宁聚龙","古鳌点钞机","维融点钞机"};
    
    String[] spMechinasKeHu = new String[]{"中钞信达点钞机","2015版中钞信达点钞机", "古鳌点钞机"};

    private void addButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addButtonMouseClicked
        String ip = this.ipTextField.getText().trim();
        String mType = (String) this.typeComboBox.getSelectedItem();
        int iType = this.typeComboBox.getSelectedIndex();
        String user1 = this.user1TextField.getText();
        String user2 = this.user2TextField.getText();
        String rxt = "\\b(([01]?\\d?\\d|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d?\\d|2[0-4]\\d|25[0-5])\\b";

        Pattern pattern = Pattern.compile(rxt);
        Matcher matcher = pattern.matcher(ip);
        boolean b = matcher.matches();
        if (!b) {
            this.ipTextField.selectAll();
            this.ipTextField.requestFocus();
            JOptionPane.showMessageDialog(null, "请输入正确的ip地址!");
            return;
        }

        MachinesCfg tem = new MachinesCfg();
        tem.setMachineType(mType);
        tem.setMachineNum(iType);
        tem.setIp(ip);
        tem.setUser1(user1);
        tem.setUser2(user2);
        tem.setType(this.cfgType);
        int flag = xml.updateOrAddMachineInfo(tem);

        if (1 == flag) {
            String[] newRow = new String[tableModle.getColumnCount()];
            newRow[0] = mType;
            newRow[1] = ip;
            newRow[2] = user1;
            newRow[3] = user2;
            tableModle.addRow(newRow);
            this.showTable.invalidate();
            JOptionPane.showMessageDialog(null, "新增成功！");
        } else if (0 == flag) {
            int rows = tableModle.getRowCount();
            for (int i = 0; i < rows; i++) {
                if (((String) (tableModle.getValueAt(i, 1))).equals(ip)) {
                    tableModle.setValueAt(mType, i, 0);
                    tableModle.setValueAt(user1, i, 2);
                    tableModle.setValueAt(user2, i, 3);
                    break;
                }
            }
            this.showTable.invalidate();
            JOptionPane.showMessageDialog(null, "更新成功！");

        } else {
            JOptionPane.showMessageDialog(null, "新增失败！");
        }

    }//GEN-LAST:event_addButtonMouseClicked

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        this.parent.controlButton(this.showTable.getRowCount());
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        int flag = 0;
        int rows = tableModle.getRowCount();
        for (int i = 0; i < rows; i++) {
            MachinesCfg tem = new MachinesCfg();
            tem.setMachineType((String) tableModle.getValueAt(i, 0));
            tem.setMachineNum(getMechinaType((String) tableModle.getValueAt(i, 0),this.cfgType));
            tem.setIp((String) tableModle.getValueAt(i, 1));
            tem.setUser1((String) tableModle.getValueAt(i, 2));
            tem.setUser2((String) tableModle.getValueAt(i, 3));
            tem.setType(this.cfgType);
            flag = xml.updateOrAddMachineInfo(tem);
            if (-1 == flag) {
                return;
            }
        }
        if (this.cfgType == FsnComProperty.comBusType) {
            this.parent.flashMach();
        } else {
            this.parent.flashMachClient();
        }
        this.parent.controlButton(rows);

    }//GEN-LAST:event_formWindowClosing

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
            logger.log(Level.INFO, null,ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MechinaConfigJFrame(FsnComProperty.comBusType).setVisible(true);
            }
        });
    }

    private PanelMessageI parent;
    private XmlSax xml = XmlSax.getInstance();
    private JMenuItem delete = null;
    private JPopupMenu popupmenu = null;
    private DefaultTableModel tableModle = null;
    private int cfgType;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JTextField ipTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable showTable;
    private javax.swing.JComboBox typeComboBox;
    private javax.swing.JTextField user1TextField;
    private javax.swing.JTextField user2TextField;
    // End of variables declaration//GEN-END:variables

    /**
     * @param parent the parent to set
     */
    public void setParent(PanelMessageI parent) {
        this.parent = parent;
    }

    /**
     * @return the cfgType
     */
    public int getCfgType() {
        return cfgType;
    }

    /**
     * @param cfgType the cfgType to set
     */
    public void setCfgType(int cfgType) {
        this.cfgType = cfgType;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (e.getSource() == this.ipTextField) {
                this.typeComboBox.requestFocus();
            } else if (e.getSource() == this.typeComboBox) {
                this.user1TextField.selectAll();
                this.user1TextField.requestFocus();
            } else if (e.getSource() == this.user1TextField) {
                this.user2TextField.selectAll();
                this.user2TextField.requestFocus();
            } else if (e.getSource() == this.user2TextField) {
                this.addButton.requestFocus();
            } //else if (e.getSource() == this.addButton) {
                //   this.addButton.doClick();
//            } else if (e.getSource() == this.saveButton) {
//                //  this.saveButton.doClick();
//            }
        }

        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            if (e.getSource() == this.ipTextField) {
                this.typeComboBox.requestFocus();
            } else if (e.getSource() == this.typeComboBox) {
                this.typeComboBox.showPopup();
            } else if (e.getSource() == this.user1TextField) {
                this.user2TextField.selectAll();
                this.user2TextField.requestFocus();
            } else if (e.getSource() == this.user2TextField) {
                this.addButton.requestFocus();
            } //else if (e.getSource() == this.addButton) {
//                this.saveButton.requestFocus();
//            } else if (e.getSource() == this.saveButton) {
//                this.ipTextField.selectAll();
//                this.ipTextField.requestFocus();
//            }
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
//            if (e.getSource() == this.ipTextField) {
//                this.saveButton.requestFocus();
//            } else 
            if (e.getSource() == this.typeComboBox) {
                this.typeComboBox.showPopup();
            } else if (e.getSource() == this.user1TextField) {
                this.typeComboBox.requestFocus();
            } else if (e.getSource() == this.user2TextField) {
                this.user1TextField.selectAll();
                this.user1TextField.requestFocus();
            } else if (e.getSource() == this.addButton) {
                this.user2TextField.selectAll();
                this.user2TextField.requestFocus();
            } //else if (e.getSource() == this.saveButton) {
//                this.addButton.requestFocus();
//            }
        }

//        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
//
//            if (e.getSource() == this.addButton) {
//                this.saveButton.requestFocus();
//            }
//        }
//        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
//            if (e.getSource() == this.saveButton) {
//                this.addButton.requestFocus();
//            }
//        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
