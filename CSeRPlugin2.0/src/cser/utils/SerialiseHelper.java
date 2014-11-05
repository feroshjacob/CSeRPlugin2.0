package cser.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;


public class SerialiseHelper {
	
	public static void serialize(Serializable object,String fName) {

			try {
				
				ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(
						fName));
				o.writeObject(object);
	            			
				o.close();
			} catch (FileNotFoundException e) {
				// System.out.println("File Not Found for writing: " + fName);
			} catch (IOException e) {
				e.printStackTrace();
			}catch (Exception e) {
			}
			
		}

 
	public static Object deserialize(String fName) {
		try {
			ObjectInputStream i = new ObjectInputStream(new FileInputStream(
					fName));
			
			Object o = i.readObject();
			i.close();
			return o;
		} catch (FileNotFoundException e) {
			// System.out.println("File Not Found for reading: "+ fName);

		} catch (IOException e) {
			System.out.println("File Empty");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
			 
		}
		
		return null;
	}

	
	public static void copy(IFile src) {
	     try {
	    	 IPath path =  FileUtils.getClonePath(src);
			src.copy(path,false, new NullProgressMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	public static void rename(String oldName, String newName) {
	/*	Object obj = deserialize(fileName);
		if(! (obj instanceof List)) return ;
		List<RCFFilePair> rcfFiles = (List<RCFFilePair>)obj;
		CSeRDBController.clearAll();
		for(RCFFilePair pair: rcfFiles){
			try {
				String firstFile=pair.file1;;
				String secondFile=pair.file2;
			
				if(pair.file1.equals(oldName)){
					firstFile = newName;
					pair.setFile1(newName);
				}
				else if(pair.file2.equals(oldName)){
					secondFile = newName;
					pair.setFile2(newName);
				}

				CSeRDBController.initcopyWithoutSave(FileUtils.getFile(firstFile),FileUtils.getFile(secondFile));
				
			} catch (CSeRException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		serialize((Serializable)rcfFiles,fileName);*/
		
		/*
		 * Commented above as this will work without serializing
		 */

	}
}
