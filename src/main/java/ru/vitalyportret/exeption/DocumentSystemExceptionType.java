package ru.vitalyportret.exeption;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum DocumentSystemExceptionType {

    DOC_SYSTEM_EXCEPTION(HttpStatus.BAD_REQUEST),
    DOC_SYSTEM_EDIT_OR_SIGN_DOC_EXCEPTION(HttpStatus.BAD_REQUEST),

    COMPANY_NOT_FOUND(HttpStatus.NOT_FOUND),
    COMPANY_MAX_DOC_FLOW(HttpStatus.BAD_REQUEST),
    COMPANY_MAX_DOC_IN_HOUR(HttpStatus.BAD_REQUEST),
    COMPANIES_NOT_EQUAL_EXCEPTION(HttpStatus.BAD_REQUEST),

    MAX_CREATED_DOCS_BETWEEN_COMPANIES(HttpStatus.BAD_REQUEST),

    DOCUMENT_NOT_FOUND(HttpStatus.NOT_FOUND),
    DOCUMENT_CREATE_EXCEPTION(HttpStatus.BAD_REQUEST),
    DOCUMENT_EDIT_EXCEPTION(HttpStatus.BAD_REQUEST),
    DOCUMENT_IS_COMPLETE(HttpStatus.BAD_REQUEST);

    private final HttpStatus httpStatus;

    DocumentSystemExceptionType(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
