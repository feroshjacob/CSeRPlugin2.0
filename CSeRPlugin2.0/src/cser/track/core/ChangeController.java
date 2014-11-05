package cser.track.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.Position;

import cser.core.CSeRChange;
import cser.core.CSeRChanges;
import cser.core.CSeRClone;
import cser.core.CSeRDBController;
import cser.utils.GlobalConfig;
import cser.utils.SerialiseHelper;

/**
 * Manages the syntactic changes.
 * Changes are stored as global data.
 * @author dhou
 *
 */
public class ChangeController {

	/**
	 * file names-to-changes map
	 */
	private static Map<String, CSeRChanges> buffer = new HashMap<String, CSeRChanges>();

	public static void clearChanges() {
		buffer.clear();
	}

	public static void updateBuffer(String fileName, CSeRChanges changes) {
		buffer.put(fileName, changes);
	}

	public static CSeRChanges loadChanges(IFile file) {
        return loadChanges(file.getName());
	}

	private static String fchangeTxt = GlobalConfig.db_folder+GlobalConfig.seperator+"changes"+GlobalConfig.seperator;
	private static CSeRChanges loadChanges(String fileName) {

		if (buffer.get(fileName) != null && buffer.get(fileName).size() > 0){
			return buffer.get(fileName);
		}
		
		Object object = SerialiseHelper.deserialize(fchangeTxt + fileName);
		if(object==null) {
			CSeRChanges changes = new CSeRChanges();
			buffer.put(fileName, changes);
			SerialiseHelper.serialize(changes, fileName);
			return changes;
		}
		else if (object != null && object instanceof CSeRChanges) {
			CSeRChanges changes = (CSeRChanges) object;
			buffer.put(fileName, changes);
			return changes;
		}

		return null;
	}

	/**
	 * Update the position in file that corresponds to parentPosition.
	 * @param file
	 * @param position
	 * @param parentPosition
	 */
	public static void updatePositions(IFile file, Position parentPosition, Position position) {

		CSeRChanges changes = loadChanges(file);
		if (changes == null || changes.size() < 1)
			return;
		boolean changed = false;
		for (CSeRChange change : changes) {
			if ((change.getParentPosition().equals(parentPosition.toString()))) {
				changed = true;
				if(change.getType() ==IMessages.chgDelete){
					Position pos = new Position(position.offset + position.length-2,2);
					change.setPosition(pos.toString());
				}
				else {
				change.setPosition(position.toString());
				}
			}
		}
		if (changed) {
			buffer.put(file.getName(), changes);
		}
	}

	public static void updateParentPositions(IFile file, Position position,
			Position originalPosition) {
		List<CSeRClone> clones = CSeRDBController.getTargetClones(file);
		for (CSeRClone clone : clones) {
			for (CSeRChange change : clone.getChanges()) {
				if ((change.getParentPosition().equals(originalPosition
						.toString()))) {
					change.setParentPosition(position.toString());
				}
				buffer.put(file.getName(), clone.getChanges());
			}
		}
	}

	public static void serialize() {
 
		Set<String> keyset = buffer.keySet();
		for (String key : keyset) {
			CSeRChanges changes =  loadChanges(key);
			SerialiseHelper.serialize(changes, fchangeTxt + key);
		}
	}

}
