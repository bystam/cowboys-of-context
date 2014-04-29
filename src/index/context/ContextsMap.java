package index.context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Basically a wrapper for a Map<String, List<WordRelation>> to make context
 * less super-verbose.
 *
 * TODO: This is perhaps a bit more than just one ContextsMap. A context Set perhaps?
 */
public class ContextsMap {

    private final Map<String, List<WordRelation>> context = new HashMap<>();

    public void putContextScoresForWord (String word, List<WordRelation> wordRelations) {
        context.put(word, wordRelations);
    }

    public List<WordRelation> getContextScoresForWord (String word) {
        return context.get(word);
    }

    public Set<String> getOriginalWords () {
        return context.keySet();
    }
    
    public void clear(){
    	context.clear();
    }
}
