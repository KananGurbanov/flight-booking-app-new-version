package service.impl;

import dao.entity.FlightEntity;
import dao.repository.FlightRepository;
import exceptions.FlightNotFoundException;
import exceptions.NullFlightException;
import exceptions.NullFlightIdException;
import mapper.FlightMapper;
import model.dto.FlightDto;
import org.apache.commons.collections4.CollectionUtils;
import service.FlightService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class FlightServiceImpl implements FlightService {
    private final FlightRepository flightRepository;

    public FlightServiceImpl(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public void createFlight(FlightDto flightDto) {
        if (flightDto == null) throw new NullFlightException("Flight cannot be null!");
        flightRepository.save(FlightMapper.toEntity(flightDto));
    }

    @Override
    public void createFlights(List<FlightDto> flightDtos) {
        if (CollectionUtils.isEmpty(flightDtos))
            throw new NullFlightException("List is null or empty!");
        flightRepository.saveAll(FlightMapper.toEntityList(flightDtos));
    }

    @Override
    public FlightDto retrieveFlight(Long id) {
        if (id == null) throw new RuntimeException("Id cannot be null");
        return FlightMapper.toDto(flightRepository.findById(id).orElseThrow(() -> new FlightNotFoundException("Flight was not found!")));
    }

    @Override
    public List<FlightDto> retrieveAllFlights() {
        return FlightMapper.toDtoList(flightRepository.findAll());
    }

    @Override
    public void removeFlight(Long id) {
        if (id == null) throw new NullFlightIdException("Id cannot be null");
        else {
            FlightDto dto = FlightMapper.toDto(flightRepository.findById(id).orElseThrow(() -> new FlightNotFoundException("Flight was not found!")));
            flightRepository.delete(FlightMapper.toEntity(dto));
        }
    }

    @Override
    public void updateFlight(Long id, FlightDto updatedFlightDto) {
        if (updatedFlightDto == null) throw new NullFlightException("Updated flight cannot be null!");
        if (id == null) throw new NullFlightIdException("Flight Id cannot be null!");
        if (flightRepository.findById(id).isEmpty()) throw new FlightNotFoundException("Flight was not found!");
        flightRepository.update(id, FlightMapper.toEntity(updatedFlightDto));
    }

    @Override
    public List<FlightDto> displayOnlineBoard() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime next24hours = now.plusHours(24);
        return FlightMapper.
                toDtoList(flightRepository.
                        findAll().
                        stream().
                        filter(
                                flight -> flight.getOrigin().equalsIgnoreCase("Kiev") &&
                                        flight.getDepartureTime().isAfter(now) &&
                                        flight.getDepartureTime().isBefore(next24hours)).
                        toList());
    }

    @Override
    public FlightDto searchFlight(String destination, LocalDateTime departureTime) {
        if (destination == null || departureTime == null) throw new RuntimeException("Search credentials cannot be null!");
        Optional<FlightEntity> flightEntity = flightRepository.findAll().
                stream().
                filter(flight -> flight.getDestination().equalsIgnoreCase(destination) && flight.getDepartureTime().isEqual(departureTime)).
                findFirst();
        if (flightEntity.isEmpty()) throw new FlightNotFoundException("Flight was not found!");
        return FlightMapper.toDto(flightEntity.get());
    }
}
