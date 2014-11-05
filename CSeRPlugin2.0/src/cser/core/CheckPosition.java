package cser.core;


import org.eclipse.jface.text.Position;

import cser.utils.PositionUtils;

/**
 * Records a pair of correspondence positions <pos1, pos2> between two clones.
 * @author dhou
 *
 */
public class CheckPosition {
	private boolean isDeleted = false;
	private String currentPosition =null;
	private String parentPosition = null;
	public CheckPosition(String currentPosition, String parentPosition) {
		this.setCurrentPosition(currentPosition);
		this.setParentPosition(parentPosition);
	}
	public void setCurrentPosition(String currentPosition) {
		this.currentPosition = currentPosition;
	}
	public Position getCurrentPosition() {
		return PositionUtils.extractPositionFrom(currentPosition);
	}

	public void setParentPosition(String parentPosition) {
		this.parentPosition = parentPosition;
	}
	public Position getParentPosition() {
		return PositionUtils.extractPositionFrom(parentPosition);
	}
	
	public boolean containsCurrent(String position) {
		return PositionUtils.contains(this.currentPosition, position);
	}
	
	public boolean equals(Object obj) {
		if(obj==null || ! (obj instanceof CheckPosition)) return false;
		CheckPosition checkPosition = (CheckPosition) obj;
		return checkPosition.currentPosition.equals(this.currentPosition);
	}
	public int hashCode() {
		return this.currentPosition.hashCode();
	}

	public String toString() {
		
	return "Origin="+parentPosition +",Current=" + currentPosition;
	}
	public void setIsdeleted(boolean isdeleted) {
		this.isDeleted = isdeleted;
	}
	
	/**
	 * this pair of mapping is marked as deleted.
	 * @return
	 */
	public boolean IsDeleted() {
		return isDeleted;
	}
}