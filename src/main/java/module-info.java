module com.example.demo1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.google.zxing;
    requires com.google.zxing.javase;
    requires stripe.java;


    opens com.example.demo1 to javafx.fxml;
    opens com.example.demo1.model to javafx.base;
    exports com.example.demo1;
}
