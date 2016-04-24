package Windows;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import AES.Decrypt;
import AES.Encrypt;
import Image.DecryptFromImage;
import Image.EncryptInImage;

public class TabEncryptText extends JPanel {
	private static final long serialVersionUID = 1L;
	private JButton btn_encrypt = new JButton("Encrypt");
	private JButton btn_decrypt = new JButton("Decrypt");
	private JTextArea plainTextArea = new JTextArea(5, 20); 
	private JTextArea cipherTextArea = new JTextArea (5, 20);
	private JLabel str_key = new JLabel("Key");
	private JLabel str_plaintext = new JLabel("Plaintext");
	private JLabel str_ciphertext = new JLabel("Ciphertext");
	private JTextField jtf_key ;
	private JTextField jtf_ciphertext ;
	private GridBagConstraints gbc ;
	private JButton btn_encrypt_image = new JButton("Save ciphertext in image");
	private JButton btn_upload_image = new JButton("Upload image");
	private JButton btnGetCiphertextFromImage = new JButton("Get ciphertext from image");
	private ImageIcon imageIconOriginal;
	private String pathOriginalImage = "";
	private  JLabel picLabel_original = new JLabel();
	private  JLabel picLabel_decrypted = new JLabel();
	private JButton open_file = new JButton("Open file to decrypt");
	
	TabEncryptText(int frameLocationX, int frameLocationY) {
		setLayout(new GridBagLayout());
		plainTextArea.setText("Two One Nine Two");
		 
			JScrollPane scrollPlainTextArea = new JScrollPane (plainTextArea);  
			//scroll.setBounds(100, 10, 100, 10);
			scrollPlainTextArea.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			scrollPlainTextArea.setVisible(true);

			JScrollPane scrollCiphertextArea = new JScrollPane (cipherTextArea);  
			scrollCiphertextArea.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			scrollCiphertextArea.setVisible(true);
		    jtf_key = new JTextField(20);
			jtf_key.setText("Thats my Kung Fu");
			jtf_ciphertext = new JTextField(20);
	    gbc = new GridBagConstraints();
	    gbc.insets = new Insets(0,20,20,0);
	    gbc.anchor = GridBagConstraints.NORTHWEST;
		btn_encrypt.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				String str_plain_text = plainTextArea.getText();
				String str_key = jtf_key.getText();
				Encrypt encrText = new Encrypt(str_key);
				encrText.EncryptText(str_plain_text);
				String cipher_text = encrText.getCipherText();
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
				Decrypt decrypt = new Decrypt(jtf_key.getText());
				decrypt.DecryptText(plain_text, jtf_key.getText());
				cipherTextArea.setText("");
				cipherTextArea.setText(decrypt.get_text());
			}
			
		}
	});
	btn_upload_image.addActionListener(new ActionListener()  //Just open image
			{
				public void actionPerformed(ActionEvent arg0) {
					JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
					if (fileChooser.showOpenDialog(open_file) == JFileChooser.APPROVE_OPTION) {
						  File file = fileChooser.getSelectedFile();
						  pathOriginalImage = file.getPath();
						  ImageIcon imageIcon = new ImageIcon(file.getPath());
						  imageIconOriginal = imageIcon; //save the original image
						  int widthIcon = 60;
						  double heightIcon = (widthIcon * imageIcon.getIconHeight()) / imageIcon.getIconWidth();
						  Image img = imageIcon.getImage();
						  img = img.getScaledInstance(widthIcon, (int) heightIcon,  java.awt.Image.SCALE_SMOOTH);
						  imageIcon = new ImageIcon(img);
						  picLabel_original.setIcon(imageIcon);
						}
				}
			});
	btn_encrypt_image.addActionListener(new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0) {
			String ciphertextBase64 = jtf_ciphertext.getText();
			if(ciphertextBase64.compareTo("") == 0)
			{
				JOptionPane.showMessageDialog(null, "You have to first encrypt text");
			}
			else
			{
				if(pathOriginalImage.compareTo("") == 0)
				{
					JOptionPane.showMessageDialog(null, "Upload image!");
				}
				else
				{
					 File file1 = null;  //Output file
					 JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
					 if (fileChooser.showSaveDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {
				  		     file1 = fileChooser.getSelectedFile();
				        }
					EncryptInImage encryptInImage = new EncryptInImage(pathOriginalImage);
					if(encryptInImage.writeBytesToImage(ciphertextBase64) == true){
							encryptInImage.saveImage(file1.getPath());
					} else {
						JOptionPane.showMessageDialog(null, "Please, choose more large picture "
							    + "which has at least " + Integer.toString(encryptInImage.getProperCountPixels())
							    + " pixels");
					}
				}	
			}
		}
	});
	picLabel_original.addMouseListener(new MouseAdapter()
	{
	    public void mouseClicked(MouseEvent e) 
	    {
	    	  int preferredWidth = 600;
	    	  double preferredHeight = (preferredWidth * imageIconOriginal.getIconHeight()) / imageIconOriginal.getIconWidth();
			  ViewCourseGUI viewCourseGUI = new ViewCourseGUI(preferredWidth, (int) preferredHeight);
			  viewCourseGUI.setLocation(frameLocationX+100, frameLocationY+50);
			  viewCourseGUI.setPicture(imageIconOriginal);
	    }
	});
	btnGetCiphertextFromImage.addActionListener(new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0) {
			JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
			fileChooser.setAcceptAllFileFilterUsed(false);
			if (fileChooser.showOpenDialog(open_file) == JFileChooser.APPROVE_OPTION) {
			  File file = fileChooser.getSelectedFile();
			  DecryptFromImage dFrImage = new DecryptFromImage(file.getPath());
			  jtf_ciphertext.setText(dFrImage.getCiphertext());
			}
		}
	});
	gbc.gridx = 0;
	gbc.gridy = 0;
	add(str_plaintext, gbc);
	
	gbc.gridx = 1;
	gbc.gridy = 0;
	add(scrollPlainTextArea, gbc);
	
	//Second row
	gbc.gridx = 0;
	gbc.gridy = 1;
	add(str_key, gbc);
	
	gbc.gridx = 1;
	gbc.gridy = 1;
	add(jtf_key, gbc);
	//3
	gbc.gridx = 0;
	gbc.gridy = 2;
	add(str_ciphertext, gbc);
	
	gbc.gridx = 1;
	gbc.gridy = 2;
	add(jtf_ciphertext, gbc);
	
	//4
	gbc.gridx = 0;
	gbc.gridy =  3;
	add(btn_encrypt, gbc);
	
	gbc.gridx = 1;
	gbc.gridy = 3;
	add(btn_decrypt, gbc);
	
	gbc.gridx = 2;
	gbc.gridy = 0;
	add(scrollCiphertextArea, gbc);
	
	gbc.gridx = 1;
	gbc.gridy = 5;
	add(picLabel_original, gbc);

	gbc.gridx = 0;
	gbc.gridy = 5;
	add(btn_upload_image, gbc);
	
	gbc.gridx = 0;
	gbc.gridy = 6;
	add(btn_encrypt_image, gbc); 
	
	gbc.gridx = 1;
	gbc.gridy = 6;
	add(btnGetCiphertextFromImage, gbc);
	
	gbc.gridx = 2;
	gbc.gridy = 5;
	add(picLabel_decrypted, gbc);
	}
}
