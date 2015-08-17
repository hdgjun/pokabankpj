package com.poka.socket;

import static com.poka.socket.GuAoSocketHandler.lock;
import com.poka.entity.FsnComProperty;
import com.poka.entity.PanelMsgEntity;
import com.poka.util.LogManager;
import com.poka.util.MsgThread;
import com.poka.util.StaticVar;
import java.io.IOException;
import java.io.InputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

public class FsnListenServer {

    static final Logger logger = LogManager.getLogger(FsnListenServer.class);
    private int maxConnections;
    private int listenPort;
    private ServerSocket serverSocket;
    private FsnComProperty property;
    private volatile boolean m_stop = false;

    private ExecutorService executorService;//线程池
    private final int POOL_SIZE = 10;//单个CPU线程池大小

    public FsnListenServer(int aListenPort, int maxConnections) {
        this.listenPort = aListenPort;
        this.property = null;
        this.maxConnections = maxConnections;
    }

    public FsnListenServer(int aListenPort, int maxConnections, FsnComProperty property) {
        this.listenPort = aListenPort;
        this.property = property;
        this.maxConnections = maxConnections;
    }

    private void acceptConnections() {
        try {
            serverSocket = new ServerSocket(getListenPort());
            executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * POOL_SIZE);
            StaticVar.monTcpListen = true;
            Socket incomingConnection = null;
            while (StaticVar.monTcpListen) {
                System.out.println("等待机具接入！");
                incomingConnection = serverSocket.accept();
                if (!StaticVar.monTcpListen) {
                    System.out.println("停止接入");
                    incomingConnection.close();
                    break;
                }
                String ip = incomingConnection.getInetAddress().getHostAddress();
                synchronized (lock) {
                    if (getProperty().getXmlCfg().isCfgIp(ip, this.property.getBusType())) {
                        showMsg(PanelMsgEntity.connectMSGType, null, null, ip, PanelMsgEntity.connectState);
                        PooledConnectionHandler thread = new PooledConnectionHandler(this.getProperty(), incomingConnection);
                        executorService.execute(thread);
                    } else {
                        showMsg(PanelMsgEntity.connectMSGType, "已拒绝ip为:" + ip + " 的未配置机具接入系统！", null, ip, PanelMsgEntity.closeState);
                        incomingConnection.close();
                        incomingConnection = null;
                    }
                }
            }
        } catch (BindException e) {
        } catch (IOException ex) {
            System.out.println("" + getListenPort());
        }
    }

    private void showMsg(int busyType, String cmdMsg, String dataMsg, String ip, int state) {
        if (StaticVar.monTcpListen && this.property.getDealPanel() != null) {
            PanelMsgEntity pMsg = new PanelMsgEntity(busyType, cmdMsg, dataMsg, ip, state,PanelMsgEntity.serverType);
            MsgThread mt = new MsgThread();
            mt.setDealPanel(this.property.getDealPanel());
            mt.setpMsg(pMsg);
            SwingUtilities.invokeLater(mt);
        }
    }

    public void stop() {
        StaticVar.monTcpListen = false;
        if (serverSocket != null) {
            if (!serverSocket.isClosed()) {
                try {
                    this.serverSocket.close();
                } catch (IOException ex) {
                    logger.log(Level.INFO, null,ex);
                }
            }
        }
    }

    public void startAccept() {
        acceptConnections();
    }

    public static int readInputStreamWithTimeout(InputStream is, byte[] b, int timeoutMillis)
            throws IOException {
        int bufferOffset = 0;
        long maxTimeMillis = System.currentTimeMillis() + timeoutMillis;
        while (System.currentTimeMillis() < maxTimeMillis && bufferOffset < b.length) {
            int readLength = java.lang.Math.min(is.available(), b.length - bufferOffset);
            // can alternatively use bufferedReader, guarded by isReady():
            int readResult = is.read(b, bufferOffset, readLength);
            if (readResult == -1) {
                break;
            }
            bufferOffset += readResult;
        }
        return bufferOffset;
    }

    /**
     * @return the maxConnections
     */
    public int getMaxConnections() {
        return maxConnections;
    }

    /**
     * @param maxConnections the maxConnections to set
     */
    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    /**
     * @return the listenPort
     */
    public int getListenPort() {
        return listenPort;
    }

    /**
     * @param listenPort the listenPort to set
     */
    public void setListenPort(int listenPort) {
        this.listenPort = listenPort;
    }

    /**
     * @return the serverSocket
     */
    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    /**
     * @param serverSocket the serverSocket to set
     */
    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    /**
     * @return the property
     */
    public FsnComProperty getProperty() {
        return property;
    }

    /**
     * @param property the property to set
     */
    public void setProperty(FsnComProperty property) {
        this.property = property;
    }

}
