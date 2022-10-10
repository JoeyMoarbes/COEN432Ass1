import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class ParticularSolution {
    public Vector<Vector<String>> puzzlePieces;
    int misMatchedBeforeSolve = 96;
    int misMatchedAfterSolve = 0;
    public float totalPercentageSolved = 0;

    public Map<String, Move> moves = new HashMap<String, Move>();
    public final int RowSize = 8;
	public final int ColumnSize = 8;

    //int[] dna 
    public ParticularSolution (Vector<Vector<String>> vec){
        this.puzzlePieces = vec;
    }

    // public void setMisMatched(int n) {
    //     this.misMatchedBeforeSolve = n;
    // }
    
    // public void setMisMathedAfterSolve(int n) {
    //     this.misMatchedAfterSolve = n;
    // }

    public int puzzleTotalMisMatches() {
        return misMatchedBeforeSolve - misMatchedAfterSolve;
    }

    public int findNumberOfMisMatches() {
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

        //System.out.println("Number of mismatches : " + numberOfMismatches);
        misMatchedAfterSolve = numberOfMismatches;
        return misMatchedAfterSolve;
    }

    public float findTotalPercentageSolved() {
        int totalMatched = puzzleTotalMisMatches();
        float percent = ((float) totalMatched / misMatchedBeforeSolve) * 100;
        totalPercentageSolved = percent;
        return percent;//totalPercentageSolved;
    }

    public float getTotalPercentageSolved() {
        return totalPercentageSolved;
    }

    private int CountRowMismatches(Vector<String> first, Vector<String> second) 
	{
		int numberOfMismatches = 0;

		for (int i = 0; i < first.size(); i++)
			if (first.get(i).charAt(2) != second.get(i).charAt(0))
				numberOfMismatches++;

		return numberOfMismatches ;
	}
	
	private int CountColumnMismatches(Vector<String> first, Vector<String> second) 
	{
		int numberOfMismatches = 0;

		for (int i = 0; i < first.size(); i++)
			if (first.get(i).charAt(1) != second.get(i).charAt(3))
				numberOfMismatches++;

		return numberOfMismatches;
	}
}
