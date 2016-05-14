package Windows;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import RSA.CreateKeys;
import RSA.DecryptionRSA;
import RSA.EncryptionRSA;

public class TabRSA extends JPanel {
	private static final long serialVersionUID = 1L;
	private JButton btn_generate_keys = new JButton("Generate keys");
	private JButton btn_encrypt = new JButton("Encrypt");
	private JButton btn_decrypt = new JButton("Decrypt");
	private GridBagConstraints gbc;

	private JTextArea plainTextArea = new JTextArea(5, 20);
	private JTextArea cipherTextArea = new JTextArea(5, 20);
	private JTextArea encryptedTextArea = new JTextArea(5, 20);
	private JTextArea decryptedTextArea = new JTextArea(5, 20);
	
	private JLabel labelPublicKey = new JLabel("Public key");
	private JLabel labelPrivateKey = new JLabel("Private key");

	private JTextField jtf_public_key;
	private JTextField jtf_private_key;
	private JTextField jtf_n_public;
	private JTextField jtf_n_private;

	TabRSA(int frameLocationX, int frameLocationY) {
		
		setLayout(new GridBagLayout());
		plainTextArea.setText("Two One Nine Two");

		JScrollPane scrollPlainTextArea = new JScrollPane(plainTextArea);
		// scroll.setBounds(100, 10, 100, 10);
		scrollPlainTextArea.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPlainTextArea.setVisible(true);

		JScrollPane scrollCiphertextArea = new JScrollPane(cipherTextArea);
		scrollCiphertextArea.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollCiphertextArea.setVisible(true);
		
		JScrollPane scrollEncryptedtextArea = new JScrollPane(encryptedTextArea);
		scrollCiphertextArea.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollCiphertextArea.setVisible(true);

		JScrollPane scrollDecryptedtextArea = new JScrollPane(decryptedTextArea);
		scrollCiphertextArea.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollCiphertextArea.setVisible(true);
		
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
		btn_encrypt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				EncryptionRSA encrRSA = new EncryptionRSA(jtf_public_key.getText(), jtf_n_public.getText());
				encrRSA.EncryptText(plainTextArea.getText());
				encryptedTextArea.setText(encrRSA.getEncryptedText());
				cipherTextArea.setText(encrRSA.getEncryptedText());
			}
		});
		btn_decrypt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				DecryptionRSA decrRSA = new DecryptionRSA(jtf_private_key.getText(), jtf_n_private.getText());
				decrRSA.DecryptText(cipherTextArea.getText());
				decryptedTextArea.setText(decrRSA.getDecryptedText());

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

		gbc.gridx = 0;
		gbc.gridy = 3;
		add(btn_encrypt, gbc);

		gbc.gridx = 1;
		gbc.gridy = 3;
		add(scrollPlainTextArea, gbc);

		gbc.gridx = 2;
		gbc.gridy = 3;
		add(scrollEncryptedtextArea, gbc);
		
		
		gbc.gridx = 0;
		gbc.gridy = 4;
		add(btn_decrypt, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 4;
		add(scrollCiphertextArea, gbc);
		
		gbc.gridx = 2;
		gbc.gridy = 4;
		add(scrollDecryptedtextArea, gbc);
	}
}
