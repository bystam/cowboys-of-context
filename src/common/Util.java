package common;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Util {
	
	public static <K> void incrementMap(Map<K,Double> map, K key, Double amount){
    	if(map.containsKey(key)){
    		map.put(key, map.get(key) + amount);
    	}else{
    		map.put(key, amount);
    	}
    }
    
	public static <K,V extends Comparable<V>> Map<K,V> getMapSortedByValues(Map<K,V> sortedMap){
    	List<Entry<K,V>> entries = new LinkedList<Entry<K,V>>(sortedMap.entrySet());
    	Collections.sort(entries, new Comparator<Entry<K,V>>() {
			public int compare(Entry<K, V> o1, Entry<K, V> o2) {
				return o1.getValue().compareTo(o2.getValue());
			}
		});
    	sortedMap = new LinkedHashMap<K,V>();
    	for(Entry<K,V> e : entries){
    		sortedMap.put(e.getKey(), e.getValue());
    	}
    	return sortedMap;
    }
}
