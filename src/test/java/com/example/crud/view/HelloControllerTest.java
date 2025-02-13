package com.example.crud.view;

import static java.util.Locale.lookup;
import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.util.NodeQueryUtils.hasText;

import com.example.crud.HelloApplication;
import com.example.crud.model.Cuenta;
import java.io.IOException;
import java.time.LocalDate;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.swing.text.TabableView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.base.WindowMatchers;
import org.testfx.util.WaitForAsyncUtils;

@TestMethodOrder(OrderAnnotation.class)
@ExtendWith(ApplicationExtension.class)
class HelloControllerTest {

  @Start
  public void start(Stage stage) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/crud/lubi2.fxml"));
    Scene scene = new Scene(fxmlLoader.load(), 790, 527);
    scene.getStylesheets()
        .add(getClass().getResource("/com/example/crud/Estilo.css").toExternalForm());
    stage.setTitle("ventana");
    stage.setScene(scene);
    stage.show();
  }

  @Test
  @Order(1)
  void testcargar(FxRobot robot) {
    robot.clickOn("#cargar");
    robot.sleep(3000);
    FxAssert.verifyThat("#tabla", table -> ((TableView<Cuenta>) table).getItems().size() > 0);
  }

  private void selectDateFromPicker(FxRobot robot, String datePickerId, LocalDate date) {
    try {
      robot.clickOn(datePickerId + " .arrow-button");

      WaitForAsyncUtils.waitForFxEvents();

      DatePicker datePicker = robot.lookup(datePickerId).queryAs(DatePicker.class);

      robot.interact(() -> datePicker.setValue(date));

      WaitForAsyncUtils.waitForFxEvents();

      robot.clickOn(datePickerId);

    } catch (Exception e) {
      System.err.println("Error al seleccionar la fecha: " + e.getMessage());
      e.printStackTrace();
    }
  }

  @Test
  @Order(2)
  void testaniadir(FxRobot robot) {
    robot.clickOn("#cargar");
    robot.sleep(1000);
    robot.clickOn("#mas");
    robot.sleep(1000);

    FxAssert.verifyThat(robot.window("Nuevo elemento"), WindowMatchers.isShowing());
    robot.targetWindow("Nuevo elemento");

    robot.clickOn("#idtext");
    robot.write("101");
    robot.clickOn("#secuenciatext");
    robot.write("28");

    selectDateFromPicker(robot, "#datetext", LocalDate.of(2023, 1, 1));
    ChoiceBox<String> choiceBox = robot.lookup("#choicetext").queryAs(ChoiceBox.class);
    assertNotNull(choiceBox, "El ChoiceBox no se encontró en la escena.");
    assertFalse(choiceBox.getItems().isEmpty(), "El ChoiceBox no tiene elementos.");
    if (choiceBox.getItems().contains("False")) {
      robot.clickOn("#choicetext");
      robot.clickOn("False");
    } else {
      fail("La opción 'False' no está disponible en el ChoiceBox.");
    }

    robot.clickOn("#nametext");
    robot.write("holacomoestas");
    robot.clickOn("#botonaceptar");
  }

  @Test
  @Order(3)
  void testbuscar(FxRobot robot) {
    robot.clickOn("#cargar");
    robot.sleep(1000);
    robot.clickOn("#textobuscar");
    robot.write("500");
    robot.clickOn("#buscar");
    robot.sleep(1000);
    FxAssert.verifyThat("#tabla", table -> ((TableView<Cuenta>) table).getItems().size() > 0);
  }

  @Test
  @Order(4)
  void testeditar(FxRobot robot) {
    robot.clickOn("#textobuscar");
    robot.write("500");
    robot.clickOn("#buscar");
    robot.clickOn("#tabla"); // Hace clic en la tabla para darle foco

    TableView<Cuenta> tableView = robot.lookup("#tabla").queryAs(TableView.class);

    if (!tableView.getItems().isEmpty()) {
      robot.clickOn(tableView.getColumns().get(0).getCellData(0).toString()); // Hacemos clic en el primer valor de la primera columna
    }

    Cuenta cuentaSeleccionada = tableView.getSelectionModel().getSelectedItem();
    assertNotNull(cuentaSeleccionada, "No se seleccionó ninguna cuenta.");
    System.out.println("Cuenta seleccionada: " + cuentaSeleccionada.getName());

    robot.clickOn("#editar");
    robot.sleep(1000);
    robot.clickOn("#secuenciatext2");
    robot.write("30");
    robot.clickOn("#nametext2");
    robot.write("textoeditado");
  }
  @Test
  @Order(5)
  void testeliminar(FxRobot robot) {
    robot.clickOn("#textobuscar");
    robot.write("500");
    robot.clickOn("#buscar");
    robot.clickOn("#tabla");
    TableView<Cuenta> tableView = robot.lookup("#tabla").queryAs(TableView.class);
    if (!tableView.getItems().isEmpty()) {
      robot.clickOn(tableView.getColumns().get(0).getCellData(0).toString());
    }
    Cuenta cuentaSeleccionada = tableView.getSelectionModel().getSelectedItem();
    assertNotNull(cuentaSeleccionada, "No se seleccionó ninguna cuenta.");
    System.out.println("Cuenta seleccionada: " + cuentaSeleccionada.getName());

    robot.clickOn("#eliminar");
  }
}