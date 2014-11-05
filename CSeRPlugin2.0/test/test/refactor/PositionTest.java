package test.refactor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.Position;



public class PositionTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Position position = new Position(5,11);
		Position position1 = new Position(4,3);
		Position position2 = new Position(1,2);
		Position position3 = new Position(6,12);
		Position position4 = new Position(5,2);
	PositionListTest positions = new PositionListTest();
//	positions.add(position);
//	positions.add(position1);
//	positions.add(position2);
//	positions.add(position3);
//	positions.add(position4);
	PositionListTest positions1 = new PositionListTest();
	Position position11 = new Position(4,3);
	
	positions1.add(position11);
	positions1.add(position3);

/*	print(positions.filteroverlappingPositions().sort());
	positions =positions.filteroverlappingPositions().sort();
	Position pos= new Position(5,11);
	print(positions.before(pos));
	print(positions.after(pos));*/
	positions.addNewOnes(positions1);
	print(positions)
	;
	}

	private static void print(Position after) {
		System.out.println(after);
		
	}

	private static List<Position> checkBeforeAdd(List<Position> positions) {
		List<Integer> indices = new ArrayList<Integer>();
		for(int i=0; i< positions.size();i++){
			for(int j=0; j<positions.size();j++){
				try {
					if(i!=j && contains(positions.get(i), positions.get(j))){
						indices.add(j);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		List<Position> positions2 = new ArrayList<Position>();
		for(int j=0; j <positions.size() ;j++){
		if(!indices.contains(j)){
			positions2.add(positions.get(j));
		}
	}
		return positions2;
	}

	private static void print(List<Position> positions) {
		for(Position position: positions){
			System.out.println(position);
		}
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
	
}
