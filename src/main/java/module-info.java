module org.example.langjavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires mysql.connector.java;
    requires java.sql;


    opens org.example.langjavafx to javafx.fxml;
    exports org.example.langjavafx;
}