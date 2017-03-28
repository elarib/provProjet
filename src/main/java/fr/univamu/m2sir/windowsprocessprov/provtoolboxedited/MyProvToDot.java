package fr.univamu.m2sir.windowsprocessprov.provtoolboxedited;

import java.util.HashMap;
import java.util.Map;

import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.HasOther;
import org.openprovenance.prov.model.Relation;

import fr.univamu.m2sir.windowsprocessprov.model.prov.Activity;
import fr.univamu.m2sir.windowsprocessprov.model.prov.Entity;
import fr.univamu.m2sir.windowsprocessprov.model.prov.Used;
import fr.univamu.m2sir.windowsprocessprov.model.prov.WasGeneratedBy;

public class MyProvToDot extends ProvToDotEdited{

	private Map<String,Entity> entityStepMap;
	private Map<String,Activity> activityStepMap;
	private Map<String,Used> usedStepMap;
	private Map<String,WasGeneratedBy> wgbStepMap;

	public MyProvToDot(Map<String, Entity> entityStepMap, Map<String, Activity> activityStepMap,
			Map<String, Used> usedStepMap, Map<String, WasGeneratedBy> wgbStepMap) {
		super();
		this.entityStepMap = entityStepMap;
		this.activityStepMap = activityStepMap;
		this.usedStepMap = usedStepMap;
		this.wgbStepMap = wgbStepMap;
	}

	@Override
	public HashMap<String, String> addEntityLabel(org.openprovenance.prov.model.Entity entity, HashMap<String, String> properties) {
		properties.put("label", entity.getId().getLocalPart());
		properties.put("comment", "step:"+entityStepMap.get(entity.getId().getLocalPart()).getStep());
		return properties;
	}

	@Override
	public HashMap<String, String> addActivityLabel(org.openprovenance.prov.model.Activity activity, HashMap<String, String> properties) {
		properties.put("label", activity.getId().getLocalPart());
		properties.put("comment", "step:"+activityStepMap.get(activity.getId().getLocalPart()).getStep());
		return properties;
	}

	@Override
	protected void relationName(Relation relation, HashMap<String, String> properties) {
		super.relationName(relation, properties);
		if(relation instanceof org.openprovenance.prov.xml.Used){
			org.openprovenance.prov.xml.Used used = (org.openprovenance.prov.xml.Used) relation;
			properties.put("comment", "step:"+usedStepMap.get(used.getId().getLocalPart()).getStep());
		}
		else if(relation instanceof org.openprovenance.prov.xml.WasGeneratedBy){
			org.openprovenance.prov.xml.WasGeneratedBy wgb = 
					(org.openprovenance.prov.xml.WasGeneratedBy) relation;
			properties.put("comment", "step:"+wgbStepMap.get(wgb.getId().getLocalPart()).getStep());
		}
	}

	@Override
	public void addStepComment(HashMap<String, String> properties, HasOther statement) {
		if(statement instanceof org.openprovenance.prov.model.Entity){
			org.openprovenance.prov.model.Entity entity = (org.openprovenance.prov.model.Entity) statement;
			properties.put("comment", "step:"+entityStepMap.get(entity.getId().getLocalPart()).getStep());
		}
	}

	@Override
	public String getPropertyValueWithUrl(Attribute t) {
		return super.getPropertyValueWithUrl(t).replaceAll("\\\\","\\\\\\\\");
	}
	
	
	
}
