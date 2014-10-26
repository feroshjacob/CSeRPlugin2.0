package cser.editors;

import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
import org.eclipse.ui.texteditor.IWorkbenchActionDefinitionIds;

import cser.file.CSeRFileListener;
import cser.region.CSeRRegionListener;

@SuppressWarnings("restriction")
public class CSeREditor extends CompilationUnitEditor {

	IAction showTrackingAction = null;
	private BaseDocumentListener fileListener = null;
	private BaseDocumentListener regionListener = null;
	private IDocument document=null;

	public BaseDocumentListener getDocumentListener() {
		if (regionListener.getCurrentClone() != null)
			return regionListener;
		return fileListener;
	}

	@Override
	protected void createActions() {
		super.createActions();
		document = getDocumentProvider()
				.getDocument(getEditorInput());
		fileListener = new CSeRFileListener(this);
		document.addDocumentListener(fileListener);
		regionListener = new CSeRRegionListener(this);
		document.addDocumentListener(regionListener);

		IAction copy = new CopyAction(
				getAction(ITextEditorActionConstants.COPY));
		copy.setActionDefinitionId(IWorkbenchActionDefinitionIds.COPY);
		setAction(ITextEditorActionConstants.COPY, copy);

		IAction paste = new PasteAction(
				getAction(ITextEditorActionConstants.PASTE));
		paste.setActionDefinitionId(IWorkbenchActionDefinitionIds.PASTE);
		setAction(ITextEditorActionConstants.PASTE, paste);
	}

}
