package inventory_application;

import javafx.collections.ObservableList;

public class Product extends Part {
    private ObservableList<Part> associatedParts;

    public Product(){

    }

    public Product(int id, String name, double price, int stock, int min, int max, String type, ObservableList<Part> associatedParts) {
        super(id, name, price, stock, min, max, type);
        this.associatedParts = associatedParts;
    }

    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }

    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }

    public void deleteAssociatedPart(Part part) {
        associatedParts.remove(part);
    }
}
