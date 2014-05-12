package index.context;

/**
 * A filter removing one-character words that are not the swedish
 * words å or ö.
 */
public class OneCharacterContextFilter implements ContextFilter {

    @Override
    public boolean isValid(String word, double tf_idf) {
        if (word.length() > 1)
            return true;
        char thatCharacter = word.charAt(0);
        return thatCharacter == 'å' || thatCharacter == 'ö';
    }
}
