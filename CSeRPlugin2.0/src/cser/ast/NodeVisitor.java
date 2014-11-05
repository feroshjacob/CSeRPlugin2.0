package cser.ast;



import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jface.text.Position;

public class NodeVisitor extends BaseVisitor {

	public NodeVisitor(Position position) {
		super(position);
		// TODO Auto-generated constructor stub
	}
	@Override
	public boolean visit(SimpleName node) {
		addStatement(node);
		return super.visit(node);
	}
	@Override
	public boolean visit(NumberLiteral node) {
		addStatement(node);
		return super.visit(node);
	}
	@Override
	public boolean visit(StringLiteral node) {
		addStatement(node);
		return super.visit(node);
	}
	@Override
	public boolean visit(CharacterLiteral node) {
		addStatement(node);
		return super.visit(node);
	}


}
