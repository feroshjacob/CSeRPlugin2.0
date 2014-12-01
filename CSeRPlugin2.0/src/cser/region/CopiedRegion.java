package cser.region;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.Position;
import org.eclipse.ui.INullSelectionListener;

/**
 * A global variable ...
 * @author dhou
 *
 */
public class CopiedRegion {
	
	private static CSerCopyInfo copyInfo = null;
	
	public static  void saveInfo(IFile file, Position pos) {
		copyInfo = (new CopiedRegion()).new CSerCopyInfo(file, pos);
	}
	
	/*
	public static Position getPosition() {
		return copyInfo.getPosition();
	}*/
	
	public static Position getPosition() {
		if (copyInfo!=null)
		return copyInfo.getPosition();
		else
			return null;
	}
	
	public static IFile getFile() {
		return copyInfo.getFile();
	}
	  
	class CSerCopyInfo {
		private IFile file = null;
		private Position position =null;
		public CSerCopyInfo(IFile file,Position position){
			setFile(file);
			setPosition(position);
		}
		public void setFile(IFile file) {
			this.file = file;
		}
		public IFile getFile() {
			return file;
		}
		public void setPosition(Position position) {
			this.position = position;
		}
		public Position getPosition() {
			return position;
		}
	}
	

	public static boolean	isNull() {
		return (copyInfo==null);	
	}

}
