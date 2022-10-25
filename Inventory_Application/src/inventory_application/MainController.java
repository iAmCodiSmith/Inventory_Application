package inventory_application;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;

public class MainController {

    @FXML
    private BorderPane mainWindow;
    @FXML
    private TableView<Part> partsTableView;
    @FXML
    private TableView<Product> productsTableView;
    @FXML
    private TextField searchField;
    @FXML
    private TextField productSearchField;

    private Predicate<Part> searchParts;
    private FilteredList<Part> filteredPartsList;
    private Predicate<Part> searchAllParts;

    private Predicate<Product> searchProducts;
    private Predicate<Product> searchAllProducts;
    private FilteredList<Product> filteredProductsList;


    public void initialize() {
        partsTableView.setItems(Inventory.getInstance().getAllParts());
        partsTableView.getSelectionModel().selectFirst();
        Inventory.getInstance().setPart(partsTableView.getSelectionModel().getSelectedItem());
        productsTableView.setItems(Inventory.getInstance().getAllProducts());
        productsTableView.getSelectionModel().selectFirst();
        Inventory.getInstance().setProduct(productsTableView.getSelectionModel().getSelectedItem());
        Part item = partsTableView.getSelectionModel().getSelectedItem();
        if (item != null) {
            String type = item.getType();
            if (type.equals("Inhouse")) {
                InHouse tempIn = (InHouse) item;
                Inventory.getInstance().setCurrentInHouse(tempIn);
            } else if (type.equals("Outsourced")) {
                Outsourced tempIn = (Outsourced) item;
                Inventory.getInstance().setCurrentOutSourced(tempIn);
            }
        }

    }

    public void handleClickListView() {
        Inventory.getInstance().setPart(partsTableView.getSelectionModel().getSelectedItem());
        Part item = partsTableView.getSelectionModel().getSelectedItem();
        String type = item.getType();
        if (type.equals("Inhouse")) {
            InHouse tempIn = (InHouse) item;
            Inventory.getInstance().setCurrentInHouse(tempIn);
        } else if (type.equals("Outsourced")) {
            Outsourced tempIn = (Outsourced) item;
            Inventory.getInstance().setCurrentOutSourced(tempIn);
        }
        Inventory.getInstance().setProduct(productsTableView.getSelectionModel().getSelectedItem());
    }

    public void showAddPartDialog() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("addPart.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 400, 350);
            Stage stage = new Stage();
            stage.setTitle("Add Part");
            stage.setScene(scene);
            stage.showAndWait();

            partsTableView.getSelectionModel().selectFirst();
            Inventory.getInstance().setPart(partsTableView.getSelectionModel().getSelectedItem());
        } catch (IOException e) {
            System.out.println("Couldn't load dialog");
            e.printStackTrace();
            return;
        }
    }

    public void showModifyPartDialog() {


        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("modifyPartWindow.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 400, 350);
            Stage stage = new Stage();
            stage.setTitle("Modify Part");
            stage.setScene(scene);
            stage.showAndWait();
            partsTableView.getSelectionModel().selectLast();
            Inventory.getInstance().setPart(partsTableView.getSelectionModel().getSelectedItem());
            handleClickListView();
        } catch (IOException e) {
            System.out.println("Couldn't load dialog");
            e.printStackTrace();
            return;
        }
    }

    public void deletePart() {
        Part part = partsTableView.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Part");
        alert.setTitle("Deleting Part: '" + part.getName() + "'");
        alert.setContentText("Are you sure you wish to continue?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && (result.get() == ButtonType.OK)) {
            Inventory.getInstance().deletePart(part);
        }
    }

    public void showAddProductDialog() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("addProductWindow.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 725, 430);
            Stage stage = new Stage();
            stage.setTitle("Add Product");
            stage.setScene(scene);
            stage.showAndWait();

            productsTableView.getSelectionModel().selectFirst();
            Inventory.getInstance().setProduct(productsTableView.getSelectionModel().getSelectedItem());
        } catch (IOException e) {
            System.out.println("Couldn't load dialog");
            e.printStackTrace();
            return;
        }
    }

    public void showModifyProductDialog() {
        try {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("modifyProductWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 725, 430);
            Stage stage = new Stage();
            stage.setTitle("Modify Product");
            stage.setScene(scene);
            stage.showAndWait();
            productsTableView.getSelectionModel().selectLast();
            Inventory.getInstance().setProduct(productsTableView.getSelectionModel().getSelectedItem());
            handleClickListView();
        } catch (IOException e) {
            System.out.println("Couldn't load dialog");
            e.printStackTrace();
            return;
        }
    }

    public void deleteProduct() {
        Product product = productsTableView.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Product");
        alert.setTitle("Deleting Product: '" + product.getName() + "'");
        alert.setContentText("Are you sure you wish to continue?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && (result.get() == ButtonType.OK)) {
            Inventory.getInstance().deleteProduct(product);
        }
    }

    @FXML
    public void handlePartsFilterButton() {
        String partName = searchField.getText().trim();
        ObservableList<Part> sortedList = Inventory.getInstance().lookupPart(partName);
        partsTableView.setItems(sortedList);
        partsTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        partsTableView.getSelectionModel().selectFirst();
        handleClickListView();
    }

    @FXML
    public void handleProductsFilterButton() {
        String productName = productSearchField.getText().trim();
        ObservableList<Product> sortedList = Inventory.getInstance().lookupProduct(productName);
        productsTableView.setItems(sortedList);
        productsTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        productsTableView.getSelectionModel().selectFirst();
        handleClickListView();
    }


    @FXML
    public void handleExit() {
        Platform.exit();
    }

}
