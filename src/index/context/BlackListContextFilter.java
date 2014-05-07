package index.context;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BlackListContextFilter implements ContextFilter {
	
	

	private final Set<String> blacklist = new HashSet<String>();
	
	public BlackListContextFilter(){
		blacklist.addAll(Arrays.asList(new String[]{
				"alla",
				"allt", 
				"alltså", 
				"andra", 
				"att", 
				"bara", 
				"bli", 
				"blir", 
				"borde", 
				"bra", 
				"mitt", 
				"ser", 
				"dem", 
				"den", 
				"denna", 
				"det", 
				"detta", 
				"dig", 
				"din", 
				"dock", 
				"dom", 
				"där", 
				"edit", 
				"efter", 
				"eftersom", 
				"eller", 
				"ett", 
				"fast", 
				"fel", 
				"fick", 
				"finns", 
				"fram", 
				"från", 
				"får", 
				"fått", 
				"för", 
				"första", 
				"genom", 
				"ger", 
				"går", 
				"gör", 
				"göra", 
				"hade", 
				"han", 
				"har", 
				"hela", 
				"helt", 
				"honom", 
				"hur", 
				"här", 
				"i",
				"iaf", 
				"igen", 
				"ingen", 
				"inget", 
				"inte", 
				"jag", 
				"kan", 
				"kanske", 
				"kommer", 
				"lika", 
				"lite", 
				"man", 
				"med", 
				"men", 
				"mer", 
				"mig", 
				"min", 
				"mot", 
				"mycket", 
				"många", 
				"måste", 
				"nog", 
				"när", 
				"någon", 
				"något", 
				"några", 
				"nån", 
				"nåt", 
				"och", 
				"också", 
				"rätt", 
				"samma", 
				"sedan", 
				"sen", 
				"sig", 
				"sin", 
				"själv", 
				"ska", 
				"skulle", 
				"som", 
				"sätt", 
				"tar", 
				"till", 
				"tror", 
				"tycker", 
				"typ", 
				"upp", 
				"utan", 
				"vad", 
				"var", 
				"vara", 
				"vet", 
				"vid", 
				"vilket", 
				"vill", 
				"väl", 
				"även", 
				"över",
				"quot",
				"ref",
				"gt", // TODO not sure
				"lt" //TODO not sure
		}));
		
	}
	
	
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
