package common;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * A simple document identifier, instead of using the quite
 * stressful docID-system. The file path of the document
 * is a sufficient identifier.
 */
public class Document implements Comparable<Document> {

    private final String name;
    private final Path filePath;

    public Document (String filePath) {
        this(null, filePath);
    }

    public Document(String name, String filePath) { // TODO remove with field name
        this.name = name;
        this.filePath = Paths.get(filePath);
    }

	public String getTitle() {
		return name;
	}

	public String getFilePath() {
		return filePath.toString();
	}

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Document d = (Document) o;
        return name.equals(d.name) && filePath.equals(d.filePath);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + filePath.hashCode();
        return result;
    }

	@Override
	public String toString() {
		return String.format("[%s \"%s\"]" , filePath, name);
	}

    @Override
    public int compareTo(Document o) {
        return filePath.compareTo(o.filePath);
    }
}
