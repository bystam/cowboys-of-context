
import java.util.HashSet;

public class ContextFilter {

	
	
	/* ContextFilter.THRESH is used as general threshold
	 * for tf-idf scores
	 */
	public static double THRESH = Double.MAX_VALUE;
	
	private static HashSet<String> blacklist = new HashSet<String>(100);
	
	/* ContextFilter.isValid is used to check if a word
	 * is important enough to be considered in the
	 * synonym index. The rule to classify a document
	 * as important could e.g be if it occurs in
	 * as least one document with tf-idf > THRESH.
	 */
	public static boolean isValid(String s){
		return !blacklist.contains(s);
	}
}
