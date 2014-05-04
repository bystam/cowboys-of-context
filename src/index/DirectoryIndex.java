package index;

import common.Document;

import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
/**
 * Handles a index written to disk. Uses a cache to speed up the reading.
 */
public class DirectoryIndex implements Index {
	private final Path directory;
	private DocumentMetaData documentMetaData;
	/**
	 * A cached index which contains the last MAX_SIZE posting lists retrieved.
	 */
	private LinkedHashMap<String, PostingsList> index = new LinkedHashMap<String, PostingsList>(100000, 0.75f, true) {
		private static final long serialVersionUID = 23423535345l;
		private static final int MAX_SIZE = 100000;  //Cache size in number of postings lists
		@Override
		protected boolean removeEldestEntry(Map.Entry<String, PostingsList> entry) {
			return size()>MAX_SIZE;
		}
	};

	/**
	 * Creates a index from a directory containing the index.
	 * @param indexDirectory the directory with the cache.
	 */
	public DirectoryIndex(Path indexDirectory) {
		this.directory = indexDirectory;
		if (!Files.isReadable (indexDirectory) || !Files.isDirectory(indexDirectory)) {
			throw new IllegalArgumentException("Can't read the word index or it's not directory.");
		}
		documentMetaData = readDocumentMetaDataFromDisk();
	}
	/**
	 * Only for testing...
	 */
	public static void main(String[] args) {
		String[] wordList = {"ab", "abb", "abbas", "ab", "abbasi", "abbasite", 
							 "abbasoundet", "abbes", "abc", "abc80", "aboa"};
		DirectoryIndex index = new DirectoryIndex(Paths.get("../index"));
		for (String word : wordList) {
			PostingsList postingsList = index.getPostingsList(word);
			if (postingsList==null) {
				System.err.println("Postingslist is null");
				return;
			}
			System.out.println("Current word: "+word);
			for (PostingsEntry entry : postingsList) {
				System.out.printf("Document: %s Offsetcount: %s\n", entry.getDocument(), 
								  entry.getOffsets().size());
			}
			System.out.println("Index size: "+index.index.size());
		}
		DocumentMetaData docMeta = index.getDocumentMetaData();
		System.out.println(docMeta.getNumDocuments());
	}


	/**
	 * Retrieves a posting list from the index.
	 * @param word the word.
	 * @return the posting list associated with the word.
	 */
	public PostingsList getPostingsList(String word) {
		PostingsList postingsList = index.get(word);
		if (postingsList == null) {
			File postingsFile = wordToFileName(word);
			postingsList = readPostingsListFromDisk(postingsFile);
			if (postingsList != null) {
				index.put(word, postingsList);
			}
		}
		return postingsList != null ? postingsList : PostingsList.EMPTY;
	}

    public DocumentMetaData getDocumentMetaData() {
        return documentMetaData;
    }

    //Reads a postings list from a file
	private PostingsList readPostingsListFromDisk(File path) {
		if (!path.canRead() || path.isDirectory())
			return null;
		try (DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(path)))) {
			PostingsList postingsList = new PostingsList(in.readUTF());
			while (in.available()>0) {
				postingsList.insertPostingsEntry(readPostingsEntry(in));
			}
			return postingsList;
		} catch (IOException e) {
			return null;
		}
	}

	//Reads a single postings entry from a file
	private PostingsEntry readPostingsEntry(DataInputStream in) throws IOException {
		PostingsEntry entry = new PostingsEntry(new Document(in.readUTF()));
		int offsetCount = in.readInt();
		for (int i=0; i<offsetCount; i++) {
			entry.addOffset(in.readInt());
		}
		return entry;
	}

	private File wordToFileName(String word) {
		return wordToFileName(word, directory);
	}

	/**
	 * Maps a word to a file in the index directory.
	 * @param word the word.
	 * @return the file name.
	 */
	public static File wordToFileName(String word, Path directory) {
		try { 
			String encodedWord = URLEncoder.encode(word, "UTF-8");
			return directory.toAbsolutePath()
				.resolve(String.format("%s/index_%s.txt", encodedWord.substring(0, 1), encodedWord)).toFile();
		} catch (IOException e) {
			System.out.println("Could not find encoding: "+e);
		}
		return null;
	}

	/**
	 * Reads the DocumentMetaData from disk.
	 */
	private DocumentMetaData readDocumentMetaDataFromDisk() {
		File docMetaSavePath = directory.resolve("documentMeta.bin").toFile();
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(docMetaSavePath))) {
				return (DocumentMetaData)in.readObject();
			} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace(System.err);
		}
		return null;
	}
}
