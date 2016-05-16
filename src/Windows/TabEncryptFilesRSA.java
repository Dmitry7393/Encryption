package Windows;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import RSA.CreateKeys;
import RSA.DecryptionRSA;
import RSA.EncryptionRSA;

public class TabEncryptFilesRSA extends JPanel {
	private static final long serialVersionUID = 1L;
	private JButton btn_generate_keys = new JButton("Generate keys");
	private JButton btn_encrypt_file = new JButton("Encrypt file");
	private JButton btn_decrypt_file = new JButton("Decrypt file");
	private JButton open_file = new JButton("Open file to decrypt");
	private JButton btn_choose_files_to_encrypt = new JButton("Choose files to encrypt");
	private JButton btn_choose_files_to_decrypt = new JButton("Choose files to decrypt");
	
	private DefaultListModel<String> mDefaultListModelSourceFiles = new DefaultListModel<String>();
	private DefaultListModel<String> mDefaultListModelEncryptedFiles = new DefaultListModel<String>();

	private GridBagConstraints gbc;

	private JLabel labelPublicKey = new JLabel("Public key");
	private JLabel labelPrivateKey = new JLabel("Private key");

	private JTextField jtf_public_key;
	private JTextField jtf_private_key;
	private JTextField jtf_n_public;
	private JTextField jtf_n_private;
	private Dimension dimenstionTable = new Dimension(220, 120);

	private List<File> mArrayOriginalFiles = new ArrayList<File>();
	private List<File> mArrayEncryptedFiles = new ArrayList<File>();

	TabEncryptFilesRSA() {

		setLayout(new GridBagLayout());

		// JSeparator jSeparator = new JSeparator(SwingConstants.VERTICAL);
		// jSeparator.setPreferredSize(new Dimension(23, 50));

		jtf_public_key = new JTextField(20);
		jtf_private_key = new JTextField(20);
		jtf_n_public = new JTextField(20);
		jtf_n_private = new JTextField(20);
		gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 20, 20, 0);
		gbc.anchor = GridBagConstraints.NORTHWEST;
		btn_generate_keys.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				CreateKeys cKey = new CreateKeys();
				jtf_public_key.setText(cKey.getPublicKey().toString());
				jtf_private_key.setText(cKey.getPrivateKey().toString());
				jtf_n_public.setText(cKey.getN().toString());
				jtf_n_private.setText(cKey.getN().toString());
			}
		});
		btn_choose_files_to_encrypt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
				fileChooser.setPreferredSize(new Dimension(1000, 600));
				fileChooser.setMultiSelectionEnabled(true);
				if (fileChooser.showOpenDialog(open_file) == JFileChooser.APPROVE_OPTION) {
					File[] arrayFiles = fileChooser.getSelectedFiles();
					for (int i = 0; i < arrayFiles.length; i++) {
						mArrayOriginalFiles.add(arrayFiles[i]);
						mDefaultListModelSourceFiles.addElement(arrayFiles[i].getName());
					}
				}

			}
		});
		btn_choose_files_to_decrypt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
				fileChooser.setPreferredSize(new Dimension(1000, 600));
				fileChooser.setMultiSelectionEnabled(true);
				if (fileChooser.showOpenDialog(open_file) == JFileChooser.APPROVE_OPTION) {
					File[] arrayFiles = fileChooser.getSelectedFiles();
					for (int i = 0; i < arrayFiles.length; i++) {
						mArrayEncryptedFiles.add(arrayFiles[i]);
						mDefaultListModelEncryptedFiles.addElement(arrayFiles[i].getName());
					}
				}

			}
		});
		JList<String> listSourceFiles = new JList<String>(mDefaultListModelSourceFiles);
		JScrollPane scrollListSourceFiles = new JScrollPane(listSourceFiles);
		scrollListSourceFiles.setPreferredSize(dimenstionTable);
		Box listBoxSourceFiles = new Box(BoxLayout.Y_AXIS);
		listBoxSourceFiles.add(scrollListSourceFiles);
		listBoxSourceFiles.add(new JLabel("Source files"));

		JList<String> listEncryptedFiles = new JList<String>(mDefaultListModelEncryptedFiles);
		JScrollPane scrollListEncryptedFiles = new JScrollPane(listEncryptedFiles);
		scrollListEncryptedFiles.setPreferredSize(dimenstionTable);
		Box listBoxEncryptedFiles = new Box(BoxLayout.Y_AXIS);
		listBoxEncryptedFiles.add(scrollListEncryptedFiles);
		listBoxEncryptedFiles.add(new JLabel("Encrypted files"));

		btn_encrypt_file.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				if (mArrayOriginalFiles.size() == 0) {
					JOptionPane.showMessageDialog(null, "Choose files first");
				} else {
					// Choose directory
					JFileChooser f = new JFileChooser();
					f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					f.showSaveDialog(null);

					List<String> outputPaths = new ArrayList<String>();
					for (int count = 0; count < mArrayOriginalFiles.size(); count++) {
						outputPaths.add(f.getSelectedFile() + "/" + mArrayOriginalFiles.get(count).getName());
					}
					if (f.getSelectedFile() != null) {
						EncryptionRSA encrRSA = new EncryptionRSA(jtf_public_key.getText(), jtf_n_public.getText());
						encrRSA.EncryptFiles(mArrayOriginalFiles, outputPaths);
					}
				}
			}
		});
		btn_decrypt_file.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (mArrayEncryptedFiles.size() == 0) {
					JOptionPane.showMessageDialog(null, "Choose files first");
				} else {
					// Choose directory
					JFileChooser f = new JFileChooser();
					f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					f.showSaveDialog(null);

					List<String> outputPaths = new ArrayList<String>();
					for (int count = 0; count < mArrayEncryptedFiles.size(); count++) {
						outputPaths.add(f.getSelectedFile() + "/" + mArrayEncryptedFiles.get(count).getName());
					}
					if (f.getSelectedFile() != null) {
						DecryptionRSA decrRSA = new DecryptionRSA(jtf_private_key.getText(), jtf_n_private.getText());
						decrRSA.DecryptFiles(mArrayEncryptedFiles, outputPaths);
					}
				}
			}
		});

		gbc.gridx = 0;
		gbc.gridy = 0;
		add(btn_generate_keys, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		add(labelPublicKey, gbc);

		gbc.gridx = 1;
		gbc.gridy = 1;
		add(jtf_public_key, gbc);

		gbc.gridx = 2;
		gbc.gridy = 1;
		add(jtf_n_public, gbc);

		gbc.gridx = 2;
		gbc.gridy = 2;
		add(jtf_n_private, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		add(labelPrivateKey, gbc);

		gbc.gridx = 1;
		gbc.gridy = 2;
		add(jtf_private_key, gbc);

		gbc.gridx = 1;
		gbc.gridy = 5;
		add(btn_choose_files_to_encrypt, gbc);

		gbc.gridx = 2;
		gbc.gridy = 5;
		gbc.gridheight = 2;
		add(btn_choose_files_to_decrypt, gbc);

		gbc.gridx = 1;
		gbc.gridy = 7;
		add(listBoxSourceFiles, gbc);

		gbc.gridx = 2;
		gbc.gridy = 7;
		add(listBoxEncryptedFiles, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 9;
		add(btn_encrypt_file, gbc);

		gbc.gridx = 2;
		gbc.gridy = 9;
		add(btn_decrypt_file, gbc);

	}
}
