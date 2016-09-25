package org.cruk.automation.framework;

import java.util.HashMap;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TestCaseData extends HashMap<String,String> 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HashMap<String , String> testCaseData;
	private static final Logger LOGGER = LoggerFactory.getLogger(TestCaseData.class);
	
	public TestCaseData()
	{
		testCaseData = new HashMap<String, String>();
	}
	
	public void addValueForColumn(String columnName, String value)
	{
		testCaseData.put(columnName, value);
	}
	
	public String getValueForColumn(String columnName)
	{
		String value = null;
		if(testCaseData.containsKey(columnName))
		{
			value = testCaseData.get(columnName).trim();
		}
		else
		{
			LOGGER.info("Column: " + columnName + " not found");
		}
		return value;
	}
	
	public String[] getAllColumns()
	{
		Set<String> keys = testCaseData.keySet();
		String[] allColumns = keys.toArray(new String[keys.size()]);
				
		return allColumns;
	}

}

