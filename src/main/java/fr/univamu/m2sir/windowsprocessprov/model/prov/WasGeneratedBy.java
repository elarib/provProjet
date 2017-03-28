package fr.univamu.m2sir.windowsprocessprov.model.prov;

import java.time.LocalTime;

public class WasGeneratedBy extends Happened{
	
	private String id;
	private Entity entity;
	private Activity activity;
	private long step;
	
	public WasGeneratedBy(String id,LocalTime time, Entity entity, Activity activity, long step) {
		super(time);
		this.entity = entity;
		this.activity = activity;
		this.step = step;
		this.id = id;
	}

	public Entity getEntity() {
		return entity;
	}
	public Activity getActivity() {
		return activity;
	}
	
	public long getStep() {
		return step;
	}
	
	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return "WasGeneratedBy [entity=" + entity + ", activity=" + activity + ", getTime()=" + getTime() + "]";
	}
	
	
}
