package ru.vitalyportret.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.vitalyportret.entity.Company;
import ru.vitalyportret.entity.Document;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public interface DocumentRepository extends JpaRepository<Document, String> {

    Page<Document> findAllByFirstSide_IdOrSecondSide_IdAndDocumentStatus(Pageable pageable,
                                                                         @NotNull String firstSide_id,
                                                                         @NotNull String secondSide_id,
                                                                         Document.Status documentStatus);

    @Query(value = "SELECT COUNT(d.id) FROM Document d WHERE (d.firstSide = ?1 OR d.secondSide = ?1) AND d.documentStatus = 'CREATED'")
    int findCountCompanyWorkflow(Company company);

    @Query(value = "SELECT COUNT(d.id) FROM Document d WHERE d.firstSide = ?1 AND d.createDate BETWEEN ?2 AND ?3 AND d.documentStatus = 'CREATED'")
    int findCountCreatedDocumentForHour(Company company, LocalDateTime startDate, LocalDateTime endDate);

    @Query(value = "SELECT COUNT(d.id) FROM Document d WHERE ((d.firstSide = :c1 AND d.secondSide = :c2) OR (d.firstSide = :c2 AND d.secondSide = :c1)) AND d.documentStatus = 'CREATED'")
    int findCountCreatedDocumentsBetweenCompanies(@Param("c1") Company firstSide,@Param("c2") Company secondSide);

}
