package com.finmanager.bdd;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features",
    glue = "com.finmanager.bdd",
    plugin = {"pretty", "html:target/cucumber-reports/phase2.html"},
    monochrome = true
)
public class Phase2BDDRunner {
}
