package farmersapp;

import java.math.BigDecimal;

public class Produce {

    private Long id;
    private String produceName;
    private BigDecimal quantity;
    private Long farmerId;
    private String farmerName; // For display purposes (from JOIN)
    private String dateAdded;

    public Produce() {}

    public Produce(Long id, String produceName, BigDecimal quantity, Long farmerId, String farmerName, String dateAdded) {
        this.id = id;
        this.produceName = produceName;
        this.quantity = quantity;
        this.farmerId = farmerId;
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

