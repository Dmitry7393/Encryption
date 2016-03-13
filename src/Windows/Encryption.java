package Windows;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import AES.Decrypt;
import AES.Encrypt;

public class Encryption {
	private JLabel str_key = new JLabel("Key");
	private JLabel str_plaintext = new JLabel("Plaintext");
	private JLabel str_ciphertext = new JLabel("Ciphertext");
	private JTextField jtf_plaintext ;
	private JTextField jtf_key ;
	private JTextField jtf_ciphertext ;
	private JTextArea textArea = new JTextArea (5, 20);
	private JButton open_file = new JButton("Open file to decrypt");
	  GridBagConstraints gbc ;
	 Encryption()
	{
		JPanel panel = new JPanel(new GridBagLayout());
	    gbc = new GridBagConstraints();
	    gbc.insets = new Insets(0,20,20,0);
	  //  gbc.weightx = 1.0;
	    gbc.anchor = GridBagConstraints.NORTHWEST;
	  // gbc.gridwidth = GridBagConstraints.REMAINDER;
	    
	    JFrame jfrm = new JFrame("Encryption");
		   jfrm.setSize(720, 400);
		   jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		   jtf_key = new JTextField(12);
		   jtf_plaintext = new JTextField(12);
		   jtf_ciphertext = new JTextField(28);
		jtf_key.setText("Thats my Kung Fu");
		jtf_plaintext.setText("Two One Nine Two");
		JLabel jlab = new JLabel("_________");
		JButton jbtn1 = new JButton("Encrypt");
		JButton jbtn2 = new JButton("Decrypt");
		jbtn1.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0) {
					String str_plain_text = jtf_plaintext.getText();
					String str_key = jtf_key.getText();
					Encrypt a = new Encrypt(str_plain_text, str_key);
					byte[][] r = a.get_ciphertext();
					
					System.out.println("From main class123");
					String cipher_text = "";
					for(int j = 0; j < 4; j++)
					{
						for(int i = 0; i < 4; i++)
						{
							cipher_text = cipher_text + r[i][j] + " ";
						}
					}
					jtf_ciphertext.setText(cipher_text);
				}
			});
		jbtn2.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) {
				String plain_text = jtf_ciphertext.getText();
			/*	for(int j = 0; j < 4; j++) //first - rows
				{
					for(int i = 0; i < 4; i++)
					{
						plain_text = plain_text + (String) table.getModel().getValueAt(i, j) + " ";
					}
				}*/
				Decrypt d = new Decrypt(plain_text, jtf_key.getText());
				String source_text = d.get_text();
				jlab.setText(source_text);
			}
			
		});
		open_file.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser("D:\\");
				if (fileChooser.showOpenDialog(open_file) == JFileChooser.APPROVE_OPTION) {
				  File file = fileChooser.getSelectedFile();
				 String plain_text = read_file(file.getPath());
				 jtf_ciphertext.setText(plain_text);
				 textArea.setText(plain_text);
				}
			}
			
		});
		 textArea.setSize(100,100);  
		    textArea.setEditable(false);
		    textArea.setVisible(true);
		    JScrollPane scroll = new JScrollPane (textArea);
		    scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		          scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		//First row
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(str_plaintext, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		panel.add(jtf_plaintext, gbc);
		
		//Second row
		gbc.gridx = 0;
		gbc.gridy = 1;
		panel.add(str_key, gbc);
		gbc.gridx = 1;
		gbc.gridy = 1;
		panel.add(jtf_key, gbc);
		
		//3
		gbc.gridx = 0;
		gbc.gridy = 2;
		panel.add(str_ciphertext, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 2;
		panel.add(jtf_ciphertext, gbc);
		
		//4
		
		gbc.gridx = 0;
		gbc.gridy =  3;
		gbc.ipady = 10;
		gbc.ipadx = 10;
		panel.add(jbtn1, gbc);
		gbc.gridx = 1;
		gbc.gridy = 3;
		panel.add(jbtn2, gbc);
		
		gbc.gridx = 2;
		gbc.gridy = 0;
		panel.add(jlab, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 4;
		panel.add(open_file, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 4;
		panel.add(textArea, gbc);
		   jfrm.add(panel);
		jfrm.setVisible(true);
	}
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				new Encryption();
			}
		});
	}
	private static String read_file(String path)
    {
		BufferedReader br = null;
		String source_string = "";
		try {
			String current_line = "";
			br = new BufferedReader(new FileReader(path));
			while ((current_line = br.readLine()) != null) 
			{
				System.out.println("input1: " + current_line);
				source_string = current_line;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return source_string;
    }

}
