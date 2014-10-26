package cser.track.core.deleteinsert.block;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTMatcher;
import org.eclipse.jdt.core.dom.ASTNode;

import cser.track.core.deleteinsert.BaseIDController;

public class BlockController extends BaseIDController {

	public BlockController(List<ASTNode> insertedNodes,
			List<ASTNode> deletedNodes) {
		super(insertedNodes, deletedNodes);
		// TODO Auto-generated constructor stub
	}
	


	
	protected boolean checkBeforeAndAfter(ASTNode astNode1, ASTNode astNode2, int i,
			int j) {
		
		ASTNode temp  = getAfterNode(i, intialdelNodes);
		ASTNode temp1 =  getAfterNode(j, intialinsNodes);
		if(temp==null && temp1 ==null) {
			return checkBefore(astNode1, astNode2,i,j);
		}
		else if (temp==null) {
			return false;
		}
		else if (temp1==null) {
			return false;
		}
		
		if(!temp.subtreeMatch(new ASTMatcher(), temp1)) {
			return false;
		}
			
		return checkBefore(astNode1,astNode2,i,j);
		
	}

	private boolean checkBefore(ASTNode astNode1, ASTNode astNode2, int i, int j) {
		ASTNode temp  = getBeforeNode(i, intialdelNodes);
		ASTNode temp1 =  getBeforeNode(j, intialinsNodes);
		if(temp==null && temp1 ==null) return true;
		else if (temp==null) return false;
		else if (temp1==null) return false;
		if(!temp.subtreeMatch(new ASTMatcher(), temp1)) return false;
			return true;
	}
	private ASTNode getBeforeNode(int i, List<ASTNode> list){
	
		ASTNode node =	list.get(i+1);
		while(node==null && i >  0 ){
			i=i-3;
			node = list.get(i+1);
		}
		return node;
		}
	private ASTNode getAfterNode(int i, List<ASTNode> list){
		if(i<list.size()-1)  {
		ASTNode node =	list.get(i+2);
		while(node==null && i <  list.size() ){

			node = list.get(i+2);
			i=i+3;
		}
		return node;
		}
		return null;
	}
}
