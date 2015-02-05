import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;
import java.util.PriorityQueue;
public class AStar {
	 
	private Board initialState;
	private Board goalState;
	private AStarHeuristic heuristic;

	public AStar(Board initial, Board goal, AStarHeuristic heur)
	{
		initialState = initial;
		goalState = goal;
		heuristic = heur;
	}

	public void search()
	{
      	/* Declare and initialize Frontier and Explored data structures */ 
		/* Put start node in Fringe list Frontier */
		ArrayList<Frontier> Explored = new ArrayList<Frontier>();
		ArrayList<Frontier> Frontier = new ArrayList<Frontier>();
		Frontier.add(new Frontier(initialState,heuristic.getCost(initialState, goalState),0));
		
		while (!Frontier.isEmpty())
		{
			/* Remove from Frontier list the node n for which f(n) is minimum */
			/* Add n to Explored list*/
			Board n = null;
			int minCost = 99999999;
			int minSortVal = 9999999;
			int index = 0;
			
			for(int h=0; h<Frontier.size(); h++) {
				if(Frontier.get(h).getSortVal() < minSortVal) {
					n = Frontier.get(h).getBoard();
					minSortVal = Frontier.get(h).getSortVal();
					minCost = Frontier.get(h).getCost();
					index = h;
				}
			}
			Frontier.remove(index);
			Explored.add(new Frontier(n,minSortVal,minCost));

			if (n.equals(goalState))
			{
				/* Print the solution path and other required information */
				/* Trace the solution path from goal state to initial state using getParent() function*/
				ArrayList<Board> solutionPath = new ArrayList<Board>();
				solutionPath.add(n); //goal
				int count = 0;
				
				while(n.getParent() != null){ //ordering of solution path: (goal <--> initial)
					n = n.getParent();
					solutionPath.add(n);
					count++;
				}
				
				for(int g=solutionPath.size()-1; g>=0; g--) { //print in reverse order
					solutionPath.get(g).print();
					System.out.println("");
				}
				System.out.println(count);
				System.out.println("");
				System.out.println(Explored.size());
			}

			ArrayList<Board> successors = n.getSuccessors();
			for (int i = 0 ;i<successors.size(); i++)
			{
				Board n1 = successors.get(i);
				n1.setParent(n);
				
				
				boolean existsFrontier = false,existsExplored = false;
				int existsCostF = 999999, existsCostE = 999999;
				int existsEI = 0;
				int existsFI = 0;
			
				for(int z=0; z<Explored.size(); z++) {
					if(Explored.get(z).getBoard().equals(n1)) {
						existsExplored = true;
						existsCostE = Explored.get(z).getCost();
						existsEI = z;
					}
				}
				for(int p=0; p<Frontier.size(); p++) {
					if(Frontier.get(p).getBoard().equals(n1)) {
						existsFrontier = true;
						existsCostF = Frontier.get(p).getCost();
						existsFI = p;
					}
				}
				/* if n1 is not already in either Frontier or Explored
			      Compute h(n1), g(n1) = g(n)+c(n, n1), f(n1)=g(n1)+h(n1), place n1 in Frontier
			   if n1 is already in either Frontier or Explored
			      if g(n1) is lower for the newly generated n1
			          Replace existing n1 with newly generated g(n1), h(n1), set parent of n1 to n
			          if n1 is in Explored list
			              Move n1 from Explored to Frontier list*/
				int h_n1 = heuristic.getCost(n1,goalState);
				int c_n_n1 = 1;
				int g_n1 = minCost + c_n_n1;
				int f_n1 = g_n1 + h_n1;
				if(!existsExplored & !existsFrontier) {
					//n1.setParent(n);
					Frontier.add(new Frontier(n1,f_n1, g_n1));
				} else { //n1 is already in either Frontier or Explored
						if(existsFrontier){
							if(existsCostF > g_n1){
								Frontier.get(existsFI).setCost(f_n1);
								Frontier.get(existsFI).getBoard().setParent(n); //set parent
							}
						} else {
							if(existsCostE > g_n1){
								Explored.remove(existsEI);
								Frontier.add(new Frontier(n1, f_n1,g_n1));
							}
						}					
				}
			}
		}
		System.out.println("No Solution");
	}

}
