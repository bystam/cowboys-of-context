package index;

import common.Document;

import java.io.*;

public abstract class AbstractIndexer {

    /**
     * Indexes a directory recursively, creating a index from word to occurrences in documents.
     *
     * @param path the directory to index.
     * @return the index.
     */
    public void indexDirectory(File path) {
        System.out.println("Indexing :"+path);

        if (!path.canRead())
            throw new IllegalAccessError("Can't read: "+path);
        if (!path.isDirectory())
            throw new IllegalArgumentException(path+" is not a directory");
        for (File file : path.listFiles()) {
            if (file.isDirectory()) {
                indexDirectory(file);
            } else {
                indexFile(file);
            }
        }
    }

    /**
     * Indexes a single file, adding its words to the current Index.
     * @param path the file's path.
     * @param index the index which all words' occureces will be added to.
     */
    public void indexFile(File path) {
        if (!path.canRead())
            throw new IllegalAccessError("Can't read: "+path);
        Document document = new Document(path.getAbsolutePath());
        try (Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"))) {
            SimpleTokenizer tokenizer = new SimpleTokenizer(reader);
            int offset = 0;
            while (tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken();
                if (token.isEmpty())
                    continue;
                processToken (token, offset, document);
                offset++;
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    abstract void processToken(String token, int offset, Document document);
}
