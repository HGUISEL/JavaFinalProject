package edu.handong.csee;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellFill;

public class ExcelWriter {
	public XSSFWorkbook makenewWorkbook(Workbook oldWorkbook, String savefilename) {
		
		final XSSFWorkbook newWorkbook = new XSSFWorkbook();

        // Copy style source
        final StylesTable oldStylesSource = ((XSSFWorkbook) oldWorkbook).getStylesSource();
        final StylesTable newStylesSource = newWorkbook.getStylesSource();

        oldStylesSource.getFonts().forEach(font -> newStylesSource.putFont(font, true));
        oldStylesSource.getFills().forEach(fill -> newStylesSource.putFill(new XSSFCellFill(fill.getCTFill(), null)));
        oldStylesSource.getBorders()
                .forEach(border -> newStylesSource.putBorder(new XSSFCellBorder(border.getCTBorder())));
		
        final XSSFSheet oldSheet = (XSSFSheet) oldWorkbook.getSheetAt(0);
        final XSSFSheet newSheet = newWorkbook.createSheet(oldSheet.getSheetName());

        return newWorkbook;
        
	}
	
	public XSSFWorkbook callnewWorkbook(String savefilename, int type) {
        XSSFWorkbook newWorkbook = null;
        try {
        	newWorkbook = (XSSFWorkbook) WorkbookFactory.create(new File(savefilename));
        } catch (Exception e) {
            e.printStackTrace();
        }
		return newWorkbook;
		
	}
	
	//for header
	public XSSFWorkbook writeheader(Workbook oldWorkbook, XSSFWorkbook newWorkbook, int startrow, int lastrow) {
		XSSFSheet oldSheet = (XSSFSheet) oldWorkbook.getSheetAt(0);
		XSSFSheet newSheet = newWorkbook.getSheetAt(0);
		
        // Copy content
        for (int rowNumber = startrow; rowNumber <= lastrow; rowNumber++) {
            final XSSFRow oldRow = oldSheet.getRow(rowNumber);
            if (oldRow != null) {
                final XSSFRow newRow = newSheet.createRow(rowNumber);
                newRow.setHeight(oldRow.getHeight());
           
                
                for (int columnNumber = oldRow.getFirstCellNum(); columnNumber < oldRow
                        .getLastCellNum(); columnNumber++) {
                    newSheet.setColumnWidth(columnNumber+1, oldSheet.getColumnWidth(columnNumber));

                    final XSSFCell oldCell = oldRow.getCell(columnNumber);
                    if (oldCell != null) {
                        final XSSFCell newCell = newRow.createCell(columnNumber+1);

                        // Copy value
                        setCellValue(newCell, getCellValue(oldCell));

                        // Copy style
                        XSSFCellStyle newCellStyle = newWorkbook.createCellStyle();
                        newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
                        newCell.setCellStyle(newCellStyle);
                    }
                }
                
                if(rowNumber == lastrow) {
                	XSSFCell newCell = newRow.createCell(0);
                	XSSFCell oldCell = oldRow.getCell(0);
                	
                	setCellValue(newCell, "studentNumber");
                	
                	XSSFCellStyle newCellStyle = newWorkbook.createCellStyle();
                    newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
                    newCell.setCellStyle(newCellStyle);
                }
            }
        }
		return newWorkbook;
	}
	
	public XSSFWorkbook writecontent(Workbook oldWorkbook, XSSFWorkbook newWorkbook, String studentnumber, int startrow, int lastrow, int newtotal) {
		XSSFSheet oldSheet = (XSSFSheet) oldWorkbook.getSheetAt(0);
		XSSFSheet newSheet = newWorkbook.getSheetAt(0);
		
        // Copy content
        for (int rowNumber = startrow+1; rowNumber <= lastrow; rowNumber++) {
            final XSSFRow oldRow = oldSheet.getRow(rowNumber);
            if (oldRow != null) {
                final XSSFRow newRow = newSheet.createRow(newtotal++);
                newRow.setHeight(oldRow.getHeight());

            	XSSFCell news = newRow.createCell(0);
            	XSSFCell old = oldRow.getCell(1);
            	
            	XSSFCellStyle newCellStyles = newWorkbook.createCellStyle();
            	try {
            		newCellStyles.cloneStyleFrom(old.getCellStyle());
            		news.setCellStyle(newCellStyles);            		
            	} catch(Exception e) {
            		return newWorkbook;
            	}
            	setCellValue(news, studentnumber);

                
                for (int columnNumber = oldRow.getFirstCellNum(); columnNumber < oldRow
                        .getLastCellNum(); columnNumber++) {
//                	System.out.println(rowNumber + "\t" + columnNumber);
                    newSheet.setColumnWidth(columnNumber+1, oldSheet.getColumnWidth(columnNumber));

                    final XSSFCell oldCell = oldRow.getCell(columnNumber);
                    if (oldCell != null) {
                        final XSSFCell newCell = newRow.createCell(columnNumber+1);
                        // Copy value
                        setCellValue(newCell, getCellValue(oldCell));

//                        // Copy style
                        XSSFCellStyle newCellStyle = newWorkbook.createCellStyle();
                        try {
	                        newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
	                        newCell.setCellStyle(newCellStyle);
                        }catch(Exception e) {
                        	final XSSFCell preoldCell = oldRow.getCell(columnNumber-1);
                            newCellStyle.cloneStyleFrom(preoldCell.getCellStyle());
                            newCell.setCellStyle(newCellStyle);                        	
                        }
                    }
                }
            }
        }
		return newWorkbook;
	}
	
    private static void setCellValue(final XSSFCell cell, final Object value) {
        if (value instanceof Boolean) {
            cell.setCellValue((boolean) value);
        } else if (value instanceof Byte) {
            cell.setCellValue((byte) value);
        } else if (value instanceof Double) {
            cell.setCellValue((double) value);
        } else if (value instanceof String) {
//        	System.out.println(value);
            if (((String) value).startsWith("=")) { 
                //  Formula String  
                cell.setCellFormula(((String) value).substring(1));
            } else {
                cell.setCellValue((String) value);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    private static Object getCellValue(final XSSFCell cell) {
        switch (cell.getCellTypeEnum()) {
            case BOOLEAN:
                return cell.getBooleanCellValue(); // boolean
            case ERROR:
                return cell.getErrorCellValue(); // byte
            case NUMERIC:
                return cell.getNumericCellValue(); // double
            case STRING:
            case BLANK:
                return cell.getStringCellValue(); // String
            case FORMULA:
                return  "=" + cell.getCellFormula(); // String for formula
            default:
                throw new IllegalArgumentException();
        }
    }
}
