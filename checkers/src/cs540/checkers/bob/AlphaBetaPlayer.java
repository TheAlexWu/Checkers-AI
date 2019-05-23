import java.awt.*;
import java.util.*;
import objectdraw.*;
public class Interface extends FrameWindowController{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Color c = new Color(255,240,214), x = new Color(251,189,126), y = new Color (139,69,19),b = new Color(0,0,0);
	private boolean userTurn = true;
	private Location firstPoint, secondPoint;
	private FilledRect yellow;
	private FilledOval [][]o = new FilledOval [8][8];
	private FilledRect [][]r = new FilledRect [8][8];
	private int a[][], values[][];
	private Text z;
	private static final int WINDOW_SIZE = 822;
	public Interface() {
		resize(WINDOW_SIZE, WINDOW_SIZE+57);
		a = new int[][]{
			{ 0, 1, 0, 1, 0, 1, 0, 1}, //table is the wrong way up
			{ 1, 0, 1, 0, 1, 0, 1, 0},
			{ 0, 1, 0, 1, 0, 1, 0, 1},
			{ 0, 0, 0, 0, 0, 0, 0, 0},
			{ 0, 0, 0, 0, 0, 0, 0, 0},
			{ 2, 0, 2, 0, 2, 0, 2, 0},
			{ 0, 2, 0, 2, 0, 2, 0, 2},
			{ 2, 0, 2, 0, 2, 0, 2, 0},
		};
		values = new int[][]{
			{ 0, 4, 0, 4, 0, 4, 0, 4}, //table is the wrong way up
			{ 4, 0, 3, 0, 3, 0, 3, 0},
			{ 0, 3, 0, 2, 0, 2, 0, 4},
			{ 4, 0, 2, 0, 1, 0, 3, 0},
			{ 0, 3, 0, 1, 0, 2, 0, 4},
			{ 4, 0, 2, 0, 2, 0, 3, 0},
			{ 0, 3, 0, 3, 0, 3, 0, 4},
			{ 4, 0, 4, 0, 4, 0, 4, 0},
		};
		setBoard();
	}
	private int[][] makeHypoMove(int hypoArr[][], int fromRow, int fromCol, int toRow, int toCol) {
		//pre: makes the specified move. Removes the jumped piece from the board.
		//post: returns nothing.
		hypoArr[toRow][toCol] = hypoArr[fromRow][fromCol];
		hypoArr[fromRow][fromCol] = 0;
		if (Math.abs(fromRow - toRow) == 2) {
			hypoArr[(fromRow + toRow)/2][(fromCol + toCol)/2] = 0;
		}
		if (toRow == 0 && hypoArr[toRow][toCol] == 2)
			hypoArr[toRow][toCol] = 4;
		if (toRow == 7 && hypoArr[toRow][toCol] == 1)
			hypoArr[toRow][toCol] = 3;
		return hypoArr;
	}
	private ArrayList<CheckersMove> getHypoLegalMoves(int hypoArr[][], int player) {
		//pre: receives player
		//post: returns an ArrayList containing all the legal MakeMoves for the specified player on the current board. If the player has no legal moves, null is returned.  The value of player should be one of the constants RED or BLACK; if not, null is returned.
		if (player != 1 && player != 2) {
			return null;
		}
		int playerKing;
		if (player == 1) {
			playerKing = 3;
		}
		else {
			playerKing = 4;
		}
		ArrayList<CheckersMove> moves = new ArrayList<CheckersMove>();
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				if (hypoArr[row][col] == player || hypoArr[row][col] == playerKing) {
					if (canJump(player, row, col, row+1, col+1, row+2, col+2)) {
						moves.add(new CheckersMove(row, col, row+2, col+2));
					}
					if (canJump(player, row, col, row-1, col+1, row-2, col+2)) {
						moves.add(new CheckersMove(row, col, row-2, col+2));
					}
					if (canJump(player, row, col, row+1, col-1, row+2, col-2)) {
						moves.add(new CheckersMove(row, col, row+2, col-2));
					}
					if (canJump(player, row, col, row-1, col-1, row-2, col-2)) {
						moves.add(new CheckersMove(row, col, row-2, col-2));	
					}
				}
			}
		}
		if (moves.size() == 0) {
			for (int row = 0; row < 8; row++) {
				for (int col = 0; col < 8; col++) {
					if (hypoArr[row][col] == player || hypoArr[row][col] == playerKing) {
						if (canMove(player, row, col, row+1, col+1)) {
							moves.add(new CheckersMove(row, col, row+1, col+1));
						}
						if (canMove(player, row, col, row-1, col+1)) {
							moves.add(new CheckersMove(row,col,row-1,col+1));
						}
						if (canMove(player, row, col, row+1, col-1)) {
							moves.add(new CheckersMove(row, col, row+1, col-1));
						}
						if (canMove(player, row, col, row-1, col-1)) {
							moves.add(new CheckersMove(row, col, row-1, col-1));
						}
					}
				}
			}
		}
		if (moves.size() == 0) {
			return null;
		}
		return moves;

	}
	private CheckersMove findComputerMove() {
		int hypoArr[][] = copyBoard(a);
		int arr[][] = copyBoard(hypoArr);
		ArrayList<CheckersMove> legalMovesForBlack1;
		Random rand= new Random();
		ArrayList<int[][]> allScenarios = new ArrayList<int[][]>();//RESET BOARD EVERY TIME
		ArrayList<CheckersMove> allMoves = new ArrayList<CheckersMove>();
		ArrayList<CheckersMove> goodMoves = new ArrayList<CheckersMove>();
//		System.out.println("ORIGIONAL BOARD");
//		printArray(a);
		
		legalMovesForBlack1 = getHypoLegalMoves(hypoArr,1);//FINDS ALL LEGAL MOVES FOR BLACK
		if (legalMovesForBlack1 != null) {
			for(int a =0; a < legalMovesForBlack1.size(); a++) {//NOT FINISHED NEED TO GO THROUGH 2nd and 3rd TEIR MOVES
				
				hypoArr = copyBoard(arr);
				CheckersMove tempMove =legalMovesForBlack1.get(a);//COMPUTER PLAYS MOVE
				int hypoArr2[][]=makeHypoMove(hypoArr,tempMove.getStartRow(), tempMove.getStartColumn(), tempMove.getEndRow(), tempMove.getEndColumn());
				
//				System.out.println("COMPUTER MAKES A MOVE");
//				printArray(hypoArr2);
				
				CheckersMove temp = findBestPlayerMove(hypoArr2);//PLAYER PLAYS MOVE
				

				
				if (temp != null) {
					int hypoArr3[][]=makeHypoMove(hypoArr2,temp.getStartRow(),temp.getStartColumn(), temp.getEndRow(), temp.getEndColumn());
//					System.out.println("Player MAKES A MOVE");
//					printArray(hypoArr3);
					
					
					CheckersMove temp1 = findBestComputerMove(hypoArr3);//COMPUTER PLAYS MOVE
					if (temp1 != null) {
						int hypoArr4[][] = makeHypoMove(hypoArr3,temp1.getStartRow(),temp1.getStartColumn(),temp1.getEndRow(),temp1.getEndColumn());
						
//						System.out.println("COMPUTER MAKES A MOVE");
//						printArray(hypoArr4);
						
						CheckersMove temp2 = findBestPlayerMove(hypoArr4);//PlAYER PLAYS MOVE
						if (temp2 != null) {
							int hypoArr5[][]= makeHypoMove(hypoArr4,temp2.getStartRow(),temp2.getStartColumn(),temp2.getEndRow(),temp2.getEndColumn());
							
//							System.out.println("PLAYER MAKES A MOVE");
//							printArray(hypoArr5);
							
							CheckersMove  temp3 = findBestComputerMove(hypoArr5);//COMPUTER PLAYS MOVE
							if (temp3 != null) {
								int hypoArr6[][]=makeHypoMove(hypoArr5,temp3.getStartRow(),temp3.getStartColumn(),temp3.getEndRow(),temp3.getEndColumn());
								
//								System.out.println("COMPUTER MAKES A MOVE");
//								printArray(hypoArr6);
								
								allMoves.add(tempMove);
								allScenarios.add(hypoArr6);
							} else {
								allMoves.add(tempMove);
								allScenarios.add(hypoArr5);
							}
						}else {
							return temp;
						}
					} else {
						allMoves.add(tempMove);
						allScenarios.add(hypoArr3);
					}
				} else {
					//THINKS TOO FAR AHEAD AND DOEASNT WANT TO LOSE
					return tempMove;
				}
			}
		} else {
			isOver();
		}
		double bestScenario = Integer.MIN_VALUE;
		int moveNumber =-1;
		
//		System.out.println("ALL GAME SCORES ARE:");
//		for (int i=0; i< allScenarios.size(); i++) {
//			System.out.println(findGameScore(allScenarios.get(i),1));
//		}
		ArrayList <int[][]>goodScenarios = new ArrayList<int[][]>();
		for (int counter =0; counter < allScenarios.size(); counter++) {
			if (bestScenario < findGameScore(allScenarios.get(counter),1)) {
				bestScenario = findGameScore(allScenarios.get(counter),1);
				moveNumber =counter;
			}
		}
		
		for (int i =0; i < allScenarios.size(); i++) {
			if (findGameScore(allScenarios.get(moveNumber),1)==findGameScore(allScenarios.get(i),1)) {
				goodScenarios.add(allScenarios.get(i));
				goodMoves.add(allMoves.get(i));
			}
		}
//		System.out.println("BEST GAME SCORES ARE");
//		for (int c =0; c< goodScenarios.size(); c++) {
//			System.out.println(findGameScore(goodScenarios.get(c),1));
//		}
		if (moveNumber != -1) {
			return goodMoves.get(rand.nextInt(goodMoves.size()));
		} else {
			return null;
		}
	}
	private CheckersMove findBestPlayerMove(int board[][]) {
		Random rand = new Random();
		ArrayList<CheckersMove> goodMoves = new ArrayList<CheckersMove>();
		int [][]temp = copyBoard(board);
		ArrayList<CheckersMove> legalMovesForWhite1;
		ArrayList<int[][]> allScenarios = new ArrayList<int[][]>();
		ArrayList<CheckersMove> allMoves = new ArrayList<CheckersMove>();
		legalMovesForWhite1 = getHypoLegalMoves(temp,2);//FINDS ALL LEGAL MOVES FOR white
		if (legalMovesForWhite1 != null) {
			for(int a =0; a < legalMovesForWhite1.size(); a++) {
				CheckersMove tempMove =legalMovesForWhite1.get(a);
				allMoves.add(tempMove);
				int hypoArr2[][]=makeHypoMove(temp,tempMove.getStartRow(), tempMove.getStartColumn(), tempMove.getEndRow(), tempMove.getEndColumn());
				allScenarios.add(hypoArr2);
				temp = copyBoard(board);
			}
		}
		double bestScenario = Integer.MIN_VALUE;
		int moveNumber =-1;
		for (int counter =0; counter < allScenarios.size(); counter++) {
			if (bestScenario < findGameScore(allScenarios.get(counter),2)) {
				bestScenario = findGameScore(allScenarios.get(counter),2);
				moveNumber =counter;
			}
		}
		for (int i =0; i < allScenarios.size(); i++) {
			if (findGameScore(allScenarios.get(moveNumber),1)==findGameScore(allScenarios.get(i),1)) {
				goodMoves.add(allMoves.get(i));
			}
		}
		if (moveNumber == -1) {
			return null;
		} else {
			return goodMoves.get(rand.nextInt(goodMoves.size()));
		}
	}
	private CheckersMove findBestComputerMove(int board[][]) {
		Random rand = new Random();
		int [][]temp = copyBoard(board);
		ArrayList<CheckersMove> legalMovesForBlack1;
		ArrayList<CheckersMove> goodMoves = new ArrayList<CheckersMove>();
		ArrayList<int[][]> allScenarios = new ArrayList<int[][]>();
		ArrayList<CheckersMove> allMoves = new ArrayList<CheckersMove>();
		legalMovesForBlack1 = getHypoLegalMoves(temp,1);//FINDS ALL LEGAL MOVES FOR white
		if (legalMovesForBlack1 != null) {
			for(int a =0; a < legalMovesForBlack1.size(); a++) {
				CheckersMove tempMove =legalMovesForBlack1.get(a);
				allMoves.add(tempMove);
				int hypoArr2[][]=makeHypoMove(temp,tempMove.getStartRow(), tempMove.getStartColumn(), tempMove.getEndRow(), tempMove.getEndColumn());
				allScenarios.add(hypoArr2);
				temp = copyBoard(board);
			}
		}
		double bestScenario = Integer.MIN_VALUE;
		int moveNumber =-1;
		for (int counter =0; counter < allScenarios.size(); counter++) {
			if (bestScenario < findGameScore(allScenarios.get(counter),2)) {
				bestScenario = findGameScore(allScenarios.get(counter),2);
				moveNumber =counter;
			}
		}
		for (int i =0; i < allScenarios.size(); i++) {
			if (findGameScore(allScenarios.get(moveNumber),1)==findGameScore(allScenarios.get(i),1)) {
				goodMoves.add(allMoves.get(i));
			}
		}
		if (moveNumber == -1) {
			return null;
		} else {
			return goodMoves.get(rand.nextInt(goodMoves.size()));
		}
	}
	private int getNumKingPieces(int board[][],int player) {
		int playerKing;
		int numKingPieces = 0;
		if (player == 1) {
			playerKing = 3;
		} else {
			playerKing = 4;
		}
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (board[i][j] == playerKing) {
					numKingPieces++;
				}
			}
		}
		return numKingPieces;
	}
	private Boolean isOnBoard(int r, int c) {
		//pre: receives row and column
		//post: returns true otherwise if it's on the board
		if (r < 0 || r >= 8 || c < 0 || c >= 8) {
			return false;
		}
		return true;
	}
	private boolean isOver() {
		int over=0;
		if(getLegalMoves(1)==null&&userTurn==false)
			over=1;
		if(getLegalMoves(2)==null&&userTurn==true)
			over=2;
		if(over==1) {
			canvas.clear();
			Text t = new Text("White Wins", 250,300, canvas);
			t.setFontSize(60);
			return true;
		}
		if(over==2) {
			canvas.clear();
			Text t = new Text("Black Wins", 250,300, canvas);
			t.setFontSize(60);
			return true;
		}
		return false;
	}
	private boolean canMove(int player, int r1, int c1, int r2, int c2) {
		//pre: receives player and the row moved from (r1, c1) and to (r2, c2).
		//post: if the move is legal, returns true.
		if (!isOnBoard(r2, c2)) {
			return false;
		}
		if (a[r2][c2] != 0) {
			return false;
		}
		if (player == 2 && r2 < r1 && r1-r2 == 1 && Math.abs(c1-c2) == 1) {
			return true;
		} else if (player == 1 && r2 > r1 && r2-r1 == 1 && Math.abs(c2-c1) == 1) {
			return true;
		} else if ((a[r1][c1] == 4 || a[r1][c1] == 3 )&& (Math.abs(r2-r1) == 1 && Math.abs(c2-c1) == 1)) {
			return true;
		}
		return false;
	}
	private boolean canJump(int player, int r1, int c1, int r2, int c2, int r3, int c3) {
		//pre: receives player, the square the player is jumping from (r1, c1) and to (r3, c3) and the square between (r2, c2).
		//post: returns true if the player can jump
		if (!isOnBoard(r3,c3) || a[r3][c3] != 0) {
			return false;
		}
		if (player == 1) {
			if (a[r1][c1] == 1) {
				if (r3 < r1 || r3-r1 != 2 || Math.abs(c3-c1) != 2) {
					return false;
				}
			}
			if (a[r2][c2] != 2 && a[r2][c2] != 4) {
				return false;  
			} 
		}
		else {
			if (a[r1][c1] == 2) {
				if (r3 > r1 || r1-r3 != 2 || Math.abs(c3-c1) != 2) {
					return false;
				} 
			}
			if (a[r2][c2] != 1 && a[r2][c2] != 3) {
				return false;  
			}
		}
		if (Math.abs(r3-r1) != 2) {
			return false;
		}
		return true;
	}
	private ArrayList<CheckersMove> getLegalMoves(int player) {
		//pre: receives player
		//post: returns an ArrayList containing all the legal MakeMoves for the specified player on the current board. If the player has no legal moves, null is returned.  The value of player should be one of the constants RED or BLACK; if not, null is returned.
		if (player != 1 && player != 2) {
			return null;
		}
		int playerKing=player+2;
		ArrayList<CheckersMove> moves = new ArrayList<CheckersMove>();
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				if (a[row][col] == player || a[row][col] == playerKing) {
					if (canJump(player, row, col, row+1, col+1, row+2, col+2)) {
						moves.add(new CheckersMove(row, col, row+2, col+2));
					}
					if (canJump(player, row, col, row-1, col+1, row-2, col+2)) {
						moves.add(new CheckersMove(row, col, row-2, col+2));
					}
					if (canJump(player, row, col, row+1, col-1, row+2, col-2)) {
						moves.add(new CheckersMove(row, col, row+2, col-2));
					}
					if (canJump(player, row, col, row-1, col-1, row-2, col-2)) {
						moves.add(new CheckersMove(row, col, row-2, col-2));	
					}
				}
			}
		}
		if (moves.size() == 0) {
			for (int row = 0; row < 8; row++) {
				for (int col = 0; col < 8; col++) {
					if (a[row][col] == player || a[row][col] == playerKing) {
						if (canMove(player, row, col, row+1, col+1)) {
							moves.add(new CheckersMove(row, col, row+1, col+1));
						}
						if (canMove(player, row, col, row-1, col+1)) {
							moves.add(new CheckersMove(row,col,row-1,col+1));
						}
						if (canMove(player, row, col, row+1, col-1)) {
							moves.add(new CheckersMove(row, col, row+1, col-1));
						}
						if (canMove(player, row, col, row-1, col-1)) {
							moves.add(new CheckersMove(row, col, row-1, col-1));
						}
					}
				}
			}
		}
		if (moves.size() == 0) {
			return null;
		}
		return moves;

	}
	private boolean makeMove(int fromRow, int fromCol, int toRow, int toCol) {
		//pre: makes the specified move. Removes the jumped piece from the board.
		//post: returns nothing.
		a[toRow][toCol] = a[fromRow][fromCol];
		a[fromRow][fromCol] = 0;
		if (Math.abs(fromRow - toRow) == 2) {
			a[(fromRow + toRow)/2][(fromCol + toCol)/2] = 0;
		}
		if (toRow == 0 && a[toRow][toCol] == 2) {
			a[toRow][toCol] = 4;
			return true;
		}
		if (toRow == 7 && a[toRow][toCol] == 1) {
			a[toRow][toCol] = 3;
			return true;
		}
		return false;
	}
	private boolean hasPlayerPiece (Location l) { 
		int x = (int)Math.ceil(((l.getX()-80)/80));
		int y = (int)Math.ceil(((l.getY()-80)/80)); //8-?
		//		System.out.println(x+" "+y);
		if((x-1)>7||(x-1)<0||(y-1)>7||(y-1)<0)
			return false;
		if(a[y-1][x-1]==2||a[y-1][x-1]==4)
			return true;
		else
			return false;
	}
	private void setBoard() {
		canvas.clear();
		z = new Text("Illegal Move", 260, 5, canvas);
		z.setFontSize(55);
		z.setFont(Font.SERIF);
		z.setColor(Color.WHITE);
		boolean w = false;
		FilledRect n;
		for(int i=0; i<640; i= i+80) { //loop makes the board
			if(w==true)
				w=false;
			else
				w=true;
			for(int j=0; j<640; j= j+80) {
				n = new FilledRect(i+80,j+80,80,80, canvas);
				r[i/80][j/80]=n;
				if (w==true) {
					n.setColor(x);
					w=false;
				}
				else{
					n.setColor(y);
					w=true;
				}
			}
		} 
		FilledOval n2;
		for(int i =0; i<8; i++) { //loop to make the pieces
			for(int j=0; j<8; j++) {
				if(a[j][i]==2) {
					n2 = new FilledOval((i*80)+95,(j*80)+95,50,50, canvas);
					n2.setColor(c);
					o[i][j]=n2;
				}
				else if(a[j][i]==1) {
					n2 = new FilledOval((i*80)+95,(j*80)+95,50,50, canvas);
					n2.setColor(b);
					o[i][j]=n2;
				}
				else if(a[j][i]==3) {
					n2 = new FilledOval((i*80)+87.5,(j*80)+87.5,65,65, canvas);//or +85 with 70
					Text t = new Text('K',(i*80)+102,(j*80)+90,canvas);
					t.setColor(Color.WHITE);
					t.setFontSize(50);
					n2.setColor(b);
					o[i][j]=n2;
				}
				else if(a[j][i]==4) {
					n2 = new FilledOval((i*80)+87.5,(j*80)+87.5,65,65, canvas);//or +85 with 70
					Text t = new Text('K',(i*80)+102,(j*80)+90,canvas);
					t.setColor(Color.BLACK);
					t.setFontSize(50);
					n2.setColor(c);
					o[i][j]=n2;
				}
				else {
					o[i][j]=new FilledOval (40, 40, 1, 1, canvas);
					o[i][j].setColor(Color.WHITE);
				}
			}
		} //end of loop
	}
	public void onMousePress(Location point) { 
		//auto-calls whenever a click is made
		Integer fromRow=null, fromCol=null, toRow=null, toCol=null;
		z.setColor(Color.WHITE);
		if(userTurn==true) { //makes sure it can't be called out of turn
			if(yellow!=null&&yellow.contains(point)) {
				yellow.setColor(y);
				yellow=null;
				firstPoint=null;
			}
			else {
				if (firstPoint==null&&hasPlayerPiece(point)==true) {  //first time onMousePress is called
					firstPoint=point;
					for(int i=0; i<8; i++) { //highlights in yellow, unimportant
						for(int j=0; j<8; j++) {
							if(r[i][j].contains(point)&&r[i][j].contains(o[i][j].getLocation())) {
								r[i][j].setColor(Color.YELLOW);
								yellow=r[i][j];
							}
						}
					}
				}
				else if(secondPoint==null&&firstPoint!=null) { //second time onMousePress is called
					boolean found=false;
					secondPoint=point;
					for(int i=0; i<8; i++) {
						for(int j=0; j<8; j++) {
							if(r[i][j].contains(firstPoint)) {
								fromRow=i;
								fromCol=j;
								found=true;
							}
						}
					}
					if(found==true) {
						found=false;
						for(int i=0; i<8; i++) {
							for(int j=0; j<8; j++) {
								if(r[i][j].contains(secondPoint)) {
									toRow=i;
									toCol=j;
									found=true;
								}
							}
						}
						if(fromRow!=null&&fromCol!=null&&toRow!=null&&toCol!=null&&found==true) {
							ArrayList <CheckersMove> b =getLegalMoves(2);
							boolean f=false;
							for(int i=0; i<b.size(); i++) {
								if(b.get(i).equals(new CheckersMove(fromCol, fromRow, toCol, toRow))){
									f=true;
								}
							}
							if(f){
								makeMove ( fromCol,fromRow,  toCol,toRow);
								boolean trueFalse = true;
								setBoard();
								firstPoint=null;
								while (userTurn) {
									userTurn = false;
									if (getLegalMoves(2) != null) {
										for (int i = 0; i < getLegalMoves(2).size(); i++) {
											if (Math.abs(fromCol-toCol) == 2 && Math.abs(fromRow-toRow) == 2 && getLegalMoves(2).get(i).startRow == toCol && getLegalMoves(2).get(i).startColumn == toRow && getLegalMoves(2).size() != 0 && Math.abs(getLegalMoves(2).get(i).startRow - getLegalMoves(2).get(i).endRow) == 2 && Math.abs(getLegalMoves(2).get(i).endColumn - getLegalMoves(2).get(i).startColumn) == 2) {
												makeMove(getLegalMoves(2).get(i).startRow, getLegalMoves(2).get(i).startColumn, getLegalMoves(2).get(i).endRow, getLegalMoves(2).get(i).endColumn);
												userTurn = true;
											}
										}
									}
								}
								if (trueFalse) {	
									userTurn = false;
									if (isOver()) {
										return;
									}
									CheckersMove c = findComputerMove();
									if (c != null) {
										userTurn = makeMove(c.startRow, c.startColumn, c.endRow, c.endColumn);
										setBoard();
									}
									while (!userTurn) {
										userTurn=true;
										if (getLegalMoves(1) != null&&c!=null) {
											for (int i = 0; i < getLegalMoves(1).size(); i++) {
												if (Math.abs(c.startColumn-c.endColumn) == 2 && Math.abs(c.startRow-c.endRow) == 2 && getLegalMoves(1).get(i).startRow == c.endRow && getLegalMoves(1).get(i).startColumn == c.endColumn && getLegalMoves(1).size() != 0 && Math.abs(getLegalMoves(1).get(i).startRow - getLegalMoves(1).get(i).endRow) == 2 && Math.abs(getLegalMoves(1).get(i).endColumn - getLegalMoves(1).get(i).startColumn) == 2) {
													userTurn = false;
													c = getLegalMoves(1).get(i);
													if (makeMove(c.startRow, c.startColumn, c.endRow, c.endColumn)) {
														userTurn = true;
													}
													setBoard();
												}
											}
										}
									}
									if (isOver()) {
										return;
									}
								}
							}
							else{
								z.setColor(Color.BLACK);
							}	
						}
					}
				}
				secondPoint=null;
			}
		}
	}
	public int getNumKingPieces(int player) {
		int playerKing;
		int numKingPieces = 0;
		if (player == 1) {
			playerKing = 3;
		} else {
			playerKing = 4;
		}
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (a[i][j] == playerKing) {
					numKingPieces++;
				}
			}
		}
		return numKingPieces;
	}
	private int getNumPieces(int board[][],int player) {
		int numPieces = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (board[i][j] == player) {
					numPieces++;
				}
			}
		}
		return numPieces;
	}
	private double findGameScore(int board[][], int player) {
		int other;
		if(player==1)
			other=2;
		else
			other=1;
		double kingWeight = 8;
		double result = 0;
		result = getNumKingPieces(board, player) * kingWeight + getNumPieces(board,player) - getNumKingPieces(board,other) * kingWeight - getNumPieces(board,other); 
		for(int i=0; i<8; i++)
			for(int j=0; j<8; j++)
				if(board[i][j]==player)
					result=result+values[i][j];
		for(int i=0; i<8; i++)
			for(int j=0; j<8; j++)
				if(board[i][j]==other)
					result=result-values[i][j];
		return result;
	}
	private int[][] copyBoard (int [][]b){
		int arr[][] = new int[b.length][b[0].length];
		for (int r =0; r<b.length;r++) {
			for (int c =0; c<b[0].length; c++) {
				arr[r][c]=b[r][c];
			}
		}
		return arr;
	}
	public static void printArray(int[][]a) {
		//pre: Takes in an array
		//post: Prints out the array
		for(int i = 0; i<a.length; i++)//goes through the rows of the array
		{
			for(int j = 0; j<a[i].length; j++)//goes through the columns of the array
			{
				System.out.print(a[i][j]+"   ");//prints out the string based on the array
			}
			System.out.println();//new line once you get to the end of the row
		}
	}
}
