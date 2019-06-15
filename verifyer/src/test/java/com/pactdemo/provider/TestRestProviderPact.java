package com.pactdemo.provider;


import au.com.dius.pact.provider.junit.PactRunner;
import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactFolder;
import au.com.dius.pact.provider.junit.target.HttpTarget;
import au.com.dius.pact.provider.junit.target.Target;
import au.com.dius.pact.provider.junit.target.TestTarget;
import org.junit.runner.RunWith;

@RunWith(PactRunner.class)
@Provider("sampleprovider")
@PactFolder("pacts")

public class TestRestProviderPact {


    @State("Given a user request is about to be placed")
    public void setupstate() {
        System.out.println("Given a user request is about to be placed" );
    }


    @TestTarget
    public final Target target = new HttpTarget("http","restapi.demoqa.com", 80);

}
