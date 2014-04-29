package search;

/**
 * A generator of {@link search.SearchResults} using {@link search.Query}
 * objects. Typically using {@link index.Index} and {@link index.context.ContextIndex}
 * instances.
 */
public interface SearchEngine {

    SearchResults search (Query query);
}
