package cser.utils;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;


public class ParseHelper {
	public static CompilationUnit parse(ICompilationUnit unit) {
		ASTParser parser = ASTParser.newParser(AST.JLS8); 
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit); // set source
		//parser.setResolveBindings(true); // we need bindings later on
		return (CompilationUnit) parser.createAST(null /* IProgressMonitor */); // parse
	}
	public static CompilationUnit parse(char[] unit) {
		ASTParser parser = ASTParser.newParser(AST.JLS8); 
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit); // set source
		return (CompilationUnit) parser.createAST(null /* IProgressMonitor */); // parse
	}

	
	public static ASTVisitor visit(IFile file ,ASTVisitor visitor){
		ICompilationUnit unit =	JavaCore.createCompilationUnitFrom(file);
		CompilationUnit cu = ParseHelper.parse(unit);
		cu.accept(visitor);
		return visitor;
	}
	
}
