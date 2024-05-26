package dao.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class FlightEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long id;
    private String origin;
    private String destination;
    private LocalDateTime departureTime;
    private Integer availableSeats;

    public FlightEntity(Long id, String origin, String destination, LocalDateTime departureTime, Integer availableSeats) {
        this.id = id;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.availableSeats = availableSeats;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlightEntity that = (FlightEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(origin, that.origin) && Objects.equals(destination, that.destination) && Objects.equals(departureTime, that.departureTime) && Objects.equals(availableSeats, that.availableSeats);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, origin, destination, departureTime, availableSeats);
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", departureTime=" + departureTime +
                ", availableSeats=" + availableSeats +
                '}';
    }
}
