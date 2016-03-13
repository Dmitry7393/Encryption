package Image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

public class Encrypt_in_image {
	public Encrypt_in_image(String path, String ciphertext2) throws IOException
	{
		BufferedImage img = ImageIO.read(new File(path)); 
		
		  int[] ciphertext = new int[] {-115, 121, 73, -70, 45, 105,
				  -79, -59, 12, 123, -92, -66, -111, 110, -1, -70 };
		int  clr ;
		int  red;
	  	int  green;
	  	int  blue;
	  	int rgb ;
	  	int value = 0;
	  	int k = 0;
	  	//Creating image with ciphertext
	  	Boolean next = true;
		  for(int i = 0; i < 32; i++)
		  {
			  for(int j = 0; j < 32; j++)
			  {
				  clr =  img.getRGB(i, j); 	
				  red =  (clr & 0x00ff0000) >> 16;
			  	  green = (clr & 0x0000ff00) >> 8;
			  	  blue  =  clr & 0x000000ff;
			  	  value = ciphertext[k];
				  if(ciphertext[k] < 0)
				  {
					   value = ciphertext[k]+256;
				  }
				  if(red != value)
				  {
					  System.out.print(value + " " + i + " " + j + " " + "\n");
					  rgb = new Color(value, green, blue).getRGB();
					  img.setRGB(i, j, rgb);
					  k++; //next element in ciphertext
				  }
				  else
				  {
					  System.out.print("RED IS THE SAME!!!");
				  }
				  if(k == 16)
				  {
					  next = false;
					  break;
				  }
			  }
			  if(next == false) break;
		  }
		  JFileChooser fileChooser = new JFileChooser("D:\\");
		  if (fileChooser.showSaveDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {
		    File file = fileChooser.getSelectedFile();
		    File outputfile = new File(file.getPath());
			  ImageIO.write(img, "png", outputfile);
		  }
		 /* int position_point = path.lastIndexOf('.');
		  String first_part = path.substring(0, position_point) +  "7" + path.substring(position_point, path.length());
		  System.out.println("point = " + position_point);
		  File outputfile = new File(first_part);
		  ImageIO.write(img, "png", outputfile);
		  */
	}
	
}
