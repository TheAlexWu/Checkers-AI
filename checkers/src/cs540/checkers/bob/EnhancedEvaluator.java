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
		
		int totalScore = 0;	//overall score of the current board
		
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
	        materialScore +=  PAWN_MATERIAL_SCORE * (pawns[RED] - pawns[BLK]) 
	        		+ KING_MATERIAL_SCORE * (kings[RED] - kings[BLK]);
	        
	        //if nearing end game and/or one side is losing badly, go to corners
	        if(materialScore >= REMAINING_GAME_THRESHOLD) { 
	        	/* BONUS Corner Scoring */
	            	if (bs[1] == BLK_KING || bs[8] == BLK_KING) 	totalScore -= CORNER_BONUS;
	            	if (bs[62] == BLK_KING || bs[55] == BLK_KING) 	totalScore -= CORNER_BONUS;
	            
	            	if (bs[1] == RED_KING || bs[8] == RED_KING) 	totalScore += CORNER_BONUS;
	            	if (bs[62] == RED_KING || bs[55] == RED_KING) 	totalScore += CORNER_BONUS;
	        }
	        
	        totalScore += materialScore;
	        /* Evaluate materials END */ 
	        
	        /* Evaluate back-row guarding BEGIN */
	        int guardScore = 0;
	        
	        if (bs[1] == BLK_PAWN) guardScore -= GUARD_BONUS;
	        if (bs[3] == BLK_PAWN) guardScore -= GUARD_BONUS;
	        if (bs[5] == BLK_PAWN) guardScore -= GUARD_BONUS;
	        if (bs[7] == BLK_PAWN) guardScore -= GUARD_BONUS;
	
	        if (bs[56] == RED_PAWN) guardScore += GUARD_BONUS;
	        if (bs[58] == RED_PAWN) guardScore += GUARD_BONUS;
	        if (bs[60] == RED_PAWN) guardScore += GUARD_BONUS;
	        if (bs[62] == RED_PAWN) guardScore += GUARD_BONUS;
	        
	        totalScore += guardScore;
	        /* Evaluate back-row guarding END */
	        
	        /* Evaulate side position holding BEGIN */
	        int sideScore = 0;
	        
	        if (bs[8] == BLK_PAWN || bs[8] == BLK_KING)  	sideScore -= TIER_TWO_SIDE_BONUS;
	        if (bs[24] == BLK_PAWN || bs[24] == BLK_KING) 	sideScore -= TIER_TWO_SIDE_BONUS;
	        if (bs[40] == BLK_PAWN || bs[40] == BLK_KING) 	sideScore -= TIER_TWO_SIDE_BONUS;
	        if (bs[23] == BLK_PAWN || bs[23] == BLK_KING) 	sideScore -= TIER_TWO_SIDE_BONUS;
	        if (bs[39] == BLK_PAWN || bs[39] == BLK_KING) 	sideScore -= TIER_TWO_SIDE_BONUS;
	        if (bs[55] == BLK_PAWN || bs[55] == BLK_KING) 	sideScore -= TIER_TWO_SIDE_BONUS;
	        
	        if (bs[17] == BLK_PAWN || bs[17] == BLK_KING) 	sideScore -= TIER_ONE_SIDE_BONUS;
	        if (bs[33] == BLK_PAWN || bs[33] == BLK_KING) 	sideScore -= TIER_ONE_SIDE_BONUS;
	        if (bs[49] == BLK_PAWN || bs[49] == BLK_KING) 	sideScore -= TIER_ONE_SIDE_BONUS;
	        if (bs[14] == BLK_PAWN || bs[14] == BLK_KING) 	sideScore -= TIER_ONE_SIDE_BONUS;
	        if (bs[30] == BLK_PAWN || bs[30] == BLK_KING) 	sideScore -= TIER_ONE_SIDE_BONUS;
	        if (bs[46] == BLK_PAWN || bs[46] == BLK_KING) 	sideScore -= TIER_ONE_SIDE_BONUS;
	        
	        if (bs[8] == RED_PAWN || bs[8] == RED_KING)   	sideScore += TIER_TWO_SIDE_BONUS;
	        if (bs[24] == RED_PAWN || bs[24] == RED_KING) 	sideScore += TIER_TWO_SIDE_BONUS;
	        if (bs[40] == RED_PAWN || bs[40] == RED_KING) 	sideScore += TIER_TWO_SIDE_BONUS;
	        if (bs[23] == RED_PAWN || bs[23] == RED_KING) 	sideScore += TIER_TWO_SIDE_BONUS;
	        if (bs[39] == RED_PAWN || bs[39] == RED_KING) 	sideScore += TIER_TWO_SIDE_BONUS;
	        if (bs[55] == RED_PAWN || bs[55] == RED_KING) 	sideScore += TIER_TWO_SIDE_BONUS;
	       
	        if (bs[17] == RED_PAWN || bs[17] == RED_KING) 	sideScore += TIER_ONE_SIDE_BONUS;
	        if (bs[33] == RED_PAWN || bs[33] == RED_KING) 	sideScore += TIER_ONE_SIDE_BONUS;
	        if (bs[49] == RED_PAWN || bs[49] == RED_KING) 	sideScore += TIER_ONE_SIDE_BONUS;
	        if (bs[14] == RED_PAWN || bs[14] == RED_KING) 	sideScore += TIER_ONE_SIDE_BONUS;
	        if (bs[30] == RED_PAWN || bs[30] == RED_KING) 	sideScore += TIER_ONE_SIDE_BONUS;
	        if (bs[46] == RED_PAWN || bs[46] == RED_KING) 	sideScore += TIER_ONE_SIDE_BONUS;
	        
	        totalScore += sideScore;
	        /* Evaulate side position holding END */
	        
	        return totalScore;
	}
		
}


