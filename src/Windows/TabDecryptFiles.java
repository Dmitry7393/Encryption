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
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.Border;

import AES.Decrypt;

public class TabDecryptFiles extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private JButton btn_decrypt_file = new JButton("Decrypt file");
	private JTextField jtf_key ;
	private GridBagConstraints gbc ;
	private JButton open_file = new JButton("Open file to decrypt");
	private JLabel str_key = new JLabel("Key");
	private JCheckBox checkBoxArchive = new JCheckBox("Save files to archive");

	private  Decrypt decryptFile;
	private long sizeOfSourceFiles = 0;
	
	public TabDecryptFiles() {
		setLayout(new GridBagLayout());

		 JProgressBar progressBar = new JProgressBar();
		    progressBar.setStringPainted(true);
		    progressBar.setPreferredSize(new Dimension(240, 40));
		    progressBar.setMinimum(0);
		    progressBar.setMaximum(100);
		    Border border = BorderFactory.createTitledBorder("Decryption...");
		    progressBar.setBorder(border);
			
			Timer timer = new Timer(100, new ActionListener() {
				  @Override
				  public void actionPerformed(ActionEvent arg0) {
					long valuePercent = (100 * decryptFile.getCommonSizeOfFiles()) / sizeOfSourceFiles ;
					  progressBar.setValue((int) valuePercent);
					  if(decryptFile.threadIsAlive() == false) {
						  ((Timer)arg0.getSource()).stop();
						  JOptionPane.showMessageDialog(null, "File was decrypted!!!");
					  }
				  }
				});
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
			               sizeOfSourceFiles = 0;
			               @SuppressWarnings("unchecked")
						   List<File> droppedFiles = (List<File>)evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
			               JFileChooser f = new JFileChooser();
			               f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
			               f.showSaveDialog(null);
			                    List<String> outputPaths = new ArrayList<String>();
			                   if(f.getSelectedFile() != null)
			                   {
			                	 //Check if some file already exist in that directory
			                	   	boolean allowToSave = true;
			                	   for(int i = 0; i < droppedFiles.size(); i++) {
			                		   File tempFile = new File(f.getSelectedFile() + "/" + droppedFiles.get(i).getName());
			                		   if(tempFile.exists() == true) {
			                			   allowToSave = false;
			                		   }
			                	   }
			                	   if(allowToSave == true) 
			                	   {
				                	   for (File file : droppedFiles) {
				                		   sizeOfSourceFiles = sizeOfSourceFiles + (file.length() - 81980);
						                }
				                	   for(int i = 0; i < droppedFiles.size(); i++) {
				                		   outputPaths.add(f.getSelectedFile() + "/" + droppedFiles.get(i).getName());
				                	    }
			                		   decryptFile = new Decrypt(jtf_key.getText());
				 					   decryptFile.DecryptGroupsOfFiles(droppedFiles, outputPaths);
				 					   timer.start();
			                	   }  else  {
			                		   JOptionPane.showMessageDialog(null, "File already exist in this folder!");
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
		btn_decrypt_file.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				sizeOfSourceFiles = 0;
				File sourceFile = null;
				File outputFile = null;
				JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
				fileChooser.setPreferredSize(new Dimension(1000, 600)); 
				if (fileChooser.showOpenDialog(open_file) == JFileChooser.APPROVE_OPTION) {
				   sourceFile = fileChooser.getSelectedFile();
				   sizeOfSourceFiles = sourceFile.length() - 81980;
				}
				 if (fileChooser.showSaveDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {
					 outputFile = fileChooser.getSelectedFile();
			        }
				 if(sourceFile != null && outputFile != null) {
					 try{
						  decryptFile = new Decrypt(jtf_key.getText());
						  decryptFile.DecryptFile(sourceFile, outputFile.getPath());
						  timer.start();
					  }
					  catch(NullPointerException e){ 
						  System.out.println("NullPointerException in btn_decrypt_file: " + e);
					  }
				 }
			}
	
		});

		gbc.gridx = 0;
		gbc.gridy = 0;
		add(str_key, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		add(jtf_key, gbc);
		
		
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridheight = 2;
		add(panelDecrypt, gbc); 
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		add(btn_decrypt_file, gbc); 
		
		gbc.gridx = 3;
		gbc.gridy = 1;
		add(checkBoxArchive, gbc); 
		
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		add(progressBar, gbc); 
	}
}
