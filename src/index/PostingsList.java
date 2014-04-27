package index;

import common.Document;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PostingsList implements Serializable, Iterable<PostingsEntry> {

    private static final long serialVersionUID = 112389123123L;

    private final Map<Document, PostingsEntry> postingsLookup = new HashMap<>();

    @Override
    public Iterator<PostingsEntry> iterator() {
        return postingsLookup.values().iterator();
    }
}
