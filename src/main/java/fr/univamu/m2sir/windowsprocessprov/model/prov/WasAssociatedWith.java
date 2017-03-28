package fr.univamu.m2sir.windowsprocessprov.model.prov;

import java.time.LocalTime;

public class WasAssociatedWith extends Happened{
	private Activity activity;
	private Agent agent;
	public WasAssociatedWith(LocalTime time, Activity activity, Agent agent) {
		super(time);
		this.activity = activity;
		this.agent = agent;
	}
	public Activity getActivity() {
		return activity;
	}
	public Agent getAgent() {
		return agent;
	}
	@Override
	public String toString() {
		return "WasAssociatedWith [activity=" + activity + ", agent=" + agent + ", getTime()=" + getTime() + "]";
	}
	
	
}
