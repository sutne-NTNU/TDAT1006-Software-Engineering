package external_code;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import Logic.Cleanup;

public class HashAndSalt {
	
	
	
	/*
	 * 
	 * Hashing
	 * 
	 */

	/**Generates a salted string given the password and the salt.
	 * @param passwordToHash the password
	 * @param salt the salt
	 * @return String
	 */
	public static String SHA_256Password(String passwordToHash, byte[] salt)
	{
		String generatedPassword = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(salt);
			byte[] bytes = md.digest(passwordToHash.getBytes());
			StringBuilder sb = new StringBuilder();
			for(int i=0; i< bytes.length ;i++)
			{
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}

			generatedPassword = sb.toString();
		}
		catch (NoSuchAlgorithmException e)
		{
			Cleanup.writeMessage(e, "SHA_256Password()");
		}
		return generatedPassword;
	}

	/*
	 * 
	 * Salting
	 * 
	 */

	/**Generates a byte array salt, and retuns it.
	 * @return byte[]
	 */
	public static byte[] gensalt(){
		SecureRandom sr;
		byte[] salt = new byte[16];
		try {
			sr = SecureRandom.getInstance("SHA1PRNG");
			sr.nextBytes(salt);
		}catch(Exception e){
			Cleanup.writeMessage(e, "gensalt()");
			return null;
		}
		return salt;
	}
}
