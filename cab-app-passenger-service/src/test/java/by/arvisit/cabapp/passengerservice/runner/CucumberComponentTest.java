package by.arvisit.cabapp.passengerservice.runner;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:features",
        glue = { "by.arvisit.cabapp.passengerservice.service.impl" },
        plugin = {"pretty"})
public class CucumberComponentTest {

}
