package lanej.inventorysystem;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class InventoryApplication extends Application {
    public enum ScreenType {
        MAIN_SCREEN,
        ADD_PART,
        MODIFY_PART,
        ADD_PRODUCT,
        MODIFY_PRODUCT
    }

    /*Running into an issue here. Although I believe I did everything right, I'm getting this error:
    *   "Exception in thread "JavaFX Application Thread"
    *    java.lang.RuntimeException: java.lang.reflect.InvocationTargetException"
    * This is when I'm testing the method using the call from the addPartButton(ActionEvent, ScreenType)
    * method in MainScreen.java.
    * I believe the ScreenType is passed and behaves with the switch properly (I tested this by adding a
    * System.out.println() statement to the ADD_PART case code block).
    * I found the issue to be me implementing the switch incorrectly. I forgot that if you don't use break statements,
    * it will continue down the cases, executing those statements as well.
    */
    public static void toScreen(ActionEvent event, ScreenType type) {
        // Default variable intiializing
        String screenName = "";
        String filePath = "";
        double minWidth = 10.0;
        double minHeight = 10.0;

        switch (type) {
            case MAIN_SCREEN: {
                screenName = "Inventory Management";
                filePath = "view/main-screen.fxml";
                minWidth = 640.0;
                minHeight = 300.0;
                break;
            }
            case ADD_PART: {
                screenName = "Add Part";
                filePath = "view/add-part.fxml";
                minWidth = 360.0;
                minHeight = 550.0;
                break;
            }
            case MODIFY_PART: {
                screenName = "Modify Part";
                filePath = "view/modify-part.fxml";
                minWidth = 400.0;
                minHeight = 400.0;
                break;
            }
            case ADD_PRODUCT: {
                screenName = "Add Product";
                filePath = "view/add-product.fxml";
                break;
            }
            case MODIFY_PRODUCT: {
                screenName = "Modify Product";
                filePath = "view/modify-product.fxml";
                break;
            }
        }

        try {
            // Load FXML for screen
            // Throws IOException if there is an error during loading
            Parent root = FXMLLoader.load(Objects.requireNonNull(InventoryApplication.class.getResource(filePath)));

            // Get the stage object of the source event
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

            // New scene with root's FXML
            Scene scene = new Scene(root);

            // Set stage properties
            stage.setTitle(screenName);
            stage.setMinWidth(minWidth);
            stage.setMinHeight(minHeight);

            // Change stage to the scene
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Alert alert = new Alert(AlertType.ERROR,
                    "Error when initializing " + screenName + " Screen!\n\nMessage: \n" + e.getMessage());
            alert.showAndWait();
            InventoryApplication.terminate(event);
        }
    }

    public static void terminate(ActionEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    @Override
    public void start(Stage stage) throws IOException {
        // Import inventory data from previous usage
        // TODO: implement .csv file parsing
        // TODO: create an Inventory object and add all items from .csv

//        // Initialize Starting Screen
//        FXMLLoader fxmlLoader = new FXMLLoader(InventoryApplication.class.getResource("view/main-screen.fxml"));
//        Scene scene = new Scene(fxmlLoader.load());
//        stage.setTitle("Inventory Management");
//        stage.setScene(scene);
//        stage.setMinHeight(300.0);
//        stage.setMinWidth(640.0);
//        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}