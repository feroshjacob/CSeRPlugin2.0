package cser.editors;

import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;

import cser.core.CSeRClone;
import cser.core.CSeRDBController;
import cser.core.CSeRException;
import cser.core.CheckPosition;
import cser.core.CheckPositions;
import cser.track.core.BaseCalculator;
import cser.track.core.deleteinsert.block.BlockCalculator;
import cser.track.core.deleteinsert.node.NodeCalculator;
import cser.track.core.deleteinsert.type.TypeCalculator;

public abstract class BaseDocumentListener implements IDocumentListener {

	protected CSeRClone peerClone = null;
	protected CSeRClone currentClone = null;
	
	protected ITextEditor fEditor = null;
	protected IDocument document = null;
	protected IFile fileInEditor = null;
	protected IAnnotationModel model = null;

	public BaseDocumentListener(ITextEditor editor) {
		this.fEditor = editor;
		document = fEditor.getDocumentProvider().getDocument(
				fEditor.getEditorInput());
		fileInEditor = ((FileEditorInput) editor.getEditorInput()).getFile();
		model = fEditor.getDocumentProvider().getAnnotationModel(
				fEditor.getEditorInput());

		try {
			registerUpdatedPostions();
			showExistingChanges();
		} catch (CSeRException e) {

			e.printStackTrace();
		}
	}

	protected void showExistingChanges() {
	}

	protected abstract void registerUpdatedPostions() throws CSeRException;

	public void documentAboutToBeChanged(DocumentEvent event) {
	}

	public void documentChanged(DocumentEvent event) {

		try {
//			Position position = new Position(event.fOffset, event.fLength);
			Position position = new Position(event.fOffset, event.fText.length());
//			if (event.fText!=null){
//				boolean containsNonSpace=false;
//				for (int i=0; i<event.fText.length();++i){
//					char ch = event.fText.charAt(i);
//					if (!Character.isWhitespace(ch)){
//						containsNonSpace=true;
//						break;
//					}
//					
//				}
//				if (containsNonSpace){
					execute(position);
//				}
//			}


		} catch (CSeRException e) {
			e.printStackTrace();
		}

	}

	protected void addPositionUpdater(CheckPositions linkedPositions) {

		Set<String> positions = linkedPositions.keySet();
		for (String position : positions) {

			addPositionUpdater(linkedPositions.get(position));

		}

	}

	protected void addPositionUpdater(CheckPosition checkPosition) {

		try {

			String key = checkPosition.getParentPosition().toString();
			Position position = checkPosition.getCurrentPosition();
            BasePositionUpdater positionUpdater = new CurrentPositionUpdater(key, fileInEditor);
			if (peerClone == null) {// okay, this is a regional clone!
// dhou				key = checkPosition.getCurrentPosition().toString();
//				position = checkPosition.getParentPosition();
				positionUpdater = new ParentPositionUpdater(key, fileInEditor);
			}

			document.addPositionCategory(key);
			document.addPositionUpdater(positionUpdater);
			document.addPosition(key, position);
		} catch (BadLocationException e) {
			e.printStackTrace();
		} catch (BadPositionCategoryException e) {
			e.printStackTrace();
		}
	}

	protected void execute(Position position) throws CSeRException {

		if (peerClone == null)
			return;

		CheckPositions checkPositions = currentClone.getCheckPositions();
		CheckPosition checkPosition = checkPositions.getStrictContainingPosition(position);

		try {
				if (checkPosition == null) {
					System.out.println("The Check Position is null; will do nothing.");
					return;
				}
				
				ASTNode dbNode = CSeRDBController.getJavaElement(peerClone
						.getFile(), checkPosition.getParentPosition());

				ASTNode currentNode = CSeRDBController.getJavaElement(fileInEditor,
						checkPosition.getCurrentPosition());
				
				if (CSeRDBController.UNCOMPILABLE_CODE) {
					return ;
				}

//				ASTNode  dbNode=null;
//				ASTNode currentNode = null;
//				try {
//					ICompilationUnit unit = JavaCore.createCompilationUnitFrom(file);
//					PartCorrectionVisitor visitor = new PartCorrectionVisitor(checkPosition.getCurrentPosition());
//					CompilationUnit cu = ParseHelper.parse(unit);
//					
//					IProblem problems[] = cu.getProblems();
//					for(int i=0; i < problems.length ; i++){
//						 if (problems[i].isError()){
//							 return;
//						 }
//					}
//					
//					cu.accept(visitor);
//					currentNode = visitor.getNode();
//					
//					if (position.length!=1 && !visitor.isExactMatch()){
//						String category = checkPosition.getParentPosition().toString();
//						Position currentPosition = checkPosition.getCurrentPosition();
//						try {
//							document.removePosition(category, currentPosition);
//						} catch (BadPositionCategoryException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//
//						Position newCurrentPosition = new Position(currentPosition.offset,0);
//						checkPosition.setCurrentPosition(newCurrentPosition.toString());
//						try {
//							try {
//								document.addPosition(category, newCurrentPosition);
//							} catch (BadPositionCategoryException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//						} catch (BadLocationException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//
//						Position newPosition = new Position(currentNode.getStartPosition(), currentNode.getLength());
//						CheckPositions currentCheckPositions = currentCserFile.getCheckPositions();
//						checkPosition = currentCheckPositions.getCurrentPosition(newPosition);						
//					}
//					dbNode = CSeRDBController.getJavaElement(cserFile.getFile(), checkPosition.getParentPosition());
//					
//				} catch (RuntimeException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					throw new CSeRException(e.getMessage());
//				}
				
				if (dbNode == null) {
					throw new CSeRException("dbNode Node is null and currentNode is:"+currentNode+dbNode);
				}

				if (currentNode == null ) {
					System.out.println("currentNode is null (BaseDocumentListener).");
/*					 List statements = ((Block) dbNode.getParent()).statements();
					 Iterator stmsitr = statements.iterator();
					 Position pos =null;
					 if(stmsitr.hasNext())  {
						 ASTNode node = (ASTNode)stmsitr.next();
						  pos = new Position(node.getStartPosition(),node.getLength());
					 }
					 if(pos !=null) {
						CheckPosition checkPosition2 =currentcserFile.getCheckPositions().filterParentPositions(pos);
						pos  = new Position( checkPosition2.getCurrentPosition().offset+checkPosition2.getCurrentPosition().length,1 );
					 }
					Position  pos = new Position(dbNode.getParent().getStartPosition(),dbNode.getParent().getLength());
					CheckPosition checkPosition2 =currentcserFile.getCheckPositions().filterParentPositions(pos);
					
				    execute(checkPosition2.getCurrentPosition());*/
					return;
				}

				// System.out.println(currentNode);

				BaseCalculator calculator = null;
				Position parentPosition = checkPosition.getParentPosition();
				if (currentNode instanceof Block) {
					/*
					 * This is an insert in a method
					 */
					calculator = new BlockCalculator(model, fileInEditor, currentClone);
					calculator.refreshMarkers(dbNode, currentNode, parentPosition);

				} else if (currentNode instanceof TypeDeclaration) {
					calculator = new TypeCalculator(model, fileInEditor, currentClone);
					calculator.refreshMarkers(dbNode, currentNode, parentPosition);
				} else  {
					calculator = new NodeCalculator(model, fileInEditor, currentClone);
					calculator.refreshMarkers(dbNode, currentNode, parentPosition);
				}
//				if (currentNode instanceof Block) {
//					calculator = new BlockCalculator(model, file,
//							currentcserFile);
//					calculator.refreshMarkers(dbNode, currentNode,
//							parentPosition);
//
//				} else if (currentNode instanceof TypeDeclaration) {
//					calculator = new TypeCalculator(model, file,
//							currentcserFile);
//					calculator.refreshMarkers(dbNode, currentNode,
//							parentPosition);
//				} else  {
//					calculator = new NodeCalculator(model, file,
//							currentcserFile);
//					calculator.refreshMarkers(dbNode, currentNode,
//							parentPosition);
//					
//				}
					/*
					 * Code below is removed and above else as per the new design
					 * 
					 */
//					else if (PositionUtils.containsOrContainsPart(
//						new Position(currentNode.getStartPosition(),
//								currentNode.getLength()), position)) {
//
//					StmtMatchCalculator matchCalculator = new StmtMatchCalculator(
//							model, file, currentcserFile);
//					matchCalculator.refreshMarkers(dbNode, currentNode,
//							parentPosition);
//				}

		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	public CSeRClone getCurrentClone() {
		return currentClone;
	}

}
