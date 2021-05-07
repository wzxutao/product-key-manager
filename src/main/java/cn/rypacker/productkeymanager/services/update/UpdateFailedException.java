package cn.rypacker.productkeymanager.services.update;

public class UpdateFailedException extends Exception{

    public UpdateFailedException() {
    }

    public UpdateFailedException(String message) {
        super(message);
    }

    public UpdateFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UpdateFailedException(Throwable cause) {
        super(cause);
    }
}
