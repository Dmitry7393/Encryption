package Image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

public class EncryptInImage {
	private BufferedImage img;
	private int countSourceBytes; 
	private void initImage(String pathFile) throws IOException
	{
		img = ImageIO.read(new File(pathFile)); 
	}
	public EncryptInImage(String pathFile)
	{
		try {
			initImage(pathFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void WriteCountBytes(int count)
	{
		byte array4Bytes[] = intToByteArray(count);
		int rgb = new Color(array4Bytes[0], array4Bytes[1], array4Bytes[2]).getRGB();
		img.setRGB(0, 0, rgb);
		
		int clr =  img.getRGB(1, 0); 
		int red = array4Bytes[3];
		int  green =  (clr & 0x0000ff00) >> 8;
		int  blue  =   (clr & 0x000000ff);
		int  rgb2 = new Color(red, green, blue).getRGB();
		img.setRGB(1, 0, rgb2);
	}
	private void setByteToPixel(byte myByte, int i, int j) throws IOException
	{	
		byte cipherCode = myByte;
		
		int aInt = cipherCode & 0xFF; //get signed int
		byte first3Bytes = (byte) (aInt >> 5);
	
		byte filter = 28; //00011100
		byte second3Bytes = (byte) ((aInt & filter) >> 2);
		
		byte maskLast2Bytes = 3; //00000011
		byte third2Bytes = (byte) (aInt & maskLast2Bytes);
			
		int clr =  img.getRGB(i,j); 
		int  redInt   =  (clr & 0x00ff0000) >> 16;
		int  greenInt =  (clr & 0x0000ff00) >> 8;
		int  blueInt  =   (clr & 0x000000ff);
		//Delete the last 3 and 2 bits
		int maskLast3Bytes = 248; //11111000
		int mastLast2Bytes = 252; //11111100
		redInt = redInt & maskLast3Bytes;
		greenInt = greenInt & maskLast3Bytes;
		blueInt = blueInt & mastLast2Bytes;
		
		//Add 3, 3, 2 bits at the end of byte
		int redNew = redInt | first3Bytes;
		int greenNew = greenInt | second3Bytes;
		int blueNew = blueInt | third2Bytes;
		
		int rgbNew = new Color(redNew, greenNew, blueNew).getRGB();
		img.setRGB(i, j, rgbNew);
	}
	public Boolean writeBytesToImage(String stringBase64)
	{
		//Get bytes from stringBase64
		byte arrayBytes[] = Base64ToByte(stringBase64);
		for(int i = 0; i < arrayBytes.length; i++)
		{
			System.out.print(arrayBytes[i] +" ");
		}
		countSourceBytes = arrayBytes.length; 
		
		//If we do not have enough pixels to save information
		if(arrayBytes.length > (img.getHeight()*img.getWidth()-2)){
			return false;
		}
		//write in first 4 pixel count of bytes
		WriteCountBytes(countSourceBytes);
		
		int row = 0;
		int column = 2; //cause the in first 2 pixels we store size of source text
		for(int i = 0; i < arrayBytes.length; i++)
		{
			try {
				setByteToPixel(arrayBytes[i], column, row);
				column++;
				if(column == img.getWidth()-1)
				{
					column = 0;
					row++;  //next line
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	    File outputfile = new File("C:/Users/Dmitry/Desktop/test.png");
		try {
			ImageIO.write(img, "png", outputfile); //save new picture
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	public int getProperCountPixels()
	{
		return countSourceBytes;
	}
	//Convert string in base64 to array of bytes
	public  byte[] Base64ToByte(String myText)
	{
		byte[] decoded = Base64.getDecoder().decode(myText);
		return decoded;
	}
	//devide int to 4 bytes and return array of 4 bytes
	public final byte[] intToByteArray(int value) {
	    return new byte[] {
	            (byte)(value >>> 24),
	            (byte)(value >>> 16),
	            (byte)(value >>> 8),
	            (byte)value};
	}
	//For debug
	public  void showBitsInt(int myInt, String str)
	{
		String strMaskFirst3 = Integer.toBinaryString(myInt);
		System.out.println(str + " " + myInt + " :" + strMaskFirst3);
	}
	public  void show(byte myByte, String str)
	{
		String strMaskFirst3 = Integer.toBinaryString(myByte);
		System.out.println(str + " " + myByte + " :" + strMaskFirst3);
	}
	public  void showInt(int t, String str)
	{
		System.out.println(str + " " + t);
	}
}
