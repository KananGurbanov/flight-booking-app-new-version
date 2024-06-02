package service;

import model.dto.FlightDto;

import java.time.LocalDateTime;
import java.util.List;

public interface FlightService {
    void createFlight(FlightDto flightDto);

    void createFlights(List<FlightDto> flightDtos);

    FlightDto retrieveFlight(Long id);

    List<FlightDto> retrieveAllFlights();

    void removeFlight(Long id);

    void updateFlight(Long id, FlightDto updatedFlightDto);

    List<FlightDto> displayOnlineBoard();


    FlightDto searchFlight(String destination, LocalDateTime departureTime);

}
