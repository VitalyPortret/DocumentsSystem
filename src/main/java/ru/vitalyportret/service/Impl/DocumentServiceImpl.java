package ru.vitalyportret.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vitalyportret.DTO.DocumentForCreateDTO;
import ru.vitalyportret.entity.Company;
import ru.vitalyportret.entity.Document;
import ru.vitalyportret.exeption.DocumentSystemException;
import ru.vitalyportret.exeption.DocumentSystemExceptionType;
import ru.vitalyportret.repository.DocumentRepository;
import ru.vitalyportret.service.CompanyService;
import ru.vitalyportret.service.ConfigurationService;
import ru.vitalyportret.service.DocumentService;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;

    private final CompanyService companyService;

    private final ConfigurationService config;

    @Autowired
    public DocumentServiceImpl(DocumentRepository documentRepository,
                               CompanyService companyService,
                               ConfigurationService configurationService) {
        this.documentRepository = documentRepository;
        this.companyService = companyService;
        this.config = configurationService;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Document> getDocumentsByCompanyIdAndDocumentStatus(Pageable pageable,
                                                                   String idCompany,
                                                                   Document.Status status) {
        Page<Document> documentList =
                documentRepository.findAllByFirstSide_IdOrSecondSide_IdAndDocumentStatus(pageable, idCompany, idCompany, status);
        return documentList;
    }

    @Override
    @Transactional(readOnly = true)
    public Document findDocumentById(String id) {
        Optional<Document> doc = documentRepository.findById(id);
        return doc.orElseThrow(() -> new DocumentSystemException(DocumentSystemExceptionType.DOCUMENT_NOT_FOUND, id));
    }

    @Override
    public Document createDocument(DocumentForCreateDTO doc) {
        if (doc == null) {
            throw new DocumentSystemException(DocumentSystemExceptionType.DOCUMENT_CREATE_EXCEPTION);
        }
        Document document = new Document();
        document.setTitle(doc.getTitle());
        Company firstSide = companyService.findCompanyById(doc.getFirstSideId());
        Company secondSide = companyService.findCompanyById(doc.getSecondSideId());
        if (firstSide.equals(secondSide)) {
            throw new DocumentSystemException(
                    DocumentSystemExceptionType.DOC_SYSTEM_EXCEPTION,
                    "Компании не могут быть одинаковыми, id = " + firstSide.getId()
            );
        }
        checkMaxDocFlow(firstSide);
        checkMaxDocsInHour(firstSide);
        checkMaxDocFlow(secondSide);
        checkMaxCreatedDocsBetweenCompany(firstSide, secondSide);

        document.setFirstSide(firstSide);
        document.setSecondSide(secondSide);

        LocalDateTime dateTime = LocalDateTime.now();
        document.setCreateDate(dateTime);
        document.setLastEditDate(dateTime);

        document.setFirstEDS(true);
        document.setSecondEDS(false);
        document.setDocumentStatus(Document.Status.CREATED);
        return documentRepository.save(document);
    }

    private void checkMaxDocFlow(Company company) {
        if (company == null) {
            throw new DocumentSystemException(DocumentSystemExceptionType.COMPANY_NOT_FOUND);
        }
        int countDocFlow = documentRepository.findCountCompanyWorkflow(company);
        if (countDocFlow >= config.getMaxDocFlow()) {
            throw new DocumentSystemException(DocumentSystemExceptionType.COMPANY_MAX_DOC_FLOW, "id = " + company.getId());
        }
    }

    private void checkMaxDocsInHour(Company company) {
        if (company == null) {
            throw new DocumentSystemException(DocumentSystemExceptionType.COMPANY_NOT_FOUND);
        }
        LocalDateTime finishDateTime = LocalDateTime.now();
        int countCreateDocsForHour = documentRepository.findCountCreatedDocumentForHour(
                company,
                finishDateTime.minusHours(1),
                finishDateTime
        );
        if (countCreateDocsForHour >= config.getMaxDocsInHour()) {
            throw new DocumentSystemException(DocumentSystemExceptionType.COMPANY_MAX_DOC_IN_HOUR, "id = " + company.getId());
        }
    }

    private void checkMaxCreatedDocsBetweenCompany(Company c1, Company c2) {
        if (c1 == null || c2 == null) {
            throw new DocumentSystemException(DocumentSystemExceptionType.COMPANY_NOT_FOUND);
        }
        int maxDocsBetweenCompanies = documentRepository.findCountCreatedDocumentsBetweenCompanies(c1, c2);
        if (maxDocsBetweenCompanies >= config.getMaxCreatedDocsBetweenCompany()) {
            throw new DocumentSystemException(
                    DocumentSystemExceptionType.MAX_CREATED_DOCS_BETWEEN_COMPANIES,
                    new StringBuilder("id1 = ")
                            .append(c1.getId())
                            .append(", ")
                            .append("id2 = ")
                            .append(c2.getId())
                            .toString());
        }
    }

    @Override
    public Document editDocument(Document doc, String idEditorSide) {
        if (doc == null || doc.getId() == null) {
            throw new DocumentSystemException(DocumentSystemExceptionType.DOCUMENT_EDIT_EXCEPTION);
        }
        Document document = findDocumentById(doc.getId());
        isNotCompletedDocument(document);
        document.setTitle(doc.getTitle());
        Company editorCompany = companyService.findCompanyById(idEditorSide);
        if (!document.getSecondSide().equals(editorCompany)) {
            throw new DocumentSystemException(
                    DocumentSystemExceptionType.COMPANIES_NOT_EQUAL_EXCEPTION,
                    new StringBuilder("Компании не совпадают ")
                            .append(document.getSecondSide().getId())
                            .append(" ")
                            .append(idEditorSide).toString()
            );
        }
        Company oldFirstSide = companyService.findCompanyById(document.getFirstSide().getId());
        document.setSecondSide(oldFirstSide);
        document.setFirstSide(editorCompany);

        LocalDateTime dateTime = LocalDateTime.now();
        isCanEditOrSignDocument(dateTime);
        document.setLastEditDate(dateTime);

        document.setFirstEDS(true);
        document.setSecondEDS(false);
        return documentRepository.save(document);
    }

    private void isCanEditOrSignDocument(LocalDateTime dateTime) {
        if (dateTime.getHour() < config.getUpdateDocMinHour() || dateTime.getHour() > config.getUpdateDocMaxHour()) {
            throw new DocumentSystemException(
                    DocumentSystemExceptionType.DOC_SYSTEM_EDIT_OR_SIGN_DOC_EXCEPTION, dateTime.toString()
            );
        }
    }

    private void isNotCompletedDocument(Document document) {
        if (document.getDocumentStatus() == Document.Status.COMPLETED) {
            throw new DocumentSystemException(DocumentSystemExceptionType.DOCUMENT_IS_COMPLETE, document.getId());
        }
    }

    @Override
    public Document signDocument(Document doc, String idSignerSide) {
        if (doc == null || doc.getId() == null) {
            throw new DocumentSystemException(DocumentSystemExceptionType.DOCUMENT_EDIT_EXCEPTION, "");
        }
        Document document = findDocumentById(doc.getId());
        isNotCompletedDocument(document);
        Company singerCompany = companyService.findCompanyById(idSignerSide);
        if (!document.getSecondSide().equals(singerCompany)) {
            throw new DocumentSystemException(
                    DocumentSystemExceptionType.COMPANIES_NOT_EQUAL_EXCEPTION,
                    new StringBuilder("Компании не совпадают ")
                            .append(document.getSecondSide().getId())
                            .append(" ")
                            .append(idSignerSide).toString()
            );
        }

        LocalDateTime dateTime = LocalDateTime.now();
        isCanEditOrSignDocument(dateTime);
        document.setLastEditDate(dateTime);

        document.setSecondEDS(true);
        document.setDocumentStatus(Document.Status.COMPLETED);
        return documentRepository.save(document);
    }
}
