package ru.vitalyportret.service;

public interface ConfigurationService {

    void setUpdateDocMaxHour(int updateDocMaxHour);

    void setUpdateDocMinHour(int updateDocMinHour);

    void setMaxDocFlow(int maxDocFlow);

    void setMaxDocsInHour(int maxDocsInHour);

    void setMaxCreatedDocsBetweenCompany(int maxCreatedDocsBetweenCompany);

    int getUpdateDocMaxHour();

    int getUpdateDocMinHour();

    int getMaxDocFlow();

    int getMaxDocsInHour();

    int getMaxCreatedDocsBetweenCompany();
}
