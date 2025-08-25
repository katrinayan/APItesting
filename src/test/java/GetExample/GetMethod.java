package GetExample;

import static io.restassured.RestAssured.*;
import io.restassured.RestAssured.*;

import org.hamcrest.Matchers;
import org.hamcrest.Matcher;
import org.testng.annotations.Test;

import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;


public class GetMethod {
	
//	@Test
//	public void getUsers() {
//		given().baseUri("https://reqres.in/api/users?page=2")
//		.get()
//		.then()
//			.log().all()
//			.statusCode(200)
//			.statusLine("HTTP/1.1 200 OK")
//			.body("per_page",Matchers.equalTo(6))
//			.body("data[1].email",Matchers.containsString("janet.weaver@reqres.in"));
//	}
	
	@Test (enabled=false)
	public void getSingleUser_VerifyStatusAndEmail() {
		RequestSpecification req = RestAssured.given();
        
		req.baseUri("https://reqres.in/api/users/2");
                
        //Response res = req.get();
		Response res = req.request(Method.GET, "");
        
        String resString = res.asPrettyString();
        System.out.println("Response is: "+resString);
        
        ValidatableResponse resPonse=res.then();
        
		resPonse.statusCode(200);
		
		int statusCode = res.getStatusCode();
		System.out.println("Status code is: "+statusCode);
				
		String statusLine = res.getStatusLine();
		System.out.println("Status line is: "+statusLine);
			
		Headers allHeaders = res.headers(); 
		for(Header header : allHeaders) { 
			   System.out.println("Key: " + header.getName() + " Value: " + header.getValue()); 
			 } 
				
		String contentType = res.header("Content-Type"); 
		System.out.println("Content-Type value: " + contentType); 
		
		// Access header with a given name. 
		String serverType = res.header("Server"); 
		System.out.println("Server value: " + serverType);
		
		// Access header with a given name. Header = Content-Encoding 
		String acceptLanguage = res.header("Content-Encoding"); 
		System.out.println("Content-Encoding: " + acceptLanguage); 
		
		ResponseBody body = res.getBody();
		System.out.println("Response Body is: " + body.asString());
		String bodyAsString = body.asString();
		Assert.assertEquals(bodyAsString.contains("Caddy") /*actual value*/, true /*expected Value*/,"Response body do not contains Caddy");
		
		Assert.assertEquals(statusCode /*actual value*/, 200 /*expected value*/, "Incorrect status code returned");
		Assert.assertEquals(statusLine /*actual value*/, "HTTP/1.1 200 OK" /*expected value*/, "Incorrect status code returned");
		Assert.assertEquals(contentType /*actual value*/, "application/json; charset=utf-8" /*expected value*/, "Incorrect content type returned");
		
		JsonPath jsonPathEvaluator = res.jsonPath();
		String email = jsonPathEvaluator.get("email");

		System.out.println("Email received from Response " + email);
		Assert.assertEquals(email, "janet.weaver@reqres.in", "Incorrect email received in the Response");
		
		resPonse.body("data.id",Matchers.equalTo(2));
		resPonse.body("data.email",Matchers.containsString("janet.weaver@reqres.in"));
		resPonse.body("data.first_name",Matchers.containsString("Janet"));
		resPonse.body("data.last_name",Matchers.containsString("Weaver"));
		resPonse.body("data.avatar",Matchers.containsString("https://reqres.in/img/faces/2-image.jpg"));
		
		resPonse.body("support.url",Matchers.containsString("https://contentcaddy.io?utm_source=reqres&utm_medium=json&utm_campaign=referral"));
        resPonse.body("support.text",Matchers.containsString("Tired of writing endless social media content? Let Content Caddy generate it for you."));        
	}
	
	@Test (enabled=false)
	public void verifyProductRequest() {
		RequestSpecification req = RestAssured.given();
		
		req.baseUri("https://fakestoreapi.com/products/20");
				
		Response res = req.request(Method.GET, "");
		
		String resString = res.asPrettyString();
		System.out.println("Response is: "+resString);
		
		ValidatableResponse resPonse=res.then();
		
		resPonse.statusCode(200);
		
		int statusCode = res.getStatusCode();
		System.out.println("Status code is: "+statusCode);
				
		String statusLine = res.getStatusLine();
		System.out.println("Status line is: "+statusLine);
			
//		Headers allHeaders = res.headers(); 
//		for(Header header : allHeaders) { 
//			   System.out.println("Key: " + header.getName() + " Value: " + header.getValue()); 
//			 } 
				
		String contentType = res.header("Content-Type"); 
		System.out.println("Content-Type Value: " + contentType); 
		
		
		ResponseBody body = res.getBody();
		System.out.println("Response Body is: " + body.asString());
		String bodyAsString = body.asString();
		Assert.assertEquals(bodyAsString.contains("5%Spandex") /*actual value*/, true /*expected Value*/,"Response body do not contains 5%Spandex");
		
		Assert.assertEquals(statusCode /*actual value*/, 200 /*expected value*/, "Incorrect status code returned");
		Assert.assertEquals(statusLine /*actual value*/, "HTTP/1.1 200 OK" /*expected value*/, "Incorrect status code returned");
		Assert.assertEquals(contentType /*actual value*/, "application/json; charset=utf-8" /*expected value*/, "Incorrect content type returned");
		
		JsonPath jsonPathEvaluator = res.jsonPath();
		String title = jsonPathEvaluator.get("title");

		System.out.println("Title received from Response " + title);
		Assert.assertEquals(title, "DANVOUY Womens T Shirt Casual Cotton Short", "Incorrect title received in the Response");
	}
	
	@Test (enabled=true)
	public void generateAndVerifyAuthToken() {
		
		String jsonString="{\"email\": \"eve.holt@reqres.in\",\"password\": \"pistol\"}";
		
		RequestSpecification req = RestAssured.given();
		req.contentType(ContentType.JSON);
		req.body(jsonString);
		req.baseUri("https://reqres.in/api/register");
		Response res = req.post();
		
		System.out.println(res.asPrettyString());
		
		ValidatableResponse vRes=res.then();
		
		vRes.statusCode(200);
		vRes.body("id", Matchers.equalTo(4));
		vRes.body("token", Matchers.notNullValue());
		
		vRes.body("token.length()", Matchers.equalTo(17));
		vRes.body("token", Matchers.matchesRegex("[a-z0-9]+"));

	}

}
