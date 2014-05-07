package index.context;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Basically a wrapper for a Map<String, List<WordRelation>> to make context
 * less super-verbose.
 *
 * TODO: This is perhaps a bit more than just one ContextsMap. A context Set perhaps?
 */
public class ContextsMap {

    private final Map<String, ContextPostingsList> context = new HashMap<>(100);
    private Map<String, Double> queryExpansionCandidates = new HashMap<>(100);

    private double mean = 0;

    public void putContextScoresForWord (String word, ContextPostingsList wordRelations) {
        context.put(word, wordRelations);

        //Collect the scores of 'target' words, i.e all the words that words in the query has relations to
        for(WordRelation wr : wordRelations){
            if(queryExpansionCandidates.containsKey(wr.getSecondWord())){
                queryExpansionCandidates.put(wr.getSecondWord(), wr.getScore() + queryExpansionCandidates.get(wr.getSecondWord()));
            }else{
                queryExpansionCandidates.put(wr.getSecondWord(), wr.getScore());
            }
        }
    }

    public Map<String, Double> getQueryExpansionCandidates(){ return queryExpansionCandidates; }

    public ContextPostingsList getContextScoresForWord (String word) {
        return context.get(word);
    }

    public Set<String> getOriginalWords () {
        return context.keySet();
    }
    
    public void clear(){
    	context.clear();
    }
}
