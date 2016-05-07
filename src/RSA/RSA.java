package RSA;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

public class RSA implements Runnable {
	private ArrayList<BigInteger> arrayBigIntE = new ArrayList<BigInteger>();
	private ArrayList<BigInteger> arrayBigIntD = new ArrayList<BigInteger>();
	private Thread thread;

	public BigInteger getRandomBigInteger(int pow) {
		Random rand = new Random();
		BigInteger result = new BigInteger(pow, rand); // (2^4-1) = 15 is the
														// maximum value
		return result;
	}

	private int Random(int Min, int Max) {
		return (Min + (int) (Math.random() * ((Max - Min) + 1)));
	}

	public BigInteger gcd(BigInteger a, BigInteger b) {
		while (b.compareTo(BigInteger.ZERO) != 0) {
			BigInteger tmp = a.mod(b);
			a = b;
			b = tmp;
		}
		return a;
	}

	public void test() {
		thread = new Thread(this, "RSA");
		thread.start();
	}

	@Override
	public void run() {
		// BigInteger p = new BigInteger(
		// "12131072439211271897323671531612440428472427633701410925634549312301964373042085619324197365322416866541017057361365214171711713797974299334871062829803541");
		// BigInteger q = new BigInteger(
		// "12027524255478748885956220793734512128733387803682075433653899983955179850988797899869146900809131611153346817050832096022160146366346391812470987105415233");
		BigInteger p;
		BigInteger q;
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

		System.out.println("p = " + p);
		System.out.println("q = " + q);

		BigInteger n = p.multiply(q);
		BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
		System.out.println("phi = " + phi);

		// Create e 1 < e < phi, e and n are coprime
		BigInteger e = new BigInteger("65537");
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
		BigInteger d = e.modInverse(phi);

		System.out.println("public key: (e, n) (" + e + "," + n + ")");
		System.out.println("private key: (d, n) (" + d + "," + n + ")");

		BigInteger m = new BigInteger("1976620216402300889624482718775150");

		BigInteger c = m.modPow(e, n);
		System.out.println("Encryption: " + c);

		BigInteger m_decrypted = c.modPow(d, n);
		System.out.println("Decryption: " + m_decrypted);

	}
}
