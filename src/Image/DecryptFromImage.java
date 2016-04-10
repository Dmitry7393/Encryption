package Image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import javax.imageio.ImageIO;

public class DecryptFromImage {
	private String ciphertextBase64;
	private BufferedImage img;
	private int getAmountOfBytes()
	{
		byte arrayBytesInt[] = new byte[4];
		for(int i = 0; i < 4; i++) {
			arrayBytesInt[i] = getByteFromPixel(i, 0);
		}
		int amountEncryptedBytes = getInt(arrayBytesInt);
		return amountEncryptedBytes;
	}
	private byte getByteFromPixel(int j, int i)
	{
		int clr = img.getRGB(j, i); 
		int  red1 =  (clr & 0x00ff0000) >> 16;
		int  green1 =  (clr & 0x0000ff00) >> 8;
		int  blue1  =   (clr & 0x000000ff);
		
		byte red = (byte) red1;
		
		byte maskLast3Bytes = 7;
		byte maskLast2Bytes = 3;
		
		red = (byte) (red & maskLast3Bytes);
		byte result = (byte) (red << 5);

		byte green = (byte) green1;
		green = (byte) (green & maskLast3Bytes);
		green = (byte) (green << 2);
	
		result = (byte) (result | green);
		
		byte blue = (byte) blue1;
		blue = (byte) (blue & maskLast2Bytes);
		
		result = (byte) (result | blue);
		
		return result;
	}
	private byte[] getBytesFromFile() throws IOException
	{
		int countBytes = getAmountOfBytes();
		byte arrayBytesCiphertext[] = new byte[countBytes];
		int k = 0;  //index for array
		int c = 4;  //first four pixels contain size of ciphertext
		for(int i = 0; i < img.getHeight(); i++)  //rows
		{
			for(int j = c; j < img.getWidth(); j++)
			{	
				arrayBytesCiphertext[k] = getByteFromPixel(j, i);
				k++;
				if(k >= countBytes)
				{
					return arrayBytesCiphertext;
				}
			}
			c = 0;
		}
		return arrayBytesCiphertext;
	}
	public DecryptFromImage(String pathToPicture){
		byte ciphertextBytes[] = null;
		try {
			img = ImageIO.read(new File(pathToPicture));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			ciphertextBytes = getBytesFromFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ciphertextBase64 = ConvertToBase64(ciphertextBytes);
	}
	public String getCiphertext() {
		return ciphertextBase64;
	}
	public String ConvertToBase64(byte myByteArray[]){
		Base64.Encoder myEncoder = Base64.getEncoder().withoutPadding();
		String  base64String = myEncoder.encodeToString(myByteArray);
		return base64String;
	}
	public int getInt(byte arrayBytes[])
	{
		int i = ((arrayBytes[0] & 0xFF) << 24) + 
				((arrayBytes[1] & 0xFF) << 16) + 
				((arrayBytes[2] & 0xFF) << 8) +
				(arrayBytes[3] & 0xFF);
		return i;
	}
}
