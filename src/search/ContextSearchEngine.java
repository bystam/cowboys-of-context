package search;

/**
 * Search engine expanding query context before performing ranked retrieval.
 */
public class ContextSearchEngine extends RankedRetrievalSearchEngine {

    @Override
    public SearchResults search(Query query) {
        query = expandQueryWithContext (query);
        return super.search(query);
    }

    private Query expandQueryWithContext(Query query) {
        return query; // TODO use context index to expand the query
    }
}
