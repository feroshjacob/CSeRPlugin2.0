package cser.ast.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.SelectionProviderAction;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.texteditor.ITextEditor;

import cser.Activator;
import cser.compare.view.CompareUtils;
import cser.core.CSeRClone;
import cser.core.CSeRException;
import cser.utils.WriteText;

public abstract class CSeRMarkerActions extends SelectionProviderAction {

	protected CSeRMarkerActions(ISelectionProvider provider, String text) {
		super(provider, text);
		setEnabled(true);
		// TODO Auto-generated constructor stub
	}

	protected IFile getFile(String filePath) {

		IFile infile = FileBuffers
				.getWorkspaceFileAtLocation(new Path(filePath));
		return infile;
	}

	public void run() {
		try {
			execute();
		} catch (CSeRException ex) {
			WriteText.write(ex.getMessage());
			ex.printStackTrace();
		}
	}

	protected void execute()throws CSeRException {
		// TODO Auto-generated method stub

	}
}

class OpenAction extends CSeRMarkerActions {

	protected OpenAction(ISelectionProvider provider) {
		super(provider, "Open");
		setImageDescriptor(Activator.getImageDescriptor("open.gif"));
		setToolTipText("Open In CnP Editor");

	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute() {

		IStructuredSelection selection = getStructuredSelection();
		
		CSeRClone clone =null;
		if(selection.isEmpty()) return;
		 Object object = selection.getFirstElement();
		 if(!(object instanceof CSeRClone)) return;
			clone = (CSeRClone) object;

			if(clone.getParent()!=null)
	      CompareUtils.openCompareEditor(clone.getFile(), clone.getParent().getFile());
	

	}
}

@SuppressWarnings("unchecked")
class MarkerSelectionProviderAdapter implements ISelectionProvider {

	List listeners = new ArrayList();

	ISelection theSelection = null;

	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		listeners.add(listener);
	}

	public ISelection getSelection() {
		return theSelection;
	}

	public void removeSelectionChangedListener(
			ISelectionChangedListener listener) {
		listeners.remove(listener);
	}

	public void setSelection(ISelection selection) {
		theSelection = selection;
		final SelectionChangedEvent e = new SelectionChangedEvent(this,
				selection);
		Object[] listenersArray = listeners.toArray();

		for (int i = 0; i < listenersArray.length; i++) {
			final ISelectionChangedListener l = (ISelectionChangedListener) listenersArray[i];
			SafeRunner.run(new SafeRunnable() {
				public void run() {
					l.selectionChanged(e);
				}
			});
		}
	}

}
