package com.example.crud.view;

import com.example.crud.model.IrUiMenu;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;

public class HelloController2 {

    @FXML
    private DatePicker datetext;
    @FXML
    private TextField secuenciatext;
    @FXML
    private ChoiceBox<String> choicetext;
    @FXML
    private TextField idtext;
    @FXML
    private Button BotonAceptar;
    @FXML
    private Button BotonCancelar;
    @FXML
    private TextField nametext;

    private final StringProperty idTextProperty = new SimpleStringProperty();
    private final StringProperty secuenciaTextProperty = new SimpleStringProperty();
    private final ObjectProperty<String> choiceTextProperty = new SimpleObjectProperty<>();
    private final StringProperty nameTextProperty = new SimpleStringProperty();
    private final ObjectProperty<LocalDate> dateTextProperty = new SimpleObjectProperty<>();

    public void initialize() {
        choicetext.getItems().addAll("True", "False");

        BooleanBinding camposIncompletos = idTextProperty.isEmpty()
                .or(secuenciaTextProperty.isEmpty())
                .or(choiceTextProperty.isNull())
                .or(nameTextProperty.isEmpty())
                .or(dateTextProperty.isNull());

        BotonAceptar.disableProperty().bind(camposIncompletos);

        idtext.textProperty().bindBidirectional(idTextProperty);
        secuenciatext.textProperty().bindBidirectional(secuenciaTextProperty);
        choicetext.valueProperty().bindBidirectional(choiceTextProperty);
        nametext.textProperty().bindBidirectional(nameTextProperty);
        datetext.valueProperty().bindBidirectional(dateTextProperty);
    }

    @FXML
    public void BotonAceptarAction(ActionEvent actionEvent) {
        Integer id = Integer.parseInt(idTextProperty.get());
        Integer sec = Integer.parseInt(secuenciaTextProperty.get());
        Boolean choice = "True".equals(choiceTextProperty.get());
        String name = nameTextProperty.get();
        java.sql.Date fecha = java.sql.Date.valueOf(dateTextProperty.get());

        IrUiMenu nuevoElemento = new IrUiMenu(id, sec, choice, fecha, name);
        try {
            nuevoElemento.meter();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ((Stage) idtext.getScene().getWindow()).close();
    }

    @FXML
    public void BotonCancelarAction(ActionEvent actionEvent) {
        ((Stage) idtext.getScene().getWindow()).close();
    }
}
