package Windows;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import AES.Decrypt;
import AES.Encrypt;
import Image.Decrypt_image;
import Image.Encrypt_in_image;

public class Encryption  {
	private JButton btn_encrypt = new JButton("Encrypt");
	private JButton btn_decrypt = new JButton("Decrypt");
	private JButton btn_encrypt_image = new JButton("Save to image");
	private JButton btn_open_image = new JButton("Open original");
	private JButton btn_decrypt_images = new JButton("Decrypt images");
	private JButton btn_encrypt_file = new JButton("Encrypt file");
	private JButton btn_decrypt_file = new JButton("Decrypt file");
	private JLabel str_key = new JLabel("Key");
	private JLabel str_plaintext = new JLabel("Plaintext");
	private JLabel str_ciphertext = new JLabel("Ciphertext");
	private JTextField jtf_plaintext ;
	private JTextField jtf_key ;
	private JTextField jtf_ciphertext ;
	//private JTextArea textArea = new JTextArea (5, 5);
	private JButton open_file = new JButton("Open file to decrypt");
	private GridBagConstraints gbc ;
	private  JLabel picLabel_original = new JLabel();
	private  JLabel picLabel_decrypted = new JLabel();
	//private JTabbedPane  tab = new JTabbedPane();
	private String path_picture_original = "";
	private String path_picture_decrypted = "";
	 Encryption() throws IOException 
	{
		 
		// image_original = ImageIO.read(new File("D:/image.png")).getScaledInstance(100, 100, Image.SCALE_FAST);
			
		// picLabel	= new JLabel(new ImageIcon(image_original));
		/* picLabel.setSize(100, 100);
		 Dimension maximumSize = new Dimension(100, 100);
		 
		 picLabel.setMaximumSize(maximumSize);
		 picLabel.setPreferredSize(maximumSize);*/
		JPanel panel = new JPanel(new GridBagLayout());
	    gbc = new GridBagConstraints();
	    gbc.insets = new Insets(0,20,20,0);
	  //  gbc.weightx = 1.0;
	    gbc.anchor = GridBagConstraints.NORTHWEST;
	  // gbc.gridwidth = GridBagConstraints.REMAINDER;
	    
	    JFrame jfrm = new JFrame("Encryption");
		   jfrm.setSize(920, 500);
		   jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		   jtf_key = new JTextField(20);
		   jtf_plaintext = new JTextField(20);
		   jtf_ciphertext = new JTextField(20);
		jtf_key.setText("Thats my Kung Fu");
		jtf_plaintext.setText("Two One Nine Two");
		JLabel jlab = new JLabel("_________");
		btn_encrypt.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0) {
					String str_plain_text = jtf_plaintext.getText();
					String str_key = jtf_key.getText();
					Encrypt encrText = new Encrypt(str_key);
					encrText.EncryptText(str_plain_text, str_key);
					String cipher_text = encrText.get_ciphertext();
					jtf_ciphertext.setText(cipher_text);
				}
			});
		btn_decrypt.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) {
				if(jtf_ciphertext.getText().equals(""))
				{
					JOptionPane.showMessageDialog(null, "Enter ciphertext first!");
				}
				else
				{
					String plain_text = jtf_ciphertext.getText();
					Decrypt d = new Decrypt(jtf_key.getText());
					d.DecryptText(plain_text, jtf_key.getText());
					String source_text = d.get_text();
					jlab.setText(source_text);
				}
				
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
				// textArea.setText(plain_text);
				}
			}
		});
		
		btn_open_image.addActionListener(new ActionListener()  //Just open image
		{
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser("D:\\");
				if (fileChooser.showOpenDialog(open_file) == JFileChooser.APPROVE_OPTION) {
				  File file = fileChooser.getSelectedFile();
				  path_picture_original = file.getPath();
				  ImageIcon icon = new ImageIcon(file.getPath());
				  Image img = icon.getImage();
				  Image newimg = img.getScaledInstance(100, 100,  java.awt.Image.SCALE_SMOOTH);
				  icon = new ImageIcon(newimg);
				  picLabel_original.setIcon(icon);
					}
			}
		});
		btn_encrypt_image.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0) {
				String ciphertext = jtf_ciphertext.getText();
				if(ciphertext.compareTo("") == 0)
				{
					JOptionPane.showMessageDialog(null, "You have to first encrypt text");
				}
				else
				{
					if(path_picture_original.compareTo("") == 0)
					{
						JOptionPane.showMessageDialog(null, "Upload image!");
					}
					else
					{
						try 
						{
							Encrypt_in_image en = new Encrypt_in_image();
							en.EncryptInImage(path_picture_original, ciphertext);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					
				}
			}
		});
		btn_decrypt_images.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0) {
				if(path_picture_original.compareTo("") == 0)
				{
					JOptionPane.showMessageDialog(null, "Upload the original image");
				}
				else
				{
					String cf = "";
					JFileChooser fileChooser = new JFileChooser("D:\\");
					if (fileChooser.showOpenDialog(open_file) == JFileChooser.APPROVE_OPTION) {
					  File file = fileChooser.getSelectedFile();
					  path_picture_decrypted = file.getPath();
					  System.out.println("path_picture_decrypted " + path_picture_decrypted);
					  ImageIcon icon = new ImageIcon(file.getPath());
					  Image img = icon.getImage();
					  Image newimg = img.getScaledInstance(100, 100,  java.awt.Image.SCALE_SMOOTH);
					  icon = new ImageIcon(newimg);
					  picLabel_decrypted.setIcon(icon);
					  
					  Decrypt_image dc = new Decrypt_image();
					  try {
						 cf = dc.get_text(path_picture_original, path_picture_decrypted);
					} catch (IOException e) {
						e.printStackTrace();
					}
					  jtf_ciphertext.setText(cf);
					}
				}
				
			}
		});
		btn_encrypt_file.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0) {
					File sourceFile = null;
					String resultFilePath = "";
					JFileChooser fileChooser = new JFileChooser("D:\\");
					if (fileChooser.showOpenDialog(open_file) == JFileChooser.APPROVE_OPTION) {
					   sourceFile = fileChooser.getSelectedFile();
					}
					 if (fileChooser.showSaveDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {
				  		    File file = fileChooser.getSelectedFile();
				  		  resultFilePath = file.getPath();
				        }
					  try
					  {
						  Encrypt encryptFile = new Encrypt(jtf_key.getText());
						  encryptFile.EncryptFile(jtf_key.getText(), sourceFile, resultFilePath);
						  
					  }
					  catch(NullPointerException e){ 
						  System.out.println("NullPointerException: " + e);
					  }
				}
		
			});
		btn_decrypt_file.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				File sourceFile = null;
				String resultFilePath = "";
				JFileChooser fileChooser = new JFileChooser("D:\\");
				if (fileChooser.showOpenDialog(open_file) == JFileChooser.APPROVE_OPTION) {
				   sourceFile = fileChooser.getSelectedFile();
				}
				 if (fileChooser.showSaveDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {
			  		    File file = fileChooser.getSelectedFile();
			  		  resultFilePath = file.getPath();
			        }
				  try
				  {
					  Decrypt decryptFile = new Decrypt(jtf_key.getText());
					  decryptFile.DecryptFile(jtf_key.getText(), sourceFile, resultFilePath);
				  }
				  catch(NullPointerException e){ 
					  System.out.println("NullPointerException: " + e);
				  }
			}
	
		});
			//textArea.setSize(100,100);  
		  //  textArea.setEditable(false);
		    //textArea.setVisible(true);
		//    JScrollPane scroll = new JScrollPane (textArea);
	//	    scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		   //       scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		//First row
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(str_plaintext, gbc); //
		
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
		//gbc.ipady = 10;
		//gbc.ipadx = 10;
		panel.add(btn_encrypt, gbc);
		gbc.gridx = 1;
		gbc.gridy = 3;
		panel.add(btn_decrypt, gbc);
		
		gbc.gridx = 2;
		gbc.gridy = 0;
		panel.add(jlab, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 4;
		panel.add(open_file, gbc);
		
		//gbc.gridx = 1;
		//gbc.gridy = 4;
		//panel.add(textArea, gbc);
		//////////

			gbc.gridx = 1;
			gbc.gridy = 5;
			panel.add(picLabel_original, gbc);
	

		
		gbc.gridx = 0;
		gbc.gridy = 5;
		panel.add(btn_open_image, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 6;
		panel.add(btn_encrypt_image, gbc); 
		
		gbc.gridx = 1;
		gbc.gridy = 6;
		panel.add(btn_decrypt_images, gbc);
		
		gbc.gridx = 2;
		gbc.gridy = 5;
		panel.add(picLabel_decrypted, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 7;
		panel.add(btn_encrypt_file, gbc); 
		
		gbc.gridx = 1;
		gbc.gridy = 7;
		panel.add(btn_decrypt_file, gbc); 
		
		   jfrm.add(panel);
		jfrm.setLocation(100, 100);
		jfrm.setVisible(true);
	}
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				try {
					new Encryption();
				} catch (IOException e) {
					e.printStackTrace();
				}
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
