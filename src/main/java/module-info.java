module lanej.inventorysystem {
    requires javafx.controls;
    requires javafx.fxml;


    opens lanej.inventorysystem to javafx.fxml;
    exports lanej.inventorysystem;
    opens lanej.inventorysystem.controller to javafx.fxml;
    exports lanej.inventorysystem.controller;
    exports lanej.inventorysystem.model;
}