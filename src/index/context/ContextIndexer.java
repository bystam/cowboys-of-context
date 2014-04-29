package index.context;

import index.Index;

import java.io.File;

/**
 * This is the writer of an index that afterwards can be read by
 * a {@link ContextIndex}. This to separate writing and reading
 * of indexes.
 */
public interface ContextIndexer {
	
	void buildIndex(Index index, File indexDirectory);

    //void addContextScore (String firstWord, String secondWord, double score);

    //void writeIntoDirectory (File indexDirectory);
}
