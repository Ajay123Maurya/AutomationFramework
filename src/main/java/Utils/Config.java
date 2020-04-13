package Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.openqa.selenium.WebDriver;
import org.testng.asserts.SoftAssert;

public class Config {
	
	
	public String uniqueId = null;
	public Method testMethod;
	public boolean testResult;
	public Connection connection = null;
	public String testLog;
	public SoftAssert softAssert;
	public boolean endExecutionOnfailure = false;
	public static String fileSeparator = File.separator;
	public boolean debugMode = false;
	public boolean logToStandardOut = true;
	public boolean enableScreenshot = true;
	public WebDriver driver;
	String testName;
	String testStartTime;
	public static String Environment;
	public boolean recordPageHTMLOnFailure = false;
	public static String MobilePlatFormName;
	public static String BrowserName;
	public static String MobileUAFlag;
	public static String ResultsDir;
	public static String PlatformName;
	public static String RemoteAddress;
	public static String SharedDirectory;
	public static String ProjectName;
	public static String BrowserVersion;
	public static String BuildId;
	public static String RunType;
	public boolean remoteExecution;
	public boolean localRemoteExecution;
	public boolean isMobile = false;
	public String downloadPath = null;
	
	Properties runtimeProperties;
	
	
	public Config(Method method)
	{
		try
		{
			endExecutionOnfailure = true;

			this.uniqueId = Helper.generateRandomAlphaNumericString(4) + "-" +
					Helper.generateRandomAlphaNumericString(5) + "-" +
					Helper.generateRandomAlphaNumericString(4);
			this.testMethod = method;
			this.testResult = true;
			this.connection = null;
			this.testLog = "";
			this.softAssert = new SoftAssert();
			// Read the Config file
			Properties property = new Properties();
			
			String path = System.getProperty("user.dir") + fileSeparator + "Parameters" + fileSeparator + "Config.properties";
			
			if (debugMode)
				logComment("Read the configuration file:-" + path);
			FileInputStream fn = new FileInputStream(path);
			property.load(fn);
			fn.close();
			
			// override the environment value if passed through ant command line
			if (!(Environment == null || Environment.isEmpty()))
				property.put("Environment", Environment.toLowerCase());
			
			// override environment if declared in @TestVariables
			TestVariables testannotation = null;
			if(method != null)
			{
				testannotation = ObjectUtils.firstNonNull(method.getAnnotation(TestVariables.class), method.getDeclaringClass().getAnnotation(TestVariables.class));
				if (testannotation != null)
				{
					String environment = testannotation.environment();
					if(!environment.isEmpty())
					{
						logComment("Running on " + environment.toLowerCase() + " environment");
						property.put("Environment", environment.toLowerCase());
					}

					String applicationName = testannotation.applicationName();
					if (!applicationName.isEmpty())
					{
						logComment("Loading settings for application " + applicationName);

						path = System.getProperty("user.dir") + fileSeparator + "Parameters" + fileSeparator + applicationName + ".properties";
						logComment("Read the environment file:- " + path);

						fn = new FileInputStream(path);
						property.load(fn);
						fn.close();
					}
				}
			}
			
			path = System.getProperty("user.dir") + fileSeparator + "Parameters" + fileSeparator + property.get("Environment") + ".properties";
			logComment("Read the environment file:- " + path);
			
			fn = new FileInputStream(path);
			property.load(fn);
			fn.close();
						
			this.runtimeProperties = new Properties();
			Enumeration<Object> em = property.keys();
			while (em.hasMoreElements())
			{
				String str = (String) em.nextElement();
				putRunTimeProperty(str, (String) property.get(str));
			}
			
			this.debugMode = (getRunTimeProperty("DebugMode").toLowerCase().equals("true")) ? true : false;
			this.logToStandardOut = (getRunTimeProperty("LogToStandardOut").toLowerCase().equals("true")) ? true : false;
			this.recordPageHTMLOnFailure = (getRunTimeProperty("RecordPageHTMLOnFailure").toLowerCase().equals("true")) ? true : false;
			// override run time properties if passed through ant command line
			if (!(BrowserName == null || BrowserName.isEmpty()))
				putRunTimeProperty("Browser", BrowserName);
			
			if (!(ProjectName == null || ProjectName.isEmpty()))
				putRunTimeProperty("ProjectName", ProjectName);
			
			if (!(PlatformName == null || PlatformName.isEmpty())&& !PlatformName.equalsIgnoreCase("Local") && !getRunTimeProperty("PlatformName").equalsIgnoreCase("Android") && !getRunTimeProperty("PlatformName").equalsIgnoreCase("iOS"))
		 	putRunTimeProperty("PlatformName", PlatformName);
						
			if (!(RemoteAddress == null || RemoteAddress.isEmpty()) && !RemoteAddress.equalsIgnoreCase("null"))
			{
			 	putRunTimeProperty("RemoteAddress", RemoteAddress);
			 	putRunTimeProperty("RemoteExecution", "true");		
			}

			if (!(BuildId == null || BuildId.isEmpty()))
			 	putRunTimeProperty("BuildId", BuildId);
			
			if (!(BrowserVersion == null || BrowserVersion.isEmpty()))
			 	putRunTimeProperty("BrowserVersion", BrowserVersion);
			
			if(!(RunType == null || RunType.isEmpty()))
				putRunTimeProperty("RunType", RunType);
			else
			{
				RunType = getRunTimeProperty("RunType");
			}
			
			if (!(ResultsDir == null || ResultsDir.isEmpty()))
			{
				putRunTimeProperty("ResultsDir", ResultsDir);
			}
			else
			{
				// Set the full path of results dir
				String resultsDir = System.getProperty("user.dir") + getRunTimeProperty("ResultsDir");
				logComment("Results Directory is:- " + resultsDir);
				putRunTimeProperty("ResultsDir", resultsDir);
				
			}
			
			// override the MobileUAFlag value if passed through command line
			if (!(MobileUAFlag == null || MobileUAFlag.isEmpty()))
				putRunTimeProperty("MobileUAFlag", MobileUAFlag);
			
			// override environment if declared in @TestVariables
			if (testannotation != null)
			{
				String mobileUAFlag = testannotation.MobileUAFlag();
				if(!mobileUAFlag.isEmpty())
				{
					putRunTimeProperty("MobileUAFlag",mobileUAFlag );
				}
			}
			//TODO Uncomment for android web execution
//			if (getRunTimeProperty("MobileUAFlag").equals("true"))
//			{
//				putRunTimeProperty("browser", "android_web");
//			}
								
			// Set the full path of test data sheet
			String testDataSheet = System.getProperty("user.dir") + getRunTimeProperty("TestDataSheet");
			if (debugMode)
				logComment("Test data sheet is:-" + testDataSheet);
			putRunTimeProperty("TestDataSheet", testDataSheet);
			
			String testData = System.getProperty("user.dir") + getRunTimeProperty("TestData");
			if (debugMode)
				logComment("Test data sheet is:-" + testData);
			putRunTimeProperty("TestData", testData);
			
			// Set the full path of checkout page
			if (getRunTimeProperty("checkoutPage") != null)
			{
				String checkoutPage = System.getProperty("user.dir") + getRunTimeProperty("checkoutPage");
				if (debugMode)
					logComment("Checkout page is:-" + checkoutPage);
				putRunTimeProperty("checkoutPage", checkoutPage);
			}
			
			if (testannotation != null)
			{
				String remote = testannotation.remoteExecution();
				if(!remote.isEmpty())
				{
					putRunTimeProperty("RemoteExecution", remote);
				}
			}
			
			endExecutionOnfailure = false;
			remoteExecution = (getRunTimeProperty("RemoteExecution").toLowerCase().equals("true")) ? true : false;
			isMobile = (((getRunTimeProperty("Browser").equals("android_web") || getRunTimeProperty("Browser").equals("android_native")) && getRunTimeProperty("RemoteExecution").equals("true")) || getRunTimeProperty("MobileUAFlag").equals("true"));
			
			
			if(testMethod != null)
			{
				if(remoteExecution)
				{
					SharedDirectory = getRunTimeProperty("SharedDirectory");
					downloadPath =  SharedDirectory + Config.ProjectName + fileSeparator + "Downloads" + fileSeparator + Config.BuildId + fileSeparator + testMethod.getName();
				}
				else
				{
					downloadPath = System.getProperty("user.home") + fileSeparator + "Downloads" + fileSeparator + testMethod.getName();
				}

			/*	boolean status = Helper.createFolder(downloadPath);

				if(status)
				{
					downloadPath = downloadPath + fileSeparator;
				}
				else
				{
					System.out.println("Something went Wrong.!! Error in Creating Folder -" + downloadPath + " switching to predefined download Path - " + System.getProperty("user.home") + fileSeparator + "Downloads" + fileSeparator);
					downloadPath = System.getProperty("user.home") + fileSeparator + "Downloads" + fileSeparator;
				}  */
			}
		}
		catch (IOException e)
		{
			logException(e);
		}
	}
	
	public String getTestName()
	{
		return testName;
	}
	
	public boolean getTestCaseResult()
	{
		return testResult;
	}
	
	public void logWarning(String message)
	{
		message = "[" + this.uniqueId + "]  " + message;
		Log.Warning(message, this);
	}
	
	public void logComment(String message)
	{
		if(message != null && message.contains("window.onload"))
		{
			//This is done to remove the unnecessary redirecting of results page when viewing the reports, this will not affect the flow of testcase
			Log.Comment("Message to be print contains 'window.onload' so replacing it by '*window.onload*' before printing.", this);
			message = message.replace("window.onload", "*window.onload*");
		}
		message = "[" + this.uniqueId + "]  " + message;
		Log.Comment(message, this);
	}
	
	/**
	 * Get the Run Time Property value
	 * 
	 * @param key
	 *            key name whose value is needed
	 * @return value of the specified key
	 */
	public String getRunTimeProperty(String key)
	{
		String keyName = key.toLowerCase();
		String value = "";
		try
		{
			value = runtimeProperties.get(keyName).toString();
			logComment("Reading Run-Time key-" + keyName + " value:-'" + value + "'");
		}
		catch (Exception e)
		{
				logComment(e.toString());
				logComment("'" + key + "' not found in Run Time Properties");
			return "";
		}
		return value;
	}
	/**
	 * Add the given key value pair in the Run Time Properties
	 * 
	 * @param key
	 * @param value
	 */
	public void putRunTimeProperty(String key, Object value)
	{
		String keyName = key.toLowerCase();
		runtimeProperties.put(keyName, value);
		if (debugMode)
			logComment("Putting Run-Time key-" + keyName + " value:-'" + value + "'");
	}
	
	/**
	 * Add the given key value pair in the Run Time Properties
	 * 
	 * @param key
	 * @param value
	 */
	public void putRunTimeProperty(String key, String value)
	{
		String keyName = key.toLowerCase();
		runtimeProperties.put(keyName, value);
		if (debugMode)
		{
				logComment("Putting Run-Time key-" + keyName + " value:-'" + value + "'");
		}
	}
	
	/**
	 * Removes the given key from the Run Time Properties
	 * 
	 * @param key
	 */
	public void removeRunTimeProperty(String key)
	{
		String keyName = key.toLowerCase();
		if (debugMode)
			logComment("Removing Run-Time key-" + keyName);
		runtimeProperties.remove(keyName);
	}
	public void logException(Throwable e)
	{
		testResult = false;
		String fullStackTrace = ExceptionUtils.getStackTrace(e);
		fullStackTrace = "[" + this.uniqueId + "]  " + fullStackTrace;
		Log.Fail(fullStackTrace, this);
	}
	
	
	public void logFail(String message)
	{
		testResult = false;
		message = "[" + this.uniqueId + "]  " + message;
		Log.Fail(message, this);
	}
	public void logPass(String message)
	{
		message = "[" + this.uniqueId + "]  " + message;
		Log.Pass(message, this);
	}
	
}
