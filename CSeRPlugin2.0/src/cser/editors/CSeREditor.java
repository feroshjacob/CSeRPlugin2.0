package cser.editors;

import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IWorkbenchCommandConstants;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
import org.eclipse.ui.texteditor.IWorkbenchActionDefinitionIds;

import cser.region.CSeRRegionListener;

@SuppressWarnings("restriction")
public class CSeREditor extends CompilationUnitEditor {

	IAction showTrackingAction = null;
	private BaseDocumentListener regionListener = null;
	private IDocument document=null;

	public BaseDocumentListener getDocumentListener() {
	
			return regionListener;
		
	}
 
	@Override
	protected void createActions() {
		super.createActions();
		document = getDocumentProvider()
				.getDocument(getEditorInput());
		regionListener = new CSeRRegionListener(this);
		document.addDocumentListener(regionListener);

		IAction copy = new CopyAction(
				getAction(ITextEditorActionConstants.COPY));
		copy.setActionDefinitionId(IWorkbenchActionDefinitionIds.COPY); //deprecated, replaced by new
		//copy.setActionDefinitionId(IWorkbenchCommandConstants.EDIT_COPY);
		setAction(ITextEditorActionConstants.COPY, copy);
		System.out.println("Copy action created:"+copy.toString());

		IAction paste = new PasteAction(
				getAction(ITextEditorActionConstants.PASTE));
		paste.setActionDefinitionId(IWorkbenchActionDefinitionIds.PASTE); //deprecated, replaced by new
		//paste.setActionDefinitionId(IWorkbenchCommandConstants.EDIT_PASTE);
		setAction(ITextEditorActionConstants.PASTE, paste);
		System.out.println("Paste action created:"+paste.toString());
	}

}
