package index.context;

public class CompositeContextFilter implements ContextFilter{
	
	ContextFilter[] filters;
	
	public CompositeContextFilter(ContextFilter... filters) {
		this.filters = filters;
	}

	@Override
	public boolean isValid(String word, double tf_idf) {
		for(ContextFilter filter : filters){
			if(!filter.isValid(word, tf_idf)){
				return false;
			}
		}
		return true;
	}
	
}
