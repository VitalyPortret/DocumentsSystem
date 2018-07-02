package ru.vitalyportret.service;

import ru.vitalyportret.entity.Company;

import java.time.LocalDateTime;

public interface ConfigurationService {

    void setUpdateDocMaxHour(int updateDocMaxHour);

    void setUpdateDocMinHour(int updateDocMinHour);

    void setMaxDocFlow(int maxDocFlow);

    void setMaxDocsInHour(int maxDocsInHour);

    void setMaxCreatedDocsBetweenCompany(int maxCreatedDocsBetweenCompany);

    void isCanEditOrSignDocument(LocalDateTime dateTime);

    void checkMaxDocFlow(Company company);

    void checkMaxDocsInHour(Company company);

    void checkMaxCreatedDocsBetweenCompany(Company c1, Company c2);
}
