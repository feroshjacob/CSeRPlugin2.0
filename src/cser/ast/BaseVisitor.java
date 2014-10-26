package cser.ast;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jface.text.Position;

import cser.utils.PositionList;

public class BaseVisitor extends ASTVisitor {
	
	protected PositionList statements = new PositionList();
	protected Map<Position, ASTNode> astNodes = new HashMap<Position, ASTNode>();
	protected Position position =null;
	public BaseVisitor(Position position) {
		this.position = position;
	}
	protected void addStatement(ASTNode node) {
	
		Position position =new Position(node.getStartPosition(),node.getLength());
		if(position.equals(this.position)) return;
		astNodes.put(position, node);
		statements.add(position);
		
	}

	public PositionList getStatements() {
		
			return statements;
	}

	
	public ASTNode getASTNode(Position position) {
		return astNodes.get(position);
	}
	
	
	// support for un-filtered statements
	protected PositionList unfilteredStatements = new PositionList();
	protected void addUnfilteredStatement(ASTNode node) {
	
		Position position =new Position(node.getStartPosition(), node.getLength());
		astNodes.put(position, node);
		unfilteredStatements.add(position);
		
	}

	public PositionList getUnfilteredStatements() {
			return unfilteredStatements;
	}

}
