package RSA;

import java.math.BigInteger;
import java.util.Random;

public class RSA {
	private BigInteger p;
	private BigInteger q;
	private BigInteger phi;
	
	protected BigInteger e;
	protected BigInteger d;
	protected BigInteger n;

	public BigInteger getRandomBigInteger(int pow) {
		Random rand = new Random();
		BigInteger result = new BigInteger(pow, rand); // (2^4-1) = 15 is the
														// maximum value
		return result;
	}

	public BigInteger gcd(BigInteger a, BigInteger b) {
		while (b.compareTo(BigInteger.ZERO) != 0) {
			BigInteger tmp = a.mod(b);
			a = b;
			b = tmp;
		}
		return a;
	}

	protected void createKeys() {
		while (true) {
			p = getRandomBigInteger(512);
			if (p.isProbablePrime(1) == true) {
				break;
			}
		}
		while (true) {
			q = getRandomBigInteger(512);
			if (q.isProbablePrime(1) == true) {
				break;
			}
		}

		//System.out.println("p = " + p);
		//System.out.println("q = " + q);

		n = p.multiply(q);
		phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
		//System.out.println("phi = " + phi);

		// Create e 1 < e < phi, e and n are coprime
		BigInteger randomBigInteger;
		while (true) {
			randomBigInteger = getRandomBigInteger(32);
			if (gcd(randomBigInteger, phi).equals(BigInteger.ONE) && randomBigInteger.compareTo(BigInteger.ONE) > 0
					&& randomBigInteger.compareTo(phi) < 0) {
				e = randomBigInteger;
				break;
			}
		}

		// Search d
		d = e.modInverse(phi);
		//System.out.println("d = " + d);
	}



}
