package cser.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jface.text.Position;

/**
 * Currently, this class represents a cloned file.
 * It should be extended with a position that specifies the range of code that forms a clone.
 * @author dhou
 *
 */
public class CSeRClone {
	public CSeRClone(IFile file) {
		this.file = file;
		fileName = this.file.getName();
	}

	private IFile file = null;
	private CSeRClone parent = null;
	private String fileName = "";
	private CheckPositions checkPositions = new CheckPositions();
	private List<ASTNode> ASTNodes = new ArrayList<ASTNode>();
	private CSeRChanges changes = new CSeRChanges();

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	public IFile getFile() {
		return file;
	}

	public void setFile(IFile file) {
		this.file = file;
	}

	public CSeRClone getParent() {
		return parent;
	}
	public void setParent(CSeRClone parent) {
		this.parent = parent;
	}


	public void setChanges(CSeRChanges changes) {
		this.changes = changes;
	}

	public CheckPositions getCheckPositions() {
		return checkPositions;
	}

//	public void setCheckPositions(CheckPositions checkPositions) {
//		this.checkPositions = checkPositions;
//	}

	/**
	 * 
	 * @param currentPosition: position in the current file (where this clone resides).
	 * @param parentPosition: position in the source clone file.
	 */
	public void addCheckPosition(Position currentPosition, Position parentPosition) {
		final String currentPosAsStr = currentPosition.toString();
		final String parentPosAsStr = parentPosition.toString();
		
		CheckPosition checkPosition = new CheckPosition(currentPosAsStr, parentPosAsStr);
		checkPositions.put(parentPosAsStr, checkPosition);
	}

	public void setASTNodes(List<ASTNode> ASTNodes) {
		if(this.ASTNodes!=null && this.ASTNodes.size() >0) {
			this.ASTNodes.addAll(ASTNodes);
		}
		else 
		this.ASTNodes = ASTNodes;
	}

	public List<ASTNode> getASTNodes() {
		return ASTNodes;
	}

	public void addASTNode(ASTNode astNode) {
		this.ASTNodes.add(astNode);
	}

	public void addChange(String chgmessage,int type, String position,String parentPosition,String fileName) {
		this.changes.addChange(chgmessage,type, position,parentPosition,fileName);
	}
	
	public void addChangeForParent(String chgmessage,int type, String position,String parentPosition,String fileName) {
		this.changes.addChange(chgmessage,type, position,parentPosition,fileName,true);
	}

	public CSeRChanges getChanges() {
		return changes;
	}

}