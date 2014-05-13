package index.context;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Basically a wrapper for a Map<String, List<WordRelation>> to make context
 * less super-verbose.
 */
public class ContextsMap {

    private final Map<String, ContextPostingsList> context = new HashMap<>();

    public void putContextScoresForWord (String word, ContextPostingsList wordRelations) {
        context.put(word, wordRelations);
    }

    public ContextPostingsList getContextScoresForWord (String word) {
        return context.get(word);
    }

    public Set<String> getOriginalWords () {
        return context.keySet();
    }

    public void sortContexts() {
    	for(ContextPostingsList postingsList : context.values()){
    		postingsList.sort();
    	}
    }

    public static ContextsMap newStrippedAndNormalizedContextsMap(ContextsMap original) {
        ContextsMap stripped = new ContextsMap();
        for (String word : original.getOriginalWords())
            stripped.putContextScoresForWord(word, original.getContextScoresForWord(word).strip().normalize());

        return stripped;
    }
}
