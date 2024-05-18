package by.arvisit.cabapp.driverservice.e2e;

import io.cucumber.java.Before;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.RestAssured;

@CucumberContextConfiguration
public class CucumberE2EConfiguration {

    @Before
    public void setUpPort() {
        String serverPort = System.getProperty("serverPort");
        RestAssured.port = Integer.parseInt(serverPort);
    }
}
