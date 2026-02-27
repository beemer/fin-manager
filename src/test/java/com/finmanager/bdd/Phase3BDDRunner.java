package com.finmanager.bdd;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features/CategoryManagement.feature",
    glue = "com.finmanager.bdd",
    plugin = {"pretty", "html:target/cucumber-reports/phase3.html"}
)
public class Phase3BDDRunner {
}
