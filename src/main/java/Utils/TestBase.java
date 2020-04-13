package Utils;

import java.lang.reflect.Method;

import org.testng.annotations.DataProvider;

public class TestBase {
	
	protected static ThreadLocal<Config[]> threadLocalConfig = new ThreadLocal<Config[]>();
	
	@DataProvider(name = "GetTestConfig")
	public Object[][] GetTestConfig(Method method)
	{
		Config testConf = new Config(method);
		testConf.testName = method.getDeclaringClass().getName() + "." + method.getName();
		testConf.testStartTime = Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss");
		threadLocalConfig.set(new Config[] { testConf });
		return new Object[][] { { testConf } };
	}
	
}
