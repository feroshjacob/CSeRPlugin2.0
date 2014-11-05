package cser.ast;


import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.IDocument;

public class InsertComment {
	IDocument document = null;
	IFile file = null;
	
public InsertComment(IFile file, IDocument document) {
	this.document = document;

	this.file = file;
}

public void insertOneLineComment(String text){
//	ICompilationUnit unit =	JavaCore.createCompilationUnitFrom(file);
//	CompilationUnit cu = ParseHelper.parse(unit);
//	 AST ast = cu.getAST();
//	 StringBuffer comment = new StringBuffer();
//		comment.append("/*\n");
//		comment.append(" * Parent File :"+text+"\n");
//		comment.append(" * Date        :"+ new SimpleDateFormat("EEE, MMM d, yy").format(Calendar.getInstance().getTime())+"\n");
//		comment.append(" */");
//
//		ASTRewrite rewrite= ASTRewrite.create(ast);
//		ListRewrite listRewrite= rewrite.getListRewrite((TypeDeclaration)cu.types().get(0), TypeDeclaration.BODY_DECLARATIONS_PROPERTY);
//		ASTNode placeHolder= rewrite.createStringPlaceholder(comment.toString(), ASTNode.EMPTY_STATEMENT);
//		listRewrite.insertFirst(placeHolder, null);
//	 TextEdit edits = rewrite.rewriteAST(document, null);
//	 try {
//		UndoEdit undo = edits.apply(document);
//	} catch (MalformedTreeException e) {
//		e.printStackTrace();
//	} catch (BadLocationException e) {
//		e.printStackTrace();
//	}
	
	
}

}
