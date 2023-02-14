module com.example.pingpongjavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens com.example.pingpongjavafx to javafx.fxml;
    exports com.example.pingpongjavafx;
    exports com.example.pingpongjavafx.controller;
    opens com.example.pingpongjavafx.controller to javafx.fxml;
}