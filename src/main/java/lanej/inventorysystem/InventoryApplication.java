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
import lanej.inventorysystem.model.InHouse;
import lanej.inventorysystem.model.Inventory;
import lanej.inventorysystem.model.Outsourced;
import lanej.inventorysystem.model.Product;
import lanej.inventorysystem.model.Part;

import java.io.*;
import java.util.*;

public class InventoryApplication extends Application {
    private static String dataFileName = "output.csv";
    public enum ScreenType {
        MAIN_SCREEN,
        ADD_PART,
        MODIFY_PART,
        ADD_PRODUCT,
        MODIFY_PRODUCT
    }
    public static Part partToModify;
    public static Product productToModify;

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
            case MAIN_SCREEN -> {
                screenName = "Inventory Management";
                filePath = "view/main-screen.fxml";
                minWidth = 640.0;
                minHeight = 300.0;
            }
            case ADD_PART -> {
                screenName = "Add Part";
                filePath = "view/add-part.fxml";
                minWidth = 360.0;
                minHeight = 550.0;
            }
            case MODIFY_PART -> {
                screenName = "Modify Part";
                filePath = "view/modify-part.fxml";
                minWidth = 400.0;
                minHeight = 400.0;
            }
            case ADD_PRODUCT -> {
                screenName = "Add Product";
                filePath = "view/add-product.fxml";
            }
            case MODIFY_PRODUCT -> {
                screenName = "Modify Product";
                filePath = "view/modify-product.fxml";
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
        try {
            System.out.println("\nAttempting to write to file!");
            writeFile();
        }
        catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR,
                    "Error when attempting to write to " + dataFileName + " file!\n\nMessage: \n" +
                            e.getMessage());
            alert.showAndWait();
        }
        stage.close();
    }

    public static List<List<String>> parseCsv(String resourceFilePath) {
        List<List<String>> foundRecords = new ArrayList<>();
        try {
            File csvFile = new File(resourceFilePath);
            BufferedReader br = new BufferedReader(new FileReader(csvFile));
            String currLine;
            while ((currLine = br.readLine()) != null) {
                // Filter currLine to only have UTF-8 compatible characters
                currLine = currLine.replaceAll("[^\\x00-\\x7F\\x80-\\xFF]", "");

                // Assume that the .csv file is simple and doesn't involve commas in the data
                String[] values = currLine.split(",");
                foundRecords.add(Arrays.asList(values));
            }
            br.close();
        }
        catch (NullPointerException | FileNotFoundException e) {
            System.err.println("Error when attempting to find file: ");
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR,
                    "Error when attempting to find " + resourceFilePath + " file!\n" +
                    "Make sure the file is in the directory: src/main/java/lanej/resources/inventorysystem/" +
                    "\n\nMessage: \n" + e.getMessage());
            alert.showAndWait();
        }
        catch (IOException e) {
            System.err.println("Error when attempting to read file: ");
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR,
                    "Error when attempting to read " + resourceFilePath + "!\n\nMessage: \n" + e.getMessage());
            alert.showAndWait();
        }
        return foundRecords;
    }

    public static void writeFile() throws Exception {
        List<Part> allParts = Inventory.getAllParts();
        List<Product> allProducts = Inventory.getAllProducts();
        List<String> outputLines = new LinkedList<>();

        // Only valid header with this application
        outputLines.add("ProductOrPart,ID,Name,Price,Stock,Min,Max,MachineID,CompanyName,AssociatedParts");

        // Add all Parts to outputLines
        for (Part currentPart : allParts) {
            int partId = currentPart.getId();
            String partType = Objects.requireNonNull(Inventory.lookupPart(partId)).getClass().getSimpleName();
            partType = partType.substring(partType.lastIndexOf(".") + 1);
            String currentLine = "Part," +
                    partId + "," +
                    currentPart.getName() + "," +
                    currentPart.getPrice() + "," +
                    currentPart.getStock() + "," +
                    currentPart.getMin() + "," +
                    currentPart.getMax() + ",";
            if (partType.equals("InHouse")) {
                InHouse inHousePart = (InHouse)Inventory.lookupPart(partId);
                assert inHousePart != null;
                currentLine += inHousePart.getMachineId() + ",,";
            }
            if (partType.equals("Outsourced")) {
                Outsourced outsourcedPart = (Outsourced)Inventory.lookupPart(partId);
                assert outsourcedPart != null;
                currentLine += "," + outsourcedPart.getCompanyName() + ",";
            }
            outputLines.add(currentLine);
        }

        // Add all Products to outputLines
        for (Product currentProduct : allProducts) {
            List<Part> associatedParts = new LinkedList<>(currentProduct.getAllAssociatedParts());
            StringBuilder associatedPartsString = new StringBuilder();
            if (associatedParts.size() > 0) {
                for (Part part : associatedParts) {
                    associatedPartsString.append(".").append(part.getId()); // Using periods to separate Part IDs
                }
                associatedPartsString.deleteCharAt(0); // Remove leading period
            }
            else { // Product not associated with any parts
                associatedPartsString.append("0");
            }
            String currentLine = "Product," +
                    currentProduct.getId() + "," +
                    currentProduct.getName() + "," +
                    currentProduct.getPrice() + "," +
                    currentProduct.getStock() + "," +
                    currentProduct.getMin() + "," +
                    currentProduct.getMax() + ",,," +
                    associatedPartsString;
            outputLines.add(currentLine);
        }

        // Preview output in console
        System.out.println("Will be output to " + dataFileName + ":");
        for (String line : outputLines) {
            System.out.println(line);
        }

        // Write to the file that is specified
        FileOutputStream outStream = null;
        try {
            String path = dataFileName;
            outStream = new FileOutputStream(path);
            PrintWriter outputStream = new PrintWriter(outStream);
            for (String line : outputLines) {
                outputStream.println(line);
            }
            outputStream.close();
        } catch (IOException e) {
            System.err.println("Error finding file to write to:");
            e.printStackTrace();
            throw new IOException("Error finding file to write to. \n" + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error when attempting to write to file:");
            e.printStackTrace();
            throw new Exception("Unexpected error when attempting to write to file! \n" + e.getMessage());
        } finally {
            if (outStream != null) {
                outStream.close();
            }
        }
    }

    /*
    * Ran into an issue with this code. When making the sample data, I used Microsoft Excel to format the csv file.
    * When attempting to test the data for equality with List<String> validHeaders, it would never return the expected
    * equality value, even though I copy and pasted the content into the Strings that were added to validHeaders.
    *
    * I first tried to use different comparison algorithms, printing the equality (boolean) value to the console.
    * They all returned the same result, displaying something along the lines of:
    *       "Content equality: false"
    *       "ProductOrPart does not equal ﻿ProductOrPart"
    * (I had it print the first element that didn't match).
    *
    * I then tried reversing the equality check. I wanted to see the first element that WAS equal. The print statement
    * exposed that the SECOND element was indeed equal, just not the first. This makes sense with what I found to be
    * the root causing issue.
    *
    * As evident here, the last word of the print statement above contains an "illegal character". This is according to
    * the IDE I'm using (it was not seen visually in the console output). After a lot of troubleshooting and
    * brainstorming, I discovered that it was indeed importing the data from the .csv file correctly, however,
    * that first character is header data from MS Excel. It doesn't match UTF-8 encoding, so filtering the data
    * in parseCsv() to only include the UTF-8 character set fixed this issue.
    * */
    public static void importDataStrings(List<List<String>> dataTable) {
        List<String> dataHeader = dataTable.get(0);
        List<String> validDataHeader = new ArrayList<>(List.of(new String[]{
                "ProductOrPart",
                "ID",
                "Name",
                "Price",
                "Stock",
                "Min",
                "Max",
                "MachineID",
                "CompanyName",
                "AssociatedParts"
        }));
        boolean headerIsValid = true; // Assume true, prove false
        // For loop is the best method here, since we need to check the ordering of the header row as well.
        for (int i = 0; i < validDataHeader.size(); ++i) {
            if (!dataHeader.get(i).equals(validDataHeader.get(i))) {
                headerIsValid = false;
                break;
            }
        }

        if (!headerIsValid) {
            Alert alert = new Alert(AlertType.ERROR, """
                Error when attempting to read provided data file! \
                Make sure your column headers match and are in the right order.

                Valid header order:
                \t"ProductOrPart",
                \t"ID",
                \t"Name",
                \t"Price",
                \t"Stock",
                \t"Min",
                \t"Max",
                \t"MachineID",
                \t"CompanyName",
                \t"AssociatedParts\"""");
            alert.showAndWait();
            return;
        } // File header must be valid beyond this point.

        // Print sample data that was found to the console
        System.out.println("\nData file found and will be imported!");
        System.out.println("Importing data:");
        System.out.println("Length: " + dataTable.get(0).size() + " \t" + dataTable.get(0));
        // Remove header row to make importing the data easier (using for each)
        dataTable.remove(0);
        for (List<String> strings : dataTable) {
            System.out.println("Length: " + strings.size() + " \t" + strings);
        }
        System.out.println();



        // Store as String list so that we can perform validation before adding to inventory.
        List<List<String>> inHouseParts = new ArrayList<>(); // Will be adding/removing more often here.
        List<List<String>> outsourcedParts = new ArrayList<>();
        List<List<String>> products = new ArrayList<>();
        List<Integer> partIds = new ArrayList<>();
        List<Integer> productIds = new ArrayList<>();

        for (List<String> currentLineList : dataTable) {
            int itemId = Integer.parseInt(currentLineList.get(1));
            // List<String> size of 8 indicates an InHouse Part.
            if (currentLineList.size() == 8) {
                inHouseParts.add(currentLineList);
                partIds.add(itemId);
            }
            // List<String> size of 9 indicates an Outsourced Part.
            else if (currentLineList.size() == 9) {
                outsourcedParts.add(currentLineList);
                partIds.add(itemId);
            }
            // List<String> size of 10 indicates an Outsourced Part.
            else if (currentLineList.size() == 10) {
                products.add(currentLineList);
                productIds.add(itemId);
            }
            else {
                System.out.println("File data is malformed!");
            }
        }

        // Validate Part IDs and Product IDs
        boolean partIdsValid = true;
        Collections.sort(partIds);
        for (int i = 0; i < partIds.size(); ++i) {
            // Check if sorted partIds are unique and step starting at 1
            if (partIds.get(i) != (i + 1)) {
                partIdsValid = false;
                break;
            }
        }
        boolean productIdsValid = true;
        Collections.sort(productIds);
        for (int i = 0; i < productIds.size(); ++i) {
            // Check if sorted productIds are unique and step starting at 1
            if (productIds.get(i) != (i + 1)) {
                productIdsValid = false;
                break;
            }
        }
        if (!partIdsValid || !productIdsValid) {
            System.out.println("File data is malformed!\n" +
                    "Product or Part IDs don't count as expected. " +
                    "Ensure there are no duplicate or missing entries!");
        }

        // Import data
        for (List<String> stringData : inHouseParts) {
            int id             = Integer.parseInt(stringData.get(1));
            String name        = stringData.get(2);
            double price       = Double.parseDouble(stringData.get(3));
            int stock          = Integer.parseInt(stringData.get(4));
            int min            = Integer.parseInt(stringData.get(5));
            int max            = Integer.parseInt(stringData.get(6));
            int machineId      = Integer.parseInt(stringData.get(7));
            InHouse newPart    = new InHouse(id, name, price, stock, min, max, machineId);
            Inventory.addPart(newPart);
        }
        for (List<String> stringData : outsourcedParts) {
            int id             = Integer.parseInt(stringData.get(1));
            String name        = stringData.get(2);
            double price       = Double.parseDouble(stringData.get(3));
            int stock          = Integer.parseInt(stringData.get(4));
            int min            = Integer.parseInt(stringData.get(5));
            int max            = Integer.parseInt(stringData.get(6));
            String companyName = stringData.get(8);
            Outsourced newPart = new Outsourced(id, name, price, stock, min, max, companyName);
            Inventory.addPart(newPart);
        }
        for (List<String> stringData : products) {
            int id             = Integer.parseInt(stringData.get(1));
            String name        = stringData.get(2);
            double price       = Double.parseDouble(stringData.get(3));
            int stock          = Integer.parseInt(stringData.get(4));
            int min            = Integer.parseInt(stringData.get(5));
            int max            = Integer.parseInt(stringData.get(6));
            Product newProduct = new Product(id, name, price, stock, min, max);

            String associatedParts = stringData.get(9);
            String[] associatedStringIds = associatedParts.split("\\.");
            for (String stringVal : associatedStringIds) {
                newProduct.addAssociatedPart(Inventory.lookupPart(Integer.parseInt(stringVal)));
            }

            Inventory.addProduct(newProduct);
        }
    }

    public static int nextPartId() {
        List<Part> partsList = Inventory.getAllParts();
        List<Integer> idsUsed = new LinkedList<>();

        // Get list of all currently used Part IDs
        for (Part currentPart : partsList) {
            idsUsed.add(currentPart.getId());
        }

        // Find first available ID int
        int testId = 1;
        while (true) {
            if (!idsUsed.contains(testId)) {
                return testId;
            }
            ++testId;
        }
    }

    public static int nextProductId() {
        List<Product> productsList = Inventory.getAllProducts();
        List<Integer> idsUsed = new LinkedList<>();

        // Get list of all currently used Part IDs
        for (Product currentProduct : productsList) {
            idsUsed.add(currentProduct.getId());
        }

        // Find first available ID int
        int testId = 1;
        while (true) {
            if (!idsUsed.contains(testId)) {
                return testId;
            }
            ++testId;
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        // Import inventory data from previous usage
        List<List<String>> fileStringData;
        fileStringData = parseCsv(dataFileName);

        // Add all items from .csv to Inventory
        importDataStrings(fileStringData);

        // Initialize Starting Screen
        FXMLLoader fxmlLoader = new FXMLLoader(InventoryApplication.class.getResource("view/main-screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Inventory Management");
        stage.setScene(scene);
        stage.setMinHeight(300.0);
        stage.setMinWidth(640.0);
        stage.setOnCloseRequest(closeEvent -> {
            try {
                System.out.println("\nAttempting to write to file!");
                writeFile();
            }
            catch (Exception e) {
                Alert alert = new Alert(AlertType.ERROR,
                        "Error when attempting to write to " + dataFileName + " file!\n\nMessage: \n" +
                                e.getMessage());
                alert.showAndWait();
            }
        });
        stage.show();
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            dataFileName = args[0];
        }
        launch();
    }
}