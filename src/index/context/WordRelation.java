package index.context;

import java.io.Serializable;

/**
 * This describes a simple context score between two words.
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

    public String getOtherWord(String name){
        if(name.equals(firstWord)){
            return secondWord;
        }
        return firstWord;

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
}
