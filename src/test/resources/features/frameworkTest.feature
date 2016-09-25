Feature: Feature file to test the automation framework library
	I want to use this library to build my test automation

@sanity
Scenario: Verify AJ framwework build successfully
Given I am on "/"
And I click on "Donate" link
Then I should see "Choose where your donation goes" on the page