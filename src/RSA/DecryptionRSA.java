package RSA;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.List;

public class DecryptionRSA extends RSA {
	private String stringDecryptedText;
	private BigInteger resultDecryption;
	
	private BigInteger nCopy;
	private int countOutPutBytes = 0;
	private byte[] arrayOutPutBytes;
	// Init private key
	public DecryptionRSA(String d, String n) {
		super.d = new BigInteger(d);
		super.n = new BigInteger(n);
		this.nCopy = new BigInteger(n);
		countOutPutBytes = 0;
		while (nCopy.compareTo(BigInteger.ZERO) == 1) {
			nCopy = nCopy.shiftRight(8);
			countOutPutBytes++;
		}
		countOutPutBytes += 1;
		System.out.println("countOutPutBytes from Decrypt = " + countOutPutBytes);
	}

	private BigInteger DecryptWithRSA(BigInteger encryptedMessage) {
		// System.out.println("private key: (d, n) (" + d + "," + n + ")");
		BigInteger m_decrypted = encryptedMessage.modPow(d, n);
		return m_decrypted;
	}

	private String hexToString(byte[] arrayBytes) {
		String str = "";
		for (int j = 0; j < arrayBytes.length; j++) {
			str = str + (char) arrayBytes[j];
		}
		return str;
	}

	public void DecryptText(String textInteger) {
		resultDecryption = DecryptWithRSA(new BigInteger(textInteger));

		BigInteger value255 = BigInteger.valueOf(255);
		byte[] outputBytes = new byte[16];
		for (int i = 0; i < 16; i++) {
			outputBytes[i] = resultDecryption.shiftRight(i * 8).and(value255).byteValue();
		}
		stringDecryptedText = hexToString(outputBytes);
	}

	public String getDecryptedText() {
		return stringDecryptedText;
	}

	private void DecryptLine(FileOutputStream fos, byte[] arrayBytes128) throws IOException {
		BigInteger decryptedBigInt = DecryptWithRSA(new BigInteger(arrayBytes128));
		BigInteger b255 = BigInteger.valueOf(255);
		byte[] tempBytes = new byte[16];
		for (int i = 0; i < 16; i++) {
			tempBytes[i] = decryptedBigInt.shiftRight(i * 8).and(b255).byteValue();
		}
		WriteFile(fos, tempBytes);
	}

	private void createDecryptedFiles(File encryptedFile, String newPath) throws IOException {
		InputStream is = new FileInputStream(encryptedFile);
		FileOutputStream fos = new FileOutputStream(newPath);
		int value = 0;
		int bytesCounter = 0;
		int j = 0;
		arrayOutPutBytes = new byte[countOutPutBytes];
		while ((value = is.read()) != -1) {
			arrayOutPutBytes[j] = (byte) value;
			j++;
			if (bytesCounter == countOutPutBytes-1) {
				showBytes(arrayOutPutBytes);
				DecryptLine(fos, arrayOutPutBytes);
				bytesCounter = 0;
				j = 0;
			} else {
				bytesCounter++;
			}
		}
		// if still got content - the last a few bytes
		if (bytesCounter != 0) {
			DecryptLine(fos, arrayOutPutBytes);
		}
		fos.close();
		is.close();
	}

	public void DecryptFiles(List<File> sourceFile, List<String> outputPath) {
		for (int i = 0; i < sourceFile.size(); i++) {
			try {
				createDecryptedFiles(sourceFile.get(i), outputPath.get(i));
			} catch (IOException e) {
			}
		}
	}

	private void WriteFile(FileOutputStream fos, byte[] arrayBytes) throws IOException {
		for (int i = 0; i < 16; i++) {
			fos.write(arrayBytes[i]);
		}
	}
}
