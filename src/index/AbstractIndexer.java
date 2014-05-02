package index;

import common.Document;
import java.nio.file.*;
import java.io.*;

public abstract class AbstractIndexer {
	Path root;
    /**
     * Indexes a directory recursively, creating a index from word to occurrences in documents.
     *
     * @param path the directory to index.
     * @return the index.
     */
	public void indexDirectory(File path) {
		root = path.toPath();
		indexDirectoryHelper(path);
	}

    private void indexDirectoryHelper(File path) {
        System.out.println("Indexing :"+path);
        if (!path.canRead())
            throw new IllegalAccessError("Can't read: "+path);
        if (!path.isDirectory())
            throw new IllegalArgumentException(path+" is not a directory");
        for (File file : path.listFiles()) {
			System.out.println("index file: "+file);
            if (file.isDirectory()) {
                indexDirectoryHelper(file);
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
		System.out.println("index file");
        if (!path.canRead())
            throw new IllegalAccessError("Can't read: "+path);
        Document document = new Document(root.relativize(path.toPath()).toString());
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

    public abstract void processToken(String token, int offset, Document document);
}
