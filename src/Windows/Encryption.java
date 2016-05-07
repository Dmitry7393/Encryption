package Windows;

import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

public class Encryption {
	Encryption() throws IOException {
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
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					new Encryption();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
	/*
	 * private static String read_file(String path) { BufferedReader br = null;
	 * String source_string = ""; try { String current_line = ""; br = new
	 * BufferedReader(new FileReader(path)); while ((current_line =
	 * br.readLine()) != null) { System.out.println("input1: " + current_line);
	 * source_string = current_line; } } catch (IOException e) {
	 * e.printStackTrace(); } finally { try { if (br != null) br.close(); }
	 * catch (IOException ex) { ex.printStackTrace(); } } return source_string;
	 * }
	 */
}
