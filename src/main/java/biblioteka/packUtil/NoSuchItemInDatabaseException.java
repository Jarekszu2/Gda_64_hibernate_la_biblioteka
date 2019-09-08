package biblioteka.packUtil;

public class NoSuchItemInDatabaseException extends Throwable {
    public NoSuchItemInDatabaseException(String message) {
        super(message);
    }
}
