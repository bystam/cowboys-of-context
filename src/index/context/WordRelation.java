package index.context;

/**
 * This describes a simple context score between two words.
 */
public class WordRelation implements Comparable<WordRelation> {

    private final String firstWord;
    private final String secondWord;
    private final double score;

    public WordRelation(String firstWord, String secondWord, double score) {
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
    public int compareTo(WordRelation context) {
        return -Double.compare(score, context.score);
    }
}
