package Windows;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Encryption {
	private JTextField jtf;
	  GridBagConstraints gbc = new GridBagConstraints();
	  
	 Encryption()
	{
		JFrame jfrm = new JFrame("Encryption");
	//jfrm.setLayout(new FlowLayout());
		   jfrm.setLayout(new GridBagLayout());
		 
		jfrm.setSize(720, 400);
		jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		jtf = new JTextField(16);
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
		jfrm.add(jtf, gbc);
		gbc.gridx = 4;
		gbc.gridy = 0;
		jfrm.add(jbtn1, gbc);
		gbc.gridx = 0;
		gbc.gridy = 2;
		jfrm.add(jbtn2, gbc);
		
		gbc.gridx = 4;
		gbc.gridy = 1;
		jfrm.add(jlab, gbc);
		gbc.gridx = 3;
		gbc.gridy = 3;
		 
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
