package cser.region;

import org.eclipse.jface.text.Position;
import org.eclipse.ui.texteditor.ITextEditor;

import cser.core.CSeRClone;
import cser.core.CSeRDBController;
import cser.core.CSeRException;
import cser.core.CheckPositions;
import cser.editors.BaseDocumentListener;
import cser.editors.PasteAction;

/**
 * this is for copy-and-paste of code fragments smaller than a file?
 * @author dhou
 *
 */
public class CSeRRegionListener extends BaseDocumentListener {

	public CSeRRegionListener(ITextEditor editor) {
		super(editor);
	}

	protected void execute(Position event) throws CSeRException {
		if(PasteAction.pasted==1  && !CopiedRegion.isNull()){
			Position pos = new Position(event.getOffset(), CopiedRegion.getPosition().getLength());
			// this is a dirty trick to stop creating new clones inside existing clones?
			CSeRClone copyRoot = CSeRDBController.getSourceCloneObject(fileInEditor);
			if (copyRoot!=null && CSeRDBController.getTargetClones(copyRoot.getFile()).size() >0) {
				return;
			}
			
			CSeRDBController.createClonePair(CopiedRegion.getFile(), fileInEditor, CopiedRegion.getPosition(), pos);
			CheckPositions checkPositions = CSeRDBController.getLinkedPositions(fileInEditor);
			addPositionUpdater(checkPositions);
			peerClone = CSeRDBController.getCloneObject(fileInEditor);
			currentClone = peerClone;
			}
		else super.execute(event);
	}

	protected void registerUpdatedPostions() throws CSeRException {
		
	}

}
