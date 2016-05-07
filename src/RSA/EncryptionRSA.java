package RSA;

import java.math.BigInteger;

public class EncryptionRSA extends RSA implements Runnable {
	private Thread thread;
	private int[] sourceTextHex;
	private BigInteger encryptedText;
	
	private int[] getHexCode(String text) {
		int block16Byte[] = new int[text.length()];
		String convert;
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			int ascii = (int) c;
			convert = Integer.toHexString(ascii);
			block16Byte[i] = (byte) Integer.parseInt(convert, 16);
		}
		return block16Byte;
	}

	private void showBytes(int[] text16) {
		StringBuffer strBuffer = new StringBuffer();
		for (int i = 0; i < text16.length; i++) {
			strBuffer.setLength(0);
			strBuffer.append(Long.toHexString(text16[i]).toUpperCase());

			System.out.print(strBuffer + " ");
			strBuffer.setLength(0);
		}

	}

	public void EncryptText(String text) {
		sourceTextHex = getHexCode(text);
		showBytes(sourceTextHex); // just show in console
		thread = new Thread(this, "Encrypt with RSA");
		thread.start();
	}

	@Override
	public void run() {
		String strSourceText = "";
		for (int i = 0; i < sourceTextHex.length; i++) {
			strSourceText += Integer.toString(sourceTextHex[i]);
		}

		System.out.println("strSourceText " + strSourceText);
		encryptedText = EncryptWithRSA(strSourceText);
	}

}
