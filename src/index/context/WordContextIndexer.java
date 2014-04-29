package index.context;

import common.Document;
import index.*;

import java.io.*;
import java.util.*;

/**
 * Created by olivergafvert on 2014-04-29.
 */
public class WordContextIndexer extends AbstractIndexer implements ContextIndexer {


    private File savePath;
    private File sourcePath;
    private Index index;

    public void buildIndex(Index index, File savePath, File sourcePath){

        this.savePath = savePath;
        this.sourcePath = sourcePath;
        this.index = index;

        indexDirectory(sourcePath);

    }


    private int horizon = 100;

    private HashMap<String, Double> tf_idf_map = new HashMap<String, Double>(100);


    //Only stores the 10000 last used postings list. When ever it's full writes the last post to disk.
    private LinkedHashMap<String, ContextPostingsList> c_index = new LinkedHashMap<String, ContextPostingsList>(100000, 0.75f, true) {
        private static final long serialVersionUID = 23423535345l;
        private final int MAX_SIZE = 100000;
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, ContextPostingsList> postingsList) {
            if (size()>MAX_SIZE) {
                savePostingsListToDisk(postingsList.getValue());
                return true;
            }
            return false;
        }
    };


    private ArrayList<String> prev = new ArrayList<String>(horizon);
    private Document current = null;

    private int horizon_p = 0;

    public void processToken(String token, int offset, Document document){

        if(document.equals(current)){
            prev.clear();
            c_index.clear();
            tf_idf_map.clear();
            current = document;
        }

        if(!tf_idf_map.containsKey(token)){
            tf_idf_map.put(token, index.getTfIdf(token, current));
        }

        if(!c_index.containsKey(token)){
            c_index.put(token, new ContextPostingsList(token));
        }

        ContextPostingsList t_map = c_index.get(token);

        for(int i=0;i<prev.size();i++){
            String prevt = prev.get(i);

            //Kind of sure contextWeight gets the right offset difference
            Double weight = contextWeight(tf_idf_map.get(token), tf_idf_map.get(prevt), horizon_p+1, (horizon_p - i)%horizon, current.getLength());

            t_map.addEntry(prevt, weight);
            c_index.get(prevt).addEntry(token, weight);
        }

        prev.add(horizon_p, token);
        horizon_p = (horizon_p + 1) % horizon;


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
        File saveFileName = WordIndexDisk.wordToFileName(postingsList.getOriginalWord(), savePath);
        saveFileName.getParentFile().mkdirs();
        boolean continueWriting = saveFileName.exists();
        try (OutputStream outStream = new BufferedOutputStream(new FileOutputStream(saveFileName, true))) {
            DataOutputStream out = new DataOutputStream(outStream);
            if (!continueWriting) {
                out.writeUTF(postingsList.getOriginalWord());
            }
            for (WordRelation entry : postingsList) {
                savePostingsEntry(out, entry, postingsList.getOriginalWord());
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    private void savePostingsEntry(DataOutputStream out, WordRelation entry, String name) throws IOException {
        out.writeUTF(entry.getOtherWord(name));
        out.writeDouble(entry.getScore());
    }

    public void saveAllPostingsLists() {
        for (ContextPostingsList postingsList : c_index.values()) {
            savePostingsListToDisk(postingsList);
        }
    }


    }
