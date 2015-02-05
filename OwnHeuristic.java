
public class OwnHeuristic implements AStarHeuristic{
	//cost is equal to the number of tiles out of row + the number of tiles out of column
	// heuristic is admissible
	public int getCost(Board state, Board goalState)
	{
		int cost = 0;
		int[] goal = new int[2];
		
		//[state.rows,state.columns] should equal [goalState.rows,goalState.columns]
		for (int i = 0 ;i<state.rows; i++) {
			for (int j = 0; j<state.columns; j++) {
				
				switch(state.tiles[i][j]) {
				
				case 0: goal[0]=i; //dont count 0
						goal[1]=j;
						break;
				case 1: goal = findNumCoordinates(goalState, 1);
						break;
				case 2: goal = findNumCoordinates(goalState, 2);
						break;
				case 3: goal = findNumCoordinates(goalState, 3);
						break;
				case 4: goal = findNumCoordinates(goalState, 4);
						break;
				case 5: goal = findNumCoordinates(goalState, 5);
						break;
				case 6: goal = findNumCoordinates(goalState, 6);
						break;
				case 7: goal = findNumCoordinates(goalState, 7);
						break;
				case 8: goal = findNumCoordinates(goalState, 8);
						break;
				case 9: goal = findNumCoordinates(goalState, 9);
						break;
				case 10:goal = findNumCoordinates(goalState, 10);
						break;
				case 11:goal = findNumCoordinates(goalState, 11);
						break;
				case 12:goal = findNumCoordinates(goalState, 12);
						break;
				case -1:goal[0]=i;
						goal[1]=j;
						break;
				default: //shouldn't ever reach here, but for debugging
						System.out.println("ERROR IN OWN HEURISTIC STATE MATRIX");
						break;
				
				}
				if(i != goal[0]) {
					cost++;
				}
				if(j != goal[1]) {
					cost++;
				}
			}
		}
		return cost;
	}
	
	private int[] findNumCoordinates(Board board, int num) {
		
		int[] coordinates = new int[2]; //coordinates[0] = row, coordinates[1] = column
		boolean done = false;
		
		for (int i = 0 ;i<board.rows & !done; i++){
			for (int j = 0; j<board.columns & !done; j++) {
				if(board.tiles[i][j] == num){
					coordinates[0] = i;
					coordinates[1] = j;
					done = true;
				}
			}
		}
		
		return coordinates;
	
	}
	
}

