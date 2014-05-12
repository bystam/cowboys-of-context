package index.context;

import common.Util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ContextPostingsList implements Serializable, Iterable<WordRelation> {

    private static final int STRIPPED_CONTEXT_AMOUNT = 5;

    public static final ContextPostingsList EMPTY = new ContextPostingsList(null);

    private final String originalWord;
    private Map<String, WordRelation> entries = new HashMap<>();

    ContextPostingsList(String originalWord){
        this.originalWord = originalWord;
    }

    public String getOriginalWord(){
        return originalWord;
    }

    public void addEntry(String token, Double score){
        if(!entries.containsKey(token)){
            entries.put(token, new WordRelation(originalWord, token, score));
        }
        else{
            entries.get(token).incScore(score);
        }
    }

    public WordRelation get(String name){
        return entries.get(name);
    }

    @Override
    public Iterator<WordRelation> iterator() {
        return entries.values().iterator();
    }
    
    public void sort(){
    	entries = Util.getMapSortedByValuesDescending(entries);
    }
    
    public String toString(){
    	return originalWord + " -> " + entries;
    }

    public void normalizeScores () {
        final double scoreSum = entries.values()
                .stream()
                .mapToDouble(WordRelation::getScore)
                .sum();
        entries.values().forEach((wr) -> wr.setScore(wr.getScore() / scoreSum));
    }

    /**
     * Creates a new ContextPostingsList that only contains the top
     * ranked word relations of itself (sorted).
     *
     * @return A contextPostingsList with the sorted top word relations
     */
    public ContextPostingsList strip () {
        ContextPostingsList stripped = new ContextPostingsList(this.originalWord);

        sort();
        int amount = 0;
        for (WordRelation wr : this) {
            if (amount++ == STRIPPED_CONTEXT_AMOUNT)
                break;
            stripped.entries.put(wr.getSecondWord(), wr);
        }
        return stripped;
    }
}
