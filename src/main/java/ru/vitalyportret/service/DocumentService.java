package ru.vitalyportret.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.vitalyportret.DTO.DocumentForCreateDTO;
import ru.vitalyportret.entity.Document;

public interface DocumentService {

    Page<Document> getDocumentsByCompanyIdAndDocumentStatus(Pageable pageable, String idCompany, Document.Status status);

    Document findDocumentById(String id);

    Document createDocument(DocumentForCreateDTO document);

    Document editDocument(Document document, String idEditorSide);

    Document signDocument(Document document, String idSignerSide);
}
