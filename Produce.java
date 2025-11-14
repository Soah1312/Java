public class Produce { // just a simple data holder for one produce item
    private long id;    // DB id so I can fetch/update if needed
    private String name; // what the thing is called
    private double kg;   // how many kilograms I have
    private double price; // price per kg

    public Produce(long id, String name, double kg, double price) { // quick constructor to set everything
        this.id = id;
        this.name = name;
        this.kg = kg;
        this.price = price;
    }

    public long getId() { return id; } // I need getters so the TableView can read values
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getKg() { return kg; }
    public void setKg(double kg) { this.kg = kg; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    
}
