package cser.ast;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeParameter;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.jface.text.Position;

public class PartVisitor extends ASTVisitor {

	private Position position = null;
	private ASTNode node = null;

	public PartVisitor(Position pos) {
		this.position = pos;
	}

	@Override
	public boolean visit(TryStatement node) {
		check(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(SynchronizedStatement node) {
		check(node);
		return super.visit(node);
	}
	
	@Override
	public boolean visit(ArrayInitializer node) {
		check(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(SwitchStatement node) {
		check(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(ThrowStatement node) {
		check(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(VariableDeclarationStatement node) {
		check(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(ConstructorInvocation node) {
		check(node);
		List arguments = node.arguments();
		for(int i=0;i< arguments.size();i++){
			check((ASTNode)arguments.get(i));
		}
		return super.visit(node);

		
	}

	@Override
	public boolean visit(SuperConstructorInvocation node) {
		check(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(IfStatement node) {
		
		check(node);
		check(node.getExpression());
	
		return super.visit(node);
	}

	@Override
	public boolean visit(LabeledStatement node) {
		check(node);
		return super.visit(node);
	}



	@Override
	public boolean visit(AssertStatement node) {
		check(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(BreakStatement node) {
		check(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(ContinueStatement node) {
		check(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(DoStatement node) {
		check(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(ForStatement node) {
		check(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(WhileStatement node) {
		check(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(EnhancedForStatement node) {
		check(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(AnnotationTypeMemberDeclaration node) {
		check(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(EnumConstantDeclaration node) {
		check(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(Initializer node) {
		check(node);
		return super.visit(node);
	}




	public boolean visit(MethodDeclaration node) {
		check(node);
		return super.visit(node);

	}

	public boolean visit(MethodInvocation node) {
		check(node);
		List arguments = node.arguments();
		for(int i=0;i< arguments.size();i++){
			check((ASTNode)arguments.get(i));
		}
		return super.visit(node);

	}

	public boolean visit(FieldDeclaration node) {
		check(node);
		return super.visit(node);

	}

	public boolean visit(ExpressionStatement node) {
		check(node);
		return super.visit(node);
	}
	public boolean visit(InfixExpression node) {
		check(node);
		return super.visit(node);
	}

	public boolean visit(ReturnStatement node) {
		check(node);
		return super.visit(node);
	}


	public boolean visit(VariableDeclarationFragment nod) {

		check(nod);
		return super.visit(nod);

	}

	public boolean visit(SingleVariableDeclaration nod) {

		check(nod);
		return super.visit(nod);

	}

	public boolean visit(Block node) {
		check(node);
		return super.visit(node);
	}

	public boolean visit(ImportDeclaration node) {
		check(node);
		return super.visit(node);
	}

	public boolean visit(NumberLiteral node) {
		check(node);
		return super.visit(node);
	}

	

	/*
	 * 
	 * Something is wrong not able to use this
	 */
	/*
	 * @Override public boolean visit(Modifier node) { check(node);
	 * return super.visit(node); }
	 */
	@Override
	public boolean visit(PackageDeclaration node) {
		check(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(TypeDeclaration node) {
		check(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(StringLiteral node) {
		check(node);
		return super.visit(node);
	}

	
	public boolean visit(SimpleName node) {
		check(node);
		return super.visit(node);
	}
	public boolean visit(TypeParameter node) {
		check(node);
		return super.visit(node);
	}


	public ASTNode getNode() {
		return node;
	}

	private void check(ASTNode nod) {
		if (nod != null && nod.getStartPosition() == position.getOffset()
				&& nod.getLength() == position.getLength()) {
			node = nod;
		}
	}

}
