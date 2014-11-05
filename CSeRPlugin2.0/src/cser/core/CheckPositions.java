package cser.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.text.Position;

import cser.utils.PositionUtils;

/**
 * A map with a key of parent position represented as string => CheckPosition 
 */
public class CheckPositions extends HashMap<String, CheckPosition> {

//	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private static final long serialVersionUID = 5672756805668049096L;
	/**
	 * position is in current file.
	 * @param position
	 * @return
	 */
	public  CheckPosition getCurrentPosition(Position position) {
		Position smallestPosition = new Position(0, Integer.MAX_VALUE);
		CheckPosition smallestCheckPosition = null;
		 for(String positionItr : super.keySet()){
			 CheckPosition checkPosition = super.get(positionItr);

	//		 System.out.println("k=" + positionItr + super.get(positionItr.toString()));
			 if(!checkPosition.IsDeleted() && PositionUtils.containsOrContainsPart(checkPosition.getCurrentPosition(),position)){
				 if(checkPosition!=null && (smallestPosition.length > checkPosition.getCurrentPosition().length)){
		//		 System.out.println(checkPosition);
				 smallestPosition = checkPosition.getCurrentPosition();
				 smallestCheckPosition = checkPosition;
				 }
			 }
		 }
		 return smallestCheckPosition;
		
	}
	/**
	 * position is in current file.
	 * @param position
	 * @return
	 */
	public  CheckPosition getStrictContainingPosition(Position position) {
		Position smallestPosition = new Position(0,Integer.MAX_VALUE);
		CheckPosition smallestCheckPosition = null;
		for(String it : super.keySet()){
			CheckPosition checkPosition = super.get(it);
			if (checkPosition.IsDeleted()){
				 continue;
			}
			Position currentPosition = checkPosition.getCurrentPosition();
			if(PositionUtils.strictlyContains(currentPosition, position)){
				 int endOfSmall = smallestPosition.offset+smallestPosition.length;
				 int endOfCurrent = currentPosition.offset+currentPosition.length;
				 if(smallestPosition.offset<=currentPosition.offset && endOfCurrent<=endOfSmall){
					 smallestPosition = currentPosition;
					 smallestCheckPosition = checkPosition;
				 }
			}
		 }
		 return smallestCheckPosition;
	}
	
	public  CheckPosition filterParentPositions(Position position) {
		 for(String positionItr : super.keySet()){
			 CheckPosition checkPosition = super.get(positionItr);

	//		 System.out.println("k=" + positionItr + super.get(positionItr.toString()));
			 if(!checkPosition.IsDeleted() && PositionUtils.isSame(checkPosition.getParentPosition(),position)){
				 return checkPosition;
			 }
		 }
		 return null;
		
	}
	
	public  List<CheckPosition> filterDeletePositions(Position position) {
		List<CheckPosition> checkPositions = new ArrayList<CheckPosition>();
		 for(String positionItr : super.keySet()){
			 CheckPosition checkPosition = super.get(positionItr.toString());
			 if(checkPosition.IsDeleted() && PositionUtils.contains(position,checkPosition.getCurrentPosition())){
				 checkPositions.add(checkPosition);
			 }
		 }
		 
		 /*
		  * This is the just of list of all AST's which is  contained inside this, we need the largest in that
		  * so we need to filter again like this (1,10) , (5,2), (2,13) will return 
		  */
		 
		 return checkBeforeAdd(checkPositions);
		
	}
	
	private  List<CheckPosition> checkBeforeAdd(List<CheckPosition> positions) {
		List<Integer> indices = new ArrayList<Integer>();
		for(int i=0; i< positions.size();i++){
			for(int j=0; j<positions.size();j++){
				try {
					if(i!=j && PositionUtils.contains(positions.get(i).getParentPosition(), positions.get(j).getParentPosition())){
						indices.add(j);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		List<CheckPosition> positions2 = new ArrayList<CheckPosition>();
		for(int j=0; j <positions.size() ;j++){
		if(!indices.contains(j)){
			positions2.add(positions.get(j));
		}
		}
		return positions2;
	}


	public void updateCurrentPosition(Position orginalPosition, Position newPosition) {
		CheckPosition checkPosition = super.get(orginalPosition.toString());
		if(checkPosition==null){
			System.out.println("SOMETHING IS WRONG WITH THE ORGINAL POSITION STORED" + orginalPosition +"," + newPosition);
			return;
		}
/*		if(orginalPosition.length >= newPosition.length){
			updateDeletedPosition(orginalPosition);
		}*/
		checkPosition.setCurrentPosition(newPosition.toString());
	}
	
	public void updateDeletedPosition(Position orginalPosition) {
		CheckPosition checkPosition = super.get(orginalPosition.toString());
		if(checkPosition==null){
			System.out.println("SOMETHING IS WRONG WITH THE ORGINAL POSITION STORED" + orginalPosition );
			return;
		}
		checkPosition.setIsdeleted(true);
	}
	
	public void updateParentPosition(Position currentPosition,
			Position newParentPosition) {
	     CheckPosition checkPosition = findCheckPosition(currentPosition);
		if(checkPosition==null){
			System.out.println("SOMETHING IS WRONG WITH THE ORGINAL POSITION STORED" + currentPosition +"," + newParentPosition);
			return;
		}
		checkPosition.setParentPosition(newParentPosition.toString());
		remove(checkPosition);
		put(newParentPosition.toString(), checkPosition);
	}
	private CheckPosition findCheckPosition(Position currentPosition) {

		for(String key : this.keySet()){
		    CheckPosition position = this.get(key);
		    if (position.getCurrentPosition().equals(currentPosition)) {
		    	return position;
		    }
		}
		
		return null;
	}
}