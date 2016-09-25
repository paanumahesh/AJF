package org.cruk.automation.framework.pages;

import org.openqa.selenium.WebElement;
import org.cruk.automation.framework.WebUtils;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginPage 
{

	@FindBy(id="edit-name")
	WebElement username;
	
	@FindBy(id="edit-pass")
	WebElement password;

	@FindBy(id="edit-submit")
	WebElement login;
	
	@FindBy(css=".admin-menu-account strong")
	WebElement loggedInUser;
	
	@FindBy(css="a[href='/user/logout']")
	WebElement logout;
	
	WebUtils webUtils;
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(LoginPage.class);
	
	public LoginPage(WebUtils utils) 
	{
		webUtils =  utils;
		PageFactory.initElements(webUtils.getDriver(), this);
	}
	
	public void loginAs(String role) throws Throwable
	{
		String uName = null;
		String pwd = null;
		//webUtils.visitPage(webUtils.getValueFromProperties("baseUrl") + "/user");
		//webUtils.waitForSeconds(2);
		if (isAnyoneLoggedIn())
		{
			logoutUser();
		}
		webUtils.visitPage(webUtils.getValueFromProperties("baseUrl") + "/user");
		switch (role.toLowerCase()) 
		{
			case "administrator":
				uName = webUtils.getValueFromProperties("adminUser");
				pwd = webUtils.getValueFromProperties("adminPassword");
				break;
			
			case "crawler content admin":
				uName =  webUtils.getValueFromProperties("ccAdminUser");
				pwd = webUtils.getValueFromProperties("ccAdminPassword");
				break;
				
			case "event administrator":
				uName = webUtils.getValueFromProperties("eventAdminUser");
				pwd = webUtils.getValueFromProperties("eventAdminPassword");
				break;
		}
		webUtils.fillTextField(username, uName);
		webUtils.fillTextField(password, pwd);
		LOGGER.info("Logged in as user: " + uName);
		webUtils.clickButton(login);
		
	}
	
	private boolean isAnyoneLoggedIn() 
	{
		boolean isAnyoneLoggedIn = false;
		try
		{
			if(logout.isDisplayed())
			{
				isAnyoneLoggedIn = true;
			}
		}
		catch(Exception ex)
		{
			//Element is not displayed nea
			isAnyoneLoggedIn = false;
		}
		return isAnyoneLoggedIn;
	}

	public void logoutUser() 
	{
		webUtils.clickButton(logout);
	}

	public String getLoggedInUserName()
	{
		return loggedInUser.getText();
	}
	
}
