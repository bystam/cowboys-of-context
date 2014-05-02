package index;

import common.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Data describing any existing number of occurrences of a word inside a document.
 */
public class PostingsEntry implements Serializable {

    private static final long serialVersionUID = 12837189273L;

    private final Document document;
    private final List<Integer> offsets = new ArrayList<>();
    private double tf_idf;

    public PostingsEntry(Document document) {
        this.document = document;
    }

    public Document getDocument() {
        return document;
    }

    public List<Integer> getOffsets() {
        return offsets;
    }

    public void addOffset(int offset) {
        offsets.add (offset);
    }


    //TODO: Tf-idf scores are not yet supported in WordIndexer
    public double getTfIdf(){ return tf_idf; }

    public void setTfIdf(double tf_idf){ this.tf_idf = tf_idf; }
}
