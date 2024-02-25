package cn.rypacker.productkeymanager.exception;

import lombok.Data;
import lombok.Getter;

public class IdentifiedWebException extends RuntimeException
{
    @Getter
    private final int status;

    public IdentifiedWebException(int status) {
        this.status = status;
    }

    public IdentifiedWebException(String message, int status) {
        super(message);
        this.status = status;
    }

    public IdentifiedWebException(String message, Throwable cause, int status) {
        super(message, cause);
        this.status = status;
    }

    public IdentifiedWebException(Throwable cause, int status) {
        super(cause);
        this.status = status;
    }

    public IdentifiedWebException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, int status) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.status = status;
    }
}
