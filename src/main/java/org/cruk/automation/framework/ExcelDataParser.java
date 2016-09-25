package org.cruk.automation.framework;

import java.io.FileInputStream;
//import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelDataParser implements DataParser 
{
	private ArrayList<TestCaseData> alltestCasesData;
	private XSSFWorkbook xssfWorkbook;
	//private final FileOutputStream outputFile = null;
	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelDataParser.class);
	
	@Override
	public void loadData(String filePath, String tabName) 
	{
        try 
        {
              FileInputStream file = new FileInputStream(filePath);
              LOGGER.info("Before workbook:" + new Date().toString());
              xssfWorkbook = new XSSFWorkbook(file);
              LOGGER.info("After workbook:" + new Date().toString());
              XSSFSheet sheet = xssfWorkbook.getSheet(tabName);
              LOGGER.info("After tab:" + new Date().toString());
              Iterator<Row> rowIterator = sheet.iterator(); 
              boolean isHeader = true;
              int colPosition = 0;
              ArrayList<String> colList = new ArrayList<String>();
              alltestCasesData = new ArrayList<TestCaseData>();
              while (rowIterator.hasNext()) 
              {
                    Row row = (Row) rowIterator.next();
                    if(isHeader)
                    {     
                          Iterator<Cell> cellIterator = row.cellIterator();
                          while (cellIterator.hasNext()) {
                                Cell cell = (Cell) cellIterator.next();
                          
                                switch (cell.getCellType()) 
                                {
                                      case Cell.CELL_TYPE_STRING:
                                            colList.add(cell.getStringCellValue());
                                            //System.out.println(cell.getStringCellValue());
                                            break;
                                      case Cell.CELL_TYPE_BOOLEAN:
                                            colList.add(String.valueOf(cell.getBooleanCellValue()));
                                            //System.out.println(cell.getBooleanCellValue());
                                            break;
                                      case Cell.CELL_TYPE_NUMERIC:
                                            colList.add(String.valueOf(cell.getNumericCellValue()));
                                            //System.out.println(cell.getNumericCellValue());
                                            break;
                                }
                                
                                //colCnt++;
                          }
                          LOGGER.info("Columns Count: " + colList.size());
                          isHeader = false;
                    }
                    else
                    {
                    	  TestCaseData tcData = new TestCaseData();                    	
                          Iterator<Cell> cellIterator = row.cellIterator();
                          while (cellIterator.hasNext()) 
                          {
                        	  	
                        	  	Cell cell = (Cell) cellIterator.next();
                                switch (cell.getCellType()) 
                                {
                                      
                                	  case Cell.CELL_TYPE_STRING:
                                		  /*
                                		  if(cell.getStringCellValue().equalsIgnoreCase("BLANK"))
                                		  {	  
                                			  tcData.addValueForColumn(colList.get(colPosition), "");
                                			  //System.out.println("Blank Type: " + cell.getStringCellValue());
                                		  }
                                		  */
                                		  if(!cell.getStringCellValue().equalsIgnoreCase("BLANK"))
                                		  {
                                			  tcData.addValueForColumn(colList.get(colPosition), cell.getStringCellValue());
                                		  }	  
                                          break;
                                      case Cell.CELL_TYPE_BOOLEAN:
                                    	  tcData.addValueForColumn(colList.get(colPosition), String.valueOf(cell.getBooleanCellValue()));
                                            break;
                                      case Cell.CELL_TYPE_NUMERIC:
                                    	  tcData.addValueForColumn(colList.get(colPosition), String.valueOf(cell.getNumericCellValue()));
                                    	  //System.out.println("Number Type: " + String.valueOf(cell.getNumericCellValue()));
                                            break;
                                     /* case Cell.CELL_TYPE_BLANK:
                                    	  tcData.addValueForColumn(colList.get(colPosition),"");
                                    	  //System.out.println("Blank Type: " + cell.getStringCellValue());
                                    	  	break;
                                     */
                                }
                                
                                colPosition++;
                          }
                          alltestCasesData.add(tcData);
                          colPosition = 0;
                    }     
              }
              LOGGER.info("Total tcs found: " + alltestCasesData.size());
        } 
        catch (Exception e) 
        {
        	LOGGER.info(e.getMessage());
        }

  }


	@Override
	public List<TestCaseData> getAllData() 
	{
		//System.out.println("All Data Count: " + alltestCasesData.size());
		return alltestCasesData;
	}
	
		
}
