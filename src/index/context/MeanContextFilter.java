package index.context;

/**
 * Created by olivergafvert on 2014-05-04.
 */
public class MeanContextFilter implements ContextFilter {

    private double sum_tf_idf = 0;
    private int num_words_processed = 0;
    
    @Override
    public boolean isValid(String word, double tf_idf) {
        num_words_processed ++;
        sum_tf_idf += tf_idf;
    	return tf_idf >= sum_tf_idf/(double)num_words_processed;
    }

    @Override
    public void update(double tf_idf) { }
}
