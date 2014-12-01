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
	  // System.out.println("Key:"+key);
	 Iterator<CSeRChange> iterator =  this.iterator();
	 int count=0; //SR
	 while(iterator.hasNext()){
		// System.out.println("Change# "+(count++));
		 CSeRChange change = iterator.next();
		 if(PositionUtils.contains(change.getPosition(), key)){
			// System.out.println("filterChanges() returns change");
			 return change;
		 }
	 }
	// System.out.println("filterChanges() returns null");
	 return null;
   }
 
   /*
// SR code to display the changes-----------------------------
	public void showChanges(){
		Iterator<CSeRChange> iterator =  this.iterator();
		int count=0;
		while(iterator.hasNext()){
			System.out.println("Change# "+(count++));
			CSeRChange change = iterator.next();
			System.out.println("Position: "+change.getPosition());
			System.out.println("Parent Position: "+change.getParentPosition());
			System.out.println("Type: "+change.getType());
			System.out.println("Text: "+change.getMessage());
			System.out.println("......................................................................");				
		}
	}
	// SR code to display the changes-----------------------------
   
 
*/
}
