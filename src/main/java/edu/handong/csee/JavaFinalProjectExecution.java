package edu.handong.csee;

import java.io.File;
import java.util.ArrayList;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import edu.handong.csee.error.NotEnoughArgumentException;

public class JavaFinalProjectExecution {
	String input;
	String output;
	boolean help;

	@SuppressWarnings("null")
	public void main(String[] args) {
		// TODO Auto-generated method stub
			Options options = createOptions();
			if(parseOptions(options,args)) {
				if(help) {
					printHelp(options);
					return;
				}
			}

			String path = input;
			String savepath = output;
			File dir = new File(path);
			File[] fileList = dir.listFiles();
			Thread t = null;
			int i = 0;
			for(File file : fileList) {		
				String studentid = file.getName().toString().split(".zip")[0];
				
				ZipReader zipReader = new ZipReader(file.toString(), studentid);
				t = new Thread(zipReader);
				t.start();
				
				//for collecting header
				if(i==0) {
					try {
						t.join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					i=1;
				}
			}
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			

			String[] save = savepath.split("\\.");	
			Writer writer1 = new Writer(ExcelReader.header1, ExcelReader.result1, save[0] + "1." + save[1]);
			Writer writer2 = new Writer(ExcelReader.header2, ExcelReader.result2, save[0] + "2." + save[1]);
			
			Thread result1 = new Thread(writer1);
			Thread result2 = new Thread(writer2);
		
			result1.start();
			result2.start();
			writer2.writeErrorExcel();
	}
	
	public void Argcheck(String[] args) {
		try {
			if(args.length<2)
				throw new NotEnoughArgumentException();
		} catch (NotEnoughArgumentException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
	}
	
	private boolean parseOptions(Options options, String[] args) {
		// TODO Auto-generated method stub
		CommandLineParser parser = new DefaultParser();
		
		try {
			CommandLine cmd = parser.parse(options, args);
			input = cmd.getOptionValue("i");
			output = cmd.getOptionValue("o");
			help = cmd.hasOption("h");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			printHelp(options);
			return false;
		}
		return true;
	}
	
	private Options createOptions() {
		// TODO Auto-generated method stub
		Options options = new Options();

		// add options by using OptionBuilder
		options.addOption(Option.builder("i").longOpt("input")
				.desc("Set an input file path")
				.hasArg()
				.argName("input path")
				.required()
				.build());
		
		options.addOption(Option.builder("o").longOpt("output")
				.desc("Set an output file path")
				.hasArg()
				.argName("output path")
				.required()
				.build());	
		
		options.addOption(Option.builder("h").longOpt("help")
		        .desc("Show a Help page")
		        .build());
			
		return options;
	}
	
	private void printHelp(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		String header = "Java Final Project";
		String footer = "21500026 kwak hyo bin (TF45)2019-1 Java Programming";
		formatter.printHelp("JavaFinalProject",  header, options, footer, true);
	}
}
