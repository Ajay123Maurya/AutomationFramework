package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class FileHandler {

	public Map<String,String> getDataWithRowNo() throws IOException {
		Map<String,String> mp = new HashMap<String, String>();
		
			FileInputStream file = new FileInputStream(new File("/Users/ajay.maurya/Automation/AutomationFramework/Parameter/AmazonTestData.xlsx"));
			XSSFWorkbook workbook = new XSSFWorkbook(file);
		    XSSFSheet sheet = workbook.getSheetAt(0);
		    
		
		return mp;
	}
	
}
