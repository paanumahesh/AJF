package org.cruk.automation.framework;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;

public class Hooks {
	private final WebUtils utils;
	private static final Logger LOGGER = LoggerFactory.getLogger(Hooks.class);

	private static boolean initialized = false;

	public Hooks(WebUtils webUtils) {
		utils = webUtils;
	}

	/**
	 * * @author: Gaurav Seth
	 * 
	 * @since: 5th September 2016
	 * @param : Scenario scenario
	 * @return : void
	 * @Description :This function will print the scenario the test is going to
	 *              execute
	 * 
	 * */

	@Before(order = 1)
	public void before(Scenario scenario) {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Executing scenario -->" + scenario.getName() + "\n");
		}
	}

	/**
	 * * @author: Gaurav Seth
	 * 
	 * @since: 5th September 2016
	 * @param : Scenario scenario
	 * @return : void
	 * @Description :This function will capture the screenshot and embed to the
	 *              failed test
	 * 
	 * */

	@After(order = 4)
	public void captureScreenshot(Scenario scenario) {
		if (scenario.isFailed()) {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("captureScreenshot() - order=4 - capturing the screenshot for the failed test"
						+ "\n");
			}

			try {
				byte[] screenshot = utils.getDriver().getScreenshotAs(
						OutputType.BYTES);
				scenario.embed(screenshot, "image/png");
			} catch (WebDriverException somePlatformsDoNotSupportScreenShots) {
				LOGGER.info(somePlatformsDoNotSupportScreenShots.getMessage());
			}
		}
	}

	/**
	 * * @author: Gaurav Seth
	 * 
	 * @since: 5th September 2016
	 * @param : Scenario scenario
	 * @return : void
	 * @Description :This function will print the scenario status at the end of
	 *              test execution
	 * 
	 * */

	@After(order = 3)
	public void printScenarioStatus(Scenario scenario) {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("printScenarioStatus() order=3"
					+ "Scenario with name  -->" + scenario.getName()
					+ " --> has been executed and its status is -->"
					+ scenario.getStatus() + "\n");
		}

	}

	/**
	 * * @author: Gaurav Seth
	 * 
	 * @since: 5th September 2016
	 * @param : Scenario scenario
	 * @return : void
	 * @Description :This function will delete the cookies at the end of test
	 *              execution
	 * 
	 * */
	@After(order = 2)
	public void deleteCookiesAfterTestExecution() {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("deleteCookiesAfterTestExecution() -order=2, Delete all cookies at the end of the test execution");
		}
		utils.getDriver().manage().deleteAllCookies();
	}

	/**
	 * * @author: Gaurav Seth
	 * 
	 * @since: 5th September 2016
	 * @param : Scenario scenario
	 * @return : void
	 * @Description :This function will close all the browsers opened by the
	 *              WebDriver and also Kill the browser related drivers from the
	 *              processes
	 * 
	 * */

	@cucumber.api.java.Before
	public void setUp() throws Throwable {
		if (!initialized) {
			// Init context. Run just once before first scenario starts

			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					if (LOGGER.isInfoEnabled()) {
						LOGGER.info("End of test - closing all Webdriver Browsers");
					}
					utils.getDriver().quit();

					if (LOGGER.isInfoEnabled()) {
						LOGGER.info("End of test - Killing All WebDrivers");
					}
					try {
						utils.killDrivers();
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
			});

			initialized = true;
		}

	}

}
