package cser.track.core.deleteinsert;

import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;

/**
 * This class decides whether a newly added node is a replica of the existing node
 * It takes a list of inserted nodes and another list deleted node. 
 * The output is the calculated inserted nodes, calculated deleted nodes, and updated nodes.
 */
public interface IIDController {
	
	List<ASTNode> getInsertedNodes ();
	List<ASTNode> getDeletedNodes ();
	Map<ASTNode,ASTNode>  getMovedNodes ();
	Map<ASTNode,ASTNode> getUpdatedNodes();
	Map<ASTNode,ASTNode> getRemovedNodes ();
}

