package com.api.automation;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.QueryableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import io.restassured.specification.SpecificationQuerier;

import static org.hamcrest.Matchers.*;
import static io.restassured.module.jsv.JsonSchemaValidator.*;


public class PostApiAutomation {
	
	ResponseSpecification responseSpec;
	RequestSpecification requestSpec;
	@BeforeMethod
	public void setup() {
	ResponseSpecBuilder builder = new ResponseSpecBuilder();
	builder.expectStatusCode(200);
	builder.expectStatusLine("HTTP/1.1 200 OK");
	builder.expectResponseTime(lessThan(6L), TimeUnit.SECONDS);
	responseSpec = builder.build();
	
	
	RequestSpecBuilder builderRequest = new RequestSpecBuilder();
	builderRequest.addHeader("Content-Type","application/json");
	requestSpec = builderRequest.build();
	}
	@Test
	public void postRequestWithJsonRequest() {
		//Specify the base uri
		//RestAssured.baseURI = "https://acssim.payubiz.in:3000";

		//Create request object

		RequestSpecification httpRequest = RestAssured.given();
		
		String req = "{\"transactionParams\":{\"payuId\":\"900000000000384\",\"mihpayid\":\"a6cbff28959eb1c73b9b3bf1a8e51f70\",\"amount\":\"10.00\",\"firstName\":\"Payu-Admin\",\"lastName\":\"\",\"productInfo\":\"Product Info\",\"merchantTxnId\":\"6b231105e00709bd9d7b\",\"merchantKey\":\"BCtv0f\",\"merchantName\":\"Redbus\",\"deviceInfo\":\"0_|_0\",\"addedon\":\"2019-11-16 12:42:21\"},\"pgParams\":{\"ret_url\":\"https:\\/\\/pp33secure.payu.in\\/83cdeab1afdb9df2f7cd457fab2a3c710b3f4764fe26b77208ffe9fad5fffd8a\\/CommonPgResponseHandler\",\"merchant_mapping\":\"{\\\"burl\\\":\\\"RU\\\"}\",\"s2s_enabled\":\"1\",\"gatewayFee\":\"2.00\",\"post_vars\":\"{\\\"MD\\\":\\\"#md#\\\",\\\"PID\\\":\\\"#payee_id#\\\",\\\"PRN\\\":\\\"#txnid#\\\",\\\"ITC\\\":\\\"#id#\\\",\\\"CRN\\\":\\\"#currency#\\\",\\\"RU\\\":\\\"#ret_url#\\\",\\\"CG\\\":\\\"#cg#\\\",\\\"AMT\\\":\\\"#amount#\\\", \\\"RESPONSE\\\":\\\"#response#\\\"}\",\"CRN\":\"INR\",\"MD\":\"P\",\"CG\":\"Y\",\"RESPONSE\":\"AUTO\",\"RU\":\"\\/AXIS_NB_RESPONSE.php\",\"ver_url\":\"\\/CommonPgResponseHandler\",\"post_uri\":\"https:\\/\\/acssim.payubiz.in:3000\\/commonpg\\/axishome\",\"title\":\"Welcome to Axis\",\"payu_aggregator\":\"1\",\"accpet_manual_request\":\"1\",\"payee_id\":\"000049217410\",\"new_post_uri\":\"https:\\/\\/retail.axisbank.co.in\\/wps\\/portal\\/rBanking\\/AxisSMRetailLogin\\/axissmretailpage?AuthenticationFG.MENU_ID=CIMSHP&AuthenticationFG.CALL_MODE=2&CATEGORY_ID=NED\",\"new_ret_url\":\"\\/AXIS_NB_RESPONSE.php\",\"s2s_direct\":\"payu\",\"blazePayBankCode\":\"CID002\"},\"bankcode\":\"CID002\"}";
		
		JSONObject requestObj = new JSONObject(req);
		//httpRequest.header("Content-Type","application/json");
		
		/////////////////////////////////////////////////  request specification reuse /////////////////////////////
		httpRequest.spec(requestSpec);
		httpRequest.body(requestObj.toString());
		
		///////////////////////////////////////////////   querying request specification //////////////////////////////
		
		
		QueryableRequestSpecification queryable = SpecificationQuerier.query(httpRequest);
		String headerValue = queryable.getHeaders().getValue("header");
		System.out.println( " ********************* header ****************************    " +headerValue);
		String param = queryable.getContentType();
		System.out.println("  ******************************** content type *************************    " + param);
		String body = queryable.getBody();
		System.out.println("  ******************************** body *************************    " + body);
		//Response response = httpRequest.request(Method.POST, "https://acssim.payubiz.in:3000/commonpg/verifytransaction");
		
		
		httpRequest.log().all();
		Response response = httpRequest.post("https://acssimuat.payubiz.in:3000/commonpg/getHtmlParam");
		response.then().log().all();
		
		
		/////////////////////////////////////  getting header ////////////////////////////////////////////
		Iterator<Header> itr = response.headers().iterator();
		while(itr.hasNext()) {
			Header h = itr.next();
			System.out.println("Header key   "+ h.getName() +"      Header value   " + h.getValue());
			
		}
		System.out.println("Header key   "+ response.getHeader("Connection"));
		response.then().assertThat().header("X-Powered-By","Express");
		response.then().assertThat().headers("X-Powered-By","Express","Content-Type","application/json; charset=utf-8","Content-Length", "383");
		
		
		////////////////////////////////////  getting cookies //////////////////////////////////////////////
		
		Map<String, String> hm = new HashMap<String, String>();
		hm = response.cookies();
		System.out.println("Cookies :::::::::::::::::::::::::   " + hm);
		
		///////////////////////////////////////////  Printing response ////////////////////////////////////////
		
		//String res = response.getBody().asString();
		//System.out.println("\n\n\n\n\n\n\n\n ************   Response  *******************\n" +response.body().prettyPrint());
		//System.out.println("\n\n\n\n\n\n\n\n ************   Response  *******************\n" +response.print());
		//System.out.println("\n\n\n\n\n\n\n\n ************   Response  *******************\n" +response.peek().asString());
		
		
		///////////////////////////////////   get body ////////////////////////////////////////
		//String res = response.getBody().asString();
		System.out.println("\n\n\n\n\n\n\n\n ************   Response  *******************\n" +response.body().prettyPrint());

		
		////////////////////////////////       get status code //////////////////////////////
		//int code = response.getStatusCode();
		//int code = response.statusCode();
		//response.then().assertThat().statusCode(200);
		//System.out.println("******************* status ******************* \n " +code);

		////////////////////////////////////       get status line /////////////////////////
		//String stline = response.getStatusLine();
		//String stline = response.statusLine();
		//System.out.println("********************* status line *************** \n " +stline);
		//response.then().assertThat().statusLine("HTTP/1.1 200 OK");
        ////////////////////////   get session id /////////////////////////////////
		//System.out.println("******************* sessionId ******************* \n " +response.getSessionId());
		System.out.println("******************* sessionId ******************* \n " +response.sessionId());
		
		
		//////////////////////////   get response time ///////////////////////////////////////
	  //  System.out.println("******************* time ******************* \n " +response.time());
		//  System.out.println("*******************  get  time  ******************* \n " +response.getTime());
	//	System.out.println("******************* time in  ******************* \n " +response.timeIn(TimeUnit.SECONDS));
		//  System.out.println("******************* get time in ******************* \n " +response.getTimeIn(TimeUnit.SECONDS));
		//  response.then().assertThat().time(lessThan(5L), TimeUnit.SECONDS);
		  
		  ///////////////////////////////  getting contentType   ///////////////////////////////////
		  System.out.println("******************* Content Type ******************* \n " +response.contentType());
		  System.out.println("*******************  Content Type  ******************* \n " +response.getContentType()); 
		  response.then().assertThat().contentType(ContentType.JSON);
		  
		  /////////////////////////////////     getting json content   //////////////////////////////////////////
		  
		  System.out.println("*****************  RU ===================================       " + response.body().jsonPath().getString("postParams.RU"));
		  
	      ///////////////////////////////   response specification reuse ///////////////////////////////////////
		  
		  response.then().spec(responseSpec);
	}
	
	@Test
	public void ApiWithBodyTextPlain() {
		//Specify the base uri
		//RestAssured.baseURI = "https://acssim.payubiz.in:3000";

		//Create request object

		RequestSpecification httpRequest = RestAssured.given();
		
		String req = "<id></id> <password></password> <action>1</action> <amt>2.00</amt> <currencycode>356</currencycode> <trackid>900000000000361</trackid> <merchantResponseUrl>https://pp33secure.payu.in/bf3046742d13360b595242d987ba8f7fb513ea2f4cfff0e89760caa3e0daa4e8/HdfcRupay_response.php</merchantResponseUrl> <merchantErrorUrl>https://pp33secure.payu.in/bf3046742d13360b595242d987ba8f7fb513ea2f4cfff0e89760caa3e0daa4e8/HdfcRupay_response.php</merchantErrorUrl> <card>6MAESTROMAESTRO2</card> <expmonth>_expmonth_</expmonth> <expyear>_expyear_</expyear> <cvv2>_cvv_</cvv2> <member>Test User</member> <udf1>Product Info</udf1> <udf2>_emailid_</udf2> <udf3>1234567890</udf3> <udf4></udf4> <udf5>e9292b91953f458af0ed</udf5>";
		httpRequest.header("Content-Type","text/plain");
		httpRequest.body(req);
		//Response response = httpRequest.request(Method.POST, "https://acssim.payubiz.in:3000/commonpg/verifytransaction");
		
		
		/////////////////////////////////////////////////////    logging of request   //////////////////////////////
		//httpRequest.log().all();
		httpRequest.log().body(true);
		httpRequest.log().headers();
		System.out.println("  everything   ");
		httpRequest.log().everything(true);
		//httpRequest.then().log().ifValidationFails()
		Response response = httpRequest.post("https://acssimuat.payubiz.in:3000/pg/hdfcr/enrollment");
		
		
/////////////////////////////   Response logging /////////////////////////////////
		response.then().log().body(true);
		
		System.out.println(" Ecerything of response");
		response.then().log().everything(true);
		//response.then().log().ifValidationFails(logDetail)
		//response.then().log().all(true);
		//String res = response.getBody().asString();
		System.out.println("\n\n\n\n\n\n\n\n ************   Response  *******************\n" +response.prettyPrint());
		System.out.println("\n\n\n\n\n\n\n\n ************   udf1  *******************\n" +response.getBody().xmlPath().getString("result.udf1"));
		
		
		int code = response.getStatusCode();

		System.out.println("******************* status ******************* \n " +code);

		String stline = response.getStatusLine();

		System.out.println("********************* status line *************** \n " +stline);
     }
	

	@Test
	public void ApiWithBodyApplicationXml() {
		//Specify the base uri
		//RestAssured.baseURI = "https://acssim.payubiz.in:3000";

		//Create request object

		RequestSpecification httpRequest = RestAssured.given();
		
		String req = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ThreeDSecure><Message id=\"7111234567000091\"><VEReq><version>1.0.2</version><pan>5MASTERCARDMAST6</pan><Merchant><acqBIN>512345</acqBIN><merID>70001054</merID><password>payu123</password></Merchant><Browser><deviceCategory>3</deviceCategory><accept/><userAgent/></Browser><Extension critical=\"false\" id=\"visa.3ds.india_ivr\"><npc356chphoneidformat>D</npc356chphoneidformat><npc356chphoneid>9999999999</npc356chphoneid><npc356pareqchannel>DIRECT</npc356pareqchannel><npc356shopchannel>IVR</npc356shopchannel><npc356availauthchannel>IVR</npc356availauthchannel></Extension></VEReq></Message></ThreeDSecure>";
		httpRequest.header("Content-Type","application/xml");
		httpRequest.body(req);
		//Response response = httpRequest.request(Method.POST, "https://acssim.payubiz.in:3000/commonpg/verifytransaction");
		httpRequest.log().all();
		Response response = httpRequest.post("https://acssim.payubiz.in:3000/pg/icici/enrollment");
		response.then().log().all();
		//String res = response.getBody().asString();
		System.out.println("\n\n\n\n\n\n\n\n ************   Response  *******************\n" +response.prettyPrint());
		System.out.println("\n\n\n\n\n\n\n\n ************   veres version  *******************\n" +response.body().xmlPath().getString("ThreeDSecure.Message.VERes.version"));

		int code = response.getStatusCode();

		System.out.println("******************* status ******************* \n " +code);

		String stline = response.getStatusLine();

		System.out.println("********************* status line *************** \n " +stline);
     }
	
	@Test
	public void ApiWithBodyApplicationUrlEncoded() {
		
		RequestSpecification httpRequest = RestAssured.given();
		httpRequest.contentType(ContentType.URLENC.withCharset("UTF-8"));
		httpRequest.param("PaReq", "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ThreeDSecure><Message id=\"7111234567000091\"><PAReq><version>1.0.2</version><Merchant><acqBIN>512345</acqBIN><merID>payu</merID><name>Redbus</name><country>356</country><url>www.redbus.in</url></Merchant><Purchase><xid>ODA1Mzk1MjgxNjM2NjgxMDYyOTI=</xid><date>20191110 16:02:26</date><amount>1000</amount><purchAmount>1000</purchAmount><currency>356</currency><exponent>2</exponent><desc>This is a transaction of Rs 100</desc></Purchase><CH><acctID>201904221608085863uB9pZ7kT</acctID><expiry>202005</expiry></CH><Extension critical=\"false\" id=\"visa.3ds.india_ivr\"><npc356authuserdata><attribute encrypted=\"FALSE\" name=\"OTP2\" value=\"987654\" status=\"Y\"/></npc356authuserdata><npc356authitpdata><attribute authenticated=\"\" identifier=\"\"/></npc356authitpdata></Extension></PAReq></Message></ThreeDSecure>");
		httpRequest.param("MD", "payU123");
		httpRequest.param("TermUrl", "https://pp34secure.payu.in/OnusOtpResponse.php");
		httpRequest.param("url", "https://www.3dsecure.icicibank.com/ACSWeb/EnrollWeb/ICICIBank/server/AccessControlServer?idct=8112.V.I");

		httpRequest.log().all();
		Response response = httpRequest.post("https://acssimuat.payubiz.in:3000/pg/icici/authentication");
		response.then().log().all();
		String res = response.getBody().asString();
		System.out.println("\n\n\n\n\n\n\n\n ************   Response  *******************\n" +res);

		int code = response.getStatusCode();

		System.out.println("******************* status ******************* \n " +code);

		String stline = response.getStatusLine();

		System.out.println("********************* status line *************** \n " +stline);
     }
	
	@Test
	public void ApiWithBodyApplicationUrlEncodedWithFormParam() {
		
		RequestSpecification httpRequest = RestAssured.given();
		httpRequest.contentType(ContentType.URLENC.withCharset("UTF-8"));
		httpRequest.param("vpc_Version", "1");
		httpRequest.param("vpc_Command", "pay");
		httpRequest.param("vpc_AccessCode", "6A62786A");
		httpRequest.param("vpc_Merchant", "PYINDPAYUMIS");
		httpRequest.param("vpc_Currency", "INR");
		httpRequest.param("vpc_SecureHash", "865A1754DADEDCCE627965B3FB9B5F5F");
		httpRequest.param("vpc_SecureHashType", "SHA256");
		httpRequest.param("vpc_ReturnAuthResponseData", "Y");
		httpRequest.param("vpc_VerType", "3DS");
		httpRequest.param("vpc_VerToken", "uB/8dwoZCxxIz+Ed5rHPkMzTUBg=");
		httpRequest.param("vpc_3DSECI", "05");
		httpRequest.param("vpc_3DSenrolled", "Y");
		httpRequest.param("vpc_3DSstatus", "Y");
		httpRequest.param("vpc_MerchTxnRef", "900000000000357");
		httpRequest.param("vpc_OrderInfo", "ProductInfo");
		httpRequest.param("vpc_Amount", "1000");
		httpRequest.param("vpc_CardNum", "5MASTERCARDMAST6");
		httpRequest.param("vpc_CardExp", "_expiry_");
		httpRequest.param("vpc_CardSecurityCode", "XXX");
		
		httpRequest.log().all();
		Response response = httpRequest.post("https://acssimuat.payubiz.in:3000/pg/icici/authorization");
		response.then().log().all();
		String res = response.getBody().asString();
		System.out.println("\n\n\n\n\n\n\n\n ************   Response  *******************\n" +res);

		int code = response.getStatusCode();

		System.out.println("******************* status ******************* \n " +code);

		String stline = response.getStatusLine();

		System.out.println("********************* status line *************** \n " +stline);
     }
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Test
	public void learningValidationOfJsonResponse() {
		//Specify the base uri
		//RestAssured.baseURI = "https://acssim.payubiz.in:3000";

		//Create request object

		RequestSpecification httpRequest = RestAssured.given();
		
		String req = "{\n" + 
				"\"lotto\":{\n" + 
				" \"lottoId\":5,\n" + 
				" \"winning-numbers\":[2,45,34,23,7,5,3],\n" + 
				" \"winners\":[{\n" + 
				"   \"winnerId\":23,\n" + 
				"   \"numbers\":[2,45,34,23,3,5]\n" + 
				" },{\n" + 
				"   \"winnerId\":54,\n" + 
				"   \"numbers\":[52,3,12,11,18,22],\n" + 
				"   \"price\":12.12\n" + 
				" }]\n" + 
				"}\n" + 
				"}";
		
//		String req = "{\n" + 
//				"  \"pan\": \"5129670000000005\",\n" + 
//				"  \"merCountryCode\": \"356\",\n" + 
//				"  \"purDateTime\": \"20191114 19:59:23\",\n" + 
//				"  \"purchase_amount\": \"1000\",\n" + 
//				"  \"currency\": \"356\",\n" + 
//				"  \"exponent\": \"2\",\n" + 
//				"  \"expiry\": \"2005\",\n" + 
//				"  \"transactionId\": \"9000000032310097\",\n" + 
//				"  \"issuingBank\": \"HDFC\",\n" + 
//				"  \"installments\": \"\",\n" + 
//				"  \"recur_frequency\": \"\",\n" + 
//				"  \"recur_expiry\": \"\",\n" + 
//				"  \"merName\": \"Redbus\",\n" + 
//				"  \"mpiTid\": \"2\"\n" + 
//				"}";
		
		JSONObject requestObj = new JSONObject(req);
		httpRequest.header("Content-Type","application/json");
		httpRequest.body(requestObj.toString());
		//Response response = httpRequest.request(Method.POST, "https://acssim.payubiz.in:3000/commonpg/verifytransaction");
		httpRequest.log().all();
		Response response = httpRequest.post("https://acssimuat.payubiz.in:3000/termUrl/DecoupledResponse");
		response.then().log().all();
		
		ValidatableResponse ob = response.then().root("lotto.winners");
		/////////////////////////////////////////////   Validation of response ////////////////////////////////////////////
	//	response.then().body("lotto.lottoId", equalTo(5));
	//response.then().body("lotto.winners.winnerId", hasItems(23, 54));
		ob.body("winnerId", hasItems(23, 54));
	//	response.then().body("lotto.winners[1].price", is(12.12f));
		//response.then().assertThat().body(containsString("recur_expiry"));
		
		//response.then().body("lotto.winners.findAll{it.winnerId>50}.price[0]", is(12.12f));
		ob.body("findAll{it.winnerId>50}.price[0]", is(12.12f));
		//response.then().body("lotto.winners.winnerId.collect{it.length()>0}.sum()", greaterThan(0));
		
		/////////////////////////////////////////////   Validation of schema //////////////////////////////////////////////
		
	//	response.then().assertThat().body(matchesJsonSchemaInClasspath("json-schema.json"));
		
		//////////////////////////////////////////     Deserialisation of response ////////////////////////////////////////
		//Map<String, Object> products = response.as(new TypeRef<Map<String, Object>>() {});
		
		//System.out.println(products);

	}
	
	@Test
	public void learningValidationOfXmlResponse() {
		//Specify the base uri
		//RestAssured.baseURI = "https://acssim.payubiz.in:3000";
		 RestAssured.registerParser("application/octet-stream", Parser.XML);
		//Create request object

		RequestSpecification httpRequest = RestAssured.given();
		
//		String req = "<foo xmlns:ns=\"http://localhost/\">\n" + 
//				"  <bar>sudo </bar>\n" + 
//				"  <ns:bar>make me a sandwich!</ns:bar>\n" + 
//				"</foo>";
//		String req = "<greeting>\n" + 
//				"   <firstName>Ajay</firstName>\n" + 
//				"   <lastName>Kumar</lastName>\n" + 
//				"</greeting>";
		
		String req = "<shopping>\n" + 
				"      <category type=\"groceries\">\n" + 
				"        <item>Chocolate</item>\n" + 
				"        <item>Coffee</item>\n" + 
				"      </category>\n" + 
				"      <category type=\"supplies\">\n" + 
				"        <item>Paper</item>\n" + 
				"        <item quantity=\"4\">Pens</item>\n" + 
				"      </category>\n" + 
				"      <category type=\"present\">\n" + 
				"        <item when=\"Aug 10\">Kathryn's Birthday</item>\n" + 
				"      </category>\n" + 
				"</shopping>";
		httpRequest.header("Content-Type","application/xml");
		httpRequest.body(req);
		//Response response = httpRequest.request(Method.POST, "https://acssim.payubiz.in:3000/commonpg/verifytransaction");
		httpRequest.log().all();
		Response response = httpRequest.post("https://acssimuat.payubiz.in:3000/termUrl/DecoupledResponse");
		 
		System.out.println("content type of response    :    "+ response.contentType());
		response.then().log().all();
		// verifying the response
	//	response.then().body("foo.bar.text()", equalTo("sudo make me a sandwich!")).
//	    body(":foo.:bar.text()", equalTo("sudo "));
		
		//verify the xpath
	//	response.then().body(hasXPath("/greeting/firstName"), containsString("A"));
	//	response.then().body(hasXPath("/greeting/firstName[text()='Ajay']"));
	  //Schema and DTD validation pending
		
		
		// validation of multiple value
		response.then().
	       body("shopping.category.find { it.@type == 'groceries' }.item", hasItems("Chocolate", "Coffee"));
		
		// depth search
		response.then().
	       body("**.find { it.@type == 'groceries' }.item", hasItems("Chocolate", "Coffee"));
		
		
	}
}
