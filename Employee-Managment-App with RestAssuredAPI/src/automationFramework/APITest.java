package automationFramework;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;

public class APITest {
	public static final String baseUri="https://staff-scanner.vercel.app";
	Header token=new Header("Authorization","Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjY1NDhkZmU3MDdhZjJkMWU5NGZjZjk3NSIsImlhdCI6MTcwMTQ0ODg5NH0.bdKhcx8FBwVrBlufealGNL6-SO3r2nj4c6LrfW7lhfg");
    String jsonBody = "{ \"email\": \"admin@admin.com\", \"password\": \"123\" }";
    String id="";
    HashMap <String,Object> map=new HashMap();

	@Test (priority=1)
	public void loginApiCompany() {
        // Make the POST request
          Response res=  given()
            .baseUri(baseUri)
            .contentType(ContentType.JSON)
            .body(jsonBody)
        .when()
            .post("/auth/signinCompany")
        .then().extract().response();
          Assert.assertEquals(res.statusCode(),200);
    }
	
	@Test(priority=2)
	public void getEmployes() {
		Response res=given().baseUri(baseUri)
		.header(token).param("perPage",7)
		.when().get("/company/getEmployees")
		.then().extract().response();
		map.put("employeeId","");

		map.replace("employeeId",res.jsonPath().getString("employees[1].id"));
		Assert.assertTrue(res.jsonPath().getString("employees.userName").contains("ayham"));
		
	}
	
	@Test(priority=3)
	public void CheckIn() {
		Response res=given().baseUri(baseUri)
		.contentType(ContentType.JSON)
		.header(token).body(map)
		.when().post("/company/checkInEmployee")
		.then().extract().response();
		System.out.println(res.asString());
		Assert.assertTrue(res.jsonPath().getString("message").contains("success"));
	}

	@Test(priority=4)
	public void CheckOut() {
		Response res=given().baseUri(baseUri)
		.contentType(ContentType.JSON)
		.header(token).body(map)
		.when().post("/company/checkOutEmployee")
		.then().extract().response();
		System.out.println(res.asString());
		Assert.assertTrue(res.jsonPath().getString("message").contains("success"));
	}
}
