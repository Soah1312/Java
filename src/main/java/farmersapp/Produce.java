package farmersapp;

import java.math.BigDecimal;

public class Produce {

    private Long id;
    private String produceName;
    private BigDecimal quantity;
    private String farmerName;
    private String dateAdded;

    public Produce() {}

    public Produce(Long id, String produceName, BigDecimal quantity, String farmerName, String dateAdded) {
        this.id = id;
        this.produceName = produceName;
        this.quantity = quantity;
        this.farmerName = farmerName;
        this.dateAdded = dateAdded;
    }

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

