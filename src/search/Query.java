package search;

import common.Util;

import java.util.*;
import java.util.Map.Entry;


public class Query implements Iterable<Entry<String,Double>> {
	
	public static final double DEFAULT_WEIGHT = 1;

	private final Map<String, Double> weightedTerms = new HashMap<>();

    public Query (Query original) {
        this.weightedTerms.putAll(original.weightedTerms);
    }

    public Query (String queryString) {
        queryString = queryString.trim().toLowerCase();
        List<String> terms = Arrays.asList(queryString.split(" "));
        for (String word : terms)
        	weightedTerms.put(word, DEFAULT_WEIGHT);
    }
    
    public void addOrIncrementTermWeight(String term, double weight){
    	Util.incrementMap(weightedTerms, term, weight);
    }

    @Override
    public Iterator<Entry<String,Double>> iterator() {
        return weightedTerms.entrySet().iterator();
    }
    
    public Set<String> getTerms(){
    	return weightedTerms.keySet();
    }
}
