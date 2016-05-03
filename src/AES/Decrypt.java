package AES;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Decrypt extends AES implements Runnable {
	private String source_text =  "";
	private  byte Round[][][] = new byte[11][4][4];
	private Thread thread;
	private List<File> sourceFilesList = new ArrayList<File>();
	private List<String> outputPathList = new ArrayList<String>();
	private long CommonSizeOfFiles = 0;
	public String get_text()
	{
		return source_text;
	}
	private void initRoundKey(String str_key)
	{
		/*byte cipher[][] = new byte[4][4];
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
		}*/
	}
	private byte[] Decrypt_block(byte[][] block_16, String typeDecryption)
	{
		byte plain_text[][] = new byte[4][4];
		for(int i = 0; i < 4; i++)
		{
			for(int j = 0; j < 4; j++)
			{
				plain_text[i][j] = block_16[i][j];
			}
		}
		//show(plain_text);
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
	
		//show(plain_text);
		if(typeDecryption.equals("string"))
		{
			source_text += hex_to_string(plain_text);
		}
		byte decryptedText[] = new byte[16];
		int k = 0; 
		for(int j = 0; j < 4; j++)
		{
			for(int i = 0; i < 4; i++)
			{
				decryptedText[k] = plain_text[i][j]; 
				k++;
			}
		}
		return decryptedText;
	}
	public void DecryptText(String str_plain_text)
	{
		source_text = "";
		byte byteList[] = Base64ToByte(str_plain_text);
		byte[][] block16 = new byte[4][4];
		int r = 0;
		for(int i = 0; i < byteList.length; i += 16)
		{
			r = byteList.length - i;
			if(r >= 16)
			{
				block16 = getBlock4_4(byteList, i, i+16);
				Decrypt_block(block16, "string");
			}
			else
			{
				block16 = getBlock4_4(byteList, i, i+r);
				Decrypt_block(block16, "string");
			}
		}
	}
	public void DecryptGroupsOfFiles(List<File> sourceFile, List<String> outputPath)
	{
		sourceFilesList = sourceFile;
		outputPathList = outputPath;
		thread = new Thread(this, "Decryption files");
		thread.start();
	}
	public void DecryptFile(File sourceFile, String pathOutput)
	{
		sourceFilesList.add(sourceFile);
		outputPathList.add(pathOutput);
		thread = new Thread(this, "Decryption file");
		thread.start();
	}
	public Decrypt(String key){
		initRoundKey(key);
		CommonSizeOfFiles = 0;
	}
    public  void convertToHex(File file, String pathNew) throws IOException {
		InputStream is = new FileInputStream(file);
		FileOutputStream fos = new FileOutputStream(pathNew);
		int bytesCounter = 0;		
		int value = 0;
		int j = 0;
		int countBytesInImage = 0;
		byte tempBytes[][] = new byte[4][4];
		byte decryptedBytes[] = new byte[16];
		byte currentBytes[] = new byte[16];
		while ((value = is.read()) != -1) { 
			if(countBytesInImage <= 81980)
				countBytesInImage++;
			if(countBytesInImage > 81980) //pass the image
			{
				currentBytes[j] = (byte) value; //read 16 bytes
				j++;
			    if(bytesCounter==15) {
			    	tempBytes = getBlock4_4(currentBytes, 16);
			    	decryptedBytes = Decrypt_block(tempBytes, "file");
			       	WriteFile(fos, decryptedBytes);
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
			
	       }		
		//if still got content - the last a few bytes
		if(bytesCounter != 0)
		{		
			for(int i = 0; i < 16; i++)
	       	{
	       		decryptedBytes[i] = 0;
	       	}
			tempBytes = getBlock4_4(currentBytes, 16);
			decryptedBytes = Decrypt_block(tempBytes, "file");
		  	WriteFile(fos, decryptedBytes);
	    }
		 fos.close();
	     is.close();
	  }
    public void WriteFile(FileOutputStream fos, byte[] arrayBytes) throws IOException
  	{
  		for(int i = 0; i < 16; i++)
  		{
  			fos.write(arrayBytes[i]);
  		}
  		CommonSizeOfFiles +=  arrayBytes.length; 
  	}
	private byte[][] getBlock4_4(byte[] cipher_code, int index1, int index2)
	{
		byte[][] tmp = new byte[4][4];
		for(int i = 0; i < 4; i++)
		{
			for(int j = 0; j < 4; j++)
			{
				tmp[i][j] = 0;
			}
		}
		int i1 = 0; int j1 = 0 ;
		for(int i = index1; i < index2; i++)
		{
			tmp[i1][j1] = cipher_code[i];
			i1++;
			if(i1 == 4)
			{
				i1 = 0;
				j1++;
			}
		}
		return tmp;
	}
	@Override
	public void run() {
		for(int i = 0; i < sourceFilesList.size(); i++) {
			try
			{
				convertToHex(sourceFilesList.get(i), outputPathList.get(i));
			}
			catch(IOException e){
			}
		}
	}
	public Boolean threadIsAlive() {
		return thread.isAlive();
	}
	public long getCommonSizeOfFiles() {
		return CommonSizeOfFiles;
	}
}
