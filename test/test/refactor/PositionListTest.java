package test.refactor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.jdt.internal.ui.text.PreferencesAdapter;
import org.eclipse.jface.text.Position;

import cser.utils.PositionUtils;

public class PositionListTest extends ArrayList<Position> {


	/*
	 * when given (1,4 ) (1,3)  (10,1)it will return 1,4 and (10,1)
	 */
	public static void main(String[] args) {
		
		PositionListTest listTest = new PositionListTest();
		listTest.add(new Position(114,1));
		listTest.add(new Position(132,32));
		listTest.add(new Position(132,32));
		PositionListTest listTest2 =listTest.filteroverlappingPositions();
		
	}
	
	public PositionListTest filteroverlappingPositions() {
		List<Integer> indices = new ArrayList<Integer>();
		for(int i=0; i< this.size();i++){
			for(int j=0; j<this.size();j++){
				try {
					
					if(i!=j &&!  PositionUtils.contains(this.get(i), this.get(j))){
						indices.add(j);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		PositionListTest newpositions = new PositionListTest();
		for(int j=0; j <this.size() ;j++){
		if(!indices.contains(j)){
			newpositions.add(this.get(j));
		}
	}
		return newpositions;
	}
	
	public PositionListTest sort () {
	      Collections.sort(this, new Comparator<Position>(){

		public int compare(Position o1, Position o2) {
			if( o1.offset > o2.offset) return 1 ;
			if( o1.offset < o2.offset) return -1 ;
			return 0;
		}
		 
	 });
	      return this ;
		
	}
	public static boolean contains(Position pos1, Position pos2) {

		if (pos1.offset > pos2.offset)
			return false;
		if (getSecondPoint(pos1) < getSecondPoint(pos2))
			return false;
		return true;
	}

	private static int getSecondPoint(Position pos) {
		return pos.length + pos.offset;
	}
	 public Position before(Position position) {
		 sort();
		 Object[] objects =  this.toArray();
		 if(objects.length <2) return null;
		 if(objects[0].equals(position)) return null;
		 for(int i =1; i < objects.length ;i++){
			 if(objects[i].equals(position)) return (Position)objects[i-1];
		 }
		 return null;
		 
	 }
	 public Position after(Position position) {
		 Object[] objects =  this.toArray();
		 if(objects.length <2) return null;
		 if(objects[objects.length-1].equals(position)) return null;
		 for(int i =0; i < objects.length-1 ;i++){
			 if(objects[i].equals(position)) return (Position)objects[i+1];
		 }
		 return null;
		 
	 }
	 public void  addNewOnes(List<Position> positions) {
		 
		 for(Iterator<Position> iterator = positions.iterator(); iterator.hasNext(); ) {
			 Position  position= iterator.next();
		 if(this.contains(position)) this.remove(position);
		 else this.add(position);
		 }
	 }
	 @Override
	public boolean add(Position o) {
	    if (!this.contains(o))
		return super.add(o);
	    return false;
	}
}
