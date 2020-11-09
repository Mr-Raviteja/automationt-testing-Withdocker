package testing;

import static com.jayway.restassured.RestAssured.given;

import org.hamcrest.Matcher;
import org.json.simple.JSONObject;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.matcher.ResponseAwareMatcher;
import com.jayway.restassured.response.ResponseBody;
import com.jayway.restassured.response.ValidatableResponse;
import com.jayway.restassured.specification.RequestSpecification;

import static org.hamcrest.Matchers.equalTo;

import java.util.Map;

public class Driver{
	private String uri;
	private ValidatableResponse response;
	private String contentType;
	private String body;
	private static final String GET = "GET";
	private static final String POST = "POST";

	public void sendRequest(String reqestMethod) {
		 if(GET.equalsIgnoreCase(reqestMethod)) {
			 response = given().when().get(uri).then();
		 } else if(POST.equalsIgnoreCase(reqestMethod)) {
			 RequestSpecification request = RestAssured.given();
				request.header("Content-Type", this.contentType);
				request.body(this.body);
		 		
			 response =	request.post(uri).then();
		 }
	}
	
	public void createURI(String serviceName) {
		this.uri = serviceName;
	}

	public void addEndpoint(String endpoint) {
		this.uri = uri.concat(endpoint);
	}

	public void expectedResponse(int responseCode) {
		response.statusCode(responseCode);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void validateResponse(Map<String, String> validateData) {
		for(Map.Entry<String, String> entrySet: validateData.entrySet()) {
			String key = entrySet.getKey();
			final String value = entrySet.getValue();
			response.body(key, new ResponseAwareMatcher() {
	             public Matcher matcher(ResponseBody response) throws Exception {
					return equalTo(value);
				}
	        });
		}
	}
	
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@SuppressWarnings("unchecked")
	public void setRequestBody(Map<String, String> requestBody) {
		JSONObject requestParams = new JSONObject();
		
		for(Map.Entry<String, String> entrySet: requestBody.entrySet()) {
			requestParams.put(entrySet.getKey(), entrySet.getValue()); 
		}
		this.body = requestParams.toJSONString();
	}
}
