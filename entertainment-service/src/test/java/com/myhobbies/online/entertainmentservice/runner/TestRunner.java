package com.myhobbies.online.entertainmentservice.runner;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.Test;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = "com/myhobbies/online/entertainmentservice/cucumber/stepdefinition",
        plugin = {"pretty", "html:target/entertainment-service/report.html", "json:target/entertainment-service/reports.json"},
        monochrome = true,
        tags = "@APITesting"
)
@Test
public class TestRunner extends AbstractTestNGCucumberTests {

}