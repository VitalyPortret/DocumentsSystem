package ru.vitalyportret.DTO;

import lombok.Data;
import ru.vitalyportret.entity.Company;

@Data
public class DocumentForCreateDTO {

    private String title;

    private String firstSideId;

    private String secondSideId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstSideId() {
        return firstSideId;
    }

    public void setFirstSideId(String firstSideId) {
        this.firstSideId = firstSideId;
    }

    public String getSecondSideId() {
        return secondSideId;
    }

    public void setSecondSideId(String secondSideId) {
        this.secondSideId = secondSideId;
    }

    public DocumentForCreateDTO(String title, String firstSideId, String secondSideId) {
        this.title = title;
        this.firstSideId = firstSideId;
        this.secondSideId = secondSideId;
    }
}
