package search;


import index.Index;
import index.PostingsEntry;
import index.PostingsList;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import common.Document;

/**
 * Basic search engine using ranked retrieval.
 */
public class RankedRetrievalSearchEngine implements SearchEngine {

    private final Index index;

    public RankedRetrievalSearchEngine (Index index) {
        this.index = index;
    }

    //TODO SearchResults should be sorted!
    @Override
    public SearchResults search(Query query) {
    	int numDocs = 0; //TODO
    	Map<Document, Double> docScores = new HashMap<Document,Double>();
        for(Entry<String, Double> weightedTerm : query){
        	String term = weightedTerm.getKey();
        	PostingsList postingsList = index.getPostingsList(term);
        	for(PostingsEntry postingsEntry : postingsList){
        		double termFrequency = postingsEntry.getOffsets().size();
				double numDocsContainingTerm = postingsList.getNumDocuments();
				double docLength = 0; //TODO
				double invDocFrequency = Math.round(Math.log(numDocs / numDocsContainingTerm));
				double tfIdfScore = termFrequency * invDocFrequency / docLength;
				incrementMap(docScores, postingsEntry.getDocument(), tfIdfScore);
        	}
        }
        return new SearchResults(docScores);
    }
    
    private void incrementMap(Map<Document,Double> map, Document key, Double amount){
    	if(map.containsKey(key)){
    		map.put(key, map.get(key) + amount);
    	}else{
    		map.put(key, amount);
    	}
    }
}
