package fr.univamu.m2sir.windowsprocessprov.utilities;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ListsMap<T> {
	private Map<String,List<T>> mMap = new HashMap<>();
	private List<T> mList = new LinkedList<>();
	
	public Optional<T> peek(String key){
		List<T> list = mMap.get(key);
		if(list!=null) return Optional.of(list.get(0));
		else return Optional.empty();
	}
	
	public void push(String key, T t){
		List<T> list = mMap.get(key);
		if(list==null){
			list = new LinkedList<>();
			mMap.put(key, list);
		}
		list.add(0,t);
		mList.add(t);
	}
	
	public boolean containsKey(String key){
		return mMap.containsKey(key);
	}
	
	public List<T> toList(){
		return mList;
	}
}
