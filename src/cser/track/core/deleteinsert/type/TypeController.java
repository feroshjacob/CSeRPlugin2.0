package cser.track.core.deleteinsert.type;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;

import cser.track.core.deleteinsert.BaseIDController;

public class TypeController extends BaseIDController {

	public TypeController(List<ASTNode> insertedNodes,
			List<ASTNode> deletedNodes) {
		super(insertedNodes, deletedNodes);
	}

	@Override
	protected boolean checkBeforeAndAfter(ASTNode astNode1, ASTNode astNode2,
			int i, int j) {
		return true;
	}

}
