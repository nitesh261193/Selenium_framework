package com.merlin.utils;

import java.io.File;

import org.apache.log4j.Logger;
import org.testng.Assert;

import com.merlin.common.Constants;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.io.File;

/**
 * @author Pooja Chopra
 *
 */
public class Rest_Api {

	public static Logger log = Logger.getLogger(Rest_Api.class);
	public static Response response = null;

	public static String config_Name = "";
	public String url = Constants.dashboard_Url;
	public String end_Point;
	public String headers;
	public String request_Body;
	public String status_Code;
	public static String file_Path = "";
  	public String api_Name;
	
  	public String Get_Api() {

	  RestAssured.baseURI = url;
	  log.info("url :" + url);
	  log.info("endpoint : "+ end_Point);
	    try {

	    	response = RestAssured.given().headers(Constants.header_Map).auth().preemptive()
	    			  .basic("qaadmin@naehas.com", "naehas2016").when().get(end_Point);
	    } catch (Exception e) {
	      e.printStackTrace();
	    }

	    log.info("Response :" + response.asString());
	    log.info("Status Code : " + response.getStatusCode());
	    Assert.assertEquals( response.getStatusCode(),Integer.parseInt(status_Code), "Response status not received as expected");
	   return response.asString();
  }

  	public String Get_Api_withparams() {
		RestAssured.baseURI = url;
		log.info("url :" + url);
		log.info("endpoint : "+ end_Point);
		try {

			response = RestAssured.given().headers(Constants.header_Map).auth().preemptive()
					.basic("qaadmin@naehas.com", "naehas2016").formParams(Constants.form_Map).when().get(end_Point);
		} catch (Exception e) {
			e.printStackTrace();
		}

		log.info("Response :" + response.asString());
		log.info("Status Code : " + response.getStatusCode());
		Assert.assertEquals( response.getStatusCode(),Integer.parseInt(status_Code), "Response status not received as expected");
		return response.asString();
	}

  	public String Post_Api(String content_Type) {
	  log.info("url : "+ url);
	  log.info("endpoint : "+ end_Point);
	  log.info("Request Body : "+ request_Body);

	  RestAssured.baseURI = url;

	  log.info(url);
	  switch(content_Type)
	  {
	  case "json" : response = RestAssured.given().headers(Constants.header_Map).auth()
			  .form("qaadmin@naehas.com", "naehas2016").when().body(request_Body).post(end_Point);
	  break;
	  case "form_Url" : response = RestAssured.given().contentType("application/x-www-form-urlencoded")
			  .headers(Constants.header_Map).auth().preemptive().basic("qaadmin@naehas.com", "naehas2016").formParams(Constants.form_Map).post(end_Point);
	  break; 
	  case "form_Url1" : response = RestAssured.given().relaxedHTTPSValidation()
			  .headers(Constants.header_Map).auth().preemptive().basic("qaadmin@naehas.com", "naehas2016").formParams(Constants.form_Map).post(end_Point);
	  break;
	  case "upload_File" : File UploadFile = new File(file_Path);
	  						response = RestAssured.given().multiPart(UploadFile)
	  								.headers(Constants.header_Map).auth().preemptive().basic("qaadmin@naehas.com", "naehas2016").formParams(Constants.form_Map).post(end_Point);
		break;
		
	  case "post_Without_Url_Encoding":
			response = RestAssured.given().contentType("application/x-www-form-urlencoded").contentType("charset=UTF-8")
					.accept("application/json").urlEncodingEnabled(false).headers(Constants.header_Map).auth()
					.preemptive().basic("qaadmin@naehas.com", "naehas2016").formParams(Constants.form_Map)
					.post(end_Point);
			break;	
	  }
	  log.info("Response :" + response.asString());
	  log.info("Status Code : " + response.getStatusCode());
	  log.info(response.getHeaders());
	  Assert.assertEquals( response.getStatusCode(),Integer.parseInt(status_Code), "Response status not received as expected");
	  return response.asString();

  }

}