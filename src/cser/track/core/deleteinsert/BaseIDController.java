package cser.track.core.deleteinsert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jdt.core.dom.ASTMatcher;
import org.eclipse.jdt.core.dom.ASTNode;

import cser.utils.GlobalConfig;

/**
 * This class decides whether a newly added node is a replica of the existing node
 * It takes a list of inserted nodes and another list of deleted nodes. 
 * The output is the calculated inserted nodes, calculated deleted nodes, and updated nodes.
 */

public abstract class BaseIDController implements IIDController {
		
	protected List<ASTNode> intialinsNodes = null;
	protected List<ASTNode> intialdelNodes = null;

	// nodes that are inserted and deleted.
	protected List<ASTNode> finalinsNodes = null;
	protected List<ASTNode> finaldelNodes = null;
	
	// Nodes that were deleted since this clone is created. TODO: Is this field necessary?
	private static List<ASTNode> deletedNodesStore =  new ArrayList<ASTNode>();
	protected Map<ASTNode,ASTNode> updatedNodes = new HashMap<ASTNode,ASTNode>();
    public Map<ASTNode,ASTNode> getUpdatedNodes() {
		return updatedNodes;
	}
    // Nodes that were deleted but now added back!
	private Map<ASTNode,ASTNode> removedNodes = new HashMap<ASTNode,ASTNode>();
    private Map<ASTNode,ASTNode> movedNodes = new HashMap<ASTNode,ASTNode>();
	
	public BaseIDController(List<ASTNode> insertedNodes, List<ASTNode> deletedNodes) {
		this.intialdelNodes = deletedNodes;
		this.intialinsNodes = insertedNodes;
		this.finaldelNodes = getThirdElementFromList(deletedNodes);
		this.finalinsNodes = getThirdElementFromList(insertedNodes);
		
		
		/*
		 * This removes the nodes which are both in insertedNode List and deletedNode List
		 * This is basically to support the undo operation
		 */
		removeDeletedNodesCurrentSession();
		deletedNodesStore.addAll(getDeletedNodes());
		removeDeletedNodesGlobally();
//		processMovedNodes();
		
	}

//	private void processMovedNodes() {
//	
//		for(int i=0; i < finalinsNodes.size(); i++) 
//			for(int j=0; j < finalinsNodes.size();j++)
//				if(i!=j && finalinsNodes.get(j).toString().contains(finalinsNodes.get(i).toString()) && !(finalinsNodes.get(i).toString().contentEquals( finalinsNodes.get(j).toString()))){
//				 ASTNode node =finalinsNodes.get(i);
//				finalinsNodes.remove(node);
//				movedNodes.put(node,finalinsNodes.get(j));
//
//		}
//		
//	}



	private void removeDeletedNodesGlobally( ) {
		   Iterator<ASTNode> iterator = deletedNodesStore.iterator();
		
			for(int i=0; iterator.hasNext();i++) {
				   Iterator<ASTNode> iterator1 = finalinsNodes.iterator();
				   ASTNode parentNode = iterator.next();
					for(int j=0; iterator1.hasNext();j++ ) {
						 ASTNode currentNode = iterator1.next();
						 if(parentNode.subtreeMatch(new ASTMatcher(), currentNode)){	
							 movedNodes.put(currentNode,parentNode);
							 iterator.remove();
							 iterator1.remove();
						 }
					}
			}
		}
	
	/**
	 * A node may be deleted and then added back. 
	 * Such a node should be treated as either moved or updated, 
	 * not separately as ``deleted'' and ``added''.
	 */
	private void removeDeletedNodesCurrentSession( ) {
			for(int i=0; i<finaldelNodes.size();i++) {
				ASTNode parentNode = finaldelNodes.get(i);
				for(int j=0; j<finalinsNodes.size();j++ ) {
					ASTNode currentNode = finalinsNodes.get(j);
					if (parentNode.subtreeMatch(new ASTMatcher(), currentNode)){
						// If parentNode and currentNode have the same neighbors, 
						// treat them as ``undo'',
						// otherwise, as ``moved''.
						boolean removeMe =checkBeforeAndAfter(parentNode, currentNode, 3*i, 3*j);
						if(removeMe) {
							removedNodes.put(parentNode,currentNode);
						}
						else {
							movedNodes.put(parentNode,currentNode);
						}
						// Remove them from finalDelNodes and finalInsNodes
						finaldelNodes.remove(i);
						finalinsNodes.remove(j);
					}
					else if(isSimilar(parentNode, currentNode)) {
						updatedNodes.put(parentNode,currentNode);
						finaldelNodes.remove(i);
						finalinsNodes.remove(j);
					}				
				}
			}
//	   Iterator<ASTNode> iterator = finaldelNodes.iterator();
//	
//		for(int i=0; iterator.hasNext();i++) {
//			   Iterator<ASTNode> iterator1 = finalinsNodes.iterator();
//			   ASTNode parentNode = iterator.next();
//				for(int j=0; iterator1.hasNext();j++ ) {
//					 ASTNode currentNode = iterator1.next();
//				if (parentNode.subtreeMatch(new ASTMatcher(), currentNode)){
//					// If parentNode and currentNode have the same neighbors, treat them as ``undo''.
//					// otherwise, as ``moved''.
//					boolean removeMe =checkBeforeAndAfter(parentNode, currentNode, 3*i, 3*j);
//					if(removeMe) {
//						removedNodes.put(parentNode,currentNode);
//					}
//					else {
//						movedNodes.put(parentNode,currentNode);
//					}
//					// Remove them from finalDelNodes and finalInsNodes
//					iterator.remove();
//					iterator1.remove();
//				}
//				else if(isSimilar(parentNode, currentNode)) {
//					updatedNodes.put(parentNode,currentNode);
//					iterator.remove();
//					iterator1.remove();
//				}
//			
//			}
//			
//		}
		
	}
	private boolean isSimilar(ASTNode astNode1, ASTNode astNode2) {
      if(astNode1.getNodeType() != astNode2.getNodeType()) {
    	 return false;
      }
      if (astNode1.getNodeType()==ASTNode.INFIX_EXPRESSION){
    	  return false;
      }
      double levenshtein = StringUtils.getLevenshteinDistance(astNode1.toString(), astNode2.toString());
      int lenCode1 = astNode1.toString().length();
      int lenCode2 = astNode2.toString().length();
      int maxLen = lenCode1>=lenCode2?lenCode1:lenCode2;
      return (levenshtein/maxLen  < GlobalConfig.levenshteinRatio);
	}

	protected abstract boolean checkBeforeAndAfter(ASTNode astNode1, ASTNode astNode2,
			int i, int j) ;

	public List<ASTNode> getDeletedNodes() {
		Collections.sort(finaldelNodes, new Comparator<ASTNode> (){

			public int compare(ASTNode o1, ASTNode o2) {
				if(o1.getStartPosition() > o2.getStartPosition()) return 1;
				if(o1.getStartPosition() < o2.getStartPosition()) return -1;
				return 0;
				
			} });
		return finaldelNodes;
	}

	public  List<ASTNode> getInsertedNodes() {
		return finalinsNodes;
	}
	
private List<ASTNode> getThirdElementFromList(List<ASTNode> list) {
	List<ASTNode> aSTNodes = new ArrayList<ASTNode>();
	for(int i =1; i < list.size(); ){
		aSTNodes.add(list.get(i));
		i = i+3;
	}
	return aSTNodes;
}




public Map<ASTNode,ASTNode> getRemovedNodes() {
	
	return removedNodes;
}
public  Map<ASTNode,ASTNode>  getMovedNodes() {
	return movedNodes;
}

}
