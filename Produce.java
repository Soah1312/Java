public class Produce {
    private long id;
    private String name;
    private double kg;
    private double price;

    public Produce(long id, String name, double kg, double price) {
        this.id = id;
        this.name = name;
        this.kg = kg;
        this.price = price;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getKg() { return kg; }
    public void setKg(double kg) { this.kg = kg; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public double getTotal() { return kg * price; }
}
