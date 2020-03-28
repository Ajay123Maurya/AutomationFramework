package utils;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;


/**
 * 
 * Author : Ajay Kumar
 *
 */

public class SeleniumFunction {


	public boolean isNavigated = false;
	String filePathScreenShot = "./src/main/resources/screenshots";
	public static RemoteWebDriver driver;
	private static String fileSeperator = System.getProperty("file.separator");

	public void initializeBrowser() throws IOException {
		
		if (System.getProperty("os.name").contains("Mac")) {
			System.setProperty("webdriver.chrome.driver", "src/resources/mac/chromedriver");
			

		} else {
			System.setProperty("webdriver.chrome.driver", "src/resources/linux/chromedriver");
		}
		
		try{
			driver = new ChromeDriver();
			driver.manage().window().maximize();
		}
		catch(Exception e){
			driver = null;
			driver = new ChromeDriver();
		}
		driver.manage().window().maximize();
	}
	
	public void navigateBack() {
		driver.navigate().back();
	}

	public String getCurrentWindow() {
		return driver.getWindowHandle();
	}

	public void switchToActive() {
		driver.switchTo().activeElement();
	}

	public void switchToAnotherWindow(String currentWindow) {
		driver.switchTo().window(currentWindow);
	}

	public void hoverOnElement(WebElement ele) {
		Actions action = new Actions(driver);
		action.moveToElement(ele).build().perform();
	}

	public void clickOnHoverElement(WebElement ele) {
		Actions action = new Actions(driver);
		action.moveToElement(ele).click().build().perform();
	}

	public void hoverOnMDDElement(WebElement ele) {
		Actions action = new Actions(driver);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		action.moveToElement(ele).build().perform();
	}

	public void openURL(boolean visit, String URL) {
		if (visit == true) {
			driver.navigate().to(URL);
		}
	}


	public void openURL(String url) {
		try {
			driver.get(url);
			// driver.navigate().to(URL + url);
		} catch (TimeoutException e2) {
			refreshPage();
			System.out.println("Catching timeout exception");
		}
	}

	public void refreshPage() {
		driver.navigate().refresh();
	}

	public void closeCurrentWindow() {
		driver.close();
	}

	public String getCurrentPageUrl() {
		String currentUrl = driver.getCurrentUrl();
		return currentUrl;
	}

	public void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void scrollPage(String Xpath) {
		Point loc = driver.findElement(By.xpath(Xpath)).getLocation();
		JavascriptExecutor js = driver;
		js.executeScript("javascript:window.scrollBy(0," + loc.y + ")");
	}

	public void selectDropDownById(String id, String data) {

		Select dropDown = new Select(findElementByXpath(id));
		// dropDown.selectByIndex(index);
		dropDown.selectByValue(data);

		/*
		 * Select dropDown = new Select(findElementById(id));
		 * dropDown.selectByIndex(index); dropDown.selectByVisibleText(data);
		 */
	}

	public void selectDropdownByXpath(String xpath, String data) {
		Select dropDown = new Select(findElementByXpath(xpath));
		//System.out.println("abc xpath" + xpath);
		dropDown.selectByIndex(1);
	}

	public void dropDown(String locator, String locator_value, String selectby, String value) {
		if (locator.equalsIgnoreCase("id")) {
			Select dropdown = new Select(findElementById(locator_value));
			if (selectby.equalsIgnoreCase("visibletext")) {
				dropdown.selectByVisibleText(value);
			} else if (selectby.equalsIgnoreCase("value")) {
				dropdown.selectByValue(value);
			}
		}
	}

	public void alertdismiss() {
		Alert simpleAlert = driver.switchTo().alert();
		String alertText = simpleAlert.getText();
		System.out.println("Alert text is " + alertText);
		simpleAlert.dismiss();
	}

	public void switchToFrame(String FrameName) {
		driver.switchTo().frame(FrameName);
	}

	public void setInputValue(WebElement ele, String value) {
		JavascriptExecutor exec = driver;
		exec.executeScript("arguments[0].setAttribute('value', '" + value + "')", ele);
	}

	public By locateBy(String elementName, String locator) {
		if (elementName.substring(elementName.length() - 2).toLowerCase().equals("Id".toLowerCase())) {
			return By.id(locator);
		} else if (elementName.substring(elementName.length() - 5).toLowerCase().equals("Class".toLowerCase())) {
			return By.className(locator);
		} else if (elementName.substring(elementName.length() - 4).toLowerCase().equals("Name".toLowerCase())) {
			return By.name(locator);
		} else if (elementName.substring(elementName.length() - 3).toLowerCase().equals("Tag".toLowerCase())) {
			return By.tagName(locator);
		} else if (elementName.substring(elementName.length() - 5).toLowerCase().equals("Xpath".toLowerCase())) {
			return By.xpath(locator);
		} else if (elementName.substring(elementName.length() - 3).toLowerCase().equals("Css".toLowerCase())) {
			return By.cssSelector(locator);
		} else if (elementName.substring(elementName.length() - 8).toLowerCase().equals("LinkText".toLowerCase())) {
			return By.linkText(locator);
		} else {
			return By.partialLinkText(locator);
		}
	}

	public By locateById(String element) {
		return By.id(element);
	}

	public By locateByClassName(String element) {
		return By.className(element);
	}

	public By locateByCssSelector(String element) {
		return By.cssSelector(element);
	}

	public By locateByLinkText(String element) {
		return By.linkText(element);
	}

	public By locateByName(String element) {
		return By.name(element);
	}

	public By locateByPartialLinkText(String element) {
		return By.partialLinkText(element);
	}

	public By locateByXpath(String element) {
		return By.xpath(element);
	}

	public By locateByTagName(String element) {
		return By.tagName(element);
	}

	public WebElement findElement(String elementName, String locator) {
		if (elementName.substring(elementName.length() - 2).toLowerCase().equals("Id".toLowerCase())) {
			return findElementById(locator);
		} else if (elementName.substring(elementName.length() - 5).toLowerCase().equals("Class".toLowerCase())) {
			return findElementByClassName(locator);
		} else if (elementName.substring(elementName.length() - 4).toLowerCase().equals("Name".toLowerCase())) {
			return findElementByName(locator);
		} else if (elementName.substring(elementName.length() - 3).toLowerCase().equals("Tag".toLowerCase())) {
			return findElementByTagName(elementName);
		} else if (elementName.substring(elementName.length() - 5).toLowerCase().equals("Xpath".toLowerCase())) {
			return findElementByXpath(locator);
		} else if (elementName.substring(elementName.length() - 3).toLowerCase().equals("Css".toLowerCase())) {
			return findElementByCss(locator);
		} else if (elementName.substring(elementName.length() - 8).toLowerCase().equals("LinkText".toLowerCase())) {
			return findElementByLinkText(locator);
		} else {
			return findElementByPartialLinkText(locator);
		}

	}

	public List<WebElement> findElements(String elementName, String locator) {
		if (elementName.substring(elementName.length() - 2).toLowerCase().equals("Id".toLowerCase())) {
			return findElementsById(locator);
		} else if (elementName.substring(elementName.length() - 5).toLowerCase().equals("Class".toLowerCase())) {
			return findElementsByClassName(locator);
		} else if (elementName.substring(elementName.length() - 4).toLowerCase().equals("Name".toLowerCase())) {
			return findElementsByName(locator);
		} else if (elementName.substring(elementName.length() - 3).toLowerCase().equals("Tag".toLowerCase())) {
			return findElementsByTagName(locator);
		} else if (elementName.substring(elementName.length() - 5).toLowerCase().equals("Xpath".toLowerCase())) {
			return findElementsByXpath(locator);
		} else if (elementName.substring(elementName.length() - 3).toLowerCase().equals("Css".toLowerCase())) {
			return findElementsByCss(locator);
		} else if (elementName.substring(elementName.length() - 8).toLowerCase().equals("LinkText".toLowerCase())) {
			return findElementsByLinkText(locator);
		} else {
			return findElementsByPartialLinkText(locator);
		}
	}

	public WebElement findElementById(String element) {
		waitForPresenceOfElement(locateById(element), 20, element);
		return driver.findElement(By.id(element));
	}

	public List<WebElement> findElementsById(String element) {
		waitForPresenceOfElement(locateById(element), 20, element);
		return driver.findElements(By.id(element));
	}

	public WebElement findElementByClassName(String element) {
		waitForPresenceOfElement(locateByClassName(element), 20, element);
		return driver.findElement(By.className(element));
	}

	public WebElement findElementByName(String element) {
		waitForPresenceOfElement(locateByName(element), 20, element);
		return driver.findElement(By.name(element));
	}

	public WebElement findElementByTagName(String element) {
		waitForPresenceOfElement(locateByTagName(element), 20, element);
		return driver.findElement(By.tagName(element));
	}

	public List<WebElement> findElementsByTagName(String element) {
		return driver.findElements(By.tagName(element));
	}

	public WebElement findElementByXpath(String element) {
		waitForPresenceOfElement(locateByXpath(element), 120, element);
		return driver.findElement(By.xpath(element));
	}

	public List<WebElement> findElementsByXpath(String element) {
		waitForPresenceOfElement(locateByXpath(element), 120, element);
		return driver.findElements(By.xpath(element));
	}

	public List<WebElement> findElementsByName(String element) {
		waitForPresenceOfElement(locateByName(element), 120, element);
		return driver.findElements(By.name(element));
	}

	public List<WebElement> findElementsByPartialLinkText(String element) {
		waitForPresenceOfElement(locateByPartialLinkText(element), 120, element);
		return driver.findElements(By.partialLinkText(element));
	}

	public WebElement findElementByCss(String element) {
		waitForPresenceOfElement(locateByCssSelector(element), 20, element);
		return driver.findElement(By.cssSelector(element));
	}

	public List<WebElement> findElementsByCss(String element) {
		return driver.findElements(By.cssSelector(element));
	}

	public WebElement findElementByLinkText(String element) {
		waitForPresenceOfElement(locateByLinkText(element), 20, element);
		return driver.findElement(By.linkText(element));
	}

	public List<WebElement> findElementsByLinkText(String element) {
		waitForPresenceOfElement(locateByLinkText(element), 10, element);
		return driver.findElements(By.linkText(element));
	}

	public WebElement findElementByPartialLinkText(String element) {
		waitForPresenceOfElement(locateByPartialLinkText(element), 20, element);
		return driver.findElement(By.partialLinkText(element));
	}

	public List<WebElement> findElementsByClassName(String element) {
		waitForPresenceOfElement(locateByClassName(element), 20, element);
		try {
			return driver.findElements(By.className(element));
		} catch (StaleElementReferenceException e) {
			System.out.println("Element with classname" + element + "is not attached to the page dom");
			return null;
		}
	}

	public String getParentHandle() {
		String parentHandle = driver.getWindowHandle(); // get the current
														// window handle
		return parentHandle;
	}

	public void switchToParentHandle(String parentHandle) {
		driver.switchTo().window(parentHandle);
	}

	public String getTitle() {
		return driver.getTitle();
	}

	public void navigateToNewTab() {
		for (String winHandle : driver.getWindowHandles()) { // Gets the new
																// window handle
			driver.switchTo().window(winHandle); // switch focus of WebDriver to
													// the next found window
													// handle (that's your newly
													// opened window)
			isNavigated = true;
		}
	}

	public void navigateToOther() {
		for (String winHandle : driver.getWindowHandles()) { // Gets the new
																// window handle
			driver.switchTo().window(winHandle); // switch focus of WebDriver to
													// the next found window
													// handle (that's your newly
													// opened window)
		}
		isNavigated = false;
	}

	public boolean waitForJSandJQueryToLoad() {

		WebDriverWait wait = new WebDriverWait(driver, 30);

		// wait for jQuery to load
		ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {

			public Boolean apply(WebDriver driver) {
				try {
					return ((Long) ((JavascriptExecutor) driver).executeScript("return jQuery.active") == 0);
				} catch (Exception e) {
					// no jQuery present
					return true;
				}
			}
		};

		// wait for Javascript to load
		ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {

			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString()
						.equals("complete");
			}
		};

		return wait.until(jQueryLoad) && wait.until(jsLoad);
	}

	public File takeScreenShot(String testClassName, String testMethodName) {
//		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH").format(new Date());
//		String newFolderName = filePathScreenShot + fileSeperator + timeStamp + fileSeperator;
//		System.out.println(driver.getCurrentUrl());
//		File targetFolder = new File(newFolderName);
//		if (!targetFolder.exists()) {
//			targetFolder.mkdir();
//		}
//		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);// taking
//		File targetFile = new File(newFolderName + testClassName + "_" + testMethodName + ".png");
//		try {
//			FileUtils.copyFile(scrFile, targetFile);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		return new File("");
	}

	public String getTestClassName(String testName) {
		String[] reqTestClassname = testName.split("\\.");
		int i = reqTestClassname.length - 1;
		return reqTestClassname[i];
	}

	public void waitForPresenceOfElement(By locator, int timeOut, String element) {
		String msg;
		long time = System.currentTimeMillis();
		try {
			WebDriverWait wait = null;
			wait = new WebDriverWait(driver, timeOut);
			wait.until(ExpectedConditions.presenceOfElementLocated(locator));
			msg = "Time taken to find out the element \"" + element + "\" is "
					+ ((System.currentTimeMillis() - time) / 1000) + " seconds.";
			// System.out.println(msg);
		} catch (Exception e) {
			System.out.println("Ohh! " + element + " couldn't be found. The Timeout happened in "
					+ ((System.currentTimeMillis() - time) / 1000) + " seconds.");
		}
	}

	public String returnElementText(String element) {
		return findElementByXpath(element).getText();
	}

	public void waitForVisibilityOfElement(By locator, int timeOut, String element) {
		String msg;
		long time = System.currentTimeMillis();
		try {
			WebDriverWait wait = null;
			wait = new WebDriverWait(driver, timeOut);
			wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			msg = "Time taken to find out the element \"" + element + "\" is "
					+ ((System.currentTimeMillis() - time) / 1000) + " seconds.";
			// System.out.println(msg);
		} catch (Exception e) {
			System.out.println(
					"The " + element + " couldn't be visbile in " + timeOut + " seconds. The Timeout happened in "
							+ ((System.currentTimeMillis() - time) / 1000) + " seconds.");
		}
	}

	public void waitForElementVisiblisty(WebElement element, int timeoutInSeconds) {
		WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
		wait.until(ExpectedConditions.visibilityOf(element));

	}

	public void alertaccept() {
		Alert simpleAlert = driver.switchTo().alert();
		String alertText = simpleAlert.getText();
		System.out.println("Alert text is " + alertText);
		simpleAlert.accept();
	}
}
