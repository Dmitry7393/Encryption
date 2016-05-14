package Windows;

import java.awt.AWTException;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

public class Encryption {
	private SystemTray systemTray = SystemTray.getSystemTray();
	private TrayIcon trayIcon;
	private Boolean displayTrayMessage;
	Encryption() throws IOException, URISyntaxException {

		JFrame frame = new JFrame("Encryption");
		frame.setSize(920, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setLocation(100, 100);

		JTabbedPane tabbedPane = new JTabbedPane();

		tabbedPane.addTab("Encrypt text", new TabEncryptText(frame.getLocation().x, frame.getLocation().y));
		tabbedPane.addTab("Encrypt files", new TabEncryptFiles());
		tabbedPane.addTab("Decrypt files", new TabDecryptFiles());
		tabbedPane.addTab("RSA", new TabRSA(frame.getLocation().x, frame.getLocation().y));
		frame.add(tabbedPane);
		frame.setVisible(true);

		// Create a temporary file
		File tempFileImageLock = null;
		try {
			tempFileImageLock = File.createTempFile("imgtrlock", ".tmp");
			tempFileImageLock.deleteOnExit();
		} catch (IOException e) {
			e.printStackTrace();
		}

		FileOutputStream fos = new FileOutputStream(tempFileImageLock.getAbsolutePath());
		int value = 0;
		InputStream inputStreamImage = Encryption.class.getResourceAsStream("/resources/image_tray_lock4.png");
		while ((value = inputStreamImage.read()) != -1) {
			fos.write((byte) value);
		}
		fos.close();
		inputStreamImage.close();

		trayIcon = new TrayIcon(ImageIO.read(tempFileImageLock), "Encryption");

		trayIcon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(true);
				frame.setState(JFrame.NORMAL);
				removeTrayIcon();
			}
		});
		displayTrayMessage = true;

		frame.addWindowStateListener(new WindowStateListener() {
			public void windowStateChanged(WindowEvent e) {
				if (e.getNewState() == JFrame.ICONIFIED) {
					frame.setVisible(false);
					addTrayIcon();
				}
			}
		});
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					new Encryption();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	private void removeTrayIcon() {
		systemTray.remove(trayIcon);
	}

	private void addTrayIcon() {
		try {
			systemTray.add(trayIcon);
			if(displayTrayMessage) {
				trayIcon.displayMessage("Encryption", "Window minimised to tray, double click to show",
						TrayIcon.MessageType.INFO);
				displayTrayMessage = false;
			}
		
		} catch (AWTException ex) {
			ex.printStackTrace();
		}
	}
}
