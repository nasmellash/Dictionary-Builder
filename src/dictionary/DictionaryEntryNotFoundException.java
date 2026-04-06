package dictionary;

/**
 * A custom unchecked exception thrown when a word is not found
 * in dictionary during a search or removal.
 */
public class DictionaryEntryNotFoundException extends RuntimeException {
    public DictionaryEntryNotFoundException(String message) {
        super(message);
    }
}
