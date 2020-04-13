package Utils;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.Augmentable;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import io.github.bonigarcia.wdm.WebDriverManager;

public class Browser {
	
	
	// This class overrides the setCompressionQuality() method to workaround
	// a problem in compressing JPEG images using the javax.imageio package.
	public static class MyImageWriteParam extends JPEGImageWriteParam
	{
		public MyImageWriteParam()
		{
			super(Locale.getDefault());
		}
		
		// This method accepts quality levels between 0 (lowest) and 1 (highest)
		// and simply converts
		// it to a range between 0 and 256; this is not a correct conversion
		// algorithm.
		// However, a proper alternative is a lot more complicated.
		// This should do until the bug is fixed.
		@Override
		public void setCompressionQuality(float quality)
		{
			if (quality < 0.0F || quality > 1.0F)
			{
				throw new IllegalArgumentException("Quality out-of-bounds!");
			}
			this.compressionQuality = 256 - (quality * 256);
		}
	}
	
	/**
	 * Uses the specified method name to generate a destination file name where
	 * screenshot can be saved
	 * 
	 * @param Config
	 *            test config instance
	 * @return file using which we can call takescreenshot
	 */
	
	private static File getScreenShotDirectory(Config testConfig)
	{
		File dest = new File(testConfig.getRunTimeProperty("ResultsDir") + File.separator + "html" + File.separator );
		return dest;
	}
	public static File getScreenShotFile(Config testConfig)
	{
		File dest = getScreenShotDirectory(testConfig);
		return new File(dest.getPath() + File.separator + getScreenshotFileName(testConfig.testMethod));
	}
	
	private static String getScreenshotFileName(Method testMethod)
	{
		String nameScreenshot = testMethod.getDeclaringClass().getName() + "." + testMethod.getName();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		Date date = new Date();
		return dateFormat.format(date) + "_" + nameScreenshot + ".png";
	}
	/**
	 * Takes the screenshot of the current active browser window
	 * 
	 * @param Config
	 *            test config instance
	 * @param destination
	 *            file to which screenshot is to be saved
	 */
	public static void takeScreenShoot(Config testConfig, File destination)
	{
		try
		{
			if (testConfig.driver != null)
			{
				byte[] screenshot = null;
				
				try
				{
					screenshot = captureScreenshot(testConfig);
				}
				catch (NullPointerException ne)
				{
					testConfig.logWarning("NullPointerException:Screenshot can't be taken. Probably browser is not reachable");
					//test case will end, setting this as null will prevent taking screenshot again in cleanup
					testConfig.driver = null;
				}
				
				if (screenshot != null)
				{
					try
					{
						FileUtils.writeByteArrayToFile(destination, screenshot);
						
						float compressionQuality = (float) 0.5;
						try
						{
							compressionQuality = Float.parseFloat(testConfig.getRunTimeProperty("ScreenshotCompressionQuality"));
						}
						catch (Exception e)
						{
							// We are not using testConfig.logException(e) to print
							// exception here
							// testConfig.logException(e) creates a infinite loop
							// and breaks all test cases
							e.printStackTrace();
						}
						compressJpegFile(destination, destination, compressionQuality);
					}
					catch (IOException e)
					{
						// We are not using testConfig.logException(e) to print
						// exception here
						// testConfig.logException(e) creates a infinite loop and
						// breaks all test cases
						e.printStackTrace();
					}
				}
				String href = convertFilePathToHtmlUrl(destination.getPath());
				testConfig.logComment("<B>Screenshot</B>:- <a href=" + href + " target='_blank' >" + destination.getName() + "</a>");
			}
		}
		catch (Exception e)
		{
			testConfig.enableScreenshot = false;
			testConfig.logWarning("Unable to take screenshot2:- " + ExceptionUtils.getStackTrace(e));
			try {
				throw e;
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	//@Attachment(value = "Screenshot", type = "image/png")
	private static byte[] captureScreenshot(Config testConfig)
	{
		byte[] screenshot = null;
		
		try
		{
			if(Popup.isAlertPresent(testConfig, false))
			{
				Popup.ok(testConfig);
			}
			
			if (testConfig.driver.getClass().isAnnotationPresent(Augmentable.class) || testConfig.driver.getClass().getName().startsWith("org.openqa.selenium.remote.RemoteWebDriver$$EnhancerByCGLIB")) 
			{
				WebDriver augumentedDriver = new Augmenter().augment(testConfig.driver);
				screenshot = ((TakesScreenshot) augumentedDriver).getScreenshotAs(OutputType.BYTES);					
			}
			else
			{
				screenshot = ((TakesScreenshot) testConfig.driver).getScreenshotAs(OutputType.BYTES);
			}
		
		}
		catch (UnhandledAlertException alert)
		{
			Popup.ok(testConfig);
			testConfig.logWarning(ExceptionUtils.getStackTrace(alert));
		}
		catch (NoSuchWindowException NoSuchWindowExp)
		{
			testConfig.logWarning("NoSuchWindowException:Screenshot can't be taken. Probably browser is not reachable");
			//test case will end, setting this as null will prevent taking screenshot again in cleanup
			testConfig.driver = null;
		}
		catch (WebDriverException webdriverExp)
		{
			testConfig.logWarning("Unable to take screenshot1:- " + ExceptionUtils.getStackTrace(webdriverExp));
		}
		catch(Exception e)
		{
			testConfig.logComment("****************************Exception in Browser.java***********************");
			e.printStackTrace();
		}
		return screenshot;
	}
	// Reads the jpeg image in infile, compresses the image,
	// and writes it back out to outfile.
	// compressionQuality ranges between 0 and 1,
	// 0-lowest, 1-highest.
	private static void compressJpegFile(File infile, File outfile, float compressionQuality)
	{
		try
		{
			// Retrieve jpg image to be compressed
			RenderedImage rendImage = ImageIO.read(infile);
			
			// Find a jpeg writer
			ImageWriter writer = null;
			Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("gif");
			if (iter.hasNext())
			{
				writer = iter.next();
			}
			
			// Prepare output file
			ImageOutputStream ios = ImageIO.createImageOutputStream(outfile);
			writer.setOutput(ios);
			
			// Set the compression quality
			ImageWriteParam iwparam = new MyImageWriteParam();
			iwparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			iwparam.setCompressionQuality(compressionQuality);
			
			// Write the image
			writer.write(new IIOImage(rendImage, null, null));
			
			// Cleanup
			ios.flush();
			writer.dispose();
			ios.close();
		}
		
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (OutOfMemoryError outOfMemoryError)
		{
			outOfMemoryError.printStackTrace();
		}
	}
	/**
	 * This function return the URL of a file on runtime depending on LOCAL or OFFICIAL Run
	 * @param testConfig
	 * @param fileUrl
	 * @return
	 */
	public static String convertFilePathToHtmlUrl(String fileUrl)
	{
		fileUrl = fileUrl.replace(File.separator, "/");
		fileUrl = fileUrl.replace("RegressionResults/", "");
        return fileUrl;
	}
	/**
	 * Opens the new browser instance using the given config
	 * 
	 * @return new browser instance
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	public static WebDriver openBrowser(Config testConfig)
	{
		WebDriver driver = null;
		String browser = testConfig.getRunTimeProperty("Browser");
		switch(browser) {
		
		case "firefox":
			String path = System.getProperty("user.dir") + File.separator + ".." + File.separator + "Parameter" + File.separator + "geckodriver";
			System.setProperty("webdriver.gecko.driver",path);
			DesiredCapabilities capabilities=DesiredCapabilities.firefox();
			capabilities.setCapability("marionette", true);
		   driver = new FirefoxDriver(capabilities);
		   break;
		case "chrome":
			WebDriverManager.chromedriver().setup();
			ChromeOptions chromeOptions = new ChromeOptions();
			chromeOptions.addArguments("start-maximized");  
			chromeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"}); 
			driver = new ChromeDriver(chromeOptions); 
			driver = new ChromeDriver(chromeOptions); 
		   break;
		}
		
		testConfig.logComment("Browser '" + browser + "' launched successfully for testcase:- "+testConfig.getTestName());
		return driver;
	}
	
}
