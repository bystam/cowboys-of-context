package search;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import common.Util;


public class Query implements Iterable<Entry<String,Double>> {
	
	public static final double DEFAULT_WEIGHT = 1;

	private final Map<String, Double> weightedTerms;

    public Query (String queryString) {
        queryString = queryString.trim();
        List<String> terms = Arrays.asList(queryString.split(" \t"));
        weightedTerms = new HashMap<String,Double>();
        for(String word : terms){
        	weightedTerms.put(word, DEFAULT_WEIGHT);
        }
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
