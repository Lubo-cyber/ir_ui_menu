package com.example.crud.view;

import com.example.crud.model.Conexion;
import com.example.crud.model.Cuenta;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class HelloController3 {

    // Propiedades JavaFX
    @FXML
    private StringProperty nameText2Property = new SimpleStringProperty();
    @FXML
    private ObjectProperty<String> choiceText2Property = new SimpleObjectProperty<>();
    @FXML
    private ObjectProperty<LocalDate> dateText2Property = new SimpleObjectProperty<>();
    @FXML
    private StringProperty idText2Property = new SimpleStringProperty();
    @FXML
    private StringProperty secuenciaText2Property = new SimpleStringProperty();

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

    private Cuenta cuenta;

    public void initialize() {
        choicetext2.getItems().addAll("True", "False");

        idtext2.textProperty().bindBidirectional(idText2Property);
        secuenciatext2.textProperty().bindBidirectional(secuenciaText2Property);
        choicetext2.valueProperty().bindBidirectional(choiceText2Property);
        nametext2.textProperty().bindBidirectional(nameText2Property);
        datetext2.valueProperty().bindBidirectional(dateText2Property);

        BooleanBinding camposIncompletos = idText2Property.isEmpty()
                .or(secuenciaText2Property.isEmpty())
                .or(choiceText2Property.isNull())
                .or(nameText2Property.isEmpty())
                .or(dateText2Property.isNull());

        BotonAceptar2.disableProperty().bind(camposIncompletos);
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
        if (cuenta != null) {
            idText2Property.set(cuenta.getID().toString());
            secuenciaText2Property.set(cuenta.getSEQUENCE().toString());
            choiceText2Property.set(cuenta.getACTIVE() ? "True" : "False");
            dateText2Property.set(cuenta.getCREATE_DATE().toLocalDate());
            nameText2Property.set(cuenta.getNAME());
        }
    }

    @FXML
    public void BotonAceptarAction(ActionEvent actionEvent) {
        if (cuenta != null) {
            try {
                Integer id = Integer.parseInt(idText2Property.get());
                Integer secuencia = Integer.parseInt(secuenciaText2Property.get());
                Boolean activo = "True".equals(choiceText2Property.get());
                String name = nameText2Property.get();
                Date createDate = Date.valueOf(dateText2Property.get());

                int rowsAffected = actualizarCuenta(id, secuencia, activo, createDate, name);

                if (rowsAffected > 0) {
                    System.out.println("Actualización exitosa.");
                } else {
                    System.out.println("No se encontró el registro para actualizar.");
                }

                ((Stage) BotonAceptar2.getScene().getWindow()).close();
            } catch (NumberFormatException e) {
                System.err.println("Error de formato de número: " + e.getMessage());
            } catch (SQLException e) {
                System.err.println("Error de SQL: " + e.getMessage());
            }
        }
    }

    public int actualizarCuenta(Integer id, Integer sequence, Boolean active, Date createDate, String name) throws SQLException {
        String sql = "UPDATE ir_ui_menu SET SEQUENCE = ?, ACTIVE = ?, CREATE_DATE = ?, NAME = ?::jsonb WHERE ID = ?";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement pst = conexion.prepareStatement(sql)) {

            pst.setInt(1, sequence);
            pst.setBoolean(2, active);
            pst.setDate(3, createDate);

            // Convertir el nombre a un formato JSON
            String jsonString = "{\"name\": \"" + name + "\"}";
            pst.setObject(4, jsonString, java.sql.Types.OTHER);
            pst.setInt(5, id);

            return pst.executeUpdate();
        }
    }

    // Método que se ejecuta al hacer clic en el botón de Cancelar
    @FXML
    public void BotonCancelarAction(ActionEvent actionEvent) {
        ((Stage) BotonCancelar2.getScene().getWindow()).close();
    }
}
