package inventory_application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Optional;

public class AddPartController {
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
    private RadioButton inHouseRB;
    @FXML
    private RadioButton outHouseRB;
    @FXML
    private Label classSpecificLabel;
    @FXML
    private TextField classSpecificField;
    @FXML
    private Button saveButton;
    @FXML
    private Button closeButton;

    public void initialize() {
        inHouseRB.setSelected(true);
        saveButton.setDisable(true);
    }

    @FXML
    public void handleKeyReleased() {
        if (inHouseRB.isSelected()) {
            classSpecificLabel.setText("Machine ID");
            classSpecificField.setPromptText("Mach ID");
        }
        if (outHouseRB.isSelected()) {
            classSpecificLabel.setText("Company Name");
            classSpecificField.setPromptText("Comp Nm");
        }

        String name = nameField.getText().trim();
        String price = priceField.getText();
        String stock = stockField.getText();
        String min = minField.getText();
        String max = maxField.getText();
        String classSpecific = classSpecificField.getText();

        boolean disableButton = name.isEmpty() || name.trim().isEmpty() || price.isEmpty() || price.trim().isEmpty()
                || stock.trim().isEmpty() || stock.isEmpty() || min.trim().isEmpty() || min.trim().isEmpty()
                || max.isEmpty() || max.trim().isEmpty() || classSpecific.trim().isEmpty() || classSpecific.isEmpty();
        saveButton.setDisable(disableButton);
    }

    public Part processPart() {
        boolean allGood;

        String name = nameField.getText().trim();
        double price = Double.parseDouble(priceField.getText());
        int stock = Integer.parseInt(stockField.getText());
        int min = Integer.parseInt(minField.getText());
        int max = Integer.parseInt(maxField.getText());

            if (min >= max) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Min must be less than Max");
                Optional<ButtonType> result = alert.showAndWait();
                allGood = false;
            } else if (min < 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Min must be >= 0");
                Optional<ButtonType> result = alert.showAndWait();
                allGood = false;
            } else if (stock > max) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Inventory cannot be greater than Max");
                Optional<ButtonType> result = alert.showAndWait();
                allGood = false;
            } else if (stock < min) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Inventory cannot be less than min");
                Optional<ButtonType> result = alert.showAndWait();
                allGood = false;
            } else {
                allGood = true;
            }
            if (allGood) {
                Inventory.incrementCounter();
                if (inHouseRB.isSelected()) {
                    int machineID = Integer.parseInt(classSpecificField.getText());
                    InHouse newPart = new InHouse(Inventory.getCounter(), name, price, stock, min, max, "Inhouse", machineID);
                    Inventory.getInstance().addPart(newPart);
                    Stage stage = (Stage) saveButton.getScene().getWindow();
                    stage.close();
                    return newPart;

                } else {
                    String companyName = classSpecificField.getText().trim();
                    Outsourced newPart = new Outsourced(Inventory.getCounter(), name, price, stock, min, max, "Outsourced", companyName);
                    Inventory.getInstance().addPart(newPart);
                    Stage stage = (Stage) saveButton.getScene().getWindow();
                    stage.close();
                    return newPart;
                }
            } else {
                System.out.println("Do nothing");
                Part dummyPart = new Part(0, "0", 0.0, 0, 0, 0, "Dummy");
                return dummyPart;
            }
    }

    @FXML
    public void handleCloseButtonAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Adding Part");
        alert.setTitle("Cancelling Adding Part");
        alert.setContentText("Are you sure you wish to cancel?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && (result.get() == ButtonType.OK)) {
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    public void handleSaveButtonAction(ActionEvent event) {
        processPart();
    }
}
