package inventory_application;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;

public class AddProductController {

    private ObservableList<Part> tempAssociatedParts;
    @FXML
    private TextField nameField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField stockField;
    @FXML
    private TextField minField;
    @FXML
    private TextField maxField;
    @FXML
    private TextField searchField;
    @FXML
    private TableView<Part> partsTableView;
    @FXML
    private TableView<Part> associatedPartsTableView;
    @FXML
    private Button saveButton;
    @FXML
    private Button closeButton;

    private FilteredList<Part> filteredPartsList;
//    private Predicate<Part> searchParts;
//    private Predicate<Part> searchAllParts;

    public void initialize() {
        partsTableView.setItems(Inventory.getInstance().getAllParts());
        partsTableView.getSelectionModel().selectFirst();
        tempAssociatedParts = FXCollections.observableArrayList();
        associatedPartsTableView.setItems(tempAssociatedParts);
        associatedPartsTableView.getSelectionModel().selectFirst();
        saveButton.setDisable(true);
    }

    public void handleClickListView() {
        Inventory.getInstance().setPart(partsTableView.getSelectionModel().getSelectedItem());
    }

    @FXML
    public void handleKeyReleased() {
        String name = nameField.getText().trim();
        String price = priceField.getText();
        String stock = stockField.getText();
        String min = minField.getText();
        String max = maxField.getText();

        boolean disableButton = name.isEmpty() || name.trim().isEmpty() || price.isEmpty() || price.trim().isEmpty()
                || stock.trim().isEmpty() || stock.isEmpty() || min.trim().isEmpty() || min.trim().isEmpty()
                || max.isEmpty() || max.trim().isEmpty();
        saveButton.setDisable(disableButton);
    }

    public void addAssociatedPart(){
        tempAssociatedParts.add(Inventory.getInstance().getPart());
    }

    public void deleteAssociatedPart(){
        Part item = associatedPartsTableView.getSelectionModel().getSelectedItem();
        tempAssociatedParts.remove(item);
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

    public Product processProduct(){
        if(tempAssociatedParts.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Product Must Contain at least 1 Part");
            Optional<ButtonType> result = alert.showAndWait();
            Product dummyProduct = new Product(0,"0",0,0,0,0,"Empty",tempAssociatedParts);
            return dummyProduct;
        }else{
        Inventory.incrementCounter();
        String name = nameField.getText().trim();
        double price = Double.parseDouble(priceField.getText());
        int stock = Integer.parseInt(stockField.getText());
        int min = Integer.parseInt(minField.getText());
        int max = Integer.parseInt(maxField.getText());
        Product item = new Product(Inventory.getCounter(),name,price,stock,min,max,"Product",tempAssociatedParts);
        Inventory.getInstance().addProduct(item);
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();
        return item;}
    }

    @FXML
    public Product handleCloseButtonAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Adding Part");
        alert.setTitle("Cancelling Adding Part");
        alert.setContentText("Are you sure you wish to cancel?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && (result.get() == ButtonType.OK)) {
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
        }
        Product dummyProduct = new Product(0,"0",0,0,0,0,"Empty",tempAssociatedParts);
        return dummyProduct;
    }

}
