package search;

import index.Index;
import index.context.ContextIndex;

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
        return query; // TODO use context index to expand the query
    }
}
