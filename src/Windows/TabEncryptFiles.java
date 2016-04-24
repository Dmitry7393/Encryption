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
import AES.Encrypt;

public class TabEncryptFiles extends JPanel {
	private static final long serialVersionUID = 1L;
	private JButton btn_encrypt_file = new JButton("Encrypt file");
	private JTextField jtf_key ;
	private GridBagConstraints gbc ;
	private JButton open_file = new JButton("Open file to decrypt");
	private JLabel str_key = new JLabel("Key");
	private JCheckBox checkBoxArchive = new JCheckBox("Save files to archive");

	private  Encrypt encryptFile;
	private long sizeOfSourceFiles = 0;
	public TabEncryptFiles() {
		
		setLayout(new GridBagLayout());
		
	    JProgressBar progressBar = new JProgressBar();
	    progressBar.setStringPainted(true);
	    progressBar.setPreferredSize(new Dimension(240, 40));
	    progressBar.setMinimum(0);
	    progressBar.setMaximum(100);
	    Border border = BorderFactory.createTitledBorder("Encryption...");
	    progressBar.setBorder(border);
	    
		 Timer timer = new Timer(100, new ActionListener() {
			  @Override
			  public void actionPerformed(ActionEvent arg0) {
				long value1 = (100 * encryptFile.getCommonSizeOfFiles()) / sizeOfSourceFiles ;
				  progressBar.setValue((int) value1);
				  if(encryptFile.threadIsAlive() == false) {
					  ((Timer)arg0.getSource()).stop();
					  JOptionPane.showMessageDialog(null, "File was encrypted!!!");
				  }
			  }
			});
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
			                   List<String> outputPaths = new ArrayList<String>();
			                   for (File file : droppedFiles) 
		                	   {
			                	   sizeOfSourceFiles += file.length();
		                	   }
			                   if(f.getSelectedFile() != null)
			                   {
			                	   for(int i = 0; i < droppedFiles.size(); i++) {
			                		   outputPaths.add(f.getSelectedFile() + "/" + droppedFiles.get(i).getName() + "_encrypted.png");
			                	   }
			                	   encryptFile = new Encrypt(jtf_key.getText());
			                	   encryptFile.EncryptGroupsOfFiles(droppedFiles, outputPaths);
			                	   timer.start(); 
			                   }
			          } catch (Exception ex) {
			              ex.printStackTrace();
			          }
			      }
			  });
		panelEncrypt.setBorder(BorderFactory.createLineBorder(Color.RED, 2, true));
				

	    
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
					File fileOutput = null;
					
					JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
					fileChooser.setPreferredSize(new Dimension(1000, 600)); 
					
					if (fileChooser.showOpenDialog(open_file) == JFileChooser.APPROVE_OPTION) {
					   sourceFile = fileChooser.getSelectedFile();
					   sizeOfSourceFiles = sourceFile.length(); //size of 1 file
					}
					 if (fileChooser.showSaveDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {
						 fileOutput = fileChooser.getSelectedFile();
				        }
					 if(sourceFile != null && fileOutput != null) {
						 try
						  {
							  encryptFile = new Encrypt(jtf_key.getText());
							  encryptFile.EncryptFile(sourceFile, fileOutput.getPath()+ "_encrypted.png");
							  timer.start(); 
						  }
						  catch(NullPointerException e){ 
							  System.out.println("NullPointerException: " + e);
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
		add(panelEncrypt, gbc);
				
		gbc.gridx = 0;
		gbc.gridy = 1;
		add(btn_encrypt_file, gbc); 
		
		gbc.gridx = 3;
		gbc.gridy = 1;
		add(checkBoxArchive, gbc); 
		
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		add(progressBar, gbc); 
	}
}
