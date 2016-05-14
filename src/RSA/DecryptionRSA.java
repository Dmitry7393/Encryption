package RSA;

import java.math.BigInteger;

public class DecryptionRSA extends RSA {
	private String stringDecryptedText;
	private BigInteger resultDecryption;

	// Init public key
	public DecryptionRSA(String d, String n) {
		super.d = new BigInteger(d);
		super.n = new BigInteger(n);
	}

	private String hexToString(byte[] arrayBytes) {
		String str = "";
		for (int j = 0; j < arrayBytes.length; j++) {
			str = str + (char) arrayBytes[j];
		}
		return str;
	}

	public void DecryptText(String textInteger) {
		System.out.println("textInteger: " + textInteger);
		resultDecryption = DecryptWithRSA(new BigInteger(textInteger));

		BigInteger value255 = BigInteger.valueOf(255);
		byte[] outputBytes = new byte[16];
		for (int i = 0; i < 16; i++) {
			outputBytes[i] = resultDecryption.shiftRight(i * 8).and(value255).byteValue();
			System.out.print(outputBytes[i] + " ");
		}
		stringDecryptedText = hexToString(outputBytes);
	}

	public String getDecryptedText() {
		return stringDecryptedText;
	}

	private BigInteger DecryptWithRSA(BigInteger encryptedMessage) {

		System.out.println("private key: (d, n) (" + d + "," + n + ")");
		BigInteger m_decrypted = encryptedMessage.modPow(d, n);
		return m_decrypted;
	}
}
