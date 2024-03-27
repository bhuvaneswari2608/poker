package exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
public class GameUpdateException extends RuntimeException {
    public GameUpdateException(String message) {
        super(message);
    }
}
