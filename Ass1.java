

import java.io.File;  
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

public class Ass1 
{
	static List<Move> moves = new ArrayList<Move>();
	
	static int CountRowMismatches(Vector<String> first, Vector<String> second) 
	{
		int numberOfMismatches = 0;

		for (int i = 0; i < first.size(); i++)
			if (first.get(i).charAt(2) != second.get(i).charAt(0))
				numberOfMismatches++;

		return numberOfMismatches ;
	}
	
	static int CountColumnMismatches(Vector<String> first, Vector<String> second) 
	{
		int numberOfMismatches = 0;

		for (int i = 0; i < first.size(); i++)
			if (first.get(i).charAt(1) != second.get(i).charAt(3))
				numberOfMismatches++;

		return numberOfMismatches;
	}
	
	public static void main(String[] args) 
	{
		Vector<Vector<String>> puzzlePieces = new Vector<Vector<String>>();
		
		try 
		{
			String inputFilePath = "C:/Users/abdul/COEN432Ass1/Ass1Output.txt"; //"C:/Users/joeym/OneDrive/Desktop/Work/COEN_432/Ass1/Ass1Output.txt";
			File inputFile = new File(inputFilePath);
			Scanner myLineReader = new Scanner(inputFile);
		     
			if (myLineReader.hasNextLine())
				myLineReader.nextLine();
		      
		    while (myLineReader.hasNextLine()) 
		    {
		    	Vector<String> row = new Vector<String>();
		        
		    	Scanner myWordReader = new Scanner(myLineReader.nextLine());
		    	 
		    	while (myWordReader.hasNext()) 
		    		row.add(myWordReader.next());
		        
		    	puzzlePieces.add(row);
		    }
		      
		    myLineReader.close();
		} 
		catch (FileNotFoundException e) 
		{
			System.out.println("An error occurred.");
		    e.printStackTrace();
		}
		 
		final int RowSize = 8;
	    final int ColumnSize = 8;
		 
		int numberOfMismatches = 0;
		 
		for (int i = 0; i < RowSize - 1; i++)
			numberOfMismatches += CountRowMismatches(puzzlePieces.get(i), puzzlePieces.get(i + 1));
			
		for (int i = 0; i < ColumnSize - 1; i++)
	    {
			Vector<String> firstColumn = new Vector<String>();
			Vector<String> secondColumn = new Vector<String>();

			for (int j = 0; j < RowSize; j++)
			{
				firstColumn.add(puzzlePieces.get(j).get(i));
				secondColumn.add(puzzlePieces.get(j).get(i + 1));
			}
            
			numberOfMismatches += CountColumnMismatches(firstColumn, secondColumn);
	    }
    	    	 
		System.out.println("Number of mismatches : " + numberOfMismatches);

		System.out.println("Solving puzzle : ");
		solvePuzzle(puzzlePieces);

		// final int RowSize = 8;
	    // final int ColumnSize = 8;
		 
		numberOfMismatches = 0;
		 
		for (int i = 0; i < RowSize - 1; i++)
			numberOfMismatches += CountRowMismatches(puzzlePieces.get(i), puzzlePieces.get(i + 1));
			
		for (int i = 0; i < ColumnSize - 1; i++)
	    {
			Vector<String> firstColumn = new Vector<String>();
			Vector<String> secondColumn = new Vector<String>();

			for (int j = 0; j < RowSize; j++)
			{
				firstColumn.add(puzzlePieces.get(j).get(i));
				secondColumn.add(puzzlePieces.get(j).get(i + 1));
			}
            
			numberOfMismatches += CountColumnMismatches(firstColumn, secondColumn);
	    }

		System.out.println("Number of mismatches : " + numberOfMismatches);
	}
	
	private static void solvePuzzle(Vector<Vector<String>> puzzlePieces) {
		for(Vector<String> row: puzzlePieces) {
			for(int i = 0; i < row.size() - 1; i++) {
				String left = row.get(i); // current column or LEFT
				String right = row.get(i + 1); // next volumn or RIGHT
				
				System.out.println("LEFT : " + left + ", RIGHT: " + right);
				
				Map<String, Move> newMoves = tilesEdgeMatch(left, right);
				if(newMoves.size() > 0) {
					if(newMoves.containsKey("LEFT")){
						Move m = newMoves.get("LEFT");
						row.set(i, m.altered);
						moves.add(m);
					}

					if(newMoves.containsKey("RIGHT")){
						Move m = newMoves.get("RIGHT");
						row.set(i + 1, m.altered);
						moves.add(m);
					}
				} else {
					System.out.println("MATCH NOT FOUND OR ROTATION NOT NEEDED -> {" + left + ", " + right + "}");
				}
			}
		}

		System.out.println("Done solving");
	}	

	private static Map<String, Move> tilesEdgeMatch(String left, String right) {
		//Boolean matched = false;
		Map<String, Move> newMoves = new HashMap<String, Move>();

		// basically im thinking if the numbers are on opposite sides of their tile
		// then both tiles have to rotate or else there wont be a match
		// this is also the case for tiles with values in bottom or top
		int indexOfFirstMatchedEdge = -1;
		int indexOfSecondMatchedEdge = -1;
		for(int i = 0; i < left.length(); i++) {
			indexOfFirstMatchedEdge = i;//right.indexOf(left.charAt(i));
			indexOfSecondMatchedEdge = right.indexOf(left.charAt(i));;
			if(indexOfSecondMatchedEdge > -1) {
				// match found
				// break out of for loop
				//matched = true;
				break;
			}
		}
		
		

		if(indexOfSecondMatchedEdge > -1) {
			System.out.println(String.format("indexes {%d, %d} values {%s, %s}", indexOfFirstMatchedEdge, indexOfSecondMatchedEdge,  String.valueOf(left.charAt(indexOfFirstMatchedEdge)),  String.valueOf(right.charAt(indexOfSecondMatchedEdge))));// 	return newMoves;

			// rotate v1 (right rotation) until the matched number is in index 3
			String matchedEdge = String.valueOf(left.charAt(indexOfFirstMatchedEdge));
			
			Move mvLeft = rotateRight(left, matchedEdge); // counter intuitive but the left value we want to rotate right
			Move mvRight = rotateLeft(right, matchedEdge); // and right value to the left

			if(mvLeft.moves > 0)
				newMoves.put("LEFT",mvLeft);

			if(mvRight.moves > 0)
				newMoves.put("RIGHT",mvRight);
		}

		return newMoves;
	}

	private static Move rotateRight(String toRotate, String matchedNumber) {
		Move mv = new Move(toRotate);
		char[] rotateArr = toRotate.toCharArray();

		// compare the test arr 4th element to the rotated arraays first element
		// to see if they match. if so stop rotation.
		// count the rotates.
		while(!String.valueOf(rotateArr[1]).equals(matchedNumber)) {
			// rotate array = [0 1 2 3]
			// to rotate right we do: [3 0 1 2]
			// essentially replace first element with last and shift all elements;
			// were using a temp variable for now but can prolly be more efficient
			char[] temp = new char[4];
			temp[0] = rotateArr[3];
			temp[1] = rotateArr[0];
			temp[2] = rotateArr[1];
			temp[3] = rotateArr[2];

			rotateArr = temp;
			mv.increaseMove();
		}

		mv.setAltered(String.valueOf(rotateArr));

		return mv;
	}

	private static Move rotateLeft(String toRotate, String matchedNumber) {
		Move mv = new Move(toRotate);
		char[] rotateArr = toRotate.toCharArray();

		// compare the test arr 4th element to the rotated arraays first element
		// to see if they match. if so stop rotation.
		// count the rotates.
		while(!String.valueOf(rotateArr[3]).equals(matchedNumber)) {
			// rotate array = [0 1 2 3]
			// to rotate left we do: [1 2 3 0]
			// essentially replace last element with first and shift all elements;
			// were using a temp variable for now but can prolly be more efficient
			char[] temp = new char[4];
			temp[0] = rotateArr[1];
			temp[1] = rotateArr[2];
			temp[2] = rotateArr[3];
			temp[3] = rotateArr[0];

			rotateArr = temp;
			mv.increaseMove();
		}
		mv.setAltered(String.valueOf(rotateArr));

		return mv;
	}
	// private static Move rotateRight(String test, String toRotate) {
	// 	Move mv = new Move(toRotate);
	// 	char[] testArr = test.toCharArray();
	// 	char[] rotateArr = toRotate.toCharArray();

	// 	// compare the test arr 4th element to the rotated arraays first element
	// 	// to see if they match. if so stop rotation.
	// 	// count the rotates.
	// 	while(testArr[3] != rotateArr[0]) {
	// 		// rotate array = [0 1 2 3]
	// 		// to rotate right we do: [3 0 1 2]
	// 		// essentially replace first element with last and shift all elements;
	// 		// were using a temp variable for now but can prolly be more efficient
	// 		char[] temp = new char[4];
	// 		temp[0] = rotateArr[3];
	// 		temp[1] = rotateArr[0];
	// 		temp[2] = rotateArr[1];
	// 		temp[3] = rotateArr[2];

	// 		rotateArr = temp;
	// 		mv.increaseMove();
	// 	}

	// 	return mv;
	// }
}
