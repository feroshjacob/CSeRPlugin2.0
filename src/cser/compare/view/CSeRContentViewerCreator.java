package cser.compare.view;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.IViewerCreator;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class CSeRContentViewerCreator implements IViewerCreator {

	public Viewer createViewer(Composite parent, CompareConfiguration mp) {
		return new CSeRMergeViewer(parent, SWT.NULL, mp);
	}
	
}
