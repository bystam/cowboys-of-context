package index.context;

/**
 * Created by olivergafvert on 2014-05-04.
 */
public class MeanContextFiler implements ContextFilter {

    private double mean_tf_idf = 0;
    private int num_updates = 0; //number of tf-idf scores used in mean prediction
    private double THRESH;
    private double factor; //percentage of mean that should be used as thresh
    private double alpha; //momentum of mean value prediction

    MeanContextFiler(double p, double alpha){
        this.factor = p;
        this.alpha = alpha;
    }

    public void update(double tf_idf){
        num_updates++;
        mean_tf_idf = alpha*((mean_tf_idf+tf_idf)/num_updates - mean_tf_idf) + mean_tf_idf;
        THRESH = mean_tf_idf*factor;
    }

    @Override
    public boolean isValid(String word, double tf_idf) {
        return tf_idf > THRESH;
    }
}
