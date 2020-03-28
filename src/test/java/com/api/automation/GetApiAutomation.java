package com.api.automation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.authentication.FormAuthConfig;
import io.restassured.authentication.PreemptiveBasicAuthScheme;
import io.restassured.http.Method;
import io.restassured.internal.AuthenticationSpecificationImpl;
import io.restassured.response.Response;
import io.restassured.specification.AuthenticationSpecification;
import io.restassured.specification.RequestSpecification;

public class GetApiAutomation {

	@Test(enabled = true)
	public void getTransactionDetail() {
		//Specify the base uri
		RestAssured.baseURI = "https://acssim.payubiz.in:3000";

		//Create request object
        HashMap<String, String> hm = new HashMap<String, String>();
        List<String> al = new ArrayList<String>();
        hm.put("Amount", "10");
        hm.put("PG","CC");
        hm.put("Ibibo_Code","CC");
        hm.put("flow","NoCostEmi");
        al.add("EMIA3");
        al.add("EMIA6");
        hm.put("Subvention_Eligibility", "EMIA3,EMIA6");
        
		RequestSpecification httpRequest = RestAssured.given();
		//***************************** Using query param ************************************
		httpRequest.queryParams("Amount", "10", "PG","CC","Ibibo_Code","CC","flow","NoCostEmi","tenure","EMIA3");
		//httpRequest.queryParam("Subvention_Eligibility", "EMIA3","EMIA6");
	    httpRequest.queryParam("Subvention_Eligibility", al);
		 
		//httpRequest.queryParams(hm);
		
		//********************************* Using Param **************************************
		 
		//httpRequest.params("Amount", "10", "PG","CC","Ibibo_Code","CC","flow","NoCostEmi","tenure","EMIA3");
		//httpRequest.param("Subvention_Eligibility", "EMIA3","EMIA6");
	   // httpRequest.param("Subvention_Eligibility", al);
		 
		//httpRequest.params(hm);
		
		//Response response = httpRequest.request(Method.GET, "/automation/getDetails?Amount=10&PG=CC&Ibibo_Code=CC&flow=StoredCard&tenure = EMIA3&Subvention_Eligibility=EMIA3,EMIA6");
	   
		//******************************** No param passed **************************************
		//Response response =  httpRequest.get("/automation/getResponse");
	    httpRequest.log().all();
		Response response =  httpRequest.get("/automation/getDetails");
		response.then().log().all();
		String res = response.getBody().asString();
		System.out.println(" \n\n\n\n\n *************   Response  *******************\n" +res);

		int code = response.getStatusCode();

		System.out.println("******************* status ******************* \n " +code);

		String stline = response.getStatusLine();

		System.out.println("********************* status line *************** \n " +stline);

//		//Verification of content
//
//		int amt = response.jsonPath().get("Amount");
//
//		System.out.println("******************** Amount ***************** \n "  + amt);
//		//Verification of header
//
//		String con = response.header("Content-type");
//
//		System.out.println("********************** Content-type ********************* \n " + con);
//		String tag = response.header("Etag");
//
//		System.out.println("********************** Etag ********************* \n " + tag);
//
//		//printing all header
//
//		Headers allHeader = response.headers();
//
//		for(Header header : allHeader){
//
//			System.out.println("Key -- " + header.getName() + "value -- " +header.getValue());
//		}
	}
	
	@Test
	public void getRequestWithPathParam() {
		//Specify the base uri
		RestAssured.baseURI = "http://dummy.restapiexample.com/api/v1";
		int empid = 23042;
		//Create request object

		RequestSpecification httpRequest = RestAssured.given();
		httpRequest.pathParam("employeeId", empid);
		
		//Similarly we can add in middle also with multiple path param
		httpRequest.log().all();
		Response response = httpRequest.get("/employee/{employeeId}");
		//Response response = httpRequest.request(Method.GET, "/employee/" + empid);
		response.then().log().all();
		String res = response.getBody().asString();
		System.out.println(" \n\n\n\n\n\n\n\n\n  *************   Response  *******************\n" +res);

		int code = response.getStatusCode();

		System.out.println("******************* status ******************* \n " +code);

		String stline = response.getStatusLine();

		System.out.println("********************* status line *************** \n " +stline);
	}
	
	@Test
	public void getApiWithAuthenticationCheck() {
		//Specify the base uri
		RestAssured.baseURI = "http://restapi.demoqa.com/authentication/CheckForAuthentication";
		RequestSpecification httpRequest = RestAssured.given();
		// Provide authentication 
// ******************************* Method 1 *********************************
	//	httpRequest.auth().preemptive().basic("ToolsQA", "TestPassword");
		//PreemptiveBasicAuthScheme auth =  new PreemptiveBasicAuthScheme();
		//auth.setUserName("ToolsQA");
		//auth.setPassword("TestPassword");

		//RestAssured.authentication = auth;
		
		//Create request object

	
// ****************************** Method 2 *****************************************
		
		AuthenticationSpecification auth = httpRequest.auth();
		auth.basic("ToolsQA", "TestPassword");
	
// ****************************** Method 3 *****************************************
		
	//	 httpRequest.auth().form("ToolsQA", "TestPassword",new FormAuthConfig("/j_spring_security_check", "ToolsQA", "TestPassword"));
	//	httpRequest.auth().form("John", "Doe", formAuthConfig().withAutoDetectionOfCsrf()));
	//	httpRequest.auth().oauth2(accessToken)
    //
		httpRequest.log().all();
		Response response = httpRequest.request(Method.GET, "/");
		response.then().log().all();
		String res = response.getBody().asString();
		System.out.println(" *************   Response  *******************\n" +res);

		int code = response.getStatusCode();
		System.out.println("******************* status ******************* \n " +code);

		String stline = response.getStatusLine();

		System.out.println("********************* status line *************** \n " +stline);


	}
}
