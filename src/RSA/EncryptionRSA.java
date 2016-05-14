package RSA;

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

	public String getEncryptedText() {
		return encryptedText.toString();
	}

	private BigInteger EncryptWithRSA(BigInteger message) {
		//System.out.println("public key: (e, n) (" + e + "," + n + ")");
		BigInteger c = message.modPow(e, n);
		return c;
	}
}
