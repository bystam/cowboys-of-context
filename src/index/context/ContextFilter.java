package index.context;

public interface ContextFilter {


    /**
     * The general score threshold for tf-idf scores
     */
    public static final double TF_IDF_THRESHOLD = Double.MAX_VALUE;


    /**
     * Determines whether or not the given word is relevant to context
     * indexing.
     * @param word
     * @return <code>true</code> if the word should be included, and <code>false</code> if not
     */
    boolean isValid(String word);
}
