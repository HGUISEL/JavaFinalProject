package edu.handong.csee;

import java.io.*;
import java.util.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import edu.handong.csee.error.NotHeaderMatchedException;
import edu.handong.csee.linkedlist.LinkedList;

public class ExcelReader {	
	static ArrayList<String> header1 = new ArrayList<String>();
	static ArrayList<String> header2 = new ArrayList<String>();
	static ArrayList<String> error = new ArrayList<String>();
	static LinkedList result1 = new LinkedList();
	static LinkedList result2 = new LinkedList();
	
	
	public void getData(InputStream is,String studentid) {
		ArrayList<String> values = new ArrayList<String>();
		ArrayList<String> header = new ArrayList<String>();
		LinkedList result = new LinkedList();
		ExcelWriter writer = new ExcelWriter();
		String savefilename = null;
		XSSFWorkbook newWorkbook;
		int startrow = 0;
		int type = 0;
		
		try (InputStream inp = is) {		    
			Workbook oldWorkbook = WorkbookFactory.create(inp);
			Sheet sheet = oldWorkbook.getSheetAt(0);
	        
			try {
		        if(sheet.getRow(0).getLastCellNum()==7) {
		        	header = header1;
		        	result = result1;
		        } else if (sheet.getRow(0).getLastCellNum()==4) {
		        	header = header2;
		        	result = result2;
		        	startrow = 1;
		        	type = 1;
		        } else {
		        	throw new NotHeaderMatchedException();
		        }				
			} catch (NotHeaderMatchedException e) {
				System.out.println(e.getMessage());
				error.add(studentid);
			}

	        
//	        header check
	        Row row = sheet.getRow(startrow);
	        if(header.isEmpty()) {
//	        	System.out.println("");
//	        	System.out.println("******************�뿤�뜑 留뚮뱾湲�");
		        for(int i=0; i<row.getLastCellNum(); i++) {
		        	Cell cell = row.getCell(i);
		        	switch (cell.getCellType()) { 
		              case NUMERIC: 
		            	  header.add(String.valueOf(cell.getNumericCellValue()));
		                  break; 
		              case STRING: 
//		            	  System.out.println(cell.getStringCellValue());
		            	  header.add(cell.getStringCellValue());
		                  break; 
		        	} 
		        }
	        } else {
//	        	System.out.println("�뿤�뜑 鍮꾧탳");
	        	try {
		        	for(int i=0; i<row.getLastCellNum(); i++) {
		        		if(!row.getCell(i).toString().equals(header.get(i))) {
		        			throw new NotHeaderMatchedException();
		        		}
			        }				
				} catch (NotHeaderMatchedException e) {
					System.out.println(e.getMessage());
					error.add(studentid);
					return;
				}

	        }

	        // Copy content
	        for (int rowNumber = startrow+1; rowNumber <= sheet.getLastRowNum(); rowNumber++) {
	        	final XSSFRow oldRow = (XSSFRow) sheet.getRow(rowNumber);
	            if (oldRow != null) {
	            	for (int columnNumber = 0; columnNumber < header.size(); columnNumber++) {                   
	                    final XSSFCell oldCell = oldRow.getCell(columnNumber);
	                    if (oldCell != null) {
	    		        	switch (oldCell.getCellType()) { 
			  		        	case NUMERIC: 
			  		        		values.add(String.valueOf(oldCell.getNumericCellValue()));
			  		        		break; 
			  		            case STRING: 
			  		            	if(oldCell.getStringCellValue().contentEquals(" ")) {
			  		            		values.add(" ");
			  		            	}
			  		            	values.add(oldCell.getStringCellValue());
			  		            	break; 
			  		            default:
			  		            	values.add(" ");
    		        		}
	                    } else {
	                    	values.add(" ");
	                    }
            		}
	            } else {
	            	values.add(" ");
	            }
	        }
   
	        oldWorkbook.close();
	        result.add(values, studentid);
		}catch (FileNotFoundException e) {
			//e.printStackTrace();
			//return;
		} catch (IOException e) {
			//e.printStackTrace();
			//return;
		} catch (Exception e) {
			
		}
	}
	
}