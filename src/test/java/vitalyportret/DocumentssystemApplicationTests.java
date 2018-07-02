package vitalyportret;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ru.vitalyportret.DTO.DocumentForCreateDTO;
import ru.vitalyportret.DocumentssystemApplication;
import ru.vitalyportret.entity.Company;
import ru.vitalyportret.entity.Document;
import ru.vitalyportret.exeption.DocumentSystemException;
import ru.vitalyportret.service.CompanyService;
import ru.vitalyportret.service.ConfigurationService;
import ru.vitalyportret.service.DocumentService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DocumentssystemApplication.class)
@Transactional
public class DocumentssystemApplicationTests {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ConfigurationService configurationService;

    Company company1 = new Company("Горгаз");
    Company company2 = new Company("Горводоканал");
    Company company3 = new Company("ДСУ");
    Company company4 = new Company("Сбер");
    Document doc1;
    Document doc2;
    Document doc3;
    Document doc4;

    @Before
    public void initialize() {
        company1 = companyService.save(company1);
        company2 = companyService.save(company2);
        company3 = companyService.save(company3);
        company4 = companyService.save(company4);

        DocumentForCreateDTO dto1 = new DocumentForCreateDTO("Выполнение услуг", company1.getId(), company2.getId());
        DocumentForCreateDTO dto2 = new DocumentForCreateDTO("Выполнение услуг 2", company1.getId(), company2.getId());
        DocumentForCreateDTO dto3 = new DocumentForCreateDTO("Выполнение услуг 3", company2.getId(), company3.getId());
        DocumentForCreateDTO dto4 = new DocumentForCreateDTO("Выполнение услуг 4", company3.getId(), company1.getId());

        doc1 = documentService.createDocument(dto1);
        doc2 = documentService.createDocument(dto2);
        doc3 = documentService.createDocument(dto3);
        doc4 = documentService.createDocument(dto4);

        configurationService.setUpdateDocMinHour(6);
        configurationService.setUpdateDocMaxHour(22);
    }

    @Test(expected = DocumentSystemException.class)
    public void createMoreMaxDocsBetweenCompany() {
        DocumentForCreateDTO dto = new DocumentForCreateDTO("Выполнение услуг Z", company1.getId(), company2.getId());
        documentService.createDocument(dto);
    }

    @Test(expected = DocumentSystemException.class)
    public void createMaxDocsInHour() {
        DocumentForCreateDTO dto = new DocumentForCreateDTO("MaxDocsInHour1", company1.getId(), company3.getId());
        DocumentForCreateDTO dto3 = new DocumentForCreateDTO("MaxDocsInHour4", company1.getId(), company3.getId());
        DocumentForCreateDTO dto1 = new DocumentForCreateDTO("MaxDocsInHour2", company1.getId(), company4.getId());
        DocumentForCreateDTO dto2 = new DocumentForCreateDTO("MaxDocsInHour3", company1.getId(), company4.getId());
        documentService.createDocument(dto);
        documentService.createDocument(dto1);
        documentService.createDocument(dto2);
        documentService.createDocument(dto3);
    }

    @Test
	public void editDocumentSuccess() {
        String docId = doc1.getId();
        Document doc = documentService.editDocument(doc1, company2.getId());
        Assert.assertEquals(docId, doc.getId());
        Assert.assertEquals(doc.getFirstSide(), company2);
        Assert.assertEquals(doc.getSecondSide(), company1);
	}

    @Test(expected = DocumentSystemException.class)
    public void editDocumentWithException() {
        documentService.editDocument(doc1, company3.getId());
    }

    @Test
    public void signDocumentSuccess() {
        Document doc = documentService.signDocument(doc1, company2.getId());
        Assert.assertEquals(doc.getDocumentStatus(), Document.Status.COMPLETED);
    }

    @Test(expected = DocumentSystemException.class)
    public void changeEditOrSignTime() {
        configurationService.setUpdateDocMinHour(19);
        configurationService.setUpdateDocMaxHour(22);
        documentService.signDocument(doc4, company1.getId());
    }


}
