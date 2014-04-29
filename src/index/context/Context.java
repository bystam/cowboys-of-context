package index.context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Basically a wrapper for a Map<String, List<ContextScore>> to make context
 * less super-verbose.
 *
 * TODO: This is perhaps a bit more than just one Context. A context Set perhaps?
 */
public class Context {

    private final Map<String, List<ContextScore>> context = new HashMap<>();

    public void putContextScoresForWord (String word, List<ContextScore> contextScores) {
        context.put(word, contextScores);
    }

    public List<ContextScore> getContextScoresForWord (String word) {
        return context.get(word);
    }

    public Set<String> getOriginalWords () {
        return context.keySet();
    }
    
    public void clear(){
    	context.clear();
    }
}
