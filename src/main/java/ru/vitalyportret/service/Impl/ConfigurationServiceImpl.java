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
    private int docSystemMaxWorkflow;

    @Value("${docsystem.max-docs-in-hour}")
    private int docSystemMaxDocsInHour;

    @Value("${docsystem.max-workflow-between-company}")
    private int docSystemMaxBetweenCompany;

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
    public void setDocSystemMaxWorkflow(int docSystemMaxWorkflow) {
        this.docSystemMaxWorkflow = docSystemMaxWorkflow;
    }

    @Override
    public void setDocSystemMaxDocsInHour(int docSystemMaxDocsInHour) {
        this.docSystemMaxDocsInHour = docSystemMaxDocsInHour;
    }

    @Override
    public void setDocSystemMaxBetweenCompany(int docSystemMaxBetweenCompany) {
        this.docSystemMaxBetweenCompany = docSystemMaxBetweenCompany;
    }
}
