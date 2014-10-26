package cser.editors;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.DefaultPositionUpdater;
import org.eclipse.jface.text.DocumentEvent;

public abstract class BasePositionUpdater extends DefaultPositionUpdater{

	protected IFile file =null;
	public BasePositionUpdater(String category, IFile latestFile) {
		super(category);
		this.file = latestFile;
	}
	
	@Override
	public void update(DocumentEvent event) {
		super.update(event);
		updateLinkedPositions();
//		if(fReplaceLength ==0 && PositionUtils.contains(new Position(event.getOffset(),event.getLength()), fOriginalPosition) ){
//			try {
//				CSeRDBController.updateDeletedPositions(file, PositionUtils.getPosition(getCategory()));
//			} catch (CSeRException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
			
	}

	protected abstract void updateLinkedPositions() ;
}
