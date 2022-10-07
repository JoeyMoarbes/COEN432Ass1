public class Move {
    // can prolly use a class like this to calculate our fitness and stuff
    String original = "";
    String altered = "";
    int moves = 0;

    public Move(String original)  {
        this.original = original;
    }

    public Move(String original, String altered, int moves) {
        this(original);
        this.altered = altered;
        this.moves = moves;
    }

    public void increaseMove() {
        moves++;
    }
}
