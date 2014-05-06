package common;

import java.util.*;
import java.util.Map.Entry;

public class Util {
	
	public static <K> void incrementMap(Map<K,Double> map, K key, Double amount){
    	if(map.containsKey(key)){
    		map.put(key, map.get(key) + amount);
    	}else{
    		map.put(key, amount);
    	}
    }

    // sorted in ascending order
	public static <K,V extends Comparable<V>> LinkedHashMap<K,V> getMapSortedByValues(Map<K,V> map){
    	List<Entry<K,V>> entries = new LinkedList<Entry<K,V>>(map.entrySet());
    	Collections.sort(entries, (o1, o2) -> -o1.getValue().compareTo(o2.getValue()));
    	LinkedHashMap<K,V> sortedMap = new LinkedHashMap<K,V>();
    	for(Entry<K,V> e : entries){
    		sortedMap.put(e.getKey(), e.getValue());
    	}
    	return sortedMap;
    }
	
	public static <K,V extends Comparable<V>> LinkedHashMap<K,V> getMapSortedByValuesDescending(Map<K,V> map){
    	List<Entry<K,V>> entries = new LinkedList<Entry<K,V>>(map.entrySet());
    	Collections.sort(entries, (o1, o2) -> o1.getValue().compareTo(o2.getValue()));
    	LinkedHashMap<K,V> sortedMap = new LinkedHashMap<K,V>();
    	for(Entry<K,V> e : entries){
    		sortedMap.put(e.getKey(), e.getValue());
    	}
    	return sortedMap;
    }
}
