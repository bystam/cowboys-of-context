package index.context;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public List<WordRelation> getContextForWord(String word) {
        List<WordRelation> context = dummyContext.get(word).keySet()
                .stream()
                .map(associatedWord ->
                        new WordRelation(word, associatedWord, dummyContext.get(word).get(associatedWord)))
                .collect(Collectors.toList());
        context.sort(null);
        return context;
    }

    @Override
    public ContextsMap getContextsForWords(Collection<String> words) {
        ContextsMap contextsMap = new ContextsMap();
        for (String word : words)
            contextsMap.putContextScoresForWord(word, getContextForWord(word));
        return contextsMap;
    }
}
