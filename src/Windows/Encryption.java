package Windows;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Encryption {
	private JLabel str_key = new JLabel("Key");
	private JTextField jtf;
	  GridBagConstraints gbc ;
	  
	 Encryption()
	{
		JPanel panel = new JPanel(new GridBagLayout());
	    gbc = new GridBagConstraints();
	    gbc.insets = new Insets(0,20,20,0);
	  //  gbc.weightx = 1.0;
	    gbc.anchor = GridBagConstraints.NORTHWEST;
	  // gbc.gridwidth = GridBagConstraints.REMAINDER;
	    
	   
	   
	    JFrame jfrm = new JFrame("Encryption");
		   jfrm.setSize(720, 400);
		   jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		jtf = new JTextField(12);
		JLabel jlab = new JLabel("message");
		JButton jbtn1 = new JButton("Encrypt");
		JButton jbtn2 = new JButton("Decrypt");
		
		jbtn1.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0) {
					String str = jtf.getText();
					jlab.setText(str);
					
				}
			});
		jbtn2.addActionListener(new ActionListener() 
		{

			@Override
			public void actionPerformed(ActionEvent e) {
				jlab.setText("Decrypt");
				
			}
			
		});
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(str_key, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		panel.add(jtf, gbc);
		
		gbc.gridx = 1;
		gbc.gridy =  1;

		gbc.ipady = 20;
		gbc.ipadx = 20;
		panel.add(jbtn1, gbc);
		gbc.gridx = 2;
		gbc.gridy = 1;
		panel.add(jbtn2, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 2;
		panel.add(jlab, gbc);
		   jfrm.add(panel);
		jfrm.setVisible(true);
	}
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				new Encryption();
			}
		});
	}

}
