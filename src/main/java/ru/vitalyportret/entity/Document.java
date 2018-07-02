package ru.vitalyportret.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;


@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Document extends CommonEntity {

    public enum Status { CREATED, COMPLETED }

    @Size(max = 500)
    @NotNull
    private String title;

    private LocalDateTime createDate;

    private LocalDateTime lastEditDate;

    private boolean firstEDS;

    @JoinColumn(name = "id_first_side")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Company firstSide;

    private boolean secondEDS;

    @JoinColumn(name = "id_second_side")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Company secondSide;

    @Enumerated(value = EnumType.STRING)
    private Status documentStatus;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getLastEditDate() {
        return lastEditDate;
    }

    public void setLastEditDate(LocalDateTime lastEditDate) {
        this.lastEditDate = lastEditDate;
    }

    public boolean isFirstEDS() {
        return firstEDS;
    }

    public void setFirstEDS(boolean firstEDS) {
        this.firstEDS = firstEDS;
    }

    public Company getFirstSide() {
        return firstSide;
    }

    public void setFirstSide(Company firstSide) {
        this.firstSide = firstSide;
    }

    public boolean isSecondEDS() {
        return secondEDS;
    }

    public void setSecondEDS(boolean secondEDS) {
        this.secondEDS = secondEDS;
    }

    public Company getSecondSide() {
        return secondSide;
    }

    public void setSecondSide(Company secondSide) {
        this.secondSide = secondSide;
    }

    public Status getDocumentStatus() {
        return documentStatus;
    }

    public void setDocumentStatus(Status documentStatus) {
        this.documentStatus = documentStatus;
    }
}
