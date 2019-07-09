package edu.handong.csee;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;

import com.google.errorprone.annotations.Var;

import edu.handong.csee.linkedlist.LinkedList;

public class ZipReader{
	private String file;
	private String studentid;
	
	public ZipReader(String file, String studentid) {
		this.file = file;
		this.studentid = studentid;
	}
	
	public void run() {
		System.out.println("***Thread" + " " + studentid);
		readFileInZip(file, studentid.substring(0,8));	
	}

	public void readFileInZip(String path, String studentid) {
		ArrayList<String> datas ;
		ZipFile zipFile;
		try {
			zipFile = new ZipFile(path);
			Enumeration<? extends ZipArchiveEntry> entries = zipFile.getEntries();
			
		    while(entries.hasMoreElements()){
		    	ZipArchiveEntry entry = entries.nextElement();
		    	
		        InputStream stream = zipFile.getInputStream(entry);
		        ExcelReader myReader = new ExcelReader();
		        
		        myReader.getData(stream, studentid);
		    }
		    zipFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
	}
}
