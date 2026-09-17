module org.example.proiect_sgbd_vizionare_filme {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.zaxxer.hikari;
    requires java.rmi;

    opens org.example.proiect_sgbd_vizionare_filme to javafx.fxml;
    exports org.example.proiect_sgbd_vizionare_filme;

    opens org.example.proiect_sgbd_vizionare_filme.controllers to javafx.fxml;
    exports org.example.proiect_sgbd_vizionare_filme.controllers;
}