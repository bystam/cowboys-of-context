package index;

import common.Document;

import java.io.File;

public interface Indexer {

    void insert (String word, Document document, int offset);

    void writeIntoDirectory (File indexDirectory);
}
