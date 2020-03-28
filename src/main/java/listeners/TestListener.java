package listeners;

import java.util.Set;

import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import utils.SeleniumFunction;

/**
 * 
 * Author : Ajay Maurya
 *
 */

public class TestListener extends TestListenerAdapter {

	SeleniumFunction helper = new SeleniumFunction();
	String previousTestCase = "";

	@Override
	public void onTestSuccess(ITestResult result) {
		String testClassName = helper.getTestClassName(result.getInstanceName()).trim();
		System.out.println("Passed : " + testClassName + " : " + result.getName() + "() :- "
				+ result.getMethod().getDescription() + "\n");
	}

	@Override
	public void onTestFailure(ITestResult result) {
		String testClassName = helper.getTestClassName(result.getInstanceName()).trim();

		System.out.println("Failed : " + testClassName + " : " + result.getName() + "() :- "
				+ result.getMethod().getDescription() + "\n" + "\n");

		String testMethodName = result.getName().toString().trim();
		try {
			helper.takeScreenShot(testClassName, testMethodName);

			previousTestCase = testMethodName;

		} catch (Exception ex) {
			System.out.println("Web:Problem with Screenshot or Alerting" + ": " + ex.getMessage());
		}

	}

	@Override
	public void onTestSkipped(ITestResult result) {
		String testClassName = helper.getTestClassName(result.getInstanceName()).trim();
		System.out.println("Skipped : " + testClassName + " : " + result.getName() + "() :- "
				+ result.getMethod().getDescription() + "\n");
	}

	@Override
	public void onFinish(ITestContext context) {
		Set<ITestResult> failedTests = context.getFailedTests().getAllResults();
		for (ITestResult temp : failedTests) {
			ITestNGMethod method = temp.getMethod();
			if (context.getFailedTests().getResults(method).size() > 1) {
				failedTests.remove(temp);
			} else {
				if (context.getPassedTests().getResults(method).size() > 0) {
					failedTests.remove(temp);
				}
			}
		}
	}
}
