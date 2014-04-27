package search;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

// TODO add query weights?
public class Query implements Iterable<String> {

    private final List<String> terms;

    public Query (String queryString) {
        terms = Arrays.asList(queryString.split(" \t"));
    }

    @Override
    public Iterator<String> iterator() {
        return terms.iterator();
    }
}
