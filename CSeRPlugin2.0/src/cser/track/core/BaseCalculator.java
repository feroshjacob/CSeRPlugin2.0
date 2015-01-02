package cser.track.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationModel;

import cser.core.CSeRChange;
import cser.core.CSeRChanges;
import cser.core.CSeRClone;
import cser.core.CSeRException;
import cser.editors.CSeRMarkerAnnotation;
import cser.utils.PositionList;
import cser.utils.PositionUtils;

public abstract class BaseCalculator implements IMessages {
	protected IAnnotationModel model = null;
	protected IFile file = null;
	protected IDocument document = null;
	protected CSeRClone currentCSeRFile = null;
	
	// Mappings that are added back. Never used? TODO
	protected Map<ASTNode,ASTNode> newUpdaters = new HashMap<ASTNode, ASTNode>();

	public Map<ASTNode, ASTNode> getNewUpdaters() {
		return newUpdaters;
	}

	public BaseCalculator(IAnnotationModel model, IFile file,
			CSeRClone currentCSeRFile) {
		this.file = file;
		this.model = model;
		this.currentCSeRFile = currentCSeRFile;
	}

	private boolean isCurrentSession(Annotation annotation,
			String[] annotationTypes) {
		if (annotation instanceof CSeRMarkerAnnotation) {
			
			for(int i=0; i < annotationTypes.length;i++) 
			if (annotation.getType().equals(annotationTypes[i]))
				return true;
		}
		return false;
	}

	protected void refreshMarkers(Position position,String annotationType,Position key) {

		removeChange(position, annotationType,key);
	}

	public abstract void refreshMarkers(ASTNode dbNode, ASTNode currentNode,Position lastPosition);

	protected void createAnnotation(Position position, String message,
			String annotationType, int type, String key) {
			
		createAnnotation(position, message, annotationType);	
		addChange(position, message, type, key);
	}

	// creates annotation for a given type and position, parameter 'message' isn't used
	protected void createAnnotation(Position position, String message,
			String annotationType) {
			CSeRMarkerAnnotation marker = null;
		try {
				marker = new CSeRMarkerAnnotation(annotationType, "", file);
				marker.setQuickFixable(true);
				model.addAnnotation(marker, position);

		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	private void addChange(Position position, String message, int type,String key) {
      addChange(currentCSeRFile,position, message, type, key);
      addChangeForParent(currentCSeRFile.getParent(),PositionUtils.extractPositionFrom(key), message +":" +currentCSeRFile.getFileName(), type, key );        
	}

	private void addChange(CSeRClone clone, Position position, String message, int type, String key) {
		if(clone==null) {
			 System.out.println("SOMETHING WRONG CONTACT FEROSH JACOB");
		}
		
		CSeRChange change = clone.getChanges().filterChanges(position.toString());
		if(change==null ){
			clone.addChange(message,type,position.toString(),key,currentCSeRFile.getFileName());
		}
		else {
			if (change.getType()==chgUpdate ){
				change.setMessage( message,file.getName());
			}
			else {
				change.setMessage(message +"\n"+ change.getMessage(),currentCSeRFile.getFileName());
			}
		}
		
		ChangeController.updateBuffer(clone.getFileName(), clone.getChanges());
	}

	private void addChangeForParent(CSeRClone clone,Position position, String message, int type,String key) {
		if(clone==null) {
			 System.out.println("SOMETHING WRONG CONTACT FEROSH JACOB");
		}
		CSeRChange change = clone.getChanges().filterChanges(position.toString());
		if(change==null )clone.addChangeForParent(message,type,position.toString(),key,currentCSeRFile.getFileName());
		else if (change.getType()==chgUpdate ){
			change.setMessage( message,file.getName());
		}
		else 
			change.setMessage(message +"\n"+ change.getMessage(),currentCSeRFile.getFileName());
		ChangeController.updateBuffer(clone.getFileName(), clone.getChanges());
	}
	protected void removeAnnotation(Position position ,String[] annotationTypes) {

		Iterator<Annotation> anoot = model.getAnnotationIterator();
		while (anoot.hasNext()) {
			Annotation annotation = anoot.next();

			if (annotation instanceof CSeRMarkerAnnotation
					&& PositionUtils.strictlyContains(position, model.getPosition(annotation)) && isCurrentSession(annotation, annotationTypes) ) {
				model.removeAnnotation(annotation);			
			}
		}
	}


	protected void removeAnnotation( String[] annotationTypes) {

		Iterator<Annotation> anoot = model.getAnnotationIterator();
		while (anoot.hasNext()) {
			Annotation annotation = anoot.next();

			if (isCurrentSession(annotation, annotationTypes)) {
				model.removeAnnotation(annotation);
			}
		}
	}
	protected void removeChange(Position position, String annotationType, Position key) {
		removeAnnotation(position, new String[]{annotationType});
		try {
			currentCSeRFile.getChanges().remove(new CSeRChange("",getAnnotationType(annotationType),"",key.toString(),currentCSeRFile.getFileName()));
			currentCSeRFile.getParent().getChanges().remove(new CSeRChange("",getAnnotationType(annotationType),"",key.toString(),currentCSeRFile.getFileName(),true));
		} catch (CSeRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private int getAnnotationType(String type) throws CSeRException {
		if (type.equals(annotationForDelete))
			return chgDelete;
		if (type.equals(annotationForInsert))
			return chgInsert;
		if (type.equals(annotationForUpdate))
			return chgUpdate;
		if (type.equals(annotationForDeleteF))
			return chgDelete_Field;
		if (type.equals(annotationForInsertF))
			return chgInsert_Field;
		if (type.equals(annotationForMove))
			return moveMessge;

		throw new CSeRException("Unexpected Change");
	}
}
