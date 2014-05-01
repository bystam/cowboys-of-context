package index.context;

import java.util.Collection;
import java.util.List;

/**
 * A ContextsMap Index is an index that contains information about the relations
 * between word couples. The relation is built from context in which
 * the words have been encountered in documents.
 * <p>
 * This is the read version of the index. For writing, see {@link ContextIndexer}.
 */
public interface ContextIndex {

    /**
     * Get all related words (context scores) to the input word.
     * @param word The word to find context scores for
     * @return A List of sortable context scores
     */
    List<WordRelation> getContextForWord(String word);

    /**
     * Get all related words (context scores) to the input words.
     * @param words The words to find context scores for
     * @return A context for all the words
     */
    ContextsMap getContextsForWords(Collection<String> words);


    ContextPostingsList readPostingsList(String name);

}
