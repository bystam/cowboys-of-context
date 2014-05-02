package index;
import java.util.*;
import common.*;
import java.io.Serializable;

<<<<<<< HEAD
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

	public int getDocumentCount() {
		return documentCount;
	}

	public int getDocumentLength(Document doc) {
		return documentLengths.get(doc);
=======
import common.Document;

public class DocumentMetaData {
	
	public int getNumDocuments(){
		return 0; //TODO
	}
	
	public int getDocumentLength(Document document){
		return 0; //TODO
>>>>>>> 87681e72d33c01e08a046f4b09e7f8b38617bdcc
	}
}
