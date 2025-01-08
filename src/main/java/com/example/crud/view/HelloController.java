package com.example.crud.view;

import com.example.crud.HelloApplication;
import com.example.crud.model.Conexion;
import com.example.crud.model.Cuenta;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Controlador principal.
 */
public class HelloController {

  private final ObservableList<Cuenta> listaCuentas = FXCollections.observableArrayList();

  private final BooleanProperty isDataLoaded = new SimpleBooleanProperty(false);

  @FXML
  private Button eliminar;
  @FXML
  private Button buscar;
  @FXML
  private Button editar;
  @FXML
  private Button mas;
  @FXML
  private Button Cargar;
  @FXML
  private TableView<Cuenta> tabla;
  @FXML
  private TableColumn<Cuenta, Integer> tablaid;
  @FXML
  private TableColumn<Cuenta, Integer> tablasecuencia;
  @FXML
  private TableColumn<Cuenta, Boolean> tablaactivo;
  @FXML
  private TableColumn<Cuenta, LocalDate> tablacreardate;
  @FXML
  private TableColumn<Cuenta, String> tablaname;
  @FXML
  private TextArea textobuscar;
  @FXML
  private AnchorPane fondo;

  /**
   * Metodo Inicializar.
   */
  public void initialize() {
    mas.visibleProperty().bind(isDataLoaded);
    eliminar.visibleProperty().bind(isDataLoaded);
    editar.visibleProperty().bind(isDataLoaded);
    buscar.visibleProperty().bind(isDataLoaded);

    tabla.setItems(listaCuentas);
  }

  /**
   * El boton de añadir.
   */
  @FXML
  public void botonmas(ActionEvent actionEvent) throws IOException {
    showModal("Anadir.fxml", "Nuevo elemento");
  }
  /**
   * El boton de eliminar.
   */

  @FXML
  public void botoneliminar(ActionEvent actionEvent) {
    Cuenta cuentaSeleccionada = tabla.getSelectionModel().getSelectedItem();
    if (cuentaSeleccionada != null) {
      try {
        eliminarCuentaDeBaseDatos(cuentaSeleccionada);
        listaCuentas.remove(cuentaSeleccionada);
        tabla.refresh();
        mostrarAlerta("Elemento eliminado",
            "El elemento seleccionado ha sido eliminada exitosamente.");
      } catch (SQLException e) {
        e.printStackTrace();
        mostrarAlerta("Error", "No se pudo eliminar el elemento.");
      }
    } else {
      mostrarAlerta("Advertencia", "No hay elemento seleccionada para eliminar.");
    }
  }

  private void eliminarCuentaDeBaseDatos(Cuenta cuenta) throws SQLException {
    String sql = "DELETE FROM ir_ui_menu WHERE id = ?";
    try (Connection conexion = Conexion.conectar();
         PreparedStatement pst = conexion.prepareStatement(sql)) {
      pst.setInt(1, cuenta.getID());
      pst.executeUpdate();
    }
  }

  private void mostrarAlerta(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }
  /**
   * El boton de buscar.
   */

  @FXML
  public void botonbuscar(ActionEvent actionEvent) {
    String idInput = textobuscar.getText().trim();

    if (idInput.isEmpty()) {
      mostrarAlerta("Error", "Por favor ingresa un ID para buscar.");
      return;
    }

    try {
      int id = Integer.parseInt(idInput);
      Cuenta cuentaBuscada = buscarCuentaPorId(id);

      listaCuentas.clear();  // Limpiar la lista antes de agregar
      if (cuentaBuscada != null) {
        listaCuentas.add(cuentaBuscada);
      } else {
        mostrarAlerta("No encontrado", "No se encontró ninguna cuenta con el ID especificado.");
      }

      tabla.setItems(listaCuentas);

    } catch (NumberFormatException e) {
      mostrarAlerta("Error", "El ID ingresado no es válido.");
    } catch (SQLException e) {
      e.printStackTrace();
      mostrarAlerta("Error", "No se pudo acceder a la base de datos.");
    }
  }


  private Cuenta buscarCuentaPorId(int id) throws SQLException {
    String sql = "SELECT ID, SEQUENCE, ACTIVE, CREATE_DATE, NAME FROM ir_ui_menu WHERE ID = ?";
    try (Connection conexion = Conexion.conectar();
         PreparedStatement pst = conexion.prepareStatement(sql)) {
      pst.setInt(1, id);
      ResultSet resultado = pst.executeQuery();

      if (resultado.next()) {
        int sequence = resultado.getInt("SEQUENCE");
        boolean active = resultado.getBoolean("ACTIVE");
        LocalDate createDate = resultado.getDate("CREATE_DATE").toLocalDate();
        String name = resultado.getString("NAME");

        return new Cuenta(id, sequence, active, createDate, name);
      } else {
        return null;
      }
    }
  }

  private void cargarTodasLasCuentas() {
    Task<Void> cargarDatosTask = new Task<Void>() {
      @Override
      protected Void call() throws Exception {
        List<Cuenta> datos = new ArrayList<>();
        try (Connection conexion = Conexion.conectar();
             Statement sentencia = conexion.createStatement();
             ResultSet resultado = sentencia.executeQuery(
                 "SELECT ID, SEQUENCE, ACTIVE, CREATE_DATE, NAME FROM ir_ui_menu")) {

          while (resultado.next()) {
            int id = resultado.getInt("ID");
            int sequence = resultado.getInt("SEQUENCE");
            boolean active = resultado.getBoolean("ACTIVE");
            LocalDate createDate = resultado.getDate("CREATE_DATE").toLocalDate();
            String name = resultado.getString("NAME");
            datos.add(new Cuenta(id, sequence, active, createDate, name));
          }


          Platform.runLater(() -> {
            listaCuentas.setAll(datos);
            tabla.setItems(listaCuentas);
            isDataLoaded.set(true);
          });

        } catch (SQLException e) {
          e.printStackTrace();
          Platform.runLater(() -> mostrarAlerta("Error", "No se pudo cargar la base de datos."));
        }

        return null;
      }
    };

    Thread hilo = new Thread(cargarDatosTask);
    hilo.start();
  }
  /**
   * El boton de editar.
   */

  @FXML
  public void botoneditar(ActionEvent actionEvent) {
    Cuenta cuentaSeleccionada = tabla.getSelectionModel().getSelectedItem();
    if (cuentaSeleccionada != null) {
      try {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Editar.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 619, 583);
        Stage stage = new Stage();
        scene.getStylesheets()
            .add(getClass().getResource("/com/example/crud/Estilo.css").toExternalForm());
        stage.setTitle("Editar elemento");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);

        HelloController3 controller = fxmlLoader.getController();
        controller.setCuenta(cuentaSeleccionada);

        stage.show();
      } catch (IOException e) {
        e.printStackTrace();
        mostrarAlerta("Error", "No se ha podido cargar la ventana de edición.");
      }
    } else {
      mostrarAlerta("Advertencia", "Por favor, selecciona una cuenta para editar.");
    }
  }
  /**
   * El boton de cargar.
   */

  @FXML
  public void BotonCargar(ActionEvent actionEvent) {
    isDataLoaded.set(false);
    cargarTodasLasCuentas();
  }

  private void showModal(String fxmlPath, String title) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxmlPath));
    Scene scene = new Scene(fxmlLoader.load(), 619, 583);
    Stage stage = new Stage();
    scene.getStylesheets()
        .add(getClass().getResource("/com/example/crud/Estilo.css").toExternalForm());
    stage.setTitle(title);
    stage.setScene(scene);
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.setResizable(false);
    stage.show();
  }
}
