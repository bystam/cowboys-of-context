package search;


import index.DocumentMetaData;
import index.Index;
import index.PostingsEntry;
import index.PostingsList;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import common.Document;
import common.Util;

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
    	Map<Document, Double> docScores = new HashMap<Document,Double>();
        for(Entry<String, Double> weightedTerm : query){
        	String term = weightedTerm.getKey();
        	double weight = weightedTerm.getValue();
        	PostingsList postingsList = index.getPostingsList(term);
        	for(PostingsEntry postingsEntry : postingsList){
				Document doc = postingsEntry.getDocument();
				double tfIdfScore = postingsEntry.getTfIdf();
				tfIdfScore *= weight;
				Util.incrementMap(docScores, doc, tfIdfScore);
        	}
        }
        LinkedHashMap<Document,Double> sortedDocScores = Util.getMapSortedByValues(docScores);
        return new SearchResults(sortedDocScores);
    }

}
