package org.cruk.automation.framework.steps;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.cruk.automation.framework.ExcelDataParser;
import org.cruk.automation.framework.WebUtils;
import org.cruk.automation.framework.pages.LoginPage;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

// TODO: Auto-generated Javadoc
/**
 * The Class CommonSteps.
 */
public class CommonSteps 
{
	
	/** The utils. */
	WebUtils utils;
	LoginPage loginPage;
	
	
	/** The data parser. */
	private ExcelDataParser dataParser;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WebUtils.class);
	
	/**
	 * Instantiates a new common steps.
	 *
	 * @param webUtils the web utils
	 */
	public CommonSteps(WebUtils webUtils, LoginPage lPage) 
	{
		utils = webUtils;
		loginPage = lPage;
	}
	
	/**
	 * I am on.
	 *
	 * @param page the page
	 * @throws Throwable the throwable
	 */
	@Given("^I am on \"([^\"]*)\"$")
	public void i_am_on(String page) throws Throwable 
	{
		LOGGER.info("Executing from commonsteps.java");
		String baseURL = utils.getValueFromProperties("baseUrl") + page;
		utils.visitPage(baseURL);
	}
	
	/**
	 * I am not logged in.
	 * @author Shirwa01
	 * @throws Throwable the throwable
	 * 
	 */
	@Given("^I am not logged in$")
	public void i_am_not_logged_in() throws Throwable 
	{
		utils.visitPage(utils.getValueFromProperties("baseUrl") + "/user");
	}

	/**
	 * I visit.
	 * This step is used for visiting specific page or node
	 * @author Shirwa01
	 * @param: path - This is relative path for specific page
	 * @throws Throwable the throwable
	 * 	 
	 * */
	@When("^I visit \"([^\"]*)\"$")
	public void i_visit(String path) throws Throwable 
	{
		utils.visitPage(utils.getValueFromProperties("baseUrl") + path);
	}
	
	/**
	 * I execute from file.
	 *
	 * @param tabName the tab name
	 * @param filePath the file path
	 * @throws Throwable the throwable
	 */
	@When("^I execute \"([^\"]*)\" from file \"([^\"]*)\"$")
	public void i_execute_from_file(String tabName, String filePath) throws Throwable 
	{
		dataParser = new ExcelDataParser(); 
		dataParser.loadData(filePath, tabName);
		LOGGER.info("File loaded from: " + filePath + " from tab: " + tabName);
	}

	/**
	 * The validations should be successful for.
	 *
	 * @param formName the form name
	 * @throws Throwable the throwable
	 */
	@Then("^the validations should be successful for \"([^\"]*)\"$")
	public void the_validations_should_be_successful_for(String formName) throws Throwable 
	{
		Assert.assertTrue(utils.validateForm(dataParser.getAllData()));
	}
	
	/**
	 * I submit form using button.
	 *
	 * @param btnLabel the btn label
	 * @throws Throwable the throwable
	 */
	@When("^I submit form using button \"([^\"]*)\"$")
	public void i_submit_form_using_button(String btnLabel) throws Throwable 
	{
		String cssLocator = "input[value='" + btnLabel + "']";
		WebElement submitBtn = utils.getDriver().findElement(By.cssSelector(cssLocator));
		
		if(submitBtn != null)
		{
			utils.clickButton(submitBtn);
		}	
		else
		{
			throw new Exception("Submit button not found with label: " + btnLabel);
		}	
	}
	
	
	/**
	 * I should see on the page.
	 *
	 * @param searchString the search string
	 * @throws Throwable the throwable
	 */
	@Then("^I should see \"([^\"]*)\" on the page$")
	public void i_should_see_on_the_page(String searchString) throws Throwable 
	{
		utils.verifyPageContains(searchString);
	}

	
	/**
	 * I should see following error messages.
	 *
	 * @param expectedMessages the expected messages
	 * @throws Throwable the throwable
	 */
	@Then("^I should see following error messages$")
	public void i_should_see_following_error_messages(List<String> expectedMessages) throws Throwable 
	{
		utils.verifyAllErrorMessages(expectedMessages);
	}
	
	/**
	 * I fill in the following fields.
	 *
	 * @param allData the all data
	 * @throws Throwable the throwable
	 */
	@When("^I fill in the following fields:$")
	public void i_fill_in_the_following_fields(Map<String,String> allData) throws Throwable
	{
		Iterator<String> itr = allData.keySet().iterator();
		
		while(itr.hasNext())
		{
			String label = itr.next();
			utils.fillValueForLabel(label, allData.get(label));
		}	
	}
	
	/**
	 * Following fields should not bt mandatory.
	 *
	 * @param allLabels the all labels
	 * @throws Throwable the throwable
	 */
	@Then("^Following fields should not bt mandatory:$")
	public void following_fields_should_not_bt_mandatory(List<String> allLabels) throws Throwable 
	{
			utils.verifyAllFieldsAreNotMandatory(allLabels);
	}
	
	/**
	 * I opt in for.
	 *
	 * @param optin the optin
	 * @throws Throwable the throwable
	 */
	@Then("^I opt in for \"([^\"]*)\"$")
	public void i_opt_in_for(String optin) throws Throwable 
	{
		utils.optInFor(optin);
	}

	/**
	 * I opt out from.
	 *
	 * @param optout the optout
	 * @throws Throwable the throwable
	 */
	@Then("^I opt out from \"([^\"]*)\"$")
	public void i_opt_out_from(String optout) throws Throwable 
	{
		utils.optOutFrom(optout);
	}
	
	/**
	 * I fill in field with.
	 *
	 * @param label the label
	 * @param value the value
	 * @throws Throwable the throwable
	 */
	@When("^I fill in \"([^\"]*)\" field with \"([^\"]*)\"$")
	public void i_fill_in_field_with(String label, String value) throws Throwable 
	{
		utils.fillValueForLabel(label, value);
	}

	/**
	 * I press.
	 *
	 * @param label the label
	 * @throws Throwable the throwable
	 */
	@When("^I press \"([^\"]*)\"$")
	public void i_press(String label) throws Throwable 
	{
		utils.clickButtonWithValue(label);;
	}

	/**
	 * I should see following values for fields.
	 *
	 * @param allData the all data
	 * @throws Throwable the throwable
	 */
	@Then("^I should see following values for fields:$")
	public void i_should_see_following_values_for_fields(Map<String,String> allData) throws Throwable 
	{
		utils.verifyALLValuesForFields(allData);
	}

	
	@Given("^I am logged in as a user with the \"([^\"]*)\" role$")
	public void i_am_logged_in_as_a_user_with_the_role(String role) throws Throwable 
	{
		loginPage.loginAs(role);
	}
	
	/**
	 * I select option for gender.
	 *
	 * @param option the option
	 * @throws Throwable the throwable
	 */
	@When("^I select option \"([^\"]*)\" for Gender$")
	public void i_select_option_for_Gender(String option) throws Throwable 
	{
		utils.selectGenderOption(option);
	}
	
	/**
	 * I click on link.
	 *
	 * @param linkName the link name
	 * @throws Throwable the throwable
	 */
	@Given("^I click on \"([^\"]*)\" link$")
	public void i_click_on_link(String linkName) throws Throwable 
	{
		utils.clickOnLink(linkName);
	}

}

