package fr.univamu.m2sir.windowsprocessprov.model.prov;

import java.time.LocalTime;

public abstract class Happened {
	private LocalTime time;

	public Happened(LocalTime time) {
		super();
		this.time = time;
	}

	public LocalTime getTime() {
		return time;
	}

}
