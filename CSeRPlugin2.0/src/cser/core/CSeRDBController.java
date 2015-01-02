package cser.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.Position;

import cser.ast.MainVisitor;
import cser.ast.PartVisitor;
import cser.utils.FileUtils;
import cser.utils.ParseHelper;
import cser.utils.SerialiseHelper;

public class CSeRDBController {
	private static Map<String, CSeRClone> clones = new HashMap<String, CSeRClone>();

	public static Map<String, CSeRClone> getFiles() throws CSeRException {
		return clones;
	}

	public static void clearAll() {
		clones = new HashMap<String, CSeRClone>();
	}

	public static boolean UNCOMPILABLE_CODE = false;

	public static CSeRClone getCloneObject(IFile file) throws CSeRException {

		String filePath = file.getFullPath().toString();
		if (!clones.containsKey(filePath))
			return null;
		return clones.get(filePath);
	}

	// Returns the original/source clone of a copy-paste clone pair in a file
	//TODO This seems restricted to one clone-pair per file, need extension???
	public static CSeRClone getSourceCloneObject(IFile file) throws CSeRException {
		String filePath =file.getFullPath().toString();
		if (!clones.containsKey(filePath)) // if not in the clone list
			return null;
		CSeRClone file2 = clones.get(filePath); // get the clone from the list
		if (file2 == null)
			return null;
		return file2.getParent();
	}

	/**
	 * Gets all the clones that have the parameter file as its source.
	 * @param file
	 * @return
	 */
	 public static List<CSeRClone> getTargetClones(IFile file) { 
		 List<CSeRClone> linkedClones = new ArrayList<CSeRClone>();
		  for(String filePath :clones.keySet()){
			  CSeRClone clone = clones.get(filePath);
			  CSeRClone parent = clone.getParent();
			  if(parent!=null && parent.getFile().equals(file)) {
				  linkedClones.add(clone);
			  }
		  }
		  return linkedClones;
	 }
     


	
	/**
	 * Update the position corresponds to parentPosition to newPosition.
	 * @param file
	 * @param parentPosition
	 * @param newPosition
	 * @throws CSeRException
	 */
	public static void updateLinkedPositions(IFile file,
			Position parentPosition, Position newPosition) throws CSeRException {

		CSeRClone clone = getCloneObject(file);
		if (clone == null) {
			throw new CSeRException("The File doesn't exist in DB");
		}
		clone.getCheckPositions().updateCurrentPosition(parentPosition,
				newPosition);

	}
	
	/**
	 * Update from currentPosition to newPosition for the clones that has the parameter file as its source.
	 * 
	 * Note that the for loop below may not be needed as it does not make sense for multiple clones to have the same currentPosition and newPosition.
	 *  
	 * @param file
	 * @param currentPosition
	 * @param newPosition
	 * @throws CSeRException
	 */
	public static void updateParentPositions(IFile file,
			Position currentPosition, Position newPosition) throws CSeRException {

		CSeRClone file2 = getCloneObject(file);
		if (file2 == null) {
			throw new CSeRException("The File doesn't exist in DB");
		}
		
		// This is unnecessarily general!
		List<CSeRClone> clones = CSeRDBController.getTargetClones(file);
		for(CSeRClone clone: clones){
		   clone.getCheckPositions().updateParentPosition(currentPosition, newPosition);
		 }
	}

	/**
	 * Marks the tracked position corresponding to originalPosition as being Deleted.
	 * @param file
	 * @param orginalPosition
	 * @throws CSeRException
	 */
	public static void updateDeletedPositions(IFile file,
			Position orginalPosition) throws CSeRException {

		CSeRClone clone = getCloneObject(file);
		if (clone == null) {
			throw new CSeRException("The File doesn't exist in DB");
		}
		clone.getCheckPositions().updateDeletedPosition(orginalPosition);

	}

	/**
	 * Obtains all the tracked positions for the parameter file.
	 * @param file
	 * @return
	 * @throws CSeRException
	 */
	public static CheckPositions getLinkedPositions(IFile file)
			throws CSeRException {
		CSeRClone clone = getCloneObject(file);
		if (clone == null) {
			throw new CSeRException("The File doesn't exist in DB");
		}
		return clone.getCheckPositions();

	}
	 /**
	  * Records the fact that <file2, pos2> is a clone of <file1, pos1>.
	  */
	public static void createClonePair(IFile file1, IFile file2,
			Position pos1, Position pos2) throws CSeRException {
		
		if(file1==null || file2 ==null) {
			return;
		}
		
		System.out.println("creating clone pair for source in: "+file1.getName()+ " and pos: "+ pos1);
		System.out.println("copy in: "+file2.getName()+ " and pos: "+ pos2);
	    CSeRClone clone1 = createCloneObject(file1, pos1);// original copy
	    CSeRClone clone2 = createCloneObject(file2, pos2); // copy-pasted block

	    System.out.println("In createClonePair() AST in source#:"+clone1.getASTNodes().size());
	    System.out.println("In createClonePair() AST in clone#:"+clone2.getASTNodes().size());
	    clone2.setParent(clone1); // set original as parent of cloned block
	    
	    System.out.println("Again AST in clone#:"+clone2.getASTNodes().size());
		// Do we need to differentiate?? seems we don't
	    /*
	    if(file1.equals(file2)){
			linkClonePairFromSameFile(clone2); // clones from same file
					}
		else {
			linkClonePairFromDifferentFiles(clone1, clone2);  // clones from different files
		}
		*/
	    // Both for clones from same or different files for copied blocks(methods)
	    linkClonePairFromDifferentFiles(clone1, clone2);
	}
	public static ASTNode getJavaElement(IFile file, Position position)
			throws CSeRException {

		PartVisitor visitor;
		try {
			ICompilationUnit unit = JavaCore.createCompilationUnitFrom(file);
			visitor = new PartVisitor(position);
			CompilationUnit cu = ParseHelper.parse(unit);
			UNCOMPILABLE_CODE = false;
			IProblem problems[] = cu.getProblems();
			for(int i=0; i < problems.length ; i++){
				 if (problems[i].isError()){
					 UNCOMPILABLE_CODE = true;
					 break;
				 }
	
			}
			cu.accept(visitor);
			return visitor.getNode();
				
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new CSeRException(e.getMessage());
		}

	}
	
	/*
	 // original version
	private static CSeRClone createCloneObject(IFile file, Position pos) throws CSeRException {

		MainVisitor visitor = (MainVisitor) ParseHelper.visit(file, new MainVisitor(pos));
		System.out.println("Number of AST Nodes in clone["+pos+"]:"+visitor.getASTNodes().size());
		CSeRClone file2 = getCloneObject(file); // load if the clone is already in the list 
		if(file2 ==null) {  // if not in the clone list, add it to the clone list
			file2 = new CSeRClone(file);
			file2.setFile(file);
			clones.put(file.getFullPath().toString(), file2);
			System.out.println("Clone added in the list");
		}
		file2.setASTNodes(visitor.getASTNodes()); // set the ASTNodes for the clone from the visitors
		System.out.println("In createCloneObject() Nodes added#:"+file2.getASTNodes().size());
		return file2;

	}
	 */

	
	// this new version does not look for any previous copy of clone for the same file
	private static CSeRClone createCloneObject(IFile file, Position pos) throws CSeRException {

		MainVisitor visitor = (MainVisitor) ParseHelper.visit(file, new MainVisitor(pos));
		System.out.println("Number of AST Nodes in clone["+pos+"]:"+visitor.getASTNodes().size());
		CSeRClone clone = new CSeRClone(file);
		clone.setFile(file);
		clones.put(file.getFullPath().toString(), clone); // add to list, keys need to be modified to differentiate between clones
		System.out.println("Clone added in the list");
		
		clone.setASTNodes(visitor.getASTNodes()); // set the ASTNodes for the clone from the visitors
		System.out.println("In createCloneObject() Nodes added#:"+clone.getASTNodes().size());
		return clone;

	}
	
	
	private static void linkClonePairFromDifferentFiles(CSeRClone source, CSeRClone target) throws CSeRException {
        
		if (target == null || source==null) {
			throw new CSeRException("Either of clones  cannot be null");
		}	
	
		int count = 0, size1, size2, size;
		
		final List<ASTNode> sourceNodes = source.getASTNodes();
		final List<ASTNode> targetNodes = target.getASTNodes();
		System.out.println("AST Nodes in source #: "+sourceNodes.size());
		System.out.println("AST Nodes in target #: "+targetNodes.size());
		//showASTNodes(source,target);
		size1=sourceNodes.size();
		size2=targetNodes.size();
		// in case of size mismatch avoid invalid indexing occurred in some cases, 
		if(size1<size2)
			size=size1;
			else 
			size=size2;
		
		System.out.println("Linking clones from different files.");
		
		while (count < size) {
			ASTNode sourceNode = sourceNodes.get(count);
			Position sourcePosition = new Position(sourceNode.getStartPosition(), sourceNode.getLength());
		
			ASTNode targetNode = targetNodes.get(count);
			Position currentPosition = new Position(targetNode.getStartPosition(), targetNode.getLength());
			
			target.addCheckPosition(currentPosition, sourcePosition);
			System.out.println("source: "+count + ")" + sourceNode +"[" + sourcePosition + "]");
			System.out.println("clone: "+count + ")" + targetNode +"[" + currentPosition + "]");
			++count;
		}
	}
   
	// for debugging purpose display the corresponding ASTNodes in a clone pair
	public static void showASTNodes(CSeRClone clone1, CSeRClone clone2){

		int size1,size2, size;
		
		final List<ASTNode> sourceNodes = clone1.getASTNodes();
		final List<ASTNode> targetNodes = clone2.getASTNodes();
		size1=sourceNodes.size();
		size2=targetNodes.size();
		
		System.out.println("AST Nodes in source #: "+size1);
		System.out.println("AST Nodes in target #: "+size2);
		
		if (size1<size2)
			size=size1;
		else
			size=size1;
			
		
		for( int i=0; i<size; i++){
			System.out.println(i+") source:["+sourceNodes.get(i).getStartPosition()+","+sourceNodes.get(i).getLength()+"]");
			System.out.println(i+") target:["+targetNodes.get(i).getStartPosition()+","+targetNodes.get(i).getLength()+"]");
		}
	}
	
	// SR: Seems that the following is for the case when a whole class is copied in the same file
	private static void linkClonePairFromSameFile(CSeRClone file2) throws CSeRException {
		int SIZE = file2.getASTNodes().size()/2;
		int count =0;
		
		while (count < SIZE) {
			ASTNode nodeInSource = file2.getASTNodes().get(count); // get an ASTNode from the node-list for a clone 
			Position position = new Position(nodeInSource.getStartPosition(), nodeInSource.getLength()); // the position of the node
	
			ASTNode nodeInClone = file2.getASTNodes().get(count+SIZE); // ?? 
			Position updatedPosition = new Position(nodeInClone.getStartPosition(), nodeInClone.getLength());
			
			System.out.println("Source:"+position+" clone:"+updatedPosition);
			file2.addCheckPosition(updatedPosition, position);
			
			System.out.println(count + ")" + nodeInSource + "[" + updatedPosition + "]");
			
			++count;
	
		}
   }
	
}
