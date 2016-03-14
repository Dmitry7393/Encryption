package Image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Decrypt_image {
	public String get_text(String img_original, String img_decrypted) throws IOException
	{
		BufferedImage img = ImageIO.read(new File(img_original)); 
		BufferedImage img2 = ImageIO.read(new File(img_decrypted)); 
	      int clr1; 
		  int  red1  ;
		  int clr2; 
		  int  red2  ;
		  int bt = 0 ;
		  String str_ciphertext = "";
		  for(int i = 0; i < img.getWidth(); i++)
		  {
			  for(int j = 0; j < img.getHeight(); j++)
			  {
				  clr1 =  img.getRGB(i, j); 
				  red1 =  (clr1 & 0x00ff0000) >> 16;	
			  	  clr2 =  img2.getRGB(i, j); 
				  red2 =  (clr2 & 0x00ff0000) >> 16;	
			  	  if(red1 != red2)
			  	  {
			  		  if(red2 >= 128)
			  		  {
			  			  bt = red2 - 256;
			  		  }
			  		  else bt = red2;
			  		//System.out.println("Different colours " + i + " " + j + "    " + bt);
			  		str_ciphertext = str_ciphertext + Integer.toString(bt) + " ";
			  	  }
			  }
		  }
		  /*System.out.println("Red Color value = "+ red);
		  System.out.println("Green Color value = "+ green);
		  System.out.println("Blue Color value = "+ blue);*/
		return str_ciphertext;
	}
	public  Decrypt_image()
	{
		
	}
}
