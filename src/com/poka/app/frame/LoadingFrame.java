package com.poka.app.frame;

import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class LoadingFrame extends JFrame implements Runnable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8236850107130802140L;
	private static LoadingFrame loadingFrame;

	public static LoadingFrame instance() {
		if (loadingFrame == null)
			loadingFrame = new LoadingFrame();
		return loadingFrame;
	}

	public LoadingFrame() {
		super("宝嘉冠字号采集系统");
		setSize(400, 234);
		loadingFrame = this;
		setUndecorated(true);
		//setAlwaysOnTop(true);
		setLocationRelativeTo(null);
//		AWTUtilities.setWindowOpaque(this, false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImage(new ImageIcon(this.getClass().getResource(
				"/com/poka/images/poka.jpg")).getImage());
		setVisible(true);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		ImageIcon bg = new ImageIcon(this.getClass().getResource(
				"/com/poka/images/loading.png"));
		g.drawImage(bg.getImage(), 0, 0, getWidth(), getHeight(), this);
	}

	@Override
	public void run() {
		instance();
	}
}
