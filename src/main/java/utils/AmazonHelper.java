package utils;

import java.io.IOException;

public class AmazonHelper {

	SeleniumFunction function;
	
	public void openBrowserAndNavigateToUrl(String url) {
		function =new SeleniumFunction();
		try {
			function.initializeBrowser();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		function.openURL(url);
	}
}
