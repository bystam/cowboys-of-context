package search;

import index.Index;
import index.context.ContextIndex;
import index.context.ContextsMap;
import index.context.WordRelation;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Search engine expanding query context before performing ranked retrieval.
 *
 * TODO: perhaps a search engine should be able to change its indexes
 *       (this because we might allow the user choosing the index directory)
 */
public class ContextSearchEngine extends RankedRetrievalSearchEngine {

    private final ContextIndex contextIndex;
    
    private final static int MAX_NUM_EXPANSIONS_PER_WORD = 5; 

    public ContextSearchEngine (Index index, ContextIndex contextIndex) {
        super (index);
        this.contextIndex = contextIndex;
    }

    @Override
    public SearchResults search(Query query) {
        query = new Query(query);
        ContextsMap contexts = contextIndex.getContextsForWords(query.getTerms());
        contexts.sortContexts();
        expandQueryWithContext (query, contexts);
        System.out.println(query); //TODO
        SearchResults simpleResults = super.search(query);
        return new SearchResults(simpleResults, contexts);
    }

    private void expandQueryWithContext(Query query, ContextsMap contextsMap) {
    	Collection<String> originalTerms = new ArrayList<String>(query.getTerms());
    	//contextsMap.getOriginalWords() might not contain all of query's original terms
    	//since some of the terms might not have a context (maybe considered a stopword)
    	for(String originalTerm : contextsMap.getOriginalWords()){
    		int expansion = 0;
    		for(WordRelation relation : contextsMap.getContextScoresForWord(originalTerm)){
    			query.addOrIncrementTermWeight(relation.getSecondWord(), relation.getScore());
    			expansion ++;
    			if(expansion > MAX_NUM_EXPANSIONS_PER_WORD){
    				break;
    			}
    		}
    	}
    	query.ensureTermsHaveHighestWeights(originalTerms);
    }
}
