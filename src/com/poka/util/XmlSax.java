/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poka.util;

import com.poka.entity.Arg;
import com.poka.entity.FsnComProperty;
import com.poka.entity.MachinesCfg;
import com.poka.entity.OperationUser;
import com.poka.dao.service.SQLiteService;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author poka
 */
public class XmlSax {

    private XmlSax() {

    }

    private static XmlSax xmlSax = null;

    private static final String path = System.getProperty("user.dir");

    private static final String regis = path + "\\registry.xml";

    public static final String f = path + "\\hibernate.cfg.xml";

    private static final String bankFile = path + "\\BankNum.xml";

    // private static final String gzhFile = path + "\\GZH.xml";
    private static final String sqlserverFile = path + "\\hibernate.cfg.sqlserver.xml";

    private final String encodingType = "GBK";

    private final String dbTyep = "mysql";

    private String ip = "";
    private String port = "";
    private String user = "";
    private String pwd = "";
    private String names = "";
    private String bankNum = "";
    private String networkNum = "";
    private String lastLoginer = "";
    private String lastAddCom = "";
    private String lastCom = "";
    private String locatNO = "";
    private String lastPaperSize = "";
    private List<MachinesCfg> machines;
    private List<MachinesCfg> addMachines;
    private List<MachinesCfg> guaoMachines;
    private List<Arg> argList;

    
    public static XmlSax getInstance() {
        if (xmlSax == null) {
            xmlSax = new XmlSax();
        }
        return xmlSax;
    }

    public Document load(String f, boolean isHiberConfig) {
        Document document = null;
        try {
            SAXReader saxReader = new SAXReader();
            //saxReader.setValidation(false);  
            //saxReader.setFeature("http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd", false);              
            //document = saxReader.read(f); //读取XML文件,获得document对象
            if (isHiberConfig) {
                saxReader.setEntityResolver(new EntityResolver() {
                    @Override
                    public InputSource resolveEntity(String publicId, String systemId)
                            throws SAXException, IOException {
                        // TODO Auto-generated method stub
                        InputSource is = new InputSource(this.getClass().getClassLoader().getResourceAsStream("com/poka/images/hibernate-configuration-3.0.dtd"));
                        is.setPublicId(publicId);
                        is.setSystemId(systemId);
                        return is;
                    }
                });
                saxReader.setValidation(true);
            }
            File tFile = new File(f);
            if (!tFile.exists() && !isHiberConfig) {
                document = DocumentHelper.createDocument();
            //    document.addDocType("hibernate-configuration", "-//Hibernate/Hibernate Configuration DTD 3.0//EN", "http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd");

                // 创建根节点并添加进文档
                Element root = DocumentHelper.createElement("root");
                document.setRootElement(root);
            } else {
                document = saxReader.read(tFile);
            }

        } catch (DocumentException ex) {
        }
        return document;
    }

    public Document load(URL f) {
        Document document = null;
        try {
            SAXReader saxReader = new SAXReader();
            String ff = f.getFile();
            File tem = new File(ff);
            if (!tem.exists()) {
                tem.createNewFile();
            }
            document = saxReader.read(tem);
        } catch (IOException | DocumentException ex) {
        }
        return document;
    }

    public boolean writeToXml(Document doc, String file) {
        try {
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding(encodingType);
            XMLWriter writer = new XMLWriter(new FileWriter(file), format);
            writer.write(doc);
            writer.flush();
            writer.close();

        } catch (IOException ex) {
            Logger.getLogger(XmlSax.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public void writeRegist(String reg) {
        Document doc = load(regis, false);
        Element rootElm = doc.getRootElement();
        Element root1Elm = rootElm.element("regist");
        if (root1Elm == null) {
            root1Elm = rootElm.addElement("regist");
        }
        root1Elm.setText(reg);

        writeToXml(doc, regis);
    }

    public void writeNeedLogin(String reg) {
        Document doc = load(regis, false);
        Element rootElm = doc.getRootElement();
        Element root1Elm = rootElm.element("login");
        if (root1Elm == null) {
            root1Elm = rootElm.addElement("login");
        }
        root1Elm.setText(reg);

        writeToXml(doc, regis);
    }

    public String getQuans(String mon, String kun) {
        Document doc = load(bankFile, false);
        Element rootElm = doc.getRootElement();
        Element root1Elm = rootElm.element("quans");
        if (root1Elm == null) {
            root1Elm = rootElm.addElement("quans");
            root1Elm.addElement("A100A10").setText("5A");
            root1Elm.addElement("A50A10").setText("5B");
            root1Elm.addElement("A20A10").setText("5C");
            root1Elm.addElement("A10A10").setText("5D");
            root1Elm.addElement("A5A10").setText("5E");
            root1Elm.addElement("A100A5").setText("9A");
            root1Elm.addElement("A50A5").setText("9B");
            root1Elm.addElement("A20A5").setText("9C");
            root1Elm.addElement("A10A5").setText("9D");
            root1Elm.addElement("A5A5").setText("9E");
            writeToXml(doc, bankFile);
        }
        String tem;
        Element node = root1Elm.element("A" + mon.trim() + "A" + kun.trim());
        if (node == null) {
            tem = null;
        } else {
            tem = node.getText();
        }
        return tem;
    }

    public final static String flowMoney = "flow";
    public final static String damagedMoney = "damaged";
    public final static String clientPort = "client";
    public final static String serverPort = "server";

    public String getFlowMoney(String type) {
        Document doc = load(bankFile, false);
        Element rootElm = doc.getRootElement();
        Element root1Elm = rootElm.element("folwMoney");
        if (root1Elm == null) {
            root1Elm = rootElm.addElement("folwMoney");
            root1Elm.addElement(flowMoney).setText("1|2");
            root1Elm.addElement(damagedMoney).setText("3");
            writeToXml(doc, bankFile);
        }
        String tem;
        Element node = root1Elm.element(type);
        if (node == null) {
            tem = null;
        } else {
            tem = node.getText();
        }
        return tem;
    }
    public static String machineA = "ma";
    public static String machineB = "mb";

    public String getGRMech(String type) {
        Document doc = load(bankFile, false);
        Element rootElm = doc.getRootElement();
        Element root1Elm = rootElm.element("grmachine");
        if (root1Elm == null) {
            root1Elm = rootElm.addElement("grmachine");
            root1Elm.addElement(machineA).setText("1");
            root1Elm.addElement(machineB).setText("2");
            writeToXml(doc, bankFile);
        }
        String tem;
        Element node = root1Elm.element(type);
        if (node == null) {
            tem = null;
        } else {
            tem = node.getText();
        }
        return tem;
    }

    public int getLastQF() {
        Document doc = load(bankFile, false);
        Element rootElm = doc.getRootElement();
        Element root1Elm = rootElm.element("QFJ");
        if (root1Elm == null) {
            return 0;
        }
        Element node = root1Elm.element("laftQF");
        if (node == null) {
            return 0;
        }
        return Integer.parseInt(node.getText().trim());
    }

    public void setLastQF(int item) {
        Document doc = load(bankFile, false);
        Element rootElm = doc.getRootElement();
        Element root1Elm = rootElm.element("QFJ");
        if (root1Elm == null) {
            root1Elm = rootElm.addElement("QFJ");
        }
        Element node = root1Elm.element("laftQF");
        if (node == null) {
            node = root1Elm.addElement("laftQF");
        }
        node.setText("" + item);
        writeToXml(doc, bankFile);
    }

    public String readRegist() {
        Document doc = load(regis, false);
        Element rootElm = doc.getRootElement();
        Element root1Elm = rootElm.element("regist");
        if (root1Elm == null) {
            return "";
        } else {
            return root1Elm.getText();
        }
    }

    public String getDiaoChaoLogin() {
        Document doc = load(bankFile, false);
        Element rootElm = doc.getRootElement();
        Element root1Elm = rootElm.element("login");
        String res = "";
        if (root1Elm == null) {
            root1Elm = rootElm.addElement("login");
            root1Elm.setText("0");
            res = "0";
            writeToXml(doc, bankFile);
        } else {
            res = root1Elm.getText().trim();
        }
        return res;
    }

    public String getZCXDFileNameL() {
        Document doc = load(bankFile, false);
        Element rootElm = doc.getRootElement();
        if (rootElm == null) {
            rootElm = doc.addElement("root");
        }
        Element root1Elm = rootElm.element("zcxdL");
        if (root1Elm == null) {
            root1Elm = rootElm.addElement("zcxdL");
            root1Elm.setText("[0-9]{8}_[0-9A-Za-z]*_[0-9]*_1_(CNY)[.]FSN$");
            writeToXml(doc, bankFile);
            return "[0-9]{8}_[0-9A-Za-z]*_[0-9]*_1_(CNY)[.]FSN$";
        } else {
            return root1Elm.getTextTrim();
        }
    }

    public String getZCXDFileNameC() {
        Document doc = load(bankFile, false);
        Element rootElm = doc.getRootElement();
        if (rootElm == null) {
            rootElm = doc.addElement("root");
        }
        Element root1Elm = rootElm.element("zcxdC");
        if (root1Elm == null) {
            root1Elm = rootElm.addElement("zcxdC");
            root1Elm.setText("[0-9]{8}_[0-9A-Za-z]*_[0-9]*_2_(CNY)[.]FSN$");
            writeToXml(doc, bankFile);
            return "[0-9]{8}_[0-9A-Za-z]*_[0-9]*_2_(CNY)[.]FSN$";
        } else {
            return root1Elm.getTextTrim();
        }
    }

    public String getGDFileNameL() {
        Document doc = load(bankFile, false);
        Element rootElm = doc.getRootElement();
        if (rootElm == null) {
            rootElm = doc.addElement("root");
        }
        Element root1Elm = rootElm.element("dgL");
        if (root1Elm == null) {
            root1Elm = rootElm.addElement("dgL");
            root1Elm.setText("[0-9A-Za-z]*_[0-9]{14}_[0-9A-Za-z]{8}_[0-9]*_(1|2|3)[.]FSN$");
            writeToXml(doc, bankFile);
            return "[0-9A-Za-z]*_[0-9]{14}_[0-9A-Za-z]{8}_[0-9]*_(1|2|3)[.]FSN$";
        } else {
            return root1Elm.getTextTrim();
        }
    }

    public String getGDFileNameC() {
        Document doc = load(bankFile, false);
        Element rootElm = doc.getRootElement();
        if (rootElm == null) {
            rootElm = doc.addElement("root");
        }
        Element root1Elm = rootElm.element("dgC");
        if (root1Elm == null) {
            root1Elm = rootElm.addElement("dgC");
            root1Elm.setText("[0-9A-Za-z]*_[0-9]{14}_[0-9A-Za-z]{8}_[0-9]*_(4|8|12)[.]FSN$");
            writeToXml(doc, bankFile);
            return "[0-9A-Za-z]*_[0-9]{14}_[0-9A-Za-z]{8}_[0-9]*_(4|8|12)[.]FSN$";
        } else {
            return root1Elm.getTextTrim();
        }
    }

    public String getLLJLFileNameL() {
        Document doc = load(bankFile, false);
        Element rootElm = doc.getRootElement();
        if (rootElm == null) {
            rootElm = doc.addElement("root");
        }
        Element root1Elm = rootElm.element("lljlL");
        if (root1Elm == null) {
            root1Elm = rootElm.addElement("lljlL");
            root1Elm.setText("[0-9A-Z]*_1_[0-9]{4}_[0-9]{17}[.]FSN$");
            writeToXml(doc, bankFile);
            return "[0-9A-Z]*_1_[0-9]{4}_[0-9]{17}[.]FSN$";
        } else {
            return root1Elm.getTextTrim();
        }
    }

    public String getLLJLFileNameC() {
        Document doc = load(bankFile, false);
        Element rootElm = doc.getRootElement();
        if (rootElm == null) {
            rootElm = doc.addElement("root");
        }
        Element root1Elm = rootElm.element("lljlC");
        if (root1Elm == null) {
            root1Elm = rootElm.addElement("lljlC");
            root1Elm.setText("[0-9A-Z]*_2_[0-9]{4}_[0-9]{17}[.]FSN$");
            writeToXml(doc, bankFile);
            return "[0-9A-Z]*_2_[0-9]{4}_[0-9]{17}[.]FSN$";
        } else {
            return root1Elm.getTextTrim();
        }
    }

    public boolean isNeedBreakQF() {
        Document doc = load(bankFile, false);
        Element rootElm = doc.getRootElement();
        if (rootElm == null) {
            rootElm = doc.addElement("root");
        }
        Element root1Elm = rootElm.element("breadqf");
        if (root1Elm == null) {
            root1Elm = rootElm.addElement("breadqf");
            root1Elm.setText("0");
            writeToXml(doc, bankFile);
            return false;
        } else {
            String tem = root1Elm.getTextTrim();
            return tem.equals("1");
        }
    }

    public boolean isNeedLogin() {
        Document doc = load(regis, false);
        Element rootElm = doc.getRootElement();
        if (rootElm == null) {
            rootElm = doc.addElement("root");
        }
        Element root1Elm = rootElm.element("login");
        if (root1Elm == null) {
            return false;
        } else {
            String tem = root1Elm.getTextTrim();
            return tem.equals("0");
        }
    }

    public boolean isRegist() {
        Document doc = load(regis, false);
        Element rootElm = doc.getRootElement();
        if (rootElm == null) {
            rootElm = doc.addElement("root");
        }
        Element root1Elm = rootElm.element("regist");
        if (root1Elm == null) {
            return false;
        } else {
            String tem = root1Elm.getTextTrim();
            String tem1 = RegistrationUtil.getRegistration();
            return tem.equals(tem1);
        }
    }

    /**
     * 修改SqlServer配置文件
     *
     * @return
     */
    public boolean changeSqlServerXmlAtt() {

        Document doc = load(sqlserverFile, true);
        Element rootElm = doc.getRootElement();
        Element root1Elm = rootElm.element("session-factory");

        List nodes = root1Elm.elements("property");
        for (Iterator it = nodes.iterator(); it.hasNext();) {
            Element elm = (Element) it.next();
            String name = elm.attributeValue("name");
            if (name.equals("connection.url")) {
                //String url = "jdbc:mysql://"+ip+":"+port+"/"+names;
                String url = "jdbc:sqlserver://" + ip + ":" + port + ";DatabaseName=" + names;

                elm.setText(url);
            }
            if (name.equals("connection.username")) {
                elm.setText(user);
            }
            if (name.equals("connection.password")) {
                elm.setText(pwd);
            }
        }

        return writeToXml(doc, sqlserverFile);
    }

    /**
     * 获取sqlserver数据库配置文件中的值
     *
     * @return
     */
    public void getSqlServerXmlVal() {
        Document doc = load(sqlserverFile, true);
        Element rootElm = doc.getRootElement();
        Element root1Elm = rootElm.element("session-factory");
        List nodes = root1Elm.elements("property");
        for (Iterator it = nodes.iterator(); it.hasNext();) {
            Element elm = (Element) it.next();
            String name = elm.attributeValue("name");
            if (name.equals("connection.url")) {
                String url = elm.getText();
                int begingan = url.indexOf("//");
                ip = url.substring(begingan + 2, url.lastIndexOf(":"));
                port = url.substring(url.lastIndexOf(":") + 1, url.lastIndexOf(";"));
                names = url.substring(url.lastIndexOf("=") + 1);

            }
            if (name.equals("connection.username")) {
                user = elm.getText();
            }

            if (name.equals("connection.password")) {
                pwd = elm.getText();
            }
        }
    }

    public boolean changeXmlAtt() {
        Document doc = load(f, true);
        Element rootElm = doc.getRootElement();
        Element root1Elm = rootElm.element("session-factory");
        List nodes = root1Elm.elements("property");
        for (Iterator it = nodes.iterator(); it.hasNext();) {
            Element elm = (Element) it.next();
            String name = elm.attributeValue("name");
            if (name.equals("connection.url")) {
                String url = "";
                if (dbTyep.equals("mysql")) {
                    url = " jdbc:mysql://" + ip + ":" + port + "/" + names + "?useUnicode=true&characterEncoding=utf8";
                } else {
                    url = "jdbc:sqlserver://" + ip + ":" + port + ";DatabaseName=" + names;
                }
                elm.setText(url);
            }
            if (name.equals("connection.username")) {
                elm.setText(user);
            }
            if (name.equals("connection.password")) {
                elm.setText(pwd);
            }
        }
        return writeToXml(doc, getF());
    }

    public boolean changeBankNumXmlAtt() {
        Document doc = load(bankFile, false);
        Element rootElm = doc.getRootElement();
        Element root1Elm = rootElm.element("bankInfos");
        if (root1Elm == null) {
            root1Elm = rootElm.addElement("bankInfos");
        }
        boolean flag = false;
        List nodes = root1Elm.elements("bankInfo");
        for (Iterator it = nodes.iterator(); it.hasNext();) {
            Element elm = (Element) it.next();
            String attr = elm.attributeValue("id");
            if (attr.equals("1")) {
                flag = true;
                elm.setAttributeValue("ip", this.ip);
                elm.setAttributeValue("port", this.port);
                elm.setAttributeValue("bankNum", this.bankNum);
                elm.setAttributeValue("NetWorkNum", this.networkNum);
            } else {
                continue;
            }
        }
        if (!flag) {
            Element elm = root1Elm.addElement("bankInfo");
            elm.setAttributeValue("ip", this.ip);
            elm.setAttributeValue("port", this.port);
            elm.setAttributeValue("bankNum", this.bankNum);
            elm.setAttributeValue("NetWorkNum", this.networkNum);
            elm.setAttributeValue("id", "1");
        }
        return writeToXml(doc, bankFile);
    }

    public String getDPort(String type) {
        Document doc = load(bankFile, false);
        Element rootElm = doc.getRootElement();
        Element root1Elm = rootElm.element("dport");

        if (root1Elm != null) {

            Element em = root1Elm.element(type);
            if (em != null) {
                return em.getTextTrim();
            }
        }
        return null;
    }

    public void setDPort(String type, String name) {
        Document doc = load(bankFile, false);
        Element rootElm = doc.getRootElement();
        Element root1Elm = rootElm.element("dport");
        if (root1Elm == null) {
            root1Elm = rootElm.addElement("dport");
        }
        Element node = root1Elm.element(type);
        if(node == null){
            node = root1Elm.addElement(type);
        }
        node.setText(name.trim());
        writeToXml(doc, bankFile);
    }

    public String getHPort() {
        Document doc = load(bankFile, false);
        Element rootElm = doc.getRootElement();
        Element root1Elm = rootElm.element("hport");

        if (root1Elm == null) {
            return null;
        } else {
            return root1Elm.getTextTrim();
        }
    }

    public void setHPort(String name) {
        Document doc = load(bankFile, false);
        Element rootElm = doc.getRootElement();
        Element root1Elm = rootElm.element("hport");
        if (root1Elm == null) {
            root1Elm = rootElm.addElement("hport");
        }
        root1Elm.setText(name.trim());
        writeToXml(doc, bankFile);
    }

    public String getBnakName() {
        Document doc = load(bankFile, false);
        Element rootElm = doc.getRootElement();
        Element root1Elm = rootElm.element("bankName");
        if (root1Elm == null) {

            return null;
        }
        Element node = root1Elm.element("name");
        if (node == null) {
            return null;
        } else {
            return node.getTextTrim();
        }
    }

    public void setBnakName(String name) {
        Document doc = load(bankFile, false);
        Element rootElm = doc.getRootElement();
        Element root1Elm = rootElm.element("bankName");
        if (root1Elm == null) {
            root1Elm = rootElm.addElement("bankName");
        }
        boolean flag = false;
        Element node = root1Elm.element("name");
        if (node == null) {
            node = root1Elm.addElement("name");

        }
        node.setText(name.trim());
        writeToXml(doc, bankFile);
    }

    public String getMechineNO() {
        Document doc = load(bankFile, false);
        Element rootElm = doc.getRootElement();
        Element root1Elm = rootElm.element("mechineNo");
        if (root1Elm == null) {

            return null;
        }
        Element node = root1Elm.element("title");
        if (node == null) {
            return null;
        } else {
            return node.getTextTrim();
        }
    }

    public void setMechineNO(String title) {
        Document doc = load(bankFile, false);
        Element rootElm = doc.getRootElement();
        Element root1Elm = rootElm.element("mechineNo");
        if (root1Elm == null) {
            root1Elm = rootElm.addElement("mechineNo");
        }
        boolean flag = false;
        Element node = root1Elm.element("title");
        if (node == null) {
            node = root1Elm.addElement("title");
        }
        node.setText(title.trim());
        writeToXml(doc, bankFile);
    }

    public void getBankInfo() {
        Document doc = load(bankFile, false);
        Element rootElm = doc.getRootElement();
        Element root1Elm = rootElm.element("bankInfos");
        if (root1Elm == null) {
            root1Elm = rootElm.addElement("bankInfos");
        }
        List nodes = root1Elm.elements("bankInfo");
        for (Iterator it = nodes.iterator(); it.hasNext();) {
            Element elm = (Element) it.next();
            String attr = elm.attributeValue("id");
            if (attr.equals("1")) {
                this.setIp(elm.attributeValue("ip"));
                this.setPort(elm.attributeValue("port"));
                setBankNum(elm.attributeValue("bankNum"));
                setNetworkNum(elm.attributeValue("NetWorkNum"));
            }
        }
    }

    public void getMachineInfo() {
        this.machines = new ArrayList<MachinesCfg>();
        Document doc = load(bankFile, false);
        Element rootElm = doc.getRootElement();
        Element root1Elm = rootElm.element("machines");
        if (root1Elm == null) {
            root1Elm = rootElm.addElement("machines");
        }
        List nodes = root1Elm.elements("machine");
        for (Iterator it = nodes.iterator(); it.hasNext();) {
            Element elm = (Element) it.next();
            if (elm.attributeValue("type").equals("" + FsnComProperty.comBusType)) {
                MachinesCfg cfg = new MachinesCfg();
                cfg.setIp(elm.attributeValue("ip"));
                cfg.setMachineType(elm.attributeValue("machineType"));
                cfg.setMachineNum(Integer.parseInt(elm.attributeValue("machineNum").trim()));
                cfg.setUser1(elm.attributeValue("user1"));
                cfg.setUser2(elm.attributeValue("user2"));
                cfg.setType(Integer.parseInt(elm.attributeValue("type").trim()));
                this.machines.add(cfg);
            }
        }
    }

    public MachinesCfg getMachineInfo(int type, String ip) {
        Document doc = load(bankFile, false);
        Element rootElm = doc.getRootElement();
        Element root1Elm = rootElm.element("machines");
        if (root1Elm == null) {
            root1Elm = rootElm.addElement("machines");
        }
        List nodes = root1Elm.elements("machine");
        for (Iterator it = nodes.iterator(); it.hasNext();) {
            Element elm = (Element) it.next();
            if (elm.attributeValue("type").equals("" + type) && elm.attributeValue("ip").equals(ip.trim())) {
                MachinesCfg cfg = new MachinesCfg();
                cfg.setIp(elm.attributeValue("ip"));
                cfg.setMachineType(elm.attributeValue("machineType"));
                cfg.setMachineNum(Integer.parseInt(elm.attributeValue("machineNum").trim()));
                cfg.setUser1(elm.attributeValue("user1"));
                cfg.setUser2(elm.attributeValue("user2"));
                cfg.setType(Integer.parseInt(elm.attributeValue("type").trim()));
                return cfg;
            }
        }
        return null;
    }

    public void getAddMachineInfo() {
        this.addMachines = new ArrayList<>();
        Document doc = load(bankFile, false);
        Element rootElm = doc.getRootElement();
        Element root1Elm = rootElm.element("machines");
        if (root1Elm == null) {
            root1Elm = rootElm.addElement("machines");
        }
        List nodes = root1Elm.elements("machine");
        for (Iterator it = nodes.iterator(); it.hasNext();) {
            Element elm = (Element) it.next();
            if (elm.attributeValue("type").equals("" + FsnComProperty.atmAddBusType)) {
                MachinesCfg cfg = new MachinesCfg();
                cfg.setIp(elm.attributeValue("ip"));
                cfg.setMachineType(elm.attributeValue("machineType"));
                cfg.setMachineNum(Integer.parseInt(elm.attributeValue("machineNum").trim()));
                cfg.setUser1(elm.attributeValue("user1"));
                cfg.setUser2(elm.attributeValue("user2"));
                cfg.setType(Integer.parseInt(elm.attributeValue("type").trim()));
                this.addMachines.add(cfg);
            }
        }
    }

    public void getGuaoMachineInfo() {
        this.guaoMachines = new ArrayList<MachinesCfg>();
        Document doc = load(bankFile, false);
        Element rootElm = doc.getRootElement();
        Element root1Elm = rootElm.element("machines");
        if (root1Elm == null) {
            root1Elm = rootElm.addElement("machines");
        }
        List nodes = root1Elm.elements("machine");
        for (Iterator it = nodes.iterator(); it.hasNext();) {
            Element elm = (Element) it.next();
            if (elm.attributeValue("type").equals("" + FsnComProperty.guaoBusType)) {
                MachinesCfg cfg = new MachinesCfg();
                cfg.setIp(elm.attributeValue("ip"));
                cfg.setMachineType(elm.attributeValue("machineType"));
                cfg.setMachineNum(Integer.parseInt(elm.attributeValue("machineNum").trim()));
                cfg.setUser1(elm.attributeValue("user1"));
                cfg.setUser2(elm.attributeValue("user2"));
                cfg.setType(Integer.parseInt(elm.attributeValue("type").trim()));
                this.guaoMachines.add(cfg);
            }
        }
    }

    public int updateOrAddMachineInfo(MachinesCfg cfg) {
        Document doc = load(bankFile, false);
        Element rootElm = doc.getRootElement();
        Element root1Elm = rootElm.element("machines");
        if (root1Elm == null) {
            root1Elm = rootElm.addElement("machines");
        }
        List nodes = root1Elm.elements("machine");
        boolean flag = false;
        for (Iterator it = nodes.iterator(); it.hasNext();) {
            Element elm = (Element) it.next();
            if (elm.attributeValue("ip").trim().equals(cfg.getIp().trim()) && elm.attributeValue("type").trim().equals("" + cfg.getType())) {
                elm.setAttributeValue("machineType", cfg.getMachineType().trim());
                elm.setAttributeValue("machineNum", "" + cfg.getMachineNum());
                elm.setAttributeValue("user1", cfg.getUser1().trim());
                elm.setAttributeValue("user2", cfg.getUser2().trim());
                elm.setAttributeValue("type", "" + cfg.getType());
                flag = true;
                break;
            }
        }
        if (!flag) {
            Element tem = root1Elm.addElement("machine");
            tem.addAttribute("machineType", cfg.getMachineType().trim());
            tem.setAttributeValue("machineNum", "" + cfg.getMachineNum());
            tem.addAttribute("ip", cfg.getIp().trim());
            tem.addAttribute("user1", cfg.getUser1().trim());
            tem.addAttribute("user2", cfg.getUser2().trim());
            tem.addAttribute("type", "" + cfg.getType());
        }
        try {
            OutputFormat format = OutputFormat.createPrettyPrint();
            // format.setEncoding("UTF-8");
            format.setEncoding(encodingType);
            XMLWriter writer = new XMLWriter(new FileWriter(bankFile), format);
            writer.write(doc);
            writer.flush();
            writer.close();
            if (flag) {//更新
                return 0;
            } else {//新增
                return 1;
            }
            //  return true;
        } catch (IOException ex) {
            Logger.getLogger(XmlSax.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public OperationUser getUser1AndUser2(String sIp, int type) {
        Document doc = load(bankFile, false);
        Element rootElm = doc.getRootElement();
        Element root1Elm = rootElm.element("machines");
        if (root1Elm == null) {
            root1Elm = rootElm.addElement("machines");
        }
        List nodes = root1Elm.elements("machine");
        OperationUser usr = new OperationUser();
        for (Iterator it = nodes.iterator(); it.hasNext();) {
            Element elm = (Element) it.next();
            if (elm.attributeValue("ip").trim().equals(sIp.trim()) && elm.attributeValue("type").trim().equals("" + type)) {
                usr.setUser1(elm.attributeValue("user1").trim());
                usr.setUser2(elm.attributeValue("user2").trim());
            }
        }
        return usr;
    }

    public boolean isCfgIp(String sIp, int type) {
        Document doc = load(bankFile, false);
        Element rootElm = doc.getRootElement();
        Element root1Elm = rootElm.element("machines");
        if (root1Elm == null) {
            root1Elm = rootElm.addElement("machines");
        }
        List nodes = root1Elm.elements("machine");
        boolean flag = false;
        for (Iterator it = nodes.iterator(); it.hasNext();) {
            Element elm = (Element) it.next();
            if (elm.attributeValue("ip").trim().equals(sIp.trim()) && elm.attributeValue("type").trim().equals("" + type)) {
                return true;
            }
        }
        return false;
    }

    public synchronized void clearMon() {
        SQLiteService.clearMon();
    }

    public synchronized boolean isRepeatMon(String mon, int limit) {
        return SQLiteService.isRepeatMon(1, mon, limit);
    }

    public synchronized boolean isRepeatMonATMAdd(String mon, int limit) {
        return SQLiteService.isRepeatMon(2, mon, limit);
    }

    public boolean deleteMachineInfo(MachinesCfg cfg) {
        Document doc = load(bankFile, false);
        Element rootElm = doc.getRootElement();
        Element root1Elm = rootElm.element("machines");
        if (root1Elm == null) {
            root1Elm = rootElm.addElement("machines");
        }
        List nodes = root1Elm.elements("machine");
        boolean flag = false;
        for (Iterator it = nodes.iterator(); it.hasNext();) {
            Element elm = (Element) it.next();
            if (elm.attributeValue("ip").trim().equals(cfg.getIp().trim()) && elm.attributeValue("type").trim().equals("" + cfg.getType())) {
                root1Elm.remove(elm);
                break;
            }
        }

        return writeToXml(doc, bankFile);
    }

    /**
     * 获取数据库配置文件中的值dddddd
     *
     * @return
     */
    public void getHibernateXmlVal() {
        Document doc = load(f, true);
        Element rootElm = doc.getRootElement();
        Element root1Elm = rootElm.element("session-factory");
        List nodes = root1Elm.elements("property");
        for (Iterator it = nodes.iterator(); it.hasNext();) {
            Element elm = (Element) it.next();
            String name = elm.attributeValue("name");
            if (name.equals("connection.url")) {
                String url = elm.getText();
                if (this.dbTyep.equals("mysql")) {
                    if (url.contains("mysql")) {
                        int begingan = url.indexOf("//");
                        ip = url.substring(begingan + 2, url.lastIndexOf(":"));
                        port = url.substring(url.lastIndexOf(":") + 1, url.lastIndexOf("/"));
                        names = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("?"));
                    } else {
                        ip = "";
                        port = "";
                        names = "";
                    }
                } else {
                    if (url.contains("sqlserver")) {
                        int begingan = url.indexOf("//");
                        ip = url.substring(begingan + 2, url.lastIndexOf(":"));
                        port = url.substring(url.lastIndexOf(":") + 1, url.lastIndexOf(";"));
                        names = url.substring(url.lastIndexOf("=") + 1);
                    } else {
                        ip = "";
                        port = "";
                        names = "";
                    }
                }
            }
            if (name.equals("connection.username")) {
                user = elm.getText();
            }

            if (name.equals("connection.password")) {
                pwd = elm.getText();
            }
        }
    }//lastPaperSize

    public void getLastlastPaperSizeInfo() {
        Document doc = load(bankFile, false);
        Element rootElm = doc.getRootElement();
        Element root1Elm = rootElm.element("paperSize");
        if (root1Elm == null) {
            return;
        }
        Element node = root1Elm.element("lastSize");
        if (node == null) {
            return;
        }
        this.lastPaperSize = node.getText();
    }

    public void getLastAddComInfo() {
        Document doc = load(bankFile, false);
        Element rootElm = doc.getRootElement();
        Element root1Elm = rootElm.element("addcoms");
        if (root1Elm == null) {
            return;
        }
        Element node = root1Elm.element("com");
        if (node == null) {
            return;
        }
        this.lastAddCom = node.getText();
    }

    public void getLastComInfo() {
        Document doc = load(bankFile, false);
        Element rootElm = doc.getRootElement();
        Element root1Elm = rootElm.element("coms");
        if (root1Elm == null) {
            return;
        }
        Element node = root1Elm.element("com");
        if (node == null) {
            return;
        }
        this.lastCom = node.getText();
    }

    public void getLocalNOInfo() {
        Document doc = load(bankFile, false);
        Element rootElm = doc.getRootElement();
        Element root1Elm = rootElm.element("localnos");
        if (root1Elm == null) {
            return;
        }
        Element node = root1Elm.element("NO");
        if (node == null) {
            return;
        }
        this.locatNO = node.getText();
    }

    public void getLastInfo() {
        Document doc = load(bankFile, false);
        Element rootElm = doc.getRootElement();
        Element root1Elm = rootElm.element("loginers");
        if (root1Elm == null) {
            return;
        }
        Element node = root1Elm.element("loginer");
        if (node == null) {
            return;
        }
        this.lastLoginer = node.getText();
    }

    public void getSoftTitle() {
        Document doc = load(bankFile, false);
        Element rootElm = doc.getRootElement();
        Element root1Elm = rootElm.element("bankInfos");
        if (root1Elm == null) {
            root1Elm = rootElm.addElement("bankInfos");
        }
        List nodes = root1Elm.elements("bankInfo");
        for (Iterator it = nodes.iterator(); it.hasNext();) {
            Element elm = (Element) it.next();
            String attr = elm.attributeValue("id");
            if (attr.equals("2")) {
                StaticVar.showSoftName = elm.attributeValue("myname");
            }
        }
    }

    public void setSoftTitle(String title) {
        Document doc = load(bankFile, false);
        Element rootElm = doc.getRootElement();
        Element root1Elm = rootElm.element("bankInfos");
        if (root1Elm == null) {
            root1Elm = rootElm.addElement("bankInfos");
        }
        boolean flag = false;
        List nodes = root1Elm.elements("bankInfo");
        for (Iterator it = nodes.iterator(); it.hasNext();) {
            Element elm = (Element) it.next();
            String attr = elm.attributeValue("id");
            if (attr.equals("2")) {
                flag = true;
                elm.setAttributeValue("myname", title);
            }
        }
        if (!flag) {
            Element elm = root1Elm.addElement("bankInfo");
            elm.setAttributeValue("myname", title);
            elm.setAttributeValue("id", "2");
        }
        writeToXml(doc, bankFile);
    }

    /**
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip the ip to set
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @return the port
     */
    public String getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(String port) {
        this.port = port;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return the pwd
     */
    public String getPwd() {
        return pwd;
    }

    /**
     * @param pwd the pwd to set
     */
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    /**
     * @return the name
     */
    public String getName() {
        return names;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.names = name;
    }

    /**
     * @return the bankNum
     */
    public String getBankNum() {
        return bankNum;
    }

    /**
     * @param bankNum the bankNum to set
     */
    public void setBankNum(String bankNum) {
        this.bankNum = bankNum;
    }

    /**
     * @return the networkNum
     */
    public String getNetworkNum() {
        return networkNum;
    }

    /**
     * @param networkNum the networkNum to set
     */
    public void setNetworkNum(String networkNum) {
        this.networkNum = networkNum;
    }

    /**
     * @return the machines
     */
    public List<MachinesCfg> getMachines() {

        getMachineInfo();

        return machines;
    }

    /**
     * @return the addMachines
     */
    public List<MachinesCfg> getAddMachines() {

        getAddMachineInfo();

        return addMachines;
    }

    public List<MachinesCfg> getGuaoMachines() {

        getGuaoMachineInfo();

        return guaoMachines;
    }

    /**
     * @return the lastLoginer
     */
    public String getLastLoginer() {

        getLastInfo();

        return lastLoginer;
    }

    /**
     * @param lastLoginer the lastLoginer to set
     */
    public void setLastLoginer(String lastLoginer) {
        this.lastLoginer = lastLoginer;
        Document doc = load(bankFile, false);
        Element rootElm = doc.getRootElement();
        Element root1Elm = rootElm.element("loginers");
        if (root1Elm == null) {
            root1Elm = rootElm.addElement("loginers");
        }
        Element node = root1Elm.element("loginer");
        if (node == null) {
            node = root1Elm.addElement("loginer");
        }
        node.setText(this.lastLoginer);
        writeToXml(doc, bankFile);

    }

    /**
     * @return the f
     */
    public static String getF() {
        return f;
    }

    /**
     * @return the lastAddCom
     */
    public String getLastAddCom() {

        this.getLastAddComInfo();

        return lastAddCom;
    }

    /**
     * @param lastAddCom the lastAddCom to set
     */
    public void setLastAddCom(String lastAddCom) {
        this.lastAddCom = lastAddCom;
        Document doc = load(bankFile, false);
        Element rootElm = doc.getRootElement();
        Element root1Elm = rootElm.element("addcoms");
        if (root1Elm == null) {
            root1Elm = rootElm.addElement("addcoms");
        }
        Element node = root1Elm.element("com");
        if (node == null) {
            node = root1Elm.addElement("com");
        }
        node.setText(this.lastAddCom);
        writeToXml(doc, bankFile);
    }

    public String getLastPaperSize() {

        this.getLastlastPaperSizeInfo();

        return lastPaperSize;
    }

    public void setLastPaperSize(String lastPaperSize) {
        this.lastPaperSize = lastPaperSize;
        Document doc = load(bankFile, false);
        Element rootElm = doc.getRootElement();
        Element root1Elm = rootElm.element("paperSize");
        if (root1Elm == null) {
            root1Elm = rootElm.addElement("paperSize");
        }
        Element node = root1Elm.element("lastSize");
        if (node == null) {
            node = root1Elm.addElement("lastSize");
        }
        node.setText(this.lastPaperSize);
        writeToXml(doc, bankFile);
    }

    /**
     * @return the lastCom
     */
    public String getLastCom() {

        this.getLastComInfo();

        return lastCom;
    }

    /**
     * @param lastCom the lastCom to set
     */
    public void setLastCom(String lastCom) {
        this.lastCom = lastCom;
        Document doc = load(bankFile, false);
        Element rootElm = doc.getRootElement();
        Element root1Elm = rootElm.element("coms");
        if (root1Elm == null) {
            root1Elm = rootElm.addElement("coms");
        }
        Element node = root1Elm.element("com");
        if (node == null) {
            node = root1Elm.addElement("com");
        }
        node.setText(this.lastCom);
        writeToXml(doc, bankFile);
    }

    /**
     * @return the locatNO
     */
    public String getLocatNO() {

        this.getLocalNOInfo();

        return locatNO;
    }

    /**
     * @param locatNO the locatNO to set
     */
    public void setLocatNO(String locatNO) {
        this.locatNO = locatNO;
        Document doc = load(bankFile, false);
        Element rootElm = doc.getRootElement();
        Element root1Elm = rootElm.element("localnos");
        if (root1Elm == null) {
            root1Elm = rootElm.addElement("localnos");
        }
        Element node = root1Elm.element("NO");
        if (node == null) {
            node = root1Elm.addElement("NO");
        }
        node.setText(this.lastCom);
        writeToXml(doc, bankFile);
    }

    /**
     * @return the argList
     */
    public List<Arg> getArgList() {
        getArgListInfo();
        return argList;
    }

    public void getArgListInfo() {
        this.argList = new ArrayList<Arg>();
        Document doc = load(bankFile, false);
        Element rootElm = doc.getRootElement();
        Element root1Elm = rootElm.element("args");
        if (root1Elm == null) {
            root1Elm = rootElm.addElement("args");
        }
        List nodes = root1Elm.elements();
        for (Iterator it = nodes.iterator(); it.hasNext();) {
            Element elm = (Element) it.next();
            Arg cfg = new Arg();
            cfg.setAkey(elm.getName().trim());
            cfg.setAvalue(elm.getTextTrim());
            this.argList.add(cfg);

        }
    }

    public void updateArg(Arg ar) {
        Document doc = load(bankFile, false);
        Element rootElm = doc.getRootElement();
        Element root1Elm = rootElm.element("args");
        if (root1Elm == null) {
            root1Elm = rootElm.addElement("args");
        }
        Element node = root1Elm.element(ar.getAkey().trim());
        if (node == null) {
            node = root1Elm.addElement(ar.getAkey().trim());
        }
        node.setText(ar.getAvalue().trim());
        writeToXml(doc, bankFile);
    }

    /**
     * @param argList the argList to set
     */
    public void setArgList(List<Arg> argList) {
        this.argList = argList;
    }
}
