package search;

import index.Index;
import index.context.Context;
import index.context.ContextIndex;
import index.context.ContextScore;

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
    	Context context = contextIndex.getContextForWords(query.getTerms());
    	for(String term : context.getOriginalWords()){
    		for(ContextScore cs : context.getContextScoresForWord(term)){
    			query.addOrIncrementTermWeight(cs.getSecondWord(), cs.getScore());
    		}
    	}
    	return query;
    }

}
