package com.example.crud.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import org.json.JSONObject;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * Clase que representa el modelo de la tabla "ir_ui_menu" en la base de datos.
 */
public class IrUiMenu {

  Map<String, Cuenta> cuentalist = new ConcurrentHashMap<>();

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
   * @param createDate Fecha de creación de la entrada en formato LocalDate.
   * @param name       Nombre asociado a la entrada.
   */
  public IrUiMenu(Integer id, Integer secuencia, Boolean active, java.sql.Date createDate, String name) {
    if (id == null || secuencia == null || active == null || createDate == null || name == null) {
      throw new IllegalArgumentException("Ningún campo puede ser nulo");
    }


    JSONObject jsonObject = new JSONObject();
    jsonObject.put("name", name);

    // En el constructor de IrUiMenu
    this.cuenta = new Cuenta(id, secuencia, active, createDate.toLocalDate(), jsonObject.toString());

  }

  /**
   * Método para insertar los datos en la tabla "ir_ui_menu".
   *
   * @throws SQLException si ocurre un error en la consulta SQL.
   */
  public void meter() throws SQLException {
    String sql = "INSERT INTO ir_ui_menu (id, sequence, active, create_date, name) "
        + "VALUES (?,?,?,?,CAST(? AS jsonb))"; // Convertimos el String a JSONB

    try (Connection conectar = Conexion.conectar();
        PreparedStatement pst = conectar.prepareStatement(sql)) {

      pst.setInt(1, cuenta.getId());
      pst.setInt(2, cuenta.getSequence());
      pst.setBoolean(3, cuenta.getActive());
      pst.setDate(4, java.sql.Date.valueOf(cuenta.getCreatedate())); // Conversión a SQL Date
      pst.setString(5, cuenta.getName());

      pst.executeUpdate();
    } catch (SQLException e) {
      System.err.println("Error al insertar en la base de datos: " + e.getMessage());
      throw e;
    }
  }

  /**
   * Método para obtener todas las cuentas.
   *
   * @return Colección de cuentas.
   */
  public Collection<Cuenta> getCuentas() {
    return cuentalist.values();
  }

  /**
   * Método auxiliar para crear un objeto IrUiMenu desde datos que contengan una fecha en java.sql.Date.
   *
   * @param id        Identificador.
   * @param secuencia Secuencia.
   * @param active    Estado activo o inactivo.
   * @param sqlDate   Fecha en formato java.sql.Date.
   * @param name      Nombre.
   * @return Un objeto IrUiMenu con la fecha convertida a LocalDate.
   */
  public static IrUiMenu crearDesdeSqlDate(Integer id, Integer secuencia, Boolean active, java.sql.Date sqlDate, String name) {

    return new IrUiMenu(id, secuencia, active, sqlDate, name);
  }
}
