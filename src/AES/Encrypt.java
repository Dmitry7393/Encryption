package AES;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Encrypt extends AES implements Runnable {
	Thread thread;
	private String cipherTextBase64 = "";
	private byte Round[][][] = new byte[11][4][4];
	private ArrayList<Byte> arrayListBytes = new ArrayList<Byte>();
	private List<File> sourceFilesList = new ArrayList<File>();
	private List<String> outputPathsList = new ArrayList<String>();
	private long CommonSizeOfFiles = 0;
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
	private byte[] Encrypt_block(byte[][] plain_text, String typeEncryption)
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
		//write_to_file(plain_text, "D:/ciphertext.txt");
		
		byte[] ciphertextBytes = new byte[16];
		if(typeEncryption.equals("file"))
		{
			int k = 0; 
			for(int j = 0; j < 4; j++)
			{
				for(int i = 0; i < 4; i++)
				{
					ciphertextBytes[k] = plain_text[i][j]; 
					k++;
				}
			}
		}
		if(typeEncryption.equals("string")) //encrypt text
		{
	        for(int j = 0; j < 4; j++)
	        {
	            for(int i = 0; i < 4; i++)
	            {
	            	arrayListBytes.add(plain_text[i][j]);
	            }
	        } 
		}
		return ciphertextBytes;
	}
	public void EncryptText(String str_plain_text)
	{
		int r = 0;
		cipherTextBase64 = "";
		for(int i = 0; i < str_plain_text.length(); i += 16)
		{
			r = str_plain_text.length() - i;
			if(r >= 16)
			{
				Encrypt_block(toHex(str_plain_text.substring(i, i+16)), "string");
			}
			else
			{
				Encrypt_block(toHex(str_plain_text.substring(i, i+r)), "string");
			}
		}
	} 
	public void EncryptGroupsOfFiles(List<File> sourceFile, List<String> outputPath)
	{
		this.sourceFilesList = sourceFile;
		this.outputPathsList = outputPath;
		thread = new Thread(this, "Encryption file");
		System.out.println("new thread for multiple files was created " + thread );
		thread.start();
	}
	public void EncryptFile(File sourceFile, String outputFile)
	{
		sourceFilesList.add(sourceFile);
		outputPathsList.add(outputFile);
		thread = new Thread(this, "Encryption file");
		System.out.println("new thread for one file was created " + thread );
		thread.start();
	}
	public Encrypt(String key){
		initRoundKey(key);
		CommonSizeOfFiles = 81980;
	}
    public  void convertToHex(File file, String pathNew) throws IOException {
		InputStream is = new FileInputStream(file);
		FileOutputStream fos = new FileOutputStream(pathNew);	
		int value = 0;

		//First write bytes from image imageEncryptedFile.png to new file
		 InputStream inputStreamImage = Encrypt.class.getResourceAsStream("/resources/imageEncryptedFile.png");
		 while ((value = inputStreamImage.read()) != -1) {    
			fos.write((byte) value);
		}
		inputStreamImage.close();
		
		int j = 0;
		int bytesCounter = 0;
		value = 0;
		byte block4_4[][] = new byte[4][4];
		byte encryptedBytes[] = new byte[16];
		byte currentBytes[] = new byte[16];
		while ((value = is.read()) != -1) {    
			currentBytes[j] = (byte) value; //read 16 bytes
			j++;
		    if(bytesCounter==15) {
		    	block4_4 = getBlock4_4(currentBytes, 16);
		    	encryptedBytes = Encrypt_block(block4_4, "file");
		       	WriteFile(fos, encryptedBytes);
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
			encryptedBytes = Encrypt_block(block4_4, "file");
		  	WriteFile(fos, encryptedBytes);
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
	public String getCipherText()
	{
		byte tempBytes[] = new byte[arrayListBytes.size()];
		for(int i = 0; i < arrayListBytes.size(); i++)
		{
			tempBytes[i] = arrayListBytes.get(i);
		}
		cipherTextBase64 += ConvertToBase64(tempBytes);
		return cipherTextBase64;
	}
	@Override
	public void run() {
		for(int i = 0; i < sourceFilesList.size(); i++)
		{
			try {
				convertToHex(sourceFilesList.get(i), outputPathsList.get(i));
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

