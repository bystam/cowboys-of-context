package search;

import index.Index;
import index.context.ContextsMap;
import index.context.ContextIndex;
import index.context.WordRelation;

/**
 * Search engine expanding query context before performing ranked retrieval.
 *
 * TODO: perhaps a search engine should be able to change its indexes
 *       (this because we might allow the user choosing the index directory)
 */
public class ContextSearchEngine extends RankedRetrievalSearchEngine {

    private final ContextIndex contextIndex;

    public ContextSearchEngine (Index index, ContextIndex contextIndex) {
        super (index);
        this.contextIndex = contextIndex;
    }

    @Override
    public SearchResults search(Query query) {
        query = expandQueryWithContext (query);
        return super.search(query);
    }

    private Query expandQueryWithContext(Query query) {
    	ContextsMap contextsMap = contextIndex.getContextsForWords(query.getTerms());
    	for(String term : contextsMap.getOriginalWords()){
    		for(WordRelation cs : contextsMap.getContextScoresForWord(term)){
    			query.addOrIncrementTermWeight(cs.getSecondWord(), cs.getScore());
    		}
    	}
    	return query;
    }

}
