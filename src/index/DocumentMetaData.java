package index;
import java.util.*;
import common.*;
import java.io.Serializable;

public class DocumentMetaData implements Serializable {
	private static final long serialVersionUID = 4224235L;
	//The total number of documents
	private int documentCount;
	//A map of the lengtg, as the number of words, of each document
	private Map<Document, Integer> documentLengths = new HashMap<>();

	public void setDocumentCount(int docCount) {
		documentCount = docCount;
	}

	public void setDocumentLength(Document doc, int length) {
		documentLengths.put(doc, length);
	}

	public int getNumDocuments() {
		return documentCount;
	}

	public int getDocumentLength(Document doc) {
		return documentLengths.get(doc);
	}
}
