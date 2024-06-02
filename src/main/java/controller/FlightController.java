package controller;

import model.dto.BookingDto;
import model.dto.FlightDto;
import service.FlightService;

import java.time.LocalDateTime;
import java.util.List;

public class FlightController {
    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    public void createFlight(FlightDto flightDto) {
        flightService.createFlight(flightDto);
    }

    public void createFlights(List<FlightDto> flightDtos) {
        flightService.createFlights(flightDtos);
    }

    public FlightDto retrieveFlight(Long id) {
        return flightService.retrieveFlight(id);
    }


    public List<FlightDto> retrieveAllFlights() {
        return flightService.retrieveAllFlights();
    }


    public void removeFlight(Long id) {
        flightService.removeFlight(id);
    }


    public void updateFlight(Long id, FlightDto updatedFlightDto) {
        flightService.updateFlight(id, updatedFlightDto);
    }

    public List<FlightDto> displayOnlineBoard() {
        return flightService.displayOnlineBoard();
    }

    public FlightDto searchFlight(String destination, LocalDateTime departureTime) {
        return flightService.searchFlight(destination, departureTime);
    }

}
