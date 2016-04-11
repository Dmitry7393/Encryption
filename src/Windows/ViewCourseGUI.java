package Windows;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ViewCourseGUI extends JFrame{ 
	private static final long serialVersionUID = 1L;
    private JFrame frame=new JFrame("Image");
    private int widthWindow;
    private int heightWindow;
    private int locationX;
    private int locationY;
    private  Image img;
    private JLabel imageLabel ;
     public  ViewCourseGUI(int a, int b){
    	 widthWindow = a;
    	 heightWindow = b;
     }        
     public void setLocation(int a, int b)
     {
    	 locationX = a;
    	 locationY = b;
     }
     public void setPicture(ImageIcon imageIcon)
     {
    	 frame.setSize(widthWindow, heightWindow);
         frame.setLocation(locationX, locationY);
         frame.setVisible(true);  
         img = imageIcon.getImage();
         img = img.getScaledInstance(widthWindow, heightWindow,  java.awt.Image.SCALE_SMOOTH);
         imageIcon = new ImageIcon(img);
         imageLabel = new JLabel(imageIcon);     
         frame.add(imageLabel);
     }
}