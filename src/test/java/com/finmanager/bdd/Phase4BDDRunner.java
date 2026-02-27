package com.finmanager.bdd;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features/Analytics.feature",
    glue = "com.finmanager.bdd",
    plugin = {
        "pretty",
        "html:target/cucumber-reports/analytics-report.html"
    }
)
public class Phase4BDDRunner {
}
