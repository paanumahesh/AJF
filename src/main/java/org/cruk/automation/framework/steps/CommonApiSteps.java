package org.cruk.automation.framework.steps;

import com.sun.jersey.api.client.ClientResponse;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.commons.io.FileUtils;
import org.cruk.automation.framework.ApiUtils;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.File;
import java.io.IOException;
import java.lang.String;

/**
 * Created by turner34 on 25/08/2016.
 *
 * This adds the steps that can be commonly used
 */
public class CommonApiSteps {

    /**
     * The api utils.
     */
    ApiUtils api;

    //Create a logger instance in case we need to record errors etc
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiUtils.class);

    /**
     * Instantiates a new common api steps.
     *
     * @param apiUtils - the apiutils
     */
    public CommonApiSteps(ApiUtils apiUtils)
    {
        api = apiUtils;
    }

    /**
     * I get an Authentication code
     *
     * @param apiAuthURL - the authentication URL
     * @throws Throwable the throwable
     */
    @Given("^I get an authentication code at URL \"([^\"]*)\"$" )
    public void i_get_an_authentication_code(String apiAuthURL) throws Throwable
    {
        LOGGER.info("Executing method: i_get_an_authentication_code from commonsteps.java");
        api.setAccessToken(apiAuthURL);
    }

    /**
     * I connect to the api at URL with Authentication code
     *
     * @param apiURL    - the URL where the api resides
     * @param apiParams - the parameters needed by the api
     *
     * @throws Throwable the throwable
     */
    @When("^I connect to the api at \"([^\"]*)\" with parameters \"([^\"]*)\" and the authentication code$" )
    public void i_connect_to_the_api_at(String apiURL,String apiParams) throws Throwable
    {
        LOGGER.info("Executing method: i_connect_to_the_api_at from commonsteps.java");
        api.set_a_REST_GET_response(apiURL, apiParams);
    }

    /**
     * The GET response header status will be 200
     *
     * @throws Throwable the throwable
     */
    @Then("^The GET response header status will be 200$")
    public void the_GET_response_header_status_will_be_200()throws Throwable
    {
        ClientResponse GET_REST_response=api.get_REST_GET_Response();
        int status =GET_REST_response.getStatus();
        System.out.println("test: the_GET_response_header_status_will_be_200 ");
        System.out.println("actual: " + status);
        System.out.println("expected: " + 200);
        Assert.assertEquals("The page status is not 200!", 200, status);
        /************* Need to add if there is a 401, do another get token ******************/
        if (GET_REST_response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + GET_REST_response.getStatus());
        }

    }

    /**
     * The length of the GET response is
     *
     * @param responseLength - the expected length of the response
     *
     * @throws Throwable the throwable
     */
    @Then("^The length of the GET response is \"([^\"]*)\"$")
    public void the_length_of_the_GET_response_is(int responseLength)throws Throwable
    {
        ClientResponse GET_REST_response=api.get_REST_GET_Response();
        System.out.println("test: length_of_JSON_is_as_expected");
        int lengthOfJSON = GET_REST_response.getLength();
        System.out.println("actual: " + lengthOfJSON);
        System.out.println("expected: " + responseLength);
        Assert.assertEquals("The length is not what is expected", responseLength, lengthOfJSON);
    }

    /**
     * The GET response will be the same as in the file at
     *
     * @param filePath - the expected length of the response
     *
     * @throws Throwable the throwable
     */
    @Then("^The GET response will be the same as in the file at \"([^\"]*)\"$")
    public void the_GET_response_will_be_the_same_as_in_the_file_at(String filePath)throws Throwable
    {
        ClientResponse GET_REST_response=api.get_REST_GET_Response();
        String output = GET_REST_response.getEntity(String.class);
        System.out.println("test: Api_response_is_the_same_as_in_file");
        System.out.println("actual: " + output);
        try {
            File file = new File(filePath);
            String expectedJSON = FileUtils.readFileToString(file, "UTF-8");
            System.out.println("expected" + expectedJSON);
            Assert.assertEquals("The JSONs do not match", expectedJSON, output);
        } catch (IOException e) {
            System.err.println("Caught IOException: " + e.getMessage());
        }

    }

}



