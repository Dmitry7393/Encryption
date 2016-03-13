package Windows;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import AES.Encrypt;

public class Encryption {
	private JLabel str_key = new JLabel("Key");
	private JTextField jtf ;
	private DefaultTableModel model = new DefaultTableModel(); 
	private JTable table = new JTable(model); 
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
		jtf.setText("Thats my Kung Fu");
		JLabel jlab = new JLabel("message");
		JButton jbtn1 = new JButton("Encrypt");
		JButton jbtn2 = new JButton("Decrypt");
		// Create a couple of columns 
		model.addColumn("Col1"); 
		model.addColumn("Col2"); 
		model.addColumn("Col3"); 
		model.addColumn("Col4"); 
		jbtn1.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0) {
					
					String str_plain_text = "Two One Nine Two";
					String str_key = jtf.getText();
					Encrypt a = new Encrypt(str_plain_text, str_key);
					byte[][] r = a.get_ciphertext();
					
					System.out.println("From main class123");
					// Append a row 
					for(int i = 0; i < 4; i++)
					{
						model.addRow(new Object[]{Byte.toString(r[i][0]), Integer.toString((r[i][1])),
								Byte.toString(r[i][2]), Integer.toString((r[i][3])) });
					}
					jlab.setText(str_key);
					
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
		
		gbc.gridx = 1;
		gbc.gridy = 3;
		panel.add(table, gbc);
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
