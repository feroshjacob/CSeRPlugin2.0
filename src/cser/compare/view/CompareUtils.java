package cser.compare.view;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareEditorInput;
import org.eclipse.compare.CompareUI;
import org.eclipse.compare.IStreamContentAccessor;
import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.structuremergeviewer.DiffNode;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.graphics.Image;

import cser.core.CSeRClone;
import cser.core.CSeRDBController;
import cser.core.CSeRException;



public class CompareUtils {
	
	public static void openCompareEditor(final IFile left,final  IFile right) {

		CompareUI.openCompareDialog(new CompareEditorInput(
				new CompareConfiguration()) {

			protected Object prepareInput(IProgressMonitor monitor)
					throws InvocationTargetException, InterruptedException {
				return new DiffNode(getTypeElement(left), getTypeElement(right));
			}
		});

	}
	private static ITypedElement getTypeElement(IFile file) {

		return new CSerNode(file);
	}

	static class CSerNode implements ITypedElement, IStreamContentAccessor {

		private IFile file;

		public CSerNode(IFile file) {
			this.file = file;
		}

		public String getType() {
			return "cser.compare.view.CSeRContentViewerCreator".toUpperCase(); //$NON-NLS-1$
		}

		public Image getImage() {
			return CompareUI.getImage(getType());
		}

		public String getName() {
			return file.getName();
		}

		public InputStream getContents() throws CoreException {
			return file.getContents();
		}
	}
	public static Object getClones() {
		 try {
			Map<String, CSeRClone> files = CSeRDBController.getFiles();
			List<CSeRClone> clones = new ArrayList<CSeRClone>();
			for(String key :files.keySet()){
				if(files.get(key).getParent()!=null){
				clones.add(files.get(key));
				clones.add(files.get(key).getParent());
				}
			}
			return  clones;
		} catch (CSeRException e) {
			e.printStackTrace();
		}
		 
		return null;
	}
}
