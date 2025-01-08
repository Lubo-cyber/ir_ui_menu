package com.example.crud.view;

import com.example.crud.model.Cuenta;
import com.example.crud.model.IrUiMenu;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Date;
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

    public void initialize()
    {
        choicetext.getItems().addAll("True", "False");
    }


    @FXML
    public void BotonAceptarAction(ActionEvent actionEvent)
    {
        Integer id = Integer.parseInt(idtext.getText());
        Integer sec = Integer.parseInt(secuenciatext.getText());
        Boolean choice = choicetext.getValue() != null && choicetext.getValue().equals("True");
        String name = nametext.getText();
        LocalDate fecha = datetext.getValue();


        IrUiMenu nuevoElemento = new IrUiMenu(id, sec, choice, fecha, name);
        try {
            nuevoElemento.meter();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ((Stage) idtext.getScene().getWindow()).close();
    }



    @FXML
    public void BotonCancelarAction(ActionEvent actionEvent)
    {
        ((Stage) idtext.getScene().getWindow()).close();
    }
}
