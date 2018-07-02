package ru.vitalyportret.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.vitalyportret.entity.Company;
import ru.vitalyportret.exeption.DocumentSystemException;
import ru.vitalyportret.exeption.DocumentSystemExceptionType;
import ru.vitalyportret.repository.DocumentRepository;
import ru.vitalyportret.service.ConfigurationService;

import java.time.LocalDateTime;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {

    private static final int DOC_SYSTEM_MAX_WORK_HOUR = 23;

    private static final int DOC_SYSTEM_MIN_WORK_HOUR = 0;

    @Value("${docsystem.max-work-hour}")
    private int updateDocMaxHour;

    @Value("${docsystem.min-work-hour}")
    private int updateDocMinHour;

    @Value("${docsystem.max-workflow}")
    private int maxDocFlow;

    @Value("${docsystem.max-docs-in-hour}")
    private int maxDocsInHour;

    @Value("${docsystem.max-workflow-between-company}")
    private int maxCreatedDocsBetweenCompany;

    private final DocumentRepository documentRepository;

    @Autowired
    public ConfigurationServiceImpl(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @Override
    public void setUpdateDocMaxHour(int updateDocMaxHour) {
        this.updateDocMaxHour = setUpdateDocHour(updateDocMaxHour);
    }

    private int setUpdateDocHour(int updateDocHour) {
        if (updateDocHour > DOC_SYSTEM_MAX_WORK_HOUR) {
            return DOC_SYSTEM_MAX_WORK_HOUR;
        } else if (updateDocHour < DOC_SYSTEM_MIN_WORK_HOUR) {
            return DOC_SYSTEM_MIN_WORK_HOUR;
        }
        return updateDocHour;
    }

    @Override
    public void setUpdateDocMinHour(int updateDocMinHour) {
        this.updateDocMinHour = setUpdateDocHour(updateDocMinHour);
    }

    @Override
    public void setMaxDocFlow(int maxDocFlow) {
        this.maxDocFlow = maxDocFlow;
    }

    @Override
    public void setMaxDocsInHour(int maxDocsInHour) {
        this.maxDocsInHour = maxDocsInHour;
    }

    @Override
    public void setMaxCreatedDocsBetweenCompany(int maxCreatedDocsBetweenCompany) {
        this.maxCreatedDocsBetweenCompany = maxCreatedDocsBetweenCompany;
    }

    @Override
    public void isCanEditOrSignDocument(LocalDateTime dateTime) {
        if (dateTime.getHour() < updateDocMinHour || dateTime.getHour() > updateDocMaxHour) {
            throw new DocumentSystemException(
                    DocumentSystemExceptionType.DOC_SYSTEM_EDIT_OR_SIGN_DOC_EXCEPTION, dateTime.toString()
            );
        }
    }

    @Override
    public void checkMaxDocFlow(Company company) {
        if (company == null) {
            throw new DocumentSystemException(DocumentSystemExceptionType.COMPANY_NOT_FOUND);
        }
        int countDocFlow = documentRepository.findCountCompanyWorkflow(company);
        if (countDocFlow >= maxDocFlow) {
            throw new DocumentSystemException(DocumentSystemExceptionType.COMPANY_MAX_DOC_FLOW, "id = " + company.getId());
        }
    }

    @Override
    public void checkMaxDocsInHour(Company company) {
        if (company == null) {
            throw new DocumentSystemException(DocumentSystemExceptionType.COMPANY_NOT_FOUND);
        }
        LocalDateTime finishDateTime = LocalDateTime.now();
        int countCreateDocsForHour = documentRepository.findCountCreatedDocumentForHour(
                company,
                finishDateTime.minusHours(1),
                finishDateTime
        );
        if (countCreateDocsForHour >= maxDocsInHour) {
            throw new DocumentSystemException(DocumentSystemExceptionType.COMPANY_MAX_DOC_IN_HOUR, "id = " + company.getId());
        }
    }

    @Override
    public void checkMaxCreatedDocsBetweenCompany(Company c1, Company c2) {
        if (c1 == null || c2 == null) {
            throw new DocumentSystemException(DocumentSystemExceptionType.COMPANY_NOT_FOUND);
        }
        int maxDocsBetweenCompanies = documentRepository.findCountCreatedDocumentsBetweenCompanies(c1, c2);
        if (maxDocsBetweenCompanies >= maxCreatedDocsBetweenCompany) {
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
}
