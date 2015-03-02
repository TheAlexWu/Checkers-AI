package cs540.checkers.bob;

import cs540.checkers.CheckersConsts.*;
import cs540.checkers.Evaluator;

import java.util.*;

/**
 * This Static Board Evaluator (SBE) has been enhanced from the SimpleEvaluator
 * to optimize play strategy by providing heuristics for the AI. It evaluates a board state
 * by awarding points for the following criteria:
 * 	material; each pawn remaining on the board contributes one point,
 * 		  each remaining king remaining on the board contributes two points.
 * 	checkers guarding the back row. 
 * 	moving pieces to the outer squares.
 * 	forming diagonals.
 * 
 * @author Bob (Robert) Wagner
 */
public class EnhancedEvaluator implements Evaluator {
	
	private int[] currentBS = null;	//the current board state to be evaluated
	
	/* SCORE WEIGHTING */
	protected final int PAWN_MATERIAL_SCORE = 400;
	protected final int KING_MATERIAL_SCORE = 1750;
	protected final int REMAINING_GAME_THRESHOLD = 3000;
	protected final int CORNER_BONUS = 300;
	protected final int GUARD_BONUS = 250;
	protected final int TIER_TWO_SIDE_BONUS = 50;
	protected final int TIER_ONE_SIDE_BONUS = 25;
	
	/* Evaluates the current board state based upon the criteria
	 * listed in the class description comment.
	 * @param bs	current board state
	 * @return 	evaluated board score
	 */
	public int evaluate(int[] bs) {
		currentBS = bs;
		return    evaluateMaterials() 
			+ evaluateGuarding() 
			+ evaluateSidePositioning();
	}
	
	private int evaluateMaterials() {
		/* Evaluate materials BEGIN */
		int materialScore = 0;
		int[] pawns = new int[2],
		      kings = new int[2];
		      
		//records the number of each piece {red/black pawn, king}
	        for (int i = 0; i < H * W; i++) {
	        	int v = bs[i];
	            	switch(v) {
	                	case RED_PAWN: //switch fall into next category
	                	case BLK_PAWN:
	                		pawns[v % 4] += 1;
	        			break;
	                	case RED_KING: //switch fall into next category
	                	case BLK_KING:
	                    		kings[v % 4] += 1;
	                		break;
	            	}
	        }
	        /* This formula weights the pieces controlled by each side and adds the resulting 
	         * score to the total. 
	         * The weights were optimized through trial and error due to time constraints, 
	         * but could eventually be updated based upon machine-learned results.
	         */
	        materialScore +=  (PAWN_MATERIAL_SCORE * (pawns[RED] - pawns[BLK]))
	        		+ (KING_MATERIAL_SCORE * (kings[RED] - kings[BLK]));
	        
	        //if nearing end game and/or one side is losing badly, go to corners
	        if(materialScore >= REMAINING_GAME_THRESHOLD) { 
	        	
	        	/* BONUS Corner Scoring */
	            	if (bs[1] == BLK_KING || bs[8] == BLK_KING) {
	            		materialScore -= CORNER_BONUS;
	            	}
	            	if (bs[62] == BLK_KING || bs[55] == BLK_KING) { 
	            		materialScore -= CORNER_BONUS;
	            	}
	            	if (bs[1] == RED_KING || bs[8] == RED_KING) {
	            		materialScore += CORNER_BONUS;
	            	}
	            	if (bs[62] == RED_KING || bs[55] == RED_KING) {
	            		materialScore += CORNER_BONUS;
	            	}
	            	
	        }
	        
	        return materialScore;
	        /* Evaluate materials END */ 
	}
	
	private int evaluateGuard(int[] bs) {
	        /* Evaluate back-row guarding BEGIN */
	        int guardScore = 0;
	        
	        for(int i = 0; i < 4; i++) {
	        	int index = (2*i)+1;
	        	if (bs[index] == BLK_PAWN) {
	        		guardScore -= GUARD_BONUS;
	        	}
	        	if (bs[63-index] == RED_PAWN) {
	        		guardScore += GUARD_BONUS;
	        	}
	        }
	        
	        return guardScore;
	        /* Evaluate back-row guarding END */
	}
	 
	private int evaluateSidePositioning(int[] bs) {       
	        /* Evaulate side position holding BEGIN */
	        int sideScore = 0;
	        
	        //handle tier two bonus side squares
	        for(int i = 0; i < 3; i++) {
	        	int index = 8+(i*16);
	        	if (bs[index] == BLK_PAWN || bs[index] == BLK_KING) {
	        		sideScore -= TIER_TWO_SIDE_BONUS;
	        	}
	        	if (bs[15+index] == BLK_PAWN || bs[15+index] == BLK_PAWN) {
	        		sideScore -= TIER_TWO_SIDE_BONUS;
	        	}
	        	if (bs[index] == RED_PAWN || bs[index] == RED_KING) {
	        		sideScore += TIER_TWO_SIDE_BONUS;
	        	}
	        	if (bs[15+index] == RED_PAWN || bs[15+index] == RED_PAWN) {
	        		sideScore += TIER_TWO_SIDE_BONUS;
	        	}
	        }
	        
	        //handle tier one bonus side squares
	        for(int i = 0; i < 3; i++) {
	        	int index = 14+(i*16);
	        	if (bs[3+index] == BLK_PAWN || bs[3+index] == BLK_KING) {
	        		sideScore -= TIER_TWO_SIDE_BONUS;
	        	}
	        	if (bs[index] == BLK_PAWN || bs[index] == BLK_PAWN) {
	        		sideScore -= TIER_TWO_SIDE_BONUS;
	        	}
	        	if (bs[3+index] == RED_PAWN || bs[3+index] == RED_KING) {
	        		sideScore += TIER_TWO_SIDE_BONUS;
	        	}
	        	if (bs[index] == RED_PAWN || bs[index] == RED_PAWN) {
	        		sideScore += TIER_TWO_SIDE_BONUS;
	        	}
	        }
	        
	        return sideScore;
	        /* Evaulate side position holding END */
	}
		
}
