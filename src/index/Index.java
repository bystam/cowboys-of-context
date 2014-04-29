package index;

/**
 * An Index contains information about the occurrences of a word
 * in different documents.
 * <p>
 * This is the read version of the index. For writing, see {@link index.Indexer}.
 */
public interface Index {
    PostingsList getPostingsList (String word);

    DocumentMetaData getDocumentMetaData();
}
