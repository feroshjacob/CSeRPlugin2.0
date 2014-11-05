package cser.track.core.deleteinsert.node;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.dom.ASTMatcher;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.IAnnotationModel;

import cser.ast.BaseVisitor;
import cser.ast.NodeVisitor;
import cser.ast.StatementVisitor;
import cser.core.CSeRChange;
import cser.core.CSeRChanges;
import cser.core.CSeRClone;
import cser.core.CSeRDBController;
import cser.track.core.IMessages;
import cser.track.core.deleteinsert.IDCalculator;
import cser.track.core.deleteinsert.IIDController;
import cser.track.core.deleteinsert.block.BlockController;
import cser.utils.PositionList;
import cser.utils.PositionUtils;

public class NodeCalculator extends IDCalculator {
	private BaseVisitor currentVisitor =null;
	private BaseVisitor parentVisitor =null;

	public NodeCalculator(IAnnotationModel model, IFile file,
			CSeRClone currentCSeRFile) {
		super(model, file, currentCSeRFile);
	}

	@Override
	protected IIDController getController(List<ASTNode> insertedNodes,
			List<ASTNode> deletedNodes) {
		return new BlockController(insertedNodes, deletedNodes);
	}
	@Override
	protected boolean processForUpdate(PositionList currentPositions,
			PositionList parentPositions) {
		if(!dbNode.subtreeMatch(new ASTMatcher(), currentNode)){
			int i=0;
			for(Position position : currentPositions){
				ASTNode cNode = getCurrentASTNode(position);
				ASTNode dNode = getASTNode(parentPositions.get(i));
			
				if(!dNode.subtreeMatch(new ASTMatcher(), cNode)){
					createAnnotation(position, "{"+dNode +"->"+cNode+"}",	annotationForUpdate, chgUpdate,parentPositions.get(i).toString());
				}
				i++;
			}
			
		}
		return true;
		
	}
	
	@Override
	public void createInsertDeleteAnnotations(BaseVisitor currentVisitor,
			BaseVisitor parentVisitor) {
	
	if(dbNode.getNodeType()== currentNode.getNodeType() && getTypes().contains(dbNode.getNodeType())){
		
			Position parentPostion = new Position(dbNode.getStartPosition(), dbNode.getLength());
			Position currentPosition =new Position(currentNode.getStartPosition(), currentNode.getLength());
			removeChange(currentPosition, IMessages.annotationForUpdate, parentPostion);
			if(!dbNode.subtreeMatch(new ASTMatcher(), currentNode))
			createAnnotation(currentPosition, "{"+dbNode +"->"+currentNode+"}",
					annotationForUpdate, chgUpdate,parentPostion.toString());
			return ;
		
		}
		super.createInsertDeleteAnnotations(currentVisitor, parentVisitor);
		
	}

	protected BaseVisitor getCurrentVisitor() {
		if (currentVisitor == null)
			currentVisitor = new NodeVisitor(new Position(currentNode
					.getStartPosition(), currentNode.getLength()));
		return currentVisitor;
	}

	protected BaseVisitor getParentVisitor() {
		if (parentVisitor == null)
			parentVisitor = new NodeVisitor(new Position(dbNode
					.getStartPosition(), dbNode.getLength()));
		return parentVisitor;
	}

	public boolean initialCheck() {
		removeChanges();

		if (dbNode.subtreeMatch(new ASTMatcher(), currentNode)) {
			return false;
		}

	


		return true;
	}

	private void removeChanges() {
		Position position = new Position(currentNode.getStartPosition(),
				currentNode.getLength());
		CSeRChanges changes = currentCSeRFile.getChanges();
		Iterator<CSeRChange> iterator = changes.iterator();
		removeAnnotation(position,
				new String[] { annotationForDelete, annotationForInsert,
						annotationForProcessing, annotationForMove, annotationForUpdate });
		while (iterator.hasNext()) {
			CSeRChange change = iterator.next();
			if (//change.getType() != chgUpdate && 
				PositionUtils.strictlyContains(position, PositionUtils.extractPositionFrom(change.getPosition()))) {
				iterator.remove();
			}
		}
	}
	
	protected int getInsertType() {
		return nodeChgInsert;
	}

	protected int getDeleteType() {
		return nodeChgDelete;
	}
	
	private static List<Integer> types = new ArrayList<Integer>();
	public static List<Integer> getTypes () {
		if(types.size() <1){
		 types.add(ASTNode.SIMPLE_NAME);
		 types.add(ASTNode.STRING_LITERAL);
		 types.add(ASTNode.NUMBER_LITERAL);
		}
		 return types;	
	}
}
