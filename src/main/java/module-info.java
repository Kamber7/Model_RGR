module com.example.model_rgr {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.model_rgr to javafx.fxml;
    exports com.example.model_rgr;
}