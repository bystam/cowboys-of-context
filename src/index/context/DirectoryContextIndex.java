package index.context;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
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
    public List<WordRelation> getContextForWord(String word) {
        return null;
    }

    @Override
    public ContextsMap getContextsForWords(Collection<String> words) {
        return null;
    }

    public ContextPostingsList readPostingsList(String name){
        File path = indexDirectory.resolve(name).toFile();
        if (!path.canRead() || path.isDirectory())
            return null;
        try (DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(path)))) {
            ContextPostingsList postingsList = new ContextPostingsList(in.readUTF());
            while (in.available()>0) {
                postingsList.addEntry(in.readUTF(), in.readDouble());
            }
            return postingsList;
        } catch (IOException e) {
            return null;
        }

    }

}
