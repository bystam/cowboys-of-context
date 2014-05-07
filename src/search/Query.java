package search;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import common.Util;


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
    
    
    public void ensureTermsHaveHighestWeights(Collection<String> terms){
    	double maxWeight = Collections.max(weightedTerms.values());
    	for(String term : terms){
    		System.out.println("incr " + term + " with " + maxWeight); //TODO
    		addOrIncrementTermWeight(term, maxWeight);
    	}
    }

    @Override
    public Iterator<Entry<String,Double>> iterator() {
        return weightedTerms.entrySet().iterator();
    }
    
    public Set<String> getTerms(){
    	return weightedTerms.keySet();
    }
    
    public String toString(){
    	//TODO Might be very costly to call this toString() often
    	return Util.getMapSortedByValues(weightedTerms).toString();
    }
}
