package search;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import common.Document;

/**
 * This is a class representing the results of running a query
 * through a search engine. Should typically be able to give
 * the user links to different hits.
 */
public class SearchResults implements Iterable<Entry<Document, Double>>{
	private Map<Document, Double> documents;
	
	public SearchResults(Map<Document,Double> documents){
		this.documents = documents;
	}

	@Override
	public Iterator<Entry<Document, Double>> iterator() {
		return documents.entrySet().iterator();
	}
}
