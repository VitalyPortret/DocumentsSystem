package ru.vitalyportret.service.Impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.vitalyportret.service.ConfigurationService;

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
    public int getUpdateDocMaxHour() {
        return updateDocMaxHour;
    }

    @Override
    public int getUpdateDocMinHour() {
        return updateDocMinHour;
    }

    @Override
    public int getMaxDocFlow() {
        return maxDocFlow;
    }

    @Override
    public int getMaxDocsInHour() {
        return maxDocsInHour;
    }

    @Override
    public int getMaxCreatedDocsBetweenCompany() {
        return maxCreatedDocsBetweenCompany;
    }
}
