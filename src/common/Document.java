package common;

import java.nio.file.Paths;

public class Document {

    private final String name;
    private final String filePath;

    public Document(String name, String filePath) {
        this.name = name;
        this.filePath = filePath;
    }

	public String getTitle() {
		return name;
	}

	public String getFilePath() {
		return filePath;
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
		return String.format("[%s \"%s\"]" , Paths.get(filePath), name);
	}
}
