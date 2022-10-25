package inventory_application;
public class Counter {
    private static int counter=0;
    private String one="0";
    private String two="0";
    private String three="0";
    private String four="0";
    private String five="0";
    private String type="Counter";
    private String six="0";

    public Counter() {
    }

    public void setCounter(int n){
        this.counter=n;
    }

    public int getCounter() {
        return counter;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void incrementCounter(){
        this.counter+=1;
    }
}
