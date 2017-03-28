package fr.univamu.m2sir.windowsprocessprov.model.prov;

import java.time.LocalTime;

public class Used extends Happened{
	
	private String id;
	private Activity activity;
	private Entity entity;
	private long step;
	
	public Used(String id,LocalTime time, Activity activity, Entity entity, long step) {
		super(time);
		this.activity = activity;
		this.entity = entity;
		this.step = step;
		this.id = id;
	}
	
	public Activity getActivity() {
		return activity;
	}
	public Entity getEntity() {
		return entity;
	}
	
	public long getStep() {
		return step;
	}

	public String getId(){
		return id;
	}
	
	@Override
	public String toString() {
		return "Used [activity=" + activity + ", entity=" + entity + ", getTime()=" + getTime() + "]";
	}

	
	
}
