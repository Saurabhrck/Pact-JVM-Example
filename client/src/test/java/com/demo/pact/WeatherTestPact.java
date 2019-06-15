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

public class WeatherTestPact {

	@Rule
	public PactProviderRuleMk2 provider = new PactProviderRuleMk2("sampleprovider", "localhost", 81, this);

	@Pact(consumer = "createWeatherPact", provider = "sampleprovider")
	public RequestResponsePact createWeatherPact(PactDslWithProvider builder) {
		Map<String, String> headers = new HashMap();
		headers.put("Content-Type", "application/json");

		DslPart etaResults = new PactDslJsonBody().stringValue("City", "Kolkata")
				.stringType("Temperature", "30 Degree celsius").stringType("WeatherDescription", "haze")
				.stringType("WindSpeed", "3.1 Km per hour").stringType("WindDirectionDegree", "40 Degree").asBody();
		
		return builder.uponReceiving("The request")
				.path("/utilities/weather/city/Kolkata").method("GET")
				.willRespondWith().status(200).headers(headers).body(etaResults)
				.toPact();

	}
	
	
	
	@Test
	@PactVerification(fragment = "createWeatherPact")
	public void testPactWeather() {
		System.out.println();
		WeatherTest weather = new WeatherTest(provider.getUrl());
		String temp = weather.checkTemp("Kolkata");
		System.out.println(temp);
		assertTrue(Integer.valueOf(temp) < 50);
	}

}
