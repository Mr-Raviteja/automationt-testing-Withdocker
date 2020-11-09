package testing;

import java.io.File;
import org.junit.runner.RunWith;

import com.vimalselvam.cucumber.listener.Reporter;

import org.junit.AfterClass;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"src/test/java/features"},
        tags = "~@Ignore",
        glue = {"stepDefinition"},
        plugin = {"pretty", "html:target/cucumber-reports", "json:target/cucumber.json",
        			"com.vimalselvam.cucumber.listener.ExtentCucumberFormatter:target/cucumber-reports/report.html"},
        monochrome = true)
public class TestRunner {
	
	@AfterClass
    public static void writeExtentReport() {
        Reporter.loadXMLConfig(new File("src/test/java/config/extent_report-config.xml"));
    
    }
}