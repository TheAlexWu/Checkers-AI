
public class Frontier {
	
	private Board wrapperBoard;
	private int wrapperCost = 0;
	private int wrapperSortVal;
	
	public Frontier(Board board, int sortVal, int cost) {
		wrapperBoard = board;
		wrapperSortVal = sortVal;
		wrapperCost = cost;
	}
	
	public Board getBoard(){
		return wrapperBoard;
	}
	
	public void setBoard(Board board){
		wrapperBoard = board;
	}
	
	public int getCost(){
		return wrapperCost;
	}
	
	public void setCost(int cost){
		wrapperCost = cost;
	}
	
	public void setSortVal(int val){
		wrapperSortVal = val;
	}
	
	public int getSortVal() {
		return wrapperSortVal;
	}

}
