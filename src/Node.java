package hw03_jckim_arielv;

public class Node {
	//---------------------------------------
	private int[][] grid;
	private int row, col;
	private int level;
	private int misplacedTiles;
	private String fromDirection;	// This is the direction that was used to get to this Node.
	private Node prevNode;		// the Node this Node came from
	
	//---------------------------------------
	
	public Node(int[][] matrix) {
		grid = new int[3][3];
	//	LastState = new int[3][3];
		setGrid(matrix);
		setRowCol();
		misplacedTiles = 0;
		fromDirection = "none"; 
		level = 0;
		prevNode = null;
	}
	// Copy Constructor
	public Node(Node node) {
		this.row = node.getRow();
		this.col = node.getCol();
		this.level = node.getLevel();
		this.misplacedTiles = node.getMisplacedTiles();
		this.fromDirection = node.getFromDirection();
		this.prevNode = node.getPrevNode();
		this.copyGrid(node.getGrid());
	}
	
	
	//---------------------------------------------BEGIN: GETTERS/SETTERS----------------------------------
	public void setGrid(int[][] matrix) {
		grid = matrix;
	}
	public int[][] getGrid() {
		return grid;
	}
	
	public void setRowCol() {
		outerloop:
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (grid[i][j] == 0) {
					row = i;
					col = j;
					break outerloop;
				}
			}
		}
	}
	public int getRow() {
		return row;
	}
	public int getCol() {
		return col;
	}
	
	public void setLevel(int l) {
		level = l;
	}
	public int getLevel() {
		return level;
	}
	
	public void calculateMisplacedTiles(int[][] GoalState) {
		misplacedTiles = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j ++) {
				if ((GoalState[i][j] != 0) && (GoalState[i][j] != grid[i][j]))
					misplacedTiles++;
			}
		}
	}
	public int getMisplacedTiles() {
		return misplacedTiles;
	}
	
	
	public void setFromDirection(String d) {
		fromDirection = d;
	}
	public String getFromDirection() {
		return fromDirection;
	}
	
	public void setPrevNode(Node node) {
		prevNode = node;
	}
	public Node getPrevNode() {
		return prevNode;
	}
	
	public int getCost(String heuristic, int[][] GoalState) {
		if (heuristic.equalsIgnoreCase("a"))
			return getMisplacedTilesCost(GoalState);
		else if (heuristic.equalsIgnoreCase("a"))
			return getManhattanCost(GoalState);
		return -1;
	}
	public int getManhattanCost(int[][] GoalState) {
		int mCost = 0;
		int x1, x2, y1, y2;
		x1 = x2 = y1 = y2 = 0;
		
		for (int i = 1; i < 9; i++) {
			for (int j = 0; j < 3; j++) {
				for (int k = 0; k < 3; k++) {
					if(grid[j][k] == i) {
						x1 = k;
						y1 = j;
					}
					if(GoalState[j][k] == i) {
						x2 = k;
						y2 = j;
					}
				}
			}
			mCost += (Math.abs(x1-x2)+Math.abs(y1-y2));//finds heuristic value of 2d array
		}
		return (mCost + level);
	}
	public int getMisplacedTilesCost(int[][] GoalState) {
		calculateMisplacedTiles(GoalState);
		return level + misplacedTiles;
	}
	//---------------------------------------------END: GETTERS/SETTERS----------------------------------
	
	//---------------------------------------------BEGIN: BLANK TILE MOVEMENT----------------------------------
	public void moveBlankUp() {
		if (row > 0) {
			grid[row][col] = grid[--row][col];
			grid[row][col] = 0;
			incrementLevel();
			setFromDirection("up");
		}
		else
			System.out.println("----------------------ERROR: CAN'T MOVE BLANK SPACE UP----------------------");
	}
	
	public void moveBlankDown() {
		if (row < 2) {
			grid[row][col] = grid[++row][col];
			grid[row][col] = 0;
			incrementLevel();
			setFromDirection("down");
		}
		else
			System.out.println("----------------------ERROR: CAN'T MOVE BLANK SPACE DOWN----------------------");
	}

	public void moveBlankRight() {
		if (col < 2) {
			grid[row][col] = grid[row][++col];
			grid[row][col] = 0;
			incrementLevel();
			setFromDirection("right");
		}
		else 
			System.out.println("----------------------ERROR: CAN'T MOVE BLANK SPACE RIGHT----------------------");
	}	
	
	public void moveBlankLeft() {
		if (col > 0) {
			grid[row][col] = grid[row][--col];
			grid[row][col] = 0;
			incrementLevel();
			setFromDirection("left");
		}
		else
			System.out.println("----------------------ERROR: CAN'T MOVE BLANK SPACE LEFT----------------------");
	}
	//---------------------------------------------END: BLANK TILE MOVEMENT----------------------------------
	
	//---------------------------------------------BEGIN: BLANK TILE CHECKING----------------------------------
	public boolean checkBlankUp() {
		if (row > 0 && !fromDirection.equalsIgnoreCase("down")) 
			return true;
		return false;
	}
	
	public boolean checkBlankDown() {
		if (row < 2 && !fromDirection.equalsIgnoreCase("up"))
			return true;
		return false;
	}

	public boolean checkBlankRight() {
		if (col < 2 && !fromDirection.equalsIgnoreCase("left"))
			return true;
		return false;
	}	
	
	public boolean checkBlankLeft() {
		if (col > 0 && !fromDirection.equalsIgnoreCase("right"))
			return true;
		return false;
	}
	//---------------------------------------------END: BLANK TILE CHECKING----------------------------------
	
	public void printGrid() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j ++) {
				if (grid[i][j] == 0)
					System.out.print(" _");		// prints whitespace then character : " 8"
				else
					System.out.print(" " + grid[i][j]); 
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public void incrementLevel() {
		level++;
	}
	public void decrementLevel() {
		level--;
	}
	
	public void copyGrid(int[][] g) {
		grid = new int[3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++)
				grid[i][j] = g[i][j];
		}
	}
}