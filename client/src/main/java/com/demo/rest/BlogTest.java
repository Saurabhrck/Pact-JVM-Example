package com.demo.rest;

import org.json.simple.JSONObject;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class BlogTest {

  private String host = "https://jsonplaceholder.typicode.com";

  public BlogTest() {
    RestAssured.baseURI = this.host;
    System.out.println("Using address: " + host);
  }

  public BlogTest(String host) {
    RestAssured.baseURI = host;
    System.out.println("Using address: " + host);
  }

  public static void main(String[] args) {
    BlogTest weather = new BlogTest();
    System.out.println(weather.checkId(1));
//    System.out.println(weather.createBlog());
  }

  public int checkId(int id) {
    int userId = 0;
    try {
      RequestSpecification httprequest = RestAssured.given();

      Response response = httprequest.get("/posts/" + id);
      JsonPath json = response.jsonPath();
      json.prettyPrint();
      System.out.println(response.statusCode());
      userId = json.getInt("userId");
    } catch (Exception e) {
      System.out.println("Unable to get userId, " + e);
    }
    return userId;
  }

  public int createBlog() {
    int id = 0;
    try {
      RequestSpecification httprequest = RestAssured.given();

      httprequest.header("Content-type", "application/json; charset=UTF-8");
      httprequest.header("Accept", "application/json");

      JSONObject jsonbody = new JSONObject();
      jsonbody.put("title", "foo");
      jsonbody.put("body", "bar");
      jsonbody.put("userId", 1);

      httprequest.body(jsonbody.toJSONString());

      Response response = httprequest.request(Method.POST, "/posts");

      System.out.println(response.jsonPath().prettify());

      id = response.getStatusCode();
    } catch (Exception e) {
      System.out.println("Cant get it to work " + e);
    }
    return id;
  }
}
