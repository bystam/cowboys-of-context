package index;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 *
 */
public class DirectoryIndex implements Index {

    private final Path indexDirectory;

    public DirectoryIndex (Path indexDirectory) {
        if (!Files.isDirectory(indexDirectory))
            throw new IllegalArgumentException("Input must be an index directory: " + indexDirectory);
        this.indexDirectory = indexDirectory;
    }

    @Override
    public PostingsList getPostingsList(String word) {
        return null;
    }
}
