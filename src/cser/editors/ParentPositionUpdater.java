package cser.editors;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.Position;

import cser.core.CSeRDBController;
import cser.core.CSeRException;
import cser.track.core.ChangeController;
import cser.utils.PositionUtils;

public class ParentPositionUpdater extends BasePositionUpdater {

	public ParentPositionUpdater(String category, IFile latestFile) {
		super(category, latestFile);
	}

	@Override
	protected void updateLinkedPositions() {
		try {
			Position currentPosition = PositionUtils.extractPositionFrom(getCategory());
//			CSeRDBController.updateParentPositions(file, currentPosition, fPosition);
			CSeRDBController.updateLinkedPositions(file, currentPosition, fPosition);
			updateChangePositions();
		} catch (CSeRException e) {
			e.printStackTrace();
		}
	}

	private void updateChangePositions() {
		ChangeController.updateParentPositions(file, fPosition, fOriginalPosition);
	}

}
