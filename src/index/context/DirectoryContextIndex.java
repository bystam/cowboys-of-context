package index.context;

import java.io.File;
import java.util.List;

public class DirectoryContextIndex implements ContextIndex {

    private final File indexDirectory;

    public DirectoryContextIndex (File indexDirectory) {
        if (!indexDirectory.isDirectory())
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
