package Windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import AES.Decrypt;
import AES.Encrypt;

public class TabEncryptFiles extends JPanel {
	private static final long serialVersionUID = 1L;
	private JButton btn_encrypt_file = new JButton("Encrypt file");
	private JButton btn_decrypt_file = new JButton("Decrypt file");
	private JTextField jtf_key ;
	private GridBagConstraints gbc ;
	private JButton open_file = new JButton("Open file to decrypt");
	private JLabel str_key = new JLabel("Key");
	private JCheckBox checkBoxArchive = new JCheckBox("Save files to archive");
	public TabEncryptFiles() {
		
		setLayout(new GridBagLayout());
		JPanel panelEncrypt = new JPanel();
		panelEncrypt.setLayout(new BoxLayout(panelEncrypt, BoxLayout.X_AXIS ));
		
		panelEncrypt.setPreferredSize(new Dimension(240,100));
		panelEncrypt.setVisible(true);
		
		panelEncrypt.add(Box.createHorizontalGlue());
		panelEncrypt.add(new JLabel("Drag files here to encrypt them"));
		panelEncrypt.add(Box.createHorizontalGlue());
		
		panelEncrypt.setDropTarget(new DropTarget() {
			   private static final long serialVersionUID = 1L;
			   public synchronized void drop(DropTargetDropEvent evt) {
			          try {
			               evt.acceptDrop(DnDConstants.ACTION_COPY);
			               @SuppressWarnings("unchecked")
						List<File> droppedFiles = (List<File>)evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
			               JFileChooser f = new JFileChooser();
			                    f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
			                    f.showSaveDialog(null);
			                   if(f.getSelectedFile() != null)
			                   {
			                	   for (File file : droppedFiles) {
					                	Encrypt encryptFile = new Encrypt(jtf_key.getText());
					                  encryptFile.EncryptFile(jtf_key.getText(), file, f.getSelectedFile() + "/" + file.getName() + "_encrypted.png");
					                  //Waiting when previous file will be encrypted
					                }
			                   }
			          } catch (Exception ex) {
			              ex.printStackTrace();
			          }
			      }
			  });
		panelEncrypt.setBorder(BorderFactory.createLineBorder(Color.RED, 2, true));
		JPanel panelDecrypt = new JPanel();
		
		panelDecrypt.setLayout(new BoxLayout(panelDecrypt, BoxLayout.X_AXIS ));
		
		panelDecrypt.setPreferredSize(new Dimension(240,100));
		panelDecrypt.setVisible(true);
		
		panelDecrypt.add(Box.createHorizontalGlue());
		panelDecrypt.add(new JLabel("Drag files here to decrypt them"));
		panelDecrypt.add(Box.createHorizontalGlue());
		
		panelDecrypt.setDropTarget(new DropTarget() {
			   private static final long serialVersionUID = 1L;
			   public synchronized void drop(DropTargetDropEvent evt) {
			          try {
			               evt.acceptDrop(DnDConstants.ACTION_COPY);
			               @SuppressWarnings("unchecked")
						List<File> droppedFiles = (List<File>)evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
			               JFileChooser f = new JFileChooser();
			                    f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
			                    f.showSaveDialog(null);
			                   if(f.getSelectedFile() != null)
			                   {
			                	   for (File file : droppedFiles) 
			                	   {
					                	Decrypt decryptFile = new Decrypt(jtf_key.getText());
					                	decryptFile.DecryptFile(jtf_key.getText(), file, f.getSelectedFile() + "/" + file.getName());
					                }
			                   }
			          } catch (Exception ex) {
			              ex.printStackTrace();
			          }
			      }
			  });
		panelDecrypt.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2, true));
				
		
	    gbc = new GridBagConstraints();
	    gbc.insets = new Insets(0,20,20,0);
	    gbc.anchor = GridBagConstraints.NORTHWEST;
	
	    jtf_key = new JTextField(20);
		
		jtf_key.setText("Thats my Kung Fu");

		btn_encrypt_file.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0) {
					File sourceFile = null;
					String resultFilePath = "";
					JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
					
					if (fileChooser.showOpenDialog(open_file) == JFileChooser.APPROVE_OPTION) {
					   sourceFile = fileChooser.getSelectedFile();
					}
					 if (fileChooser.showSaveDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {
				  		    File file = fileChooser.getSelectedFile();
				  		  resultFilePath = file.getPath();
				        }
					 if(sourceFile != null && resultFilePath != null) {
						 try
						  {
							  Encrypt encryptFile = new Encrypt(jtf_key.getText());
							  encryptFile.EncryptFile(jtf_key.getText(), sourceFile, resultFilePath);
							  System.out.println("continue"); 
						  }
						  catch(NullPointerException e){ 
							  System.out.println("NullPointerException: " + e);
						  }
					 }
					 
				}
			});
		btn_decrypt_file.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				File sourceFile = null;
				String resultFilePath = "";
				JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
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

		gbc.gridx = 0;
		gbc.gridy = 0;
		add(str_key, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		add(jtf_key, gbc);
		
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.gridheight = 3;
		add(panelEncrypt, gbc);
		
		gbc.gridx = 2;
		gbc.gridy = 3;
		add(panelDecrypt, gbc); 
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		add(btn_encrypt_file, gbc); 
		
		gbc.gridx = 1;
		gbc.gridy = 1;
		add(btn_decrypt_file, gbc); 
		
		gbc.gridx = 3;
		gbc.gridy = 1;
		add(checkBoxArchive, gbc); 
		
	}
}
