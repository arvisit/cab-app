package by.arvisit.cabapp.passengerservice.component;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import io.cucumber.core.options.Constants;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("by/arvisit/cabapp/passengerservice/component")
@ConfigurationParameter(key = Constants.FEATURES_PROPERTY_NAME, value = "classpath:features")
@ConfigurationParameter(key = Constants.FILTER_TAGS_PROPERTY_NAME, value = "@component")
@ConfigurationParameter(key = Constants.PLUGIN_PROPERTY_NAME, value = "pretty, html:target/cucumber-ct-reports")
public class CucumberRunnerComponentTest {

}
