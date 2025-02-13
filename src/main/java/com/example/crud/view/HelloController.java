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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Controlador principal de la aplicación.
 * Gestiona las interacciones con la interfaz gráfica y las operaciones con la base de datos.
 */
public class HelloController {

  private final ObservableList<Cuenta> listaCuentas = FXCollections.observableArrayList();

  private final BooleanProperty isDataLoaded = new SimpleBooleanProperty(false);

  @FXML
  public Button eliminar;
  @FXML
  public Button buscar;
  @FXML
  public Button editar;
  @FXML
  public Button mas;
  @FXML
  public Button cargar;
  @FXML
  public TableView<Cuenta> tabla;
  @FXML
  public TableColumn<Cuenta, Integer> tablaid;
  @FXML
  public TableColumn<Cuenta, Integer> tablasecuencia;
  @FXML
  public TableColumn<Cuenta, Boolean> tablaactivo;
  @FXML
  public TableColumn<Cuenta, LocalDate> tablacreardate;
  @FXML
  public TableColumn<Cuenta, String> tablaname;
  @FXML
  public TextField textobuscar;

  /**
   * Inicializa el controlador y configura las propiedades iniciales de los componentes.
   */

  public void initialize() {
    tablaid.setCellValueFactory(new PropertyValueFactory<>("id"));
    tablasecuencia.setCellValueFactory(new PropertyValueFactory<>("sequence"));
    tablaactivo.setCellValueFactory(new PropertyValueFactory<>("active"));
    tablacreardate.setCellValueFactory(new PropertyValueFactory<>("createDate"));
    tablaname.setCellValueFactory(new PropertyValueFactory<>("name"));

    tabla.setItems(listaCuentas);

    mas.visibleProperty().bind(isDataLoaded);
    eliminar.visibleProperty().bind(isDataLoaded);
    editar.visibleProperty().bind(isDataLoaded);
    buscar.visibleProperty().bind(isDataLoaded);
  }

  /**
   * Muestra la ventana para añadir un nuevo elemento.
   *
   * @param actionEvent Evento generado al hacer clic en el botón.
   * @throws IOException Si ocurre un error al cargar la vista.
   */

  @FXML
  public void botonmas(ActionEvent actionEvent) throws IOException {
    showModal("Anadir.fxml", "Nuevo elemento");
  }
  /**
   * Elimina el elemento seleccionado de la tabla y de la base de datos.
   *
   * @param actionEvent Evento generado al hacer clic en el botón.
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
  /**
   * Elimina una cuenta específica de la base de datos.
   *
   * @param cuenta La cuenta a eliminar.
   * @throws SQLException Si ocurre un error en la base de datos.
   */

  private void eliminarCuentaDeBaseDatos(Cuenta cuenta) throws SQLException {
    String sql = "DELETE FROM ir_ui_menu WHERE id = ?";
    try (Connection conexion = Conexion.conectar();
         PreparedStatement pst = conexion.prepareStatement(sql)) {
      pst.setInt(1, cuenta.getId());
      pst.executeUpdate();
    }
  }
  /**
   * Muestra una alerta con el título y mensaje especificados.
   *
   * @param title Título de la alerta.
   * @param message Mensaje de la alerta.
   */

  private void mostrarAlerta(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }
  /**
   * Busca un elemento en la base de datos según el ID ingresado.
   *
   * @param actionEvent Evento generado al hacer clic en el botón.
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
  /**
   * Busca una cuenta en la base de datos por su ID.
   *
   * @param id ID de la cuenta a buscar.
   * @return La cuenta encontrada, o null si no existe.
   * @throws SQLException Si ocurre un error en la base de datos.
   */

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
  /**
   * Carga todas las cuentas desde la base de datos en un hilo secundario.
   */

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
            System.out.println("Datos cargados: " + listaCuentas);
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
   * Abre la ventana de edición para el elemento seleccionado.
   *
   * @param actionEvent Evento generado al hacer clic en el botón.
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
   * Carga los datos de la base de datos y actualiza la tabla.
   *
   * @param actionEvent Evento generado al hacer clic en el botón.
   */

  @FXML
  public void botoncargar(ActionEvent actionEvent) {
    isDataLoaded.set(false);
    cargarTodasLasCuentas();
  }
  /**
   * Muestra un modal con la vista especificada.
   *
   * @param fxmlPath Ruta del archivo FXML a cargar.
   * @param title Título del modal.
   * @throws IOException Si ocurre un error al cargar el archivo FXML.
   */

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
