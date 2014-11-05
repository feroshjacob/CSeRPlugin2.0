package cser.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.text.Position;

public class PositionList extends ArrayList<Position> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * when given (1,4 ) (1,3) (10,1), it will return (1,4) and (10,1)
	 */
	public PositionList filteroverlappingPositions() {
		List<Integer> indices = new ArrayList<Integer>();
		for (int i = 0; i < this.size(); i++) {
			for (int j = 0; j < this.size(); j++) {
				try {
					if (i != j
							&& PositionUtils.contains(this.get(i), this.get(j))) {
						indices.add(j);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		PositionList newpositions = new PositionList();
		for (int j = 0; j < this.size(); j++) {
			if (!indices.contains(j)) {
				newpositions.add(this.get(j));
			}
		}
		return newpositions;
	}

	public PositionList sort() {
		Collections.sort(this, new Comparator<Position>() {

			public int compare(Position o1, Position o2) {
				if (o1.offset > o2.offset)
					return 1;
				if (o1.offset < o2.offset)
					return -1;
				return 0;
			}

		});
		return this;

	}

	public void diff(List<Position> positions) {

		 for(Iterator<Position> iterator = positions.iterator(); iterator.hasNext(); ) {
			 Position  position= iterator.next();
		 if(this.contains(position)) this.remove(position);
		 else this.add(position);
		 }
	}
	public void subtract(List<Position> positions) {

		 for(Iterator<Position> iterator = positions.iterator(); iterator.hasNext(); ) {
			 Position  position= iterator.next();
		 if(this.contains(position)) this.remove(position);
		 }
	}
	
	public Position before(Position position) {
		Object[] objects = this.toArray();
		if (objects.length < 2)
			return null;
		if (objects[0].equals(position))
			return null;
		for (int i = 1; i < objects.length; i++) {
			if (objects[i].equals(position))
				return (Position) objects[i - 1];
		}
		return null;

	}

	public Position after(Position position) {
		Object[] objects = this.toArray();
		if (objects.length < 2)
			return null;
		if (objects[objects.length - 1].equals(position))
			return null;
		for (int i = 0; i < objects.length - 1; i++) {
			if (objects[i].equals(position))
				return (Position) objects[i + 1];
		}
		return null;

	}
	public boolean add(Position o) {
	    if (!this.contains(o))
		return super.add(o);
	    return false;
	}

}
