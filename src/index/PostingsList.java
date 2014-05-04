package index;

import common.Document;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PostingsList implements Serializable, Iterable<PostingsEntry> {

    public static final PostingsList EMPTY = new PostingsList(null);

    private static final long serialVersionUID = 112389123123L;
    private final String word;

    private final Map<Document, PostingsEntry> postingsLookup = new HashMap<>();

	public PostingsList(String word) {
		this.word = word;
	}
	
    @Override
    public Iterator<PostingsEntry> iterator() {
        return postingsLookup.values().iterator();
    }
	
	public void insertWord(Document doc, int offset) {
		PostingsEntry entry = postingsLookup.get(doc);
		if (entry == null) {
			entry = new PostingsEntry(doc);
			postingsLookup.put(doc, entry);
		}
		entry.addOffset(offset);
	}

	public void insertPostingsEntry(PostingsEntry entry) {
		postingsLookup.put(entry.getDocument(), entry);
	}

	public String getWord() {
		return word;
	}
	
	public int getNumDocuments(){
		return postingsLookup.size();
	}
}
