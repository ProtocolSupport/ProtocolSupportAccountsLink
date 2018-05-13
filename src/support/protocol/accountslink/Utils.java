package support.protocol.accountslink;

import java.security.SecureRandom;

public class Utils {

	private static final String random_str_symbols = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	private static SecureRandom rnd = new SecureRandom();

	public static String generateRandomString(int len) {
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			sb.append(random_str_symbols.charAt(rnd.nextInt(random_str_symbols.length())));
		}
		return sb.toString();
	}

}
