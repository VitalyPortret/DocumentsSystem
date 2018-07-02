package ru.vitalyportret.service;

import ru.vitalyportret.entity.Company;

public interface CompanyService {

    Company findCompanyById(String id);

    Company save(Company company);
}
