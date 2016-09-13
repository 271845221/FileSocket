package com.socket.server.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.socket.server.CastServer;
import com.socket.server.FileServer;
import com.socket.server.util.UIUtils;
import com.socket.server.util.Uitity;

public class ServerWindow extends JFrame implements ActionListener {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -7465792743452538252L;
	
	private static final String WINDOW_TITLE = "Socket文件Server";
	private static final int WINDOW_WIDTH  = 800;
	private static final int WINDOW_HEIGHT = 600;
	private static final String ACTION_SELECT_FILE 	= "ACTION_SELECT_FILE";
	private static final String ACTION_START_SERVER = "ACTION_START_SERVER";
	
	private JTextField mFileField;
	private JTextField mPortField;
	private JButton mSelectFileButton;
	private JButton mStartServerButton;
	private FileServer mServer;
	private CastServer mCastServer;
	private int mPort = 7878;
	
	public ServerWindow() {
		setTitle(WINDOW_TITLE);
		initWindow();
		addHostUI();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addWindowListener(new WindowListener() {
			
			public void windowOpened(WindowEvent e) {
			}
			
			public void windowIconified(WindowEvent e) {
			}
			
			public void windowDeiconified(WindowEvent e) {
			}
			
			public void windowDeactivated(WindowEvent e) {
			}
			
			public void windowClosing(WindowEvent e) {
				if (mServer != null) {
					mServer.quit();
				}
				if (mCastServer != null) {
					mCastServer.quit();
				}
			}
			
			public void windowClosed(WindowEvent e) {
			}
			
			public void windowActivated(WindowEvent e) {
			}
		});

	}
	
	private void initWindow() {
		setTitle(WINDOW_TITLE);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		int[] scrren = UIUtils.getScreenDisplay();
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setLocation((scrren[0] - WINDOW_WIDTH) / 2, (scrren[1] - WINDOW_HEIGHT) / 2);
	}
	
	private void addHostUI() {
		JPanel infoPanel = new JPanel();
		
		JLabel portLabel = new JLabel("端口：");
		infoPanel.add(portLabel);
		mPortField = new JTextField(10);
		mPortField.setText("" + mPort);
		infoPanel.add(mPortField);
		
		JLabel fileLabel = new JLabel("文件保存目录：");
		infoPanel.add(fileLabel);
		mFileField = new JTextField(30);
		infoPanel.add(mFileField);
		
		mSelectFileButton = new JButton("选择目录");
		mSelectFileButton.setActionCommand(ACTION_SELECT_FILE);
		mSelectFileButton.addActionListener(this);
		infoPanel.add(mSelectFileButton);
		add(infoPanel);
		
		mStartServerButton = new JButton("启动Server");
		mStartServerButton.setActionCommand(ACTION_START_SERVER);
		mStartServerButton.addActionListener(this);
		add(mStartServerButton);
	}
	
	public void actionPerformed(ActionEvent e) {
		final String action = e.getActionCommand();
		if (ACTION_SELECT_FILE.equals(action)) {
			actionSelectFile();
		} else if (ACTION_START_SERVER.equals(action)) {
			actionStartServer();
		}
	}
	
	private void actionSelectFile() {
        JFileChooser jfc = new JFileChooser();  
        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );  
        jfc.showDialog(new JLabel(), "选择文件保存目录");  
        File file = jfc.getSelectedFile();
        if (file != null) {
            if (file.isDirectory()) {
            	mFileField.setText(file.getAbsolutePath());
            } else {
            	UIUtils.toast(this, "请选择目录！");
            }
        }
	}
	
	private void actionStartServer() {
		if (Uitity.isEmpty(mPortField.getText())) {
			UIUtils.toast(this, "端口不能为空！");
			return;
		}
		try {
			mPort = Integer.parseInt(mPortField.getText());
		} catch (Exception e) {
			UIUtils.toast(this, "端口信息格式不对！");
			return;
		}
		final String path = mFileField.getText();
		if (Uitity.isEmpty(path)) {
			UIUtils.toast(this, "文件保存目录不能为空！");
			return;
		}
		mPortField.setEnabled(false);
		mFileField.setEnabled(false);
		mSelectFileButton.setEnabled(false);
		mStartServerButton.setEnabled(false);
		
		mServer = new FileServer(mPort, path);
		mServer.start();
		
		mCastServer = new CastServer();
		String msg = Uitity.gitLocalIP() + "," + mPort;
		mCastServer.sendTimerCast(msg.getBytes());
		mStartServerButton.setEnabled(false);
	}
	
}
