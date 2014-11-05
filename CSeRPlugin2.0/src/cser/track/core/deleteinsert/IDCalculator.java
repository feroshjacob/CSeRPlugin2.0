package cser.track.core.deleteinsert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.IAnnotationModel;

import cser.core.CSeRClone;
import cser.core.CSeRDBController;
import cser.core.CSeRException;
import cser.core.CheckPosition;
import cser.track.core.IMessages;
import cser.utils.PositionList;

public abstract class IDCalculator extends BaseIDCalculator {

	public IDCalculator(IAnnotationModel model, IFile file,
			CSeRClone currentCSeRFile) {
		super(model, file, currentCSeRFile);
	}

	/**
	 * @param: allPositions: inserted or deleted positions in the current file.
	 * @param: oldPositions: positions in the current file that exist before the changes.
	 * @param: currentPositions: positions in the current file that exist now.
	 */
	protected void process(PositionList allPositions,
			PositionList oldPositions, PositionList currentPositions) {
		/**
		 * int i =0; i++; is changed to int i =0; int j=0; i++; allpositions is
		 * the positions after change which includes the variable while old
		 * positions is the list with positions we were tracking with position
		 * updaters
		 */

		List<ASTNode> insertedNodes = new ArrayList<ASTNode>();

		/**
		 * Whenever we add AST Node we also add the neighbors also,
		 * i.e before and after.
		 */
		Map<ASTNode, Position> deletedPositions = new HashMap<ASTNode, Position>();
		Map<String, Position> insertedPositions = new HashMap<String, Position>();

		List<ASTNode> deletedNodes = new ArrayList<ASTNode>();
		for (Position position : allPositions) {

			if (getCurrentVisitor().getASTNode(position) != null) {
				ASTNode astNodeInCurrent = getCurrentASTNode(position);

				Position beforePositionInParent = getBeforeParentPositionFromCurrent(position, currentPositions);
				ASTNode astNodeBeforeInParent = getASTNode(beforePositionInParent);
				
				Position afterPositionInParent = getAfterParentPositionFromCurrent(position, currentPositions);
				ASTNode astNodeAfterInParent = getASTNode(afterPositionInParent);

				insertedNodes.add(astNodeBeforeInParent);
				insertedNodes.add(astNodeInCurrent);
				insertedNodes.add(astNodeAfterInParent);

				System.out.println("ins" + astNodeInCurrent);
				insertedPositions.put(astNodeInCurrent.toString(), getPositionForIns(position,
						currentPositions,astNodeInCurrent));
			} else {
				// position = (offset:xxx, length:0)
				ASTNode astNodeInParent = getASTNode(getParentFromCurrent(position));
				ASTNode astNodeBeforeInParent = getASTNode(getParentFromCurrent(oldPositions
						.before(position)));
				ASTNode astNodeAfterInParent = getASTNode(getParentFromCurrent(oldPositions
						.after(position)));

				deletedNodes.add(astNodeBeforeInParent);
				deletedNodes.add(astNodeInParent);
				deletedNodes.add(astNodeAfterInParent);

				System.out.println("del"+astNodeInParent);

				Position beforePositionInCurrent = getPositionForDeletion(position,
						oldPositions, astNodeInParent);
				updateCheckPosition(beforePositionInCurrent);
				deletedPositions.put(astNodeInParent, beforePositionInCurrent);

			}
		}

		currentPositions.subtract(allPositions);
		oldPositions.subtract(allPositions);
		IIDController controller = getController(insertedNodes, deletedNodes);
		createChanges(currentPositions, oldPositions, controller, deletedPositions, insertedPositions);

	}

	private Position getBeforeParentPositionFromCurrent(Position position, PositionList currentPositions) {
		Position positionInParent = null;
		do {
			Position beforePositionInCurrent = currentPositions.before(position);
			positionInParent = currentToParent.get(beforePositionInCurrent);
			position = beforePositionInCurrent;
		} while (positionInParent==null && position!=null);
		return positionInParent;
	}
	private Position getAfterParentPositionFromCurrent(Position position, PositionList currentPositions) {
		Position positionInParent = null;
		do {
			Position afterPositionInCurrent = currentPositions.after(position);
			positionInParent = currentToParent.get(afterPositionInCurrent);
			position = afterPositionInCurrent;
		} while (positionInParent==null&&position!=null);
		return positionInParent;
	}


	protected void createChanges(PositionList untouchedPositions, PositionList undeletedPositions, IIDController controller,
			Map<ASTNode, Position> neighbourOfDeletedPositions,
			Map<String, Position> insertedPositions ) {
		
		
	

		for (ASTNode astNode : controller.getInsertedNodes()) {
			// removeannotation(astNode, annotationTypeinsert);
			Position position = new Position(astNode.getStartPosition(),astNode.getLength());
			createAnnotation(position, astNode + IMessages.insMessage,
					getannotationTypeinsert(), getInsertType(), insertedPositions.get(astNode.toString()).toString());

		}
		for (ASTNode astNode : controller.getMovedNodes().keySet()) {
			// removeannotation(astNode, annotationTypeinsert);
			ASTNode ast2 = controller.getMovedNodes().get(astNode);
			System.out.println("mv:" + astNode);
			Position position = new Position(astNode.getStartPosition(),astNode.getLength());
			
			createAnnotation(position, IMessages.move, annotationForMove,moveMessge, new Position(ast2.getStartPosition(),ast2.getLength()).toString());

		}
		for (ASTNode astNode : controller.getDeletedNodes()) {
			Position position = neighbourOfDeletedPositions.get(astNode);
		 
			Position pos = new Position(position.offset + position.length-2,2);
			
			String msg = astNode + IMessages.deleteMessage;
			if(! (astNode instanceof Statement)) 
			msg =  astNode +IMessages.conditionMessage;


		
			
			createAnnotation(pos, msg,getannotationTypedelete(), getDeleteType(),getParentFromCurrent(position).toString());

		}
		Map<ASTNode, ASTNode> updatedNodes = controller.getUpdatedNodes();
		for (ASTNode astNode : updatedNodes.keySet()) {
			
//			if(astNode instanceof Expression) continue;
			System.out.println("up:" + astNode);
			ASTNode castNode = updatedNodes.get(astNode);
	
			Position ppostion = new Position(astNode.getStartPosition(),astNode.getLength());
			Position cpostion = new Position(castNode.getStartPosition(),castNode.getLength());
			
			createAnnotation(cpostion, "{"+astNode +"->"+castNode+"}",
					annotationForUpdate, chgUpdate,ppostion.toString());

		}
		newUpdaters = controller.getRemovedNodes();
	}

	private CheckPosition updateCheckPosition(Position position) {
		CheckPosition checkPosition = currentCSeRFile.getCheckPositions().getCurrentPosition(position);
		try {
			CSeRDBController.updateLinkedPositions(file, checkPosition.getParentPosition(), position);
			currentToParent.put(position, checkPosition.getParentPosition());
		} catch (CSeRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return checkPosition;
	}

	protected String getannotationTypedelete() {
		return annotationForDelete;
	}

	protected String getannotationTypeinsert() {
		return annotationForInsert;
	}

	protected int getInsertType() {
		return chgInsert;
	}

	protected int getDeleteType() {
		return chgDelete;
	}



	protected abstract IIDController getController(List<ASTNode> insertedNodes,
			List<ASTNode> deletedNodes);

	/**
	 * both positionOfDeletedNode and positions are for the current file.
	 * returns a position in the current file where marker can be put on.
	 * @param positionOfDeletedNode
	 * @param positions
	 * @return
	 */
	protected Position getPositionForDeletion(Position positionOfDeletedNode,
			PositionList positions) {
//		ASTNode ast1 = getCurrentASTNode(position);
		Position pos = positions.before(positionOfDeletedNode);
		
//		ASTNode ast2 = getCurrentASTNode(pos);

		
		while (pos != null && getCurrentASTNode(pos) == null){
			pos = positions.before(pos);			
		}
		if (pos != null){
			return pos;			
		}
		
		pos = positions.after(positionOfDeletedNode);
		while (pos != null && getCurrentASTNode(pos) == null){
			pos = positions.after(pos);
		}
		if (pos != null){
			return pos;
		}

		return new Position(currentNode.getStartPosition(), currentNode
				.getLength());
	}
	protected Position getPositionForDeletion(Position position,
			PositionList oldPositions, ASTNode astNode) {
		return getPositionForDeletion(position, oldPositions);
	}
	protected Position getPositionForIns(Position position,
			PositionList oldPositions, ASTNode astNode) {
		Position pos = getPositionForDeletion(position, oldPositions);
		Position pos1 = getParentFromCurrent(pos);
        return pos1;
	}

}
