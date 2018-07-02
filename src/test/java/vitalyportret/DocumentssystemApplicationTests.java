package vitalyportret;

import org.junit.Before;
import org.junit.BeforeClass;
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

//    @BeforeClass

    @Before
    public void initialize() {
        company1 = companyService.save(company1);
        company2 = companyService.save(company2);
    }

    @Test
	public void createDocument() {
        DocumentForCreateDTO dto1 = new DocumentForCreateDTO("Выполнение услуг", company1.getId(), company2.getId());
        Document doc1 = documentService.createDocument(dto1);
        Document doc2 = documentService.createDocument(dto1);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Document doc3 = documentService.createDocument(dto1);
        Document doc4 = documentService.createDocument(dto1);
        Document doc5 = documentService.createDocument(dto1);
        Document doc6 = documentService.createDocument(dto1);
	}



}
