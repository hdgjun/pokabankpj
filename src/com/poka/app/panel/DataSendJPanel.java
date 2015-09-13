/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poka.app.panel;

import com.poka.app.util.FileUploadMonitor;
import com.poka.app.impl.LoginActionI;
import com.poka.app.frame.MechinaConfigJFrame;
import com.poka.app.impl.PanelMessageI;
import com.poka.app.frame.PokaMainFrame;
import com.poka.socket.FsnListenServer;
import com.poka.entity.Arg;
import com.poka.entity.FsnComProperty;
import com.poka.entity.MachinesCfg;
import com.poka.entity.PanelMsgEntity;
import com.poka.entity.PokaFsn;
import com.poka.entity.PokaFsnBody;
import com.poka.util.BundleDeal;
import com.poka.util.PokaSftp;
import com.poka.util.StaticVar;
import com.poka.util.UploadFtp;
import com.poka.util.XmlSax;
import com.poka.util.argPro;
import com.poka.socket.ClientTypeHandleThread;
import com.poka.util.LogManager;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author poka
 */
public class DataSendJPanel extends javax.swing.JPanel implements ActionListener, LoginActionI, PanelMessageI {

    /**
     * Creates new form DataSendJPanel
     */
    private List<MachinesCfg> machines;
    private List<MachinesCfg> machinesKewu;

    static final Logger logger = LogManager.getLogger(DataSendJPanel.class);

    public DataSendJPanel() {
        initComponents();

        if(XmlSax.getInstance().getDiaoChaoLogin().equals("1")){
            userjLabel.setVisible(false);
        }
        String sp1 = xml.getDPort(XmlSax.clientPort);
        this.portTextField.setText(sp1==null?"2222":sp1);
        
        this.numTextField.setText("1000");

         String sp2 = xml.getDPort(XmlSax.serverPort);
        this.guaoPortTextField.setText(sp2==null?"1000":sp2);
        
        this.limitComboBox.setSelectedIndex(1);
        this.guaoLimitComboBox.setSelectedIndex(1);
        fileUploadMonitor.setVisible(false);
        this.msgLabel.setForeground(Color.red);
        machines = xml.getMachines();
        this.machinesKewu = xml.getGuaoMachines();

        //this.label1.setVisible(false);              
        this.label1.setText("");
        
        this.timer.start();
        
        ipModle = new DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "ip", "类型", "状态", "张数"
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
        this.mechTable.setModel(ipModle);

        guaoIpModle = new DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "ip", "类型", "状态", "张数"
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
        this.guaoIpjTable.setModel(guaoIpModle);

        if (machines.size() <= 0) {
            this.receButton.setEnabled(false);
            this.msgLabel.setText("请配置机具信息！");
        } else {
            this.flashMach();
        }

        if (this.machinesKewu.size() <= 0) {
            this.guaoReceButton.setEnabled(false);
        } else {
            this.flashMachClient();
        }
       
        propertyGuao = new FsnComProperty(StaticVar.bankId, StaticVar.agencyNo, StaticVar.cfgMap.get(argPro.localAddr), "", this);

        guaoTimet = new Timer(15000, new ActionListener() {
            FsnComProperty property;

            public void setProperty(FsnComProperty property) {
                this.property = property;
            }

            public void actionPerformed(ActionEvent e) {
                this.property = propertyGuao;
                List<MachinesCfg> list = this.property.getXmlCfg().getGuaoMachines();
                for (MachinesCfg cfg : list) {
                    propertyGuao.setIp(cfg.getIp());
                    propertyGuao.setMechinaType(cfg.getMachineNum());
                    ClientTypeHandleThread hd = new ClientTypeHandleThread();
                    hd.setProperty(propertyGuao);
                    Thread ht = new Thread(hd);
                    ht.start();

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        logger.log(Level.INFO, null, e);
                        // Logger.getLogger(DataSendJPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
        });

        autoDealFiletimer = new Timer(5000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Thread tt = new Thread(new Runnable() {
                    public void run() {
                        if (!sending) {
                            autoDealFile(true);
                        }

                    }
                ;
                });
                  tt.start();
            }
        });

        timer.start();
    }
    private int nextId = 0;

    @Override
    public void flashMach() {
        machines = this.xml.getMachines();
        int co = this.ipModle.getColumnCount();
        ipModle.setRowCount(0);
        for (MachinesCfg cfg : machines) {
            String[] newRow = new String[co];
            newRow[0] = cfg.getIp().trim();
            newRow[1] = cfg.getMachineType().trim();
            newRow[2] = "未连接";
            newRow[3] = "" + 0;
            this.ipModle.addRow(newRow);
        }
        this.mechTable.invalidate();
        //  flasgMachClient();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        guaoPanel = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        guaoPortTextField = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        guaoMechineButton = new javax.swing.JButton();
        guaoReceButton = new javax.swing.JButton();
        guaoStopButton = new javax.swing.JButton();
        guaoLimitComboBox = new javax.swing.JComboBox();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        guaoTcpComboBox = new javax.swing.JComboBox();
        jScrollPane3 = new javax.swing.JScrollPane();
        guaoMonList = new javax.swing.JList();
        jLabel171 = new javax.swing.JLabel();
        jLabel181 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        guaoIpjTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        monList = new javax.swing.JList();
        jLabel5 = new javax.swing.JLabel();
        msgLabel = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        portTextField = new javax.swing.JTextField();
        numTextField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        mechineButton = new javax.swing.JButton();
        receButton = new javax.swing.JButton();
        stopButton = new javax.swing.JButton();
        limitComboBox = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        tcpComboBox = new javax.swing.JComboBox();
        jScrollPane5 = new javax.swing.JScrollPane();
        mechTable = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        messagejLabel = new javax.swing.JLabel();
        showList = new java.awt.List();
        label1 = new javax.swing.JLabel();
        fileUploadMonitor = new com.poka.app.util.FileUploadMonitor();
        jLabel3 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        autoDealCheckBox = new javax.swing.JCheckBox();
        allDealButton = new javax.swing.JButton();
        handButton = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        user2TextField = new javax.swing.JTextField();
        user1TextField = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        ftpComboBox = new javax.swing.JComboBox();
        logoutButton = new javax.swing.JButton();
        userjLabel = new javax.swing.JLabel();
        loginLabel = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(797, 629));
        setMinimumSize(new java.awt.Dimension(797, 629));

        guaoPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        com.poka.util.LimitDocument doc = new com.poka.util.LimitDocument(5);
        doc.setAllowChar("0123456789");
        portTextField.setDocument(doc);

        jLabel14.setText("端口号：");

        guaoMechineButton.setText("机具配置");
        guaoMechineButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                guaoMechineButtonMouseClicked(evt);
            }
        });
        guaoMechineButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guaoMechineButtonActionPerformed(evt);
            }
        });

        guaoReceButton.setText("接收数据");
        guaoReceButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                guaoReceButtonMouseClicked(evt);
            }
        });

        guaoStopButton.setText("停止");
        guaoStopButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                guaoStopButtonMouseClicked(evt);
            }
        });

        guaoLimitComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1分钟", "5分钟", "10分钟", "15分钟", "20分钟", "30分钟", "1小时" }));

        jLabel15.setText("去重时限：");

        jLabel16.setText("模式:");

        guaoTcpComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "默认", "存款", "取款" }));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(guaoPortTextField)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(guaoMechineButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(guaoReceButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(guaoTcpComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(guaoLimitComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(guaoStopButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(27, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14)
                .addGap(7, 7, 7)
                .addComponent(guaoPortTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(guaoLimitComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(guaoTcpComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                .addComponent(guaoMechineButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(guaoReceButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(guaoStopButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52))
        );

        DefaultListModel defaultListModel1  = new DefaultListModel();
        guaoMonList.setModel(defaultListModel1);
        jScrollPane3.setViewportView(guaoMonList);

        jLabel171.setText("已接收冠字号数据：");

        jLabel181.setText("设备接入信息：");

        guaoIpjTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(guaoIpjTable);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel181, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel171, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(33, 33, 33))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel171, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel181, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(3, 3, 3)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE)
                        .addComponent(jScrollPane3))
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(42, Short.MAX_VALUE))
        );

        guaoPanel.addTab("采集软件客户端模式", jPanel6);

        jLabel2.setText("设备接入信息：");

        DefaultListModel defaultListModelg1  = new DefaultListModel();
        monList.setModel(defaultListModelg1);
        jScrollPane2.setViewportView(monList);

        jLabel5.setText("已接收冠字号数据：");

        com.poka.util.LimitDocument docg = new com.poka.util.LimitDocument(5);
        docg.setAllowChar("0123456789");
        portTextField.setDocument(docg);

        com.poka.util.LimitDocument docg1 = new com.poka.util.LimitDocument(5);
        docg1.setAllowChar("0123456789");
        numTextField.setDocument(docg1);

        jLabel7.setText("文件容量：");

        jLabel6.setText("端口号：");

        mechineButton.setText("机具配置");
        mechineButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mechineButtonMouseClicked(evt);
            }
        });

        receButton.setText("接收数据");
        receButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                receButtonMouseClicked(evt);
            }
        });

        stopButton.setText("停止");
        stopButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                stopButtonMouseClicked(evt);
            }
        });

        limitComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1分钟", "5分钟", "10分钟", "15分钟", "20分钟", "30分钟", "1小时" }));

        jLabel10.setText("去重时限：");

        jLabel12.setText("模式:");

        tcpComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "默认", "存款", "取款" }));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(portTextField)
                    .addComponent(numTextField)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(mechineButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(receButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tcpComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(limitComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(stopButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(27, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(portTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(numTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(limitComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tcpComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addComponent(mechineButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(receButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(stopButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        mechTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane5.setViewportView(mechTable);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(msgLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 553, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 398, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 57, Short.MAX_VALUE))
                    .addComponent(jScrollPane5)
                    .addComponent(jScrollPane2))
                .addGap(10, 10, 10)
                .addComponent(msgLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        guaoPanel.addTab(" 采集软件服务端模式 ", jPanel2);

        jLabel1.setText("FSN文件列表");

        messagejLabel.setText("文件个数:");

        label1.setText("请勿关闭，数据正在处理中....");

        jLabel3.setText("* 在文件进行传输时，可能需要一些时间进行后台处理，请耐心等待");

        autoDealCheckBox.setText("自动处理");
        autoDealCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                autoDealCheckBoxItemStateChanged(evt);
            }
        });

        allDealButton.setText("批量传输");
        allDealButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                allDealButtonMouseClicked(evt);
            }
        });

        handButton.setText("单个传输");
        handButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                handButtonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(autoDealCheckBox)
                    .addComponent(handButton)
                    .addComponent(allDealButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(autoDealCheckBox)
                .addGap(27, 27, 27)
                .addComponent(allDealButton, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(handButton, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(43, Short.MAX_VALUE))
        );

        jLabel8.setText("操作员");

        jLabel9.setText("检查员");

        com.poka.util.LimitDocument lit1q = new com.poka.util.LimitDocument(8);

        user2TextField.setDocument(lit1q);
        user2TextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                user2TextFieldActionPerformed(evt);
            }
        });

        user1TextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                user1TextFieldActionPerformed(evt);
            }
        });

        jLabel11.setText("模式:");

        ftpComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "默认", "存款", "取款" }));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel8))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(user1TextField, javax.swing.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE)
                    .addComponent(user2TextField)
                    .addComponent(ftpComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(user1TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(user2TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(ftpComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 376, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(fileUploadMonitor, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(messagejLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(showList, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 36, Short.MAX_VALUE)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(label1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(showList, javax.swing.GroupLayout.PREFERRED_SIZE, 404, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(messagejLabel)
                .addGap(10, 10, 10)
                .addComponent(fileUploadMonitor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        guaoPanel.addTab("   FTP模式  ", jPanel1);
        jPanel1.getAccessibleContext().setAccessibleName("");

        logoutButton.setText("退出登录");
        logoutButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                logoutButtonMouseClicked(evt);
            }
        });

        userjLabel.setText("当前用户：");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(userjLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(loginLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(451, 451, 451)
                        .addComponent(logoutButton))
                    .addComponent(guaoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 777, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(logoutButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(userjLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(loginLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(guaoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 585, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private int getSelectlimit(int idex) {

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

//    private int checkUser() {
//        String user = this.user1TextField.getText().trim();
//        String checker = this.user2TextField.getText().trim();
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
    String curDate = new java.text.SimpleDateFormat("yyyyMMdd").format(new Date());

    ;

    private void autoDealFile(boolean isPrompt) {
        sending = true;
        handButton.setEnabled(false);
        int moType = this.ftpComboBox.getSelectedIndex();
        String files[] = showList.getItems();
        if (files.length == 0) {
            if (isPrompt) {
                fileUploadMonitor.setVisible(false);
                messagejLabel.setText("暂时没有需要处理的文件");
            }
            sending = false;

            return;
        }

        this.user1TextField.setEditable(false);
        this.user2TextField.setEditable(false);
        String loPath = StaticVar.cfgMap.get(argPro.localAddr);
        PokaFsn po = new PokaFsn();

        String temPath = loPath + File.separator + UploadFtp.tem;
        String baseBakPath = loPath + File.separator + UploadFtp.basebak;
        String pokaBakPath = loPath + File.separator + UploadFtp.fsnbak;
        String reUploadPath = loPath + File.separator + UploadFtp.reUpload;
        String user1 = this.userCode;
        String user2 = this.checkerCode;

        File baseBakFile = new File(baseBakPath);
        if (!baseBakFile.exists()) {
            baseBakFile.mkdirs();
        }
        File pokaBakFile = new File(pokaBakPath);
        if (!pokaBakFile.exists()) {
            pokaBakFile.mkdirs();
        }
        File temFile = new File(temPath);
        if (!temFile.exists()) {
            temFile.mkdirs();
        }

        List<String> pokaList;
        pokaList = new ArrayList();
        timer.stop();
        fileUploadMonitor.setVisible(true);
        fileUploadMonitor.setMaximum(files.length);
        fileUploadMonitor.setValue(0);
        fileUploadMonitor.setToolTipText("正在转化文件......");
        messagejLabel.setText("正在转化文件......");
        //  System.out.println("files count =" + files.length);
        for (String s : files) {
            messagejLabel.setText("正在转化文件:" + s);
            try {
                Thread.sleep(20);
            } catch (InterruptedException ex) {
                // ex.printStackTrace();
                logger.log(Level.INFO, null, ex);
                // Logger.getLogger(DataSendJPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                String fsn = loPath + File.separator + s;
                Date date = null;
                date = BundleDeal.getDBTime();

                String newPokaName = null;
                newPokaName = StaticVar.bankId + "_" + StaticVar.agencyNo + "_XXXXXXXXXXXXXX_XXXXXXX" + (new java.text.SimpleDateFormat("yyyyMMddHHmmssSSS")).format(date) + ".FSN";

                po.readBaseFsnFile(fsn);
                for (PokaFsnBody bd : po.getbList()) {
                    bd.setUserId1(user1);
                    bd.setUserId2(user2);
                    bd.setFlag((byte) moType);
                }
                //在临时文件夹中生成poka fsn文件
                po.writePokaFsnFile(temPath + File.separator + newPokaName);

                //备份标准fsn文件
                File f = new File(fsn);
                File fNew = UploadFtp.newDir(baseBakPath + File.separator + curDate + File.separator + s);
                if (fNew.exists()) {
                    fNew.delete();
                }
                if (f.renameTo(fNew)) {
                    f.delete();
                    pokaList.add(newPokaName);

                } else {
                    //备份失败，下一循环重新处理该标准fsn文件，并把已经生成的pokafsn文件删除
                    new File(temPath + File.separator + newPokaName).delete();
                    continue;
                }
            } catch (IOException ex) {
                // ex.printStackTrace();
                sending = false;
                logger.log(Level.INFO, null, ex);
                //  Logger.getLogger(PokaMain.class.getName()).log(Level.SEVERE, null, ex);
            }
            showList.remove(s);
            fileUploadMonitor.setValue(fileUploadMonitor.getValue() + 1);
        }
        System.out.println("pokaList count =" + pokaList.size());
        fileUploadMonitor.setToolTipText("文件上传中.....");
        //将生成的pokafsn文件上传到前置机
        PokaSftp sftp = new PokaSftp();
        boolean flag = false;
        int count = 0;
        int error = 0;
        sftp.connect(StaticVar.cfgMap.get(argPro.ftp), Integer.valueOf(StaticVar.cfgMap.get(argPro.port)), StaticVar.cfgMap.get(argPro.user), StaticVar.cfgMap.get(argPro.pwd), 6000);
        if (sftp.getSftp() == null) {
            for (String s : pokaList) {
                File upFile = UploadFtp.newDir(temPath + File.separator + s);
                String tempFilePath = reUploadPath + File.separator + s;
                File tempFile = UploadFtp.newDir(tempFilePath);
                if (upFile.renameTo(tempFile)) {
                    upFile.delete();
                }
            }
            this.sending = false;
            this.user1TextField.setEditable(true);
            this.user2TextField.setEditable(true);
            return;
        }
        for (String s : pokaList) {
            messagejLabel.setText("正在上传文件到前置机中:" + s.substring(s.lastIndexOf(File.separator) + 1));

            flag = UploadFtp.oneFileUploadFtp(s, UploadFtp.fsnbak, (FileUploadMonitor) fileUploadMonitor, sftp);
            if (flag) {//上传成功，备份poka fsn文件
                count++;
            } else {//上传失败，不处理poka fsn文件
                error++;
            }
        }
        sftp.disConnect();

        timer.restart();
        if (isPrompt) {
            // JOptionPane.showMessageDialog(null, "标准FSN文件处理完成！");
        }
        sending = false;
        this.user1TextField.setEditable(true);
        this.user2TextField.setEditable(true);
        fileUploadMonitor.setVisible(false);
        messagejLabel.setText("文件处理完成," + count + "个文件处理成功，" + error + "个文件处理失败!");
    }
    private void allDealButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_allDealButtonMouseClicked
        if (this.allDealButton.isEnabled()) {
            if (this.user1TextField.getText().trim().length() <= 0) {
                JOptionPane.showMessageDialog(null, "请输入操作员！");
                this.user1TextField.selectAll();
                return;
            }
            if (this.user2TextField.getText().trim().length() <= 0) {
                JOptionPane.showMessageDialog(null, "请输入检查员！");
                this.user2TextField.selectAll();
                return;
            }

//            int iRet = checkUser();
//            if (iRet == -1 || iRet == 1) {
//                JOptionPane.showMessageDialog(null, "请输入正确的操作员和检查员！");
//                this.user1TextField.selectAll();
//                return;
//            } else if (iRet == 2) {
//                JOptionPane.showMessageDialog(null, "请输入正确的检查员！");
//                this.user2TextField.selectAll();
//                return;
//            }
            fileUploadMonitor.setValue(0);
            fileUploadMonitor.setOrientation(JProgressBar.HORIZONTAL);
            fileUploadMonitor.setStringPainted(true);
            fileUploadMonitor.setBorderPainted(true);
            fileUploadMonitor.setBackground(Color.pink);
            fileUploadMonitor.setVisible(true);
            Thread tt = new Thread(new Runnable() {
                public void run() {
                    autoDealFile(true);
                    handButton.setEnabled(true);
                }
            ;
            });
            tt.start();
        }
    }//GEN-LAST:event_allDealButtonMouseClicked

    private void handButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_handButtonMouseClicked
        if (this.handButton.isEnabled()) {
            if (this.user1TextField.getText().trim().length() <= 0) {
                JOptionPane.showMessageDialog(null, "请输入操作员！");
                this.user1TextField.selectAll();
                return;
            }
            if (this.user2TextField.getText().trim().length() <= 0) {
                JOptionPane.showMessageDialog(null, "请输入检查员！");
                this.user2TextField.selectAll();
                return;
            }
//            int iRet = checkUser();
//            if (iRet == -1 || iRet == 1) {
//                JOptionPane.showMessageDialog(null, "请输入正确的操作员和检查员！");
//                this.user1TextField.selectAll();
//                return;
//            } else if (iRet == 2) {
//                JOptionPane.showMessageDialog(null, "请输入正确的检查员！");
//                this.user2TextField.selectAll();
//                return;
//            }
            sending = true;
            String files = showList.getSelectedItem();
            if (files == null) {
                JOptionPane.showMessageDialog(null, "请选择需要处理的文件");
                sending = false;
                return;
            }
            handButtonThread ru = new handButtonThread();
            ru.setFiles(files);
            Thread tt = new Thread(ru);
            tt.start();
        }
    }//GEN-LAST:event_handButtonMouseClicked

    /**
     * @return the monList
     */
    public javax.swing.JList getMonList() {
        return monList;
    }

    /**
     * @param pokaMain the pokaMain to set
     */
    public void setPokaMain(PokaMainFrame pokaMain) {
        this.pokaMain = pokaMain;
    }

    @Override
    public void initLogin() {
        

        this.loginLabel.setText(StaticVar.loginName);
        this.pokaMain.dealGuanZiHaojMenu.setEnabled(false);
        this.pokaMain.getGuangdianMenu().setEnabled(false);
        this.pokaMain.atmMenu.setEnabled(false);
        this.user1TextField.setText(this.pokaMain.getLoginUserName());
        if (this.pokaMain.getLoginUser().length() > 0) {
            this.user1TextField.setEditable(false);
        } else {
            this.user1TextField.setEditable(true);
        }
        this.user2TextField.setText(this.pokaMain.getCheckUserName());
        if (this.pokaMain.getCheckUser().length() > 0) {
            this.user2TextField.setEditable(false);
        } else {
            this.user2TextField.setEditable(true);
        }
    }

    public synchronized void guAoShowMessage(PanelMsgEntity msg) {
        DefaultListModel monModel = (DefaultListModel) (this.guaoMonList.getModel());

        if (msg.getBusyType() == PanelMsgEntity.connectMSGType) {

            String ip = msg.getIp().trim();
            int co = this.guaoIpModle.getColumnCount();
            int rows = guaoIpModle.getRowCount();
            for (int i = 0; i < rows; i++) {
                if (((String) (guaoIpModle.getValueAt(i, 0))).equals(ip)) {
                    guaoIpModle.setValueAt(PanelMsgEntity.stateString[msg.getState()], i, 2);
                    break;
                }
            }

            // ipModle.setDataVector(null, null);
            this.guaoIpjTable.invalidate();

        } else if (msg.getBusyType() == PanelMsgEntity.monMSGType) {
            if (monModel.getSize() > 500) {
                monModel.removeAllElements();
            }
            monModel.addElement(msg.getDataMsg());
            guaoMonList.invalidate();

            String ip = msg.getIp().trim();
            int rows = guaoIpModle.getRowCount();
            for (int i = 0; i < rows; i++) {
                if (((String) (guaoIpModle.getValueAt(i, 0))).equals(ip)) {
                    int va = Integer.parseInt((String) guaoIpModle.getValueAt(i, 3));
                    guaoIpModle.setValueAt("" + (++va), i, 3);
                    break;
                }
            }
            this.guaoIpjTable.invalidate();
        }
    }

    private void serverShowMessage(PanelMsgEntity msg, DefaultTableModel modle, javax.swing.JTable table, javax.swing.JList list) {
        DefaultListModel monModel = (DefaultListModel) (list.getModel());
        if (msg.getBusyType() == PanelMsgEntity.connectMSGType) {

            String ip = msg.getIp().trim();
            int co = modle.getColumnCount();
            int rows = modle.getRowCount();
            for (int i = 0; i < rows; i++) {
                if (((String) (modle.getValueAt(i, 0))).equals(ip)) {
                    modle.setValueAt(PanelMsgEntity.stateString[msg.getState()], i, 2);
                    break;
                }
            }

            // ipModle.setDataVector(null, null);
            table.invalidate();

        } else if (msg.getBusyType() == PanelMsgEntity.monMSGType) {
            if (monModel.getSize() > 50) {
                monModel.removeAllElements();
            }
            monModel.addElement(msg.getDataMsg());
            list.invalidate();

            String ip = msg.getIp().trim();
            int co = modle.getColumnCount();
            int rows = modle.getRowCount();
            for (int i = 0; i < rows; i++) {
                if (((String) (modle.getValueAt(i, 0))).equals(ip)) {
                    int va = Integer.parseInt((String) modle.getValueAt(i, 3));
                    modle.setValueAt("" + (++va), i, 3);
                    //  ipModle.setValueAt(PanelMsgEntity.stateString[msg.getState()], i, 2);
                    break;
                }
            }

            // ipModle.setDataVector(null, null);
            table.invalidate();
        }
    }

    @Override
    public synchronized void showMessage(PanelMsgEntity msg) {

        //   DefaultListModel mechinaModel = (DefaultListModel) (this.getMechinaList().getModel());
        if (msg.getLinkType() == PanelMsgEntity.serverType) {

            serverShowMessage(msg, this.ipModle, this.mechTable, this.monList);
//            DefaultListModel monModel = (DefaultListModel) (this.getMonList().getModel());
//            if (msg.getBusyType() == PanelMsgEntity.connectMSGType) {
//
//                String ip = msg.getIp().trim();
//                int co = this.ipModle.getColumnCount();
//                int rows = ipModle.getRowCount();
//                for (int i = 0; i < rows; i++) {
//                    if (((String) (ipModle.getValueAt(i, 0))).equals(ip)) {
//                        ipModle.setValueAt(PanelMsgEntity.stateString[msg.getState()], i, 2);
//                        break;
//                    }
//                }
//
//                // ipModle.setDataVector(null, null);
//                this.mechTable.invalidate();
//
//            } else if (msg.getBusyType() == PanelMsgEntity.monMSGType) {
//                if (monModel.getSize() > 500) {
//                    monModel.removeAllElements();
//                }
//                monModel.addElement(msg.getDataMsg());
//                monList.invalidate();
//
//                String ip = msg.getIp().trim();
//                int co = this.ipModle.getColumnCount();
//                int rows = ipModle.getRowCount();
//                for (int i = 0; i < rows; i++) {
//                    if (((String) (ipModle.getValueAt(i, 0))).equals(ip)) {
//                        int va = Integer.parseInt((String) ipModle.getValueAt(i, 3));
//                        ipModle.setValueAt("" + (++va), i, 3);
//                        //  ipModle.setValueAt(PanelMsgEntity.stateString[msg.getState()], i, 2);
//                        break;
//                    }
//                }
//
//                // ipModle.setDataVector(null, null);
//                this.mechTable.invalidate();
//            }
        } else {
            serverShowMessage(msg, this.guaoIpModle, this.guaoIpjTable, this.guaoMonList);
        }
    }

    @Override
    public void controlButton(int rows) {
        if (ipModle.getRowCount() > 0) {
            this.receButton.setEnabled(true);
        } else {
            this.receButton.setEnabled(false);
        }
        
        if (guaoIpModle.getRowCount() > 0) {
            this.guaoReceButton.setEnabled(true);
        }else{
            this.guaoReceButton.setEnabled(false);
        }
        this.setEnabled(true);
    }

    /**
     * @return the logoutButton
     */
    public javax.swing.JButton getLogoutButton() {
        return logoutButton;
    }

    /**
     * @return the fServer
     */
    public FsnListenServer getfServer() {
        return fServer;
    }

    @Override
    public void flashMachClient() {
        machinesKewu = this.xml.getGuaoMachines();
        int co = this.guaoIpModle.getColumnCount();
        guaoIpModle.setRowCount(0);

        for (MachinesCfg cfg : machinesKewu) {
            String[] newRow = new String[co];
            newRow[0] = cfg.getIp().trim();
            newRow[1] = cfg.getMachineType().trim();
            newRow[2] = "未连接";
            newRow[3] = "" + 0;
            this.guaoIpModle.addRow(newRow);
        }
        this.guaoIpjTable.invalidate();
    }

    public class handButtonThread implements Runnable {

        private String files;

        public void run() {
            dealSingleBoxItemSelected(files);
        }

        public void setFiles(String files) {
            this.files = files;
        }
    }

    public void dealSingleBoxItemSelected(String files) {
        this.sending = true;
        this.user1TextField.setEditable(false);
        this.user2TextField.setEditable(false);
        int moType = this.ftpComboBox.getSelectedIndex();
        messagejLabel.setText("正在处理文件：" + files);
        String loPath = StaticVar.cfgMap.get(argPro.localAddr);
        PokaFsn po = new PokaFsn();
        String temPath = loPath + File.separator + UploadFtp.tem;
        String baseBakPath = loPath + File.separator + UploadFtp.basebak;
        String pokaBakPath = loPath + File.separator + UploadFtp.fsnbak;
        String user1 = this.userCode;
        String user2 = this.checkerCode;

        String newPokaName = "";
        File baseBakFile = new File(baseBakPath);
        if (!baseBakFile.exists()) {
            baseBakFile.mkdirs();
        }
        File pokaBakFile = new File(pokaBakPath);
        if (!pokaBakFile.exists()) {
            pokaBakFile.mkdirs();
        }
        File temFile = new File(temPath);
        if (!temFile.exists()) {
            temFile.mkdirs();
        }

        //  List<String> pokaList;
        // pokaList = new ArrayList();
        timer.stop();
        String newPokaPath = null;
        try {
            String fsn = loPath + File.separator + files;
            po.readBaseFsnFile(fsn);
            for (PokaFsnBody bd : po.getbList()) {
                bd.setUserId1(user1);
                bd.setUserId2(user2);
                bd.setFlag((byte) moType);
            }
            // Date date = new Date();
            //pokafsn文件名
            newPokaName = StaticVar.bankId + "_" + StaticVar.agencyNo + "_XXXXXXXXXXXXXX_XXXXXXX" + (new java.text.SimpleDateFormat("yyyyMMddHHmmssSSS")).format(BundleDeal.getDBTime()) + ".FSN";
            newPokaPath = temPath + File.separator + newPokaName;
            po.writePokaFsnFile(newPokaPath);

            //备份标准fsn文件
            File f = new File(fsn);
            
            File fNew = UploadFtp.newDir(baseBakPath + File.separator + curDate + File.separator + files);
            if (fNew.exists()) {
                fNew.delete();
            }
            if (f.renameTo(fNew)) {
                f.delete();
                //  pokaList.add(newPokaPath);
            } else {
                //备份失败，下一循环重新处理该标准fsn文件，并把已经生成的pokafsn文件删除
                new File(newPokaPath).delete();
                timer.restart();
                sending = false;
                this.user1TextField.setEditable(true);
                this.user2TextField.setEditable(true);
                return;
            }

        } catch (IOException ex) {
            //  Logger.getLogger(PokaMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        showList.remove(files);

        messagejLabel.setText("正在上传文件：" + newPokaPath.substring(newPokaPath.lastIndexOf(File.separator) + 1));

        fileUploadMonitor.setVisible(true);
        //将生成的pokafsn文件上传到前置机
        UploadFtp.oneFileUploadFtp(newPokaName, UploadFtp.fsnbak, (FileUploadMonitor) fileUploadMonitor);

        timer.restart();
        this.label1.setVisible(false);
        //  JOptionPane.showMessageDialog(null, "标准FSN文件" + files + "处理完成！");
        fileUploadMonitor.setVisible(false);
        sending = false;
        this.user1TextField.setEditable(true);
        this.user2TextField.setEditable(true);
        messagejLabel.setText("文件" + files + "处理完成");
    }
    private void autoDealCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_autoDealCheckBoxItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            if (this.user1TextField.getText().trim().length() <= 0) {
                JOptionPane.showMessageDialog(null, "请输入操作员！");
                this.user1TextField.selectAll();
                return;
            }
            if (this.user2TextField.getText().trim().length() <= 0) {
                JOptionPane.showMessageDialog(null, "请输入检查员！");
                this.user2TextField.selectAll();
                return;
            }
//            int iRet = checkUser();
//            if (iRet == -1 || iRet == 1) {
//                JOptionPane.showMessageDialog(null, "请输入正确的操作员和检查员！");
//                this.user1TextField.selectAll();
//                return;
//            } else if (iRet == 2) {
//                JOptionPane.showMessageDialog(null, "请输入正确的检查员！");
//                this.user2TextField.selectAll();
//                return;
//            }
            this.user1TextField.setEditable(false);
            this.user2TextField.setEditable(false);
            this.allDealButton.setEnabled(false);
            this.handButton.setEnabled(false);
            this.label1.setText("当前采用的是自动处理模式");

            autoDealFiletimer.start();

        } else {
            this.user1TextField.setEditable(true);
            this.user2TextField.setEditable(true);
            this.allDealButton.setEnabled(true);
            this.handButton.setEnabled(true);
            this.label1.setText("");
            sending = false;
            autoDealFiletimer.stop();
        }
    }//GEN-LAST:event_autoDealCheckBoxItemStateChanged

    private void receButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_receButtonMouseClicked
        if (this.receButton.isEnabled()) {
            int iPort;
            int iNum;
            int moType = this.tcpComboBox.getSelectedIndex();
            String num = this.numTextField.getText();
            String port = this.portTextField.getText();

            if (port.length() <= 0) {
                JOptionPane.showMessageDialog(null, "请输入正确的监听端口号！");
                this.portTextField.requestFocus();
                this.portTextField.selectAll();
                return;
            }
//            Pattern pattern = Pattern.compile("[^0-9]*");
//            Matcher matcher = pattern.matcher(port);
//            boolean b = matcher.matches();
//            if (b) {
//                JOptionPane.showMessageDialog(null, "请输入正确的监听端口号！");
//                this.portTextField.selectAll();
//                return;
//            } else {//1024到49151
            iPort = Integer.parseInt(port);
            if (iPort < 1024 || iPort > 49151) {
                JOptionPane.showMessageDialog(null, "请输入正确的监听端口号！");
                this.portTextField.requestFocus();
                this.portTextField.selectAll();
                return;
            }
//            }

            if (num.length() <= 0) {
                JOptionPane.showMessageDialog(null, "请输入正确的文件容量！");
                this.numTextField.requestFocus();
                this.numTextField.selectAll();
                return;
            }
//            matcher = pattern.matcher(num);
//            b = matcher.matches();
//            if (b) {
//                JOptionPane.showMessageDialog(null, "请输入正确的文件容量！");
//                this.numTextField.selectAll();
//                return;
//            } else {//1024到49151
            
            xml.setDPort(XmlSax.clientPort, port);
            
            iNum = Integer.parseInt(num);
            int idex = this.limitComboBox.getSelectedIndex();
            int limit = getSelectlimit(idex);

            this.mechineButton.setEnabled(false);
            this.receButton.setEnabled(false);
            FsnComProperty property = new FsnComProperty(StaticVar.bankId, StaticVar.agencyNo, StaticVar.cfgMap.get(argPro.localAddr), "", this);
            property.setXmlCfg(xml);
            property.setLimit(limit);
            property.setBusType(FsnComProperty.comBusType);
            //  property.setMechinaType(FsnComProperty.zhongchaoMeType);
            property.setCount(iNum);
            if(moType==0)moType=5;//点钞机设置为5
            property.setMoType(moType);
            fServer = new FsnListenServer(iPort, 10);
            getfServer().setProperty(property);
            tt = new Thread(new Runnable() {
                public void run() {
                    getfServer().startAccept();
                    System.out.println("Thread stop");
                }
            });
            tt.setName("fserver");
            tt.start();
            socketStart = true;
        }
    }//GEN-LAST:event_receButtonMouseClicked

    Thread tt;
    private boolean socketStart = false;
    private void stopButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_stopButtonMouseClicked

        if (getfServer() != null) {
            getfServer().stop();
            fServer = null;
        }
//        if (clearXmlTimer.isRunning()) {
//            clearXmlTimer.stop();
//        }
        if (tt != null) {
            tt.interrupt();
        }
        if (!this.receButton.isEnabled()) {
            this.receButton.setEnabled(true);
        }
        if (!this.mechineButton.isEnabled()) {
            this.mechineButton.setEnabled(true);
        }
        ((DefaultListModel) monList.getModel()).removeAllElements();
        socketStart = false;
    }//GEN-LAST:event_stopButtonMouseClicked

    private void logoutButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutButtonMouseClicked
        if (pokaMain.login) {
            if (socketStart) {
                JOptionPane.showMessageDialog(null, "正在接收点钞机数据，请先关闭与点钞机的通讯！");
                return;
            }
            if (sending) {
                JOptionPane.showMessageDialog(null, "正在处理文件，请稍后！");
                return;
            }
            pokaMain.dealGuanZiHaojMenu.setEnabled(true);
            pokaMain.getGuangdianMenu().setEnabled(true);
            this.pokaMain.atmMenu.setEnabled(true);
            pokaMain.login = false;
            if (this.timer.isRunning()) {
                this.timer.stop();
            }
            if (this.autoDealFiletimer.isRunning()) {
                this.autoDealFiletimer.stop();
            }
//            if (this.clearXmlTimer.isRunning()) {
//                this.clearXmlTimer.stop();
//            }
            this.loginLabel.setText("");
            this.stopButton.doClick();
            this.pokaMain.chenbo.removeAll();
            this.pokaMain.chenbo.repaint();
        }
    }//GEN-LAST:event_logoutButtonMouseClicked

    private void mechineButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mechineButtonMouseClicked
        if (this.isEnabled()) {
            MechinaConfigJFrame mCfg = new MechinaConfigJFrame(FsnComProperty.comBusType);
            this.setEnabled(false);
            mCfg.setParent(this);
            //mCfg.setCfgType();
            mCfg.show();
        }
    }//GEN-LAST:event_mechineButtonMouseClicked

    private void user2TextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_user2TextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_user2TextFieldActionPerformed

    private void user1TextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_user1TextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_user1TextFieldActionPerformed

    private void guaoMechineButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_guaoMechineButtonMouseClicked
        if (this.isEnabled()) {
            MechinaConfigJFrame mCfg = new MechinaConfigJFrame(FsnComProperty.guaoBusType);
            this.setEnabled(false);
            mCfg.setParent(this);
            //mCfg.setCfgType();
            mCfg.show();
        }
    }//GEN-LAST:event_guaoMechineButtonMouseClicked

    private void guaoReceButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_guaoReceButtonMouseClicked
        if (this.guaoReceButton.isEnabled()) {
            List<MachinesCfg> machines = this.xml.getGuaoMachines();
            if (machines.size() == 0) {
                JOptionPane.showMessageDialog(null, "请配置机具信息！");
                return;
            }
            int iPort;
            int iNum;
            int moType = this.guaoTcpComboBox.getSelectedIndex();
            //String num = this.guaoNumTextField.getText();
            String port = this.guaoPortTextField.getText();

            if (port.length() <= 0) {
                JOptionPane.showMessageDialog(null, "请输入正确的监听端口号！");
                this.guaoPortTextField.requestFocus();
                this.guaoPortTextField.selectAll();
                return;
            }
            iPort = Integer.parseInt(port);
//            if (iPort < 1024 || iPort > 49151) {
//                JOptionPane.showMessageDialog(null, "请输入正确的监听端口号！");
//                this.guaoPortTextField.requestFocus();
//                this.guaoPortTextField.selectAll();
//                return;
//            }
//            }

//            if (num.length() <= 0) {
//                JOptionPane.showMessageDialog(null, "请输入正确的文件容量！");
//                this.guaoNumTextField.requestFocus();
//                this.guaoNumTextField.selectAll();
//                return;
//            }
            // iNum = Integer.parseInt(num);
            xml.setDPort(XmlSax.serverPort, port);
            this.guaoReceButton.setEnabled(false);
            int idex = this.guaoLimitComboBox.getSelectedIndex();
            int limit = getSelectlimit(idex);

            propertyGuao.setXmlCfg(xml);
            propertyGuao.setLimit(limit);
            propertyGuao.setBusType(FsnComProperty.guaoBusType);

            // propertyGuao.setCount(iNum);
            if(moType==0)moType=5;//点钞机设置为5
            propertyGuao.setMoType(moType);
            propertyGuao.setPort(iPort);

            this.guaoTimet.start();
        }
    }//GEN-LAST:event_guaoReceButtonMouseClicked
 
    FsnComProperty propertyGuao = null;


    private void guaoStopButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_guaoStopButtonMouseClicked
        this.guaoTimet.stop();
        this.guaoReceButton.setEnabled(true);
        //flasgMachClient();
        flashMachClient();
        ((DefaultListModel) guaoMonList.getModel()).removeAllElements();
    }//GEN-LAST:event_guaoStopButtonMouseClicked

    private void guaoMechineButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guaoMechineButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_guaoMechineButtonActionPerformed

    public synchronized void startScanFile() {
        String path = StaticVar.cfgMap.get(argPro.localAddr);
        refreshFile(path);
    }

    public synchronized void refreshFile(String path) {
        File dir = new File(path.trim());
        File[] files = dir.listFiles();
        File temFile = new File(path + File.separator + UploadFtp.tem + File.separator + "temFile");
        if (!temFile.getParentFile().exists()) {
            temFile.getParentFile().mkdirs();
        }
        if (files == null) {
            return;
        }
        if (this.showList.getItemCount() > 0) {
            this.showList.removeAll();
        }

        int count = 0;
        for (File file : files) {
            if (this.showList.getItemCount() > 100) {
                break;
            }
            if (file.isDirectory()) {
            } else {
                String fileName = file.getName();
                if (fileName.endsWith(".FSN")) {
                    temFile = new File(path + File.separator + UploadFtp.tem + File.separator + fileName + ".temFile");
                    if (file.renameTo(temFile)) {
                        temFile.renameTo(file);
                        this.showList.add(fileName);
                        count++;
                        messagejLabel.setText("待处理文件个数：" + this.showList.getItemCount());
                        //  System.out.println("this.showList:"+this.showList.getItemCount()+" count:"+count);
                    } else {
                        System.out.println("文件" + fileName + "上传中！");
                    }
                }
            }
        }
        // this.fileSizeLabel.setText("" + count);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        Thread tt = new Thread(new Runnable() {
            public void run() {
                startScanFile();
            }
        ;
        });
                  tt.start();
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton allDealButton;
    private javax.swing.JCheckBox autoDealCheckBox;
    private javax.swing.JProgressBar fileUploadMonitor;
    private javax.swing.JComboBox ftpComboBox;
    private javax.swing.JTable guaoIpjTable;
    private javax.swing.JComboBox guaoLimitComboBox;
    private javax.swing.JButton guaoMechineButton;
    private javax.swing.JList guaoMonList;
    private javax.swing.JTabbedPane guaoPanel;
    private javax.swing.JTextField guaoPortTextField;
    public javax.swing.JButton guaoReceButton;
    private javax.swing.JButton guaoStopButton;
    private javax.swing.JComboBox guaoTcpComboBox;
    private javax.swing.JButton handButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel171;
    private javax.swing.JLabel jLabel181;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JLabel label1;
    private javax.swing.JComboBox limitComboBox;
    public javax.swing.JLabel loginLabel;
    private javax.swing.JButton logoutButton;
    private javax.swing.JTable mechTable;
    private javax.swing.JButton mechineButton;
    private javax.swing.JLabel messagejLabel;
    private javax.swing.JList monList;
    private javax.swing.JLabel msgLabel;
    private javax.swing.JTextField numTextField;
    private javax.swing.JTextField portTextField;
    public javax.swing.JButton receButton;
    private java.awt.List showList;
    private javax.swing.JButton stopButton;
    private javax.swing.JComboBox tcpComboBox;
    private javax.swing.JTextField user1TextField;
    private javax.swing.JTextField user2TextField;
    private javax.swing.JLabel userjLabel;
    // End of variables declaration//GEN-END:variables
    public Timer timer = new Timer(20000, this);
    public Timer autoDealFiletimer;
    

    private DefaultTableModel ipModle = null;
    private DefaultTableModel guaoIpModle = null;
    public Timer guaoTimet;
    private boolean loginFlag = false;
    private XmlSax xml = XmlSax.getInstance();
    private FsnListenServer fServer;
    private PokaMainFrame pokaMain;
    private boolean sending = false;

}
