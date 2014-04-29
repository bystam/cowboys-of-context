package search;

import common.Document;
import index.context.ContextsMap;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This is a class representing the results of running a query
 * through a search engine. Should typically be able to give
 * the user links to different hits.
 */
public class SearchResults implements Iterable<Entry<Document, Double>>{
	private final Map<Document, Double> documents;
	
	public SearchResults(Map<Document,Double> documents){
		this.documents = documents;
	}

	@Override
	public Iterator<Entry<Document, Double>> iterator() {
		return documents.entrySet().iterator();
	}

    public ContextsMap getContextsMap () {
        return null;
    }
}
