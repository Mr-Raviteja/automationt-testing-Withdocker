Feature: Testing the API's

Scenario: Get Customer
Given I create a new request with http://testingautomation.us-e2.cloudhub.io/ service
And I add the test endpoint to the service
And I send the GET request to the service
Then I get the 200 response code
Then I assert the following values in the response body
| givenName | vikas |


Scenario: Register Customer
Given I create a new request with http://posttestscript.us-e2.cloudhub.io/api/customers/registrations/ service
And I add the registerUser endpoint to the service
And  I pass application/json as content type
And I send the following values in the request body
| firstName | Vikas |
| lastName | Puri |
| phoneNumber | 999 |
| emailId | vikas.puri@facebook.com |
| password | xxx |
And I send the POST request to the service
Then I get the 200 response code
Then I assert the following values in the response body
| message | User registered successfully successfully. |