package cser.track.core.deleteinsert.block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.dom.ASTMatcher;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.IAnnotationModel;

import cser.ast.BaseVisitor;
import cser.ast.StatementVisitor;
import cser.core.CSeRChange;
import cser.core.CSeRChanges;
import cser.core.CSeRClone;
import cser.track.core.IMessages;
import cser.track.core.deleteinsert.IDCalculator;
import cser.track.core.deleteinsert.IIDController;
import cser.utils.PositionList;
import cser.utils.PositionUtils;

public class BlockCalculator extends IDCalculator {

	private BaseVisitor currentVisitor = null;
	private BaseVisitor parentVisitor = null;

	protected String annotationType = "cserstmtinsert";

	public BlockCalculator(IAnnotationModel model, IFile file,
			CSeRClone currentCSeRFile) {
		super(model, file, currentCSeRFile);
	}

	protected BaseVisitor getCurrentVisitor() {
		if (currentVisitor == null)
			currentVisitor = new StatementVisitor(new Position(currentNode
					.getStartPosition(), currentNode.getLength()));
		return currentVisitor;
	}

	protected BaseVisitor getParentVisitor() {
		if (parentVisitor == null)
			parentVisitor = new StatementVisitor(new Position(dbNode
					.getStartPosition(), dbNode.getLength()));
		return parentVisitor;
	}

	@Override
	public void createInsertDeleteAnnotations(BaseVisitor currentVisitor,
			BaseVisitor parentVisitor) {

		if (dbNode.getNodeType() == currentNode.getNodeType()
				&& getTypes().contains(dbNode.getNodeType())) {

			Position parentPostion = new Position(dbNode.getStartPosition(),
					dbNode.getLength());
			Position currentPosition = new Position(currentNode
					.getStartPosition(), currentNode.getLength());
			removeChange(currentPosition, IMessages.annotationForUpdate,
					parentPostion);
			if (!dbNode.subtreeMatch(new ASTMatcher(), currentNode))
				createAnnotation(currentPosition, "{" + dbNode + "->"
						+ currentNode + "}", annotationForUpdate, chgUpdate,
						parentPostion.toString());
			return;

		}
		super.createInsertDeleteAnnotations(currentVisitor, parentVisitor);
	}

	@Override
	public boolean initialCheck() {
		removeChanges();

		if (dbNode.subtreeMatch(new ASTMatcher(), currentNode)) {
			return false;
		}

		return true;
	}

	private static List<Integer> types = new ArrayList<Integer>();

	public static List<Integer> getTypes() {
		if (types.size() < 1) {
			types.add(ASTNode.SIMPLE_NAME);
			types.add(ASTNode.STRING_LITERAL);
			types.add(ASTNode.NUMBER_LITERAL);
		}
		return types;
	}

	private void removeChanges() {
		Position position = new Position(currentNode.getStartPosition(),
				currentNode.getLength());
		CSeRChanges changes = currentCSeRFile.getChanges();
		Iterator<CSeRChange> iterator = changes.iterator();
		while (iterator.hasNext()) {
			CSeRChange change = iterator.next();
			Position pos = PositionUtils.extractPositionFrom(change.getPosition());
			if (//change.getType() != chgUpdate &&
				 PositionUtils.strictlyContains(position, pos)
					&& change.getType() < 10) {
				iterator.remove();

			}
		}
		removeAnnotation(position,
				new String[] { annotationForDelete, annotationForInsert,
						annotationForProcessing, annotationForMove, annotationForUpdate });
	}

	@Override
	protected IIDController getController(List<ASTNode> insertedNodes,
			List<ASTNode> deletedNodes) {

		return new BlockController(insertedNodes, deletedNodes);
	}

	@Override
	/*
	 * return a position in parent where marker can be put on.
	 */
	protected Position getPositionForDeletion(Position position,
			PositionList oldpositions, ASTNode astNode) {
		Position positionInCurrent = super.getPositionForDeletion(position,
				oldpositions, astNode);

		Block parentBlock = (Block) dbNode;

		if (parentBlock.subtreeMatch(new ASTMatcher(), astNode.getParent())) {
			return positionInCurrent;
		}
		return findStatementContaining((Block) currentNode, positionInCurrent);

	}

	/**
	 * return a position in block that contains the given position.
	 * 
	 * @param block
	 * @param position
	 * @return
	 */
	private Position findStatementContaining(Block block, Position aPosition) {
		List statements = block.statements();
		Iterator it = statements.iterator();
		while (it.hasNext()) {
			Statement statement = (Statement) it.next();
			Position position = new Position(statement.getStartPosition(),
					statement.getLength());
			if (PositionUtils.contains(position, aPosition))
				return position;

		}
		return null;
	}

	private List<ASTNode> filter(List<ASTNode> nodes, Position position) {
		List<ASTNode> newnodes = new ArrayList<ASTNode>();
		for (ASTNode node : nodes) {
			Position pos = new Position(node.getStartPosition(), node
					.getLength());
			if (PositionUtils.contains(position, pos)) {
				newnodes.add(node);
			}
		}
		return newnodes;

	}

	private Map<ASTNode, ASTNode> filter(Map<ASTNode, ASTNode> nodes,
			Position position) {
		Map<ASTNode, ASTNode> newnodes = new HashMap<ASTNode, ASTNode>();
		for (ASTNode node1 : nodes.keySet()) {
			ASTNode node = nodes.get(node1);
			Position pos = new Position(node.getStartPosition(), node
					.getLength());
			if (PositionUtils.contains(position, pos)) {
				newnodes.put(node1, node);
			}
		}
		return newnodes;

	}

	@Override
	protected void createChanges(PositionList untouchedPositions,
			PositionList unDeletedPositions, IIDController controller,
			Map<ASTNode, Position> deletedPositions,
			Map<String, Position> insertedPositions) {
		Block caBlock = (Block) currentNode;
		List statements = caBlock.statements();
		Iterator stmtit = statements.iterator();
		while (stmtit.hasNext()) {
			Statement statement = (Statement) stmtit.next();
			Position position = new Position(statement.getStartPosition(),
					statement.getLength());

			if (untouchedPositions.contains(position))
				continue;
			boolean checkfortouch = checkForTouch(untouchedPositions, position);
			List<ASTNode> nodes = filter(controller.getInsertedNodes(),
					position);
			if (checkfortouch) {
				for (ASTNode astNode : nodes) {
					Position pos = new Position(astNode.getStartPosition(),
							astNode.getLength());
					createAnnotation(pos, astNode + IMessages.insMessage,
							getannotationTypeinsert(), getInsertType(),
							insertedPositions.get(astNode.toString())
									.toString());
				}

			} else {
				if (nodes.size() > 0) {
					createAnnotation(position,
							statement + IMessages.insMessage,
							getannotationTypeinsert(), getInsertType(),
							insertedPositions.get(nodes.get(0).toString())
									.toString());
				}

			}
			Map<ASTNode, ASTNode> nodeMap = filter(controller.getMovedNodes(),
					position);
			if (checkfortouch) {
				for (ASTNode astNode : nodeMap.keySet()) {
					Position pos = new Position(astNode.getStartPosition(),
							astNode.getLength());
					ASTNode ast2 = nodeMap.get(astNode);

					createAnnotation(new Position(ast2.getStartPosition(), ast2
							.getLength()), IMessages.move, annotationForMove,
							moveMessge, pos.toString());
				}

			} else {
				if (nodeMap.size() > 0) {
					ASTNode ast2 = nodeMap.keySet().iterator().next();
					createAnnotation(position, IMessages.move,
							annotationForMove, moveMessge, new Position(ast2
									.getStartPosition(), ast2.getLength())
									.toString());
				}
			}
			nodeMap = filter(controller.getUpdatedNodes(), position);
			if (checkfortouch) {
				for (ASTNode astNode : nodeMap.keySet()) {
					ASTNode castNode = nodeMap.get(astNode);

					Position ppostion = new Position(
							astNode.getStartPosition(), astNode.getLength());
					Position cpostion = new Position(castNode
							.getStartPosition(), castNode.getLength());

					createAnnotation(cpostion, "{" + astNode + "->" + castNode
							+ "}", annotationForUpdate, chgUpdate, ppostion
							.toString());
				}

			} else {
				if (nodeMap.size() > 0) {
					ASTNode astNode = nodeMap.keySet().iterator().next();
					ASTNode castNode = nodeMap.get(astNode);
					Position ppostion = new Position(
							astNode.getStartPosition(), astNode.getLength());
					createAnnotation(position, "{" + astNode + "->" + castNode
							+ "}", annotationForUpdate, chgUpdate, ppostion
							.toString());
				}

			}
		}

		Block paBlock = (Block) dbNode;
		statements = paBlock.statements();
		PositionList newList = getParentListFromCurrent(unDeletedPositions);
		stmtit = statements.iterator();
		while (stmtit.hasNext()) {
			Statement statement = (Statement) stmtit.next();
			Position position = new Position(statement.getStartPosition(),
					statement.getLength());

			if (newList.contains(position))
				continue;
			boolean checkfortouch = checkForTouch(newList, position);

			List<ASTNode> nodes = filter(controller.getDeletedNodes(), position);
			if (checkfortouch) {
				for (ASTNode astNode : nodes) {
					Position pos1 = deletedPositions.get(nodes.get(0));
					Position pos = new Position(pos1.offset + pos1.length - 2,
							2);
					createAnnotation(pos, astNode + IMessages.insMessage,
							getannotationTypedelete(), getDeleteType(),
							getParentFromCurrent(pos1).toString());
				}

			} else {
				if (nodes.size() > 0) {
					Position pos1 = deletedPositions.get(nodes.get(0));
					Position pos = new Position(pos1.offset + pos1.length - 2,	2);
					createAnnotation(pos,
							statement + IMessages.insMessage,
							getannotationTypedelete(), getDeleteType(),
							getParentFromCurrent(pos1).toString());
				}

			}
		}
	}

	private boolean checkForTouch(PositionList untouchedPositions,
			Position position) {
		for (Position position2 : untouchedPositions) {
			if (PositionUtils.contains(position, position2))
				return true;
		}
		return false;

	}
}
