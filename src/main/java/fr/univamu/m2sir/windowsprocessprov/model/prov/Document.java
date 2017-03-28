package fr.univamu.m2sir.windowsprocessprov.model.prov;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.interop.InteropFramework.ProvFormat;

import fr.univamu.m2sir.windowsprocessprov.provtoolboxedited.MyProvToDot;

public class Document {
	
	private List<Entity> entities;
	private List<Activity> activities;
	private List<Agent> agents;
	private List<Used> usedList;
	private List<WasGeneratedBy> wasGeneratedByList;
	private List<WasAssociatedWith> wasAssociatedWithList;
	
	public Document(List<Entity> entities, List<Activity> activities, List<Agent> agents, List<Used> usedList,
			List<WasGeneratedBy> wasGeneratedByList, List<WasAssociatedWith> wasAssociatedWithList) {
		super();
		this.entities = entities;
		this.activities = activities;
		this.agents = agents;
		this.usedList = usedList;
		this.wasGeneratedByList = wasGeneratedByList;
		this.wasAssociatedWithList = wasAssociatedWithList;

	}
	public List<Entity> getEntities() {
		return entities;
	}
	public List<Activity> getActivities() {
		return activities;
	}
	public List<Agent> getAgents() {
		return agents;
	}
	public List<Used> getUsedList() {
		return usedList;
	}
	public List<WasGeneratedBy> getWasGeneratedByList() {
		return wasGeneratedByList;
	}
	public List<WasAssociatedWith> getWasAssociatedWithList() {
		return wasAssociatedWithList;
	}
	
	public void writeJsonToFile(File file) throws IOException{
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))){
			writer.write("{\n");
			
			writer.write("\"prefix\": {");
			{
				writer.write("\"xsd\": \"http://www.w3.org/2001/XMLSchema#\",");
				writer.write("\"prov\": \"http://www.w3.org/ns/prov#\",");
				writer.write("\"sir\": \"http://www.univ-amu.fr/\"");
			}
			writer.write("},\n");
			
			int i=0;
			writer.write("\"entity\": {");
			for(Entity entity : this.entities){
				if(i++!=0) writer.write(", ");
				writer.write("\"sir:"+entity.getId()+"\": {");
				{
					if(entity.getVersion()==0){
						writer.write("\"prov:value\": {");
						{
							writer.write("\"$\": \""+entity.getPath().replace("\\","\\\\")+"\", ");
							writer.write("\"type\": \"xsd:string\"");
						}
						writer.write("}");
					}
				}
				writer.write("}");
			}
			writer.write("},\n");
			
			i=0;
			writer.write("\"activity\": {");
			for(Activity activity : this.activities){
				if(i++!=0) writer.write(", ");
				writer.write("\"sir:"+activity.getId()+"\": {");
				{
				}
				writer.write("}");
			}
			writer.write("},\n");
			
			i=0;
			writer.write("\"agent\": {");
			for(Agent ag : this.agents){
				
			}
			writer.write("},\n");

			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss.nnnnnnnnn");
			
			i=0;
			writer.write("\"used\": {");			
			for(Used used : this.usedList){
				if(i++!=0) writer.write(", ");
				writer.write("\"sir:"+used.getId()+"\": {");
				{
					writer.write("\"prov:activity\": ");
					writer.write("\"sir:"+used.getActivity().getId()+"\"");
					writer.write(", ");
					writer.write("\"prov:entity\": ");
					writer.write("\"sir:"+used.getEntity().getId()+"\"");
					writer.write(", ");
					writer.write("\"prov:time\": ");
					writer.write("\""+used.getTime().format(dateTimeFormatter)+"\"");
				}
				writer.write("}");
			}
			writer.write("},\n");
			
			i=0;
			writer.write("\"wasGeneratedBy\": {");			
			for(WasGeneratedBy wgb : this.wasGeneratedByList){
				if(i++!=0) writer.write(", ");
				writer.write("\"sir:"+wgb.getId()+"\": {");
				{
					writer.write("\"prov:activity\": ");
					writer.write("\"sir:"+wgb.getActivity().getId()+"\"");
					writer.write(", ");
					writer.write("\"prov:entity\": ");
					writer.write("\"sir:"+wgb.getEntity().getId()+"\"");
					writer.write(", ");
					writer.write("\"prov:time\": ");
					writer.write("\""+wgb.getTime().format(dateTimeFormatter)+"\"");
				}
				writer.write("}");
			}
			writer.write("},\n");
			
			i=0;
			writer.write("\"wasAssociatedWith\": {");
			for(WasAssociatedWith waw : this.wasAssociatedWithList){
				
			}
			writer.write("}\n");
				
			writer.write("}");
		}	
	}
	
	public void writeJsonToFile(String filePath) throws IOException{
		writeJsonToFile(new File(filePath));
	}
	
	public org.openprovenance.prov.model.Document toStandardDocument() throws IOException{
		File tempFile = File.createTempFile("document_temp", ".json");
		writeJsonToFile(tempFile);
		org.openprovenance.prov.model.Document doc = null;
		try(BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(tempFile))){
			InteropFramework interopFramework = new InteropFramework();
			doc = interopFramework.readDocument(inputStream, ProvFormat.JSON);
		}
		tempFile.delete();
		return doc;
	}
	
	public void writeSvgToFile(File file) throws IOException{
		try(BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))){
			org.openprovenance.prov.model.Document doc = toStandardDocument();
			File temp = File.createTempFile("dot-temp","dot");
			MyProvToDot myProvToDot = new MyProvToDot(
					entities.parallelStream().collect(
							Collectors.toMap(Entity::getId, Function.identity()))
					,activities.parallelStream().collect(
							Collectors.toMap(Activity::getId, Function.identity()))
					,usedList.parallelStream().collect(
							Collectors.toMap(Used::getId, Function.identity()))
					,wasGeneratedByList.parallelStream().collect(
							Collectors.toMap(WasGeneratedBy::getId, Function.identity())));
			myProvToDot.convert(doc, temp.getAbsolutePath(), outputStream,"svg","Windows Process Prov Graph");
			temp.delete();
		};
	}
	
	public void writeHtmlToFile(String filePath) throws IOException{
		writeHtmlToFile(new File(filePath));
	}
	
	public void writeHtmlToFile(File file) throws IOException{
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))){
			try(BufferedReader reader = new BufferedReader(
					new InputStreamReader(getClass().getResourceAsStream("html_prefix.txt")))){
				String line;
				while((line=reader.readLine())!=null)
					writer.write(line+"\n");
			}
			File tempSvg = File.createTempFile("svg-temp","svg");
			writeSvgToFile(tempSvg);
			try(BufferedReader reader = new BufferedReader(
					new FileReader(tempSvg))){
				String line="";
				while(!line.contains("<svg"))
					line = reader.readLine();
				writer.write(line.substring(line.indexOf("<svg"))+"\n");
				while((line=reader.readLine())!=null)
					writer.write(line+"\n");
			}
			tempSvg.delete();
			try(BufferedReader reader = new BufferedReader(
					new InputStreamReader(getClass().getResourceAsStream("html_suffix.txt")))){
				String line;
				while((line=reader.readLine())!=null)
					writer.write(line+"\n");
			}
		};
	}
	
	public void writeSvgToFile(String filePath) throws IOException{
		writeSvgToFile(new File(filePath));
	}
	
	@Override
	public String toString() {
		return "Document [entities=" + entities + ", activities=" + activities + ", agents=" + agents + ", usedList="
				+ usedList + ", wasGeneratedByList=" + wasGeneratedByList + ", wasAssociatedWithList="
				+ wasAssociatedWithList + "]";
	}
	
	
}
