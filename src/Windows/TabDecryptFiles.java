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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

import AES.Decrypt;

public class TabDecryptFiles extends JPanel {
	private static final long serialVersionUID = 1L;
	private JLabel str_key = new JLabel("Key");
	private JLabel str_amount_bytes = new JLabel("0 bytes");

	private JButton btn_choose_files = new JButton("Choose files");
	private JButton btn_decrypt_files = new JButton("Decrypt files");
	private JButton open_file = new JButton("Open file to decrypt");
	private JButton btn_clear_list = new JButton("Clear list");

	private JTextField jtf_key;
	private GridBagConstraints gbc;
	private JCheckBox checkBoxArchive = new JCheckBox("Save files to archive");
	private DefaultListModel<String> mDefaultListModel = new DefaultListModel<String>();
	private DefaultTableModel mDefaultTableModel = new DefaultTableModel();
	private Dimension dimenstionTable = new Dimension(230, 120);

	private Decrypt mDecryptFile;
	private List<File> mArrayFiles = new ArrayList<File>();
	private long mSizeOfSourceFiles = 0;

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
				long valuePercent = (100 * mDecryptFile.getCommonSizeOfFiles()) / mSizeOfSourceFiles;
				progressBar.setValue((int) valuePercent);
				if (mDecryptFile.threadIsAlive() == false) {
					((Timer) arg0.getSource()).stop();
					JOptionPane.showMessageDialog(null, "Files were encrypted!!!");
					progressBar.setValue(0);
				}
			}
		});
		JList<String> list = new JList<String>(mDefaultListModel);
		JScrollPane scrollList = new JScrollPane(list);
		scrollList.setPreferredSize(dimenstionTable);
		Box listBoxSourceFiles = new Box(BoxLayout.Y_AXIS);
		listBoxSourceFiles.add(scrollList);
		listBoxSourceFiles.add(new JLabel("Encrypted files"));

		list.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DELETE) {
					int selectedIndex = list.getSelectedIndex();
					if (selectedIndex != -1) {
						mSizeOfSourceFiles -= mArrayFiles.get(selectedIndex).length();
						mArrayFiles.remove(selectedIndex);
						mDefaultListModel.remove(selectedIndex);
						mDefaultTableModel.removeRow(selectedIndex);
					}
					showSizeOfFiles();
				}
			}

			public void keyReleased(KeyEvent e) {
			}

			public void keyTyped(KeyEvent e) {
			}
		});

		JPanel panelDecrypt = new JPanel();
		panelDecrypt.setLayout(new BoxLayout(panelDecrypt, BoxLayout.X_AXIS));

		panelDecrypt.setPreferredSize(new Dimension(240, 100));
		panelDecrypt.setVisible(true);

		panelDecrypt.add(Box.createHorizontalGlue());
		panelDecrypt.add(new JLabel("Drag files here to encrypt them"));
		panelDecrypt.add(Box.createHorizontalGlue());

		JTable table = new JTable(mDefaultTableModel);
		mDefaultTableModel.addColumn("");
		table.setShowGrid(false);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollTable = new JScrollPane(table);
		scrollTable.setColumnHeader(null);
		scrollTable.setPreferredSize(dimenstionTable);
		Box tableBox = new Box(BoxLayout.Y_AXIS);
		tableBox.add(scrollTable);
		tableBox.add(new JLabel("New decrypted files"));

		panelDecrypt.setDropTarget(new DropTarget() {
			private static final long serialVersionUID = 1L;

			public synchronized void drop(DropTargetDropEvent evt) {
				try {
					evt.acceptDrop(DnDConstants.ACTION_COPY);
					@SuppressWarnings("unchecked")
					List<File> droppedFiles = (List<File>) evt.getTransferable()
							.getTransferData(DataFlavor.javaFileListFlavor);
					for (File file : droppedFiles) {
						mArrayFiles.add(file);
						mDefaultTableModel.addRow(new String[] { file.getName() + "_encrypted.png" });
						mDefaultListModel.addElement(file.getName());
						mSizeOfSourceFiles += file.length();
					}
					showSizeOfFiles();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		panelDecrypt.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2, true));

		gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 20, 20, 0);
		gbc.anchor = GridBagConstraints.NORTHWEST;

		jtf_key = new JTextField(20);
		jtf_key.setText("Thats my Kung Fu");

		// Limiting the number of characters in a JTextField
		jtf_key.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (jtf_key.getText().length() >= 32) // limit to 32 characters
					e.consume();
			}
		});
		// Choose files with OpenDialogWindow
		btn_choose_files.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
				fileChooser.setPreferredSize(new Dimension(1000, 600));
				fileChooser.setMultiSelectionEnabled(true);
				if (fileChooser.showOpenDialog(open_file) == JFileChooser.APPROVE_OPTION) {
					File[] arrayFiles = fileChooser.getSelectedFiles();
					for (int i = 0; i < arrayFiles.length; i++) {
						mArrayFiles.add(arrayFiles[i]);
						mDefaultListModel.addElement(arrayFiles[i].getName());
						mDefaultTableModel.addRow(new String[] { arrayFiles[i].getName() + "_encrypted.png" });
						mSizeOfSourceFiles += arrayFiles[i].length();
					}
					showSizeOfFiles();
				}

			}
		});
		btn_decrypt_files.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (mArrayFiles.size() == 0) {
					JOptionPane.showMessageDialog(null, "Choose files first");
				} else {
					// Choose directory
					JFileChooser f = new JFileChooser();
					f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					f.showSaveDialog(null);

					List<String> outputPaths = new ArrayList<String>();

					if (f.getSelectedFile() != null) {
						mSizeOfSourceFiles = 0; // Calculate common size of
												// files from scratch
						for (int i = 0; i < mArrayFiles.size(); i++) {
							mSizeOfSourceFiles += mArrayFiles.get(i).length();
						}
						for (int count = 0; count < mDefaultTableModel.getRowCount(); count++) {
							outputPaths.add(
									f.getSelectedFile() + "/" + mDefaultTableModel.getValueAt(count, 0).toString());
						}
					}
					mDecryptFile = new Decrypt(jtf_key.getText());
					mDecryptFile.DecryptGroupsOfFiles(mArrayFiles, outputPaths);
					timer.start();
				}

			}
		});
		btn_clear_list.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mArrayFiles.clear();
				mDefaultListModel.removeAllElements();
				mDefaultTableModel.setRowCount(0);
				mSizeOfSourceFiles = 0;
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
		add(btn_choose_files, gbc);

		gbc.gridx = 1;
		gbc.gridy = 3;
		add(checkBoxArchive, gbc);

		gbc.gridx = 1;
		gbc.gridy = 5;
		gbc.gridwidth = 2;
		add(progressBar, gbc);

		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.gridheight = 2;
		add(listBoxSourceFiles, gbc);

		gbc.gridx = 4;
		gbc.gridy = 1;
		add(tableBox, gbc);

		gbc.gridx = 2;
		gbc.gridy = 3;
		add(btn_clear_list, gbc);

		gbc.gridx = 4;
		gbc.gridy = 5;
		add(btn_decrypt_files, gbc);

		gbc.gridx = 2;
		gbc.gridy = 0;
		add(str_amount_bytes, gbc);
	}

	private void showSizeOfFiles() {
		str_amount_bytes.setText(Long.toString((mSizeOfSourceFiles / 1024) / 1024) + " megabytes");
	}
}
