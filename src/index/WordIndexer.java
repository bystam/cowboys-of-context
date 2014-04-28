package index;
import java.util.*;
import java.io.File;
import java.io.*;
import common.*;
public class WordIndexer {
	private File indexPath;
	private File savePath;
	private File titleIndexPath;
	TitleIndex titleIndex;

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
		if (args.length < 3) {
			System.out.println("Usage: java WordIndexer savepath titlefile docpath [doc path]...");
			return;
		}
		File savePath = new File(args[0]);
		savePath.mkdirs();
		File titleIndexPath = new File(args[1]);
		WordIndexer indexer = new WordIndexer(savePath, titleIndexPath);
		for (int i=2; i<args.length; i++) {
			File loadPath = new File(args[i]);
			try {
				indexer.indexDirectory(loadPath);
			} catch (Exception e) {
				System.err.println("There was an error while processing the directory: "+loadPath);
				System.err.println(e.getMessage());
			}
		}
		indexer.saveAllPostingsLists();
	}
	
	/**
	 * Creates a indexer which stores the index at the supplied path.
	 * @param savePath the path to save the index at.
	 */
	public WordIndexer(File savePath, File titleIndex) {
		if (!savePath.isDirectory())
			throw new IllegalArgumentException("The save path must be a directory: "+savePath);
		if (!titleIndex.canRead())
			throw new IllegalArgumentException("Can't read from titleIndex: "+titleIndex);		
		this.savePath = savePath;
		this.titleIndexPath = titleIndex;
		this.titleIndex = new TitleIndex(titleIndexPath);
	}
	/**
	 * Indexes a directory recursivly, creating a index from word to occurences in documents.
	 *
	 * @param path the directory to index.
	 * @return the index.
	 */
	public void indexDirectory(File path) {
		System.out.println("Indexing :"+path);
		indexPath = path;
		if (!path.canRead())
			throw new IllegalAccessError("Can't read: "+path);
		if (!path.isDirectory())
			throw new IllegalArgumentException(path+" is not a directory");
		for (File file : path.listFiles()) {
			if (file.isDirectory()) {
				indexDirectory(file);
			} else {
				indexFile(file);
			}
		}
	}

	/**
	 * Indexes a single file, adding its words to the current Index.
	 * @param path the file's path.
	 * @param index the index which all words' occureces will be added to.
	 */
	public void indexFile(File path) {
		if (!path.canRead())
			throw new IllegalAccessError("Can't read: "+path);
		Document doc = new Document(titleIndex.getTitle(path), path.getPath());
		try (Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"))) {
			SimpleTokenizer tokenizer = new SimpleTokenizer(reader);
			int offset = 0;
			while (tokenizer.hasMoreTokens()) {
				String token = tokenizer.nextToken();
				if (token.isEmpty())
					continue;
				insertWord(token, doc, offset);
				offset++;
			}
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}
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
		out.writeUTF(entry.getDocument().getTitle());
		out.writeUTF(entry.getDocument().getFilePath());
		List<Integer> offsets = entry.getOffsets();
		out.writeInt(offsets.size());
		for (int offset : offsets) {
			out.writeInt(offset);
		}
	}

	/**
	 * Saves all postings lists stored in memory to disk. Should be used when there are no more
	 * documents to procces.
	 */
	public void saveAllPostingsLists() {
		for (PostingsList postingsList : index.values()) {
			savePostingsListToDisk(postingsList);
		}
	}
}
