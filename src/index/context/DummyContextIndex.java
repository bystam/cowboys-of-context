package index.context;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Dummy context index using some hard coded data.
 */
public class DummyContextIndex implements ContextIndex {

    private final Map<String, Map<String, Double>> dummyContext = new HashMap<>();

    public DummyContextIndex () {
        Map<String, Double> apaMap = new HashMap<>();
        apaMap.put("primat", 0.45);
        apaMap.put("banan", 0.22);
        apaMap.put("träd", 0.10);
        dummyContext.put("apa", apaMap);

        Map<String, Double> bilMap = new HashMap<>();
        bilMap.put("hjul", 0.45212);
        bilMap.put("förgasare", 0.33);
        bilMap.put("Volvo", 0.15);
        dummyContext.put("bil", bilMap);
    }

    @Override
    public ContextPostingsList getContextForWord(String word) {
        ContextPostingsList contextPostingsList = new ContextPostingsList(word);
        dummyContext.get(word).entrySet()
                .stream()
                .forEach((e) -> {
                    String otherWord = e.getKey();
                    double score = e.getValue();
                    contextPostingsList.addEntry(otherWord, score);
                });
        return contextPostingsList;
    }

    @Override
    public ContextsMap getContextsForWords(Collection<String> words) {
        ContextsMap contextsMap = new ContextsMap();
        for (String word : words)
            contextsMap.putContextScoresForWord(word, getContextForWord(word));
        return contextsMap;
    }
}
