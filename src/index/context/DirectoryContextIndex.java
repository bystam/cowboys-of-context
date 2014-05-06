package index.context;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * An implementation of a {@link ContextIndex} which uses a directory
 * containing serialized postings lists as its source.
 */
public class DirectoryContextIndex implements ContextIndex {

    private final Path indexDirectory;

    /**
     * A cached index which contains the last MAX_SIZE posting lists retrieved.
     */
    private LinkedHashMap<String, ContextPostingsList> index = new LinkedHashMap<String, ContextPostingsList>(100000, 0.75f, true) {
        private static final long serialVersionUID = 23423535345l;
        private static final int MAX_SIZE = 100000;  //Cache size in number of postings lists
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, ContextPostingsList> entry) {
            return size()>MAX_SIZE;
        }
    };


    public DirectoryContextIndex (Path indexDirectory) {
        if (!Files.isDirectory(indexDirectory))
            throw new IllegalArgumentException("Input must be an index directory: " + indexDirectory);
        this.indexDirectory = indexDirectory;
    }


    @Override
    public ContextPostingsList getContextForWord(String word) {

        if(index.containsKey(word)){
            return index.get(word);
        }
        ContextPostingsList pl = readPostingsList(word);
        if(pl != null){
            index.put(word, pl);
        }
        return pl;
    }


    @Override
    public ContextsMap getContextsForWords(Collection<String> words) {
        ContextsMap map = new ContextsMap();
        for(String word : words){
            ContextPostingsList pl = getContextForWord(word);
            if(pl != null){
                map.putContextScoresForWord(word, pl);
            }
        }
        return map;
    }



    ContextPostingsList readPostingsList(String name){
        File path = indexDirectory.resolve(name.charAt(0) + "/index_" + name + ".txt").toFile();
        if (!path.canRead() || path.isDirectory()){
        	System.out.println("readPostingsList()   cannot read " + path);//TODO
        	System.out.println("file exists: " + path.exists());
        	return null;
        }
            
        try (DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(path)))) {
            ContextPostingsList postingsList = new ContextPostingsList(in.readUTF());
            while (in.available()>0) {
                postingsList.addEntry(in.readUTF(), in.readDouble());
            }
            return postingsList;
        } catch (IOException e) {
        	System.out.println("readPostingsList()   IOException " + e);//TODO
            return null;
        }

    }

}
