package RSA;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

public class RSA {
		private  ArrayList<BigInteger> arrayBigIntE = new ArrayList<BigInteger>();
		private  ArrayList<BigInteger> arrayBigIntD = new ArrayList<BigInteger>();

		public  BigInteger getRandomBigInteger(int pow) {
			Random rand = new Random();
			BigInteger result = new BigInteger(pow, rand); // (2^4-1) = 15 is the
															// maximum value
			return result;
		}

		private  int Random(int Min, int Max) {
			return (Min + (int) (Math.random() * ((Max - Min) + 1)));
		}

		public  BigInteger gcd(BigInteger a, BigInteger b) {
			while (b.compareTo(BigInteger.ZERO) != 0) {
				BigInteger tmp = a.mod(b);
				a = b;
				b = tmp;
			}
			return a;
		}
    public void rsa_example() {
    	// String strP =
    			// "12131072439211271897323671531612440428472427633701410925634549312301964373042085619324197365322416866541017057361365214171711713797974299334871062829803541";
    			// String strQ =
    			// "12027524255478748885956220793734512128733387803682075433653899983955179850988797899869146900809131611153346817050832096022160146366346391812470987105415233";
    			String strP = "113";
    			String strQ = "149";

    			BigInteger p = getRandomBigInteger(56); // new BigInteger(strP);
    			BigInteger q = getRandomBigInteger(56);
    			BigInteger n = p.multiply(q);
    			System.out.println("n = " + n);
    			BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
    			System.out.println("phi = " + phi);

    			BigInteger e;
    			BigInteger randomBigInteger;
    			while (true) {
    				randomBigInteger = getRandomBigInteger(12);
    				if (gcd(randomBigInteger, phi).equals(BigInteger.ONE)) {
    					e = randomBigInteger;
    					break;
    				}
    			}

    			System.out.println("e = " + e);

    			while (true) {
    				randomBigInteger = getRandomBigInteger(10);
    				System.out.println("random " + randomBigInteger);
    				if (e.multiply(randomBigInteger).mod(phi).compareTo(BigInteger.ONE) == 0) {
    					System.out.println("randomBigInteger " + randomBigInteger);
    					arrayBigIntD.add(randomBigInteger);
    					break;
    				}
    			}
    			for (int i = 0; i < arrayBigIntD.size(); i++) {
    				// System.out.println("arrayBigIntD " + arrayBigIntD.get(i));
    			}
    			// BigInteger d = arrayBigIntD.get(Random(1, arrayBigIntD.size() - 1));
    			BigInteger d = arrayBigIntD.get(0);
    			System.out.println("public key: (e, n) (" + e + "," + n + ")");
    			System.out.println("private key: (d, n) (" + d + "," + n + ")");

    			BigInteger m = BigInteger.valueOf(19);

    			BigInteger c = m.modPow(e, n);
    			System.out.println("Encryption: " + c);

    			BigInteger m_decrypted = c.modPow(d, n);
    			System.out.println("Decryption: " + m_decrypted);

    			/*
    			 * BigInteger randomBigInteger; for(int i = 0; i < 225; i++) {
    			 * randomBigInteger = getRandomBigInteger();
    			 * if(e.multiply(randomBigInteger).mod(phi).compareTo(BigInteger.ONE) ==
    			 * 0) { System.out.println("randomBigInteger " +randomBigInteger); } }
    			 */
    			/*
    			 * for(int i = 0; i < 225; i++) { randomBigInteger =
    			 * getRandomBigInteger();
    			 * if(e.multiply(randomBigInteger).mod(phi).compareTo(BigInteger.ONE) ==
    			 * 0) { System.out.println("randomBigInteger " +randomBigInteger); } }
    			 */
    }
}
