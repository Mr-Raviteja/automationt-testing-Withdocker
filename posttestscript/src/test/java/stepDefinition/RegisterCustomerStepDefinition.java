package stepDefinition;

import java.util.Map;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import testing.Driver;

public class RegisterCustomerStepDefinition {

	private Driver driver = new Driver();
	
	@Given("^I create a new request with (.*) service$")
    public void createNewRequest(String serviceName) {
    	driver.createURI(serviceName);
    }

    @Given("^I add the (.*) endpoint to the service$")
    public void addEndpoint(String endpoint) {
    	driver.addEndpoint(endpoint);
    }

    @When("^I send the (.*) request to the service$")
    public void sendRequest(String method) throws Throwable {
        driver.sendRequest(method);
    }
    
    @When("^I pass (.*) as content type$")
    public void setContentType(String contentType) throws Throwable {
        driver.setContentType(contentType);
    }

    @Then("^I get the (\\d+) response code$")
    public void responseCodeValidation(int responseCode) throws Throwable {
    	driver.expectedResponse(responseCode);
    }

    @Then("^I assert the following values in the response body$")
    public void responseAttributeValidation(Map<String, String> validateData) throws Throwable {
        driver.validateResponse(validateData);
    }
    
    @And("^I send the following values in the request body$")
    public void setRequestBody(Map<String, String> requestBody) throws Throwable {
        driver.setRequestBody(requestBody);
    }

}
