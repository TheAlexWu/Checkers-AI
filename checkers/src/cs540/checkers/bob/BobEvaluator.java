package cs540.checkers.bob;

import java.util.*;

import static cs540.checkers.CheckersConsts.*;
import cs540.checkers.Evaluator;


/**
 * This Enhanced Static Board Evaluator:
 * - assigns points for material.  Each pawn remaining on the board contributes one point,
 * and each remaining king remaining on the board contributes two points.
 * - assigns points for having checkers guarding the back row. 
 * - assigns points for moving pieces to the outer squares
 * - assigns points for forming diagonals
 */
public class BobEvaluator implements Evaluator {
	
	public int evaluate(int[] bs) {
		
		int totalScore = 0;
		
		/** MATERIALS */
		int materialScore = 0;
		int[] pawns = new int[2],
		      kings = new int[2];

        for (int i = 0; i < H * W; i++) {
        	
        	int v = bs[i];
            switch(v) {
            
                case RED_PAWN: //fall into next category
                case BLK_PAWN:
                	pawns[v % 4] += 1;
                break;
                case RED_KING: //fall into next category
                case BLK_KING:
                    kings[v % 4] += 1;
                break;
            }
         
        }
        
        materialScore += 400 * (pawns[RED] - pawns[BLK]) + 1750 * (kings[RED] - kings[BLK]);
        
        //if nearing end game and/or one side is losing badly, go to corners
        boolean doForRestOfGame = false;
        if(materialScore >= 3000) { 
        	doForRestOfGame = true; 
        }
        	
        if(doForRestOfGame) { //incorporate this scoring for rest of game
        	
        	/** BONUS Corner Score */
            int cornerScore = 0;

            if (bs[1] == BLK_KING || bs[8] == BLK_KING) cornerScore -= 300;
            if (bs[62] == BLK_KING || bs[55] == BLK_KING) cornerScore -= 300;
            
            if (bs[1] == RED_KING || bs[8] == RED_KING) cornerScore += 300;
            if (bs[62] == RED_KING || bs[55] == RED_KING) cornerScore += 300;
            
            totalScore += cornerScore;
            ///END///
        	
        }
        
        totalScore += materialScore;
        ///END///
        
        /** BACK ROW GUARDS */
        int guardScore = 0;
        
        if (bs[1] == BLK_PAWN) guardScore -= 250;
        if (bs[3] == BLK_PAWN) guardScore -= 250;
        if (bs[5] == BLK_PAWN) guardScore -= 250;
        if (bs[7] == BLK_PAWN) guardScore -= 250;

        if (bs[56] == RED_PAWN) guardScore += 250;
        if (bs[58] == RED_PAWN) guardScore += 250;
        if (bs[60] == RED_PAWN) guardScore += 250;
        if (bs[62] == RED_PAWN) guardScore += 250;
        
        totalScore += guardScore;
        ///END///
        
        /** MOVE TO SIDES */
        int sideScore = 0;
        
        if (bs[8] == BLK_PAWN || bs[8] == BLK_KING) sideScore -= 50;
        if (bs[24] == BLK_PAWN || bs[24] == BLK_KING) sideScore -= 50;
        if (bs[40] == BLK_PAWN || bs[40] == BLK_KING) sideScore -= 50;
        if (bs[23] == BLK_PAWN || bs[23] == BLK_KING) sideScore -= 50;
        if (bs[39] == BLK_PAWN || bs[39] == BLK_KING) sideScore -= 50;
        if (bs[55] == BLK_PAWN || bs[55] == BLK_KING) sideScore -= 50;
        
        if (bs[17] == BLK_PAWN || bs[17] == BLK_KING) sideScore -= 25;
        if (bs[33] == BLK_PAWN || bs[33] == BLK_KING) sideScore -= 25;
        if (bs[49] == BLK_PAWN || bs[49] == BLK_KING) sideScore -= 25;
        if (bs[14] == BLK_PAWN || bs[14] == BLK_KING) sideScore -= 25;
        if (bs[30] == BLK_PAWN || bs[30] == BLK_KING) sideScore -= 25;
        if (bs[46] == BLK_PAWN || bs[46] == BLK_KING) sideScore -= 25;
        
        if (bs[8] == RED_PAWN || bs[8] == RED_KING) sideScore += 50;
        if (bs[24] == RED_PAWN || bs[24] == RED_KING) sideScore += 50;
        if (bs[40] == RED_PAWN || bs[40] == RED_KING) sideScore += 50;
        if (bs[23] == RED_PAWN || bs[23] == RED_KING) sideScore += 50;
        if (bs[39] == RED_PAWN || bs[39] == RED_KING) sideScore += 50;
        if (bs[55] == RED_PAWN || bs[55] == RED_KING) sideScore += 50;
       
        if (bs[17] == RED_PAWN || bs[17] == RED_KING) sideScore += 25;
        if (bs[33] == RED_PAWN || bs[33] == RED_KING) sideScore += 25;
        if (bs[49] == RED_PAWN || bs[49] == RED_KING) sideScore += 25;
        if (bs[14] == RED_PAWN || bs[14] == RED_KING) sideScore += 25;
        if (bs[30] == RED_PAWN || bs[30] == RED_KING) sideScore += 25;
        if (bs[46] == RED_PAWN || bs[46] == RED_KING) sideScore += 25;
        
        totalScore += sideScore;
        ///END///
        
        return totalScore;
	
	 }
		
}


