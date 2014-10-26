package cser.compare.view;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.contentmergeviewer.ITokenComparator;
import org.eclipse.jdt.internal.ui.compare.JavaMergeViewer;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.RuleBasedPartitioner;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.swt.widgets.Composite;

@SuppressWarnings("restriction")
public class CSeRMergeViewer extends JavaMergeViewer {

	public CSeRMergeViewer(Composite parent, int styles, CompareConfiguration mp) {
		super(parent, styles, mp);

	}

	public String getTitle() {
			return "CSeR Clone Compare";
	}
@SuppressWarnings("deprecation")
@Override
protected IDocumentPartitioner getDocumentPartitioner() {

	String[] strings = new String[]{};
 	return new RuleBasedPartitioner(new RuleBasedScanner(),strings);
}


}
