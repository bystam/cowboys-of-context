package index;

import common.Document;

import java.io.File;

/**
 * This is the writer of an index that afterwards can be read by
 * a {@link index.Index}. This to separate writing and reading
 * of indexes.
 */
public interface Indexer {

    void insert (String word, Document document, int offset);

    void writeIntoDirectory (File indexDirectory);
}
