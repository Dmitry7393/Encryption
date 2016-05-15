package RSA;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

public class EncryptionRSA extends RSA {

	private byte[] sourceTextHex;
	private BigInteger encryptedText;

	// Init public key
	public EncryptionRSA(String e, String n) {
		super.e = new BigInteger(e);
		super.n = new BigInteger(n);
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

	protected void showBytes(byte[] plain_text) {
		StringBuffer strBuffer = new StringBuffer();
		for (int j = 0; j < plain_text.length; j++) {
			strBuffer.setLength(0);
			strBuffer.append(Long.toHexString(plain_text[j]).toUpperCase());

			System.out.print(strBuffer + " ");
			strBuffer.setLength(0);
		}
		System.out.println("");
	}

	protected void showInts(int[] plain_text) {
		StringBuffer strBuffer = new StringBuffer();
		for (int j = 0; j < plain_text.length; j++) {
			strBuffer.setLength(0);
			strBuffer.append(Long.toHexString(plain_text[j]).toUpperCase());

			System.out.print(strBuffer + " ");
			strBuffer.setLength(0);
		}
		System.out.println("");
	}

	private void EncryptBytes(FileOutputStream fos, int currentBytes[]) throws IOException {
		BigInteger number256 = BigInteger.valueOf(256);
		BigInteger bigNumber;
		bigNumber = BigInteger.valueOf(0);
		for (int i = 0; i < 16; i++) {
			bigNumber = bigNumber.add(number256.pow(i).multiply(BigInteger.valueOf(currentBytes[i])));
		}
		//System.out.println("bigNumber: " + bigNumber);
		// Encryption
		BigInteger encryptedBigInt = EncryptWithRSA(bigNumber);
		//System.out.println("encryptedBigInt: " + encryptedBigInt);
		WriteFile(fos, encryptedBigInt);
	}

	public void EncryptFile(String inputFile, String outputPath) throws IOException {
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
		System.out.println();
	}

	public void WriteFile(FileOutputStream fos, BigInteger encryptedBigInt) throws IOException {
		String outputStr;
		if(encryptedBigInt.toString().length() < n.toString().length()) {
			outputStr = "0" + encryptedBigInt.toString();
		} else {
			outputStr = encryptedBigInt.toString();
		}
		byte byteTemp;
		for (int i = 0; i < outputStr.length(); i++) {
			byteTemp = Byte.valueOf(outputStr.substring(i, i + 1));
			fos.write(byteTemp);
		}

	}

	public String getEncryptedText() {
		return encryptedText.toString();
	}

	private BigInteger EncryptWithRSA(BigInteger message) {
		// System.out.println("public key: (e, n) (" + e + "," + n + ")");
		BigInteger c = message.modPow(e, n);
		return c;
	}
}
