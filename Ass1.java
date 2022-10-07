

import java.io.File;  
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
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
	}	

	private static boolean tilesEdgeMatch(String v1, String v2) {
		Boolean matched = true;
		
		// basically im thinking if the numbers are on opposite sides of their tile
		// then both tiles have to rotate or else there wont be a match
		// this is also the case for tiles with values in bottom or top
		int indexOfFirstMatchedEdge = -1;
		int indexOfSecondMatchedEdge = -1;
		for(int i = 0; i < v1.length(); i++) {
			indexOfFirstMatchedEdge = i;//v2.indexOf(v1.charAt(i));
			indexOfFirstMatchedEdge = v2.indexOf(v1.charAt(i));
			if(indexOfFirstMatchedEdge > -1) {
				// match found
				// break out of for loop
				matched = true;
				break;
			}
		}
			
		if(!matched)
			return false;

		// rotate v1 (right rotation) until the matched number is in index 3
		int matchedEdge = v1.charAt(indexOfFirstMatchedEdge);
		
		Move mv1 = rotateRight(v1, matchedEdge);
		Move mv2 = rotateLeft(v2, matchedEdge);
		if(mv1.moves > 0)
			moves.add(mv1);
		if(mv2.moves > 0)
			moves.add(mv2);

		return matched;
	}

	private static Move rotateRight(String toRotate, int matchedNumber) {
		Move mv = new Move(toRotate);
		char[] rotateArr = toRotate.toCharArray();

		// compare the test arr 4th element to the rotated arraays first element
		// to see if they match. if so stop rotation.
		// count the rotates.
		while(rotateArr[1] != matchedNumber) {
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

		return mv;
	}

	private static Move rotateLeft(String toRotate, int matchedNumber) {
		Move mv = new Move(toRotate);
		char[] rotateArr = toRotate.toCharArray();

		// compare the test arr 4th element to the rotated arraays first element
		// to see if they match. if so stop rotation.
		// count the rotates.
		while(rotateArr[3] != matchedNumber) {
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
