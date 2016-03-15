package AES;

public class Encrypt extends AES {
	private String whole_ciphertext = "";
	
	private String Encrypt_block(String str_plain_text, String str_key)
	{
		byte plain_text[][] = new byte[4][4];
		byte cipher[][] = new byte[4][4];
		byte Round[][][] = new byte[11][4][4];
		
		
		plain_text = toHex(str_plain_text);
		cipher = toHex(str_key);
		System.out.println("Plaintext: ");
		show(plain_text);
		
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
		XOR(plain_text, Round[0]);
		for(int i = 1; i < 10; i++)
		{
			SubBytes(plain_text, false);
			ShiftRows(plain_text, false);
			MixColumns(plain_text, false);
			XOR(plain_text, Round[i]);  
		}
		//AES Round 10 no Mix columns 
		SubBytes(plain_text, false);
		ShiftRows(plain_text, false);
		XOR(plain_text, Round[10]);
		
		System.out.println("Ciphertext: ");
		show(plain_text);
		write_to_file(plain_text, "D:/ciphertext.txt");
		
		String cipher_text = "";
		for(int j = 0; j < 4; j++)
		{
			for(int i = 0; i < 4; i++)
			{
				cipher_text = cipher_text + plain_text[i][j] + " ";
			}
		}
		return cipher_text;
	}
	public Encrypt(String str_plain_text, String str_key) {
		int r = 0;
		whole_ciphertext = "";
		for(int i = 0; i < str_plain_text.length(); i += 16)
		{
			r = str_plain_text.length() - i;
			if(r >= 16)
			{
				whole_ciphertext = whole_ciphertext + Encrypt_block(str_plain_text.substring(i, i+16), str_key);
			}
			else
			{
				whole_ciphertext = whole_ciphertext + Encrypt_block(str_plain_text.substring(i, i+r), str_key);
			}
		}
	}
	public String get_ciphertext()
	{
		return whole_ciphertext;
	}

}

