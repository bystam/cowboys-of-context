package index.context;

import common.Document;
import index.*;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;


/**
 * Created by olivergafvert on 2014-04-29.
 */
public class WordContextIndexer extends AbstractIndexer implements ContextIndexer {

    private static final int HORIZON = 5;

    private File savePath;
    private Index index;
    protected DirectoryContextIndex disk_index;

    private DocumentMetaData metaData;

    private Document current = null;
    private Map<String, Double> tf_idf_map = new HashMap<>(100);
    private Queue<String> prev = new LinkedList<String>();
    
    
    //ContextFilter to determine which words are relevant
    private final ContextFilter context_filter = new CompositeContextFilter(
    		new Mean2ContextFilter(0.5, 0.01), 
    		new BlackListContextFilter());
    
    //private final ContextFilter context_filter = new Mean2ContextFilter(0.5, 0.01);


    /**
     * Main function that builds the index.
     */
    public void buildIndex(Index index, File savePath, File sourcePath){
        this.savePath = savePath;
        savePath.mkdirs();
        this.index = index;
        this.metaData = index.getDocumentMetaData();

        disk_index = new DirectoryContextIndex(Paths.get(savePath.getAbsolutePath()));

        indexDirectory(sourcePath);

        saveAllPostingsLists();
    }


    //Only stores the 10000 last used postings list. When ever it's full writes the last post to disk.
    private LinkedHashMap<String, ContextPostingsList> c_index = new LinkedHashMap<String, ContextPostingsList>(100000, 0.75f, true) {
        private static final long serialVersionUID = 23423535345l;
        private static final int MAX_SIZE = 100000;
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, ContextPostingsList> postingsList) {
            if (size()>MAX_SIZE) {
                savePostingsListToDisk(postingsList.getValue());
                return true;
            }
            return false;
        }
    };


    @Override public void processToken(String token, int offset, Document document) {
        if(!document.equals(current)){
        	prev.clear();
            tf_idf_map.clear();
            current = document;
            //System.out.println(numDocumentsProcessed + " documents");
        }

        if(!tf_idf_map.containsKey(token)){
            tf_idf_map.put(token, getTfIdf(token, current));
        }

        if(!context_filter.isValid(token, tf_idf_map.get(token))){
            return;
        }

        if(!c_index.containsKey(token)){
            c_index.put(token, new ContextPostingsList(token));
        }

        int i  = 0;
        for(String prevt : prev){
            if(prevt.equals(token)) continue;

            //Kind of sure contextWeight gets the right offset difference
            Double weight = contextWeight(tf_idf_map.get(token),  tf_idf_map.get(prevt), HORIZON, i, metaData.getDocumentLength(current));

            c_index.get(prevt).addEntry(token, weight);
            c_index.get(token).addEntry(prevt, weight);

            i++;
        }
        
        if(prev.size() >= HORIZON){
        	prev.poll();
        }
        prev.add(token);
    }

    double getTfIdf (String token, Document current) {
        PostingsList pl = index.getPostingsList(token);
        if(pl==null){
            return 0;
        }
        PostingsEntry pe = pl.getEntryForDocument(current);
        return pe != null ? pe.getTfIdf() : 0;
    }

    double contextWeight(double tf_a, double tf_b, double off_a, double off_b, double length){
        return (tf_a+tf_b)*distanceFactor(off_a, off_b, length);
    }

    /*A function that weights the distance between two words in some
	 * document. 'a' is the position of word1 and 'b' is the position of word2,
	 * length is the length of the document.
	 */
    double distanceFactor(double a, double b, double length){
        if (length == 0) {
            return 0;
        }
        return Math.exp(-((a-b)*(a-b))/(length*length));
    }


    private void savePostingsListToDisk(ContextPostingsList postingsList) {
    	System.out.print("*");   
        File saveFileName = DirectoryIndex.wordToFileName(postingsList.getOriginalWord(), savePath.toPath());
        saveFileName.getParentFile().mkdirs();
        boolean exists = saveFileName.exists();

        /* If the postingslist exists on file, read that postingslist from file and merge it
            with postingslist in memory and then save to file.
         */
        if(exists){
            ContextPostingsList pl = disk_index.readPostingsList(postingsList.getOriginalWord());
            if(pl == null){
            	throw new NullPointerException("origWord: " + postingsList.getOriginalWord()
            			+ "\nsaveFileName: " + saveFileName 
            			+ "\npl: " + pl 
            			+ "\n" + disk_index.getContextForWord(postingsList.getOriginalWord()));
            }
            for(WordRelation wr : postingsList){
                WordRelation wr2 = pl.get(wr.getSecondWord());
                if(wr2 != null) {
                    pl.addEntry(wr.getSecondWord(),wr2.getScore());
                }
            }
            postingsList = pl;
        }
        try (OutputStream outStream = new BufferedOutputStream(new FileOutputStream(saveFileName, true))) {
            DataOutputStream out = new DataOutputStream(outStream);

            out.writeUTF(postingsList.getOriginalWord());

            for (WordRelation entry : postingsList) {
            	savePostingsEntry(out, entry);
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    private void savePostingsEntry(DataOutputStream out, WordRelation entry) throws IOException {
        out.writeUTF(entry.getSecondWord());
        out.writeDouble(entry.getScore());
    }

    public void saveAllPostingsLists() {
    	System.out.println("saveAllPostingsLists() c_index.size == " + c_index.size());
        int done = 0;
        for (ContextPostingsList postingsList : c_index.values()) {
            savePostingsListToDisk(postingsList);
            if (done++ % 10000 == 0)
                System.out.printf("%d writes are done\n", done);
        }
    }


    /**
     * Main function for indexing with a contextindexer
     * @param args
     * args[0] - savepath of the context index
     * args[1] - location of normal index
     * args[2]... - directories/files that the context indexer should process (should be the same as given to the normal indexer)
     */

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: java WordContextIndexer savepath indexpath docpath [doc path]...");
            return;
        }
        File savePath = new File(args[0]);
        Index index = new DirectoryIndex(Paths.get(args[1]));
        WordContextIndexer indexer = new WordContextIndexer();
        indexer.buildIndex(index, savePath, new File(args[2]));
        

//        for (int i=2; i<args.length; i++) {
//            File loadPath = new File(args[i]);
//            try {
//              indexer.indexDirectory(loadPath);
//            } catch (Exception e) {
//                System.err.println("There was an error while processing the directory: "+loadPath);
//                e.printStackTrace(System.err);
//            }
//        }
        
//        indexer.saveAllPostingsLists();
    }


}
