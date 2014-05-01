package index.context;

import java.io.Serializable;
import java.util.*;

/**
 * Created by olivergafvert on 2014-04-29.
 */
public class ContextPostingsList implements Serializable, Iterable<WordRelation> {

    private final String originalWord;
    private Map<String, WordRelation> entries = new HashMap<String, WordRelation>(100);

    ContextPostingsList(String originalWord){
        this.originalWord = originalWord;
    }

    public String getOriginalWord(){
        return originalWord;
    }

    public void addEntry(String token, Double score){
        if(!entries.containsKey(token)){
            entries.put(token, new WordRelation(originalWord, token, score));
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
