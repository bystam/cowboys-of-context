package index;
import java.util.*;
import java.io.*;
/**
 * Keeps a map between document ids and their title.
 */
public class TitleIndex {
	private final Map<Integer, String> titles = new HashMap<>();
	private final File titleFile;
	private static boolean debug = false;
	public TitleIndex(File titleFile) {
		if (!titleFile.canRead())
			throw new IllegalArgumentException("Can't read index file: "+titleFile);
		this.titleFile = titleFile;
		readTitleFile();
	}
	
	/**
	 * Only used for testing.
	 */
	public static void main(String[] args) {
		debug = true;
		TitleIndex titleIndex = new TitleIndex(new File("../articleTitles.txt"));
		System.out.println(titleIndex.getTitle(new File("../1000/33.txt")));
	}

	//parses the titleFile
	private void readTitleFile() {
		try (Reader r = new InputStreamReader(new FileInputStream(titleFile) , "UTF-8")) {
			BufferedReader reader = new BufferedReader(r);
			String line;
			while ((line = reader.readLine()) != null) {
				String[] splittedLine = line.split(";");
				titles.put(Integer.parseInt(splittedLine[0]), splittedLine[1]);
			}
		} catch(IOException e) {
			e.printStackTrace(System.err);
		}
	}
	
	/**
	 * Extracts a number from the file name and uses it as ID to get the title.
	 */
	public String getTitle(File docFile) {
		//Extract the number from the filename
		String idAsString = docFile.getName().replaceAll("[\\D]" , "");
		if (debug)
			System.out.println(idAsString);
		if (idAsString.isEmpty())
			return "";
		return getTitle(Integer.parseInt(idAsString));
	}
	
	/**
	 * Retrieves the title of the document with the supplied id.
	 * @param id the document id.
	 * @return the documents title.
	 */
	public String getTitle(int id) {
		String title = titles.get(id);
		return (title!=null) ? title : "";
	}
}
