package Image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

public class Encrypt_in_image {
	private  int Random(int Min, int Max)
	{
		return Min + (int)(Math.random() * ((Max - Min) + 1));
	}
	private int[] get_ciphertext(String source_string)
	{
		String[] arr = source_string.split(" ");
		int[] s = new int[arr.length];
		for (int i = 0; i < arr.length; i++)
		{
			s[i] = Integer.parseInt(arr[i]) ;
		}
		return s;
	}
	private  Boolean check_pixels(int[] ciphertext2, BufferedImage img)
	{
        int clr;
        int red;
        Boolean next_element = false;
		for(int k = 0; k < ciphertext2.length; k++)
		{
			Boolean exit1 = true;
			for(int i = 0; i < img.getWidth(); i++)
			{
				next_element = false;
				for(int j = 0; j < img.getHeight(); j++)
				{
					 clr =  img.getRGB(i, j); 
					 red =  (clr & 0x00ff0000) >> 16;	
					 if(ciphertext2[k] != red)
					 {
						 next_element = true;        //move to next element if ciphertext2
						 exit1 = false;
						 break;
					 }
				 if(next_element == true) break;
				}
			}
			if(exit1 == true) return false;
		}
		return true;
	}
	private  Boolean check(BufferedImage img, Points points[], int points_size, int a, int b, int[] ciphertext)
	{
		//If the element was already added to points
		for(int i = 0; i < points_size; i++)
		{
			if(points[i].x == a && points[i].y == b) 
			{
				return false;
			}
		}
		int clr =  img.getRGB(a, b);
		int red =  (clr & 0x00ff0000) >> 16;
		for(int i = 0; i < ciphertext.length; i++)
		{
			if(ciphertext[i] == red)
			{
				System.out.println("similar colour " + i);
				return false;
			}
		}
		return true;
	}
	public Encrypt_in_image(String path, String str_ciphertext) throws IOException
	{
		BufferedImage img = ImageIO.read(new File(path)); 
		
		int[] ciphertext = get_ciphertext(str_ciphertext);
		
		int[] ciphertext2 = new int[ciphertext.length];
		for(int i = 0; i < ciphertext.length; i++)
		{
			ciphertext2[i] = ciphertext[i];
	  		if(ciphertext[i] < 0)
	  		{
	  			ciphertext2[i] = ciphertext[i]+256;
	  		}
		}
        if(check_pixels(ciphertext2, img) == false)
        {
        	System.out.println("We can not put some colours! Error!");
        }
        else
        {
        	int points_size = 0;
    	  	Points points[] = new Points[ciphertext.length];
    	  	int a, b;
    	  	//We find the places where we can put our ciphertext
    	   while(true)
    	  	{
    	  		a = Random(0, img.getWidth()-1);
    	  		b = Random(0, img.getHeight()-1);
    	  		if(check(img, points, points_size, a, b, ciphertext) == true)
    	  		{
    	  			points[points_size]=new Points(); 
    	  			points[points_size].x = a;
    	  			points[points_size].y = b;
    	  			points_size++;
    	  		}
    	  		if(points_size == ciphertext.length) break;
    	  	}
    	   for(int i = 0; i < points_size; i++)
    	  	{
    	  		System.out.println(points[i].x + " " + points[i].y);
    	  	}
    	  	Sort(points, 0, points_size-1);
    	  	int element_x = points[0].x;
    	  	int c = 0;
    		//Sort all points by y
    		for(int i = 1; i < points_size; i++)
    		{
    			if(element_x != points[i].x || i == points_size-1) 
    			{
    				if(i == points_size-1)
    				{
    					Sort_y(points, c, i);
    					break;
    				}
    				Sort_y(points, c, i-1);
    				element_x = points[i].x;
    				c = i;
    			}
    		}
    		System.out.println("After sort: ");
    		//Put our ciphertext into image
    	  	int rgb;
      		int  green;
            int  blue;
            int clr;

    	  	for(int i = 0; i < points_size; i++)
    	  	{
    	  		System.out.println(points[i].x + " " + points[i].y);
    	  	    clr =  img.getRGB(points[i].x, points[i].y);  
                green = (clr & 0x0000ff00) >> 8; //green and blue will be the same
    	  		blue  =  clr & 0x000000ff;
    	  		rgb = new Color(ciphertext2[i], green, blue).getRGB();
    	  	    img.setRGB(points[i].x, points[i].y, rgb);
    	  	}
  		  JFileChooser fileChooser = new JFileChooser("D:\\");
  		  if (fileChooser.showSaveDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {
  		    File file = fileChooser.getSelectedFile();
  		    File outputfile = new File(file.getPath());
  			  ImageIO.write(img, "png", outputfile);
        }
		  }
	}
	//sorting by y
	 private  void Sort_y(Points point[], int start, int end) {
	        if (start >= end)
	            return;
	        int i = start, j = end;
	        int cur = i - (i - j) / 2;
	        while (i < j) {
	            while (i < cur && (point[i].y <= (point[cur].y))) {
	                i++;
	            }
	            while (j > cur && (point[cur].y <= point[j].y)) {
	                j--;
	            }
	            if (i < j) {
	            	int temp_y = point[i].y;
	                point[i].y = point[j].y;
	                point[j].y = temp_y;
	                if (i == cur)
	                    cur = j;
	                else if (j == cur)
	                    cur = i;
	            }
	        }
	        Sort_y(point, start, cur);
	        Sort_y(point, cur+1, end);
	    }
	 //sorting by x +
	 private  void Sort(Points point[], int start, int end) {
	        if (start >= end)
	            return;
	        int i = start, j = end;
	        int cur = i - (i - j) / 2;
	        while (i < j) {
	            while (i < cur && (point[i].x <= (point[cur].x))) {
	                i++;
	            }
	            while (j > cur && (point[cur].x <= point[j].x)) {
	                j--;
	            }
	            if (i < j) {
	            	int temp_x = point[i].x;
	                int temp_y = point[i].y;
	                point[i].y = point[j].y;
	                point[i].x = point[j].x;
	                
	                point[j].x = temp_x;
	                point[j].y = temp_y;
	                if (i == cur)
	                    cur = j;
	                else if (j == cur)
	                    cur = i;
	            }
	        }
	        Sort(point, start, cur);
	        Sort(point, cur+1, end);
	    }
	
}
