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
	 System.out.println("CSeRRegionListener() called");
		
	}

	protected void execute(Position event) throws CSeRException {
		System.out.println("regionListener execute() called- PasteAction.pasted="+PasteAction.pasted+" "+CopiedRegion.getPosition().offset+" "+CopiedRegion.getPosition().length);
		System.out.println("execute() called with position:"+event);
		if(PasteAction.pasted==1  && !CopiedRegion.isNull()){
			//Position pos = new Position(event.getOffset(), CopiedRegion.getPosition().getLength());
			Position pos = new Position(event.getOffset()+(event.getLength()-CopiedRegion.getPosition().getLength()), CopiedRegion.getPosition().getLength()); //SR: position adjusted because the first node was missing in the AST for clone
			System.out.println("Created position is:"+pos);
			// this is a dirty trick to stop creating new clones inside existing clones?
			CSeRClone copyRoot = CSeRDBController.getSourceCloneObject(fileInEditor);
			if (copyRoot!=null && CSeRDBController.getTargetClones(copyRoot.getFile()).size() >0) {
				System.out.println("execute() returned by force");
				return;
			}
			
			CSeRDBController.createClonePair(CopiedRegion.getFile(), fileInEditor, CopiedRegion.getPosition(), pos);
			CheckPositions checkPositions = CSeRDBController.getLinkedPositions(fileInEditor);
			addPositionUpdater(checkPositions);
			peerClone = CSeRDBController.getCloneObject(fileInEditor);
			currentClone = peerClone;
			System.out.println("Pasted and clone recorded");
			}
		else{
			System.out.println("else part called with super.execute() in regionListener");
			super.execute(event);
		}
	}

	protected void registerUpdatedPostions() throws CSeRException {
		
	}

}
