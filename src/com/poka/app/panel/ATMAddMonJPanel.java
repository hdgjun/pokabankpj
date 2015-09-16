/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the templatecomComboBox in the editor.
 */
package com.poka.app.panel;

import com.poka.app.frame.MechinaConfigJFrame;
import com.poka.app.frame.PokaMainFrame;
import com.poka.app.frame.SqlServerCfgJDialog;
import com.poka.app.impl.LoginActionI;
import com.poka.app.impl.PanelMessageI;
import com.poka.app.util.FileUploadMonitor;
import com.poka.dao.impl.BaseDaoSqlServer;
import com.poka.entity.BfBody;
import com.poka.entity.BfFile;
import com.poka.entity.FsnComProperty;
import com.poka.entity.MoneyData;
import com.poka.entity.OperationUser;
import com.poka.entity.PanelMsgEntity;
import com.poka.entity.PokaFsn;
import com.poka.entity.PokaFsnBody;
import com.poka.printer.com.ComClient;
import com.poka.serialport.PortListener;
import com.poka.socket.FsnListenServer;
import com.poka.socket.GuAoQin;
import com.poka.socket.KoreanBrandExtension;
import com.poka.util.BundleDeal;
import com.poka.util.IOUtil;
import com.poka.util.LogManager;
import com.poka.util.StaticVar;
import com.poka.util.UploadFtp;
import com.poka.util.XmlSax;
import com.poka.util.argPro;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Administrator
 */
public class ATMAddMonJPanel extends javax.swing.JPanel implements ActionListener, LoginActionI, PanelMessageI {

    static final Logger logger = LogManager.getLogger(ATMAddMonJPanel.class);

    /**
     * Creates new form ATMAddMon
     */
    public ATMAddMonJPanel() {
        initComponents();
        jTabbedPane2.remove(1);
        // jPanel7.setVisible(false);
        uploadProgressBar.setVisible(false);
        uploadProgressBar.setValue(0);
        uploadProgressBar.setOrientation(JProgressBar.HORIZONTAL);
        uploadProgressBar.setStringPainted(true);
        uploadProgressBar.setBorderPainted(true);
        uploadProgressBar.setBackground(Color.pink);
        this.inputTextField.setEnabled(false);

        this.portTextField.setText("1000");
        this.countTextField.setText("1000");
        this.qcountTextField.setText("100");

        this.errLabel.setForeground(Color.red);
        this.msgShanLabel.setForeground(Color.red);
        this.limitComboBox.setSelectedIndex(1);

        String tem = StaticVar.cfgMap.get(argPro.localAddr);
        if (null != tem) {
            this.julongFilePath = tem + File.separator + UploadFtp.qfFile;
            this.addFilePath = tem + File.separator + UploadFtp.addQFFile;
            this.baseBakPath = tem + File.separator + UploadFtp.basebak;
            UploadFtp.newDir(this.addFilePath + File.separator + "tem");
            UploadFtp.newDir(this.baseBakPath + File.separator + "tem");
            UploadFtp.newDir(this.julongFilePath + File.separator + "tem");
        }

        if (1 == this.qingComboBox.getSelectedIndex()) {
            this.sqlserverCfgjButton.setVisible(true);
        } else if (3 == this.qingComboBox.getSelectedIndex()) {
            this.showPort(true);
        } else {
            this.showPort(false);
            this.sqlserverCfgjButton.setVisible(false);
            this.qmsgShanLabel.setText("文件存放路径:" + this.julongFilePath);
        }
        this.xml = XmlSax.getInstance();
        int si = xml.getAddMachines().size();
        if (si <= 0) {
            this.startButton.setEnabled(false);
            this.msgShanLabel.setText("请配置机具信息！");
        }
        this.endButton.setEnabled(false);

//        clearXmlTimer = new Timer(3000, new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                Thread tt = new Thread(new Runnable() {
//                    public void run() {
//                        if (null != xml) {
//                            xml.clearAddMon();
//                        }
//                    }
//                ;
//                });
//                  tt.start();
//            }
//        });
//        clearXmlTimer.start();
        julongTime = new Timer(10000, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Thread tt = new Thread(new Runnable() {
                    public void run() {
                        if (qingComboBox.getSelectedIndex() == 3) {
                            if (!koread_flag) {
                                koreadLis.setListenPort(Integer.parseInt(portQfTextField.getText().trim()));
                                koreadLis.setHandle(GuAoQin.class.getName());
                                koreadLis.setPath(julongFilePath);
                                koreadLis.startAccept();
                                koread_flag = true;
                            }
                        }
                        scannerMoneyDataFile();
                    }
                ;
                });
                  tt.start();
            }

        });
        qingTimer = new Timer(3000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Thread tt = new Thread(new Runnable() {
                    public void run() {
                        scannerMoneyData();
                    }
                ;
                });
                  tt.start();
            }
        });

        this.inputTextField.addActionListener(this);/*添加回车事件监听ActionListener默认是回车事件 */

        this.saoTextField.addActionListener(this);
        this.qsaoTextField.addActionListener(this);

        this.createButton.setEnabled(false);
        this.checkerTextField.setEditable(false);
        this.adderTextField.setEditable(false);

        qingModle = new DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "序号", "ATMID", "钞箱ID", "冠字号", "扫描时间"
                }
        ) {
            Class[] types = new Class[]{
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };

        this.qshowAddTable.setModel(qingModle);

        kunAtmModle = new DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "序号", "ATM", "捆标签", "扫描时间"
                }
        ) {
            Class[] types = new Class[]{
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };

        kunModle = new DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "序号", "钞箱ID", "捆标签", "扫描时间"
                }
        ) {
            Class[] types = new Class[]{
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };

        atmModle = new DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "序号", "ATMID", "钞箱ID", "扫描时间"
                }
        ) {
            Class[] types = new Class[]{
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };

        addModle = new DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "序号", "ATMID", "钞箱ID", "冠字号", "扫描时间"
                }
        ) {
            Class[] types = new Class[]{
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };
        this.showAddTable.setModel(addModle);

        if (this.showAddTable.getColumnModel().getColumnCount() > 0) {
            this.showAddTable.getColumnModel().getColumn(0).setPreferredWidth(30);
            this.showAddTable.getColumnModel().getColumn(1).setPreferredWidth(100);
            this.showAddTable.getColumnModel().getColumn(2).setPreferredWidth(170);
            this.showAddTable.getColumnModel().getColumn(3).setPreferredWidth(170);
            this.showAddTable.getColumnModel().getColumn(4).setPreferredWidth(170);
        }
        if (this.qshowAddTable.getColumnModel().getColumnCount() > 0) {
            this.qshowAddTable.getColumnModel().getColumn(0).setPreferredWidth(30);
            this.qshowAddTable.getColumnModel().getColumn(1).setPreferredWidth(170);
            this.qshowAddTable.getColumnModel().getColumn(2).setPreferredWidth(100);
            this.qshowAddTable.getColumnModel().getColumn(3).setPreferredWidth(170);
            this.qshowAddTable.getColumnModel().getColumn(4).setPreferredWidth(170);
        }
        popupmenu = new JPopupMenu();
        delete = new JMenuItem("删除");

        delete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {

                Thread tt = new Thread(new Runnable() {
                    public void run() {
                        DefaultTableModel pokaModel = (DefaultTableModel) showTable.getModel();
                        //System.out.println("shenme:" + showTable.getSelectedRow());
                        int index = showTable.getSelectedRow();
                        if (index >= 0 && index < pokaModel.getRowCount()) {
                            pokaModel.removeRow(index);
                            showTable.invalidate();
                            if (1 == typeFlag) {
                                if (index < kunFile.getCount()) {
                                    kunFile.deleteAt(index);
                                }
                            } else {
                                if (index < chaoFile.getCount()) {
                                    chaoFile.deleteAt(index);
                                }
                            }
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

        itemFlag = false;
        CommPortIdentifier portId = null;
        Enumeration portIdentifier = ComClient.getSerialPorts();
        while (portIdentifier.hasMoreElements()) {
            portId = (CommPortIdentifier) portIdentifier.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                this.comComboBox.addItem(portId.getName());
            }
        }
        String sItem = this.xml.getLastAddCom().trim();
        this.comComboBox.setEditable(false);
        if (sItem != null) {
            this.comComboBox.setSelectedItem(sItem);
        }
        itemFlag = true;
        String lo = this.xml.getLocatNO().trim();
        if (lo.length() > 0) {
            this.localHostTextField.setText(lo);
        } else {
            this.localHostTextField.setText("1");
        }

        //  moneyBase = new BaseDaoSqlServer<Map>();
        //this.inputTextField.getDocument().addDocumentListener(new SwingOnValueChaned());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        showScrollPane = new javax.swing.JScrollPane();
        showTable = new javax.swing.JTable();
        inputTextField = new javax.swing.JTextField();
        inputLabel = new javax.swing.JLabel();
        uploadProgressBar = new com.poka.app.util.FileUploadMonitor();
        errLabel = new javax.swing.JLabel();
        messLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        kunTChaoButton = new javax.swing.JButton();
        chaoTATMButton = new javax.swing.JButton();
        createButton = new javax.swing.JButton();
        clearButton = new javax.swing.JButton();
        kunTatmButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        saoTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        chaoIdTextField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        atmIdTextField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        portTextField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        countTextField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        limitComboBox = new javax.swing.JComboBox();
        jPanel6 = new javax.swing.JPanel();
        startButton = new javax.swing.JButton();
        endButton = new javax.swing.JButton();
        mechinaButton1 = new javax.swing.JButton();
        msgLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        showAddTable = new javax.swing.JTable();
        msgShanLabel = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        qsaoTextField = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        qchaoIdTextField = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        qatmIdTextField = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        qcountTextField = new javax.swing.JTextField();
        sqlserverCfgjButton = new javax.swing.JButton();
        qingComboBox = new javax.swing.JComboBox();
        jLabel14 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        maxMoneyTextField = new javax.swing.JTextField();
        maxToggleButton = new javax.swing.JToggleButton();
        portQfTextField = new javax.swing.JTextField();
        portLabel1 = new javax.swing.JLabel();
        qmsgShanLabel = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        qshowAddTable = new javax.swing.JTable();
        jPanel9 = new javax.swing.JPanel();
        qstartButton = new javax.swing.JButton();
        qendButton = new javax.swing.JButton();
        newButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        logoutButton = new javax.swing.JButton();
        adderTextField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        checkerTextField = new javax.swing.JTextField();
        openComToggleButton = new javax.swing.JToggleButton();
        jLabel9 = new javax.swing.JLabel();
        comComboBox = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        localHostTextField = new javax.swing.JTextField();
        comMsgLabel = new javax.swing.JLabel();

        setBorder(javax.swing.BorderFactory.createTitledBorder(null, "ATM加钞", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        setMaximumSize(new java.awt.Dimension(786, 631));
        setMinimumSize(new java.awt.Dimension(786, 630));

        jTabbedPane2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabbedPane2MouseClicked(evt);
            }
        });

        showTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        showTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showTableMouseClicked(evt);
            }
        });
        showScrollPane.setViewportView(showTable);

        inputTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputTextFieldActionPerformed(evt);
            }
        });

        inputLabel.setText("输入框：");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(showScrollPane)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(inputTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(inputLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(errLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(uploadProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 533, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(messLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 533, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inputLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(inputTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(showScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(errLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(messLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(uploadProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        kunTChaoButton.setText("捆进钞箱");
        kunTChaoButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                kunTChaoButtonMouseClicked(evt);
            }
        });

        chaoTATMButton.setText("钞箱进ATM");
        chaoTATMButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                chaoTATMButtonMouseClicked(evt);
            }
        });

        createButton.setText("完成");
        createButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                createButtonMouseClicked(evt);
            }
        });

        clearButton.setText("清除");
        clearButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                clearButtonMouseClicked(evt);
            }
        });

        kunTatmButton.setText("捆进ATM");
        kunTatmButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                kunTatmButtonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(chaoTATMButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(createButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(clearButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(kunTChaoButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(kunTatmButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {chaoTATMButton, clearButton, createButton, kunTChaoButton});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(48, Short.MAX_VALUE)
                .addComponent(kunTatmButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(kunTChaoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(chaoTATMButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(createButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(clearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {chaoTATMButton, clearButton, createButton, kunTChaoButton});

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(45, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 465, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41))
        );

        jTabbedPane2.addTab("整捆加钞", jPanel1);

        jLabel1.setText("扫描框：");

        jLabel4.setText("钞箱号：");

        jLabel5.setText("ATM号：");

        jLabel6.setText("监听端口:");

        com.poka.util.LimitDocument lit1 = new com.poka.util.LimitDocument(5);
        lit1.setAllowChar("0123456789");
        portTextField.setDocument(lit1);

        jLabel7.setText("张    数:");

        com.poka.util.LimitDocument lit2 = new com.poka.util.LimitDocument(5);
        lit2.setAllowChar("0123456789");
        countTextField.setDocument(lit2);

        jLabel8.setText("去重时限:");

        limitComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1分钟", "5分钟", "10分钟", "15分钟", "20分钟", "30分钟", "1小时" }));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(saoTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(chaoIdTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
                            .addComponent(atmIdTextField))))
                .addGap(59, 59, 59)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(countTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE)
                        .addComponent(portTextField))
                    .addComponent(limitComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(saoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(portTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(chaoIdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(countTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(atmIdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(limitComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        startButton.setText("开始");
        startButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                startButtonMouseClicked(evt);
            }
        });

        endButton.setText("结束");
        endButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                endButtonMouseClicked(evt);
            }
        });

        mechinaButton1.setText("机具配置");
        mechinaButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mechinaButton1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(mechinaButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                    .addComponent(startButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(endButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap(29, Short.MAX_VALUE)
                .addComponent(mechinaButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(startButton, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(endButton, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(74, 74, 74))
        );

        showAddTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(showAddTable);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(msgShanLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addComponent(msgLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 4, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(msgLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(msgShanLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(7, 7, 7)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("点钞机散张加钞", jPanel3);

        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel11.setText("扫描框：");
        jPanel8.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));
        jPanel8.add(qsaoTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, 293, -1));

        jLabel12.setText("钞箱号：");
        jPanel8.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));
        jPanel8.add(qchaoIdTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 40, 293, -1));

        jLabel13.setText("ATM号：");
        jPanel8.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, -1, 20));
        jPanel8.add(qatmIdTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 70, 293, -1));

        jLabel15.setText("单个文件张数:");
        jPanel8.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 10, 80, 20));

        com.poka.util.LimitDocument lit2q = new com.poka.util.LimitDocument(5);
        lit2q.setAllowChar("0123456789");
        countTextField.setDocument(lit2q);
        jPanel8.add(qcountTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 10, 80, -1));

        sqlserverCfgjButton.setText("数据库配置");
        sqlserverCfgjButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sqlserverCfgjButtonMouseClicked(evt);
            }
        });
        jPanel8.add(sqlserverCfgjButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 10, 100, -1));

        qingComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "辽宁聚龙清分机", "光荣清分机", "中钞信达清分机", "古鳌清分机" }));
        qingComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                qingComboBoxItemStateChanged(evt);
            }
        });
        jPanel8.add(qingComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 50, 160, -1));

        jLabel14.setText("清分机类型:");
        jPanel8.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 50, 80, 20));

        jLabel16.setText("加钞总张数:");
        jPanel8.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 80, 70, 20));

        com.poka.util.LimitDocument lit2q1 = new com.poka.util.LimitDocument(10);
        lit2q1.setAllowChar("0123456789");
        maxMoneyTextField.setDocument(lit2q);
        jPanel8.add(maxMoneyTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 80, 80, -1));

        maxToggleButton.setText("开启");
        maxToggleButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                maxToggleButtonMouseClicked(evt);
            }
        });
        jPanel8.add(maxToggleButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 80, 60, -1));

        com.poka.util.LimitDocument l1 = new com.poka.util.LimitDocument(7);
        l1.setAllowChar("0123456789");
        portQfTextField.setDocument(l1);
        jPanel8.add(portQfTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 110, 80, -1));

        portLabel1.setText("  监听端口:");
        jPanel8.add(portLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 110, 70, 20));
        jPanel8.add(qmsgShanLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 106, 352, 30));

        showAddTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane3.setViewportView(qshowAddTable);

        qstartButton.setText("开始");
        qstartButton.setMaximumSize(new java.awt.Dimension(69, 23));
        qstartButton.setMinimumSize(new java.awt.Dimension(69, 23));
        qstartButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                qstartButtonMouseClicked(evt);
            }
        });

        qendButton.setText("结束");
        qendButton.setMaximumSize(new java.awt.Dimension(69, 23));
        qendButton.setMinimumSize(new java.awt.Dimension(69, 23));
        qendButton.setPreferredSize(new java.awt.Dimension(69, 23));
        qendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                qendButtonActionPerformed(evt);
            }
        });

        newButton.setText("初始化");
        newButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                newButtonMouseClicked(evt);
            }
        });

        cancelButton.setText("取消");
        cancelButton.setMaximumSize(new java.awt.Dimension(69, 23));
        cancelButton.setMinimumSize(new java.awt.Dimension(69, 23));
        cancelButton.setPreferredSize(new java.awt.Dimension(69, 23));
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(newButton, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                    .addComponent(qstartButton, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                    .addComponent(qendButton, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                    .addComponent(cancelButton, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addComponent(newButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(qstartButton, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(qendButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 584, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 6, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(29, 29, 29))
        );

        jTabbedPane2.addTab("清分机散张加钞", jPanel7);

        logoutButton.setText("退出登录");
        logoutButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                logoutButtonMouseClicked(evt);
            }
        });

        com.poka.util.LimitDocument lit3q = new com.poka.util.LimitDocument(8);

        adderTextField.setDocument(lit3q);

        jLabel2.setText("加钞员");

        jLabel3.setText("监督员");

        com.poka.util.LimitDocument lit4q = new com.poka.util.LimitDocument(8);

        checkerTextField.setDocument(lit4q);

        openComToggleButton.setText("开启");
        openComToggleButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                openComToggleButtonMouseClicked(evt);
            }
        });

        jLabel9.setText("扫描抢COM口");

        comComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comComboBoxItemStateChanged(evt);
            }
        });

        jLabel10.setText("扫描抢编号");

        com.poka.util.LimitDocument lit3 = new com.poka.util.LimitDocument(5);
        lit3.setAllowChar("0123456789");
        localHostTextField.setDocument(lit3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 750, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(localHostTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                            .addComponent(adderTextField))
                        .addGap(32, 32, 32)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(18, 18, 18)
                                .addComponent(comComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(32, 32, 32)
                                .addComponent(openComToggleButton))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(checkerTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(logoutButton)
                                .addGap(27, 27, 27))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(comMsgLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(adderTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(checkerTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(logoutButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(comMsgLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(comComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(openComToggleButton)
                            .addComponent(jLabel10)
                            .addComponent(localHostTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 7, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 518, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void showPort(boolean flag) {
        this.portQfTextField.setVisible(flag);
        this.portLabel1.setVisible(flag);
    }

    private int getSelectlimit() {
        int idex = this.limitComboBox.getSelectedIndex();
        switch (idex) {
            case 0:
                return 1;
            case 1:
                return 5;
            case 2:
                return 10;
            case 3:
                return 15;
            case 4:
                return 20;
            case 5:
                return 30;
            case 6:
                return 60;
            default:
                return 5;
        }
    }
    private void kunTChaoButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kunTChaoButtonMouseClicked
        if (this.kunTChaoButton.isEnabled()) {
            if (this.adderTextField.getText().length() <= 0 || this.adderTextField.getText().length() > 8
                    || this.checkerTextField.getText().length() <= 0 || this.checkerTextField.getText().length() > 8) {
                JOptionPane.showMessageDialog(null, "请输入正确的加钞员和监督员！");
                this.adderTextField.selectAll();
            } else {

//                int iRet = checkUser();
//                if (iRet == -1 || iRet == 1) {
//                    JOptionPane.showMessageDialog(null, "请输入正确的加钞员和监督员！");
//                    this.adderTextField.selectAll();
//                    return;
//                } else if (iRet == 2) {
//                    JOptionPane.showMessageDialog(null, "请输入正确的监督员！");
//                    this.checkerTextField.selectAll();
//                    return;
//                }
                this.userCode = this.adderTextField.getText().trim();
                this.checkerCode = this.checkerTextField.getText().trim();
                this.inputLabel.setText("请输入11位钞箱ID,以字母C开头");
                this.adderTextField.setEditable(false);
                this.checkerTextField.setEditable(false);
                this.showTable.setModel(kunModle);
                this.createButton.setEnabled(true);
                this.chaoTATMButton.setEnabled(false);
                this.kunTChaoButton.setEnabled(false);
                this.kunTChaoButton.setForeground(Color.red);
                this.kunTatmButton.setEnabled(false);
                this.inputTextField.setEnabled(true);
                this.inputTextField.requestFocus();
                //  xml.clearATMAll();
                typeFlag = 1;
            }
        }
    }//GEN-LAST:event_kunTChaoButtonMouseClicked

    private void chaoTATMButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_chaoTATMButtonMouseClicked
        if (this.chaoTATMButton.isEnabled()) {
            if (this.adderTextField.getText().length() <= 0 || this.adderTextField.getText().length() > 8
                    || this.checkerTextField.getText().length() <= 0 || this.checkerTextField.getText().length() > 8) {
                JOptionPane.showMessageDialog(null, "请输入正确的加钞员和监督员！");
                this.adderTextField.selectAll();
            } else {
//                int iRet = checkUser();
//                if (iRet == -1 || iRet == 1) {
//                    JOptionPane.showMessageDialog(null, "请输入正确的加钞员和监督员！");
//                    this.adderTextField.selectAll();
//                    return;
//                } else if (iRet == 2) {
//                    JOptionPane.showMessageDialog(null, "请输入正确的监督员！");
//                    this.checkerTextField.selectAll();
//                    return;
//                }

                this.userCode = this.adderTextField.getText().trim();
                this.checkerCode = this.checkerTextField.getText().trim();
                this.inputLabel.setText("请输入18位ATMID,以字母A开头");
                this.adderTextField.setEditable(false);
                this.checkerTextField.setEditable(false);
                this.showTable.setModel(atmModle);
                this.createButton.setEnabled(true);
                this.chaoTATMButton.setForeground(Color.red);
                this.chaoTATMButton.setEnabled(false);
                this.kunTChaoButton.setEnabled(false);
                this.kunTatmButton.setEnabled(false);
                this.inputTextField.setEnabled(true);
                this.inputTextField.requestFocus();
                typeFlag = 2;
            }
        }
    }//GEN-LAST:event_chaoTATMButtonMouseClicked

    private void createButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_createButtonMouseClicked
        if (this.createButton.isEnabled()) {
            Thread tt = new Thread(new Runnable() {
                public void run() {
                    createBfFile();
                }
            });
            tt.start();
            errLabel.setText("");

        }
    }//GEN-LAST:event_createButtonMouseClicked

    private void createBfFile() {
        try {
            String basePath = StaticVar.cfgMap.get(argPro.localAddr);
            String temPath = basePath + File.separator + UploadFtp.tem;

            String name = BfFile.createBfFileName(StaticVar.bankId, StaticVar.agencyNo, "XXXXXX");
            switch (this.typeFlag) {
                case 0: {
                }
                case 1: {
                    if (kunFile.getCount() > 0) {
                        kunFile.writeBfFile(temPath + File.separator + name);
                        kunFile.clear();
                    } else {
                        JOptionPane.showMessageDialog(null, "没有扫描捆标签，未能生成文件！");
                        return;
                    }
                    break;
                }
                case 2: {
                    if (chaoFile.getCount() > 0) {
                        chaoFile.writeBfFile(temPath + File.separator + name);
                        chaoFile.clear();
                    } else {
                        JOptionPane.showMessageDialog(null, "没有扫描钞箱标签，未能生成文件！");
                        return;
                    }
                    break;
                }
                default:
                    break;
            }

            //将生成的pokafsn文件上传到前置机
            uploadProgressBar.setVisible(true);

            UploadFtp.oneFileUploadFtp(name, UploadFtp.bfbak, (FileUploadMonitor) uploadProgressBar);

            uploadProgressBar.setVisible(false);
            this.chaoTATMButton.setEnabled(true);
            this.kunTChaoButton.setEnabled(true);
            this.kunTatmButton.setEnabled(true);
            this.inputTextField.setEnabled(false);
            this.createButton.setEnabled(false);
            this.adderTextField.setEditable(true);
            this.checkerTextField.setEditable(true);
            this.chaoTATMButton.setForeground(Color.black);
            this.kunTatmButton.setForeground(Color.black);
            this.kunTChaoButton.setForeground(Color.black);
            //  this.showTable.removeAll();
            DefaultTableModel pokaModel = (DefaultTableModel) this.showTable.getModel();
            pokaModel.setRowCount(0);
            this.showTable.invalidate();
            this.inputTextField.setText("");
            this.inputLabel.setText("");
            JOptionPane.showMessageDialog(null, "加钞完成！");
        } catch (IOException ex) {
            //  Logger.getLogger(ATMAddMon.class.getName()).log(Level.SEVERE, null, ex);
            logger.log(Level.INFO, null, ex);
        }
        this.messLabel.setText("");
    }
    private void clearButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_clearButtonMouseClicked
        this.errLabel.setText("");
        this.inputLabel.setText("输入框:");
        typeFlag = -1;
        this.kunFile.clear();
        this.chaoFile.clear();
        this.showTable.removeAll();
        this.chaoTATMButton.setEnabled(true);
        this.kunTatmButton.setEnabled(true);
        this.kunTChaoButton.setEnabled(true);
        this.chaoTATMButton.setForeground(Color.black);
        this.kunTatmButton.setForeground(Color.black);
        this.kunTChaoButton.setForeground(Color.black);
        this.inputTextField.setText("");
        this.inputTextField.setEnabled(false);
        this.messLabel.setText("");
        this.createButton.setEnabled(false);
        errLabel.setText("");
        DefaultTableModel pokaModel = (DefaultTableModel) this.showTable.getModel();
        pokaModel.setRowCount(0);
    }//GEN-LAST:event_clearButtonMouseClicked

    private void jTabbedPane2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane2MouseClicked

    }//GEN-LAST:event_jTabbedPane2MouseClicked

    private void showTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_showTableMouseClicked
        if (evt.getButton() == MouseEvent.BUTTON3) {
            popupmenu.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_showTableMouseClicked

    private void logoutButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutButtonMouseClicked
        if (pokaMain.login) {
            if (this.createButton.isEnabled()) {
                JOptionPane.showMessageDialog(null, "请完成整捆加钞业务后再退出登录！");
            } else if (sanFlag) {
                JOptionPane.showMessageDialog(null, "请完成散张加钞业务后再退出登录！");
            } else if (this.openComToggleButton.isSelected()) {
                JOptionPane.showMessageDialog(null, "请先关闭扫描枪串口连接！");
            } else {
                pokaMain.getGuangdianMenu().setEnabled(true);
                pokaMain.dealGuanZiHaojMenu.setEnabled(true);
                pokaMain.fileChangeMenu.setEnabled(true);
                pokaMain.login = false;

                this.pokaMain.chenbo.removeAll();
                this.pokaMain.chenbo.repaint();
            }
        }
    }//GEN-LAST:event_logoutButtonMouseClicked

    private void startButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_startButtonMouseClicked
        if (this.startButton.isEnabled()) {
            String sPort = this.portTextField.getText().trim();
            String sNum = this.countTextField.getText().trim();
            if (this.adderTextField.getText().length() <= 0 || this.adderTextField.getText().length() > 8
                    || this.checkerTextField.getText().length() <= 0 || this.checkerTextField.getText().length() > 8) {
                JOptionPane.showMessageDialog(null, "请输入正确的加钞员和监督员！");
                this.adderTextField.selectAll();
                return;
            }
            if (sNum.length() <= 0) {
                JOptionPane.showMessageDialog(null, "请输入正确的文件容量！");
                this.countTextField.requestFocus();
                this.countTextField.selectAll();
                return;
            }
            if (sPort.length() <= 0) {
                JOptionPane.showMessageDialog(null, "请输入正确的监听端口号(范围：1024-49151)！");
                this.portTextField.requestFocus();
                this.portTextField.selectAll();
                return;
            }
            int port = Integer.parseInt(sPort);
            if (port < 1024 || port > 49151) {
                JOptionPane.showMessageDialog(null, "请输入正确的监听端口号(范围：1024-49151)！");
                this.portTextField.requestFocus();
                this.portTextField.selectAll();
                return;
            }

            String atmId = this.atmIdTextField.getText().trim().toUpperCase();
            if (atmId.length() <= 0) {
                JOptionPane.showMessageDialog(null, "请输入正确的18位ATM标签号！如“A12345678901234567”");
                this.saoTextField.requestFocus();
                this.saoTextField.selectAll();
                return;
            }
            if (!atmId.startsWith("A") || atmId.length() != 18) {
                JOptionPane.showMessageDialog(null, "请输入正确的18位ATM标签号！如“A12345678901234567”");
                this.saoTextField.requestFocus();
                this.saoTextField.selectAll();
                return;
            }
            String chaoId = this.chaoIdTextField.getText().trim().toUpperCase();
            if (chaoId.length() > 0) {
                if (!chaoId.startsWith("C") || chaoId.length() != 11) {
                    JOptionPane.showMessageDialog(null, "请输入正确的11位钞箱标签号！如“C1234567890”");
                    this.chaoIdTextField.requestFocus();
                    this.chaoIdTextField.selectAll();
                    return;
                }
            }

            this.portTextField.setEditable(false);
            this.atmIdTextField.setEditable(false);
            this.chaoIdTextField.setEditable(false);
            this.countTextField.setEditable(false);
            this.saoTextField.setEditable(false);
            this.startButton.setEnabled(false);
            this.endButton.setEnabled(true);
            sanFlag = true;

            OperationUser usr = new OperationUser();
            usr.setUser3(this.adderTextField.getText().trim());
            usr.setUser4(this.checkerTextField.getText().trim());

            int limit = getSelectlimit();
            //    XmlSax xml = new XmlSax();
            int iNum = Integer.parseInt(sNum);
            fServer = new FsnListenServer(port, 10);
            FsnComProperty property = new FsnComProperty(StaticVar.bankId, StaticVar.agencyNo, StaticVar.cfgMap.get(argPro.localAddr), "", this);
            property.setXmlCfg(xml);
            property.setLimit(limit);
            property.setUsr(usr);
            property.setAtmId(atmId);
            property.setBoxId(chaoId);
            property.setCount(iNum);
            property.setBusType(FsnComProperty.atmAddBusType);
//            property.setMechinaType(FsnComProperty.zhongchaoMeType);
            fServer.setListenPort(port);
            fServer.setProperty(property);
            this.msgShanLabel.setText("正在等待点钞机接入系统...");
            tt = new Thread(new Runnable() {
                public void run() {
                    fServer.startAccept();
                }
            });
            tt.setName("atmAddServer");
            tt.start();
        }
    }//GEN-LAST:event_startButtonMouseClicked

    private void endButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_endButtonMouseClicked
        if (this.endButton.isEnabled()) {
            if (fServer != null) {
//                if (!fServer.isM_stop()) {
                fServer.stop();
//                }
            }
            if (tt != null) {
                tt.interrupt();
            }
            this.portTextField.setEditable(true);
            this.atmIdTextField.setEditable(true);
            this.chaoIdTextField.setEditable(true);
            this.countTextField.setEditable(true);
            this.saoTextField.setEditable(true);
            this.startButton.setEnabled(true);
            this.endButton.setEnabled(false);
            sanFlag = false;
            msgShanLabel.setText("");
            if (this.addModle != null) {
                this.addModle.setRowCount(0);
            }
        }
    }//GEN-LAST:event_endButtonMouseClicked

    private void mechinaButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mechinaButton1MouseClicked
        if (this.isEnabled()) {
            MechinaConfigJFrame mCfg = new MechinaConfigJFrame(FsnComProperty.atmAddBusType);
            this.setEnabled(false);
            mCfg.setParent(this);
            //mCfg.setCfgType();
            mCfg.show();
        }
    }//GEN-LAST:event_mechinaButton1MouseClicked

    private void inputTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inputTextFieldActionPerformed
//    private int checkUser() {
//        String user = this.adderTextField.getText().trim();
//        String checker = this.checkerTextField.getText().trim();
//
//        BaseDao<Map> b = new BaseDao<Map>();
//        String sql = "select"
//                + " USERCODE"
//                + " from"
//                + " USERINFO"
//                + " where"
//                + " USERNAME=:user";
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("user", user);
//
//        List<Map> u = null;
//        try {
//            u = b.findBySql(sql, map);
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null, "数据库连接错误，请检查数据库配置是否正确。");
//            return -1;
//        }
//
//        for (Map map1 : u) {
//            userCode = map1.get("USERCODE").toString();
//        }
//        if (u == null || u.size() == 0) {
//            return 1;
//        }
//
//        map.clear();
//        map.put("user", checker);
//
//        u = null;
//        try {
//            u = b.findBySql(sql, map);
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null, "数据库连接错误，请检查数据库配置是否正确。");
//        }
//        for (Map map1 : u) {
//            checkerCode = map1.get("USERCODE").toString();
//        }
//        if (u == null || u.size() == 0) {
//            return 2;
//        }
//        return 0;
//    }
    String userCode = "";
    String checkerCode = "";
    private void kunTatmButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kunTatmButtonMouseClicked
        if (this.kunTatmButton.isEnabled()) {
            if (this.adderTextField.getText().length() <= 0 || this.adderTextField.getText().length() > 8
                    || this.checkerTextField.getText().length() <= 0 || this.checkerTextField.getText().length() > 8) {
                JOptionPane.showMessageDialog(null, "请输入正确的加钞员和监督员！");
                this.adderTextField.selectAll();
            } else {

//                int iRet = checkUser();
//                if (iRet == -1 || iRet == 1) {
//                    JOptionPane.showMessageDialog(null, "请输入正确的加钞员和监督员！");
//                    this.adderTextField.selectAll();
//                    return;
//                } else if (iRet == 2) {
//                    JOptionPane.showMessageDialog(null, "请输入正确的监督员！");
//                    this.checkerTextField.selectAll();
//                    return;
//                }
                this.userCode = this.adderTextField.getText().trim();
                this.checkerCode = this.checkerTextField.getText().trim();

                this.inputLabel.setText("请输入18位ATM ID,以字母A开头");
                this.adderTextField.setEditable(false);
                this.checkerTextField.setEditable(false);
                this.showTable.setModel(kunAtmModle);
                this.createButton.setEnabled(true);
                this.chaoTATMButton.setEnabled(false);
                this.kunTChaoButton.setEnabled(false);
                this.kunTatmButton.setEnabled(false);
                this.inputTextField.setEnabled(true);
                this.inputTextField.requestFocus();
                //kunTatmButton.setBackground(Color.red);
                kunTatmButton.setForeground(Color.red);
                //  kunTatmButton.validate();
                //   xml.clearATMAll();
                typeFlag = 0;
            }
        }
    }//GEN-LAST:event_kunTatmButtonMouseClicked

    private void comComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comComboBoxItemStateChanged
        if (itemFlag) {
            String com = (String) this.comComboBox.getSelectedItem();
            this.xml.setLastAddCom(com);
        }
    }//GEN-LAST:event_comComboBoxItemStateChanged

    Thread portThread = null;
    String addres = "";
    private void openComToggleButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_openComToggleButtonMouseClicked
        if (this.openComToggleButton.isSelected()) {
            addres = this.localHostTextField.getText().trim();
            if (addres.length() <= 0) {
                return;
            }
            portThread = new Thread(new Runnable() {
                public void run() {
                    try {
                        portLis = new PortListener((String) comComboBox.getSelectedItem(), addres);
                    } catch (PortInUseException | UnsupportedCommOperationException | ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchPortException ex) {
                        logger.log(Level.INFO, null, ex);
                    } catch (IOException ex) {
                        logger.log(Level.INFO, null, ex);
                    }
                }
            ;
            });
            portThread.start();
            this.openComToggleButton.setText("关闭");
        } else {
            try {
                if (portLis != null) {
                    portLis.close();
                    portLis = null;
                }
                portThread.interrupt();
                portThread.join();
                this.openComToggleButton.setText("开启");
            } catch (InterruptedException ex) {
                logger.log(Level.INFO, null, ex);
            }
        }
    }//GEN-LAST:event_openComToggleButtonMouseClicked

    private int fileCount;
    private void qstartButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_qstartButtonMouseClicked
        if (this.qstartButton.isEnabled()) {

            if (this.adderTextField.getText().length() <= 0 || this.adderTextField.getText().length() > 8
                    || this.checkerTextField.getText().length() <= 0 || this.checkerTextField.getText().length() > 8) {
                JOptionPane.showMessageDialog(null, "请输入正确的加钞员和监督员！");
                this.adderTextField.selectAll();
                return;
            }
//            String sPort = this.qportTextField.getText().trim();
            String sNum = this.qcountTextField.getText().trim();

//            int iRet = checkUser();
//            if (iRet == -1 || iRet == 1) {
//                JOptionPane.showMessageDialog(null, "请输入正确的加钞员和监督员！");
//                this.adderTextField.selectAll();
//                return;
//            } else if (iRet == 2) {
//                JOptionPane.showMessageDialog(null, "请输入正确的监督员！");
//                this.checkerTextField.selectAll();
//                return;
//            }
            if (sNum.length() <= 0) {
                JOptionPane.showMessageDialog(null, "请输入正确的文件容量！");
                this.qcountTextField.requestFocus();
                this.qcountTextField.selectAll();
                return;
            }
            this.fileCount = Integer.parseInt(sNum);

            if (this.maxToggleButton.isSelected()) {
                if (maxMoneyTextField.getText().trim().length() <= 0) {
                    JOptionPane.showMessageDialog(null, "已经启用自动结束模式，请输入加钞总张数！");
                    this.maxMoneyTextField.requestFocus();
                    this.maxMoneyTextField.selectAll();
                    return;
                }
                maxAdd = Integer.parseInt(maxMoneyTextField.getText().trim());
                curAdd = 0;
            }

//            if (sPort.length() <= 0) {
//                JOptionPane.showMessageDialog(null, "请输入正确的监听端口号(范围：1024-49151)！");
//                this.qportTextField.requestFocus();
//                this.qportTextField.selectAll();
//                return;
//            }
            if (3 == this.qingComboBox.getSelectedIndex()) {
                String sPort = this.portQfTextField.getText().trim();
                int port = Integer.parseInt(sPort);
                if (port < 1024 || port > 49151) {
                    JOptionPane.showMessageDialog(null, "请输入正确的监听端口号(范围：1024-49151)！");
                    this.portQfTextField.requestFocus();
                    this.portQfTextField.selectAll();
                    return;
                }
            }
            String atmId = this.qatmIdTextField.getText().trim().toUpperCase();
            if (atmId.length() <= 0) {
                JOptionPane.showMessageDialog(null, "请输入正确的18位ATM标签号！如“A12345678901234567”");
                this.qsaoTextField.requestFocus();
                this.qsaoTextField.selectAll();
                return;
            }
            if (!atmId.startsWith("A") || atmId.length() != 18) {
                JOptionPane.showMessageDialog(null, "请输入正确的18位ATM标签号！如“A12345678901234567”");
                this.qsaoTextField.requestFocus();
                this.qsaoTextField.selectAll();
                return;
            }
            String chaoId = this.qchaoIdTextField.getText().trim().toUpperCase();

            if (!chaoId.startsWith("C") || chaoId.length() != 11) {
                JOptionPane.showMessageDialog(null, "请输入正确的11位钞箱标签号！如“C1234567890”");
                this.qsaoTextField.requestFocus();
                this.qsaoTextField.selectAll();
                return;
            }

            pokaFsn.setBankNo(this.xml.getBankNum());
            pokaFsn.setDotNo(this.xml.getNetworkNum());
            pokaFsn.setPath(StaticVar.cfgMap.get(argPro.localAddr) + File.separator + UploadFtp.tem);

            //   this.qportTextField.setEditable(false);
            this.qatmIdTextField.setEditable(false);
            this.qchaoIdTextField.setEditable(false);
            this.qcountTextField.setEditable(false);
            this.qsaoTextField.setEditable(false);
            this.qstartButton.setEnabled(false);
            this.qendButton.setEnabled(true);

            if (1 == this.qingComboBox.getSelectedIndex()) {
                this.qingTimer.start();
            } else {
                this.julongTime.start();
            }
        }
    }//GEN-LAST:event_qstartButtonMouseClicked
    //private List<MoneyData> myList = new ArrayList<MoneyData>();
    private int maxAdd;
    private int curAdd;
    private PokaFsn pokaFsn = new PokaFsn();
    //private int count = 0;
    private boolean dbreload = false;

    private void scannerMoneyDataFile() {
        //  String monVal = "";
        String rxt = "";
        if (0 == this.qingComboBox.getSelectedIndex()) {//辽宁聚龙
            rxt = XmlSax.getInstance().getLLJLFileNameL();
        } else if (3 == this.qingComboBox.getSelectedIndex()) {//古鳌
            rxt = XmlSax.getInstance().getGuAaoFileNameL();
        } else {
            rxt = XmlSax.getInstance().getZCXDFileNameL();//中钞信达
        }
        Pattern pattern = Pattern.compile(rxt);

        File dir = new File(this.julongFilePath);
        File[] files = dir.listFiles();
        File temFile = new File(this.julongFilePath + File.separator + "temFile");
        if (!temFile.getParentFile().exists()) {
            temFile.getParentFile().mkdirs();
        }
        if (files == null || files.length == 0) {
            dir = new File(this.addFilePath);
            files = dir.listFiles();
        }
        if (files == null) {
            return;
        }
        this.julongTime.stop();
        for (File file : files) {
            if (maxToggleButton.isSelected()) {
                if (this.curAdd == this.maxAdd) {
                    break;
                }
            }
            if (file.isDirectory()) {
            } else {
                String fileName = file.getName().trim();
                Matcher matcher = pattern.matcher(fileName);
                boolean b = matcher.matches();
                if (b) {
                    temFile = new File(this.julongFilePath + File.separator + fileName + ".temFile");
                    if (file.renameTo(temFile)) {
                        temFile.renameTo(file);
                        PokaFsn temR = new PokaFsn();
                        try {
                            temR.readBaseFsnFile(file.getPath());   //读取FSN文件信息
                        } catch (IOException ex) {
                            logger.log(Level.INFO, null, ex);
                        }
                        List<PokaFsnBody> bd = temR.getbList();
                        for (PokaFsnBody mybd : bd) {
                            mybd.setUserId1("");
                            mybd.setUserId2("");
                            mybd.setUserId3(this.adderTextField.getText().trim());  //加钞员
                            mybd.setUserId4(this.checkerTextField.getText().trim()); //监督员
                            mybd.setAtmId(qatmIdTextField.getText().trim());
                            mybd.setBagId(qchaoIdTextField.getText().trim());
                            mybd.setFlag((byte) 3);

                            // monVal = "" + mybd.getValuta();
                            pokaFsn.addAndWrite(mybd, this.fileCount);
                            this.curAdd++;
                            // pokaFsn.add(mybd);

                            if (this.qingModle.getRowCount() > 1000) {
                                this.qingModle.setRowCount(0);
                            }
                            int co = this.qingModle.getColumnCount();
                            int row = this.qingModle.getRowCount();
                            String[] newRow = new String[co];
                            newRow[0] = "" + row;
                            newRow[1] = this.qatmIdTextField.getText().trim();
                            newRow[2] = this.qchaoIdTextField.getText().trim();
                            newRow[3] = mybd.getsNo().trim();
                            newRow[4] = mybd.getDate() + " " + mybd.getTime(); //(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format();
                            this.qingModle.addRow(newRow);
                            this.qshowAddTable.invalidate();
                            qmsgShanLabel.setText("当前已加钞：" + this.curAdd + " 张");
                            if (maxToggleButton.isSelected() && this.curAdd == this.maxAdd) {
                                break;
                            }
                        }
                        file.renameTo(UploadFtp.newDir(this.baseBakPath + File.separator + (new java.text.SimpleDateFormat("yyyyMMdd")).format(new Date()) + File.separator + file.getName()));
                        file.delete();

                    }

                }
            }
        }

        if (maxToggleButton.isSelected() && this.curAdd == this.maxAdd) {
            qendButton.doClick();
        } else {
            julongTime.restart();
        }

    }

    private void scannerMoneyData() {
        qingTimer.stop();
        String sql;
        StringBuilder bf = new StringBuilder();
        StringBuilder bf1 = new StringBuilder();
        bf.append("select top 100 * from MoneyData where  ");
        String[] tem = xml.getFlowMoney(XmlSax.flowMoney).split("|");
        int a = 0;
        for (String ss : tem) {
            if (ss == null || ss.equals("") || ss.equals("|")) {
                continue;
            }
            if (a > 0) {
                bf1.append(" OR ");
            }
            bf1.append(" BVResult=");
            bf1.append(ss);
            bf1.append(" ");
            a++;
        }
        bf.append(bf1.toString());
        bf.append(" order by id");
        sql = bf.toString();//"select top 100 * from MoneyData where BVResult=1 or BVResult=2 order by id";

        if (moneyBase == null || dbreload) {
            moneyBase = new BaseDaoSqlServer<Map>();
            dbreload = false;
        }

        List<Map> moneyList = moneyBase.findBySql(sql);
        int len = moneyList.size();

        BigInteger maxdelid = BigInteger.valueOf(0);
        for (int i = 0; i < len; i++) {
            HashMap m = (HashMap) moneyList.get(i);
            maxdelid = ((BigInteger) m.get("ID")).compareTo(maxdelid) > 0 ? (BigInteger) m.get("ID") : maxdelid;
            BigInteger id = (BigInteger) m.get("ID");
            Date coltime = (Date) m.get("COLTIME");
            String mon = m.get("MON") == null ? "" : m.get("MON").toString();
            String montype = m.get("MONTYPE") == null ? "" : m.get("MONTYPE").toString();
            String monval = m.get("MONVAL") == null ? "" : m.get("MONVAL").toString();
            String monver = m.get("MONVER") == null ? "" : m.get("MONVER").toString();
            String trueflag = m.get("TRUEFLAG") == null ? "" : m.get("TRUEFLAG").toString();
            String quanlity = m.get("QUANLITY") == null ? "" : m.get("QUANLITY").toString();
            String operatorid = m.get("OPERATORID") == null ? "" : m.get("OPERATORID").toString();
            String machineId = m.get("MachineId") == null ? "" : m.get("MachineId").toString();
            Date insertdatetime = (Date) m.get("INSERTDATETIME");
            MoneyData md = new MoneyData(coltime, id, insertdatetime, mon, montype, monval, monver, operatorid, quanlity, trueflag, machineId);

            if (this.qingModle.getRowCount() > 1000) {
                this.qingModle.setRowCount(0);
            }
            int co = this.qingModle.getColumnCount();
            int row = this.qingModle.getRowCount();
            String[] newRow = new String[co];
            newRow[0] = "" + row;
            newRow[1] = this.qatmIdTextField.getText().trim();
            newRow[2] = this.qchaoIdTextField.getText().trim();
            newRow[3] = mon;
            newRow[4] = (new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(coltime);
            this.qingModle.addRow(newRow);
            this.qshowAddTable.invalidate();

            PokaFsnBody p = new PokaFsnBody();
            Date pokadate = md.getColtime();
            int year = pokadate.getYear();
            String myYear = String.valueOf(year + 1900);
            String onemouth = pokadate.getMonth() + 1 < 10 ? "0" + (pokadate.getMonth() + 1) : "" + (pokadate.getMonth() + 1);
            String onedate = pokadate.getDate() < 10 ? "0" + pokadate.getDate() : "" + pokadate.getDate();
            String lastDate = myYear + onemouth + onedate;
            p.setDate(lastDate);
            String ss = String.valueOf(pokadate.getHours());
            String mm = String.valueOf(pokadate.getMinutes());
            String second = String.valueOf(pokadate.getSeconds());
            p.setTime(ss + ":" + mm + ":" + second);
            p.setTfFlag(md.getTrueFlag());
            // p.setErrorCode("");
            p.setMoneyFlag(md.getMonType());
            int moneyVer = 0;
            if (md.getMonVer().equals("2005")) {
                moneyVer = 2;
            } else if (md.getMonVer().equals("1999")) {
                moneyVer = 1;
            } else if (md.getMonVer().equals("1990")) {
                moneyVer = 0;
            } else {
                moneyVer = 9999;
            }
            p.setVer(moneyVer);
            p.setValuta(Integer.parseInt(md.getMonVal()));
            p.setCharNum(md.getMon().length());
            p.setsNo(md.getMon());

            p.setAtmId(qatmIdTextField.getText().trim());
            p.setBagId(qchaoIdTextField.getText().trim());
            p.setMacinSno(md.getMachineId().trim());
            p.setReservel("");

            p.setUserId3(this.adderTextField.getText().trim());  //加钞员
            p.setUserId4(this.checkerTextField.getText().trim()); //监督员
            //p.setmonboxID

            p.setUserId1("");
            p.setUserId2("");
            p.setFlag((byte) 3);
            pokaFsn.addAndWrite(p, this.fileCount);
            this.curAdd++;
            if (maxToggleButton.isSelected() && this.curAdd == this.maxAdd) {
                break;
            }
        }

        bf = new StringBuilder();
        bf.append("delete from MoneyData where ( ");
        bf.append(bf1.toString());
        bf.append(" ) and id<=:maxdelid");
        String delsql = bf.toString();

        Map<String, Object> myMap = new HashMap<String, Object>();
        myMap.put("maxdelid", maxdelid);

        if (moneyBase == null || dbreload) {
            moneyBase = new BaseDaoSqlServer<Map>();
            dbreload = false;
        }
        moneyBase.executeSql(delsql, myMap);

        qmsgShanLabel.setText("当前已加钞：" + this.curAdd + " 张");
        if (maxToggleButton.isSelected() && this.curAdd == this.maxAdd) {
            qendButton.doClick();
        } else {
            qingTimer.restart();
        }
    }
    private BaseDaoSqlServer<Map> moneyBase = null;
    private void newButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_newButtonMouseClicked
        if (newButton.isEnabled()) {
            Object[] options = {"确定", "取消"};
            koread_flag = false;
            int response;
            if (1 == this.qingComboBox.getSelectedIndex()) {
                response = JOptionPane.showOptionDialog(this, "初始化操作将删除表中所有数据,是否执行", "提示", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (response == 0) {
                    String delsql = "truncate table MoneyData";
                    if (moneyBase == null || dbreload) {
                        moneyBase = new BaseDaoSqlServer<Map>();
                        dbreload = false;
                    }
                    int result = moneyBase.executeSql(delsql, null);

                    JOptionPane.showMessageDialog(null, "数据库初始化完成.");

                }
            } else {
                response = JOptionPane.showOptionDialog(this, "初始化清除本地旧文件,是否执行", "提示", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (response == 0) {
                    this.pokaFsn = new PokaFsn();
                    if (this.julongFilePath != null) {
                        IOUtil.deleteDir(new File(julongFilePath));
                    }
                    if (this.addFilePath != null) {
                        File f = new File(this.addFilePath);
                        if (f.exists()) {
                            IOUtil.deleteDir(f);
                        }
                    }
                    JOptionPane.showMessageDialog(null, "初始化完成.");

                }
            }
        }
    }//GEN-LAST:event_newButtonMouseClicked


    private void sqlserverCfgjButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sqlserverCfgjButtonMouseClicked
        if (newButton.isEnabled()) {
            SqlServerCfgJDialog cfg = new SqlServerCfgJDialog(this.pokaMain, true);
            cfg.show();
            dbreload = true;
        }
    }//GEN-LAST:event_sqlserverCfgjButtonMouseClicked

    private void qingComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_qingComboBoxItemStateChanged
        if (1 == qingComboBox.getSelectedIndex()) {
            sqlserverCfgjButton.setVisible(true);
            this.qmsgShanLabel.setText("");
        } else {
            sqlserverCfgjButton.setVisible(false);
            this.qmsgShanLabel.setText("文件存放路径:" + this.julongFilePath);
        }
        if (3 == qingComboBox.getSelectedIndex()) {
            showPort(true);
        } else {
            showPort(false);
        }
    }//GEN-LAST:event_qingComboBoxItemStateChanged

    private void maxToggleButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_maxToggleButtonMouseClicked
        if (this.maxToggleButton.isSelected()) {
            this.maxToggleButton.setText("关闭");
        } else {
            this.maxToggleButton.setText("开启");
        }
    }//GEN-LAST:event_maxToggleButtonMouseClicked

    private void qendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_qendButtonActionPerformed
        if (this.qendButton.isEnabled()) {
            //this.qportTextField.setEditable(true);
            this.qatmIdTextField.setEditable(true);
            this.qchaoIdTextField.setEditable(true);
            this.qcountTextField.setEditable(true);
            this.qsaoTextField.setEditable(true);
            this.qstartButton.setEnabled(true);
            this.qendButton.setEnabled(false);
            this.qingModle.setRowCount(0);

            if (qingTimer.isRunning()) {
                qingTimer.stop();
            }
            if (this.julongTime.isRunning()) {
                this.julongTime.stop();
            }
            if (pokaFsn.getFhead().getCount() > 0) {
                pokaFsn.addAndWrite(null, pokaFsn.getFhead().getCount());
            }
            JOptionPane.showMessageDialog(null, "加钞完成！");
            koread_flag = false;
        }
    }//GEN-LAST:event_qendButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        if (this.cancelButton.isEnabled()) {
            this.qatmIdTextField.setEditable(true);
            this.qchaoIdTextField.setEditable(true);
            this.qcountTextField.setEditable(true);
            this.qsaoTextField.setEditable(true);
            this.qstartButton.setEnabled(true);
            this.qendButton.setEnabled(false);
            this.qingModle.setRowCount(0);

            if (qingTimer.isRunning()) {
                qingTimer.stop();
            }
            if (this.julongTime.isRunning()) {
                this.julongTime.stop();
            }
            if (pokaFsn.getFhead().getCount() > 0) {
                pokaFsn.deleteFile();
            }
            koread_flag = false;
            pokaFsn = new PokaFsn();

        }
    }//GEN-LAST:event_cancelButtonActionPerformed

    private int itemCount = 0;

    @Override
    public void actionPerformed(ActionEvent e) {

        String curTiem = null;
        if (e.getSource() == this.inputTextField) {
            String text = this.inputTextField.getText().toUpperCase();
            DefaultTableModel pokaModel = (DefaultTableModel) this.showTable.getModel();
            int co = this.showTable.getColumnCount();
            int row = this.showTable.getRowCount();
            if (row == 0) {
                if (this.showTable.getColumnModel().getColumnCount() > 0) {
                    this.showTable.getColumnModel().getColumn(0).setPreferredWidth(20);
                    this.showTable.getColumnModel().getColumn(1).setPreferredWidth(100);
                    this.showTable.getColumnModel().getColumn(2).setPreferredWidth(170);
                    this.showTable.getColumnModel().getColumn(3).setPreferredWidth(170);
                }
            }
            String[] newRow = new String[co];
            if (0 == typeFlag) {//捆进ATM
                if (text.startsWith("A")) {
                    if (text.length() != 18) {
                        this.errLabel.setText("请输入正确的ATM id！");
                        this.inputTextField.selectAll();
                        return;
                    }
                    if (0 == itemCount) {
                        if (0 != row) {
                            this.errLabel.setText("请输入正确的捆id");
                            this.inputTextField.selectAll();
                            return;
                        }
                    }
                    this.errLabel.setText("");
                    this.curAtmId = text.trim();
                    this.addFlag = true;
                    newRow[0] = "" + row;
                    newRow[1] = this.curAtmId;
                    newRow[2] = "";
                    pokaModel.addRow(newRow);
                    this.messLabel.setText("当前ATM id为：" + this.curAtmId);
                    this.inputLabel.setText("请扫描捆标签：");
                    itemCount = 0;
                } else {
                    if (0 == row) {
                        this.errLabel.setText("请输入正确的ATM id！");
                        this.inputTextField.selectAll();
                        return;
                    }
                    if (text.length() != 24) {
                        this.errLabel.setText("请输入正确的捆id！");
                        this.inputTextField.selectAll();
                        return;
                    }
                    if (!kunFile.repeat(this.curAtmId, text.trim())) {
                        this.errLabel.setText("重复捆id！");
                        this.inputTextField.selectAll();
                        return;
                    }
                    itemCount++;
                    this.errLabel.setText("");
                    this.curKunId = text;

                    BfBody temB = new BfBody();
                    temB.setUserId3(this.userCode);
                    temB.setUserId4(this.checkerCode);
                    temB.setAtmId(this.curAtmId);
                    temB.setBundleId(this.curKunId);
                    curTiem = (new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(BundleDeal.getDBTime());
                    temB.setAddMoneyDateTime(curTiem);
                    this.kunFile.add(temB);
                    if (this.addFlag) {
                        pokaModel.removeRow(row - 1);
                        newRow[0] = "" + (row - 1);
                        this.addFlag = false;
                    } else {
                        newRow[0] = "" + row;
                    }

                    newRow[1] = this.curAtmId;
                    newRow[2] = this.curKunId;
                    newRow[3] = curTiem;
                    pokaModel.addRow(newRow);
                    this.inputLabel.setText("请扫描捆标签或ATM标签：");
                }

            } else if (1 == typeFlag) {//捆进钞箱 
                if (text.startsWith("C")) {
                    if (text.length() != 11) {
                        this.errLabel.setText("请输入正确的钞箱id！");
                        this.inputTextField.selectAll();
                        return;
                    }

                    if (0 == itemCount) {
                        if (0 != row) {
                            this.errLabel.setText("请输入正确的捆id");
                            this.inputTextField.selectAll();
                            return;
                        }
                    }
                    itemCount = 0;
                    this.errLabel.setText("");
                    this.curCharoId = text.trim();
                    this.addFlag = true;
                    newRow[0] = "" + row;
                    newRow[1] = this.curCharoId;
                    newRow[2] = "";
                    pokaModel.addRow(newRow);
                    this.messLabel.setText("当前钞箱id为：" + this.curCharoId);
                    this.inputLabel.setText("请扫描捆标签：");
                } else {
                    if (0 == row) {
                        this.errLabel.setText("请输入正确的钞箱id！");
                        this.inputTextField.selectAll();
                        return;
                    }
                    if (text.length() != 24) {
                        this.errLabel.setText("请输入正确的捆id！");
                        this.inputTextField.selectAll();
                        return;
                    }
                    if (!kunFile.repeat(this.curCharoId, text.trim())) {
                        this.errLabel.setText("重复捆id！");
                        this.inputTextField.selectAll();
                        return;
                    }

                    this.errLabel.setText("");
                    this.curKunId = text;
                    itemCount++;

                    BfBody temB = new BfBody();
                    temB.setUserId3(this.userCode);
                    temB.setUserId4(this.checkerCode);
                    temB.setMonBoxId(this.curCharoId);
                    temB.setBundleId(this.curKunId);
                    curTiem = (new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(BundleDeal.getDBTime());
                    temB.setAddMoneyDateTime(curTiem);
                    this.kunFile.add(temB);
                    if (this.addFlag) {
                        pokaModel.removeRow(row - 1);
                        newRow[0] = "" + (row - 1);
                        this.addFlag = false;
                    } else {
                        newRow[0] = "" + row;
                    }

                    newRow[1] = this.curCharoId;
                    newRow[2] = this.curKunId;
                    newRow[3] = curTiem;
                    pokaModel.addRow(newRow);
                    this.inputLabel.setText("请扫描捆标签或钞箱标签：");
                }

            } else {
                if (text.startsWith("A")) {
                    if (text.length() != 18) {
                        this.errLabel.setText("请输入正确的ATM id！");
                        this.inputTextField.selectAll();
                        return;
                    }
                    if (0 == itemCount) {
                        if (0 != row) {
                            this.errLabel.setText("请输入正确的钞箱id");
                            this.inputTextField.selectAll();
                            return;
                        }
                    }

                    itemCount = 0;
                    this.errLabel.setText("");
                    this.curAtmId = text.trim();
                    this.addFlag = true;
                    newRow[0] = "" + row;
                    newRow[1] = this.curAtmId;
                    newRow[2] = "";
                    pokaModel.addRow(newRow);
                    this.messLabel.setText("当前ATM标签为：" + this.curAtmId);
                    this.inputLabel.setText("请扫描钞箱标签：");
                } else {
                    if (0 == row) {
                        JOptionPane.showMessageDialog(null, "请先扫描ATM标签！");
                        this.inputTextField.selectAll();
                        return;
                    }
                    if (text.length() != 11 || !text.startsWith("C")) {
                        this.errLabel.setText("请输入正确的钞箱id！");
                        this.inputTextField.selectAll();
                        return;
                    }

                    if (!chaoFile.repeat(this.curAtmId, text.trim())) {
                        this.errLabel.setText("重复钞箱id！");
                        this.inputTextField.selectAll();
                        return;
                    }
                    itemCount++;
                    this.errLabel.setText("");
                    this.curCharoId = text;
                    BfBody temB = new BfBody();
                    temB.setUserId3(this.userCode);
                    temB.setUserId4(this.checkerCode);
                    temB.setMonBoxId(this.curCharoId);
                    curTiem = (new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(BundleDeal.getDBTime());
                    temB.setAddMoneyDateTime(curTiem);
                    temB.setAtmId(this.curAtmId);
                    this.chaoFile.add(temB);
                    if (this.addFlag) {
                        pokaModel.removeRow(row - 1);
                        newRow[0] = "" + (row - 1);
                        this.addFlag = false;
                    } else {
                        newRow[0] = "" + row;
                    }
                    newRow[1] = this.curAtmId;
                    newRow[2] = this.curCharoId;
                    newRow[3] = curTiem;
                    pokaModel.addRow(newRow);
                    this.inputLabel.setText("请扫描钞箱标签或ATM标签：");
                }
            }
            //  int lastIndex = pokaModel.getDataVector().size();
            //  Rectangle rect = this.showTable.getCellRect(lastIndex - 1, lastIndex - 1, false);
            //   this.showScrollPane.getViewport().scrollRectToVisible(rect);
            this.showTable.invalidate();
            //    this.showTable.setRowSelectionInterval(lastIndex - 1, lastIndex - 1);
            this.inputTextField.setText("");
        }
        if (e.getSource() == this.saoTextField) {
            String text = this.saoTextField.getText().toUpperCase();
            if (text.startsWith("C")) {
                this.chaoIdTextField.setText(text);
                this.saoTextField.selectAll();
            } else if (text.startsWith("A")) {
                this.atmIdTextField.setText(text);
                this.saoTextField.selectAll();
            } else {
                this.msgShanLabel.setText("请输入正确的ATM标签（钞箱标签）！");
                this.saoTextField.selectAll();
            }
        }
        if (e.getSource() == this.qsaoTextField) {
            String text = this.qsaoTextField.getText().toUpperCase();
            if (text.startsWith("C")) {
                this.qchaoIdTextField.setText(text);
                this.qsaoTextField.selectAll();
            } else if (text.startsWith("A")) {
                this.qatmIdTextField.setText(text);
                this.qsaoTextField.selectAll();
            } else {
                this.qmsgShanLabel.setText("请输入正确的ATM标签（钞箱标签）！");
                this.qsaoTextField.selectAll();
            }
        }
    }

    private int lastClicked = -1;

    private BfFile kunFile = new BfFile();
    private BfFile chaoFile = new BfFile();
    private int typeFlag = -1;
    private String curKunId = null;
    private String curCharoId = null;
    private String curAtmId = null;
    private String julongFilePath = null;
    private String addFilePath = null;
    private String baseBakPath = null;
    private boolean addFlag = false;
    private boolean sanFlag = false;
    private boolean itemFlag = false;

    private JMenuItem delete = null;
    private JPopupMenu popupmenu = null;
    private DefaultTableModel kunModle = null;
    private DefaultTableModel kunAtmModle = null;
    private DefaultTableModel atmModle = null;
    private DefaultTableModel addModle = null;
    private DefaultTableModel qingModle = null;

    private PokaMainFrame pokaMain = null;
    private PortListener portLis = null;

    private FsnListenServer fServer = null;
    private Thread tt = null;
    private boolean koread_flag = false;
    private KoreanBrandExtension koreadLis = new KoreanBrandExtension();
    private XmlSax xml;
    //   private Timer clearXmlTimer;

    private Timer qingTimer;
    private Timer julongTime;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField adderTextField;
    private javax.swing.JTextField atmIdTextField;
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField chaoIdTextField;
    private javax.swing.JButton chaoTATMButton;
    private javax.swing.JTextField checkerTextField;
    private javax.swing.JButton clearButton;
    private javax.swing.JComboBox comComboBox;
    private javax.swing.JLabel comMsgLabel;
    private javax.swing.JTextField countTextField;
    private javax.swing.JButton createButton;
    private javax.swing.JButton endButton;
    private javax.swing.JLabel errLabel;
    private javax.swing.JLabel inputLabel;
    private javax.swing.JTextField inputTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
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
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JButton kunTChaoButton;
    private javax.swing.JButton kunTatmButton;
    private javax.swing.JComboBox limitComboBox;
    private javax.swing.JTextField localHostTextField;
    private javax.swing.JButton logoutButton;
    private javax.swing.JTextField maxMoneyTextField;
    private javax.swing.JToggleButton maxToggleButton;
    private javax.swing.JButton mechinaButton1;
    private javax.swing.JLabel messLabel;
    private javax.swing.JLabel msgLabel;
    private javax.swing.JLabel msgShanLabel;
    private javax.swing.JButton newButton;
    private javax.swing.JToggleButton openComToggleButton;
    private javax.swing.JLabel portLabel1;
    private javax.swing.JTextField portQfTextField;
    private javax.swing.JTextField portTextField;
    private javax.swing.JTextField qatmIdTextField;
    private javax.swing.JTextField qchaoIdTextField;
    private javax.swing.JTextField qcountTextField;
    private javax.swing.JButton qendButton;
    private javax.swing.JComboBox qingComboBox;
    private javax.swing.JLabel qmsgShanLabel;
    private javax.swing.JTextField qsaoTextField;
    private javax.swing.JTable qshowAddTable;
    private javax.swing.JButton qstartButton;
    private javax.swing.JTextField saoTextField;
    private javax.swing.JTable showAddTable;
    private javax.swing.JScrollPane showScrollPane;
    private javax.swing.JTable showTable;
    private javax.swing.JButton sqlserverCfgjButton;
    private javax.swing.JButton startButton;
    private javax.swing.JProgressBar uploadProgressBar;
    // End of variables declaration//GEN-END:variables

    @Override
    public void initLogin() {
        this.pokaMain.dealGuanZiHaojMenu.setEnabled(false);
        //  this.pokaMain.fileChangeMenu.setEnabled(false);
        this.pokaMain.getGuangdianMenu().setEnabled(false);
        this.checkerTextField.setText(this.pokaMain.getCheckUserName());
        if (this.pokaMain.getCheckUser().length() <= 0) {
            this.checkerTextField.setEditable(true);
        } else {
            this.checkerTextField.setEditable(false);
        }
        this.adderTextField.setText(this.pokaMain.getLoginUserName());
        if (this.pokaMain.getLoginUser().length() <= 0) {
            this.adderTextField.setEditable(true);
        } else {
            this.adderTextField.setEditable(false);
        }
    }

    /**
     * @param pokaMain the pokaMain to set
     */
    public void setPokaMain(PokaMainFrame pokaMain) {
        this.pokaMain = pokaMain;
    }

    @Override
    public void showMessage(PanelMsgEntity msg) {
        // "序号", "ATMID", "钞箱ID", "冠字号", "扫描时间"
        if (PanelMsgEntity.monMSGType == msg.getBusyType()) {
            if (addModle.getRowCount() > 1000) {
                addModle.setRowCount(0);
            }
            if (addModle.getRowCount() == 0) {
                this.msgShanLabel.setText("开始接收点钞机数据！");
            }
            int co = addModle.getColumnCount();
            int row = addModle.getRowCount();
            String[] newRow = new String[co];
            newRow[0] = "" + row;
            newRow[1] = this.atmIdTextField.getText().trim();
            newRow[2] = this.chaoIdTextField.getText().trim();
            newRow[3] = msg.getDataMsg();
            newRow[4] = msg.getCmdMsg();
            addModle.addRow(newRow);

            this.showAddTable.invalidate();

        } else {
            if (msg.getCmdMsg() != null) {
                this.msgShanLabel.setText(msg.getCmdMsg());
            }
        }
        //  addModle
    }

    @Override
    public void controlButton(int rows) {
        if (rows > 0) {
            this.startButton.setEnabled(true);
        } else {
            this.startButton.setEnabled(false);
        }
        this.setEnabled(true);
    }

    @Override
    public void flashMach() {

    }

    @Override
    public void flashMachClient() {
    }

}
