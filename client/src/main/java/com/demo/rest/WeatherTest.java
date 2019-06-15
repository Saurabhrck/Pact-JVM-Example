package com.demo.rest;

import org.json.simple.JSONObject;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class WeatherTest
{
    private int port=80;
    
    private String address="http://restapi.demoqa.com:";

    public WeatherTest() {
        address=address+port;
        System.out.println("Using address: "+address);
    }

    public WeatherTest(String address) {
        this.address=address;
        System.out.println("Using address: "+address);
    }

    public static void main( String[] args ) {
    	WeatherTest weather=new WeatherTest();
    	System.out.println(weather.checkTemp("Kolkata"));
    	System.out.println(weather.createUser());
    }

    public String checkTemp(String location) {
    	String temp=null;
        try {
        	RestAssured.baseURI=address;
        	
        	RequestSpecification httprequest=RestAssured.given();
        	
        	httprequest.header("Content-Type", "application/json");
        	httprequest.header("Accept","application/json");
        	
        	Response response=httprequest.get( "/utilities/weather/city/"+location);
        	JsonPath json=response.jsonPath();
        	System.out.println(json.prettify());
        	temp=json.getString("Temperature").substring(0, json.getString("Temperature").indexOf(" "));
        }
        catch (Exception e) {
        	e.printStackTrace();
            System.out.println("Unable to get eta, e="+e);
        }
		return temp;
    }
    
    public int createUser() {
    	int code=0;
    	try {
    		RestAssured.baseURI=address;
    		RequestSpecification httprequest=RestAssured.given();
    		
    		httprequest.header("Content-Type", "application/json");
        	httprequest.header("Accept","application/json");
        	
        	JSONObject jsonbody=new JSONObject();
        	jsonbody.put("FirstName", "Test");
        	jsonbody.put("LastName", "User");
        	jsonbody.put("UserName", "testuser1");
        	jsonbody.put("Password", "Test1234");
        	jsonbody.put("Email", "Test@user1.com");
        	
        	httprequest.body(jsonbody.toJSONString());
        	
        	Response response=httprequest.request(Method.POST,"/customer/register");
        	
        	System.out.println(response.jsonPath().prettify());
        	
        	code=response.getStatusCode();
    	}
    	catch(Exception e) {
    		System.out.println("Cant get it to work "+e);
    	}
    	return code;
    }
}
