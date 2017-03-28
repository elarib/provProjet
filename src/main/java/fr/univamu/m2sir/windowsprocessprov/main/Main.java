package fr.univamu.m2sir.windowsprocessprov.main;

import java.io.File;
import java.io.IOException;
import java.util.List;

import fr.univamu.m2sir.windowsprocessprov.model.prov.Document;
import fr.univamu.m2sir.windowsprocessprov.model.windows.WindowsEvent;

public class Main {

	private final static String OUTPUT_FORMAT_ERROR_MSG = 
			"Output format couldn't be concluded from the output file name argument";
	private final static String INVALID_PID_ERROR_MSG =
			"The specified PID argument is invalid";
	private final static String WRONG_NUMBER_ARGS_ERROR_MSG =
			"Wrong number of arguments :\n"+
			"<input_csv_file_path> <output_file_path> "+
			"[ <?|process_name> <?|pid> <?|operation> <?|path_suffix> ]";
	
	
	public static void main(String[] args) throws IOException {
		if(args.length!=2 && args.length!=6)
			throw new IllegalArgumentException(WRONG_NUMBER_ARGS_ERROR_MSG);
		
		List<WindowsEvent> windowsEventList = null;
		System.out.println("Parsing CSV..");
		if(args.length==6){
			try{
				windowsEventList = WindowsEvent.parseCsv(new File(args[0])
						, args[2].equals("NULL")?null:args[2]
						, args[3].equals("NULL")?null:Long.parseLong(args[3])
						, args[4].equals("NULL")?null:args[4]
						, args[5].equals("NULL")?null:args[5]);
			}catch(NumberFormatException e){
				throw new IllegalArgumentException(INVALID_PID_ERROR_MSG);
			}
		}
		else
			windowsEventList = WindowsEvent.parseCsv(new File(args[0]), null, null, null, null);
		System.out.println("Generating PROV document..");
		Document document = WindowsEvent.toProvDocument(windowsEventList);
		
		int indexOfDot = args[1].lastIndexOf('.');
		if(indexOfDot<0)
			throw new IllegalArgumentException(OUTPUT_FORMAT_ERROR_MSG);
		String outputType = args[1].substring(indexOfDot+1).toLowerCase();
		if(outputType.equals("json")){
			System.out.println("Generating PROV-JSON..");
			document.writeJsonToFile(args[1]);
		}
		else if(outputType.equals("svg")){
			System.out.println("Generating PROV SVG graph..");
			document.writeSvgToFile(args[1]);
		}
		else if(outputType.equals("html")||outputType.equals("htm")){
			System.out.println("Generating PROV SVG graph simulator..");
			document.writeHtmlToFile(args[1]);
		}
		else
			throw new IllegalArgumentException(OUTPUT_FORMAT_ERROR_MSG);
		System.out.println("Generation completed");
	}

}
