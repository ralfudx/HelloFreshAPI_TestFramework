package hellofresh;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.containsInAnyOrder;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import org.json.JSONException;
import org.json.JSONObject;


public class APITest {
	
	/**
	 * This test gets all countries and validates that US, DE and GB were returned in the response
	 **/
	//@Test
	public void getAllCountries() {
		given()
			.get("http://services.groupkt.com/country/get/all").
		then()
			.statusCode(200)
			.body("RestResponse.result.name", hasItems("United States of America", "Germany", "United Kingdom of Great Britain and Northern Ireland"))
			.body("RestResponse.result.alpha2_code", hasItems("US", "DE", "GB"));
			
	}
	
	/**
	 * This test gets US country and validates the response body
	 **/
	//@Test
	public void getUSCountryResp() {
		given()
			.get("http://services.groupkt.com/country/get/iso2code/US").
		then()
		 	.statusCode(200)
			.body("RestResponse.result.name", equalTo("United States of America"))
			.body("RestResponse.result.alpha2_code", equalTo("US"));
	}
	
	/**
	 * This test gets DE country and validates the response body
	 **/
	//@Test
	public void getDECountryResp() {
		given()
			.get("http://services.groupkt.com/country/get/iso2code/DE").
		then()
		 	.statusCode(200)
			.body("RestResponse.result.name", equalTo("Germany"))
			.body("RestResponse.result.alpha2_code", equalTo("DE"));
	}
	
	/**
	 * This test gets GB country and validates the response body
	 **/
	//@Test
	public void getGBCountryResp() {
		given()
			.get("http://services.groupkt.com/country/get/iso2code/GB").
		then()
		 	.statusCode(200)
			.body("RestResponse.result.name", equalTo("United Kingdom of Great Britain and Northern Ireland"))
			.body("RestResponse.result.alpha2_code", equalTo("GB"));
	}
	
	/**
	 * This test gets an Inexistent country and validates the response 
	 **/
	//@Test
	public void getInvalidCountry() {
		given()
			.get("http://services.groupkt.com/country/get/iso2code/ZK").
		then()
		 	.statusCode(200)
			.body("RestResponse.messages", containsInAnyOrder("No matching country found for requested code [ZK]."))
		 	.log().all();
	}
	
	/**
	 * This test adds a new country to the list
	 *  -username and password should be added
	 *  -apiURL and apiBody should be edited
	 **/
	@Test
	public void addNewCountry() throws JSONException, InterruptedException {
		String apiURL = "http://services.groupkt.com/country/";
		String apiBody = "{\"name\": \"Test Country\",\"alpha2_code\": \"TC\",\"alpha3_code\": \"TCY\"}";
		String username = "{username}";
		String password = "{password}";
		RequestSpecBuilder builder = new RequestSpecBuilder();
		builder.setBody(apiBody);
		builder.setContentType("application/json; charset=UTF-8");
		RequestSpecification requestSpec = builder.build();
		
		Response response = 
		(Response) given()
			.auth().preemptive().basic(username, password)
			.spec(requestSpec).
		when()
			.post(apiURL).
		then()
			.statusCode(201);
		
		JSONObject JSONResponseBody = new JSONObject(response.body().asString());
		String result = JSONResponseBody.getString("name");
		Assert.assertEquals(result, "Test Country");
		
	}
}
