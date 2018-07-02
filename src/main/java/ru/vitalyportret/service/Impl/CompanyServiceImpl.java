package ru.vitalyportret.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vitalyportret.entity.Company;
import ru.vitalyportret.exeption.DocumentSystemException;
import ru.vitalyportret.exeption.DocumentSystemExceptionType;
import ru.vitalyportret.repository.CompanyRepository;
import ru.vitalyportret.service.CompanyService;

import java.util.Optional;

@Service
@Transactional
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Company findCompanyById(String id) {
        Optional<Company> company = companyRepository.findById(id);
        return company.orElseThrow(
                () -> new DocumentSystemException(DocumentSystemExceptionType.COMPANY_NOT_FOUND, id)
        );
    }

    public Company save(Company company) {
        return companyRepository.save(company);
    }
}
