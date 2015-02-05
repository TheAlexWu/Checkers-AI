import java.util.ArrayList;
public class Board {
	public static int rows=5;
	public static int columns=3;
	private Board parent = null; /* only initial board's parent is null */
	public int[][] tiles;

	public Board(int[][] cells)                 //Populate states
	{
	  tiles = new int[rows][columns];
		for (int i = 0 ;i<rows; i++)
			for (int j = 0; j<columns; j++)
			{
				tiles[i][j] = cells[i][j];
			}
	}
	public boolean equals(Object x)         //Can be used for repeated state checking
	{
		Board another = (Board)x;
		if (columns != another.columns || rows != another.rows) return false;
		for (int i = 0; i<rows; i++)
			for (int j = 0; j<columns; j++)
				if (tiles[i][j] != another.tiles[i][j])
					return false;
		return true;
	}
	public ArrayList<Board> getSuccessors()     //Use cyclic ordering for expanding nodes - Right, Down, Left, Up
	{
		
		ArrayList<Board> successors = new ArrayList<Board>();
		Board tempBoard = new Board(tiles);
		int[] coords;
		
		// 	if 0 has three switch options... {[2,0],[2,2]}
		//	else, only has two switch options...
			coords = findZeroCoordinates(tiles);
			
			if(coords[1] != 2) {
				if(tiles[coords[0]][coords[1]+1] != -1) { //east
					tempBoard.tiles[coords[0]][coords[1]] = tiles[coords[0]][coords[1]+1];
					tempBoard.tiles[coords[0]][coords[1]+1] = 0;
					successors.add(tempBoard);
					tempBoard = new Board(tiles);
				}
			}
			if(coords[0] != 4) {
				if(tiles[coords[0]+1][coords[1]] != -1) { //south
					tempBoard.tiles[coords[0]][coords[1]] = tiles[coords[0]+1][coords[1]]; //swap
					tempBoard.tiles[coords[0]+1][coords[1]] = 0; //values
					successors.add(tempBoard); //add to successors list
					tempBoard = new Board(tiles);
				}
			}
			if(coords[1] != 0) {
				if(tiles[coords[0]][coords[1]-1] != -1) { //west
					tempBoard.tiles[coords[0]][coords[1]] = tiles[coords[0]][coords[1]-1]; //swap
					tempBoard.tiles[coords[0]][coords[1]-1] = 0; //values
					successors.add(tempBoard); //add to successors list
					tempBoard = new Board(tiles);
				}
			}
			if(coords[0] != 0) {
				if(tiles[coords[0]-1][coords[1]] != -1) { //north
					tempBoard.tiles[coords[0]][coords[1]] = tiles[coords[0]-1][coords[1]]; //swap
					tempBoard.tiles[coords[0]-1][coords[1]] = 0; //values
					successors.add(tempBoard); //add to successors list
				}
			}
					
		return successors;
		
	}
	
	public void print()
	{
		for (int i = 0; i<rows; i++)
		{
			for (int j = 0 ;j<columns; j++)
			{
				if (j > 0) System.out.print("\t");
				System.out.print(tiles[i][j]);
			}
			System.out.println();
		}
	}
	
	public void setParent(Board parentBoard)
	{
		parent = parentBoard;
	}
	
	public Board getParent()
	{
		return parent;
	}
	
	private int[] findZeroCoordinates(int[][] board) {
	
		int[] coordinates = new int[2]; //coordinates[0] = row, coordinates[1] = column
		boolean done = false;
		
		for (int i = 0 ;i<rows & !done; i++){
			for (int j = 0; j<columns & !done; j++) {
				if(tiles[i][j] == 0){
					coordinates[0] = i;
					coordinates[1] = j;
					done = true;
				}
			}
		}
		
		return coordinates;
	
	}
	
}
