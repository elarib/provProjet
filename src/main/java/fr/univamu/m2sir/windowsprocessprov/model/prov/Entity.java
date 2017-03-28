package fr.univamu.m2sir.windowsprocessprov.model.prov;

import fr.univamu.m2sir.windowsprocessprov.model.windows.WindowsEvent;
import fr.univamu.m2sir.windowsprocessprov.utilities.QualifiedNamesMap;

public class Entity{
	private String path;
	private int version;
	private long step;
	private String id;
	
	public Entity(String path, int version, long step, QualifiedNamesMap qualifiedNamesMap) {
		super();
		this.path = path;
		this.version = version;
		this.step = step;
		this.id = qualifiedName(qualifiedNamesMap);
	}

	public String getPath() {
		return path;
	}

	public int getVersion() {
		return version;
	}
	
	public long getStep() {
		return step;
	}

	public String getId() {
		return id;
	}

	public boolean isInWindowsEvent(WindowsEvent event){
		return event.getPath()==path;
	}
	
	public Entity copy(long step,QualifiedNamesMap qualifiedNamesMap){
		return new Entity(path, version+1,step,qualifiedNamesMap);
	}

	@Override
	public String toString() {
		throw new UnsupportedOperationException();
	}

	public String qualifiedName(QualifiedNamesMap qualifiedNamesMap) {
		return qualifiedNamesMap.getQualifiedName(path) + "_" + version;
	}
}
