module com.example.crud {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.json;


    opens com.example.crud to javafx.fxml;
    exports com.example.crud;
    exports com.example.crud.model;
    opens com.example.crud.model to javafx.fxml;
    exports com.example.crud.view;
    opens com.example.crud.view to javafx.fxml;
}