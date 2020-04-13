package Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class Helper {
	
	
	
	/**
	 * Generate a random Alpha-Numeric string of given length
	 * 
	 * @param length
	 *            Length of string to be generated
	 */
	public static String generateRandomAlphaNumericString(int length)
	{
		Random rd = new Random();
		String aphaNumericString = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		StringBuilder sb = new StringBuilder(length);

		for (int i = 0; i < length; i++)
		{
			sb.append(aphaNumericString.charAt(rd.nextInt(aphaNumericString.length())));
		}

		return sb.toString();
	}


	public static void compareTrue(Config testConfig, String what, boolean actual)
	{
		if (!actual)
		{
			testConfig.logFail("Failed to verify " + what);
		}
		else
		{
			testConfig.logPass("Verified " + what);
		}
	}
	
	public static String getCurrentDateTime(String format)
	{
		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		String dateNow = formatter.format(currentDate.getTime());
		return dateNow;
	}
}
