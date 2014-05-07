package index.context;

/**
 * Created by olivergafvert on 2014-05-04.
 */
public class Mean2ContextFilter implements ContextFilter {

    private double mean_tf_idf = 0;
    private int num_updates = 0; //number of tf-idf scores used in mean prediction
    private double THRESH;
    private double factor; //percentage of mean that should be used as thresh
    private double alpha; //momentum of mean value prediction

    Mean2ContextFilter(double p, double alpha){
        this.factor = p;
        this.alpha = alpha;
    }


    @Override
    public boolean isValid(String word, double tf_idf) {
        num_updates++;
        mean_tf_idf += (tf_idf - mean_tf_idf)/num_updates;
        THRESH = mean_tf_idf*factor;
        return tf_idf > THRESH;
    }
}
