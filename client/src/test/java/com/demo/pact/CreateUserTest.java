package com.demo.pact;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;

import com.demo.rest.WeatherTest;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRuleMk2;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;

public class CreateUserTest {
	@Rule
	public PactProviderRuleMk2 provider = new PactProviderRuleMk2("sampleprovider", "localhost", 82, this);
	
	@Pact(consumer = "createUserPact", provider = "sampleprovider")
	public RequestResponsePact createUserPact(PactDslWithProvider builder) {
		Map<String, String> headers = new HashMap();
		headers.put("Content-Type", "application/json");
		
		DslPart reqbody=new PactDslJsonBody().stringValue("FirstName", "Test").stringValue("LastName", "User")
				.stringValue("UserName", "testuser1")
				.stringValue("Password", "Test1234")
				.stringValue("Email", "Test@user1.com");

		DslPart etaResults = new PactDslJsonBody().stringValue("FaultId", "User already exists")
				.stringValue("fault", "FAULT_USER_ALREADY_EXISTS").asBody();
		
		return builder.given("Given a user request is about to be placed").uponReceiving("The request")
				.path("/customer/register").method("POST").body(reqbody)
				.willRespondWith().status(200).headers(headers).body(etaResults)
				.toPact();

	}

	
	@Test
	@PactVerification(fragment = "createUserPact")
	public void testPactUser() {
		System.out.println();
		WeatherTest weather = new WeatherTest(provider.getUrl());
		int code= weather.createUser();
		System.out.println(code);
		assertTrue(code==200);
	}

}
