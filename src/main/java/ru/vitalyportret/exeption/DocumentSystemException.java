package ru.vitalyportret.exeption;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.logging.Level;
import java.util.logging.Logger;

@Data
@EqualsAndHashCode(callSuper = true)
public class DocumentSystemException extends IllegalArgumentException {

    private static final Logger log = Logger.getLogger("Log");

    private DocumentSystemExceptionType exceptionType;

    public DocumentSystemException(DocumentSystemExceptionType type) {
        super(type.toString());
        this.exceptionType = type;
        log.log(Level.WARNING, type.toString());
    }

    public DocumentSystemException(DocumentSystemExceptionType type, String message) {
        super(message);
        this.exceptionType = type;
        log.log(
                Level.WARNING,
                new StringBuilder(type.toString())
                        .append(" ")
                        .append(message)
                        .toString()
        );
    }
}
