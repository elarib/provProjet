package fr.univamu.m2sir.windowsprocessprov.utilities;

import java.util.HashMap;
import java.util.Map;

public class QualifiedNamesMap {
	private Map<String,String> pathMap = new HashMap<>();
	
	public synchronized String getQualifiedName(String path){
		String qualifiedName = pathMap.get(path);
		if(qualifiedName==null){
			String suffix = path.substring(path.contains("\\")?path.lastIndexOf("\\")+1:0);
			int sameSuffix = (int) pathMap.keySet().stream().parallel()
					.filter(p->suffix.equals(
							p.substring(p.contains("\\")?p.lastIndexOf("\\")+1:0)))
					.count();
			qualifiedName = "";
			for(int i=0;i<sameSuffix;i++) qualifiedName+="/";
			qualifiedName+=suffix;
			pathMap.put(path,qualifiedName);
		}
		return qualifiedName;
	}
}
