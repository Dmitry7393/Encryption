package Windows;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.JButton;
import javax.swing.JPanel;

import RSA.RSA;


public class TabRSA extends JPanel {
	private static final long serialVersionUID = 1L;
	private JButton btn_rsa = new JButton("RSA");
	private GridBagConstraints gbc ;
	TabRSA(int frameLocationX, int frameLocationY) {
		setLayout(new GridBagLayout());
	    gbc = new GridBagConstraints();
	    gbc.insets = new Insets(0,20,20,0);
	    gbc.anchor = GridBagConstraints.NORTHWEST;
	    btn_rsa.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				RSA rs = new RSA();
				rs.test();
			}
		});
	
	gbc.gridx = 0;
	gbc.gridy =  3;
	add(btn_rsa, gbc);
	
	}
}
