package index.context;

import java.io.Serializable;
import java.util.*;

/**
 * Created by olivergafvert on 2014-04-29.
 */
public class ContextPostingsList implements Serializable, Iterable<WordRelation> {



    private final String name;
    private Map<String, WordRelation> entries = new HashMap<String, WordRelation>(100);

    ContextPostingsList(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void addEntry(String token, Double score){
        if(!entries.containsKey(token)){
            entries.put(token, new WordRelation(name, token, score));
        }
        else{
            entries.get(token).incScore(score);
        }
    }

    public WordRelation get(String name){
        return entries.get(name);
    }

    @Override
    public Iterator<WordRelation> iterator() {
        return null;
    }
}
