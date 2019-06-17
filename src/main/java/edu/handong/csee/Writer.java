package edu.handong.csee;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import edu.handong.csee.linkedlist.LinkedList;

public class Writer implements Runnable{
	private ArrayList<String> header;
	private LinkedList result;
	private String savefilename;
	
	public Writer(ArrayList<String> header, LinkedList result, String savefilename) {
		this.header = header;
		this.result = result;
		this.savefilename = savefilename;
	}
	
	public void run() {
		writeExcel();
	}
	
	public void writeExcel() {
		XSSFWorkbook workbook = new XSSFWorkbook();
		int rownum = 0;
		int cellnum = 0;
		XSSFSheet sheet = workbook.createSheet("자료수집양식");

		
//		System.out.println("********header");
		Row row = sheet.createRow(rownum++);
		Cell cell = row.createCell(cellnum++);
        cell.setCellValue((String) "파일이름");
		for(String value : header) {
			cell = row.createCell(cellnum++);
            cell.setCellValue((String) value);
		}
		
//		System.out.println("********value");
		
		int index = 0;

		for(int i=0; i<result.size(); i++) {
			for(String value : result.getdata(i)) {
				if(index%header.size()==0) {
					row = sheet.createRow(rownum++);
					cellnum=0;
					cell = row.createCell(cellnum++);
		            cell.setCellValue((String) result.getstudentid(i));
				}
				cell = row.createCell(cellnum++);
	            cell.setCellValue((String) value);
	            index++;
			}		
		}
		try {
			FileOutputStream out = new FileOutputStream(new File(savefilename)); 
            workbook.write(out); 
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		System.out.println(savefilename + " was written successfully on disk."); 
	}
	
	public void writeErrorExcel() {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("ERROR ZIP FILES");
		
		int rowNum = 0;
		for(String filename : ExcelReader.error) {
			Row row = sheet.createRow(rowNum++);
			Cell cell = row.createCell(0);
			cell.setCellValue((String) filename);
		}
		
        try {
            FileOutputStream outputStream = new FileOutputStream("error.csv");
            workbook.write(outputStream);
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("error.csv file was written succesfully.");
	}

}
