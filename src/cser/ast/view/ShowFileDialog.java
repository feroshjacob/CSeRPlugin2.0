package cser.ast.view;

//Send questions, comments, bug reports, etc. to the authors:

//Rob Warner (rwarner@interspatial.com)
//Robert Harris (rbrt_harris@yahoo.com)

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import cser.utils.GlobalConfig;

/**
 * This class demonstrates FileDialog
 */


public class ShowFileDialog  extends Dialog{
	public static String copeFileName ="";
	public static String pasteFileName="";
	public static String newName="";
	private  String fileNameUnQ ="";
	
  public ShowFileDialog(Shell parentShell) {
		super(parentShell);
		// TODO Auto-generated constructor stub
	}

// These filter names are displayed to the user in the file dialog. Note that
  // the inclusion of the actual extension in parentheses is optional, and
  // doesn't have any effect on which files are displayed.
  private static final String[] FILTER_NAMES = {
      "Java Source (*.java)"};

  // These filter extensions are used to filter which files are displayed.
  private static final String[] FILTER_EXTS = { "*.java"};

  
@Override
	protected Control createContents(Composite shell) {
    shell.setLayout(new GridLayout(3, true));

    new Label(shell, SWT.NONE).setText("File Name:");

    final Text fileName = new Text(shell, SWT.BORDER);
    GridData data = new GridData(GridData.FILL_HORIZONTAL);
    data.horizontalSpan = 2;
    fileName.setLayoutData(data);

    new Label(shell, SWT.NONE).setText("paste File Name:");

    final Text fileName2 = new Text(shell, SWT.BORDER);
    GridData data2 = new GridData(GridData.FILL_HORIZONTAL);
    data2.horizontalSpan = 2;
    fileName2.setLayoutData(data2);
    
    new Label(shell, SWT.NONE).setText("File Name(Paste):");

    final Text pasteFiletext = new Text(shell, SWT.BORDER);
     data = new GridData(GridData.FILL_HORIZONTAL);
    data.horizontalSpan = 2;
    pasteFiletext.setLayoutData(data);


    Button open = new Button(shell, SWT.PUSH);
    open.setText("Open...");
    open.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent event) {
        // User has selected to open a single file
        FileDialog dlg = new FileDialog(getShell(), SWT.OPEN);
        dlg.setFilterNames(FILTER_NAMES);
        dlg.setFilterExtensions(FILTER_EXTS);
        String fn = dlg.open();
    	fileNameUnQ = dlg.getFileName();
        if (fn != null) {
          fileName.setText(fn);
        }
      }
    });

    Button save = new Button(shell, SWT.PUSH);
    save.setText("Save...");
 
    save.addSelectionListener(new SelectionAdapter() {
        public void widgetSelected(SelectionEvent event) {

      	  copeFileName = fileName.getText();
      	  pasteFileName = pasteFiletext.getText();
      	  newName = fileName2.getText();
      	  if(MessageDialog.openConfirm(getShell(), "Copy-PasteInformation", "Copy File:" + fileNameUnQ +" Paste File:" + pasteFileName))
      		  close();
      	 
        }
      });
     

    Button open1 = new Button(shell, SWT.PUSH);
    open1.setText("Open...");
    open1.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent event) {
        // User has selected to open a single file
        DirectoryDialog dlg = new DirectoryDialog(getShell(), SWT.OPEN);
          
        String configPath = GlobalConfig.rootdir+ GlobalConfig.seperator+ GlobalConfig.projectName+
          GlobalConfig.seperator+ GlobalConfig.src_folder+ GlobalConfig.src_folder;
        dlg.setFilterPath(configPath);
  /*      dlg.setFilterNames(FILTER_NAMES);
        dlg.setFilterExtensions(FILTER_EXTS);
  */      String fn = dlg.open();
  	
        if (fn != null) {
        	pasteFiletext.setText(fn+"/");
        }
      }
    });

  
    return shell;
  }


public String getFileNameUnQ() {
	return fileNameUnQ;
}


public void setFileNameUnQ(String fileNameUnQ) {
	this.fileNameUnQ = fileNameUnQ;
}

  /**
   * The application entry point
   * 
   * @param args the command line arguments
   */
 
}
