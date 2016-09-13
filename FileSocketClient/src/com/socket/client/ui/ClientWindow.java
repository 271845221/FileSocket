package com.socket.client.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

import com.socket.client.CastClient;
import com.socket.client.FileClient;
import com.socket.client.callback.OnResultListener;
import com.socket.client.callback.ProcessListener;
import com.socket.client.util.SingleThreadUtils;
import com.socket.client.util.UIUtils;
import com.socket.client.util.Uitity;

public class ClientWindow extends JFrame implements ActionListener {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -7315185995108381616L;
	
	private static final String WINDOW_TITLE = "Socket文件上传";
	private static final int WINDOW_WIDTH  = 800;
	private static final int WINDOW_HEIGHT = 600;
	
	private static final String ACTION_SELECT_FILE 		= "ACTION_SELECT_FILE";
	private static final String ACTION_UPLOAD_FILE 		= "ACTION_UPLOAD_FILE";
	private static final String ACTION_HOST_MENU_ITEM 	= "ACTION_HOST_MENU_ITEM";
	
	private JTextField mHostField;
	private JTextField mPortField;
	private JTextField mFileField;
	private JButton mFileButton;
	private JLabel mProcessLabel;
	
	private FileClient mFileClient;
	private CastClient mCastClient;
	
	private Map<String, String> mHost = new TreeMap<String, String> ();
	
	public ClientWindow() {
		initWindow();
		
		addHostUI();
		
		addProcessUI();
		
		addFileUI();
		
		addOperationUI();
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				if (mFileClient != null) {
					mFileClient.quit();
				}
				if (mCastClient != null) {
					mCastClient.quit();
				}
			}
			
			@Override
			public void windowClosed(WindowEvent e) {

			}
			
			@Override
			public void windowActivated(WindowEvent e) {
			}
		});
		mCastClient = new CastClient();
		mCastClient.receiveTimerCast(new OnResultListener<String>() {
			
			@Override
			public void onResult(String t) {
				if (Uitity.isEmpty(t)) {
					return;
				}
				String[] ss = t.split(",");
				mHostField.setText(ss[0]);
				mPortField.setText(ss[1]);
				mHost.put(ss[0], ss[1]);
				
				// actionConfigHost();
				mCastClient.quit();
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
		JPanel hostPanel = new JPanel();
		JLabel hostLabel = new JLabel("主机：");
		hostPanel.add(hostLabel);
		mHostField = new JTextField(30);
		mHostField.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				actionShowHostMenu();
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		hostPanel.add(mHostField);
		
		JLabel portLabel = new JLabel("端口：");
		hostPanel.add(portLabel);
		mPortField = new JTextField(10);
		hostPanel.add(mPortField);
		add(hostPanel);
	}
	
	private void addProcessUI() {
		mProcessLabel = new JLabel("");
		add(mProcessLabel);
	}
	
	private void addFileUI() {
		JPanel filePanel = new JPanel();
		JLabel fileLabel = new JLabel("文件：");
		filePanel.add(fileLabel);
		mFileField = new JTextField(30);
		mFileField.setEditable(false);
		filePanel.add(mFileField);
		mFileButton = new JButton("选择文件");
		mFileButton.setActionCommand(ACTION_SELECT_FILE);
		mFileButton.addActionListener(this);
		filePanel.add(mFileButton);
		add(filePanel);
	}
	
	private void addOperationUI() {
		JButton configButton = new JButton("确认上传");
		configButton.setActionCommand(ACTION_UPLOAD_FILE);
		configButton.addActionListener(this);
		add(configButton);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		if (ACTION_SELECT_FILE.equals(action)) {
			actionSelectFile();
		} else if (ACTION_UPLOAD_FILE.equals(action)) {
			if (checkConfigHost()) {
				actionUploadFile();
			}
		} else if (ACTION_HOST_MENU_ITEM.equals(action)) {
			Object obj = e.getSource();
			if (obj instanceof JMenuItem) {
				JMenuItem item = (JMenuItem) obj;
				String port = mHost.get(item.getText());
				mHostField.setText(item.getText());
				mPortField.setText(port);
			}
		}
	}
	
	private boolean checkConfigHost() {
		if (Uitity.isEmpty(mHostField.getText())) {
			UIUtils.toast(ClientWindow.this, "主机IP/域名不能为空！");
			return false;
		}
		if (Uitity.isEmpty(mPortField.getText())) {
			UIUtils.toast(ClientWindow.this, "主机端口不能为空！");
			return false;
		}
		try {
			Integer.valueOf(mPortField.getText());
		} catch (Exception ex) {
			UIUtils.toast(this, "端口信息格式不对！");
			return false;
		}
		return true;
	}
	
	private void actionSelectFile() {
        JFileChooser jfc = new JFileChooser();  
        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );  
        jfc.showDialog(new JLabel(), "选择");  
        File file = jfc.getSelectedFile();
        if (file != null) {
            if (file.isDirectory()) {
            	UIUtils.toast(this, "请选择文件！");
            } else {
            	mFileField.setText(file.getAbsolutePath());
            }
        }
	}
	
	private void actionUploadFile() {
		setButtonEnabled(false);
		SingleThreadUtils.execute(new Runnable() {
			
			@Override
			public void run() {
				if (mFileClient != null) {
					mFileClient.quit();
					mFileClient = null;
				}
				String host = mHostField.getText();
				int port = Integer.valueOf(mPortField.getText());
				String path = mFileField.getText();
				mFileClient = new FileClient(host, port);
				mFileClient.upload("" + System.nanoTime(), path, new ProcessListener() {
					
					@Override
					public void onResult(boolean isSuccess) {
						setButtonEnabled(true);
						UIUtils.toast(ClientWindow.this, isSuccess ? "上传成功！" : "上传失败！");
					}
					
					@Override
					public void onProcess(long total, long length) {
						setProccessText(total, length);
					}
				});
			}
		});
	}
	
	private void actionShowHostMenu() {
		if (mHost.size() <= 1) {
			return;
		}
		//mHostField.removeAll();
		JPopupMenu menu = new JPopupMenu();
		for (String key : mHost.keySet()) {
			JMenuItem item = new JMenuItem(key);
			item.setActionCommand(ACTION_HOST_MENU_ITEM);
			item.addActionListener(this);
			menu.add(item);
		}
		menu.show(this, mHostField.getX() + 10, mHostField.getY() + mHostField.getHeight() + 30);
	}
	
	private void setProccessText(long total, long length) {
		float p = (float) (length * 100l / length);
		mProcessLabel.setText("上传进度：" + p + "%");
	}
	
	private void setButtonEnabled(boolean enabled) {
		mHostField.setEnabled(enabled);
		mPortField.setEnabled(enabled);
		mFileField.setEnabled(enabled);
		mFileButton.setEnabled(enabled);
	}
}
