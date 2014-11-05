package cser.ast;

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

/**
 * this visitor returns the smallest node that contains the given position.
 * when the returned node exactly matches the given position, exactMatch becomes true. 
 * @author dhou
 *
 */
public class PartCorrectionVisitor extends ASTVisitor {

		private Position position = null;
		private ASTNode smallestContainingNode = null;
		private boolean exactMatch=false;

		public PartCorrectionVisitor(Position position) {
			this.position = position;
		}

		

		@Override
		public boolean visit(TryStatement node) {
			return check(node);
			
		}

		@Override
		public boolean visit(SynchronizedStatement node) {
			return check(node);
			
		}
		
		@Override
		public boolean visit(ArrayInitializer node) {
			return check(node);
			
		}

		@Override
		public boolean visit(SwitchStatement node) {
			return check(node);
			
		}

		@Override
		public boolean visit(ThrowStatement node) {
			return check(node);
			
		}

		@Override
		public boolean visit(VariableDeclarationStatement node) {
			return check(node);
			
		}

		@Override
		public boolean visit(ConstructorInvocation node) {
			return check(node);
		}

		@Override
		public boolean visit(SuperConstructorInvocation node) {
			return check(node);
			
		}

		@Override
		public boolean visit(IfStatement node) {
			
			return check(node);
		}

		@Override
		public boolean visit(LabeledStatement node) {
			return check(node);
			
		}



		@Override
		public boolean visit(AssertStatement node) {
			return check(node);
			
		}

		@Override
		public boolean visit(BreakStatement node) {
			return check(node);
			
		}

		@Override
		public boolean visit(ContinueStatement node) {
			return check(node);
			
		}

		@Override
		public boolean visit(DoStatement node) {
			return check(node);
			
		}

		@Override
		public boolean visit(ForStatement node) {
			return check(node);
			
		}

		@Override
		public boolean visit(WhileStatement node) {
			return check(node);
			
		}

		@Override
		public boolean visit(EnhancedForStatement node) {
			return check(node);
			
		}

		@Override
		public boolean visit(AnnotationTypeMemberDeclaration node) {
			return check(node);
			
		}

		@Override
		public boolean visit(EnumConstantDeclaration node) {
			return check(node);
			
		}

		@Override
		public boolean visit(Initializer node) {
			return check(node);
			
		}




		public boolean visit(MethodDeclaration node) {
			return check(node);
			

		}

		public boolean visit(MethodInvocation node) {
			return check(node);
		}

		public boolean visit(FieldDeclaration node) {
			return check(node);
		}

		public boolean visit(ExpressionStatement node) {
			return check(node);
			
		}
		public boolean visit(InfixExpression node) {
			return check(node);
			
		}

		public boolean visit(ReturnStatement node) {
			return check(node);
			
		}


		public boolean visit(VariableDeclarationFragment node) {
			return check(node);
		}

		public boolean visit(SingleVariableDeclaration node) {
			return check(node);
		}

		public boolean visit(Block node) {
			return check(node);
			
		}

		public boolean visit(ImportDeclaration node) {
			return check(node);
			
		}

		public boolean visit(NumberLiteral node) {
			return check(node);
			
		}

		

		/*
		 * 
		 * Something is wrong not able to use this
		 */
		/*
		 * @Override public boolean visit(Modifier node) { return check(node);
		 *  }
		 */
		@Override
		public boolean visit(PackageDeclaration node) {
			return check(node);
			
		}

		@Override
		public boolean visit(TypeDeclaration node) {
			return check(node);
			
		}

		@Override
		public boolean visit(StringLiteral node) {
			return check(node);
			
		}

		
		public boolean visit(SimpleName node) {
			return check(node);
			
		}
		public boolean visit(TypeParameter node) {
			return check(node);
			
		}


		public ASTNode getNode() {
			return smallestContainingNode;
		}
		
		public boolean isExactMatch(){
			return exactMatch;
		}

		private boolean check(ASTNode node) {
			int nodeStart = node.getStartPosition();
			int nodeEnd = nodeStart + node.getLength()-1;
			int posStart = position.getOffset();
			int posEnd = posStart + position.getLength()-1;
			if (nodeStart<=posStart && posEnd<=nodeEnd){
				smallestContainingNode = node;
				if (node.getStartPosition() == position.getOffset()
						&& node.getLength() == position.getLength()) {
					exactMatch = true;
					return false;
				}
				return true;
			}
			return false;
		}

	}
