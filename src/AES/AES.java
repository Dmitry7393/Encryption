package AES;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Base64;

public class AES {
	protected static byte RC[] = new byte[] { 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, (byte) 0x80, 0x1b, 0x36 };
	// Precomputed Rijndael S-BOX
	private static final char sbox[] = { 0x63, 0x7c, 0x77, 0x7b, 0xf2, 0x6b, 0x6f, 0xc5, 0x30, 0x01, 0x67, 0x2b, 0xfe,
			0xd7, 0xab, 0x76, 0xca, 0x82, 0xc9, 0x7d, 0xfa, 0x59, 0x47, 0xf0, 0xad, 0xd4, 0xa2, 0xaf, 0x9c, 0xa4, 0x72,
			0xc0, 0xb7, 0xfd, 0x93, 0x26, 0x36, 0x3f, 0xf7, 0xcc, 0x34, 0xa5, 0xe5, 0xf1, 0x71, 0xd8, 0x31, 0x15, 0x04,
			0xc7, 0x23, 0xc3, 0x18, 0x96, 0x05, 0x9a, 0x07, 0x12, 0x80, 0xe2, 0xeb, 0x27, 0xb2, 0x75, 0x09, 0x83, 0x2c,
			0x1a, 0x1b, 0x6e, 0x5a, 0xa0, 0x52, 0x3b, 0xd6, 0xb3, 0x29, 0xe3, 0x2f, 0x84, 0x53, 0xd1, 0x00, 0xed, 0x20,
			0xfc, 0xb1, 0x5b, 0x6a, 0xcb, 0xbe, 0x39, 0x4a, 0x4c, 0x58, 0xcf, 0xd0, 0xef, 0xaa, 0xfb, 0x43, 0x4d, 0x33,
			0x85, 0x45, 0xf9, 0x02, 0x7f, 0x50, 0x3c, 0x9f, 0xa8, 0x51, 0xa3, 0x40, 0x8f, 0x92, 0x9d, 0x38, 0xf5, 0xbc,
			0xb6, 0xda, 0x21, 0x10, 0xff, 0xf3, 0xd2, 0xcd, 0x0c, 0x13, 0xec, 0x5f, 0x97, 0x44, 0x17, 0xc4, 0xa7, 0x7e,
			0x3d, 0x64, 0x5d, 0x19, 0x73, 0x60, 0x81, 0x4f, 0xdc, 0x22, 0x2a, 0x90, 0x88, 0x46, 0xee, 0xb8, 0x14, 0xde,
			0x5e, 0x0b, 0xdb, 0xe0, 0x32, 0x3a, 0x0a, 0x49, 0x06, 0x24, 0x5c, 0xc2, 0xd3, 0xac, 0x62, 0x91, 0x95, 0xe4,
			0x79, 0xe7, 0xc8, 0x37, 0x6d, 0x8d, 0xd5, 0x4e, 0xa9, 0x6c, 0x56, 0xf4, 0xea, 0x65, 0x7a, 0xae, 0x08, 0xba,
			0x78, 0x25, 0x2e, 0x1c, 0xa6, 0xb4, 0xc6, 0xe8, 0xdd, 0x74, 0x1f, 0x4b, 0xbd, 0x8b, 0x8a, 0x70, 0x3e, 0xb5,
			0x66, 0x48, 0x03, 0xf6, 0x0e, 0x61, 0x35, 0x57, 0xb9, 0x86, 0xc1, 0x1d, 0x9e, 0xe1, 0xf8, 0x98, 0x11, 0x69,
			0xd9, 0x8e, 0x94, 0x9b, 0x1e, 0x87, 0xe9, 0xce, 0x55, 0x28, 0xdf, 0x8c, 0xa1, 0x89, 0x0d, 0xbf, 0xe6, 0x42,
			0x68, 0x41, 0x99, 0x2d, 0x0f, 0xb0, 0x54, 0xbb, 0x16 };

	// Precomputed inverted Rijndael S-BOX
	private static final char rsbox[] = { 0x52, 0x09, 0x6a, 0xd5, 0x30, 0x36, 0xa5, 0x38, 0xbf, 0x40, 0xa3, 0x9e, 0x81,
			0xf3, 0xd7, 0xfb, 0x7c, 0xe3, 0x39, 0x82, 0x9b, 0x2f, 0xff, 0x87, 0x34, 0x8e, 0x43, 0x44, 0xc4, 0xde, 0xe9,
			0xcb, 0x54, 0x7b, 0x94, 0x32, 0xa6, 0xc2, 0x23, 0x3d, 0xee, 0x4c, 0x95, 0x0b, 0x42, 0xfa, 0xc3, 0x4e, 0x08,
			0x2e, 0xa1, 0x66, 0x28, 0xd9, 0x24, 0xb2, 0x76, 0x5b, 0xa2, 0x49, 0x6d, 0x8b, 0xd1, 0x25, 0x72, 0xf8, 0xf6,
			0x64, 0x86, 0x68, 0x98, 0x16, 0xd4, 0xa4, 0x5c, 0xcc, 0x5d, 0x65, 0xb6, 0x92, 0x6c, 0x70, 0x48, 0x50, 0xfd,
			0xed, 0xb9, 0xda, 0x5e, 0x15, 0x46, 0x57, 0xa7, 0x8d, 0x9d, 0x84, 0x90, 0xd8, 0xab, 0x00, 0x8c, 0xbc, 0xd3,
			0x0a, 0xf7, 0xe4, 0x58, 0x05, 0xb8, 0xb3, 0x45, 0x06, 0xd0, 0x2c, 0x1e, 0x8f, 0xca, 0x3f, 0x0f, 0x02, 0xc1,
			0xaf, 0xbd, 0x03, 0x01, 0x13, 0x8a, 0x6b, 0x3a, 0x91, 0x11, 0x41, 0x4f, 0x67, 0xdc, 0xea, 0x97, 0xf2, 0xcf,
			0xce, 0xf0, 0xb4, 0xe6, 0x73, 0x96, 0xac, 0x74, 0x22, 0xe7, 0xad, 0x35, 0x85, 0xe2, 0xf9, 0x37, 0xe8, 0x1c,
			0x75, 0xdf, 0x6e, 0x47, 0xf1, 0x1a, 0x71, 0x1d, 0x29, 0xc5, 0x89, 0x6f, 0xb7, 0x62, 0x0e, 0xaa, 0x18, 0xbe,
			0x1b, 0xfc, 0x56, 0x3e, 0x4b, 0xc6, 0xd2, 0x79, 0x20, 0x9a, 0xdb, 0xc0, 0xfe, 0x78, 0xcd, 0x5a, 0xf4, 0x1f,
			0xdd, 0xa8, 0x33, 0x88, 0x07, 0xc7, 0x31, 0xb1, 0x12, 0x10, 0x59, 0x27, 0x80, 0xec, 0x5f, 0x60, 0x51, 0x7f,
			0xa9, 0x19, 0xb5, 0x4a, 0x0d, 0x2d, 0xe5, 0x7a, 0x9f, 0x93, 0xc9, 0x9c, 0xef, 0xa0, 0xe0, 0x3b, 0x4d, 0xae,
			0x2a, 0xf5, 0xb0, 0xc8, 0xeb, 0xbb, 0x3c, 0x83, 0x53, 0x99, 0x61, 0x17, 0x2b, 0x04, 0x7e, 0xba, 0x77, 0xd6,
			0x26, 0xe1, 0x69, 0x14, 0x63, 0x55, 0x21, 0x0c, 0x7d };
	private byte[][] mRoundKeys;
	private int countRoundKeys;

	protected static void XOR(byte matrix1[][], byte matrix2[][]) {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				matrix1[i][j] ^= matrix2[i][j];
			}
		}
	}

	// AES SubBytes and InvSubBytes passes
	static byte[][] SubBytes(byte[][] state, boolean inverted) {
		// Select the correct s-box, either inverted or not.
		char[] _sbox = inverted ? rsbox : sbox;
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				state[i][j] = (byte) _sbox[state[i][j] & 0xFF];
		// The mask is used to shift the byte value to the unsigned (positive)
		// one
		return state;
	}

	static byte[] SubBytes2(byte[] state, boolean inverted) {
		// Select the correct s-box, either inverted or not.
		char[] _sbox = inverted ? rsbox : sbox;
		for (int i = 0; i < 4; i++)
			// for (int j = 0; j < 4; j++)
			state[i] = (byte) _sbox[state[i] & 0xFF];
		// The mask is used to shift the byte value to the unsigned (positive)
		// one
		return state;
	}

	// AES ShiftRow and InvShiftRow passes
	protected static byte[][] ShiftRows(byte[][] state, boolean inverted) {
		byte[] t = new byte[4];
		for (int i = 1; i < 4; i++) {
			for (int j = 0; j < 4; j++)
				t[inverted ? (j + i) % 4 : j] = state[i][!inverted ? (j + i) % 4 : j];
			for (int j = 0; j < 4; j++)
				state[i][j] = t[j];
		}
		return state;
	}

	protected static void show(byte[][] plain_text) {
		StringBuffer strBuffer = new StringBuffer();
		for (int j = 0; j < 4; j++) {
			for (int i = 0; i < 4; i++) {
				strBuffer.setLength(0);
				strBuffer.append(Long.toHexString(plain_text[i][j]).toUpperCase());

				System.out.print(strBuffer + " ");
				strBuffer.setLength(0);
			}
		}
		System.out.println("");
	}

	/*
	 * private static void show2(byte[] plain_text) { StringBuffer strBuffer =
	 * new StringBuffer(); for(int i = 0; i < plain_text.length; i++) {
	 * strBuffer.setLength(0);
	 * strBuffer.append(Long.toHexString(plain_text[i]).toUpperCase());
	 * System.out.print(strBuffer + " "); strBuffer.setLength(0); }
	 * System.out.println(""); }
	 */
	private static byte[] XOR2(byte[] w, byte[] gw) {
		byte temp[] = new byte[4];
		for (int i = 0; i < 4; i++) {
			temp[i] = (byte) (w[i] ^ gw[i]);
		}
		return temp;
	}

	// Multiplication function over GF(2^8) used in the MixColumns pass
	public static byte GFMult(byte a, byte b) {
		byte r = 0, t;
		while (a != 0) {
			if ((a & 1) != 0)
				r = (byte) (r ^ b);
			t = (byte) (b & 0x80);
			b = (byte) (b << 1);
			if (t != 0)
				b = (byte) (b ^ 0x1b);
			a = (byte) ((a & 0xff) >> 1);
		}
		return r;
	}

	// AES MixColums and InvMixColumns passes
	protected static byte[][] MixColumns(byte[][] state, boolean inverted) {
		int[] tmp = new int[4];
		// In this way I can use a single method to do both the inverted and the
		// straight version. I choose the correct first operand of the
		// multiplication by checking the boolean "inverted" flag.
		byte a = (byte) (inverted ? 0x0b : 0x03);
		byte b = (byte) (inverted ? 0x0d : 0x01);
		byte c = (byte) (inverted ? 0x09 : 0x01);
		byte d = (byte) (inverted ? 0x0e : 0x02);

		for (int i = 0; i < 4; i++) {
			tmp[0] = GFMult(d, state[0][i]) ^ GFMult(a, state[1][i]) ^ GFMult(b, state[2][i]) ^ GFMult(c, state[3][i]);
			tmp[1] = GFMult(c, state[0][i]) ^ GFMult(d, state[1][i]) ^ GFMult(a, state[2][i]) ^ GFMult(b, state[3][i]);
			tmp[2] = GFMult(b, state[0][i]) ^ GFMult(c, state[1][i]) ^ GFMult(d, state[2][i]) ^ GFMult(a, state[3][i]);
			tmp[3] = GFMult(a, state[0][i]) ^ GFMult(b, state[1][i]) ^ GFMult(c, state[2][i]) ^ GFMult(d, state[3][i]);
			for (int j = 0; j < 4; j++)
				state[j][i] = (byte) (tmp[j]);
		}

		return state;
	}

	protected static String hex_to_string(byte[][] text) {
		String str = "";
		for (int j = 0; j < 4; j++) {
			for (int i = 0; i < 4; i++) {
				str = str + (char) text[i][j];
			}
		}
		return str;
	}

	protected byte[] StringKeyToHex(String text, int keySize) // 16 byte
	{
		byte temp[] = new byte[keySize];
		String convert;
		int k = 0;
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			int ascii = (int) c;
			convert = Integer.toHexString(ascii);
			temp[k] = (byte) Integer.parseInt(convert, 16);
			k++;
		}
		return temp;
	}

	protected byte[][] getBlock16(String text) // 16 byte
	{
		byte temp[][] = new byte[4][4];
		String convert;
		int i1 = 0;
		int j1 = 0;
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			int ascii = (int) c;
			convert = Integer.toHexString(ascii);
			temp[i1][j1] = (byte) Integer.parseInt(convert, 16);
			i1++;
			if (i1 == 4) {
				i1 = 0;
				j1++;
			}
		}
		return temp;
	}

	public static void write_to_file(byte[][] arr, String path) {

		try (FileWriter writer = new FileWriter(path, false)) {
			for (int j = 0; j < 4; j++) {
				for (int i = 0; i < 4; i++) {
					writer.write(Integer.toString(arr[i][j]));
					writer.write(" ");
				}
			}

		} catch (IOException ex) {

			System.out.println(ex.getMessage());
		}
	}

	private byte[] generateRoundKeys(byte[] cipher, int keySize, int NB, int Nk, int Nr, byte round_constant) {
		// Nk can be 4, 6, 8
		byte[][] w = new byte[Nk][4];
		int wIndex = 0;
		for (int i = 0; i < keySize; i += NB) { // we choose 4 bytes
			for (int j = 0; j < 4; j++) {
				w[wIndex][j] = cipher[i + j];
			}
			wIndex++;
		}
		byte[] gwLast = new byte[] { w[Nk - 1][1], w[Nk - 1][2], w[Nk - 1][3], w[Nk - 1][0] };
		SubBytes2(gwLast, false);
		gwLast[0] ^= round_constant;

		byte[][] wNew = new byte[Nk][4];
		wIndex = 0;
		for (int i = 0; i < Nk; i++) {
			if (i == 0) {
				wNew[wIndex] = XOR2(w[i], gwLast);
			} else {
				wNew[wIndex] = XOR2(wNew[i - 1], w[i]);
			}
			wIndex++;
		}
		// General round keys
		for (int i = 0; i < Nk; i++) {
			mRoundKeys[countRoundKeys] = wNew[i];
			countRoundKeys++;
		}

		byte keyCurrentRound[] = new byte[keySize];
		int k = 0;
		for (int i = 0; i < Nk; i++) {
			for (int j = 0; j < 4; j++) {
				keyCurrentRound[k] = wNew[i][j];
				k++;
			}
		}
		return keyCurrentRound;
	}

	protected byte[][][] initRoundKeys(String keyHexString, int keySize, int NB, int Nk, int Nr) {
		int lengthRoundKeys = (Nr + 1) * 4;
		if (Nk == 6)
			lengthRoundKeys = (Nr + 1) * 4 + 2;
		if (Nk == 8)
			lengthRoundKeys = (Nr + 1) * 4 + 4;
		mRoundKeys = new byte[lengthRoundKeys][NB]; // general array
		countRoundKeys = 0;

		byte keyHex[] = new byte[keySize];
		keyHex = StringKeyToHex(keyHexString, keySize);

		// Test for 128 bit key
		// byte keyHex[] = new byte[] {0x2b, 0x7e, 0x15, 0x16, 0x28, (byte)
		// 0xae, (byte) 0xd2, (byte) 0xa6, (byte) 0xab, (byte) 0xf7, 0x15,
		// (byte) 0x88, 0x09, (byte) 0xcf, 0x4f, 0x3c };

		// Test for 192 bit key
		// byte keyHex[] = new byte[] {(byte) 0x8e, 0x73, (byte) 0xb0, (byte)
		// 0xf7, (byte) 0xda, 0x0e, 0x64, 0x52, (byte) 0xc8, 0x10, (byte) 0xf3,
		// 0x2b,
		// (byte) 0x80, (byte) 0x90, 0x79, (byte) 0xe5, 0x62, (byte) 0xf8,
		// (byte) 0xea, (byte) 0xd2, 0x52, 0x2c, 0x6b, 0x7b};
		// Test for 256 bit key
		// byte keyHex[] = new byte[] {0x60, 0x3d, (byte) 0xeb, 0x10, 0x15,
		// (byte) 0xca, 0x71, (byte) 0xbe, 0x2b, 0x73, (byte) 0xae, (byte) 0xf0,
		// (byte) 0x85, 0x7d, 0x77, (byte) 0x81,
		// 0x1f, 0x35, 0x2c, 0x07, 0x3b, 0x61, 0x08, (byte) 0xd7, 0x2d, (byte)
		// 0x98, 0x10, (byte) 0xa3, 0x09, 0x14, (byte) 0xdf, (byte) 0xf4};

		// init key for Round 0 +
		for (int i = 0; i < keyHex.length; i += NB) { // we choose 4 bytes
			for (int j = 0; j < NB; j++) {
				mRoundKeys[countRoundKeys][j] = keyHex[i + j];
			}
			countRoundKeys++;
		}
		// Remaining Round keys
		byte keyHexRound[] = new byte[keySize];
		for (int i = 0; i < keySize; i++) {
			keyHexRound[i] = keyHex[i];
		}

		int countRound = 0;
		if (Nr == 10)
			countRound = 11;
		if (Nr == 12)
			countRound = 9;
		if (Nr == 14)
			countRound = 8;
		for (int i = 1; i < countRound; i++) {
			keyHexRound = generateRoundKeys(keyHexRound, keySize, NB, Nk, Nr, RC[i - 1]);
		}
		byte Round[][][] = new byte[Nr + 1][4][4];
		int i1 = 0;
		int j1 = 0;
		int roundNumber = 0;
		for (int i = 0; i < (Nr + 1) * 4; i++) {
			i1 = 0;
			for (int j = 0; j < 4; j++) {
				Round[roundNumber][i1][j1] = mRoundKeys[i][j];
				i1++; // next row
			}
			j1++; // next column
			if (j1 == 4) {
				roundNumber++;
				j1 = 0; // again first column
			}
		}
		return Round;
	}

	protected byte[][] getBlock4_4(byte[] block, int size) {
		byte temp[][] = new byte[4][4];
		int i1 = 0;
		int j1 = 0;
		for (int i = 0; i < size; i++) {
			temp[i1][j1] = block[i];
			i1++;
			if (i1 == 4) {
				i1 = 0; // new line
				j1++; // move to next column
			}
		}
		return temp;
	}

	public static String ConvertToBase64(byte myByteArray[]) {
		Base64.Encoder myEncoder = Base64.getEncoder().withoutPadding();
		String base64String = myEncoder.encodeToString(myByteArray);
		return base64String;
	}

	public static byte[] Base64ToByte(String myText) {
		byte[] decoded = Base64.getDecoder().decode(myText);
		return decoded;
	}
}
