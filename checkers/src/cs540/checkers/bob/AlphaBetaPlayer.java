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
    
	protected int pruneCount = 0;	//The number of pruned subtrees for the most recent deepening iteration.
	protected int lastPrunedNodeScore = -1;	// The score of the most recently pruned node.
	
	int currDepthLimit;		//current depth limit
	int maxDepthLimit = getDepthLimit(0);
	int currScore;			//current score
	int bestScore;
	
	Move bestMove;
	boolean setMove = false;	//whether a move should be set in the searchGameTree method
	
	protected Evaluator sbe;	//static board evaluator
	
	//constructor
	public AlphaBetaPlayer(String name, int side) { 
	    	super(name, side);
		// Use SimpleEvaluator to score terminal nodes
		sbe = new SimpleEvaluator();
	}
	
	/** Alpha-Beta pruning with Iterative Deepening */
	/* Remember to stop expanding after reaching depthLimit */
	/* Also, remember to count the number of pruned subtrees. */
	public void calculateMove(int[] bs) {
	     
		//initialize score and current depth limit
		currScore = -1;
		currDepthLimit = 0;
	     
		/* Get all the possible moves for this player on the provided board state */
		List<Move> possibleMoves = Utils.getAllPossibleMoves(bs, side);
		bestMove = possibleMoves.get(0); //initialize bestMove
		bestScore = Integer.MIN_VALUE; //initialize bestMove
	     
	    	setMove(bestMove); //set default move in case handler is interrupted before setting best move
	    
		do {
			//increment depth count (initially 0)
	        	if(currDepthLimit < maxDepthLimit) { 
	        		currDepthLimit++; 
	        	}
	        	
	        	//search
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
	
		return; //otherwise, return
	}
	
	public int searchGameTree(int[] currentState, int depth, int alpha, int beta, boolean maxPlayer) {
		
		/* Get all the possible moves for this player on the provided board state */
	        List<Move> possibleMoves = Utils.getAllPossibleMoves(currentState, side);
	        
	        if (possibleMoves.isEmpty() || depth >= currDepthLimit) {
	            /* Negate the score if not RED */
	            if (side == BLK) {
	            	return -sbe.evaluate(currentState);
	            } else {
	            	return sbe.evaluate(currentState);
	            }
			
	    		if (maxPlayer) {
				for (int i = 0; i < possibleMoves.size(); i++) {
					Move move = possibleMoves.get(i); //set move
			        	Stack<Integer> rv = Utils.execute(currentState, move); //set stack
			        
			        	//recursive call
					alpha = Math.max(alpha, searchGameTree(currentState, depth + 1, alpha, beta, false));
		
					if ( ((alpha > bestScore) || setMove) && depth == 0) {
						//only update best score if alpha was better
						if(alpha > bestScore) { 
							bestScore = alpha; 
						} 
						setMove = false; //allow ability to setMove
						bestMove = possibleMoves.get(i);	//set best move;
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
			        		pruneCount++;	//update pruneCount
			        		Utils.revert(currentState, rv); //revert board state
			        		
			            		break; //beta cut off
		        		} else { 
		        			Utils.revert(currentState, rv); 
		        		}
				}
				return alpha;
			} else { //minPlayer
				for (Move move : possibleMoves) {
					Stack<Integer> rv = Utils.execute(currentState, move);	//set stack
	
					//recursive call
		        		beta = Math.min(beta, searchGameTree(currentState, depth + 1, alpha, beta, true));
		        
		        		if( (beta > bestScore) && depth == 1 ) { //beta/move has gotten better
		        			bestScore = beta;	//update best score
						setMove = true;		//set ability to use setMove function to true
					}
						
				        /* Negate the score if not RED */
				        if (side == BLK) {
				        	beta = -beta;
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
		        			Utils.revert(currentState, rv);
		            			break; //alpha cut off
		        		} else { 
		        			Utils.revert(currentState, rv);
		        		}
				}
				return beta;
			}
		}
	}
	
	public int getPruneCount() {
		return pruneCount;
	}
    
	public int getLastPrunedNodeScore() {
		return lastPrunedNodeScore;
	}
     
}
