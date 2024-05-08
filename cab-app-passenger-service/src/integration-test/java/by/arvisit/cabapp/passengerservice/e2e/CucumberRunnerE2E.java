package by.arvisit.cabapp.passengerservice.e2e;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:features",
        tags = "@e2e",
        plugin = {"pretty"})
public class CucumberRunnerE2E {

}
