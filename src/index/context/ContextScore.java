package index.context;

/**
 * This describes a simple context score between two words.
 */
public class ContextScore implements Comparable<ContextScore> {

    private final String firstWord;
    private final String secondWord;
    private final double score;

    public ContextScore(String firstWord, String secondWord, double score) {
        this.firstWord = firstWord;
        this.secondWord = secondWord;
        this.score = score;
    }

    public String getFirstWord() {
        return firstWord;
    }

    public String getSecondWord() {
        return secondWord;
    }

    public double getScore() {
        return score;
    }

    @Override
    public int compareTo(ContextScore context) {
        return -Double.compare(score, context.score);
    }
}
