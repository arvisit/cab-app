package by.arvisit.cabapp.passengerservice.component;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:features",
        tags = "@component",
        plugin = {"pretty"})
public class CucumberRunnerCT {

}
