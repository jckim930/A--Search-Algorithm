package hw03_jckim_arielv;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import hw03_jckim_arielv.*;



public class hw03_jckim_arielv {
	public static boolean isSolvable(int[] initialArray, Map<Integer, Integer> treeMap) {
	    int count = 0; 
	    for (int i = 0; i < 7; i++) {
	    	for (int j = i + 1; j < 8; j++) {
	    		if (treeMap.get(initialArray[i]) < treeMap.get(initialArray[j]))
	    			count++;
	    	}
	    	
	    }
	    return ((count & 1) == 0); 
	} 	
	/*
	public void dynamic(Node n, ArrayList<Node> exploredStates, int searchType) {
		for (Node exploredNode : exploredStates) {
			if (Arrays.deepEquals(n.getGrid(), exploredNode.getGrid()) ) {
				if (searchType == 0) {
					if (n.getMisplacedTilesCost() <= exploredNode.getMisplacedTilesCost() )
						exploredStates.add(n);
				}
				else {
					if (n.getManhattanCost() <= exploredNode.getManhattanCost())
						exploredStates.add(n);		
				}
			}
		}
	}*/
	
	public static void addToNextStates(Node n, ArrayList<Node> ExploredStates, ArrayList<Node> NextStates) {
		boolean isIn = false;
		
		for (Node exploredNode : ExploredStates) {
			if (Arrays.deepEquals(n.getGrid(), exploredNode.getGrid())) {
				isIn = true;
				break;
			}
		}
		if (!isIn)
			NextStates.add(n);
	}
	
	public static void sortNextStates(ArrayList<Node> NextStates, int[][] GoalState, String h) {
		int min = 99999;
		int minIndex = 0;
		for (int i = 0; i < NextStates.size(); i++) {
			if (h.equalsIgnoreCase("a")) {
				if (NextStates.get(i).getMisplacedTilesCost(GoalState) < min) {
					min = NextStates.get(i).getMisplacedTilesCost(GoalState);
					minIndex = i;
				}
			}
			else {
				if (NextStates.get(i).getManhattanCost(GoalState) < min) {
					min = NextStates.get(i).getManhattanCost(GoalState);
					minIndex = i;
				}
			}
		}
		Collections.swap(NextStates, minIndex, 0);
	}
	
	public static void printMoves(Node node) {
		int count = 0;
		Node current = node;
		ArrayList<String> moves = new ArrayList<String>();
		while (current.getPrevNode() != null) {
			moves.add(0, current.getFromDirection());
			current = current.getPrevNode();
			count++;
		}
		
		System.out.println("Solution: ");
		for (String move : moves) {
			System.out.println("Move blank " + move);
		}
		System.out.println("Given the selected heuristic, the solution required " + count + " moves.");
	}
	
	public static void main(String[] args) {
		int[][] initialState = new int[3][3];
		int[][] goalState = new int[3][3];
		String heuristic = "z";
		//----------------------------Getting User Input---------------------------
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the initial state (Enter 0 for the blank tile): ");
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				initialState[i][j] = sc.nextInt();
			}
		}

		System.out.println("Enter the goal state (Enter 0 for the blank tile): ");
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				goalState[i][j] = sc.nextInt();
			}
		}
	
		System.out.println("Select the heuristic method: ");
		System.out.println("a) Number of misplaced tiles");			
		System.out.println("b) Manhattan distance");
		heuristic = sc.nextLine();
		heuristic = sc.nextLine();
		System.out.println(heuristic);
		sc.close();

		
		//----------------------------------------------------------------------------
		Node InitialNode = new Node(initialState);
		
		
		//-------------------------Setting up TreeMap to determine solvability-------------
		int[] initialArray = new int[8];
		int[] goalArray = new int[8];
		Map<Integer, Integer> treeMap = new TreeMap<Integer, Integer>();
		
		int ia = 0;
		int ga = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (InitialNode.getGrid()[i][j] != 0) {
					initialArray[ia] = InitialNode.getGrid()[i][j];
					ia++;
				}
				if (goalState[i][j] != 0) {
					goalArray[ga] = goalState[i][j];
					ga++;
				}
			}
		}
		
		for (int i = 0; i < 8; i++) {
			treeMap.put(goalArray[i], i+1);
		}
		
		boolean solvable = isSolvable(initialArray, treeMap);
		//-------------------------------------------------------------------------------------
		
		
		System.out.println("Grid of Initial State");
		InitialNode.printGrid();
		
		Node GoalNode = new Node(goalState);
		System.out.println("Grid of Goal State");
		GoalNode.printGrid();
		
		ArrayList<Node> ExploredStates = new ArrayList<Node>();
		Node CurrentState = InitialNode;
		ArrayList<Node> NextStates = new ArrayList<Node>();
		
		if (solvable) {
			boolean flag = false;
			while(!flag) { //This should be while(!CurrentNode!=GoalNode || !Impossible)
				if(ExploredStates.size()== 0) {
					CurrentState = InitialNode;
				}
				if (!Arrays.deepEquals(CurrentState.getGrid(), goalState)) {
					ExploredStates.add(CurrentState);
					if(CurrentState.checkBlankDown()) {
						Node downNode = new Node(CurrentState);
						downNode.moveBlankDown();	// Before moving tile, need to check if move would revert to old state
						downNode.calculateMisplacedTiles(goalState); // THIS MAY NEED TO BE CHANGED
						downNode.setPrevNode(CurrentState);
						addToNextStates(downNode, ExploredStates, NextStates);
					}
					if(CurrentState.checkBlankUp()) {
						Node upNode = new Node(CurrentState);
						upNode.moveBlankUp();// Before moving tile, need to check if move would revert to old state
						upNode.calculateMisplacedTiles(goalState);
						upNode.setPrevNode(CurrentState);
						addToNextStates(upNode, ExploredStates, NextStates);
					}
					if(CurrentState.checkBlankLeft()) {
						Node leftNode = new Node(CurrentState);
						leftNode.moveBlankLeft();// Before moving tile, need to check if move would revert to old state
						leftNode.calculateMisplacedTiles(goalState);
						leftNode.setPrevNode(CurrentState);
						addToNextStates(leftNode, ExploredStates, NextStates);
					}
					if(CurrentState.checkBlankRight()) {
						Node rightNode = new Node(CurrentState);
						rightNode.moveBlankRight();// Before moving tile, need to check if move would revert to old state
						rightNode.calculateMisplacedTiles(goalState);
						rightNode.setPrevNode(CurrentState);
						addToNextStates(rightNode, ExploredStates, NextStates);
					}
				}
				else {
					CurrentState.printGrid();
					printMoves(CurrentState);
					System.out.println("Number of nodes Explored: " + ExploredStates.size());
					break;
				}
				
				if (!NextStates.isEmpty()) {
					sortNextStates(NextStates, goalState, heuristic);
					CurrentState = NextStates.remove(0);
				}
			}
		}
		else {
			System.out.println("Board State is unsolvable");	
		}
	}
}