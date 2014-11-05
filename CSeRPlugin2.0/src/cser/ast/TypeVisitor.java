package cser.ast;

import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jface.text.Position;

public class TypeVisitor extends BaseVisitor {

	/**
	 * ClassDeclaration InterfaceDeclaration EnumDeclaration MethodDeclaration
	 * ConstructorDeclaration FieldDeclaration Initializer
	 * EnumConstantDeclaration AnnotationTypeDeclaration
	 * AnnotationTypeMemberDeclaration
	 */

	public TypeVisitor(Position position) {
		super(position);
	}

	@Override
	public boolean visit(AnnotationTypeMemberDeclaration node) {
		addStatement(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(EnumConstantDeclaration node) {
		addStatement(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(Initializer node) {
		addStatement(node);
		return super.visit(node);
	}

	public boolean visit(MethodDeclaration node) {
		addStatement(node);
		return super.visit(node);

	}

	public boolean visit(FieldDeclaration node) {
		addStatement(node);
		return super.visit(node);

	}

	public boolean visit(TypeDeclaration node) {

		addStatement(node);
		return super.visit(node);

	}

}
