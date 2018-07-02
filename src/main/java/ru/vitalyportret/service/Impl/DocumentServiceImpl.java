package ru.vitalyportret.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import ru.vitalyportret.service.DocumentService;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class DocumentServiceImpl implements DocumentService {

    @Value("${docsystem.max-work-hour}")
    private int docSystemMaxWorkHour;

    @Value("${docsystem.min-work-hour}")
    private int docSystemMinWorkHour;

    @Value("${docsystem.max-workflow}")
    private int maxDocFlow;

    @Value("${docsystem.max-docs-in-hour}")
    private int maxDocsInHour;

    @Value("${docsystem.max-workflow-between-company}")
    private int docSystemMaxBetweenCompany;

    private final DocumentRepository documentRepository;

    private final CompanyService companyService;

    @Autowired
    public DocumentServiceImpl(DocumentRepository documentRepository,
                               CompanyService companyService) {
        this.documentRepository = documentRepository;
        this.companyService = companyService;
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
            throw new DocumentSystemException(DocumentSystemExceptionType.DOCUMENT_CREATE_ERROR, "");
        }
        Document document = new Document();
        document.setTitle(doc.getTitle());
        Company firstSide = companyService.findCompanyById(doc.getFirstSideId());
        docSystemMaxWorkflow(firstSide);
        docSystemMaxDocsInHour(firstSide);

        Company secondSide = companyService.findCompanyById(doc.getSecondSideId());
        docSystemMaxWorkflow(secondSide);
        if (firstSide.equals(secondSide)) {

        }
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

    @Override
    public Document editDocument(Document doc, String idEditorSide) {
        if (doc == null || doc.getId() == null) {
            throw new DocumentSystemException(DocumentSystemExceptionType.DOCUMENT_EDIT_ERROR, "");
        }
        Document document = findDocumentById(doc.getId());
        isCompleteDocument(document);
        document.setTitle(doc.getTitle());
        Company editorCompany = companyService.findCompanyById(idEditorSide);
        if (!document.getSecondSide().equals(editorCompany)) {

        }
        Company lastFirstSide = companyService.findCompanyById(document.getFirstSide().getId());
        if (!document.getFirstSide().equals(lastFirstSide)) {

        }
        document.setSecondSide(lastFirstSide);
        document.setFirstSide(editorCompany);

        LocalDateTime dateTime = LocalDateTime.now();
        isCanEditOrSignDocument(dateTime);
        document.setLastEditDate(dateTime);

        document.setFirstEDS(true);
        document.setSecondEDS(false);
        return documentRepository.save(document);
    }

    private void isCompleteDocument(Document document) {
        if (document.getDocumentStatus() == Document.Status.COMPLETED) {
            throw new DocumentSystemException(DocumentSystemExceptionType.DOCUMENT_IS_COMPLETE, document.getId());
        }
    }

    @Override
    public Document signDocument(Document doc, String idSignerSide) {
        if (doc == null || doc.getId() == null) {
            throw new DocumentSystemException(DocumentSystemExceptionType.DOCUMENT_EDIT_ERROR, "");
        }
        Document document = findDocumentById(doc.getId());
        isCompleteDocument(document);
        if (!document.isFirstEDS()) {

        }
        Company singerCompany = companyService.findCompanyById(idSignerSide);
        if (!document.getSecondSide().equals(singerCompany)) {

        }

        LocalDateTime dateTime = LocalDateTime.now();
        isCanEditOrSignDocument(dateTime);
        document.setLastEditDate(dateTime);

        document.setSecondEDS(true);
        document.setDocumentStatus(Document.Status.COMPLETED);
        return documentRepository.save(document);
    }

    private void isCanEditOrSignDocument(LocalDateTime now) {
        if (now.getHour() < docSystemMinWorkHour || now.getHour() > docSystemMaxWorkHour) {
            throw new DocumentSystemException(
                    DocumentSystemExceptionType.SYSTEM_EDIT_OR_SIGN_DOC_EXCEPTION, now.toString()
            );
        }
    }

    private void docSystemMaxWorkflow(Company company) {
        if (company == null) {
            throw new DocumentSystemException(DocumentSystemExceptionType.COMPANY_MAX_DOC_FLOW, "");
        }
        int count = documentRepository.findCountCompanyWorkflow(company);
        if (count >= maxDocFlow) {
            throw new DocumentSystemException(DocumentSystemExceptionType.COMPANY_MAX_DOC_FLOW, "");
        }
    }

    private void docSystemMaxDocsInHour(Company company) {
        if (company == null) {
            throw new DocumentSystemException(DocumentSystemExceptionType.COMPANY_MAX_DOC_FLOW, "");
        }
        LocalDateTime finishDateTime = LocalDateTime.now();
        int countDocPerHour = documentRepository
                .findCountCreatedDocumentForHour(company, finishDateTime.minusHours(1), finishDateTime);
        if (countDocPerHour > maxDocsInHour) {
            throw new DocumentSystemException(DocumentSystemExceptionType.COMPANY_MAX_DOC_FLOW, "");
        }
    }

    private void docSystemMaxBetweenCompany(Company company1, Company company2) {
        if (company1 == null || company2 == null) {
            throw new DocumentSystemException(DocumentSystemExceptionType.COMPANY_MAX_DOC_FLOW, "");
        }
        int maxDocsBetweenCompanies = documentRepository
                .findCountCreatedDocumentsBetweenCompanies(company1, company2);
        if (maxDocsBetweenCompanies > docSystemMaxBetweenCompany) {
            throw new DocumentSystemException(DocumentSystemExceptionType.COMPANY_MAX_DOC_FLOW, "");
        }
    }
}
