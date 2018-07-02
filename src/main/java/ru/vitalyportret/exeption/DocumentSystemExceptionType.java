package ru.vitalyportret.exeption;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum DocumentSystemExceptionType {

    COMPANY_NOT_FOUND(HttpStatus.NOT_FOUND),
    COMPANY_MAX_WORKFLOW(HttpStatus.BAD_REQUEST),

    DOCUMENT_CREATE_ERROR(HttpStatus.BAD_REQUEST),
    DOCUMENT_EDIT_ERROR(HttpStatus.BAD_REQUEST),
    DOCUMENT_NOT_FOUND(HttpStatus.NOT_FOUND),

    SYSTEM_EDIT_OR_SIGN_DOC_EXCEPTION(HttpStatus.BAD_REQUEST), DOCUMENT_IS_COMPLETE(HttpStatus.BAD_REQUEST), COMPANY_MAX_DOC_FLOW(HttpStatus.BAD_REQUEST);

    private final HttpStatus httpStatus;

    DocumentSystemExceptionType(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
