package Windows;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import AES.Decrypt;
import AES.Encrypt;
import Image.Encrypt_in_image;

public class Encryption  {
	private JButton btn_encrypt = new JButton("Encrypt");
	private JButton btn_decrypt = new JButton("Decrypt");
	private JButton btn_encrypt_image = new JButton("Image");
	private JLabel str_key = new JLabel("Key");
	private JLabel str_plaintext = new JLabel("Plaintext");
	private JLabel str_ciphertext = new JLabel("Ciphertext");
	private JTextField jtf_plaintext ;
	private JTextField jtf_key ;
	private JTextField jtf_ciphertext ;
	private JTextArea textArea = new JTextArea (5, 20);
	private JButton open_file = new JButton("Open file to decrypt");
	private GridBagConstraints gbc ;
	private  JLabel picLabel = new JLabel();
	private Image image_original;
	Boolean show_image = false;

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
		   jfrm.setSize(720, 500);
		   jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		   jtf_key = new JTextField(12);
		   jtf_plaintext = new JTextField(12);
		   jtf_ciphertext = new JTextField(28);
		jtf_key.setText("Thats my Kung Fu");
		jtf_plaintext.setText("Two One Nine Two");
		JLabel jlab = new JLabel("_________");
		 ImageIcon ii = new ImageIcon("D:/rrr.png");
		 image_original = ii.getImage();
		btn_encrypt.addActionListener(new ActionListener()
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
		btn_decrypt.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) {
				String plain_text = jtf_ciphertext.getText();
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
		btn_encrypt_image.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser("D:\\");
				if (fileChooser.showOpenDialog(open_file) == JFileChooser.APPROVE_OPTION) {
				  File file = fileChooser.getSelectedFile();
				  ImageIcon icon = new ImageIcon(file.getPath());
				  Image img = icon.getImage();
				  Image newimg = img.getScaledInstance(100, 100,  java.awt.Image.SCALE_SMOOTH);
				  icon = new ImageIcon(newimg);
					picLabel.setIcon(icon);
				try {
					Encrypt_in_image en = new Encrypt_in_image(file.getPath(), "gfjhgj");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
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
		
		gbc.gridx = 1;
		gbc.gridy = 4;
		panel.add(textArea, gbc);
		//////////

			gbc.gridx = 1;
			gbc.gridy = 5;
			panel.add(picLabel, gbc);
	

		
		gbc.gridx = 0;
		gbc.gridy = 5;
		panel.add(btn_encrypt_image, gbc);
		   jfrm.add(panel);
		jfrm.setLocation(300, 150);
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
					// TODO Auto-generated catch block
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
