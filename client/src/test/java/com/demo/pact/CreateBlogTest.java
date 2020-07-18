package com.demo.pact;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRuleMk2;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;
import com.demo.rest.BlogTest;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class CreateBlogTest {
  @Rule
  public PactProviderRuleMk2 provider =
      new PactProviderRuleMk2("sampleprovider", "localhost", 82, this);

  @Pact(consumer = "checkBlog", provider = "sampleprovider")
  public RequestResponsePact createUserPact(PactDslWithProvider builder) {
    Map<String, String> headers = new HashMap();
    headers.put("Content-type", "application/json; charset=UTF-8");
    headers.put("Accept", "application/json");

    DslPart reqbody =
        new PactDslJsonBody()
            .stringValue("title", "foo test")
            .stringValue("body", "bar test")
            .numberValue("userId", 999);

    DslPart etaResults =
        new PactDslJsonBody()
            .stringValue("title", "foo test")
            .stringValue("body", "bar test")
            .numberValue("userId", 999)
            .numberType("id")
            .asBody();

    return builder
        .given("Given a user request is about to be placed")
        .uponReceiving("The request")
        .path("/posts")
        .method("POST")
        .headers(headers)
        .body(reqbody)
        .willRespondWith()
        .status(201)
        .body(etaResults)
        .toPact();
  }

  @Pact(consumer = "getBlog", provider = "sampleprovider")
  public RequestResponsePact createWeatherPact(PactDslWithProvider builder) {
    Map<String, String> headers = new HashMap();
    headers.put("Content-Type", "application/json");

    DslPart etaResults =
        new PactDslJsonBody()
            .stringValue(
                "title",
                "sunt aut facere repellat provident occaecati excepturi optio reprehenderit")
            .stringType("body")
            .numberValue("userId", 1)
            .numberValue("id", 1)
            .asBody();

    return builder
        .uponReceiving("The request")
        .path("/posts/1")
        .method("GET")
        .willRespondWith()
        .status(304)
        .body(etaResults)
        .toPact();
  }

  @Test
  @PactVerification(fragment = "checkBlog")
  public void testPactUser() {
    System.out.println("**************"+provider.getPort());
    BlogTest blogTest = new BlogTest(provider.getUrl());
    int code = blogTest.createBlog();
    System.out.println(code);
    assertEquals(201, code);
  }

  @Test
  @PactVerification(fragment = "getBlog")
  public void testPactWeather() {
    System.out.println("**************"+provider.getPort());
    BlogTest weather = new BlogTest(provider.getUrl());
    int id = weather.checkId(1);
    System.out.println(id);
    assertEquals(1, id);
  }
}
