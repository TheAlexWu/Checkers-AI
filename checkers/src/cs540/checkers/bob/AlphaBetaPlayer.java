package cs540.checkers.bob;

import cs540.checkers.*;
import cs540.checkers.CheckersConsts.*;

import java.util.List;
import java.util.Stack;

 /* This class implements a Checkers AI player 
  * using Alpha-Beta pruning with Iterative Deepening.
  * @author Bob (Robert) Wagner
  */
public class AlphaBetaPlayer extends CheckersPlayer implements GradedCheckersPlayer {
    
	protected int pruneCount = 0;		//The number of pruned subtrees for the most recent deepening iteration.
	protected int lastPrunedNodeScore = -1;	//The score of the most recently pruned node.
	protected Evaluator sbe;		//Static Board Evaluator (SBE)
	
	private int currDepthLimit;		//Current depth limit of iterative deeping algorithm
	private int maxDepthLimit = getDepthLimit(0);
	private int currScore;			//Current board state score
	private int bestScore;
	
	private Move bestMove;			//Current best move			
	private boolean setMove = false;	//Whether a move should be set
	
	//constructor
	public AlphaBetaPlayer(String name, int side) { 
	    	super(name, side);
		// Use SimpleEvaluator to score terminal nodes
		sbe = new SimpleEvaluator();
	}
	
	/* Uses Alpha-Beta pruning with Iterative Deepening to determine the best next move.
	 * Additionally, keeps track of the number of pruned subtrees. 
	 * @param bs	beginning board state
	 */
	public void calculateMove(int[] bs) {
	     
		//initialize score and current depth limit
		currScore = -1;
		currDepthLimit = 0;
	     
		/* Get all the possible moves for this player on the provided board state */
		List<Move> possibleMoves = Utils.getAllPossibleMoves(bs, side);
		bestMove = possibleMoves.get(0); //initialize bestMove
		bestScore = Integer.MIN_VALUE;   //initialize bestScore
	     
	    	setMove(bestMove); //set default move in case handler is interrupted before setting best move
	    
		do {
			//increment depth count (initially 0)
	        	if(currDepthLimit < maxDepthLimit) { 
	        		currDepthLimit++; 
	        	}
	        	
	        	//search for board state with best score
	        	currScore = searchGameTree(bs, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
	        	
	        	//set best move
	        	setMove(bestMove);
	        	
			/**
			 * VERBOSE
			 * =======
			 * print three things: 
			 * (1) the best move and its final minimax value
			 * (2) the number of subtrees pruned, at the end of each iteration of iterative deepening and 
			 * (3) the minimax value of the last pruned node (i.e., if a given parent node has a child pruned, 
			 * then save the minimax value of the parent; print the value of the last pruned node during the search), 
			 * at the end of each iterative deepening step. 
			 **/
			if(Utils.verbose) {
				System.out.println(">> Best Move:" + getMove().toString());
				System.out.println(">> Final Minimax Value: " + currScore);
				System.out.println(">> Final Minimax Value: " + currScore);
				System.out.println(">> Subtrees Pruned: " + getPruneCount());
				System.out.println(">> Minimax Value of Last Pruned Node: " + getLastPrunedNodeScore());
			}
		} while (currDepthLimit != maxDepthLimit); //while depth limit has not been reached
	
		return;
	}
	
	/**
	 * Uses alpha-beta pruning in conjunction with iterative deeping to recursively traverse the
	 * future board state subtrees. The method will automatically evaluate & prune subtrees that
	 * are known to have a less desirable "score" as determined by the static board evaluator (sbe).
	 * The algorithm significantly increases time performance/complexity by not evaluting the
	 * child subtrees of pruned board states.
	 * @param currentState	the current board state to be evaluated/scored
	 * @param depth		the current depth of the iterative deeping algorithm
	 * @param alpha		the current alpha player score
	 * @param beta		the current beta player score
	 * @param maxPlayer	if true, it is currently the maxPlayer's turn, else, minPlayer's turn'
	 */
	public int searchGameTree(int[] currentState, int depth, int alpha, int beta, boolean maxPlayer) {
		
		/* Get all the possible moves for this player on the provided board state */
	        List<Move> possibleMoves = Utils.getAllPossibleMoves(currentState, side);
	        
	        /* base cases: 
	         * if no more possible board states 
	         * or we have exceeded max depth limit
	         */
	        if (possibleMoves.isEmpty() || depth >= currDepthLimit) {
			/* Negate the score if not RED */
			if (side == BLK) {
			    return -sbe.evaluate(currentState);
			} else {
			    return sbe.evaluate(currentState);
			}
	        }
	        
	        
    		if (maxPlayer) { //if it is maxPlayer's turn
    			
			for (int i = 0; i < possibleMoves.size(); i++) {
				Move move = possibleMoves.get(i); //set move
		        	Stack<Integer> rv = Utils.execute(currentState, move); //set stack
		        
		        	//recursive call to traverse subtrees
				alpha = Math.max(alpha, searchGameTree(currentState, depth + 1, alpha, beta, false));
	
				if (((alpha > bestScore) || setMove) && depth == 0) {
					//only update best score if alpha was better
					if(alpha > bestScore) { 
						bestScore = alpha; 
					} 
					setMove = false; //allow ability to setMove
					bestMove = possibleMoves.get(i); //set best move;
				}
			
				/* Negate the score if not RED */
				if (side == BLK) {
					alpha = -alpha;
				}
			
				//if able to prune
				if (beta <= alpha) {
					//set last pruned node score
					if (side == BLK) {
						lastPrunedNodeScore = -sbe.evaluate(currentState);
					} else {
						lastPrunedNodeScore = sbe.evaluate(currentState);
					}
		        		pruneCount++;
		        		//revert board state on stack
		        		Utils.revert(currentState, rv); 
		            		break; //beta cut off
	        		} else { 
	        			//revert state on stack
	        			Utils.revert(currentState, rv); 
	        		}
			}
			return alpha;
	
    		} else { //else, it is minPlayer's turn 
			
			for (Move move : possibleMoves) {
				Stack<Integer> rv = Utils.execute(currentState, move);	//set stack

				//recursive call to traverse subtrees
	        		beta = Math.min(beta, searchGameTree(currentState, depth + 1, alpha, beta, true));
	        
	        		//if we have encountered a better move
	        		if( (beta > bestScore) && depth == 1 ) { 
	        			bestScore = beta;	//update best score
					setMove = true;		//set ability to use setMove function to true
				}
					
			        /* Negate the score if not RED */
			        if (side == BLK) {
			        	beta = -beta;
			        }
			        
	        		//if able to prune subtrees
	        		if (beta <= alpha) {
	        			//set last pruned node score
					if (side == BLK) {
						lastPrunedNodeScore = -sbe.evaluate(currentState);
					} else {
						lastPrunedNodeScore = sbe.evaluate(currentState);
					}
	        			pruneCount++;
	        			//revert state on stack
	        			Utils.revert(currentState, rv);
	            			break; //alpha cut off
	        		} else { 
	        			//revert state on stack
	        			Utils.revert(currentState, rv);
	        		}
			}
			return beta;
		
    		}
		
	}
	
	/**
     	* Returns the cumulative number of future board states (subtrees) that have been pruned 
     	* for the most recent deepening iteration. 
     	*/
	public int getPruneCount() {
		return pruneCount;
	}
    
    	/**
     	* Returns the score of the most recently pruned node.
     	*/
	public int getLastPrunedNodeScore() {
		return lastPrunedNodeScore;
	}
     
}
