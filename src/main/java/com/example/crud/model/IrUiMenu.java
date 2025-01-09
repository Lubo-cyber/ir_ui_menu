package com.example.crud.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import org.json.JSONObject;

/**
 * Clase que representa el modelo de la tabla "ir_ui_menu" en la base de datos.
 */
public class IrUiMenu {

  /**
   * Objeto de tipo {@link Cuenta} que encapsula los datos relacionados con la cuenta.
   */
  private final Cuenta cuenta;

  /**
   * Constructor para inicializar los elementos de la tabla "ir_ui_menu".
   *
   * @param id         Identificador único de la entrada.
   * @param secuencia  Secuencia o número asociado a la entrada.
   * @param active     Estado de actividad de la entrada.
   * @param createDate Fecha de creación de la entrada.
   * @param name       Nombre asociado a la entrada.
   */
  public IrUiMenu(int id, int secuencia, boolean active, LocalDate createDate, String name) {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("name", name);

    this.cuenta = new Cuenta(id, secuencia, active, createDate, jsonObject.toString());
  }

  /**
   * Método para insertar los datos proporcionados por el usuario en la tabla "ir_ui_menu".
   *
   * @throws SQLException si ocurre un error al ejecutar la consulta SQL.
   */
  public void meter() throws SQLException {
    String sql = "INSERT INTO ir_ui_menu (id, sequence, active, create_date, name) "
        + "VALUES (?,?,?,?,?)";
    try (Connection conectar = Conexion.conectar();
        PreparedStatement pst = conectar.prepareStatement(sql)) {

      pst.setInt(1, cuenta.getId());
      pst.setInt(2, cuenta.getSequence());
      pst.setBoolean(3, cuenta.getActive());
      pst.setDate(4, java.sql.Date.valueOf(cuenta.getCreatedate()));
      pst.setString(5, cuenta.getName());


      pst.executeUpdate();
    } catch (SQLException e) {

      System.out.println(e);
    }
  }
}

