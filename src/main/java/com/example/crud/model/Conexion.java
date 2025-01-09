package com.example.crud.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase encargada de manejar la conexión a la base de datos PostgreSQL.
 */
public class Conexion {

  /**
   * Variable estática para almacenar la conexión a la base de datos.
   */
  public static Connection con = null;

  /**
   * Método que establece la conexión a la base de datos.
   * Si la conexión ya está activa, la retorna; de lo contrario, crea una nueva.
   *
   * @return un objeto {@link Connection} que representa la conexión a la base de datos,
   *         o {@code null} si ocurre un error durante la conexión.
   */
  public static Connection conectar() {
    String url = "jdbc:postgresql://localhost:5432/Usuario";
    String usuario = "odoo";
    String contrasena = "odoo";
    try {

      Class.forName("org.postgresql.Driver");


      con = DriverManager.getConnection(url, usuario, contrasena);

      if (con != null) {
        System.out.println("Conectado a la base de datos");
      } else {
        System.out.println("No se pudo conectar a la base de datos");
      }
    } catch (SQLException e) {

      System.err.println("Error de conexión SQL: " + e.getMessage());
    } catch (ClassNotFoundException e) {

      System.err.println("Controlador no encontrado: " + e.getMessage());
    }
    return con;
  }
}
