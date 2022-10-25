package inventory_application;

public class InHouse extends Part {
private int machineId;

    public InHouse() {
    }

    public InHouse(int id, String name, double price, int stock, int min, int max, String type, int machineId) {
        super(id, name, price, stock, min, max, type);
        this.machineId=machineId;
    }

    public int getMachineId() {
        return machineId;
    }

    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
}

