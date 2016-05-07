package RSA;

import java.math.BigInteger;

public class CreateKeys extends RSA {
	public CreateKeys() {
		createKeys();
	}

	public BigInteger getPublicKey() {
		return e;
	}

	public BigInteger getPrivateKey() {
		return d;
	}
	
	public BigInteger getN() {
		return n;
	}
}
