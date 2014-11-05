package cser.core;

import java.util.HashSet;
import java.util.Set;

public class ObjectVar  {
	
	private String name="";
	private Set<String> types = new HashSet<String>();
	
	
	public ObjectVar(String name) {
	setName(name);
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setTypes(Set<String> types) {
		this.types = types;
	}
	public Set<String> getTypes() {
		return types;
	}
	public void addType(String type){
		types.add(type);
	}
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(getName()+": ");
		for(String type: types){
			buffer.append(type +",");
		}
		return buffer.toString();
	}
	
}
	
	