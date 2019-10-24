package be.model;

public class ModelException extends RuntimeException {
    // mooie klasse als zeg ik het zelf
    private static final long serialVersionUID = 1L;

    public ModelException() {
        super();
    }

    public ModelException(String message, Throwable exception) {
        super(message, exception);
    }

    public ModelException(String message) {
        super(message);
    }

    public ModelException(Throwable exception) {
        super(exception);
    }

}
