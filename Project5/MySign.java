import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MySign {

	public static void main(String[] args) throws IOException, NoSuchAlgorithmException, ClassNotFoundException {
		if (args[0].equals("s"))
			Sign(args[1]);
		else if (args[0].equals("v"))
			Verify(args[1]);
		else
			System.out.println("Please enter a valid operation(s/v)!");
	}
	
	public static void Sign(String s) throws NoSuchAlgorithmException, ClassNotFoundException, IOException
	{
		FileInputStream fr;
		ObjectInputStream r;
		try {
			fr = new FileInputStream("privkey.rsa");
			r = new ObjectInputStream(fr);
			BigInteger D = (BigInteger) r.readObject();
			BigInteger N = (BigInteger) r.readObject();
			r.close();
			fr.close();
			
			Path path = Paths.get(s);
			byte[] data;
			try {
				data = Files.readAllBytes(path);
				MessageDigest md = MessageDigest.getInstance("SHA-256");
				byte[] digest = md.digest(data);
				BigInteger M = new BigInteger(1, digest);
				BigInteger C = M.modPow(D, N);
				
				FileOutputStream fw;
				ObjectOutputStream w;
				fw = new FileOutputStream(s+".signed");
				w = new ObjectOutputStream(fw);
				w.writeObject(data);
				w.writeObject(C);
				w.close();
				fw.close();
				
				System.out.println("Sign successfully.");
			} catch (IOException e) {
				System.err.printf("File %s doesn't exist.\n", s);
			}
		} catch (FileNotFoundException e) {
			System.err.println("privkey.rsa doesn't exist.");
		}
	}
	
	public static void Verify(String s) throws NoSuchAlgorithmException, ClassNotFoundException, IOException
	{
		FileInputStream fr;
		ObjectInputStream r;
		try {
			fr = new FileInputStream("pubkey.rsa");
			r = new ObjectInputStream(fr);
			BigInteger E = (BigInteger) r.readObject();
			BigInteger N = (BigInteger) r.readObject();
			r.close();
			fr.close();
			
			try {
				FileInputStream ff = new FileInputStream(s);
				ObjectInputStream rr;
				rr = new ObjectInputStream(ff);
				byte[] oridata = (byte[]) rr.readObject();
				
				MessageDigest md = MessageDigest.getInstance("SHA-256");
				byte[] oridigest = md.digest(oridata);
				BigInteger M = new BigInteger(1, oridigest);
				
				BigInteger C = (BigInteger) rr.readObject();
				C = C.modPow(E, N);
				rr.close();
				ff.close();
				
				if (C.equals(M))
					System.out.println("Signature is valid!");
				else
					System.out.println("Signature is not valid!");
			} catch (IOException e) {
				System.err.printf("File %s doesn't exist.\n", s);
			}
		} catch (FileNotFoundException e) {
			System.err.println("pubkey.rsa doesn't exist.");
		}
	}

}
