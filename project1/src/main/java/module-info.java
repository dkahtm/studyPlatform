module org.example.frontend {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires javafx.graphics;
    requires com.google.gson;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires okhttp3;
    requires org.json;
    requires javafx.base;


    opens org.example.frontend to javafx.fxml;
    exports org.example.frontend;
    exports org.example.frontend.Controllers;
    opens org.example.frontend.Controllers to javafx.fxml;
    exports org.example.frontend.Services;
    opens org.example.frontend.Services to javafx.fxml;
    opens org.example.frontend.Models to com.fasterxml.jackson.databind; // <-- очень важно для Jackson


}