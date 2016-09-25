package org.cruk.automation.framework;

import java.util.List;

public interface DataParser 
{
	public void loadData(String filePath, String tabName);
	public List<TestCaseData> getAllData();
}
