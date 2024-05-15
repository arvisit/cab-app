package by.arvisit.cabapp.driverservice.e2e;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import io.cucumber.core.options.Constants;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("by/arvisit/cabapp/driverservice/e2e")
@ConfigurationParameter(key = Constants.GLUE_PROPERTY_NAME, value = "by.arvisit.cabapp.driverservice.e2e")
@ConfigurationParameter(key = Constants.FEATURES_PROPERTY_NAME, value = "classpath:features")
@ConfigurationParameter(key = Constants.FILTER_TAGS_PROPERTY_NAME, value = "@e2e")
@ConfigurationParameter(key = Constants.PLUGIN_PROPERTY_NAME, value = "pretty, html:target/cucumber-e2e-reports")
public class CucumberRunnerE2E {

}
