package index.context;

import java.util.HashSet;
import java.util.Set;

public class BlackListContextFilter implements ContextFilter {

	private final Set<String> blacklist = new HashSet<String>();
	
	/* BlackListContextFilter.isValid is used to check if a word
	 * is important enough to be considered in the
	 * synonym index. The rule to classify a document
	 * as important could e.g be if it occurs in
	 * as least one document with tf-idf > TF_IDF_THRESHOLD.
	 */
	public boolean isValid(String word, double tf_idf){
		return !blacklist.contains(word);
	}

}
