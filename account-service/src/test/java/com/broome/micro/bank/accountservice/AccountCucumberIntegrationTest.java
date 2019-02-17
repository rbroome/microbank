package com.broome.micro.bank.accountservice;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features",plugin= {"pretty","html:target/cucumber"})
public class AccountCucumberIntegrationTest {

}
