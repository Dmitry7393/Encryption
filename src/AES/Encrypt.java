package AES;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Encrypt extends AES {
	private String whole_ciphertext = "";
	private byte Round[][][] = new byte[11][4][4];
	private void initRoundKey(String str_key)
	{
		byte cipher[][] = new byte[4][4];
		cipher = toHex(str_key);
		for(int i = 0; i < 4; i++)
		{
			for(int j  = 0; j < 4; j++)
			{
				Round[0][i][j] = cipher[i][j];
			}
		}
		for(int i = 1; i < 11; i++)
		{
			Round[i] = get_cipher(cipher, RC[i-1]);
		}
	}
	private byte[] Encrypt_block(byte[][] plain_text,  String str_key, String typeEncryption)
	{
		//show(plain_text);
		XOR(plain_text, Round[0]);
		for(int i = 1; i < 10; i++)
		{
			SubBytes(plain_text, false);
			ShiftRows(plain_text, false);
			MixColumns(plain_text, false);
			XOR(plain_text, Round[i]);  
		}
		//AES Round 10 no Mix columns 
		SubBytes(plain_text, false);
		ShiftRows(plain_text, false);
		XOR(plain_text, Round[10]);
		
		//show(plain_text);
		//write_to_file(plain_text, "D:/ciphertext.txt");
		
		byte[] ciphertext1 = new byte[16];
		if(typeEncryption.equals("file"))
		{
			int k = 0; 
			for(int j = 0; j < 4; j++)
			{
				for(int i = 0; i < 4; i++)
				{
					ciphertext1[k] = plain_text[i][j]; 
					k++;
				}
			}
		}
		if(typeEncryption.equals("string"))
		{
			String cipher_text = "";
	        for(int j = 0; j < 4; j++)
	        {
	            for(int i = 0; i < 4; i++)
	            {
	                cipher_text = cipher_text + plain_text[i][j] + " ";
	            }
	        }
			whole_ciphertext += cipher_text;
		}
		return ciphertext1;
	}
	public void EncryptText(String str_plain_text, String str_key)
	{
		int r = 0;
		whole_ciphertext = "";
		for(int i = 0; i < str_plain_text.length(); i += 16)
		{
			r = str_plain_text.length() - i;
			if(r >= 16)
			{
				Encrypt_block(toHex(str_plain_text.substring(i, i+16)), str_key, "string");
			}
			else
			{
				Encrypt_block(toHex(str_plain_text.substring(i, i+r)), str_key, "string");
			}
		}
	}
	public void EncryptFile(String key, File sourceFile, String outputFile)
	{
		try
		{
			convertToHex(key, sourceFile, outputFile);
		}
		catch(IOException e)
		{
			
		}
	}
	public Encrypt(String key){
		initRoundKey(key);
	}
    public  void convertToHex(String key, File file, String pathNew) throws IOException {
		InputStream is = new FileInputStream(file);
		FileOutputStream fos = new FileOutputStream(pathNew);
		int bytesCounter = 0;		
		int value = 0;
		int j = 0;
		byte block4_4[][] = new byte[4][4];
		byte encryptedBytes[] = new byte[16];
		byte currentBytes[] = new byte[16];
		while ((value = is.read()) != -1) {    
			currentBytes[j] = (byte) value; //read 16 bytes
			j++;
		    if(bytesCounter==15) {
		    	block4_4 = getBlock4_4(currentBytes, 16);
		    	encryptedBytes = Encrypt_block(block4_4, key, "file");
		       	WriteFile(fos, encryptedBytes, 16);
		       	bytesCounter=0;
		       	j = 0;
		       	for(int i = 0; i < 16; i++)
		       	{
		       		currentBytes[i] = 0;
		       		encryptedBytes[i] = 0;
		       	}
		       		
		    } else{
		        bytesCounter++;
		    }
	       }		
		//if still got content - the last a few bytes
		if(bytesCounter != 0)
		{	
			block4_4 = getBlock4_4(currentBytes, 16);
			encryptedBytes = Encrypt_block(block4_4, key, "file");
		  	WriteFile(fos, encryptedBytes, j);
	    }
			fos.close();
	        is.close();
	  }
  public static void WriteFile(FileOutputStream fos, byte[] arrayBytes, int length) throws IOException
	{
		for(int i = 0; i < length; i++)
		{
			fos.write(arrayBytes[i]);
		}
	}
	public String get_ciphertext()
	{
		return whole_ciphertext;
	}

}

