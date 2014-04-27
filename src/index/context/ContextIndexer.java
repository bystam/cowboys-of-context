package index.context;

import java.io.File;

public interface ContextIndexer {
	
	void buildIndex(Index index, File indexDirectory);

    //void addContextScore (String firstWord, String secondWord, double score);

    //void writeIntoDirectory (File indexDirectory);
}
