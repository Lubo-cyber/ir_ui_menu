package com.example.crud.view;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasProperty;
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
import org.testfx.matcher.control.TableViewMatchers;
import org.testfx.matcher.control.LabeledMatchers;
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
    TableView<Cuenta> tableView = robot.lookup("#tabla").queryAs(TableView.class);
    FxAssert.verifyThat("#tabla", TableViewMatchers.hasNumRows(0));


    robot.clickOn("#cargar");
    robot.sleep(2000);
    WaitForAsyncUtils.waitForFxEvents();
    FxAssert.verifyThat("#tabla", TableViewMatchers.hasNumRows(383));
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

    TableView<Cuenta> tableView = robot.lookup("#tabla").queryAs(TableView.class);
    int filasAntes = tableView.getItems().size();

    robot.clickOn("#mas");
    robot.sleep(1000);

    FxAssert.verifyThat(robot.window("Nuevo elemento"), WindowMatchers.isShowing());

    robot.clickOn("#idtext");
    robot.write("101");
    FxAssert.verifyThat("#idtext", hasText("101"));

    robot.clickOn("#secuenciatext");
    robot.write("28");
    FxAssert.verifyThat("#secuenciatext", hasText("28"));

    selectDateFromPicker(robot, "#datetext", LocalDate.of(2023, 1, 1));

    ChoiceBox<String> choiceBox = robot.lookup("#choicetext").queryAs(ChoiceBox.class);
    assertNotNull(choiceBox, "El ChoiceBox no se encontró en la escena.");
    assertFalse(choiceBox.getItems().isEmpty(), "El ChoiceBox no tiene elementos.");

    if (choiceBox.getItems().contains("False")) {
      robot.clickOn("#choicetext");
      robot.clickOn("False");

      FxAssert.verifyThat("#choicetext", node -> {
        ChoiceBox<String> cb = (ChoiceBox<String>) node;
        return "False".equals(cb.getValue());
      });
    } else {
      fail("La opción 'False' no está disponible en el ChoiceBox.");
    }

    robot.clickOn("#nametext");
    robot.write("holacomoestas");
    FxAssert.verifyThat("#nametext", hasText("holacomoestas"));

    robot.clickOn("#botonaceptar");

    WaitForAsyncUtils.waitForFxEvents();
    robot.sleep(1000);

    robot.clickOn("#cargar");
    robot.sleep(1000);

    FxAssert.verifyThat("#tabla",
        table -> ((TableView<Cuenta>) table).getItems().size() == filasAntes + 1);
  }


  @Test
  @Order(3)
  void testbuscar(FxRobot robot) {
    robot.clickOn("#cargar");
    robot.sleep(1000);
    robot.clickOn("#textobuscar");
    robot.write("500");
    FxAssert.verifyThat("#textobuscar", hasText("500"));

    robot.clickOn("#buscar");
    robot.sleep(1000);

    FxAssert.verifyThat("#tabla", TableViewMatchers.hasNumRows(1));
  }

  @Test
  @Order(4)
  void testeditar(FxRobot robot) {
    robot.clickOn("#cargar");
    robot.clickOn("#tabla");

    TableView<Cuenta> tableView = robot.lookup("#tabla").queryAs(TableView.class);
    assertFalse(tableView.getItems().isEmpty(), "La tabla está vacía.");

    robot.interact(() -> tableView.getSelectionModel().select(0));

    Cuenta cuentaOriginal = tableView.getSelectionModel().getSelectedItem();
    assertNotNull(cuentaOriginal, "No se seleccionó ninguna cuenta.");

    robot.clickOn("#editar");
    robot.sleep(1000);

    robot.clickOn("#secuenciatext2");
    robot.eraseText(10);
    robot.write("130");
    FxAssert.verifyThat("#secuenciatext2", hasText("130"));

    robot.clickOn("#nametext2");
    robot.eraseText(30);
    robot.clickOn("#nametext2");
    robot.eraseText(30);
    robot.clickOn("#nametext2");
    robot.eraseText(30);
    robot.write("holacomoestas");
    FxAssert.verifyThat("#nametext2", hasText("holacomoestas"));
    robot.clickOn("#botonaceptar2");
    robot.sleep(1000);
    robot.interact(tableView::refresh);
  }


  @Test
  @Order(8)
  void testeliminar(FxRobot robot) {
    robot.clickOn("#cargar");
    robot.sleep(2000);
    TableView<Cuenta> tableView = robot.lookup("#tabla").queryAs(TableView.class);
    assertFalse(tableView.getItems().isEmpty(), "La tabla está vacía.");

    int filasAntes = tableView.getItems().size();

    robot.clickOn(tableView.getColumns().get(0).getCellData(0).toString());

    Cuenta cuentaSeleccionada = tableView.getSelectionModel().getSelectedItem();
    assertNotNull(cuentaSeleccionada, "No se seleccionó ninguna cuenta.");

    System.out.println("Cuenta seleccionada: " + cuentaSeleccionada.getName());

    robot.clickOn("#eliminar");

    WaitForAsyncUtils.waitForFxEvents();

    FxAssert.verifyThat("#tabla", TableViewMatchers.hasNumRows(filasAntes - 1));
  }
  @Test
  @Order(5)
  void testCancelarAniadir(FxRobot robot) {
    robot.clickOn("#cargar");
    robot.sleep(1000);

    TableView<Cuenta> tableView = robot.lookup("#tabla").queryAs(TableView.class);
    int filasAntes = tableView.getItems().size();  // Guardar número de filas antes

    robot.clickOn("#mas");
    robot.sleep(1000);

    FxAssert.verifyThat(robot.window("Nuevo elemento"), WindowMatchers.isShowing());

    robot.clickOn("#botoncancelar");

    WaitForAsyncUtils.waitForFxEvents();

    FxAssert.verifyThat("#tabla", TableViewMatchers.hasNumRows(filasAntes));
  }
  @Test
  @Order(6)
  void testCancelarEditar(FxRobot robot) {
    robot.clickOn("#cargar");
    robot.clickOn("#tabla");

    TableView<Cuenta> tableView = robot.lookup("#tabla").queryAs(TableView.class);
    assertFalse(tableView.getItems().isEmpty(), "La tabla está vacía.");

    robot.clickOn(tableView.getColumns().get(0).getCellData(0).toString());

    Cuenta cuentaSeleccionada = tableView.getSelectionModel().getSelectedItem();
    assertNotNull(cuentaSeleccionada, "No se seleccionó ninguna cuenta.");

    System.out.println("Cuenta seleccionada: " + cuentaSeleccionada.getName());

    robot.clickOn("#editar");

    robot.sleep(1000);

    robot.clickOn("#secuenciatext2");
    robot.write("30");

    robot.clickOn("#nametext2");
    robot.eraseText(30);
    robot.write("textoeditado");

    robot.clickOn("#botoncancelar2");

    Cuenta cuentaDespues = tableView.getSelectionModel().getSelectedItem();
    assertEquals(cuentaSeleccionada.getSequence(), cuentaDespues.getSequence(), "Los datos cambiaron a pesar de cancelar.");
  }

  @Test
  @Order(7)
  void testBuscarSinResultados(FxRobot robot) {
    robot.clickOn("#cargar");
    robot.sleep(1000);

    robot.clickOn("#textobuscar");
    robot.write("999999");
    robot.clickOn("#buscar");
    robot.sleep(1000);

    if (robot.lookup(".alert").tryQuery().isPresent()) {
      robot.clickOn("Aceptar");
    }

    FxAssert.verifyThat("#tabla", TableViewMatchers.hasNumRows(0));
  }

}
