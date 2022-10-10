


import java.io.File;  
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Ass1 extends Application
{
	
	//static Vector<Vector<String>> puzzlePieces = new Vector<Vector<String>>();
	//static List<ParticularSolution> solutions = new ArrayList<ParticularSolution>();

	static List<ParticularSolution> solutions = new ArrayList<ParticularSolution>();
	
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

		for(int m = 0; m < 20000; m++) {
			ParticularSolution sol = new ParticularSolution((Vector<Vector<String>>) puzzlePieces.clone());
			
			//sol.misMatchedBeforeSolve = numberOfMismatches;

			//System.out.println("Number of mismatches : " + sol.misMatchedBeforeSolve);

			//System.out.println("Solving puzzle column by column.");
	
			solvePuzzleColumnByColumn(sol);
			//System.out.println("Solving puzzle row by row.");
			solvePuzzleRowbyRow(sol);
	
			//solvePuzzleByGrid();

			System.out.println("Number of mismatches after solve: " + sol.findNumberOfMisMatches());

			System.out.println("Puzzle total % mismatches after solve: " + sol.findTotalPercentageSolved());
			solutions.add(sol);
		}
    	    	 



		launch(); 
	}
	
	private static void solvePuzzleRowbyRow(ParticularSolution sol) {
		for(Vector<String> row: sol.puzzlePieces) {
			for(int i = 0; i < row.size() - 1; i++) {
				String left = row.get(i); // current column or LEFT
				String right = row.get(i + 1); // next volumn or RIGHT
				
				//System.out.println("LEFT : " + left + ", RIGHT: " + right);
				
				Map<String, Move> newMoves = tilesEdgeMatch(left, right, 1, 3);
				if(newMoves.size() > 0) {
					if(newMoves.containsKey("LEFT")){
						Move m = newMoves.get("LEFT");
						row.set(i, m.altered);
						if(sol.moves.containsKey(m.original)) {
							//sol.moves.add(m);
							Move oldM = sol.moves.get(m.original);
							oldM.moves += m.moves;
							sol.moves.put(m.original, oldM);
						} else {
							sol.moves.put(m.original, m);
						}
					}

					if(newMoves.containsKey("RIGHT")){
						Move m = newMoves.get("RIGHT");
						row.set(i + 1, m.altered);
						//moves.add(m);
						if(sol.moves.containsKey(m.original)) {
							//sol.moves.add(m);
							Move oldM = sol.moves.get(m.original);
							oldM.moves += m.moves;
							sol.moves.put(m.original, oldM);
						} else {
							sol.moves.put(m.original, m);
						}
					}
				} else {
					//System.out.println("LR => MATCH NOT FOUND OR ROTATION NOT NEEDED -> {" + left + ", " + right + "}");
				}
			}
		}
	}	

	private static void solvePuzzleColumnByColumn(ParticularSolution sol) {
		for (int j = 0; j < sol.RowSize - 1; j++)//for (int i = 0; i < ColumnSize - 1; i++)
	    {
			for (int i = 0; i < sol.ColumnSize; i++)//for (int j = 0; j < RowSize; j++)
			{
				String top = sol.puzzlePieces.get(j).get(i);
				String bottom = sol.puzzlePieces.get(j+1).get(i);
				
				//System.out.println("TOP : " + top + ", BOTTOM: " + bottom);
					
				Map<String, Move> newMoves = tilesEdgeMatch(top, bottom, 2, 0);
				if(newMoves.size() > 0) {
					if(newMoves.containsKey("LEFT")){
						Move m = newMoves.get("LEFT");
						sol.puzzlePieces.get(j).set(i, m.altered);
						//moves.add(m);
						if(sol.moves.containsKey(m.original)) {
							//sol.moves.add(m);
							Move oldM = sol.moves.get(m.original);
							oldM.moves += m.moves;
							sol.moves.put(m.original, oldM);
						} else {
							sol.moves.put(m.original, m);
						}
					}

					if(newMoves.containsKey("RIGHT")){
						Move m = newMoves.get("RIGHT");
						sol.puzzlePieces.get(j+1).set(i, m.altered);
						//moves.add(m);
						if(sol.moves.containsKey(m.original)) {
							//sol.moves.add(m);
							Move oldM = sol.moves.get(m.original);
							oldM.moves += m.moves;
							sol.moves.put(m.original, oldM);
						} else {
							sol.moves.put(m.original, m);
						}
					}
				} else {
					//System.out.println("BT => MATCH NOT FOUND OR ROTATION NOT NEEDED -> {" + top + ", " + bottom + "}");
				}
			}

	    }

	}

	private static Map<String, Move> tilesEdgeMatch(String left, String right, int firstIndex, int secondIndex) {
		Map<String, Move> newMoves = new HashMap<String, Move>();
		List<EdgeCount> counts = new ArrayList<EdgeCount>();

		for(int i = 0; i < right.length(); i++) {
			final int ii = i; // error saying i must be final
			long count = right.chars().filter(ch -> ch == left.charAt(ii)).count();
			//counts.add(count);
			if(count > 0)
				counts.add(new EdgeCount((int) count, left.charAt(ii)));
		}

		if(counts.size() == 0)
			return newMoves;

		Random rand = new Random();
		EdgeCount c = counts.get(rand.nextInt(counts.size()));
		//EdgeCount c = counts.stream().filter(e -> e.count > 0).findAny().orElse(null);

		//System.out.println("Choosing edge: " + c.edge + " with count: " + c.count);

		String matchedEdge = String.valueOf(c.edge);
		
		Move mvLeft = rotateRight(left, matchedEdge, firstIndex); // counter intuitive but the left value we want to rotate right
		Move mvRight = rotateLeft(right, matchedEdge, secondIndex); // and right value to the left

		if(mvLeft.moves > 0)
			newMoves.put("LEFT",mvLeft);

		if(mvRight.moves > 0)
			newMoves.put("RIGHT",mvRight);

		return newMoves;
	}

	private static Move rotateRight(String toRotate, String matchedNumber, int edgeIndex) {
		Move mv = new Move(toRotate);
		char[] rotateArr = toRotate.toCharArray();

		// compare the test arr 4th element to the rotated arraays first element
		// to see if they match. if so stop rotation.
		// count the rotates.
		while(!String.valueOf(rotateArr[edgeIndex]).equals(matchedNumber)) {
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

	private static Move rotateLeft(String toRotate, String matchedNumber, int edgeIndex) {
		Move mv = new Move(toRotate);
		char[] rotateArr = toRotate.toCharArray();

		// compare the test arr 4th element to the rotated arraays first element
		// to see if they match. if so stop rotation.
		// count the rotates.
		while(!String.valueOf(rotateArr[edgeIndex]).equals(matchedNumber)) {
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

	@Override 
	public void start(Stage stage) { 
		int xGap = 50;
		int Xlocation = 25;
		int Ylocation = 25;
		int yGap= 50;
		int width = 100;
		int height = 50;

		List<Rectangle> recs = new ArrayList<Rectangle>();
		List<Text> texts = new ArrayList<Text>();
		ParticularSolution sol = solutions.stream().max(Comparator.comparing(ParticularSolution::getTotalPercentageSolved)).get();
		System.out.println("-------------------------------------------------------");
		System.out.println("Best solution % solved: " + sol.totalPercentageSolved);
		System.out.println("Number of mismatches after solve: " + sol.misMatchedAfterSolve);


		for(Vector<String> row: sol.puzzlePieces) {
			//int prevEdge = 0;
			for(String val: row) {
				//Drawing a Rectangle 
				Rectangle rectangle = new Rectangle();  

				//Setting the properties of the rectangle 
				rectangle.setX(Xlocation); 
				rectangle.setY(Ylocation); 
				rectangle.setWidth(width); 
				rectangle.setHeight(height);   
				//rectangle.border
				rectangle.setStyle("-fx-fill: white; -fx-stroke: red; -fx-stroke-width: 2;");

				recs.add(rectangle);
				

				Text textTop = new Text(Xlocation + (width / 2), Ylocation - 5, String.valueOf(val.charAt(0)));
				//Font font = new Font("Serif", 25);
				//text.setFont(font);
				textTop.setFill(Color.BLACK);
			
				Text textRight = new Text(Xlocation + width + 5, Ylocation + (height / 2), String.valueOf(val.charAt(1)));
				textRight.setFill(Color.BLACK);

				Text textBottom = new Text(Xlocation + (width / 2), Ylocation + height + 15, String.valueOf(val.charAt(2)));
				textBottom.setFill(Color.BLACK);

				Text textLeft = new Text(Xlocation - 10, Ylocation + (height / 2), String.valueOf(val.charAt(3)));
				textLeft.setFill(Color.BLACK);

				texts.add(textTop);
				texts.add(textRight);
				texts.add(textBottom);
				texts.add(textLeft);

				Xlocation += xGap + width;
			}
			Xlocation = 25;
			Ylocation += yGap + height;
		}
   
		  
	   //Creating a Group object  
	   Group root = new Group(); 
		  root.getChildren().addAll(recs);
		  root.getChildren().addAll(texts);

	   //Creating a scene object 
	   Scene scene = new Scene(root, 1200, 800);  
	   
	   //Setting title to the Stage 
	   stage.setTitle("Drawing the solution."); 
		  
	   //Adding scene to the stage 
	   stage.setScene(scene); 
		  
	   //Displaying the contents of the stage 
	   stage.show(); 
	}

	// https://stackoverflow.com/questions/20420065/loop-diagonally-through-two-dimensional-array
	// code adapted from this link
	// private static void solvePuzzleByGrid() {
	// 	for( int k = 0 ; k < ColumnSize; k++ ) {
	// 		for( int j = 0 ; j <= k ; j++ ) {
	// 			int i = k - j;
	// 			System.out.print( puzzlePieces.get(i).get(j) + " " );
	// 		}
	// 		System.out.println();
	// 	}
	// }
}
