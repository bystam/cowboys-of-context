package index.context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public ContextScore getContextScore(String firstWord, String secondWord) {
        return null;
    }

    @Override
    public List<ContextScore> getContextsForWord(String word) {
        List<ContextScore> context = dummyContext.get(word).keySet()
                .stream()
                .map(associatedWord ->
                        new ContextScore(word, associatedWord, dummyContext.get(word).get(associatedWord)))
                .collect(Collectors.toList());
        return context;
    }

    @Override
    public Context getContextForWords(List<String> words) {
        Context context = new Context();
        for (String word : words)
            context.putContextScoresForWord(word, getContextsForWord(word));
        return context;
    }
}
