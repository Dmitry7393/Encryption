package RSA;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.List;

public class EncryptionRSA extends RSA {

	private byte[] sourceTextHex;
	private BigInteger encryptedText;
	private BigInteger nCopy;
	private int countOutPutBytes = 0;
	private byte[] arrayOutPutBytes;

	// Init public key
	public EncryptionRSA(String e, String n) {
		super.e = new BigInteger(e);
		super.n = new BigInteger(n);
		this.nCopy = new BigInteger(n);
		countOutPutBytes = 0;
		while (nCopy.compareTo(BigInteger.ZERO) == 1) {
			nCopy = nCopy.shiftRight(8);
			countOutPutBytes++;
		}
		countOutPutBytes += 1;
		arrayOutPutBytes = new byte[countOutPutBytes];
		System.out.println("countOutPutBytes from Encrypt = " + countOutPutBytes);

	}

	private BigInteger EncryptWithRSA(BigInteger message) {
		// System.out.println("public key: (e, n) (" + e + "," + n + ")");
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
		BigInteger number256 = BigInteger.valueOf(256);
		BigInteger bigNumber;
		bigNumber = BigInteger.valueOf(0);
		// Translate 16 bytes to bigInt
		for (int i = 0; i < 16; i++) {
			bigNumber = bigNumber.add(number256.pow(i).multiply(BigInteger.valueOf(currentBytes[i])));
		}
		BigInteger encryptedBigInt = EncryptWithRSA(bigNumber);
		// System.out.println("encryptedBigInt " +
		// encryptedBigInt.toString().length());
		WriteFile(fos, encryptedBigInt);
	}

	private void creatingNewFiles(File inputFile, String outputPath) throws IOException {
		InputStream is = new FileInputStream(inputFile);
		FileOutputStream fos = new FileOutputStream(outputPath);
		int value = 0;
		int j = 0;
		int bytesCounter = 0;
		int currentBytes[] = new int[16];
		while ((value = is.read()) != -1) {
			currentBytes[j] = value; // read 16 bytes from file
			j++;
			if (bytesCounter == 15) {
				EncryptBytes(fos, currentBytes);
				bytesCounter = 0;
				j = 0;
				for (int i = 0; i < 16; i++) {
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
		for (int i = 0; i < sourceFile.size(); i++) {
			try {
				creatingNewFiles(sourceFile.get(i), outputPath.get(i));
			} catch (IOException e) {
			}
		}
	}

	private void WriteFile(FileOutputStream fos, BigInteger encryptedBigInt) throws IOException {
		arrayOutPutBytes = encryptedBigInt.toByteArray();
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
}
