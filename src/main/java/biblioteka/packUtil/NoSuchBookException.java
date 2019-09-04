package biblioteka.packUtil;

public class NoSuchBookException extends Throwable {
    public NoSuchBookException(String message) {
        super(message);
    }
}
