package model;

public class Passenger {
    private static Long ID = 0L;
    private Long id;
    private String fullName;

    public Passenger(String fullName) {
        this.id = ++ID;
        this.fullName = fullName;
    }

    public Passenger(Long id, String fullName) {
        this.id = id;
        this.fullName = fullName;
    }

    public Passenger(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    @Override
    public String toString() {
        return "{" + "fullName='" + fullName + '\'' + '}';
    }
}