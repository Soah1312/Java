package farmersapp;

import java.math.BigDecimal;

// this class is just for storing produce info
// learned about this in week 1 of oop lol
public class Produce {

    // these are the things we need to track
    private Long id;
    private String produceName;
    private BigDecimal quantity; // using bigdecimal cause double is weird with decimals
    private Long farmerId;
    private String farmerName; // just for showing in the table
    private String dateAdded;

    // empty constructor needed for javafx apparently
    public Produce() {}

    // constructor with all the stuff
    public Produce(Long id, String produceName, BigDecimal quantity, Long farmerId, String farmerName, String dateAdded) {
        this.id = id;
        this.produceName = produceName;
        this.quantity = quantity;
        this.farmerId = farmerId;
        this.farmerName = farmerName;
        this.dateAdded = dateAdded;
    }

    // getters and setters (had to write all of these manually took forever)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProduceName() {
        return produceName;
    }

    public void setProduceName(String produceName) {
        this.produceName = produceName;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Long getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(Long farmerId) {
        this.farmerId = farmerId;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }
    
    public String getDateAdded() {
        return dateAdded;
    }
    
    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }
}

