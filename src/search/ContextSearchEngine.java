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
        ContextsMap contexts = contextIndex.getContextsForWords(query.getTerms());
        query = expandQueryWithContext (query, contexts);
        SearchResults simpleResults = super.search(query);
        return new SearchResults(simpleResults, contexts);
    }

    private Query expandQueryWithContext(Query query, ContextsMap contextsMap) {
    	for(String term : contextsMap.getOriginalWords()){
    		for(WordRelation relation : contextsMap.getContextScoresForWord(term)){
    			query.addOrIncrementTermWeight(relation.getSecondWord(), relation.getScore());
    		}
    	}
    	return query;
    }
}
