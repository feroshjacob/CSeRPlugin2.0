package cser.track.core.deleteinsert.type;

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.dom.ASTMatcher;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.IAnnotationModel;

import cser.ast.BaseVisitor;
import cser.ast.TypeVisitor;
import cser.core.CSeRChange;
import cser.core.CSeRChanges;
import cser.core.CSeRClone;
import cser.core.CSeRDBController;
import cser.track.core.deleteinsert.IDCalculator;
import cser.track.core.deleteinsert.IIDController;
import cser.utils.PositionList;
import cser.utils.PositionUtils;


public class TypeCalculator extends IDCalculator {

	private BaseVisitor currentVisitor =null;
	private BaseVisitor parentVisitor =null;
	protected String annotationType = "cserstmtinsert";

	public TypeCalculator(IAnnotationModel model, IFile file,
			CSeRClone currentCSeRFile) {
		super(model, file, currentCSeRFile);
	}

	protected BaseVisitor getCurrentVisitor() {
		if(currentVisitor==null)
			currentVisitor = new TypeVisitor(new Position(currentNode.getStartPosition(),currentNode.getLength()));
		return currentVisitor;
	}
	protected BaseVisitor getParentVisitor() {
		if(parentVisitor==null)
			parentVisitor = new TypeVisitor(new Position(dbNode.getStartPosition(),dbNode.getLength()));
		return parentVisitor;
	}

	public void refreshMarkers(ASTNode dbNode, ASTNode currentNode) {
		this.currentNode = currentNode;
		this.dbNode = dbNode;
		createInsertDeleteAnnotations(getCurrentVisitor(), getParentVisitor());
	}

	@Override
	public boolean initialCheck() {
	 	removeAllFieldLevelChanges();
		if(dbNode.subtreeMatch(new ASTMatcher(), currentNode)) {
			return false;
		}
	 	TypeDeclaration  dbType = (TypeDeclaration)dbNode;
	 	TypeDeclaration  currentType = (TypeDeclaration)currentNode;

		return true;
	}

	private void removeAllFieldLevelChanges() {
		CSeRChanges changes = currentCSeRFile.getChanges();
		Iterator<CSeRChange> iterator = changes.iterator();
		removeAnnotation(new String[]{annotationForDeleteF,annotationForInsertF,annotationForProcessing});
		while(iterator.hasNext()){
			CSeRChange change = iterator.next();
			if(change.getType()== chgInsert_Field){
				iterator.remove();
			}
			else if(change.getType()== chgDelete_Field){
				iterator.remove();
			}
		}
	}

	@Override
	protected IIDController getController(
			List<ASTNode> insertedNodes, List<ASTNode> deletedNodes) {
		return new TypeController(insertedNodes,deletedNodes);
	}
@Override
protected int getInsertType() {
	return chgInsert_Field;
}

@Override
protected int getDeleteType() {
	return chgDelete_Field;
}
protected String getannotationTypedelete() {
	return annotationForDeleteF;
}

protected String getannotationTypeinsert() {
	return annotationForInsertF;
}

}
