package index.context;

/**
 * This filter determines whether or not a word shall be taken
 * into account when doing context indexing. Simple words
 * that are bound to no specific context (such as 'men', 'han', 'hon')
 * should be filtered out with any implemetned strategy.
 */
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
    boolean isValid(String word, double tf_idf);
}
