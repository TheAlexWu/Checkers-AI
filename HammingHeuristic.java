
public class HammingHeuristic implements AStarHeuristic{
	public int getCost(Board state, Board goalState)
	{
		int cost = 0;
		
		//[state.rows,state.columns] should equal [goalState.rows,goalState.columns]
		for (int i = 0 ;i<state.rows; i++) {
			for (int j = 0; j<state.columns; j++)
			{
				if(state.tiles[i][j] != 0) {
					if(state.tiles[i][j] != goalState.tiles[i][j]) {
						cost++;
					}
				}
			}
		}
		return cost;
	}
}

