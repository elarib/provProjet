package fr.univamu.m2sir.windowsprocessprov.model.windows;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.univamu.m2sir.windowsprocessprov.model.prov.Activity;
import fr.univamu.m2sir.windowsprocessprov.model.prov.Agent;
import fr.univamu.m2sir.windowsprocessprov.model.prov.Document;
import fr.univamu.m2sir.windowsprocessprov.model.prov.Entity;
import fr.univamu.m2sir.windowsprocessprov.model.prov.Used;
import fr.univamu.m2sir.windowsprocessprov.model.prov.WasAssociatedWith;
import fr.univamu.m2sir.windowsprocessprov.model.prov.WasGeneratedBy;
import fr.univamu.m2sir.windowsprocessprov.utilities.ListsMap;
import fr.univamu.m2sir.windowsprocessprov.utilities.QualifiedNamesMap;

public class WindowsEvent {
	private LocalTime timeOfDay;
	private String processName;
	private long pid;
	private String operation;
	private String path;
	private String result;
	private String detail;
	
	public final static String CREATE_FILE_OP = "CreateFile";
	public final static String CLOSE_FILE_OP = "CloseFile";
	public final static String READ_FILE_OP = "ReadFile";
	public final static String WRITE_FILE_OP = "WriteFile";
	
	public WindowsEvent(LocalTime timeOfDay, String processName, long pid, String operation, String path,
			String result, String detail) {
		super();
		this.timeOfDay = timeOfDay;
		this.processName = processName;
		this.pid = pid;
		this.operation = operation;
		this.path = path;
		this.result = result;
		this.detail = detail;
	}
	
	public static List<WindowsEvent> parseCsv(File file,String filterByProcessName
			, Long filterByPid, String filterByOperation, String filterByPathSuffix) throws IOException{
		File tempFile = File.createTempFile("windows_process_log", "tmp");
		try(BufferedReader reader = new BufferedReader(new FileReader(file));
				BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))){
			String line = null;
			reader.readLine();
			StringBuilder sb = new StringBuilder();
			int i=0;
			while((line=reader.readLine())!=null){
				if(sb.length()!=0)
					sb.append(" ");
				sb.append(line);
				if(line.endsWith("\"")){
					if(i++!=0)
						writer.write("\n");
					writer.write(sb.toString());
					sb = new StringBuilder();
				}
			}
		}
		
		final String windowsDateRegex = "(\\d+)\\D+(\\d+)\\D+(\\d+)\\D+(\\d+).*";
		final Pattern windowsDatePattern = Pattern.compile(windowsDateRegex);
		
		Stream<WindowsEvent> windowsEventStream = 
				Files.lines(tempFile.toPath(),StandardCharsets.ISO_8859_1)
			.parallel()
			.map(line->{
				String[] elements = line.substring(1,line.length()).split("\",\"");
				Matcher matcher = windowsDatePattern.matcher(elements[0]);
				LocalTime time = null;
				if(matcher.find()){
					time = LocalTime.of(Integer.parseInt(matcher.group(1))
							,Integer.parseInt(matcher.group(2))
							,Integer.parseInt(matcher.group(3))
							,Integer.parseInt(matcher.group(4)));
				}else
					time = LocalTime.now();
				
				return new WindowsEvent(time
										,elements[1]
										,Long.parseLong(elements[2])
										,elements[3]
										,elements[4]
										,elements[5]
										,elements[6].substring(0,elements[6].length()-1));
			})
			.filter(event->event.getOperation().equals(WindowsEvent.CREATE_FILE_OP)
						||event.getOperation().equals(WindowsEvent.CLOSE_FILE_OP)
						||event.getOperation().equals(WindowsEvent.READ_FILE_OP)
						||event.getOperation().equals(WindowsEvent.WRITE_FILE_OP));
		
			if(filterByProcessName!=null)
				windowsEventStream = windowsEventStream.filter(
						event->event.getProcessName().equals(filterByProcessName));
			if(filterByPid!=null)
				windowsEventStream = windowsEventStream.filter(
						event->Long.toString(event.getPid()).equals(filterByPid));
			if(filterByOperation!=null) 
				windowsEventStream = windowsEventStream.filter(
						event->event.getOperation().equals(filterByOperation));
			if(filterByPathSuffix!=null) 
				windowsEventStream = windowsEventStream.filter(
						event->event.getPath().endsWith(filterByPathSuffix));
		
		List<WindowsEvent> windowsEventList = windowsEventStream.collect(Collectors.toList());
		
		tempFile.delete();
		return windowsEventList;
	}
	
	public static Document toProvDocument(List<WindowsEvent> windowsEventList){
		List<Agent> agents = new LinkedList<>();
		
		ListsMap<Activity> activities = new ListsMap<>();
		
		ListsMap<Entity> entities = new ListsMap<>();		
		
		List<Used> usedList = new LinkedList<>();
		
		List<WasGeneratedBy> wasGeneratedByList = new LinkedList<>();
		
		List<WasAssociatedWith> wasAssociatedWithList = new LinkedList<>();
		
		QualifiedNamesMap qualifiedNamesMap = new QualifiedNamesMap();

		long step=1L;
		int usedId = 1;
		int wgbId = 1;
		for(WindowsEvent event : windowsEventList){
			
			boolean used = event.isUsedOperation();			
			boolean wasGenerated = event.isGeneratedOperation();
			String activityName = event.getActivityName();
			String entityName = event.getEntityName();
			
			//Create Used Entity if not already created
			if(used && !entities.containsKey(entityName))
				entities.push(entityName, new Entity(event.getPath(),0,0L,qualifiedNamesMap));
			
			Optional<Activity> lastActivityWithSameName = activities.peek(activityName);
			
			Optional<Entity> lastEntityWithSameName = entities.peek(entityName);
			
			//New Activity
			Activity activity = lastActivityWithSameName.isPresent()?
					lastActivityWithSameName.get().copy(step):
					new Activity(event.getProcessName(),event.getPid(),event.getOperation(),0,step);
			activities.push(activityName, activity);
			
			if(wasGenerated){//New Entity				
				Entity entity = lastEntityWithSameName.isPresent()?
						lastEntityWithSameName.get().copy(step,qualifiedNamesMap):
						new Entity(event.getPath(),0,step,qualifiedNamesMap);
				entities.push(entityName, entity);
				
				wasGeneratedByList.add(new WasGeneratedBy("wGB"+wgbId++,event.getTimeOfDay()
						, entity, activity,step));
			}
			
			if(used){
				Entity entity = lastEntityWithSameName.get();
				usedList.add(new Used("u"+usedId++,event.getTimeOfDay(), activity, entity,step));
			}
			step++;
		}
		
		return new Document(entities.toList(), activities.toList()
				, agents, usedList, wasGeneratedByList, wasAssociatedWithList);
	}

	public boolean isUsedOperation(){
		return getOperation().equals(WindowsEvent.READ_FILE_OP)
				||getOperation().equals(WindowsEvent.WRITE_FILE_OP)
				||getOperation().equals(WindowsEvent.CLOSE_FILE_OP);
	}
	
	public boolean isGeneratedOperation(){
		return getOperation().equals(WindowsEvent.CREATE_FILE_OP)
				||getOperation().equals(WindowsEvent.WRITE_FILE_OP);
	}

	public String getActivityName(){
		return getProcessName()+"<"+getPid()+">"+getOperation();
	}
	
	public String getEntityName(){
		return getPath();
	}

	public LocalTime getTimeOfDay() {
		return timeOfDay;
	}

	public String getProcessName() {
		return processName;
	}

	public long getPid() {
		return pid;
	}

	public String getOperation() {
		return operation;
	}

	public String getPath() {
		return path;
	}

	public String getResult() {
		return result;
	}

	public String getDetail() {
		return detail;
	}
	
	@Override
	public String toString() {
		return "WindowsProcess [timeOfDay=" + timeOfDay + ", processName=" + processName + ", pid=" + pid
				+ ", operation=" + operation + ", path=" + path + ", result=" + result + ", detail=" + detail + "]";
	}
	
	
}
