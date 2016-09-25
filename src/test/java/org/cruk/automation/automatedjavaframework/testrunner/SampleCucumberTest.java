	/**
	 * @author : Gaurav Seth
	 * @since : 14th September 2016
	 * Description :This class will create test reports at the end of the test execution. It will create three pdf reports in the target directory folder along with 
	 * feature-overview.html in the target\cucumber-html-reports folder.
	 **/

package org.cruk.automation.automatedjavaframework.testrunner;

import org.cruk.automation.framework.WebUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import com.github.mkolisnyk.cucumber.runner.ExtendedCucumber;
import com.github.mkolisnyk.cucumber.runner.ExtendedCucumberOptions;
import com.github.mkolisnyk.cucumber.runner.ExtendedTestNGRunner;
import cucumber.api.testng.AbstractTestNGCucumberTests;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(ExtendedCucumber.class)
@ExtendedCucumberOptions(jsonReport = "target/cucumber.json",
        retryCount = 0,
        detailedReport = true,
        detailedAggregatedReport = true,
        overviewReport = true,
        coverageReport = true,
        jsonUsageReport = "target/cucumber-usage.json",
        usageReport = true,
        toPDF = true,
        includeCoverageTags = {"@passed" },
        outputFolder = "target")
@CucumberOptions(plugin = { "html:target/cucumber-html-report",
        "json:target/cucumber.json", "pretty:target/cucumber-pretty.txt",
        "usage:target/cucumber-usage.json", "junit:target/cucumber-results.xml" },
        features="classpath:features",
        glue = "classpath:",
		tags={"@sanity"}
)
public class SampleCucumberTest  extends AbstractTestNGCucumberTests {

}

