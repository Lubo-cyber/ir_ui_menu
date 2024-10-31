package com.example.crud.view;

import com.example.crud.model.Conexion;
import com.example.crud.model.Cuenta;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class HelloController3 {
    @FXML
    private TextField nametext2;
    @FXML
    private ChoiceBox<String> choicetext2;
    @FXML
    private DatePicker datetext2;
    @FXML
    private Button BotonAceptar2;
    @FXML
    private TextField idtext2;
    @FXML
    private TextField secuenciatext2;
    @FXML
    private Button BotonCancelar2;

    private Cuenta cuenta; // Para almacenar la cuenta que se va a editar

    public void initialize() {
        choicetext2.getItems().addAll("True", "False");
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
        if (cuenta != null) {
            idtext2.setText(cuenta.getID().toString());
            secuenciatext2.setText(cuenta.getSEQUENCE().toString());
            choicetext2.setValue(cuenta.getACTIVE() ? "True" : "False");
            datetext2.setValue(cuenta.getCREATE_DATE().toLocalDate());
            nametext2.setText(cuenta.getNAME());
        }
    }

    @FXML
    public void BotonAceptarAction(ActionEvent actionEvent)
    {
        if (cuenta != null) {
            try {
                Integer id = Integer.parseInt(idtext2.getText());
                Integer secuencia = Integer.parseInt(secuenciatext2.getText());
                Boolean activo = choicetext2.getValue() != null && choicetext2.getValue().equals("True");
                String name = nametext2.getText();
                java.sql.Date createDate = java.sql.Date.valueOf(datetext2.getValue());

                actualizarCuenta(id, secuencia, activo, createDate, name);

                ((Stage) BotonAceptar2.getScene().getWindow()).close();
            } catch (NumberFormatException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void actualizarCuenta(Integer id, Integer secuencia, Boolean activo, java.sql.Date createDate, String name) throws SQLException {
        String sql = "UPDATE ir_ui_menu SET sequence = ?, active = ?, create_date = ?, name = ? WHERE id = ?";
        try (Connection connection = Conexion.conectar();
             PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, secuencia);
            pst.setBoolean(2, activo);
            pst.setDate(3, createDate);
            pst.setString(4, name);
            pst.setInt(5, id);
            pst.executeUpdate();
        }
    }

    @FXML
    public void BotonCancelarAction(ActionEvent actionEvent) {
        ((Stage) BotonCancelar2.getScene().getWindow()).close();
    }
}
