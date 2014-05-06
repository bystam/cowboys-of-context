package index.context;

import java.io.Serializable;

/**
 * This describes a simple context score between two words.
 *
 * Naming convention:
 *      firstWord - word of the postingslist this WordRelation belongs to
 *      secondWord - an element in the postingslist of firstWord
 *
 */
public class WordRelation implements Serializable, Comparable<WordRelation> {

    private final String firstWord;
    private final String secondWord;
    private double score;

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

    public void incScore(double score){
        this.score += score;
    }

    @Override
    public int compareTo(WordRelation context) {
        return -Double.compare(score, context.score);
    }
    
    public String toString(){
    	return firstWord + ", " + secondWord + " ~ " + score;
    }
}
