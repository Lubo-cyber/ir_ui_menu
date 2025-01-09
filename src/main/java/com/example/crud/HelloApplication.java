package com.example.crud;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Clase principal de la aplicación que arranca la interfaz gráfica.
 * Inicia la aplicación JavaFX cargando una vista FXML y aplicando un estilo CSS.
 */

public class HelloApplication extends Application {

  /**
   * Método principal que inicia la aplicación.
   * Llama al método launch() de la clase Application para iniciar el ciclo de vida de JavaFX.
   *
   * @param args Argumentos pasados a la aplicación desde la línea de comandos.
   */

  public static void main(String[] args) {
    launch();
  }

  /**
   * Método que se ejecuta al iniciar la aplicación.
   * Carga el archivo FXML, aplica el estilo CSS y configura la ventana principal.
   *
   * @param stage El escenario principal de la aplicación.
   * @throws IOException Si hay un error al cargar el archivo FXML o el CSS.
   */

  @Override
  public void start(Stage stage) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("lubi2.fxml"));
    Scene scene = new Scene(fxmlLoader.load(), 790, 527);
    scene.getStylesheets()
        .add(getClass().getResource("/com/example/crud/Estilo.css").toExternalForm());
    stage.setTitle("ventana");
    stage.setScene(scene);
    stage.show();
  }
}
