package cser.track.core;

public interface IMessages {
	
	/*
	 * Change
	 */
	String chgMessage =" Change";
	String chgName = "Name";
	String chgMethodParams = "Method Parameters";
	String chgRtnType = "Return Type";
	String chgType = "Type";
	String chgMethodToField = "Method Changed to Field";
	String chgExpression = "Expression";
	String chgString ="String";
	String chgNumber = "Number";
	String chgMethodInvocation ="Method Invocation";
	int chgUpdate = 1;
	
	
	/*
	 * Insert
	 */
	String insMessage = "";
	int chgInsert = 2;
	int chgInsert_Field = 5;

	/*
	 *Unknown 
	 */
	String unKnownMessage = "Uncategorized";
	int chgUnknown =4;
	/*
	 * Delete
	 */
	String deleteMessage = " ";
	String conditionMessage = "";
	int chgDelete = 3;
	int chgDelete_Field=6;
	String method = "Method ";
	String field = "Field ";
	String innerClass = "Inner Class ";
	String move ="Moved";
	/*
	 * Annotations
	 */
	String annotationForUpdate = "cserstmt";
	String annotationForInsert = "cserstmtinsert";
	String annotationForDelete ="cserannotationd";
	String annotationForInsertF = "cserstmtinsertF";
	String annotationForDeleteF ="cserannotationdF";
	
	
	String annotationForProcessing ="processing";
	
	String annotationForMove ="cserstmtmove";
	int moveMessge=7;
	
	
	/*
	 * Node level
	 */
	int nodeChgDelete = 16;
	int nodeChgInsert = 12;
	

}
