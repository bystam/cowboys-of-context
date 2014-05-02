package common;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.*;
/**
 * A simple document identifier, instead of using the quite
 * stressful docID-system. The file path of the document
 * is a sufficient identifier.
 */
public class Document implements Comparable<Document>, Serializable {
    private static final long serialVersionUID = 128323523L;
    private final transient Path filePath;

    public Document (String filePath) {
		this.filePath = Paths.get(filePath);
        //this(null, filePath);
    }

	public Document(Path filePath) {
		this.filePath = filePath;
	}

    /*public Document(String name, String filePath) { // TODO remove with field name
        this.filePath = Paths.get(filePath);
		}*/

	public String getName() {
		return filePath.getFileName().toString();
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
        return filePath.equals(d.filePath);
    }

    @Override
    public int hashCode() {
        return filePath.hashCode();
    }

	@Override
	public String toString() {
		return filePath.toString();
	}

    @Override
    public int compareTo(Document o) {
        return filePath.compareTo(o.filePath);
    }

	private void readObject(ObjectInputStream aInputStream) throws ClassNotFoundException, IOException {
		try {
			//always perform the default de-serialization first
			aInputStream.defaultReadObject();
			//Uses some reflection magic to set a final field of document.
			Field fp = getClass().getDeclaredField("filePath");
			fp.setAccessible(true);
			fp.set(this, Paths.get(aInputStream.readUTF()));
			fp.setAccessible(false);
		} catch (IllegalAccessException | NoSuchFieldException e) {
			e.printStackTrace(System.err);
		}
	}

    /**
    * This is the default implementation of writeObject.
    * Customise if necessary.
    */
    private void writeObject(ObjectOutputStream aOutputStream) throws IOException {
      //perform the default serialization for all non-transient, non-static fields
      aOutputStream.defaultWriteObject();
	  aOutputStream.writeUTF(filePath.toString());
    }
}
