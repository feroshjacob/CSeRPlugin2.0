package cser.ast;

import java.util.ArrayList;
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
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.jface.text.Position;

import cser.utils.PositionUtils;


public class MainVisitor extends ASTVisitor {

    private Position position =null;
	public MainVisitor(Position position) {
     this.position = position;
	}
	private List<ASTNode> statements = new ArrayList<ASTNode>();


	@Override
	public boolean visit(TryStatement node) {
		addASTNode(node);
		return super.visit(node);
	}
	@Override
	public boolean visit(ArrayInitializer node) {
		addASTNode(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(SynchronizedStatement node) {
		addASTNode(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(SwitchStatement node) {
		addASTNode(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(ThrowStatement node) {
		addASTNode(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(VariableDeclarationStatement node) {
		addASTNode(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(ConstructorInvocation node) {
		addASTNode(node);
		List arguments = node.arguments();
		for(int i=0;i< arguments.size();i++){
			addASTNode((ASTNode)arguments.get(i));
		}
		return super.visit(node);

	}

	@Override
	public boolean visit(SuperConstructorInvocation node) {
		addASTNode(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(IfStatement node) {
		addASTNode(node);
		addASTNode(node.getExpression());
	

		return super.visit(node);
	}


	@Override
	public boolean visit(LabeledStatement node) {
		addASTNode(node);
		return super.visit(node);
	}



	@Override
	public boolean visit(AssertStatement node) {
		addASTNode(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(BreakStatement node) {
		addASTNode(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(ContinueStatement node) {
		addASTNode(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(DoStatement node) {
		addASTNode(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(ForStatement node) {
		addASTNode(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(WhileStatement node) {
		addASTNode(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(EnhancedForStatement node) {
		addASTNode(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(AnnotationTypeMemberDeclaration node) {
		addASTNode(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(EnumConstantDeclaration node) {
		addASTNode(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(Initializer node) {
		addASTNode(node);
		return super.visit(node);
	}




	public boolean visit(MethodDeclaration node) {
		addASTNode(node);
		return super.visit(node);

	}

	public boolean visit(MethodInvocation node) {
		addASTNode(node);
		List arguments = node.arguments();
		for(int i=0;i< arguments.size();i++){
			addASTNode((ASTNode)arguments.get(i));
		}
		return super.visit(node);
	}

	public boolean visit(FieldDeclaration node) {
		addASTNode(node);
		return super.visit(node);

	}

	public boolean visit(ExpressionStatement node) {
		addASTNode(node);
		return super.visit(node);
	}

	public boolean visit(ReturnStatement node) {
		addASTNode(node);
		return super.visit(node);
	}


	public boolean visit(VariableDeclarationFragment nod) {

		addASTNode(nod);
		return super.visit(nod);

	}

	public boolean visit(SingleVariableDeclaration nod) {

		addASTNode(nod);
		return super.visit(nod);

	}

	public boolean visit(Block node) {
		addASTNode(node);
		return super.visit(node);
	}

	public boolean visit(ImportDeclaration node) {
		addASTNode(node);
		return super.visit(node);
	}

	public boolean visit(NumberLiteral node) {
		addASTNode(node);
		return super.visit(node);
	}

	

	/*
	 * 
	 * Something is wrong not able to use this
	 */
	/*
	 * @Override public boolean visit(Modifier node) { addStatement(node);
	 * return super.visit(node); }
	 */
	@Override
	public boolean visit(PackageDeclaration node) {
		addASTNode(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(TypeDeclaration node) {
		addASTNode(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(StringLiteral node) {
		addASTNode(node);
		return super.visit(node);
	}

	
	public boolean visit(SimpleName node) {
		addASTNode(node);
		return super.visit(node);
	}
	
	
	
	/**
	 * get AST Nodes for tracking.
	 * @return
	 */
	public List<ASTNode> getASTNodes() {
		return statements;
	}


	private void addASTNode(ASTNode node) {
		if(position==null){
			statements.add(node);
			return;
		}
		if(position !=null && checkInside(node) )  
		statements.add(node);
	}
	
	private boolean checkInside(ASTNode node) {
		if(PositionUtils.contains(position, new Position(node.getStartPosition(),node.getLength())))
		return true;
		return false;

	}
	
}