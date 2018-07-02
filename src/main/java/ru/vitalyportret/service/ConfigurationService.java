package ru.vitalyportret.service;

public interface ConfigurationService {

    void setUpdateDocMaxHour(int updateDocMaxHour);

    void setUpdateDocMinHour(int updateDocMinHour);

    void setDocSystemMaxWorkflow(int docSystemMaxWorkflow);

    void setDocSystemMaxDocsInHour(int docSystemMaxDocsInHour);

    void setDocSystemMaxBetweenCompany(int docSystemMaxBetweenCompany);
}
