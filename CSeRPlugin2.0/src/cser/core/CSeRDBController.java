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

	public static CSeRClone getSourceCloneObject(IFile file) throws CSeRException {
		String filePath =file.getFullPath().toString();
		if (!clones.containsKey(filePath))
			return null;
		CSeRClone file2 = clones.get(filePath);
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
		
	    CSeRClone clone1 = createCloneObject(file1, pos1);
	    CSeRClone clone2 = createCloneObject(file2, pos2);

	    clone2.setParent(clone1);

		if(file1.equals(file2)){
			linkClonePairFromSameFile(clone2);
		}
		else {
			linkClonePairFromDifferentFiles(clone1, clone2);
		}
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
	private static CSeRClone createCloneObject(IFile file, Position pos) throws CSeRException {

		MainVisitor visitor = (MainVisitor) ParseHelper.visit(file, new MainVisitor(pos));

		CSeRClone file2 = getCloneObject(file);  
		if(file2 ==null) {
			file2 = new CSeRClone(file);
			file2.setFile(file);
			clones.put(file.getFullPath().toString(), file2);
		}
		file2.setASTNodes(visitor.getASTNodes());
		
		return file2;

	}
	private static void linkClonePairFromDifferentFiles(CSeRClone source, CSeRClone target) throws CSeRException {

		if (target == null || source==null) {
			throw new CSeRException("Either of clones  cannot be null");
		}
				
	
		int count = 0;
		
		final List<ASTNode> sourceNodes = source.getASTNodes();
		final List<ASTNode> targetNodes = target.getASTNodes();
		
		while (count < sourceNodes.size()) {
			ASTNode sourceNode = sourceNodes.get(count);
			Position sourcePosition = new Position(sourceNode.getStartPosition(), sourceNode.getLength());
		
			ASTNode targetNode = targetNodes.get(count);
			Position currentPosition = new Position(targetNode.getStartPosition(), targetNode.getLength());
			
			target.addCheckPosition(currentPosition, sourcePosition);
			System.out.println(count + ")" + sourceNode + "[" + currentPosition
					+ "]");
		
			++count;
		}
	}
   private static void linkClonePairFromSameFile(CSeRClone file2) throws CSeRException {
		int SIZE = file2.getASTNodes().size()/2;
		int count =0;
		
		while (count < SIZE) {
			ASTNode nodeInSource = file2.getASTNodes().get(count);
			Position position = new Position(nodeInSource.getStartPosition(), nodeInSource.getLength());
	
			ASTNode nodeInClone = file2.getASTNodes().get(count+SIZE);
			Position updatedPosition = new Position(nodeInClone.getStartPosition(), nodeInClone.getLength());
			
			file2.addCheckPosition(updatedPosition, position);
			
			System.out.println(count + ")" + nodeInSource + "[" + updatedPosition + "]");
			
			++count;
	
		}
   }
}
