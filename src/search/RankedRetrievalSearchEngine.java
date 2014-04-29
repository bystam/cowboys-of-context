package search;


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
        		double termFrequency = postingsEntry.getOffsets().size();
				double numDocsContainingTerm = postingsList.getNumDocuments();
				double docLength = 0; //TODO
				double tfIdfScore = tfIdf(termFrequency, numDocsContainingTerm, docLength);
				tfIdfScore *= weight;
				Util.incrementMap(docScores, postingsEntry.getDocument(), tfIdfScore);
        	}
        }
        LinkedHashMap<Document,Double> sortedDocScores = Util.getMapSortedByValues(docScores);
        return new SearchResults(sortedDocScores);
    }
    
    private double tfIdf(double termFrequency, double numDocsContainingTerm, double docLength){
    	int numDocs = 0; //TODO
    	double invDocFrequency = Math.log10(numDocs / numDocsContainingTerm);
		return termFrequency * invDocFrequency / docLength; //Divide by length later if efficiency is important.
    }

}
