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
	private void initImage(String pathFile) throws IOException {
		img = ImageIO.read(new File(pathFile)); 
	}
	public EncryptInImage(String pathFile) {
		try {
			initImage(pathFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void WriteCountBytes(int count)
	{
		byte array4Bytes[] = intToByteArray(count);
		for(int i = 0; i < 4; i++) {
			try {
				setByteToPixel(array4Bytes[i], i, 0);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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
		countSourceBytes = arrayBytes.length; 
		
		//If we do not have enough pixels to save information
		if(arrayBytes.length > (img.getHeight()*img.getWidth()-4)){
			return false;
		}
		//write in first 4 pixel count of bytes
		WriteCountBytes(countSourceBytes);
		
		int k = 0; 
		int c = 4;
		for(int i = 0; i < img.getHeight(); i++)
		{
			for(int j = c; j < img.getWidth(); j++)
			{
				try {
					setByteToPixel(arrayBytes[k], j, i);
					k++;
					if(k == arrayBytes.length)
						return true;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			c = 0;
		}
		return true;
	}
	public void saveImage(String pathNewFile)
	{
	    File outputfile = new File(pathNewFile);
			try {
				ImageIO.write(img, "png", outputfile); //save new picture
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	public int getProperCountPixels()
	{
		return countSourceBytes+4;
	}
	//Convert string in base64 to array of bytes
	private  byte[] Base64ToByte(String myText)
	{
		byte[] decoded = Base64.getDecoder().decode(myText);
		return decoded;
	}
	//devide int to 4 bytes and return array of 4 bytes
	private final byte[] intToByteArray(int value) {
	    return new byte[] {
	            (byte)(value >>> 24),
	            (byte)(value >>> 16),
	            (byte)(value >>> 8),
	            (byte)value};
	}
}