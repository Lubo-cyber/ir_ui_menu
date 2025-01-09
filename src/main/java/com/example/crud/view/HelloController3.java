package com.example.crud.view;

import com.example.crud.model.Conexion;
import com.example.crud.model.Cuenta;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controlador para la vista de edición de cuentas.
 */

public class HelloController3 {

  @FXML
  private TextField nametext2;
  @FXML
  private ChoiceBox<String> choicetext2;
  @FXML
  private DatePicker datetext2;
  @FXML
  private Button botonaceptar2;
  @FXML
  private TextField idtext2;
  @FXML
  private TextField secuenciatext2;
  @FXML
  private Button botoncancelar2;

  private Cuenta cuenta;

  /**
   * Inicializa el controlador y configura las opciones del {@link ChoiceBox}.
   */

  public void initialize() {
    choicetext2.getItems().addAll("True", "False");
  }
  /**
   * Establece los datos de la cuenta seleccionada para ser editada.
   *
   * @param cuenta La cuenta a editar.
   */

  public void setCuenta(Cuenta cuenta) {
    this.cuenta = cuenta;
    if (cuenta != null) {
      idtext2.setText(cuenta.getId().toString());
      secuenciatext2.setText(cuenta.getSequence().toString());
      choicetext2.setValue(cuenta.getActive() ? "True" : "False");
      datetext2.setValue(cuenta.getCreatedate());
      nametext2.setText(cuenta.getName());
    }
  }
  /**
   * Acción asociada al botón "Aceptar".
   * Actualiza los datos de la cuenta en la base de datos con los valores ingresados.
   *
   * @param actionEvent Evento generado al hacer clic en el botón.
   */

  @FXML
  public void botonaceptaraction(ActionEvent actionEvent) {
    if (cuenta != null) {
      try {
        Integer id = Integer.parseInt(idtext2.getText());
        Integer secuencia = Integer.parseInt(secuenciatext2.getText());
        Boolean activo = choicetext2.getValue().equals("True");
        String name = nametext2.getText();
        Date createDate = Date.valueOf(datetext2.getValue());

        int rowsAffected = actualizarCuenta(id, secuencia, activo, createDate, name);

        if (rowsAffected > 0) {
          System.out.println("Actualización exitosa.");
        } else {
          System.out.println("No se encontró el registro para actualizar.");
        }

        ((Stage) botonaceptar2.getScene().getWindow()).close();
      } catch (NumberFormatException e) {
        System.err.println("Error de formato de número: " + e.getMessage());
      } catch (SQLException e) {
        System.err.println("Error de SQL: " + e.getMessage());
      }
    }
  }
  /**
   * Actualiza los datos de una cuenta en la base de datos.
   *
   * @param id        ID de la cuenta.
   * @param sequence  Secuencia de la cuenta.
   * @param active    Estado activo de la cuenta.
   * @param createDate Fecha de creación de la cuenta.
   * @param name      Nombre de la cuenta.
   * @return Número de filas afectadas por la actualización.
   * @throws SQLException Si ocurre un error en la base de datos.
   */

  public int actualizarCuenta(Integer id, Integer sequence, Boolean active, Date createDate,
      String name) throws SQLException {
    String sql = "UPDATE ir_ui_menu SET SEQUENCE = ?, "
        + "ACTIVE = ?, CREATE_DATE = ?, NAME = ?::jsonb WHERE ID = ?";

    try (Connection conexion = Conexion.conectar();
        PreparedStatement pst = conexion.prepareStatement(sql)) {

      pst.setInt(1, sequence);
      pst.setBoolean(2, active);
      pst.setDate(3, createDate);

      String jsonString = "{\"name\": \"" + name + "\"}";
      pst.setObject(4, jsonString, java.sql.Types.OTHER);
      pst.setInt(5, id);

      return pst.executeUpdate();
    }
  }

  /**
   * Acción asociada al botón "Cancelar".
   * Cierra la ventana actual sin realizar ninguna acción adicional.
   *
   * @param actionEvent Evento generado al hacer clic en el botón.
   */

  @FXML
  public void botoncancelaraction(ActionEvent actionEvent) {
    ((Stage) botoncancelar2.getScene().getWindow()).close();
  }
}
