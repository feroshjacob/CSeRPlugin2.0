package cser.ast.view;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.internal.corext.util.Messages;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.preferences.PreferencesMessages;
import org.eclipse.jdt.internal.ui.text.template.contentassist.TemplateEngine;
import org.eclipse.jdt.internal.ui.text.template.preferences.TemplateVariableProcessor;
import org.eclipse.jdt.internal.ui.viewsupport.ProjectTemplateStore;
import org.eclipse.jdt.internal.ui.wizards.IStatusChangeListener;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.templates.persistence.TemplatePersistenceData;
import org.eclipse.jface.text.templates.persistence.TemplateReaderWriter;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.preferences.IWorkbenchPreferenceContainer;

import cser.Activator;
import cser.core.CSeRChange;
import cser.core.CSeRChanges;
import cser.core.CSeRDBController;
import cser.core.CSeRException;
import cser.editors.CSeREditor;
import cser.file.CSeRFileListener;
import cser.track.core.ChangeController;
import cser.utils.FileUtils;
import cser.utils.GlobalConfig;

public class CSeRViewActions {
	

}
class DeleteAcion extends Action { 
	  

    /**
     * Creates the action
     */
    public DeleteAcion() {
        super("Delete");
        setImageDescriptor(Activator.getImageDescriptor("delete.gif"));
        setToolTipText("Delete");
        setEnabled(true);
    }

    public void run() {
    	CSeRDBController.clearAll();
        ChangeController.clearChanges();
   //     FileUtils.clearFolder(GlobalConfig.db_folder+GlobalConfig.seperator+"changes");
    }
}
class ShowGroupsAction extends Action { 
	  

    /**
     * Creates the action
     */
    public ShowGroupsAction() {
        super("Show Groups");
        setImageDescriptor(Activator.getImageDescriptor("meBook.gif"));
        setToolTipText("Show Groups");
        setEnabled(true);
    }

    public void run() {
    	/*
       FileDialog dialog = new FileDialog(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().Activator.getShell());
   dialog.open();
      */
     IEditorPart editorPart=Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
       
     CSeRChanges changes = ChangeController.loadChanges((( IFileEditorInput) editorPart.getEditorInput()).getFile() );
     for(CSeRChange change : changes){
    	   System.out.println("s_pos:" + change.getPosition() +", msg:" + change.getMessage() + ",p_pos:" + change.getParentPosition());  
     }   
    }
}


 
 class TrackAction extends Action { 
	  

    /**
     * Creates the action
     */
    public TrackAction() {
        super("Track");
        setImageDescriptor(Activator.getImageDescriptor("track.gif"));
        setToolTipText("track");
        setEnabled(true);
    }

    /**
     * Opens the dialog. Notifies the view if the filter has been modified.
     */
    public void run() {
    	   IEditorPart editorPart=	Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
    	   try {
    		   if(editorPart instanceof CSeREditor){
    			   IFileEditorInput editorInput = ( IFileEditorInput) editorPart.getEditorInput();
    			   CSeRDBController.createClonePair(editorInput.getFile()) ;
    			   CSeREditor editor = (CSeREditor) editorPart;
    			   IDocument document = editor.getDocumentProvider().getDocument(editorInput);
    			   document.addDocumentListener(new CSeRFileListener(editor));
    		   }
		} catch (CSeRException e) {
				e.printStackTrace();
		} 	

    }
}
 
 class CopyPasteAction extends Action { 
	  

		private static ProjectTemplateStore fTemplateStore;
	
	    /**
	     * Creates the action
	     */
	    public  CopyPasteAction () {
	        super("Add template");
	        setImageDescriptor(Activator.getImageDescriptor("refresh.png"));
	        setToolTipText("Copy Paste ");
	        setEnabled(true);
	    }

		/**
	     * Opens the dialog. Notifies the view if the filter has been modified.
	     */
	    public void run() {
	    
	    } 		
	}
 
 