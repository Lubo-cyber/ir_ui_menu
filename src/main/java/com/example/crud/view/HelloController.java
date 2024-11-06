package com.example.crud.view;

import com.example.crud.HelloApplication;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.application.Platform;
import com.example.crud.model.Conexion;
import com.example.crud.model.Cuenta;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HelloController {

    @FXML
    private Button eliminar;
    @FXML
    private Button buscar;
    @FXML
    private Button editar;
    @FXML
    private TableView<Cuenta> tabla;
    @FXML
    private Button mas;


    private ObservableList<Cuenta> listaCuentas;
    @FXML
    private Button Cargar;
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

    private ObservableList<Cuenta> listaCuenta = FXCollections.observableArrayList();

    private BooleanProperty isDataLoaded = new SimpleBooleanProperty(false);
    public void initialize() {

        mas.visibleProperty().bind(isDataLoaded);
        eliminar.visibleProperty().bind(isDataLoaded);
        editar.visibleProperty().bind(isDataLoaded);
        buscar.visibleProperty().bind(isDataLoaded);
        tabla.setItems(listaCuenta);

    }


    @FXML
    public void botonmas(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Anadir.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 619, 583);
        Stage stage = new Stage();
        scene.getStylesheets().add(getClass().getResource("/com/example/crud/Estilo.css").toExternalForm());
        stage.setTitle("Nuevo elemento");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.show();

    }


    @FXML
    public void botoneliminar(ActionEvent actionEvent) {
        Cuenta cuentaSeleccionada = tabla.getSelectionModel().getSelectedItem();
        if (cuentaSeleccionada != null) {
            try {
                eliminarCuentaDeBaseDatos(cuentaSeleccionada);
                listaCuentas.remove(cuentaSeleccionada);
                mostrarAlerta("Elemento eliminado", "El elemento seleccionado ha sido eliminada exitosamente.");

            } catch (SQLException e) {
                e.printStackTrace();
                mostrarAlerta("Error", "No se pudo eliminar el elemento.");
            }
        } else {
            System.out.println("No hay elemento seleccionada para eliminar.");
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

    @FXML
    public void botonbuscar(ActionEvent actionEvent) {
        String idInput = textobuscar.getText().trim();
        System.out.println("Buscando ID: " + idInput);

        if (idInput.isEmpty()) {
            mostrarAlerta("Error", "Por favor ingresa un ID para buscar.");
            return;
        }

        try {
            int id = Integer.parseInt(idInput);
            Cuenta cuentaBuscada = buscarCuentaPorId(id);


            listaCuentas.clear();
            if (cuentaBuscada != null) {
                System.out.println("Cuenta encontrada: " + cuentaBuscada);
                listaCuentas.add(cuentaBuscada);
            } else {
                mostrarAlerta("No encontrado", "No se encontró ninguna cuenta con el ID especificado.");
            }


            tabla.setItems(listaCuentas); // Asegúrate de refrescar la tabla

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

                return new Cuenta(id, sequence, active, Date.valueOf(createDate), name);
            } else {
                return null;
            }
        }
    }


    private void cargarTodasLasCuentas() {
        tabla.setItems(listaCuentas);
    }

    @FXML
    public void botoneditar(ActionEvent actionEvent) {
        Cuenta cuentaSeleccionada = tabla.getSelectionModel().getSelectedItem();
        if (cuentaSeleccionada != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Editar.fxml"));
                Scene scene2 = new Scene(fxmlLoader.load(), 619, 583);
                Stage stage2 = new Stage();
                scene2.getStylesheets().add(getClass().getResource("/com/example/crud/Estilo.css").toExternalForm());
                stage2.setTitle("Editar elemento");
                stage2.setScene(scene2);
                stage2.initModality(Modality.APPLICATION_MODAL);
                stage2.setResizable(false);

                HelloController3 controller = fxmlLoader.getController();
                controller.setCuenta(cuentaSeleccionada);

                stage2.show();
            } catch (IOException e) {
                e.printStackTrace();
                mostrarAlerta("Error", "No se ha podido cargar la ventana de edición.");
            }
        } else {
            mostrarAlerta("Advertencia", "Por favor, selecciona una cuenta para editar.");
        }
    }


    @FXML
    public void BotonCargar(ActionEvent actionEvent) {
        isDataLoaded.set(false);  // Desactiva los botones mientras se cargan los datos
        Task<Void> cargarDatosTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                List<Cuenta> datos = new ArrayList<>();
                try (Connection conexion = Conexion.conectar();
                     Statement sentencia = conexion.createStatement();
                     ResultSet resultado = sentencia.executeQuery("SELECT ID, SEQUENCE, ACTIVE, CREATE_DATE, NAME FROM ir_ui_menu")) {

                    while (resultado.next()) {
                        int id = resultado.getInt("ID");
                        int sequence = resultado.getInt("SEQUENCE");
                        boolean active = resultado.getBoolean("ACTIVE");
                        LocalDate createDate = resultado.getDate("CREATE_DATE").toLocalDate();
                        String name = resultado.getString("NAME");

                        // Agregar cada cuenta a la lista de datos
                        datos.add(new Cuenta(id, sequence, active, Date.valueOf(createDate), name));
                    }

                    Platform.runLater(() -> {
                        listaCuentas = FXCollections.observableArrayList(datos);
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

        // Ejecutar la tarea de carga en un hilo separado para no bloquear la interfaz
        Thread hilo = new Thread(cargarDatosTask);
        hilo.start();
    }

}