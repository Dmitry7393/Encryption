package AES;

public class Decrypt extends AES {
	String source_text =  "";
	private byte[][] get_plain_text(String source_string)
	{
		byte[][] s = new byte[4][4];
		String[] arr = source_string.split(" ");
		
		System.out.println("source_stringd " + source_string);
		int i1 = 0; int j1 = 0 ;
		for (int i = 0; i < arr.length; i++)
		{
			System.out.println("arr: " + arr[i]);
			s[i1][j1] = (byte) Integer.parseInt( arr[i]) ;
			i1++;
			if(i1 == 4)
			{
				i1 = 0;
				j1++;
			}
		}
		return s;
	}
	public String get_text()
	{
		return source_text;
	}
	public Decrypt(String str_plain_text, String str_key)
	{
		byte plain_text[][] = new byte[4][4];
		byte cipher[][] = new byte[4][4];
		byte Round[][][] = new byte[11][4][4];
		
		plain_text = get_plain_text(str_plain_text);
	
		show(plain_text);
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
		System.out.println("\n      Decryption: ");
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
		show(plain_text);
		source_text = hex_to_string(plain_text);
		System.out.println("decrypted text: "  + source_text);
	}
}
