package cser.core;

import java.util.HashSet;
import java.util.Iterator;

import cser.utils.PositionUtils;

public class CSeRChanges extends HashSet<CSeRChange> {

	/** 
	 * 
	 */
	private static final long serialVersionUID = 7200518694115266617L;

	/**
	 * 
	 */
//	private static final long serialVersionUID = 1L;
	
	
   public void addChange (String message,int type,String position, String parentPosition, String fileName) {
		   this.add(new CSeRChange(message,type,position,parentPosition,fileName));
   }
	
   public void addChange (String message,int type, String position, String parentPosition, String fileName,boolean isParent) {
		   this.add(new CSeRChange(message,type,position,parentPosition,fileName,isParent));
   }   
   
   public CSeRChange filterChanges (String key) {
	 Iterator<CSeRChange> iterator =  this.iterator();
	 while(iterator.hasNext()){
		 CSeRChange change = iterator.next();
		 if(PositionUtils.contains(change.getPosition(), key)){
			 return change;
		 }
	 }
	 return null;
   }
   
 

}
