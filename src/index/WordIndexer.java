package index;
import common.Document;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.nio.file.*;
public class WordIndexer extends AbstractIndexer {
	private File indexPath;
	private File savePath;
	private File titleIndexPath;
	private DocumentMetaData docMetaData = new DocumentMetaData();
	private int docLength = 0;
	private Document lastDoc = null;	

	//Only stores the 10000 last used postings list. When ever it's full writes the last post to disk.
	private LinkedHashMap<String, PostingsList> index = new LinkedHashMap<String, PostingsList>(100000, 0.75f, true) {
		private static final long serialVersionUID = 23423535345l;
		private final int MAX_SIZE = 100000;
		@Override
		protected boolean removeEldestEntry(Map.Entry<String, PostingsList> postingsList) {
			if (size()>MAX_SIZE) {
				savePostingsListToDisk(postingsList.getValue());
				return true;
			}
			return false;
		}
	};

	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("Usage: java WordIndexer savepath docpath [doc path]...");
			return;
		}
		File savePath = new File(args[0]);
		savePath.mkdirs();
		WordIndexer indexer = new WordIndexer(savePath);
		for (int i=1; i<args.length; i++) {
			File loadPath = new File(args[i]);
			try {
				indexer.indexDirectory(loadPath);
			} catch (Exception e) {
				System.err.println("There was an error while processing the directory: "+loadPath);
				e.printStackTrace(System.err);
			}
		}
		indexer.saveToDisk();
	}
	
	/**
	 * Creates a indexer which stores the index at the supplied path.
	 * @param savePath the path to save the index at.
	 */
	public WordIndexer(File savePath) {
		if (!savePath.isDirectory())
			throw new IllegalArgumentException("The save path must be a directory: "+savePath);
		this.savePath = savePath;
	}
	

    @Override
    public void processToken(String token, int offset, Document document) {
        insertWord(token, document, offset);
		if (offset==0 && lastDoc != null) {
			docMetaData.setDocumentLength(lastDoc, docLength);
			docMetaData.setDocumentCount(docMetaData.getNumDocuments()+1);
		}
		docLength = offset;
		lastDoc = document;
    }

    public void insertWord(String word, Document doc, int offset) {
		PostingsList postingsList = index.get(word);
		if (postingsList == null) {
			postingsList = new PostingsList(word);
			index.put(word, postingsList);
		}
		postingsList.insertWord(doc, offset);
	}

	private void savePostingsListToDisk(PostingsList postingsList) {
		File saveFileName = WordIndexDisk.wordToFileName(postingsList.getWord(), savePath);
		saveFileName.getParentFile().mkdirs();
		boolean continueWriting = saveFileName.exists();
		try (OutputStream outStream = new BufferedOutputStream(new FileOutputStream(saveFileName, true))) {
			DataOutputStream out = new DataOutputStream(outStream);
			if (!continueWriting) {
				out.writeUTF(postingsList.getWord());
			}
			for (PostingsEntry entry : postingsList) {
				savePostingsEntry(out, entry);
			}
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}
	}

	private void savePostingsEntry(DataOutputStream out, PostingsEntry entry) throws IOException {
		out.writeUTF(entry.getDocument().getFilePath());
		List<Integer> offsets = entry.getOffsets();
		out.writeInt(offsets.size());
		for (int offset : offsets) {
			out.writeInt(offset);
		}
	}
	
	private void saveDocumentMetaDataToDisk() {
		Path metaSavePath = savePath.toPath().resolve("documentMeta.bin");
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(metaSavePath.toFile()))) {
		    out.writeObject(docMetaData);
		} catch(IOException e) {
			e.printStackTrace(System.err);
		}
	}
	/**
	 * Saves all postings lists stored in memory to disk. Should be used when there are no more
	 * documents to procces.
	 */
	public void saveToDisk() {
		//Update info for the last document
		docMetaData.setDocumentLength(lastDoc, docLength);
		docMetaData.setDocumentCount(docMetaData.getNumDocuments()+1);
		for (PostingsList postingsList : index.values()) {
			savePostingsListToDisk(postingsList);
		}
		saveDocumentMetaDataToDisk();
	}
}
