
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class SynonymIndex {
		
	HashSet<String> visited = new HashSet<String>(100);
	HashMap<String, HashMap<String, Double>> symindex = new HashMap<String, HashMap<String, Double>>(100);
	
	
	/*B_THRESH determines when the index [or batch] should be
	 * written to file.
	 */
	int B_THRESH = 1000000;
	
	/*'buildIndex' builds a synonym index using a 'normal' index. The function
	 * iterates through the postingslist of each word in the dictionary of
	 * the 'normal' index. It uses the information in those postingslists (e.g. the
	 * offsets of some word in some document and the tf-idf score of that word in that
	 * document) to compute the weights used in the synonym index.
	 * 
	 * Input is the index that should be used to build the synonym index.
	 * 
	 * PROBLEMS:
	 * - Current iteration of of postingslist in combination with writing to file
	 * in batches only fills a triangular region of the "matrix representation of the graph".
	 * This could either be fixed or a search rule can be implemented as ' if(i > j){ get(j, i)}else{ get(i, j)}'
	 * 
	 */
	void buildIndex(Index index){
		Iterator<String> dict = index.getDictionary();
		
		/*Would like a more efficient iteration of the postingslists.
		 * Currently iterators are used and a HashSet is used to
		 * remember visited postingslist.
		 */
		while(dict.hasNext()){
			String source = dict.next();
			visited.add(source);
			
			if(ContextFilter.isValid(source)){
				symindex.put(source,  new HashMap<String, Double>(10));
				HashMap<String, Double> sympl = symindex.get(source);
				PostingsList sp = index.getPostings(source);
				
				Iterator<String> targetDict = index.getDictionary();
				while(targetDict.hasNext()){
					String target = targetDict.next();
					if(!visited.contains(target)){
						PostingsList tp = index.getPostings(target);
						sympl.put(target, intersect(sp, tp));
					}
				}
				
			}
			
			if(symindex.size() > B_THRESH){
				writeIndexToFile();
			}
			
		}
		writeIndexToFile();
	}
	
	
	/*Computes the intersection of p1 and p2 and at the same time computes the
	 * sum (actually convolusion) of the difference in offset for each document
	 */
	double intersect(PostingsList t1, PostingsList t2){
		if(t1==null || t2==null){
    		return 0;
    	}
	    Iterator<PostingsEntry> p2i = t2.iterator(), p1i = t1.iterator();
	    PostingsEntry p1=null, p2;
	    if(p2i.hasNext()){
	    	p2 = p2i.next();
	    }else{
	    	return 0;
	    }
	    double weight=0;
	    boolean inc=true;
	    while(true){
	    	if(inc){
	    		if(p1i.hasNext()){
	    			p1 = p1i.next();
	    		}else{
	    			break;
	    		}
	    	}
	        inc=true;
	    	
		    if(p1.docID== p2.docID){
		    	if(p1.score>ContextFilter.THRESH && p2.score>ContextFilter.THRESH){
		    		weight += (p1.score + p2.score)*sumDistanceFactors(p1.offset, p2.offset, Index.docLengths.get(""+p1.docID));
		    	}
		    }else if(p1.docID<p2.docID){
		    	if(!p2i.hasNext()){
			    	break;
			    }
		    	p2 = p2i.next();
			    inc=false;
		    }
	    }
	    return weight;
	}
	
	
	/*Computes the sum of the distance factors for two offset lists of two words.
	 * a is the offsets of word1 in some document and b are the offsets of word2 in
	 * some document. The sum is computed using the function distanceFactor.
	 */
	double sumDistanceFactors(List<Integer> a, List<Integer> b, double length){
		double sum=0;
		for(int i=0;i<a.size();i++){
			for(int j=0;j<b.size();j++){
				sum += distanceFactor(a.get(i), b.get(j), length);
			}
		}
		return sum;
	}
	
	/*A function that weights the distance between two words in some
	 * document. 'a' is the position of word1 and 'b' is the position of word2,
	 * length is the length of the document.
	 */
	double distanceFactor(double a, double b, double length){
		if(length==0){
			return 0;
		}
		return Math.exp(-((a-b)*(a-b))/(length*length));
	}

	
	
	/*Writes and appends index/batch to index file.
	 * The index format is currently unclear...
	 */
	void writeIndexToFile(){
		try{
			RandomAccessFile f = new RandomAccessFile("index.txt", "rw");
			RandomAccessFile i = new RandomAccessFile("dict.txt", "rw");
			f.seek(f.length());
			i.seek(i.length());
			Iterator<String> it = symindex.keySet().iterator();
			while(it.hasNext()){
				String s = it.next();
				
				StringBuilder sb = new StringBuilder(100);
				
				HashMap<String, Double> tm = symindex.get(s);
				Iterator<String> it2 = tm.keySet().iterator();
				while(it2.hasNext()){
					String t = it2.next();
					sb.append(t).append(',').append(tm.get(t)).append(':');
				}
				i.writeBytes(s+' '+f.getFilePointer());
				f.writeBytes(sb.toString());
			}
			f.close();
			i.close();
			symindex.clear();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	

}
