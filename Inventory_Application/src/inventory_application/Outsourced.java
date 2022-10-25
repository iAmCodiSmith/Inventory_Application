package inventory_application;

public class Outsourced extends Part{
private String companyName;

    public Outsourced() {
    }

    public Outsourced(int id, String name, double price, int stock, int min, int max, String type, String companyName) {
        super(id, name, price, stock, min, max, type);
        this.companyName=companyName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
