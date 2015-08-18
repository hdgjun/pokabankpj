package com.poka.socket;

import com.poka.entity.FsnComProperty;
import com.poka.entity.MachinesCfg;
import com.poka.util.LogManager;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PooledConnectionHandler implements Runnable {

    static final Logger logger = LogManager.getLogger(PooledConnectionHandler.class);
    protected Socket connection;
    protected static List pool = new LinkedList();
    protected FsnComProperty property;
    protected boolean m_stop = false;
    private volatile Thread blinker;
    private volatile boolean isStop = false;

    public PooledConnectionHandler() {
    }

    public PooledConnectionHandler(FsnComProperty property, Socket connection) {
        this.property = property;
        this.connection = connection;
    }

    public void handleConnection() {
        try {
            AbstractSocketHandle xDhd = null;
            MachinesCfg cfg = this.property.getXmlCfg().getMachineInfo(this.property.getBusType(), this.connection.getInetAddress().getHostAddress());
            if (cfg == null) {
                System.out.println("机具配置信息不存在！");
                return;
            }
            switch (cfg.getMachineNum()) {
                case FsnComProperty.zhongchaoMeType: {
                    SocketHandle sh = new XingDaSocketHandleImpl(this.connection);
                    xDhd = new XingDaSocketHandler(sh, this.property);
                    break;
                }
                case FsnComProperty.xinDaMeType: {

                }
                case FsnComProperty.renJieMeType: {

                }
                case FsnComProperty.yiTeNuoMeType: {
                    SocketHandle sh = new YiTenuoSockethandleImpl(this.connection);
                    xDhd = new YiTeNuoSocketHandler(sh, this.property);
                    break;
                }
                case FsnComProperty.liaoNinJulongType: {
                    SocketHandle sh = new LiaolinJulongSocketHandleImpl(this.connection);
                    xDhd = new LiaolinJulongSocketHandler(sh, this.property);
                    break;
                }
                case FsnComProperty.guaoMeType:{
                    SocketHandle sh = new GuAoSockethandleImpl(this.connection);
                    xDhd = new GuAoSocketHandler(sh, this.property);
                    break;
                }
                case FsnComProperty.weiRongType:{
                    SocketHandle sh = new WeiRongsockethandleImpl(this.connection);
                    xDhd = new WeiRongSocketHandler(sh, this.property);
                    break;
                }
            }
            SocketHandleFactory.getInstance(xDhd).receData();
        } catch (Exception ex) {
           logger.log(Level.INFO, null,ex);
           ex.printStackTrace();
        }
    }

    public static void processRequest(Socket requestToHandle) {
        synchronized (pool) {
            pool.add(pool.size(), requestToHandle);
            pool.notifyAll();
        }
    }

    public void run() {
        handleConnection();
    }

}
