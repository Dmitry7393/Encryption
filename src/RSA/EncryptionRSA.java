package RSA;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class EncryptionRSA extends RSA implements Runnable {

	private byte[] sourceTextHex;
	private BigInteger encryptedText;
	private BigInteger nCopy;
	private int countOutPutBytes = 0;
	private byte[] arrayOutPutBytes;
	private BigInteger number256;
	private BigInteger bigNumber;
	private Thread thread;
	private List<File> sourceFilesList = new ArrayList<File>();
	private List<String> outputPathsList = new ArrayList<String>();
	// Get count of bytes that we have read
	private long currentSizeOfFiles = 0;

	// Init public key
	public EncryptionRSA(String e, String n) {
		super.e = new BigInteger(e);
		super.n = new BigInteger(n);
		number256 = BigInteger.valueOf(256);
		this.nCopy = new BigInteger(n);
		countOutPutBytes = 0;
		while (nCopy.compareTo(BigInteger.ZERO) == 1) {
			nCopy = nCopy.shiftRight(8);
			countOutPutBytes++;
		}
		countOutPutBytes += 1;
	}

	private BigInteger EncryptWithRSA(BigInteger message) {
		BigInteger c = message.modPow(e, n);
		return c;
	}

	private byte[] getHexCode(String text) {
		byte block16Byte[] = new byte[text.length()];
		String convert;
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			int ascii = (int) c;
			convert = Integer.toHexString(ascii);
			block16Byte[i] = (byte) Integer.parseInt(convert, 16);
		}
		return block16Byte;
	}

	public void EncryptText(String text) {
		sourceTextHex = getHexCode(text);
		BigInteger bigNumber = BigInteger.valueOf(0);
		BigInteger number256 = BigInteger.valueOf(256);
		for (int i = 0; i < sourceTextHex.length; i++) {
			bigNumber = bigNumber.add(number256.pow(i).multiply(BigInteger.valueOf(sourceTextHex[i])));
		}
		encryptedText = EncryptWithRSA(bigNumber);
	}

	private void EncryptBytes(FileOutputStream fos, int currentBytes[]) throws IOException {
		bigNumber = BigInteger.valueOf(0);
		// Translate 16 bytes to bigInt
		for (int i = 0; i < currentBytes.length; i++) {
			bigNumber = bigNumber.add(number256.pow(i).multiply(BigInteger.valueOf(currentBytes[i])));
		}
		WriteFile(fos, EncryptWithRSA(bigNumber));
	}

	private void creatingNewFiles(File inputFile, String outputPath) throws IOException {
		InputStream is = new FileInputStream(inputFile);
		FileOutputStream fos = new FileOutputStream(outputPath);
		int value = 0;
		int j = 0;
		int bytesCounter = 0;
		int currentBytes[] = new int[SIZE_BLOCK];
		while ((value = is.read()) != -1) {
			currentBytes[j] = value; // read SIZE_BLOCK bytes from file
			j++;
			if (bytesCounter == SIZE_BLOCK - 1) {
				currentSizeOfFiles += SIZE_BLOCK;
				EncryptBytes(fos, currentBytes);
				bytesCounter = 0;
				j = 0;
				for (int i = 0; i < SIZE_BLOCK; i++) {
					currentBytes[i] = 0;
				}
			} else {
				bytesCounter++;
			}
		}
		// if still got content - the last a few bytes
		if (bytesCounter != 0) {
			EncryptBytes(fos, currentBytes);
		}
		fos.close();
		is.close();
	}

	public void EncryptFiles(List<File> sourceFile, List<String> outputPath) {
		this.sourceFilesList = sourceFile;
		this.outputPathsList = outputPath;
		thread = new Thread(this, "Encryption RSA");
		thread.start();
	}

	private void WriteFile(FileOutputStream fos, BigInteger encryptedBigInt) throws IOException {
		arrayOutPutBytes = encryptedBigInt.toByteArray();
		// System.out.println("l " + arrayOutPutBytes.length);
		for (int i = 0; i < countOutPutBytes - arrayOutPutBytes.length; i++) {
			fos.write(0);
		}
		for (int i = 0; i < arrayOutPutBytes.length; i++) {
			fos.write(arrayOutPutBytes[i]);
		}
	}

	public String getEncryptedText() {
		return encryptedText.toString();
	}

	@Override
	public void run() {
		for (int i = 0; i < sourceFilesList.size(); i++) {
			try {
				creatingNewFiles(sourceFilesList.get(i), outputPathsList.get(i));
			} catch (IOException e) {
			}
		}
	}

	public long getCurrentSizeOfEncryptedFiles() {
		return currentSizeOfFiles;
	}

	public Boolean threadIsAlive() {
		return thread.isAlive();
	}
}
