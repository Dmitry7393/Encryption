package AES;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Decrypt extends AES {
	private String source_text =  "";
	public String get_text()
	{
		return source_text;
	}
	private byte[] Decrypt_block_file(byte[][] block_16, String str_key)
	{
		byte plain_text[][] = new byte[4][4];
		byte cipher[][] = new byte[4][4];
		byte Round[][][] = new byte[11][4][4];
		
		for(int i = 0; i < 4; i++)
		{
			for(int j = 0; j < 4; j++)
			{
				plain_text[i] = block_16[i];
			}
		}
		
		show(plain_text);
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
		System.out.println("\n      Decryption: ");
		XOR(plain_text, Round[10]);
		ShiftRows(plain_text, true);
		SubBytes(plain_text, true);
		for(int i = 9; i >= 1; i--)
		{
			XOR(plain_text, Round[i]);
			MixColumns(plain_text, true);
			ShiftRows(plain_text, true);
			SubBytes(plain_text, true);
		}
		XOR(plain_text, Round[0]); 
		show(plain_text);
		byte result[] = new byte[16];
		int k = 0; 
		for(int j = 0; j < 4; j++)
		{
			for(int i = 0; i < 4; i++)
			{
				result[k] = plain_text[i][j]; 
				k++;
			}
		}
		return result;
	}
	private String Decrypt_block(byte[][] block_16, String str_key)
	{
		byte plain_text[][] = new byte[4][4];
		byte cipher[][] = new byte[4][4];
		byte Round[][][] = new byte[11][4][4];
		
		for(int i = 0; i < 4; i++)
		{
			for(int j = 0; j < 4; j++)
			{
				plain_text[i] = block_16[i];
			}
		}
		
		show(plain_text);
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
		System.out.println("\n      Decryption: ");
		XOR(plain_text, Round[10]);
		ShiftRows(plain_text, true);
		SubBytes(plain_text, true);
		for(int i = 9; i >= 1; i--)
		{
			XOR(plain_text, Round[i]);
			MixColumns(plain_text, true);
			ShiftRows(plain_text, true);
			SubBytes(plain_text, true);
		}
		XOR(plain_text, Round[0]); 
		show(plain_text);
		source_text = hex_to_string(plain_text);
		System.out.println("decrypted text: "  + source_text);
		return source_text;
	}
	public Decrypt(String str_plain_text, String str_key)
	{
		String[] arr = str_plain_text.split(" ");
		int[] cipher_code = new int[arr.length];
		for(int i = 0; i < arr.length; i++)
		{
			cipher_code[i] = Integer.parseInt(arr[i]);
		}
		source_text = "";
		byte[][] temp_block = new byte[4][4];
		for(int i = 0; i < arr.length; i += 16)
		{
			temp_block = get_block(cipher_code, i, i+16);
			source_text = source_text + Decrypt_block(temp_block, str_key);	
		}
	}
	public Decrypt(String key, File sourceFile, String pathOutput)
	{
		try
		{
			convertToHex(key, sourceFile, pathOutput);
		}
		catch(IOException e)
		{
			
		}
	}
    public  void convertToHex(String key, File file, String pathNew) throws IOException {
		InputStream is = new FileInputStream(file);
		FileOutputStream fos = new FileOutputStream(pathNew);
		int bytesCounter = 0;		
		int value = 0;
		int j = 0;
		byte tempBytes[][] = new byte[4][4];
		byte decryptedBytes[] = new byte[16];
		byte currentBytes[] = new byte[16];
		while ((value = is.read()) != -1) {    
			currentBytes[j] = (byte) value; //read 16 bytes
			j++;
		    if(bytesCounter==15) {
		    	tempBytes = getBlock4_4(currentBytes, 16);
		    	decryptedBytes = Decrypt_block_file(tempBytes, key);
		       	WriteFile(fos, decryptedBytes, 16, false);
		       	bytesCounter=0;
		       	j = 0;
		       	for(int i = 0; i < 16; i++)
		       	{
		       		currentBytes[i] = 0;
		       		decryptedBytes[i] = 0;
		       	}
		       		
		    } else{
		        bytesCounter++;
		    }
	       }		
		//if still got content - the last a few bytes
		if(bytesCounter != 0)
		{		
			tempBytes = getBlock4_4(currentBytes, 16);
			decryptedBytes = Decrypt_block_file(tempBytes, key);
		  	WriteFile(fos, decryptedBytes, j, true);
	    }
	        is.close();
	  }
    public static void WriteFile(FileOutputStream fos, byte[] arrayBytes, int length, boolean closeThread) throws IOException
  	{
  		for(int i = 0; i < length; i++)
  		{
  			fos.write(arrayBytes[i]);
  		}
  		if(closeThread)
  	    	  fos.close();
  	}
	private byte[][] get_block(int[] cipher_code, int index1, int index2)
	{
		byte[][] tmp = new byte[4][4];
		int i1 = 0; int j1 = 0 ;
		for(int i = index1; i < index2; i++)
		{
			//System.out.println("cipher_code: "  + cipher_code[i]);
			tmp[i1][j1] = (byte) cipher_code[i];
			i1++;
			if(i1 == 4)
			{
				i1 = 0;
				j1++;
			}
		}
		return tmp;
	}
}
