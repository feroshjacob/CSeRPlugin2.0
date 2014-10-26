package cser.track.core.deleteinsert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.IAnnotationModel;

import cser.ast.BaseVisitor;
import cser.core.CSeRClone;
import cser.core.CSeRDBController;
import cser.core.CSeRException;
import cser.core.CheckPosition;
import cser.track.core.BaseCalculator;
import cser.utils.PositionList;

public abstract class BaseIDCalculator extends BaseCalculator {
	protected Position lastPosition = null;

	protected ASTNode currentNode = null;
	protected ASTNode dbNode = null;

	public BaseIDCalculator(IAnnotationModel model, IFile file,
			CSeRClone currentCSeRFile) {
		super(model, file, currentCSeRFile);

	}

	@Override
	public void refreshMarkers(ASTNode dbNode, ASTNode currentNode,
			Position lastPosition) {
		this.currentNode = currentNode;

		this.dbNode = dbNode;
		this.lastPosition = lastPosition;
		createInsertDeleteAnnotations(getCurrentVisitor(), getParentVisitor());

	}

	protected abstract BaseVisitor getCurrentVisitor();

	protected abstract BaseVisitor getParentVisitor();

	/**
	 * Map from parent to current and current to parent
	 */
	protected Map<Position, Position> currentToParent = new HashMap<Position, Position>();

	protected PositionList convertParentPositions(List<Position> parentPositions) {

		PositionList convertedPositions = new PositionList();
		for (Position position : parentPositions) {
			CheckPosition checkPosition = currentCSeRFile.getCheckPositions()
			.get(position.toString());
			if (checkPosition!=null){
				convertedPositions.add(checkPosition.getCurrentPosition());
				currentToParent.put(checkPosition.getCurrentPosition(), position);
			}
		}
		return convertedPositions;
	}

	public void createInsertDeleteAnnotations(BaseVisitor currentVisitor,
			BaseVisitor parentVisitor) {
		if (!initialCheck())
			return;

		currentNode.accept(currentVisitor);
		PositionList currentPositions = currentVisitor.getStatements()
		.filteroverlappingPositions();
		// if(currentpositions.size()<1) return;

		dbNode.accept(parentVisitor);
		PositionList parentPositions = parentVisitor.getStatements()
		.filteroverlappingPositions();

		PositionList unfilteredStatements = parentVisitor
		.getUnfilteredStatements();
		convertParentPositions(unfilteredStatements);

		PositionList oldPositions = convertParentPositions(parentPositions);
		PositionList insertedOrDeletedPositions = new PositionList();
		insertedOrDeletedPositions.diff(currentPositions);
		insertedOrDeletedPositions.diff(oldPositions);
		insertedOrDeletedPositions.sort();
		if (insertedOrDeletedPositions.size() == 0) {
			if (processForUpdate(currentPositions, parentPositions))
				return;
		}

		process(insertedOrDeletedPositions, oldPositions, currentPositions);

	}

	protected boolean processForUpdate(PositionList currentPositions,
			PositionList parentPositions) {
		return true;
	}

	protected abstract void process(PositionList allpositions,
			PositionList oldpositions, PositionList currentPositions);

	public abstract boolean initialCheck();

	protected Position getParentFromCurrent(Position position) {
		if (position == null) {
			return null;
		}

		Position pos1 = currentToParent.get(position);

		if (pos1 != null)
			return pos1;
		CheckPosition checkPosition = currentCSeRFile.getCheckPositions()
		.getCurrentPosition(position);
		return checkPosition.getParentPosition();
	}

	protected PositionList getParentListFromCurrent(PositionList pList) {

		PositionList list = new PositionList();
		for (Position position : pList) {
			list.add(getParentFromCurrent(position));
		}
		return list;
	}

	protected ASTNode getCurrentASTNode(Position position) {
		if (position == null) {
			return null;
		}
		try {

			return CSeRDBController.getJavaElement(currentCSeRFile.getFile(),
					position);

		} catch (CSeRException e) {
			e.printStackTrace();
		}
		return null;

	}

	protected ASTNode getASTNode(Position position) {
		if (position == null) {
			return null;
		}
		try {
			return CSeRDBController.getJavaElement(currentCSeRFile.getParent()
					.getFile(), position);

		} catch (CSeRException e) {
			e.printStackTrace();
		}
		return null;

	}

}