package AES;

public class Decrypt extends AES {
	private String source_text =  "";
	public String get_text()
	{
		return source_text;
	}
	private String Decrypt_block(byte[][] block_16, String str_key)
	{
		byte plain_text[][] = new byte[4][4];
		byte cipher[][] = new byte[4][4];
		byte Round[][][] = new byte[11][4][4];
		
		for(int i = 0; i < 4; i++)
		{
			for(int j = 0; j < 4; j++)
			{
				plain_text[i] = block_16[i];
			}
		}
		
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
		return source_text;
	}
	public Decrypt(String str_plain_text, String str_key)
	{
		String[] arr = str_plain_text.split(" ");
		int[] cipher_code = new int[arr.length];
		for(int i = 0; i < arr.length; i++)
		{
			cipher_code[i] = Integer.parseInt(arr[i]);
		}
		source_text = "";
		byte[][] temp_block = new byte[4][4];
		for(int i = 0; i < arr.length; i += 16)
		{
			temp_block = get_block(cipher_code, i, i+16);
			source_text = source_text + Decrypt_block(temp_block, str_key);	
		}
	}
	private byte[][] get_block(int[] cipher_code, int index1, int index2)
	{
		byte[][] tmp = new byte[4][4];
		int i1 = 0; int j1 = 0 ;
		for(int i = index1; i < index2; i++)
		{
			//System.out.println("cipher_code: "  + cipher_code[i]);
			tmp[i1][j1] = (byte) cipher_code[i];
			i1++;
			if(i1 == 4)
			{
				i1 = 0;
				j1++;
			}
		}
		return tmp;
	}
}
