module org.a22ivancp.tarealista {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.a22ivancp.tareatontisima to javafx.fxml;
    exports org.a22ivancp.tareatontisima;
}