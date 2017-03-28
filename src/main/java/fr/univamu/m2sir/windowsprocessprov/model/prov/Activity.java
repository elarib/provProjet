package fr.univamu.m2sir.windowsprocessprov.model.prov;

import fr.univamu.m2sir.windowsprocessprov.model.windows.WindowsEvent;

public class Activity{
	
	private String processName;
	private long pid;
	private String operation;
	private int version;
	private long step;
	
	public Activity(String processName, long pid, String operation, int version, long step) {
		super();
		this.processName = processName;
		this.pid = pid;
		this.operation = operation;
		this.version = version;
		this.step = step;
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
	public int getVersion() {
		return version;
	}
	
	public long getStep() {
		return step;
	}
	
	public String getId(){
		return processName + "_" + pid + "_" + operation + "_" + version;
	}

	public boolean isInWindowsEvent(WindowsEvent event){
		return event.getPid()==pid 
				&& event.getProcessName().equals(processName)
				&& event.getOperation().equals(operation);
	}
	
	public Activity copy(long step){
		return new Activity(processName, pid, operation, version+1,step);
	}
	
}
