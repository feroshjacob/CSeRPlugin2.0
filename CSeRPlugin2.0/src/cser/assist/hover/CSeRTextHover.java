package cser.assist.hover;

import java.util.List;

import org.eclipse.jdt.internal.ui.text.java.hover.SourceViewerInformationControl;
import org.eclipse.jdt.ui.text.java.hover.IJavaEditorTextHover;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHoverExtension;
import org.eclipse.jface.text.ITextHoverExtension2;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.Region;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.IWorkbenchPartOrientation;

import cser.core.CSeRChange;
import cser.core.CSeRChanges;
import cser.core.CheckPosition;
import cser.editors.CSeREditor;
import cser.track.core.ChangeController;

public class CSeRTextHover implements IJavaEditorTextHover ,ITextHoverExtension, ITextHoverExtension2  {
	
	CSeREditor editor = null;

	public void setEditor(IEditorPart editor) {
		if(editor instanceof CSeREditor){
		 this.editor = (CSeREditor) editor;
		}

	}

	/*
	// original
	public String getHoverInfo(ITextViewer textViewer, IRegion hoverRegion) {
		CSeRChanges changes = ChangeController.loadChanges(((FileEditorInput) editor.getEditorInput()).getFile());
		if(changes==null) return "";
		CSeRChange change = changes.filterChanges(new Position(hoverRegion.getOffset(),hoverRegion.getLength()).toString());
		if(change ==null) return "";
			return change.getMessage();
	}

	public IRegion getHoverRegion(ITextViewer textViewer, int offset) {
		CheckPosition checkPosition= editor.getDocumentListener().getCurrentClone()
		.getCheckPositions().getCurrentPosition(new Position(offset,0));
		if(checkPosition!=null) {
			Position position = checkPosition.getCurrentPosition();
			System.out.println(position);
			return new Region(position.offset, position.length);
		}
		return null;
		
	}
 */
	
	public String getHoverInfo(ITextViewer textViewer, IRegion hoverRegion) {
		String retStr=new String("");
		try{
		
		if(editor==null){
			//System.out.println("Editor is null, return getHoverInfo() with null");
			return retStr;
		}
		else{
			//System.out.println("Editor is not null, continue..");
		}
		
		if(hoverRegion.getLength()==0){
			//System.out.println("region is null, !return getHoverInfo() with null "+hoverRegion.getOffset()+" "+hoverRegion.getLength());
			//return retStr;
		}
		else{
			//System.out.println("region is not null (length="+hoverRegion.getLength()+") continue..");
		}
		
		CSeRChanges changes = ChangeController.loadChanges(((FileEditorInput) editor.getEditorInput()).getFile());
		//System.out.println("After loadChanges(): offset:"+hoverRegion.getOffset()+"- file: "+ ((FileEditorInput) editor.getEditorInput()).getFile());
		if(changes==null) retStr= "";
		CSeRChange change = changes.filterChanges(new Position(hoverRegion.getOffset(),hoverRegion.getLength()).toString());
		if(change ==null) retStr= "";
		
		//System.out.println("After change");
		
		if(change !=null){		
			retStr= change.getMessage();
		
		}
		else{
			//System.out.println("change is null in getHoverInfo()");
		}
		
		}catch(Exception e){
			//System.out.println("Exception in getHoverInfo()");
			e.printStackTrace();
		}
		return retStr;
	}

	public IRegion getHoverRegion(ITextViewer textViewer, int offset) {
		try{
		CheckPosition checkPosition= editor.getDocumentListener().getCurrentClone()
				.getCheckPositions().getCurrentPosition(new Position(offset,0));
		if(checkPosition!=null) {
			Position position = checkPosition.getCurrentPosition();
			//System.out.println("Position for hover region:"+position);
			return new Region(position.offset, position.length);
		}
		}catch(Exception e){
			System.out.println(e.toString());
		}
		
		return null;
		
	}
	public IInformationControlCreator getHoverControlCreator() {
		return new IInformationControlCreator() {
			public IInformationControl createInformationControl(Shell parent) {
				IEditorPart editor= getEditor();
				int orientation= SWT.NONE;
				if (editor instanceof IWorkbenchPartOrientation)
					orientation= ((IWorkbenchPartOrientation) editor).getOrientation();
				return new SourceViewerInformationControl(parent, false, orientation, EditorsUI.getTooltipAffordanceString());
			}

		};
	}

	public Object getHoverInfo2(ITextViewer textViewer, IRegion hoverRegion) {
		//System.out.println(" getHoverInfo2() called in CSeRTextHover with region length "+ hoverRegion.getLength());
		return getHoverInfo(textViewer, hoverRegion);
	}
	private IEditorPart getEditor() {
		return editor;
	}


}
