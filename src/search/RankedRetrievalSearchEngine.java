package search;


import index.Index;

/**
 * Basic search engine using ranked retrieval.
 */
public class RankedRetrievalSearchEngine implements SearchEngine {

    private final Index index;

    public RankedRetrievalSearchEngine (Index index) {
        this.index = index;
    }

    @Override
    public SearchResults search(Query query) {
        return null;
    }
}
