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
	private final ContextsMap contexts;
	
	public SearchResults(Map<Document,Double> documents, ContextsMap contexts){
		this.documents = documents;
		this.contexts = contexts;
	}
	
	public SearchResults(Map<Document,Double> documents){
		this(documents, null);
	}
	
	public SearchResults(SearchResults simpleResults, ContextsMap contexts){
		this(simpleResults.documents, contexts);
	}

	@Override
	public Iterator<Entry<Document, Double>> iterator() {
		return documents.entrySet().iterator();
	}
	
	public boolean hasContextsMap(){
		return contexts != null;
	}

    public ContextsMap getContextsMap () {
    	if(contexts == null){
    		throw new IllegalStateException("This is the result from a simple search (no context).");
    	}
        return contexts;
    }
}
