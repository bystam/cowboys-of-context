package index.context;

/**
 * Created by olivergafvert on 2014-05-04.
 */
public class MeanContextFiler implements ContextFilter {

    private double mean_tf_idf = 0.1;
    private double THRESH;

    MeanContextFiler(double p){
        this.THRESH = p*mean_tf_idf;
    }

    @Override
    public boolean isValid(String word, double tf_idf) {
        return tf_idf > THRESH;
    }
}
