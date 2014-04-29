package index.context;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * An implementation of a {@link ContextIndex} which uses a directory
 * containing serialized postings lists as its source.
 */
public class DirectoryContextIndex implements ContextIndex {

    private final Path indexDirectory;

    public DirectoryContextIndex (Path indexDirectory) {
        if (!Files.isDirectory(indexDirectory))
            throw new IllegalArgumentException("Input must be an index directory: " + indexDirectory);
        this.indexDirectory = indexDirectory;
    }

    @Override
    public ContextScore getContextScore(String firstWord, String secondWord) {
        return null;
    }

    @Override
    public List<ContextScore> getContextsForWord(String word) {
        return null;
    }

    @Override
    public Context getContextForWords(List<String> words) {
        return null;
    }
}
