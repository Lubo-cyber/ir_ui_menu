package com.example.crud.model;

import javafx.beans.property.*;
import java.sql.*;
import java.time.LocalDate;

public class Cuenta {

    private final IntegerProperty ID;
    private final IntegerProperty SEQUENCE;
    private final BooleanProperty ACTIVE;
    private final ObjectProperty<LocalDate> CREATE_DATE;
    private final StringProperty NAME;

    public Cuenta(Integer id, Integer sequence, Boolean active, LocalDate createDate, String name) {
        this.ID = new SimpleIntegerProperty(id);
        this.SEQUENCE = new SimpleIntegerProperty(sequence);
        this.ACTIVE = new SimpleBooleanProperty(active);
        this.CREATE_DATE = new SimpleObjectProperty<>(createDate);
        this.NAME = new SimpleStringProperty(name);
    }

    public Integer getID() {
        return ID.get();
    }

    public void setID(Integer ID) {
        this.ID.set(ID);
    }

    public IntegerProperty idProperty() {
        return ID;
    }

    public Integer getSEQUENCE() {
        return SEQUENCE.get();
    }

    public void setSEQUENCE(Integer SEQUENCE) {
        this.SEQUENCE.set(SEQUENCE);
    }

    public IntegerProperty sequenceProperty() {
        return SEQUENCE;
    }

    public Boolean getACTIVE() {
        return ACTIVE.get();
    }

    public void setACTIVE(Boolean ACTIVE) {
        this.ACTIVE.set(ACTIVE);
    }

    public BooleanProperty activeProperty() {
        return ACTIVE;
    }

    public LocalDate getCREATE_DATE() {
        return CREATE_DATE.get();
    }

    public void setCREATE_DATE(LocalDate CREATE_DATE) {
        this.CREATE_DATE.set(CREATE_DATE);
    }

    public ObjectProperty<LocalDate> createDateProperty() {
        return CREATE_DATE;
    }

    public String getNAME() {
        return NAME.get();
    }

    public void setNAME(String NAME) {
        this.NAME.set(NAME);
    }

    public StringProperty nameProperty() {
        return NAME;
    }
}


