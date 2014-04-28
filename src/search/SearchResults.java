package search;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import common.Document;

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
