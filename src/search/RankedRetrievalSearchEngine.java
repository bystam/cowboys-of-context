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
    private final DocumentMetaData metaData;

    public RankedRetrievalSearchEngine (Index index) {
        this.index = index;
        metaData = index.getDocumentMetaData();
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
				Document doc = postingsEntry.getDocument();
				double docLength = metaData.getDocumentLength(doc);
				double tfIdfScore = tfIdf(termFrequency, numDocsContainingTerm, docLength);
				tfIdfScore *= weight;
				Util.incrementMap(docScores, doc, tfIdfScore);
        	}
        }
        LinkedHashMap<Document,Double> sortedDocScores = Util.getMapSortedByValues(docScores);
        return new SearchResults(sortedDocScores);
    }
    
    private double tfIdf(double termFrequency, double numDocsContainingTerm, double docLength){
    	int numDocs = metaData.getNumDocuments();
    	double invDocFrequency = Math.log10(numDocs / numDocsContainingTerm);
		return termFrequency * invDocFrequency / docLength; //Divide by length later if efficiency is important.
    }

}
