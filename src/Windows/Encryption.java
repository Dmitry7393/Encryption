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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import AES.Decrypt;
import AES.Encrypt;

public class Encryption {
	private JLabel str_key = new JLabel("Key");
	private JLabel str_plaintext = new JLabel("Plaintext");
	private JTextField jtf_plaintext ;
	private JTextField jtf_key ;
	private DefaultTableModel model = new DefaultTableModel(); 
	private JTable table = new JTable(model); 
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
		jtf_key.setText("Thats my Kung Fu");
		jtf_plaintext.setText("Two One Nine Two");
		JLabel jlab = new JLabel("message");
		JButton jbtn1 = new JButton("Encrypt");
		JButton jbtn2 = new JButton("Decrypt");
		// Create a couple of columns 
		model.addColumn("Col1"); 
		model.addColumn("Col2"); 
		model.addColumn("Col3"); 
		model.addColumn("Col4"); 
		jbtn1.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0) {
					String str_plain_text = jtf_plaintext.getText();
					String str_key = jtf_key.getText();
					Encrypt a = new Encrypt(str_plain_text, str_key);
					byte[][] r = a.get_ciphertext();
					
					System.out.println("From main class123");
					// Append a row 
					/*StringBuffer strBuffer1 = new StringBuffer();
					StringBuffer strBuffer2 = new StringBuffer();
					StringBuffer strBuffer3 = new StringBuffer();
					StringBuffer strBuffer4 = new StringBuffer();*/
					for(int i = 0; i < 4; i++)
					{
					/*	strBuffer1.setLength(0);
						strBuffer2.setLength(0);
						strBuffer3.setLength(0);
						strBuffer4.setLength(0);
						
						strBuffer1.append(Long.toHexString(r[i][0]).toUpperCase());
						strBuffer2.append(Long.toHexString(r[i][1]).toUpperCase());
						strBuffer3.append(Long.toHexString(r[i][2]).toUpperCase());
						strBuffer4.append(Long.toHexString(r[i][3]).toUpperCase());*/
						
						model.addRow(new Object[]{Integer.toString(r[i][0]), Integer.toString(r[i][1]),
								Integer.toString(r[i][2]), Integer.toString(r[i][3]) });
						/*strBuffer1.setLength(0);
						strBuffer2.setLength(0);
						strBuffer3.setLength(0);
						strBuffer4.setLength(0);*/
						
					}
					jlab.setText(str_key);
					
				}
			});
		jbtn2.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) {
				String plain_text = "";
				for(int j = 0; j < 4; j++) //first - rows
				{
					for(int i = 0; i < 4; i++)
					{
						plain_text = plain_text + (String) table.getModel().getValueAt(i, j) + " ";
					}
				}
				Decrypt d = new Decrypt(plain_text, jtf_key.getText());
				String source_text = d.get_text();
				jlab.setText(source_text);
			}
			
		});
		open_file.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser("D:\\");
				//fileChooser.setCurrentDirectory("D:/");
				if (fileChooser.showOpenDialog(open_file) == JFileChooser.APPROVE_OPTION) {
				  File file = fileChooser.getSelectedFile();
				 String plain_text = read_file(file.getPath());
				 Decrypt d = new Decrypt(plain_text, jtf_key.getText());
					String source_text = d.get_text();
					jlab.setText(source_text);
				}
			}
			
		});
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(str_plaintext, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		panel.add(str_key, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		panel.add(jtf_plaintext, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 1;
		panel.add(jtf_key, gbc);
		
		gbc.gridx = 0;
		gbc.gridy =  2;
		gbc.ipady = 10;
		gbc.ipadx = 10;
		panel.add(jbtn1, gbc);
		gbc.gridx = 1;
		gbc.gridy = 2;
		panel.add(jbtn2, gbc);
		
		gbc.gridx = 2;
		gbc.gridy = 0;
		panel.add(jlab, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 3;
		panel.add(table, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 4;
		panel.add(open_file, gbc);
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
