package org.cruk.automation.framework;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Platform;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cucumber.api.DataTable;

public class WebUtils {

	private static Map<String, EventFiringWebDriver> drivers = new HashMap<String, EventFiringWebDriver>();
	private static final String TASKLIST = "tasklist";
	private static String KILL = "\\System32\\taskkill /F /IM ";

	private EventFiringWebDriver driver;
	private Properties props;
	private WebDriverWait wait;
	public final String EMAIL = "email";
	public final String TEXT = "text message";
	public final String POST = "post";
	public final String PHONE = "phone";

	private static final Logger LOGGER = LoggerFactory
			.getLogger(WebUtils.class);

	@FindBy(tagName = "button")
	List<WebElement> allButtons;

	@FindBy(tagName = "a")
	List<WebElement> allLinks;

	// Find all labels on page
	@FindBy(tagName = "label")
	List<WebElement> allLabels;

	// Find all error messages on the page
	@FindBy(css = "div [generated='true'].error")
	List<WebElement> allErrors;

	@FindBy(name = "payment_card.name")
	WebElement cardHolderName;

	@FindBy(name = "payment_card.number")
	WebElement cardNumber;

	@FindBy(name = "payment_card.expiry_date.month")
	WebElement expDateMonth;

	@FindBy(name = "payment_card.expiry_date.year")
	WebElement expDateYear;

	@FindBy(name = "payment_card.cvc")
	WebElement cardSecurityCode;

	@FindBy(id = "payNow")
	WebElement payNowBtn;

	@FindBy(id = "creditcard")
	WebElement cardPayment;

	public void loadProperties(String propFile) throws Throwable {
		props = new Properties();
		FileInputStream in = new FileInputStream(new File(propFile));
		props.load(in);
		driver = getBrowser(props.getProperty("browserName"));
		wait = new WebDriverWait(driver, 20);
	}

	public WebDriverWait waitFor() {
		return wait;
	}

	public WebUtils() throws Throwable {
		// System.out.println("project path: " +
		// System.getProperty("user.dir"));
		loadProperties("src\\test\\resources\\test.properties");
		// loadProperties(System.getProperty("user.dir") +
		// "\\src\\test\\resources\\test.properties");
		PageFactory.initElements(driver, this);
	}

	/**
	 * * @author: Sameer Shirwadkar
	 * 
	 * @since: 5th September 2016
	 * @return:EventFiringWebDriver
	 * @Description: This function will return EventFiringWebDriver To make sure
	 *               we return only single instance and also making sure we are
	 *               not returning null object
	 * 
	 * */
	public EventFiringWebDriver getDriver() {
		try {
			if (driver == null) {
				String browserName = props.getProperty("browserName");
				LOGGER.info("driver is null getting new instance of "
						+ browserName);
				driver = getBrowser(browserName);
			}

		} catch (Throwable t) {
			handleErrors(t, "Getting error while creating driver instance");
		}
		return driver;
	}

	public String getValueFromProperties(String key) {
		return props.getProperty(key);
	}

	private EventFiringWebDriver getBrowser(String browserName)
			throws Throwable {
		EventFiringWebDriver driver = null;
		switch (browserName.toLowerCase()) {
		case "firefox":
			driver = drivers.get("Firefox");
			if (driver == null) {
				driver = new EventFiringWebDriver(new FirefoxDriver());
				drivers.put("Firefox", driver);
				driver.manage().deleteAllCookies();
			}
			break;
		case "ie":
			driver = drivers.get("IE");
			if (driver == null) {
				System.setProperty("webdriver.ie.driver",
						getValueFromProperties("IE_DRIVER_PATH"));
				driver = new EventFiringWebDriver(new InternetExplorerDriver());
				drivers.put("IE", driver);
				driver.manage().deleteAllCookies();
			}
			break;
		case "chrome":
			driver = drivers.get("Chrome");
			if (driver == null) {
				System.setProperty("webdriver.chrome.driver",
						getValueFromProperties("CHROME_DRIVER_PATH"));
				driver = new EventFiringWebDriver(new ChromeDriver());
				drivers.put("Chrome", driver);
				driver.manage().deleteAllCookies();
			}
			break;
		
		
		case "android":
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
			capabilities.setCapability("app", "Chrome");
			capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "4.2");
			capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "Chrome");
			capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "4df78fc10ad631c3");

			try {
			    URL url = new URL("http://127.0.0.1:4725/wd/hub");
			    driver = new EventFiringWebDriver(new AndroidDriver(url, capabilities));
			    //driver = new SelendroidDriver(sc);
			} catch (Exception e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}

		}
		return driver;
	}

	public String getRandomAlphaNumericString(int len) {
		String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		SecureRandom rnd = new SecureRandom();

		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++)
			sb.append(AB.charAt(rnd.nextInt(AB.length())));
		return sb.toString();
	}

	public String getRandomAlphaString(int len) {
		String AB = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		SecureRandom rnd = new SecureRandom();

		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++)
			sb.append(AB.charAt(rnd.nextInt(AB.length())));
		return sb.toString();
	}

	public void fillTextField(WebElement element, String value) {
		if (element != null) {
			waitUntilElementIsVisible(element);
			element.clear();
			element.sendKeys(value, Keys.TAB);
		}

	}

	public void clickButton(WebElement element) {
		if (element != null) {
			waitUntilElementIsClickable(element);
			Actions actions = new Actions(driver);
			actions.moveToElement(element);
			element.click();
		} else {
			LOGGER.info("Element is null");
		}
	}

	public WebElement waitUntilElementIsClickable(WebElement element) {
		return waitFor()
				.until(ExpectedConditions.elementToBeClickable(element));
	}

	public WebElement waitUntilElementIsVisible(WebElement element) {
		return waitFor().until(ExpectedConditions.visibilityOf(element));
	}

	public void selectValueFromDropdown(WebElement element, String value) {
		try {
			if (element != null) {
				Select selElement = new Select(element);
				selElement.selectByVisibleText(value);
			} else {
				LOGGER.info("Found Null element in selectValueFromDropdown method");
			}
		} catch (Exception e) {
			handleException(e, "Received error while setting value: " + value
					+ " for element: " + element.getText());
		}
	}

	public void selectAddressFaster(WebElement ele, String pc, String expAddress)
			throws Exception {
		// driver.findElement(By.id("edit-customer-profile-shipping-commerce-customer-address-und-0-thoroughfare"));
		LOGGER.info("Looking for address: " + expAddress);
		for (int i = 1; i <= pc.length(); i++) {
			waitUntilElementIsVisible(ele);
			ele.sendKeys(pc.substring((i - 1), i));
			waitForSeconds(0.250);
		}

		// List<WebElement> allAddress =
		// driver.findElements(By.className("pcaitem")); "pcaselected"
		List<WebElement> allAddress = driver.findElements(By
				.className("pcaselected"));
		// System.out.println("Total address found @ First level: " +
		// allAddress.size());
		boolean addrFound = false;
		for (WebElement addr : allAddress) {
			LOGGER.info("Found address:  " + addr.getAttribute("title"));
			try {
				// this.waitFor().until(ExpectedConditions.elementToBeSelected(addr));
				if (addr.getAttribute("title").contains(expAddress)) {
					addr.click();
					addrFound = true;
					break;
				}
			} catch (WebDriverException somethingWentWrongTryAgain) {
				LOGGER.info(somethingWentWrongTryAgain.getMessage());
				// selectAddressFaster(ele, pc, expAddress);
			}
		}
		if (!addrFound) {
			throw new Exception("Not able to set the address: " + expAddress);
		}
	}

	public void waitForSeconds(double seconds) 
	{
		try 
		{
			Thread.sleep(new Double((seconds * 1000)).longValue());
		} 
		catch (InterruptedException e) 
		{
			handleException(e, "Wait got interrupted");
		}
	}

	public void enterPassword(WebElement password, String passwordText)
			throws Throwable {

		for (int i = 0; i < passwordText.length(); i++) {
			password.sendKeys(String.valueOf(passwordText.charAt(i)));
			waitForSeconds(0.125);
		}

		waitForSeconds(0.125);
	}

	public String getUniqueString() {
		return String.valueOf(System.currentTimeMillis());
	}

	public void continuePaymentUsingCreditCard() {

		clickButton(cardPayment);
		fillTextField(cardHolderName, "Special Supporter");
		fillTextField(cardNumber, "4111111111111111");
		selectValueFromDropdown(expDateMonth, "03");
		selectValueFromDropdown(expDateYear, "2018");
		fillTextField(cardSecurityCode, "786");
		clickButton(payNowBtn);
	}

	public void assertPageURL(String url) {
		Assert.assertEquals(driver.getCurrentUrl(), url);
	}

	public void assertPageContainsElement(WebElement element) {
		Assert.assertTrue(element.isDisplayed());
	}

	public void assertPageContainsText(String text) {
		// wait = new WebDriverWait(driver, 5);
		Assert.assertTrue(driver.getPageSource().contains(text));
	}

	public boolean isPageContainsText(String text) {
		return driver.getPageSource().contains(text);
	}

	public void fillValueForLabel(String label, String value) {
		// System.out.println("lable name: " + label + " & Value is: " + value);
		WebElement element = getInputElementForLabel(label);
		if (element != null) {
			switch (element.getTagName().toLowerCase()) {
			case "input":
				fillTextField(element, value);
				break;

			case "select":
				selectValueFromDropdown(element, value);
				break;

			case "textarea":
				fillTextField(element, value);
				break;

			default:
				System.out.println("No Element Type found: "
						+ element.getTagName());
				break;
			}
		} else {
			System.out.println("Input element not found for: " + label);
		}
	}

	private WebElement getInputElementForLabel(String label) {
		return getElementFromLable(label, "input");
	}

	public WebElement getErrorElementForLabel(String label) {
		return getElementFromLable(label, "error");
	}

	private WebElement getElementFromLable(String label, String elementType) {
		WebElement errElement = null;
		String cssLocator = null;
		for (WebElement currLabel : allLabels) {
			String labelName = currLabel.getText();

			// If label contains * at end, strip * from label name
			if (labelName.endsWith("*")) {
				labelName = labelName.substring(0, labelName.indexOf('*'));
			}

			if (labelName.trim().equalsIgnoreCase(label.trim())) {
				switch (elementType.toLowerCase()) {
				case "error":
					cssLocator = "label[for=\"" + currLabel.getAttribute("for")
							+ "\"].error";
					break;

				case "input":
					cssLocator = "#" + currLabel.getAttribute("for");
					break;
				}
				errElement = getDriver()
						.findElement(By.cssSelector(cssLocator));
				break;
			}
		}
		return errElement;
	}

	/**
	 * @author Gaurav Seth
	 * @since 30th August 2016
	 * @param String
	 *            servicename
	 * @Description This function will check if servicename passed to it is
	 *              running on the system and accordingly return boolean value
	 * 
	 * */

	public static boolean isProcessRunning(String serviceName) throws Exception {

		Process p = Runtime.getRuntime().exec(TASKLIST);
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				p.getInputStream()));
		String line;
		while ((line = reader.readLine()) != null) {
			// System.out.println(line);
			if (line.contains(serviceName)) {
				return true;
			}
		}
		return false;
	}

	public void killDrivers() throws Throwable {

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Killing the drivers after test execution");
		}

		if (isProcessRunning("IEDriverServer.exe")) {
			killProcess("IEDriverServer.exe");
		} else if (isProcessRunning("chromedriver.exe")) {
			killProcess("chromedriver.exe");
		} else {
			killProcess("firefoxdriver.exe");
		}

	}

	/**
	 * @author Gaurav Seth
	 * @since 30th August 2016
	 * @param String
	 *            servicename
	 * @Description This function will kill the service passed to it
	 * 
	 * */

	public static void killProcess(String serviceName) throws Exception {
		KILL = System.getenv("SystemRoot") + KILL;
		Runtime.getRuntime().exec(KILL + serviceName);
	}

	/**
	 * @author Gaurav Seth
	 * @since 22th August 2016
	 * @param WebElement
	 *            element
	 * @return void
	 * @Description This function will check that element is enabled.
	 * 
	 * */

	public void clickWebElementIsEnabled(WebElement element) {
		Assert.assertTrue(element.isEnabled());
	}

	public String getCurrentUrl() {
		return getDriver().getCurrentUrl();
	}

	public void visitPage(String newUrl) {
		LOGGER.info("Visiting page: " + newUrl);
		getDriver().navigate().to(newUrl);
		getDriver().manage().window().maximize();
	}

	private boolean isExpectedError(String errorText, String[] expectedMessages) {
		boolean isValidErrorMessage = false;

		for (int i = 0; i < expectedMessages.length; i++) {
			if (expectedMessages[i].equals(errorText)) {
				isValidErrorMessage = true;
				break;
			}
		}

		return isValidErrorMessage;
	}

	public String[] populateArray(String inputString) {
		String[] stringToArray = inputString.split("\n");
		return stringToArray;
	}

	public boolean validateForm(List<TestCaseData> allTCData) {
		boolean allValidationsTrue = true;
		String homePage = driver.getCurrentUrl();
		System.out.println("No of TCs: " + allTCData.size());
		String[] allColumns = allTCData.get(0).getAllColumns();
		String[] expectedErrorMessages = null;
		String tcName = null;
		int passCnt = 0;
		int failCnt = 0;

		for (TestCaseData currentTC : allTCData) {

			this.visitPage(homePage);
			// Fill all form fields from Spreadsheet except the Expected Error
			// Messages
			for (int i = 0; i < allColumns.length; i++) {
				String labelName = allColumns[i];
				if (labelName.equalsIgnoreCase("Expected Error Messages")) {
					// get the expected error values
					String expectedErrorString = currentTC
							.getValueForColumn("Expected Error Messages");
					// System.out.println("expectedErrorString: " +
					// expectedErrorString);
					if (expectedErrorString != null) {
						// System.out.println("No error messages expected");
						expectedErrorMessages = populateArray(expectedErrorString);

					}
				} else if (labelName.equalsIgnoreCase("TC Scenario")) {
					tcName = currentTC.getValueForColumn(labelName);
				} else {
					String valueForField = currentTC
							.getValueForColumn(labelName);
					if (valueForField != null) {
						fillValueForLabel(labelName, valueForField);
					}
				}
			}

			try {
				boolean currentTCValidation = verifyAllErrorMessages(expectedErrorMessages);
				if (currentTCValidation) {
					System.out.println(tcName + " : Pass");
					passCnt++;
				} else {
					System.out.println(tcName + " : Fail");
					System.out
							.println("Expected error: "
									+ currentTC
											.getValueForColumn("Expected Error Messages"));
					allValidationsTrue = currentTCValidation;
					failCnt++;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}

		System.out.println("*********");

		System.out.println("Total TCs pexecuted: " + allTCData.size());
		System.out.println("Total TCs pass: " + passCnt);
		System.out.println("Total TCs failed: " + failCnt);

		System.out.println("*********");

		return allValidationsTrue;

	}

	public void printTextFromAllElements(List<WebElement> allElements) {
		for (WebElement currElement : allElements) {
			System.out.println(currElement.getText());
		}
	}

	public void handleException(Exception e, String message) {
		LOGGER.info(message + e.getMessage());
	}

	public void handleErrors(Throwable t, String message) {
		LOGGER.info(message + t.getMessage());
	}

	public void verifyAllErrorMessages(List<String> expectedMessages) {
		boolean result = true;
		if (expectedMessages.size() == allErrors.size()) {
			for (WebElement error : allErrors) {

				if (!expectedMessages.contains(error.getText())) {
					System.out.println("Error message was not expected:  "
							+ error.getText());
					result = false;
				} else {
					break;
				}
			}
		} else {
			System.out.println("Expected error messages: "
					+ expectedMessages.size() + " found: " + allErrors.size());
			result = false;
		}
		Assert.assertTrue("Errors are not matching", result);
	}

	public boolean verifyAllErrorMessages(String[] expectedMessages) {
		boolean result = true;
		if (expectedMessages == null && allErrors.isEmpty()) {
			result = true;
		} else if (expectedMessages.length == allErrors.size()) {
			for (WebElement currentError : allErrors) {
				if (!isExpectedError(currentError.getText(), expectedMessages)) {
					result = false;
					System.out.println("Unexpected error found: "
							+ currentError.getText());
					break;
				}
			}
		} else {
			System.out
					.println("No of errors are not matching. Expected error messages : "
							+ expectedMessages.length
							+ " found : "
							+ allErrors.size());
			printTextFromAllElements(allErrors);
			result = false;
		}
		return result;
	}

	public void optInFor(String option) {
		String cssLocator = null;
		switch (option.toLowerCase()) {
		case EMAIL:
			cssLocator = "label[for='edit-opt-in-join-email-pyem']";
			if (!isElementFound("css", cssLocator)) {
				cssLocator = "input[value='PYEM']";
			}
			break;

		case TEXT:
			cssLocator = "label[for='edit-opt-in-join-text-pysm']";
			if (!isElementFound("css", cssLocator)) {
				cssLocator = "input[value='PYSM']";
			}
			break;

		case POST:
			cssLocator = "label[for='edit-opt-in-join-post-pypo']";
			if (!isElementFound("css", cssLocator)) {
				cssLocator = "input[value='PYPO']";
			}
			break;

		case PHONE:
			cssLocator = "label[for='edit-opt-in-join-phone-pyph']";
			if (!isElementFound("css", cssLocator)) {
				cssLocator = "input[value='PYPH']";
			}

			break;
		}

		clickButton(driver.findElement(By.cssSelector(cssLocator)));

	}

	public void optOutFrom(String option) {
		String cssLocator = null;
		switch (option.toLowerCase()) {
		case EMAIL:
			cssLocator = "label[for='edit-opt-in-join-email-pnem']";
			if (!isElementFound("css", cssLocator)) {
				cssLocator = "input[value='PNEM']";
			}
			break;

		case TEXT:
			cssLocator = "label[for='edit-opt-in-join-text-pnsm']";
			if (!isElementFound("css", cssLocator)) {
				cssLocator = "input[value='PNSM']";
			}
			break;

		case POST:
			cssLocator = "label[for='edit-opt-in-join-post-pnpo']";
			if (!isElementFound("css", cssLocator)) {
				cssLocator = "input[value='PNPO']";
			}
			break;

		case PHONE:
			cssLocator = "label[for='edit-opt-in-join-phone-pnph']";
			if (!isElementFound("css", cssLocator)) {
				cssLocator = "input[value='PNPH']";
			}
			break;
		}

		clickButton(driver.findElement(By.cssSelector(cssLocator)));

	}

	private boolean isElementFound(String locatorTpye, String locator) {
		boolean isElementFound = false;
		WebElement ele = null;
		try {
			switch (locatorTpye.toLowerCase()) {

			case "id":
				ele = driver.findElement(By.id(locator));
				if (ele != null) {
					isElementFound = true;
				}
				break;

			case "css":
				ele = driver.findElement(By.cssSelector(locator));
				if (ele != null) {
					isElementFound = true;
				}
				break;

			case "link":
				ele = driver.findElement(By.linkText(locator));
				if (ele != null) {
					isElementFound = true;
				}
				break;

			case "className":
				ele = driver.findElement(By.className(locator));
				if (ele != null) {
					isElementFound = true;
				}
				break;

			case "name":
				ele = driver.findElement(By.name(locator));
				if (ele != null) {
					isElementFound = true;
				}
				break;

			case "tagName":
				ele = driver.findElement(By.tagName(locator));
				if (ele != null) {
					isElementFound = true;
				}
				break;

			case "xpath":
				ele = driver.findElement(By.xpath(locator));
				if (ele != null) {
					isElementFound = true;
				}
				break;

			}
		} catch (Exception e) {
			isElementFound = false;
		}

		return isElementFound;
	}

	public void verifyAllFieldsAreNotMandatory(List<String> allLabels) {
		boolean isMandatory = false;
		for (String label : allLabels) {
			WebElement ele = getInputElementForLabel(label);
			if (ele.getAttribute("required") != null) {
				isMandatory = true;
				System.out.println("Expected the field: " + label
						+ " optional but found mandatory");
			}
		}
		Assert.assertFalse(isMandatory);
	}

	public void clickButtonWithLabel(String label) {

		for (WebElement btn : allButtons) {
			if (btn.getText().equals(label)) {
				clickButton(btn);
				break;
			}
		}
	}

	public void verifyALLValuesForFields(Map<String, String> allData) {
		boolean areAllValuesCorrect = true;
		Iterator<String> itr = allData.keySet().iterator();

		while (itr.hasNext()) {
			String label = itr.next();
			if (!isValueInFieldCorrect(getInputElementForLabel(label),
					allData.get(label))) {
				areAllValuesCorrect = false;
			}
		}
		Assert.assertTrue("All Values are not as expected", areAllValuesCorrect);
	}

	public boolean isValueInFieldCorrect(WebElement element,
			String expectedValue) {
		boolean isValueCorrect = false;

		if (element != null) {
			String actualValue = element.getAttribute("value");
			if (actualValue.equals(expectedValue)) {
				isValueCorrect = true;
			} else {
				System.out.println("Expected value: " + expectedValue
						+ " found value: " + actualValue);
				isValueCorrect = false;
			}
		} else {
			// System.out.println("Element is null");
			isValueCorrect = false;
		}

		return isValueCorrect;
	}

	public void verifyPageContains(String searchString) {
		Assert.assertTrue("Expected String not found on page", driver
				.getPageSource().contains(searchString));
	}

	public WebElement getElementUsingCSS(String cssLocator) throws Exception {
		WebElement ele = null;
		ele = driver.findElement(By.cssSelector(cssLocator));
		if (ele == null) {
			throw new Exception("Can not find element with locator: "
					+ cssLocator);
		}
		return ele;
	}

	public void selectGenderOption(String option) {
		String cssLocator = null;

		switch (option.toLowerCase()) {
		case "male":
			cssLocator = "input[value='Male']";
			break;

		case "female":
			cssLocator = "input[value='Female']";
			break;
		}

		try {
			clickButton(getElementUsingCSS(cssLocator));
		} catch (Exception e) {
			handleException(e, "Received Error while chosing gender");
		}
	}

	public void setJoinUsOptions(String email, String text, String post,
			String phone) {
		if (email != null) {
			if (email.equalsIgnoreCase("Yes")) {
				optInFor("email");
			} else {
				optOutFrom("email");
			}
		}

		if (text != null) {
			if (text.equalsIgnoreCase("Yes")) {
				optInFor("text message");
			} else {
				optOutFrom("text message");
			}
		}

		if (post != null) {
			if (post.equalsIgnoreCase("Yes")) {
				optInFor("post");
			} else {
				optOutFrom("post");
			}
		}

		if (phone != null) {
			if (phone.equalsIgnoreCase("Yes")) {
				optInFor("phone");
			} else {
				optOutFrom("phone");
			}
		}
	}

	public String getValueFromTextField(WebElement element) {
		String value = null;
		if (element != null) {
			value = element.getAttribute("value");
		}
		return value;
	}

	public String getExpectedAddress(TestCaseData currentTC) {
		String expectedAddress = currentTC.getValueForColumn("Post Code")
				+ ", " + currentTC.getValueForColumn("Address line 1");

		if (currentTC.getValueForColumn("Address line 2") != null) {
			expectedAddress = expectedAddress + ", "
					+ currentTC.getValueForColumn("Address line 2");
		}

		if (currentTC.getValueForColumn("Address line 3") != null) {
			expectedAddress = expectedAddress + ", "
					+ currentTC.getValueForColumn("Address line 3");
		}

		expectedAddress = expectedAddress + ", "
				+ currentTC.getValueForColumn("City");

		LOGGER.info("expectedAddress: " + expectedAddress);
		return expectedAddress;
	}

	private void addAddressManually(TestCaseData currentTC) {
		/*
		 * currentTC.getValueForColumn("Country"),
		 * currentTC.getValueForColumn("Address line 1"),
		 * currentTC.getValueForColumn("Address line 2"),
		 * currentTC.getValueForColumn("Address line 3"),
		 * currentTC.getValueForColumn("City"),
		 * currentTC.getValueForColumn("Post Code")
		 */
	}

	public WebElement getElementUsingName(String name) {
		return driver.findElement(By.name(name));

	}

	public boolean verifyPageDoesNotContain(String searchString) {
		return (!driver.getPageSource().contains(searchString));

	}

	public void closeBrowser() {
		Set<String> keyset = drivers.keySet();
		Iterator<String> iterator = keyset.iterator();

		while (iterator.hasNext()) {
			drivers.get(iterator.next()).close();
		}
	}

	public void fillHiddenField(String fieldName, String value) {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		String command = "document.getElementsByName(\'" + fieldName
				+ "')[0].value=\'" + value + "\';";
		System.out.println("command: " + command);
		jse.executeScript(command);

	}

	public void clickButtonWithValue(String buttonName) throws Exception {
		String cssLocator = "input[value='" + buttonName + "']";
		clickButton(getElementUsingCSS(cssLocator));
	}

	public void clickOnLink(String linkName) throws Exception {
		WebElement ele = driver.findElement(By.linkText(linkName));
		if (ele != null) {
			ele.click();
		} else {
			throw new Exception(linkName + " link not found");
		}
	}

	/**
	 * * @author: Gaurav Seth
	 * 
	 * @since: 17th August 2016
	 * @param : WebElement element
	 * @Description This function will click on any webElement but use Actions
	 *              class. It takes an WebElement element as parameter and would
	 *              click on that WebElement For Example it can click on button,
	 *              link etc.
	 * 
	 * */
	public void clickWebElementUsingActionsClass(WebElement element) {
		if (element != null) {
			waitUntilElementIsClickable(element);
			Actions actions = new Actions(driver);
			actions.moveToElement(element);
			element.click();
		} else {
			System.out.println("Element is null");
		}
	}

	/**
	 * @author Gaurav Seth
	 * @since 17th August 2016
	 * @param WebElement
	 *            element
	 * @Description This function will click on any webElement. It takes an
	 *              WebElement element as parameter and would click on that
	 *              WebElement For Example it can click on button, link etc.
	 * 
	 * */

	public void clickWebElement(WebElement element) {
		if (element != null) {
			waitUntilElementIsClickable(element);
			element.click();
		} else {
			System.out.println("Element is null");
		}
	}

	/**
	 * @author : Gaurav Seth
	 * @since : 16th August 2016
	 * @param : WebElement element
	 * @return : void Description :This function will clear a webElement passed
	 *         to it
	 **/
	public void clearWebElement(WebElement element) {
		element.click();
		element.clear();
	}

	/**
	 * @author : Gaurav Seth
	 * @since : 16th August 2016
	 * @param : String Expected Text
	 * @return : void Description :This function will check that the text passed
	 *         to it as parameter appears on the webPage. If it does not appears
	 *         the assertion will fail.
	 **/

	public void checkTextAppears(String expectedText) {
		LOGGER.info("Checking the text appears on the page --> " + expectedText);
		List<WebElement> list = driver.findElements(By
				.xpath("//*[contains(text(),\"" + expectedText + "\")]"));
		Assert.assertTrue(list.size() > 0);
	}

	/**
	 * * @author: Gaurav Seth
	 * 
	 * @since: 6th September 2016
	 * @param : WebElement element
	 * @return : void
	 * @Description :This function will use javascript executor to click on a
	 *              WebElement. It takes WebElement as parameter
	 * 
	 * */

	public void JavaScriptClick(WebElement element) throws Throwable {
		LOGGER.info("User is using Javascript to click on element" + element);
		try {
			if (element.isEnabled() && element.isDisplayed()) {
				LOGGER.info("Clicking on element with using java script click");

				((JavascriptExecutor) driver).executeScript(
						"arguments[0].click();", element);
			} else {
				LOGGER.info("Unable to click on element element is either not enabled or not displayed");
			}
		} catch (StaleElementReferenceException e) {
			LOGGER.info("Element is not attached to the page document "
					+ e.getStackTrace());
		} catch (NoSuchElementException e) {
			LOGGER.info("Element was not found in DOM " + e.getStackTrace());
		} catch (Exception e) {
			LOGGER.info("Unable to click on element " + e.getStackTrace());
		}
	}

	
	/**
	 * @author: Gaurav Seth
	 * @since: 08th September 2016
	 * @param : WebElement element
	 * @return : void
	 * @Description :This function attempts to click on element multiple times to avoid stalement reference exception caused by rapid DOM refresh
	 * 
	 * */

	public void dependableClick(WebElement by) {
		final int MAXIMUM_WAIT_TIME = 10;
		final int MAX_STALE_ELEMENT_RETRIES = 5;

		int retries = 0;
		while (true) {
			try {
				wait.until(ExpectedConditions.elementToBeClickable(by)).click();
				return;
			} catch (StaleElementReferenceException e) {
				if (retries < MAX_STALE_ELEMENT_RETRIES) {
					retries++;
					continue;
				} else {
					throw e;
				}
			}
		}
	}
	
	/**
	 * @author: Gaurav Seth
	 * @since: 08th September 2016
	 * @param : String script, WebElement element
	 * @return : void 
	 * @Description :This function will execute javascript on a webelement 
	 * 
	 * */


	public void executeJavascript(String script, WebElement element) {
		((JavascriptExecutor) driver).executeScript(script,element);
	}

		
	/**
	 * * @author: Gaurav Seth
	 * 
	 * @since: 16th August 2016
	 * @param :String Expected Text
	 * @return : void Description :This function will check that the text passed
	 *         to it as parameter does not appears on the Page.
	 * 
	 * */

	public void checkTextNotAppears(String expectedText) {
		LOGGER.info("Checking the text does not appears on the page -->"
				+ expectedText);
		List<WebElement> list = driver.findElements(By
				.xpath("//*[contains(text(),\"" + expectedText + "\")]"));
		LOGGER.info("Checking the size of list of checking Text does not appear" + list.size());
		Assert.assertTrue(list.size() == 0);
	}

	/**
	 * * @author: Sameer
	 * 
	 * @since: 		19th Sep 2016
	 * @param: 		WebElement
	 * @return: 	boolean 
	 * Description: Method checks whether element is visible or not. 
	 * 				Added this method as WebElement.isDisplayed() throws element not found exception in POM pattern   
	 * */
	public boolean isElementVisible(WebElement element) 
	{
		boolean isVisible = false;
		
			try 
			{
				if(element.isDisplayed())
				{
					isVisible = true;
					LOGGER.info("Element " + element.getTagName() + " is visible");
				}
			} 
			catch (NoSuchElementException  e) 
			{
				LOGGER.info("Element is not visible");
				isVisible = false;
			}
		
		return isVisible;
	}

	/**
	 * * @author: Aditi
	 * 
	 * @since: 		19th Sep 2016
	 * @param: 		DataTable
	 * @return: 	TestCaseData 
	 * Description: We use TestCaseData object to read data from excel. 
	 * 				By using this function we can convert the DataTable object to TestCaseData object 
	 * */
	
	public TestCaseData dataTableToTestCaseDataConvertor(DataTable testData)
	{
		TestCaseData currentTCData = new TestCaseData();
		
		//Convert DataTable to List of Map values       
		List<Map<String, String>> allTestInput = testData.asMaps(String.class, String.class);
		
		LOGGER.info("No of records: " + allTestInput.size());
		
		Iterator iterator = allTestInput.get(0).entrySet().iterator();
			
		while (iterator.hasNext())
		{
			Map.Entry pair = (Map.Entry)iterator.next();			
			currentTCData.addValueForColumn((String)pair.getKey(), (String)pair.getValue());
		}	
				
		return currentTCData;
	}
	
}
