package cser.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

public class FileUtils{

	public static IFile getFile(String filePath) {

		IFile infile = FileBuffers.getWorkspaceFileAtLocation(new Path(filePath));
		
		return infile;
	}
	


	
	public static char[] getSystemFile(String filePath) {

			File infile = new File(filePath);
			
			FileReader reader;
			char[] chars = new char[(int)infile.length()];
			try {
				reader = new FileReader(infile);
				int i=0;
				int k =reader.read();
				while(k!=-1){
					chars[i]= (char)k;
					i++;
					k = reader.read();
				}
				reader.close();
				
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 return chars;
	 }
	  
	 public static void clearFolder(String strFolder)	  {
		   final String strExt ="java";
	          // Declare variables
	          File fLogDir = new File(strFolder);
	          
	          // Get all BCS clones
	          File[] fLogs = fLogDir.listFiles(new FilenameFilter()
	          {
	                  public boolean accept(File fDir, String strName)
	                  {
	                          return (strName.endsWith("." + strExt));
	                  }
	          });
	          
	          // Delete all clones
	          for (int i = 0; i < fLogs.length; i++)
	          {
	                 fLogs[i].delete();
	          }
	}
	  
	public static IFile getCloneFile(IFile file) {
 
        return getFile(getClonePath(file).toString());
	}
	
	public static IPath getClonePath(IFile file) {
	       StringBuffer buffer = new StringBuffer();
	        buffer.append(file.getFullPath().removeFileExtension());
	        buffer.append(GlobalConfig.CSeRFilesExt);
	        buffer.append(".java");
	        return new Path(buffer.toString());
	}

}