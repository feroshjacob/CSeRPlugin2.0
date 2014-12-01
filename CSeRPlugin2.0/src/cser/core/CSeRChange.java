package cser.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CSeRChange implements Serializable {
	private static final long serialVersionUID = 1L;
	private String message = "";
	private String position = "";
	private String parentPosition = "";
	private Map<String, String> map = new HashMap<String, String>();
	private String fileName = "";
	
	private boolean isParent = false; 

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getParentPosition() {
		return parentPosition;
	}

	public void setParentPosition(String parentPosition) {
		this.parentPosition = parentPosition;
	}

	private int type = 0;

	public CSeRChange(String message, int type, String position,
			String parentPosition, String fileName, boolean isParent) {
		this.message = message;
		this.position = position;
		this.type = type;
		this.parentPosition = parentPosition;
		this.fileName = fileName;
		this.setParent(isParent);
	}
	public CSeRChange(String message, int type, String position,
			String parentPosition, String fileName) {
		this(message,type,position,parentPosition,fileName,false);
	}
	public void setPosition(String position) {
		this.position = position;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getPosition() {
		return position;
	}

	public int getType() {
		return type;
	}


	public void setMessage(String message, String fileName) {

		map.put(fileName, message);

	}

	public String getMessage() {
		StringBuffer buffer = new StringBuffer();
		System.out.println("getMessage() called in CSeRChange");
		for (String str : map.keySet()) {
			buffer.append("\n" + map.get(str));
		}
		if(map.size() > 0) return buffer.toString();
		return message + buffer.toString();
	}



	public boolean equals(Object obj) {
		if (!(obj instanceof CSeRChange))
			return false;
		CSeRChange change = (CSeRChange) obj;
		if(this.getType() != change.getType()) return false;
		if (change.getFileName() == null && getFileName() == null)
			return getParentPosition().equals(change.getParentPosition());
		else
			return getParentPosition().equals(change.getParentPosition())
					&& change.getFileName().equals(getFileName());

	}

	public int hashCode() {
		int hashCode = (getParentPosition() + getFileName() +"").hashCode();

		return hashCode;
	}

	public void setParent(boolean isParent) {
		this.isParent = isParent;
	}

	public boolean isParent() {
		return isParent;
	}
}
