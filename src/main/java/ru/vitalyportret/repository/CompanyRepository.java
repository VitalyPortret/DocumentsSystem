package ru.vitalyportret.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vitalyportret.entity.Company;

public interface CompanyRepository extends JpaRepository<Company, String> {
}
