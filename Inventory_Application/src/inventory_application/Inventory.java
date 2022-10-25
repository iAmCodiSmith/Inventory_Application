package inventory_application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Predicate;

public class Inventory {
    private static Inventory instance = new Inventory();

    private static Part currentPart = new Part();
    private static Product currentProduct = new Product();
    private static InHouse currentInHouse = new InHouse();
    private static Outsourced currentOutSourced = new Outsourced();

    private static String filename = "InventoryData.txt";
    private static Counter counter = new Counter();
    private ObservableList<Part> allParts;
    private ObservableList<Product> allProducts;

    public static void incrementCounter() {
        counter.incrementCounter();
    }

    public static int getCounter() {
        return counter.getCounter();
    }

    public static Inventory getInstance() {
        return instance;
    }

    public ObservableList<Part> getAllParts() {
        return allParts;
    }

    public ObservableList<Product> getAllProducts() {
        return allProducts;
    }

    public Part getPart() {
        return currentPart;
    }

    public void setPart(Part part) {
        this.currentPart = part;
    }

    public Product getProduct() {
        return currentProduct;
    }

    public void setProduct(Product product) {
        this.currentProduct = product;
    }

    public InHouse getCurrentInHouse() {
        return currentInHouse;
    }

    public void setCurrentInHouse(InHouse currentInHouse) {
        Inventory.currentInHouse = currentInHouse;
    }

    public Outsourced getCurrentOutSourced() {
        return currentOutSourced;
    }

    public void setCurrentOutSourced(Outsourced currentOutSourced) {
        Inventory.currentOutSourced = currentOutSourced;
    }

    public void addPart(Part newPart) {
        allParts.add(newPart);
    }

    public void deletePart(Part part) {
        allParts.remove(part);
    }

    public void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    public void deleteProduct(Product product) {
        allProducts.remove(product);
    }

    public Part lookupPart(int partId) {
        for (int i = 0; i < allParts.size(); i++) {
            if (allParts.get(i).getId() == partId) {
                return allParts.get(i);
            }
        }
        return null;
    }

    public Product lookupProduct(int productId) {
        for (int i = 0; i < allProducts.size(); i++) {
            if (allProducts.get(i).getId() == productId)
                return allProducts.get(i);
        }
        return null;
    }

    public ObservableList<Part> lookupPart(String partName) {

        ObservableList<Part> filteredParts = FXCollections.observableArrayList();
        String name = partName.trim();

        for (int i = 0; i < allParts.size(); i++) {
            if (allParts.get(i).toString().contains(name)) {
                filteredParts.add(allParts.get(i));
            }
        }
        if (filteredParts.isEmpty()) {
            filteredParts.add(lookupPart(Integer.parseInt(name)));
            if (filteredParts.get(0) == null) {
                filteredParts.remove(0);
            }
        }

        return filteredParts;
    }

    public ObservableList<Product> lookupProduct(String productName) {

        ObservableList<Product> filteredProducts = FXCollections.observableArrayList();
        String name = productName.trim();

        for (int i = 0; i < allProducts.size(); i++) {
            if (allProducts.get(i).toString().contains(name)) {
                filteredProducts.add(allProducts.get(i));
            }
        }
        if (filteredProducts.isEmpty()) {
            filteredProducts.add(lookupProduct(Integer.parseInt(name)));
            if (filteredProducts.get(0) == null) {
                filteredProducts.remove(0);
            }
        }
        return filteredProducts;
    }

    public void updatePart(int index, Part selectedPart) {

        for (int i = 0; i < allParts.size(); i++) {
            if (allParts.get(i).getId() == index) {
                allParts.remove(allParts.get(i));
            }
        }
        allParts.add(selectedPart);
        Inventory.getInstance().setPart(selectedPart);
    }

    public void updateProduct(int index, Product selectedProduct) {

        for (int i = 0; i < allProducts.size(); i++) {
            if (allProducts.get(i).getId() == index) {
                allProducts.remove(allProducts.get(i));
            }
        }
        allProducts.add(selectedProduct);
        Inventory.getInstance().setPart(selectedProduct);
    }


    public void loadParts() throws IOException {
        allParts = FXCollections.observableArrayList();
        allProducts = FXCollections.observableArrayList();
        Path path = Paths.get(filename);
        BufferedReader br = Files.newBufferedReader(path);

        String input;


        try {
            while ((input = br.readLine()) != null) {
                String[] itemPieces = input.split("\t");

                String id = itemPieces[0];
                String name = itemPieces[1];
                String price = itemPieces[2];
                String stock = itemPieces[3];
                String min = itemPieces[4];
                String max = itemPieces[5];
                String type = itemPieces[6];
                String classSpecific = itemPieces[7];


                if (type.equals("Inhouse")) {
                    InHouse newPart = new InHouse(Integer.parseInt(id), name, Double.parseDouble(price),
                            Integer.parseInt(stock), Integer.parseInt(min), Integer.parseInt(max), type, Integer.parseInt(classSpecific));
                    allParts.add(newPart);
                } else if (type.equals("Outsourced")) {
                    Outsourced newPart = new Outsourced(Integer.parseInt(id), name, Double.parseDouble(price),
                            Integer.parseInt(stock), Integer.parseInt(min), Integer.parseInt(max), type, classSpecific);
                    allParts.add(newPart);

                } else if (type.equals("Product")) {
                    ObservableList<Part> tempAssociatedParts = FXCollections.observableArrayList();
                    List<String> associatedPartsLoadList = Arrays.asList(classSpecific.split("\\s*,\\s*"));

                    ListIterator<String> iter = associatedPartsLoadList.listIterator();
                    try {
                        while (iter.hasNext()) {
                            String temp = iter.next();
                            ListIterator<Part> partsIter = allParts.listIterator();
                            try {
                                while (partsIter.hasNext()) {
                                    if (temp.contains(partsIter.next().getName())) {
                                        tempAssociatedParts.add(partsIter.previous());
                                        partsIter.next();
                                    }
                                }
                            } finally {
                            }
                        }
                    } finally {
                    }

                    Product newProduct = new Product(Integer.parseInt(id), name, Double.parseDouble(price), Integer.parseInt(stock),
                            Integer.parseInt(min), Integer.parseInt(max), type, tempAssociatedParts);
                    allProducts.add(newProduct);
                } else if (type.equals("Counter")) {
                    counter.setCounter(Integer.parseInt(id));
                    System.out.println(counter.getCounter() + " Items Loaded...");
                }

            }
        } finally {


            if (br != null) {

                br.close();

            }
        }
    }

    public void saveParts() throws IOException {
        Path path = Paths.get(filename);
        BufferedWriter bw = Files.newBufferedWriter(path);
        try {
            ListIterator<Part> iter = allParts.listIterator();
            while (iter.hasNext()) {

                if (iter.next().getClass().toString().equals("class com.codismith.InventoryApp.InHouse")) {
                    InHouse item = (InHouse) iter.previous();
                    System.out.print(item.toString());
                    bw.write(String.format("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s",
                            item.getId(), item.getName(), item.getPrice(), item.getStock(), item.getMin(), item.getMax(), item.getType(), item.getMachineId()));

                    bw.newLine();
                    iter.next();
                } else {
                    Outsourced item = (Outsourced) iter.previous();
                    System.out.print(item.toString());
                    bw.write(String.format("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s",
                            item.getId(), item.getName(), item.getPrice(), item.getStock(), item.getMin(), item.getMax(), item.getType(), item.getCompanyName()));
                    bw.newLine();
                    iter.next();
                }
                System.out.println(" Saved");
            }
        } finally {
            try {
                ListIterator<Product> iter = allProducts.listIterator();
                while (iter.hasNext()) {
                    Product item = (Product) iter.next();
                    System.out.print(item.toString());
                    bw.write(String.format("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s",
                            item.getId(), item.getName(), item.getPrice(), item.getStock(), item.getMax(), item.getMin(),
                            item.getType(), item.getAllAssociatedParts()));
                    bw.newLine();
                    System.out.println(" Saved");
                }
            } finally {
                bw.write(String.format("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s",
                        counter.getCounter(), "0", "0", "0", "0", "0", "Counter", "0"));

                if (bw != null) {
                    bw.close();
                }
            }
        }
    }
}