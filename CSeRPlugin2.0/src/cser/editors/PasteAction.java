package cser.editors;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.widgets.Event;



public class PasteAction extends Action {
	private IAction oldPasteAction;
	
	/**
	 * 0: doc changed due to actions other than paste
	 * 1: doc change due to paste
	 * >=2: doc change like `import' as a result of a pasting
	 */
	public static int pasted=0;
		
	public PasteAction(IAction oldPasteAction){
		this.oldPasteAction = oldPasteAction; 
		this.setText(oldPasteAction.getText());
		//System.out.println("PasteAction constructor called");
	}
	public void runWithEvent(Event e){
		//System.out.println("PasteAction.runWithEvent() called");
		pasted = 1; 
		oldPasteAction.runWithEvent(e);
		pasted = 0; //SR
	}

}