import java.io.IOException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.util.Random;

public class MyKeyGen {

	public static void main(String[] args) throws IOException {
		//BigInteger one = 1;
		Random rnd = new Random();
		BigInteger P = BigInteger.probablePrime(512, rnd);
		BigInteger Q = BigInteger.probablePrime(512, rnd);
		BigInteger N = P.multiply(Q);
		BigInteger PHI = P.subtract(BigInteger.ONE).multiply(Q.subtract(BigInteger.ONE));
		BigInteger E = PHI.shiftRight(4);
		while (!PHI.gcd(E).equals(BigInteger.ONE))
		{
			E = E.subtract(BigInteger.ONE);
		}
		BigInteger D = E.modInverse(PHI);
		
		FileOutputStream fw;
		ObjectOutputStream w;
		fw = new FileOutputStream("pubkey.rsa");
		w = new ObjectOutputStream(fw);
		w.writeObject(E);
		w.writeObject(N);
		w.close();
		fw.close();
		fw = new FileOutputStream("privkey.rsa");
		w = new ObjectOutputStream(fw);
		w.writeObject(D);
		w.writeObject(N);
		w.close();
		fw.close();
		System.out.println("Generate successfully.");
	}

}
