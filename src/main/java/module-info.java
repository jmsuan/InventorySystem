/**
 * This module provides classes and resources for a simple inventory system.
 * The module's codebase requires JavaFX Controls and FXML modules.
 * The module opens the lanej.inventorysystem amd lanej.inventorysystem.controller packages to allow for FXML file
 * loading, and exports the same packages for use outside the module.
 * Javadoc files are provided in the top directory of this Project's file structure (in the javadoc/ directory).
 * <p>Two (2) <b>RUNTIME ERROR</b>s can be found in the lanej.inventorysystem.InventoryApplication Class.</p>
 * <p>One (1) <b>LOGICAL ERROR</b> can be found in the lanej.inventorysystem.controller.ModifyPart Class.</p>
 * <p>The <b>FUTURE ENHANCEMENT</b> can be found in the lanej.inventorysystem.InventoryApplication Class.</p>
 * @author Jonathan Lane
 */
module lanej.inventorysystem {
    requires javafx.controls;
    requires javafx.fxml;


    opens lanej.inventorysystem to javafx.fxml;
    exports lanej.inventorysystem;
    opens lanej.inventorysystem.controller to javafx.fxml;
    exports lanej.inventorysystem.controller;
    exports lanej.inventorysystem.model;
}