package model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.Objects;

public class FlightDto {
    private static Long ID = 0L;
    private Long id;
    private String origin;
    private String destination;
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm")
    private LocalDateTime departureTime;
    private Integer availableSeats;
    public FlightDto(String origin, String destination, LocalDateTime departureTime, Integer availableSeats) {
        if(availableSeats<0){
            throw new IllegalArgumentException("Num of seats cannot be below 0");
        }
        this.id = ++ID;
        this.origin = Objects.requireNonNull(origin, "Origin cannot be null");
        this.destination = Objects.requireNonNull(destination, "Destination cannot be null");
        this.departureTime = Objects.requireNonNull(departureTime, "Departure time cannot be null");
        this.availableSeats = Objects.requireNonNull(availableSeats, "Number of seats cannot be null");
    }

    public FlightDto(Long id, String origin, String destination, LocalDateTime departureTime, Integer availableSeats) {
        this.id = id;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.availableSeats = availableSeats;
    }

    public FlightDto() {
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
        FlightDto flightDto = (FlightDto) o;
        return Objects.equals(id, flightDto.id) && Objects.equals(origin, flightDto.origin) && Objects.equals(destination, flightDto.destination) && Objects.equals(departureTime, flightDto.departureTime) && Objects.equals(availableSeats, flightDto.availableSeats);
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
