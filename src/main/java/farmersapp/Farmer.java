package farmersapp;

// simple class to hold farmer data
public class Farmer {

    private Long id;
    private String name;
    private String phone;
    private String location;

    // empty constructor
    public Farmer() {}

    // constructor with everything
    public Farmer(Long id, String name, String phone, String location) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.location = location;
    }

    // all the getters and setters below
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    // this makes the dropdown show the farmer name instead of weird memory address
    @Override
    public String toString() {
        return name;
    }
}
