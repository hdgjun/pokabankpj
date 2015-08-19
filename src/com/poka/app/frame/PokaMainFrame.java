package com.poka.app.frame;

import com.poka.app.impl.LoginActionI;
import com.poka.app.panel.ATMAddMonJPanel;
import com.poka.app.panel.AboutJPanel;
import com.poka.app.panel.ConfigJPanel;
import com.poka.app.panel.DataSendJPanel;
import com.poka.app.panel.GetMoneyJPanel;
import com.poka.app.panel.GuanZiHaoDealJPanel;
import com.poka.app.panel.GuangDianQFJJPanel;
import com.poka.app.panel.ReUpLoadJPanel;
import com.poka.app.panel.ReprintJPanel;
import com.poka.app.panel.ShowJPanel;
import com.poka.app.panel.SimpleWebBrowserPanel;
import com.poka.util.LogManager;
import com.poka.util.PokaSftp;
import com.poka.util.StaticVar;
import com.poka.util.UploadFtp;
import com.poka.util.XmlSax;
import com.poka.util.argPro;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author poka
 */
public final class PokaMainFrame extends javax.swing.JFrame implements ActionListener {

    private static PokaMainFrame mainFrame;
    static final Logger logger = LogManager.getLogger(PokaMainFrame.class);
    //  private boolean showBrower = false;

    public static PokaMainFrame instance() {
        if (mainFrame == null) {
            mainFrame = new PokaMainFrame();
        }
        return mainFrame;
    }

    /**
     * Creates new form PokaMain
     */
    private PokaMainFrame() {
        initComponents();
        logger.log(Level.INFO, "PokaMain");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        XmlSax.getInstance().getSoftTitle();
        this.setTitle(StaticVar.showSoftName.trim());
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.pack();
        oneLogin = false;
        GuangdianMenu.setVisible(false);

        this.needLogin = xml.isNeedLogin();

        File upFileTem = new File(System.getProperty("user.dir")+File.separator+"PokaUpdate.exe.tem");
        if(upFileTem.exists()){
            File upFile = new File(System.getProperty("user.dir")+File.separator+"PokaUpdate.exe");
            if(upFile.exists()){
                upFile.delete();
            }
            upFileTem.renameTo(upFile);
        }
        String temPath = (String) StaticVar.cfgMap.get(argPro.localAddr);
        xml.getBankInfo();
        StaticVar.bankId = xml.getBankNum();
        StaticVar.agencyNo = xml.getNetworkNum();
        if (temPath != null) {
            UploadFtp.newDir(temPath + File.separator + UploadFtp.tem + File.separator + "TEM");
            cleanFile();
        }
        clearXmlTimer = new Timer(10000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Thread tt = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (null != xml) {
                            xml.clearMon();
                        }
                    }
                });
              tt.start();
            }
        });
        clearXmlTimer.start();

        UploadFtp.createDir(StaticVar.cfgMap.get(argPro.localAddr));

        Thread tt = new Thread(new Runnable() {
            @Override
            public void run() {

                File dir = new File(((String) StaticVar.cfgMap.get(argPro.localAddr)) + File.separator + UploadFtp.reUpload);
                File[] files = dir.listFiles();
                if (files == null) {
                    return;
                }
                PokaSftp sftp = new PokaSftp();
                boolean flag = false;
                if(StaticVar.cfgMap.get(argPro.port)==null){
                    return;
                }
                sftp.connect(StaticVar.cfgMap.get(argPro.ftp), Integer.valueOf(StaticVar.cfgMap.get(argPro.port)), StaticVar.cfgMap.get(argPro.user), StaticVar.cfgMap.get(argPro.pwd), 6000);
                if (sftp.getSftp() == null) {
                    return;
                }
                String fileType = "";
                for (File f : files) {
                    String file = f.getName().trim();
                    if (file.endsWith(".FSN")) {
                        fileType = UploadFtp.fsnbak;
                    } else if (file.endsWith(".BK")) {
                        fileType = UploadFtp.bkbak;
                    } else if (file.endsWith(".CT")) {
                        fileType = UploadFtp.ctbak;
                    } else if (file.endsWith(".BF")) {
                        fileType = UploadFtp.bfbak;
                    }
                    flag = UploadFtp.againFileUploadFtp(file, fileType, sftp);
                    if (flag) {//上传成功，备份poka fsn文件

                    } else {//上传失败，不处理poka fsn文件

                    }
                }
                sftp.disConnect();
            }
        });
                  tt.start();
    }

    public void cleanFile() {
        String basePath = (String) StaticVar.cfgMap.get(argPro.localAddr);
        String baseFsnPath = basePath + File.separator + UploadFtp.basebak;
        String pokaFsnPath = basePath + File.separator + UploadFtp.fsnbak;
        String bkPath = basePath + File.separator + UploadFtp.bkbak;
        String bfPath = basePath + File.separator + UploadFtp.bfbak;
        String ctPath = basePath + File.separator + UploadFtp.ctbak;
        String logPath = System.getProperty("user.dir") + File.separator + "log";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, -30);
        Date dt1 = cal.getTime();
        String reStr = sdf.format(dt1);
        deleteFile(baseFsnPath, reStr);
        deleteFile(pokaFsnPath, reStr);
        deleteFile(bkPath, reStr);
        deleteFile(bfPath, reStr);
        deleteFile(ctPath, reStr);
        deleteFile(logPath, reStr);
    }

    public void deleteFile(String path, String date) {
        File dir = new File(path.trim());
        File[] files = dir.listFiles();

        if (files == null || files.length <= 0) {
            return;
        }
        int a;
        for (File file : files) {
            if (file.isDirectory()) {
                try {
                    a = Integer.parseInt(file.getName().trim());
                    if (a <= Integer.parseInt(date)) {
                        deleteDir(file);
                        file.delete();
                    }
                } catch (NumberFormatException e) {
                    deleteDir(file);
                    file.delete();
                }
            } else {
                file.delete();
            }
        }
    }

    public void deleteDir(File dir) {
        File[] files = dir.listFiles();

        for (File file : files) {
            if (file.isDirectory()) {
                deleteDir(file);
            } else {
                file.delete();
            }
        }
    }

    public void addPanel(JPanel pan) {
        if (!pan.getClass().equals(showJPane.getClass())) {
            showJPane.removeall();
        }
        Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包    
        Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸   
        double screenWidth = screenSize.width; // 获取屏幕的宽   
        double screenHeight = screenSize.height; // 获取屏幕的高    

        chenbo.removeAll();
        javax.swing.GroupLayout chenboLayout = new javax.swing.GroupLayout(chenbo);
        chenbo.setLayout(chenboLayout);

        int tem = (int) pan.getPreferredSize().getWidth();
        GroupLayout.SequentialGroup hGroup = chenboLayout.createSequentialGroup();
        if (screenWidth > pan.getPreferredSize().getWidth()) {
            hGroup.addGap((int) ((screenWidth - pan.getPreferredSize().getWidth()) / 2));
            pan.setSize(pan.getPreferredSize());
        } else {
            pan.setMaximumSize(this.getPreferredSize());
            pan.setSize(screenSize);
        }
        hGroup.addComponent(pan);
        chenboLayout.setHorizontalGroup(hGroup);
        GroupLayout.SequentialGroup vGroup = chenboLayout.createSequentialGroup();
        vGroup.addGap(15);
        vGroup.addComponent(pan);
        chenboLayout.setVerticalGroup(vGroup);
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
        chenbo = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        fileChangeMenu = new javax.swing.JMenu();
        dealGuanZiHaojMenu = new javax.swing.JMenu();
        GuangdianMenu = new javax.swing.JMenu();
        atmMenu = new javax.swing.JMenu();
        reUpMenu = new javax.swing.JMenu();
        searchDataMenu = new javax.swing.JMenu();
        browerjMenu = new javax.swing.JMenu();
        getMoneyMenu = new javax.swing.JMenu();
        reprintMenu = new javax.swing.JMenu();
        argCfgMenu = new javax.swing.JMenu();
        aboutMenu = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        javax.swing.GroupLayout chenboLayout = new javax.swing.GroupLayout(chenbo);
        chenbo.setLayout(chenboLayout);
        chenboLayout.setHorizontalGroup(
            chenboLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 835, Short.MAX_VALUE)
        );
        chenboLayout.setVerticalGroup(
            chenboLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 380, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(chenbo);

        jMenuBar1.setMargin(new java.awt.Insets(5, 0, 5, 0));
        jMenuBar1.setMaximumSize(new java.awt.Dimension(132, 32769));
        jMenuBar1.setName(""); // NOI18N

        fileChangeMenu.setText(" 点钞机");
        fileChangeMenu.setAutoscrolls(true);
        fileChangeMenu.setMargin(new java.awt.Insets(5, 5, 5, 5));
        fileChangeMenu.setMaximumSize(new java.awt.Dimension(61, 32767));
        fileChangeMenu.getPopupMenu().setPopupSize(10, 10);
        fileChangeMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fileChangeMenuMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                fileChangeMenuMouseEntered(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                fileChangeMenuMousePressed(evt);
            }
        });
        fileChangeMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileChangeMenuActionPerformed(evt);
            }
        });
        jMenuBar1.add(fileChangeMenu);

        dealGuanZiHaojMenu.setText("清分机");
        dealGuanZiHaojMenu.setMargin(new java.awt.Insets(5, 5, 5, 5));
        dealGuanZiHaojMenu.setMaximumSize(new java.awt.Dimension(61, 32767));
        dealGuanZiHaojMenu.setPreferredSize(new java.awt.Dimension(61, 25));
        dealGuanZiHaojMenu.getPopupMenu().setPopupSize(20,20);
        dealGuanZiHaojMenu.getPopupMenu().setVisible(false);
        dealGuanZiHaojMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dealGuanZiHaojMenuMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                dealGuanZiHaojMenuMouseEntered(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                dealGuanZiHaojMenuMousePressed(evt);
            }
        });
        jMenuBar1.add(dealGuanZiHaojMenu);

        GuangdianMenu.setText("广电清分机");
        GuangdianMenu.setMargin(new java.awt.Insets(5, 5, 5, 5));
        GuangdianMenu.setMaximumSize(new java.awt.Dimension(80, 32767));
        GuangdianMenu.getPopupMenu().setPopupSize(20,20);
        GuangdianMenu.getPopupMenu().setVisible(false);
        GuangdianMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                GuangdianMenuMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                GuangdianMenuMouseEntered(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                GuangdianMenuMousePressed(evt);
            }
        });
        jMenuBar1.add(GuangdianMenu);

        atmMenu.setText("ATM加钞");
        atmMenu.setMargin(new java.awt.Insets(5, 5, 5, 5));
        atmMenu.getPopupMenu().setPopupSize(20,20);
        atmMenu.getPopupMenu().setVisible(false);
        atmMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                atmMenuMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                atmMenuMouseEntered(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                atmMenuMousePressed(evt);
            }
        });
        jMenuBar1.add(atmMenu);

        reUpMenu.setText("补传文件");
        reUpMenu.setMargin(new java.awt.Insets(5, 5, 5, 5));
        reUpMenu.setMaximumSize(new java.awt.Dimension(61, 32767));
        reUpMenu.setPreferredSize(new java.awt.Dimension(61, 25));
        reUpMenu.getPopupMenu().setPopupSize(20,20);
        reUpMenu.getPopupMenu().setVisible(false);
        reUpMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                reUpMenuMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                reUpMenuMouseEntered(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                reUpMenuMousePressed(evt);
            }
        });
        jMenuBar1.add(reUpMenu);

        searchDataMenu.setText("FSN查看");
        searchDataMenu.setMargin(new java.awt.Insets(5, 5, 5, 5));
        searchDataMenu.setMaximumSize(new java.awt.Dimension(61, 32767));
        searchDataMenu.setPreferredSize(new java.awt.Dimension(61, 25));
        searchDataMenu.getPopupMenu().setPopupSize(20,20);
        searchDataMenu.getPopupMenu().setVisible(false);
        searchDataMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchDataMenuMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                searchDataMenuMouseEntered(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                searchDataMenuMousePressed(evt);
            }
        });
        jMenuBar1.add(searchDataMenu);

        browerjMenu.setText("冠字号查询");
        browerjMenu.getPopupMenu().setPopupSize(20,20);
        browerjMenu.getPopupMenu().setVisible(false);
        browerjMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                browerjMenuMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                browerjMenuMouseEntered(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                browerjMenuMousePressed(evt);
            }
        });
        jMenuBar1.add(browerjMenu);

        getMoneyMenu.setText("整捆取款");
        getMoneyMenu.setMargin(new java.awt.Insets(5, 5, 5, 5));
        getMoneyMenu.setMaximumSize(new java.awt.Dimension(61, 32767));
        getMoneyMenu.setPreferredSize(new java.awt.Dimension(61, 25));
        getMoneyMenu.getPopupMenu().setPopupSize(20,20);
        getMoneyMenu.getPopupMenu().setVisible(false);
        getMoneyMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                getMoneyMenuMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                getMoneyMenuMouseEntered(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                getMoneyMenuMousePressed(evt);
            }
        });
        jMenuBar1.add(getMoneyMenu);

        reprintMenu.setText("重打标签");
        reprintMenu.getPopupMenu().setPopupSize(20,20);
        reprintMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                reprintMenuMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                reprintMenuMouseEntered(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                reprintMenuMousePressed(evt);
            }
        });
        jMenuBar1.add(reprintMenu);

        argCfgMenu.setText("参数设置");
        argCfgMenu.setMargin(new java.awt.Insets(5, 5, 5, 5));
        argCfgMenu.setMaximumSize(new java.awt.Dimension(61, 32767));
        argCfgMenu.setPreferredSize(new java.awt.Dimension(61, 25));
        argCfgMenu.getPopupMenu().setPopupSize(10, 10);
        argCfgMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                argCfgMenuMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                argCfgMenuMouseEntered(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                argCfgMenuMousePressed(evt);
            }
        });
        jMenuBar1.add(argCfgMenu);

        aboutMenu.setText("关于");
        aboutMenu.setMargin(new java.awt.Insets(5, 5, 5, 5));
        aboutMenu.setMaximumSize(new java.awt.Dimension(50, 32767));
        aboutMenu.setPreferredSize(new java.awt.Dimension(50, 25));
        aboutMenu.getPopupMenu().setPopupSize(10, 10);
        aboutMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                aboutMenuMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                aboutMenuMouseEntered(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                aboutMenuMousePressed(evt);
            }
        });
        jMenuBar1.add(aboutMenu);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    /**
     * 文件传输
     *
     * @param evt
     */
    private void fileChangeMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fileChangeMenuMouseClicked
//        if (fileChangeMenu.isEnabled() && !login) {
//            if (getDataSendJPanel() == null) {
//                setDataSendJPanel(new DataSendJPanel());
//                getDataSendJPanel().setPokaMain(this);
//
//            }
//            this.setCurPanel(getDataSendJPanel());
//            if (this.xml.getDiaoChaoLogin().equals("1")||this.oneLogin && this.isNeedLogin()) {
//                login = true;
//                this.setLoginUser("");
//                this.setCheckUser("");
//                this.addPanel(getDataSendJPanel());
//                this.getCurPanel().initLogin();
//                return;
//            }
//
//            TwoLoginFrame t = TwoLoginFrame.gettwoLoginFrame();
//            t.getUserA().setText("");
//            t.getUserB().setText("");
//            t.getPwdA().setText("");
//            t.getPwdB().setText("");
//            t.setLocationRelativeTo(null);
//            t.setPokaMain(this);
//            t.show();
//            this.setEnabled(false);
//        } else if (fileChangeMenu.isEnabled() && login) {
//            showDataSendJPanelPage();
//        }
//        this.showBrower = false;
        //StaticVar.TwoLoginFrameIdent="0";
        if (dataSendFram == null) {
            setDataSendFram(new DataSendFrame());
            this.dataSendFram.setPokaMain(this);
            this.dataSendFram.show();
        } else {
          //  this.dataSendFram.setExtendedState(JFrame.MAXIMIZED_BOTH);
            this.dataSendFram.setExtendedState(JFrame.NORMAL);
            this.dataSendFram.toFront();
        }
    }//GEN-LAST:event_fileChangeMenuMouseClicked
    public void showDataSendJPanelPage() {
        addPanel(getDataSendJPanel());
    }
    private void argCfgMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_argCfgMenuMouseClicked
//        this.showBrower = false;
        if (argCfgMenu.isEnabled()) {
            if (cfg == null) {
                cfg = new ConfigJPanel();
                cfg.setPokaMain(this);
            }
            if (this.isNeedLogin()) {
                cfg.loginLoadjComboBox.setSelectedIndex(0);//登陆一次
            } else {
                cfg.loginLoadjComboBox.setSelectedIndex(1);//每次登陆
            }
            addPanel(cfg);
        }
    }//GEN-LAST:event_argCfgMenuMouseClicked

    private void searchDataMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchDataMenuMouseClicked
//        this.showBrower = false;
        if (searchDataMenu.isEnabled()) {
            addPanel(showJPane);
        }
    }//GEN-LAST:event_searchDataMenuMouseClicked
    /**
     * 冠字号处理操作 将数据库里的数据显示出来生成FSN文件
     *
     * @param evt
     */
    private void dealGuanZiHaojMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dealGuanZiHaojMenuMouseClicked
//        this.showBrower = false;
        if (dealGuanZiHaojMenu.isEnabled() && !this.login) {
            if (guanZiHaoJPanel == null) {
                guanZiHaoJPanel = new GuanZiHaoDealJPanel();
                guanZiHaoJPanel.setPokaMain(this);
                //  this.setCurPanel(guanZiHaoJPanel);
            }
            this.setCurPanel(guanZiHaoJPanel);
            if (this.xml.getDiaoChaoLogin().equals("1") || this.oneLogin && this.isNeedLogin()) {
                login = true;
                this.setLoginUser("");
                this.setCheckUser("");
                this.addPanel(guanZiHaoJPanel);
                this.guanZiHaoJPanel.initLogin();
                return;
            }
            TwoLoginFrame t = TwoLoginFrame.gettwoLoginFrame();
            t.getUserA().setText("");
            t.getUserB().setText("");
            t.getPwdA().setText("");
            t.getPwdB().setText("");
            t.setLocationRelativeTo(null);
            t.setPokaMain(this);
            t.show();
            this.setEnabled(false);
        } else if (dealGuanZiHaojMenu.isEnabled() && this.login) {
            this.addPanel(guanZiHaoJPanel);
        }
    }//GEN-LAST:event_dealGuanZiHaojMenuMouseClicked

    private void atmMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_atmMenuMouseClicked
//        this.showBrower = false;
        if (this.atmMenu.isEnabled() && !login) {
            if (atmPanel == null) {
                atmPanel = new ATMAddMonJPanel();
                atmPanel.setPokaMain(this);
            }
            this.setCurPanel(atmPanel);
            if (this.xml.getDiaoChaoLogin().equals("1") || this.oneLogin && this.isNeedLogin()) {
                login = true;
                this.setLoginUser("");
                this.setCheckUser("");
                this.addPanel(atmPanel);
                this.atmPanel.initLogin();
                return;
            }
            TwoLoginFrame t = TwoLoginFrame.gettwoLoginFrame();
            t.getUserA().setText("");
            t.getUserB().setText("");
            t.getPwdA().setText("");
            t.getPwdB().setText("");
            t.setLocationRelativeTo(null);
            t.setPokaMain(this);
            t.show();
            this.setEnabled(false);
        } else if (atmMenu.isEnabled() && login) {
            this.addPanel(atmPanel);
        }
    }//GEN-LAST:event_atmMenuMouseClicked

    private void reUpMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reUpMenuMouseClicked
//        this.showBrower = false;
        if (this.reupload == null) {
            reupload = new ReUpLoadJPanel();
        }
        String path = StaticVar.cfgMap.get(argPro.localAddr) + File.separator + UploadFtp.reUpload;
        this.reupload.refreshFile(path);
        addPanel(this.reupload);
    }//GEN-LAST:event_reUpMenuMouseClicked

    private void getMoneyMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_getMoneyMenuMouseClicked
//        this.showBrower = false;
        //System.out.println("getMoneyMenuMouseClicked");
        if (this.getMoney == null) {
            this.getMoney = new GetMoneyJPanel();
        }
        this.addPanel(this.getMoney);
    }//GEN-LAST:event_getMoneyMenuMouseClicked

    private void GuangdianMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_GuangdianMenuMouseClicked
//        this.showBrower = false;
        if (getGuangdianMenu().isEnabled() && !this.login) {
            if (guangdian == null) {
                guangdian = new GuangDianQFJJPanel();
                guangdian.setPokaMain(this);
            }
            this.setCurPanel(guangdian);
            if (this.xml.getDiaoChaoLogin().equals("1") || this.oneLogin && this.isNeedLogin()) {
                login = true;
                this.setLoginUser("");
                this.setCheckUser("");
                this.addPanel(guangdian);
                this.guangdian.initLogin();
                return;
            }

            TwoLoginFrame t = TwoLoginFrame.gettwoLoginFrame();
            t.getUserA().setText("");
            t.getUserB().setText("");
            t.getPwdA().setText("");
            t.getPwdB().setText("");
            t.setLocationRelativeTo(null);
            t.setPokaMain(this);
            t.show();

            this.setEnabled(false);
        } else if (getGuangdianMenu().isEnabled() && this.login) {
            this.addPanel(guangdian);
        }
    }//GEN-LAST:event_GuangdianMenuMouseClicked

    private void argCfgMenuMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_argCfgMenuMouseEntered
        this.argCfgMenu.getPopupMenu().setVisible(false);
    }//GEN-LAST:event_argCfgMenuMouseEntered

    private void argCfgMenuMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_argCfgMenuMousePressed
        this.argCfgMenu.getPopupMenu().setVisible(false);
    }//GEN-LAST:event_argCfgMenuMousePressed

    private void getMoneyMenuMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_getMoneyMenuMousePressed
        this.getMoneyMenu.getPopupMenu().setVisible(false);
    }//GEN-LAST:event_getMoneyMenuMousePressed

    private void searchDataMenuMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchDataMenuMousePressed
        this.searchDataMenu.getPopupMenu().setVisible(false);
    }//GEN-LAST:event_searchDataMenuMousePressed

    private void reUpMenuMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reUpMenuMousePressed
        this.reUpMenu.getPopupMenu().setVisible(false);
    }//GEN-LAST:event_reUpMenuMousePressed

    private void atmMenuMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_atmMenuMousePressed
        this.atmMenu.getPopupMenu().setVisible(false);
    }//GEN-LAST:event_atmMenuMousePressed

    private void dealGuanZiHaojMenuMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dealGuanZiHaojMenuMousePressed
        this.dealGuanZiHaojMenu.getPopupMenu().setVisible(false);
    }//GEN-LAST:event_dealGuanZiHaojMenuMousePressed

    private void fileChangeMenuMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fileChangeMenuMousePressed
        this.fileChangeMenu.getPopupMenu().setVisible(false);
    }//GEN-LAST:event_fileChangeMenuMousePressed

    private void getMoneyMenuMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_getMoneyMenuMouseEntered
        this.getMoneyMenu.getPopupMenu().setVisible(false);
    }//GEN-LAST:event_getMoneyMenuMouseEntered

    private void searchDataMenuMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchDataMenuMouseEntered
        this.searchDataMenu.getPopupMenu().setVisible(false);
    }//GEN-LAST:event_searchDataMenuMouseEntered

    private void reUpMenuMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reUpMenuMouseEntered
        this.reUpMenu.getPopupMenu().setVisible(false);
    }//GEN-LAST:event_reUpMenuMouseEntered

    private void atmMenuMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_atmMenuMouseEntered
        this.atmMenu.getPopupMenu().setVisible(false);
    }//GEN-LAST:event_atmMenuMouseEntered

    private void dealGuanZiHaojMenuMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dealGuanZiHaojMenuMouseEntered
        this.dealGuanZiHaojMenu.getPopupMenu().setVisible(false);
    }//GEN-LAST:event_dealGuanZiHaojMenuMouseEntered

    private void fileChangeMenuMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fileChangeMenuMouseEntered
        this.fileChangeMenu.getPopupMenu().setVisible(false);
    }//GEN-LAST:event_fileChangeMenuMouseEntered

    private void GuangdianMenuMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_GuangdianMenuMouseEntered
        this.GuangdianMenu.getPopupMenu().setVisible(false);
    }//GEN-LAST:event_GuangdianMenuMouseEntered

    private void GuangdianMenuMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_GuangdianMenuMousePressed
        this.GuangdianMenu.getPopupMenu().setVisible(false);
    }//GEN-LAST:event_GuangdianMenuMousePressed

    private void reprintMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reprintMenuMouseClicked
//        this.showBrower = false;
        if (reprintLabel == null) {
            reprintLabel = new ReprintJPanel();
        }
        this.addPanel(reprintLabel);
    }//GEN-LAST:event_reprintMenuMouseClicked

    private void reprintMenuMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reprintMenuMouseEntered
        this.reprintMenu.getPopupMenu().setVisible(false);
    }//GEN-LAST:event_reprintMenuMouseEntered

    private void reprintMenuMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reprintMenuMousePressed
        this.reprintMenu.getPopupMenu().setVisible(false);
    }//GEN-LAST:event_reprintMenuMousePressed

    private void aboutMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_aboutMenuMouseClicked
//        this.showBrower = false;
        addPanel(about);
    }//GEN-LAST:event_aboutMenuMouseClicked

    private void aboutMenuMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_aboutMenuMouseEntered
        this.aboutMenu.getPopupMenu().setVisible(false);
    }//GEN-LAST:event_aboutMenuMouseEntered

    private void aboutMenuMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_aboutMenuMousePressed
        this.aboutMenu.getPopupMenu().setVisible(false);
    }//GEN-LAST:event_aboutMenuMousePressed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        int i = JOptionPane.showConfirmDialog(null, "确定要退出系统吗？", "退出系统", JOptionPane.YES_NO_OPTION);
        if (i == JOptionPane.YES_OPTION) {
            System.exit(0);
        } else {
            return;
        }
    }//GEN-LAST:event_formWindowClosing

    private void browerjMenuMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_browerjMenuMousePressed
        this.browerjMenu.getPopupMenu().setVisible(false);
    }//GEN-LAST:event_browerjMenuMousePressed

    private void browerjMenuMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_browerjMenuMouseEntered
        this.browerjMenu.getPopupMenu().setVisible(false);
    }//GEN-LAST:event_browerjMenuMouseEntered

    private void browerjMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_browerjMenuMouseClicked
        browser = new SimpleWebBrowserPanel();
        JFrame frame = new JFrame("冠字号查询");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(browser, BorderLayout.CENTER);
        frame.setSize(800, 600);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        ImageIcon a = new ImageIcon(getClass().getResource("/com/poka/images/2.png"));
        if (StaticVar.cfgMap.get(argPro.webPtah) != null) {
            browser.getWebBrowser().navigate(StaticVar.cfgMap.get(argPro.webPtah));
        }
        frame.setIconImage(a.getImage());
        frame.setLocationByPlatform(true);
        frame.setVisible(true);

    }//GEN-LAST:event_browerjMenuMouseClicked

    private void fileChangeMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileChangeMenuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fileChangeMenuActionPerformed

    @Override
    public void actionPerformed(ActionEvent e) {
        startScanFile();
    }

    public void startScanFile() {
        String path = StaticVar.cfgMap.get(argPro.localAddr);
        refreshFile(path);
    }

    public void refreshFile(String path) {

    }

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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PokaMainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PokaMainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PokaMainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PokaMainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PokaMainFrame().setVisible(true);

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu GuangdianMenu;
    private javax.swing.JMenu aboutMenu;
    private javax.swing.JMenu argCfgMenu;
    public javax.swing.JMenu atmMenu;
    private javax.swing.JMenu browerjMenu;
    public javax.swing.JPanel chenbo;
    public javax.swing.JMenu dealGuanZiHaojMenu;
    public javax.swing.JMenu fileChangeMenu;
    private javax.swing.JMenu getMoneyMenu;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenu reUpMenu;
    private javax.swing.JMenu reprintMenu;
    private javax.swing.JMenu searchDataMenu;
    // End of variables declaration//GEN-END:variables

    public boolean login = false;
    public boolean oneLogin = false;
    //   private BaseDao<Arg> b = new BaseDao<Arg>();
    private String loginUser;
    private String loginUserName;
    private String checkUserName;
    private String checkUser;
    private int preSelect = -1;
    private ConfigJPanel cfg = null;
    private DataSendJPanel dataSendJPanel;
    private ShowJPanel showJPane = new ShowJPanel();
    private SimpleWebBrowserPanel browser = new SimpleWebBrowserPanel();

    private GuanZiHaoDealJPanel guanZiHaoJPanel;
    private ATMAddMonJPanel atmPanel;
    private ReUpLoadJPanel reupload = null;
    private GuangDianQFJJPanel guangdian;
    private GetMoneyJPanel getMoney = null;
    private ReprintJPanel reprintLabel = null;
    private DataSendFrame dataSendFram;
    private AboutJPanel about = new AboutJPanel();
    private LoginActionI curPanel;
    private boolean needLogin = false;
    private XmlSax xml = XmlSax.getInstance();
    public Timer clearXmlTimer;

    //  private getMoneyJPanel getMoney = new getMoneyJPanel();
    /**
     * @return the curPanel
     */
    public LoginActionI getCurPanel() {
        return curPanel;
    }

    /**
     * @param curPanel the curPanel to set
     */
    public void setCurPanel(LoginActionI curPanel) {
        this.curPanel = curPanel;
    }

    /**
     * @return the loginUser
     */
    public String getLoginUser() {
        return loginUser;
    }

    /**
     * @param loginUser the loginUser to set
     */
    public void setLoginUser(String loginUser) {
        this.loginUser = loginUser;
    }

    /**
     * @return the checkUser
     */
    public String getCheckUser() {
        return checkUser;
    }

    /**
     * @param checkUser the checkUser to set
     */
    public void setCheckUser(String checkUser) {
        this.checkUser = checkUser;
    }

    /**
     * @return the dataSendJPanel
     */
    public DataSendJPanel getDataSendJPanel() {
        return dataSendJPanel;
    }

    /**
     * @param dataSendJPanel the dataSendJPanel to set
     */
    public void setDataSendJPanel(DataSendJPanel dataSendJPanel) {
        this.dataSendJPanel = dataSendJPanel;
    }

    /**
     * @return the GuangdianMenu
     */
    public javax.swing.JMenu getGuangdianMenu() {
        return GuangdianMenu;
    }

    /**
     * @param GuangdianMenu the GuangdianMenu to set
     */
    public void setGuangdianMenu(javax.swing.JMenu GuangdianMenu) {
        this.GuangdianMenu = GuangdianMenu;
    }

    /**
     * @return the needLogin
     */
    public boolean isNeedLogin() {
        return needLogin;
    }

    /**
     * @param needLogin the needLogin to set
     */
    public void setNeedLogin(boolean needLogin) {
        this.needLogin = needLogin;
    }

    /**
     * @return the loginUserName
     */
    public String getLoginUserName() {
        return loginUserName;
    }

    /**
     * @param loginUserName the loginUserName to set
     */
    public void setLoginUserName(String loginUserName) {
        this.loginUserName = loginUserName;
    }

    /**
     * @return the checkUserName
     */
    public String getCheckUserName() {
        return checkUserName;
    }

    /**
     * @param checkUserName the checkUserName to set
     */
    public void setCheckUserName(String checkUserName) {
        this.checkUserName = checkUserName;
    }

    /**
     * @return the dataSendFram
     */
    public DataSendFrame getDataSendFram() {
        return dataSendFram;
    }

    /**
     * @param dataSendFram the dataSendFram to set
     */
    public void setDataSendFram(DataSendFrame dataSendFram) {
        this.dataSendFram = dataSendFram;
    }

}
