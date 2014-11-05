package test.refactor;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.jdt.core.refactoring.RenameTypeArguments;
import org.eclipse.jdt.internal.corext.refactoring.reorg.JavaCopyProcessor;
import org.eclipse.jdt.internal.ui.refactoring.reorg.NewNameQueries;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.ltk.core.refactoring.participants.CopyRefactoring;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class GroupedUser {
	private Group agroup;
	private User user;
	
	

	public Group getGroup() {
		return agroup;
	}
	public void setGroup(Group group) {
		
		this.agroup = group;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		
		this.user = user;
	}
  void write () {
	  Shell shell=null;
	  JavaCopyProcessor s =null;
	  NewNameQueries n= new NewNameQueries(shell);
	  s.setNewNameQueries(n);
		CopyRefactoring refactoring= new CopyRefactoring(s);
	
  }
  
  
}
