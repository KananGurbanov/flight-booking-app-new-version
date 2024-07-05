package service.impl;

import dao.entity.FlightEntity;
import dao.repository.FlightRepository;
import exceptions.FlightNotFoundException;
import exceptions.NullBookingIdException;
import exceptions.NullFlightException;
import exceptions.NullFlightIdException;
import mapper.FlightMapper;
import model.dto.FlightDto;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.FlightService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class FlightServiceImpl implements FlightService {
    private final FlightRepository flightRepository;
    private static final Logger log = LoggerFactory.getLogger(FlightServiceImpl.class);

    public FlightServiceImpl(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public void createFlight(FlightDto flightDto) {
        if (flightDto == null) throw new NullFlightException("Flight cannot be null!");
        flightRepository.save(FlightMapper.toEntity(flightDto));
        log.info("Flight created {}", flightDto );
    }

    @Override
    public void createFlights(List<FlightDto> flightDtos) {
        if (CollectionUtils.isEmpty(flightDtos))
            throw new NullFlightException("List is null or empty!");
        flightRepository.saveAll(FlightMapper.toEntityList(flightDtos));
        log.info("Flights created {}", flightDtos);
    }

    @Override
    public FlightDto retrieveFlight(Long id) {
        if (id == null) throw new NullFlightIdException("Id cannot be null");
        FlightEntity flightEntity = flightRepository.findById(id).orElseThrow(() -> new FlightNotFoundException("Flight was not found!"));
        log.info("Flight retrieved {}", flightEntity);
        return FlightMapper.toDto(flightEntity);
    }

    @Override
    public List<FlightDto> retrieveAllFlights() {
        List<FlightEntity> all = flightRepository.findAll();
        log.info("Flights retrieved {}", all);
        return FlightMapper.toDtoList(all);
    }

    @Override
    public void removeFlight(Long id) {
        if (id == null) throw new NullFlightIdException("Id cannot be null");
        FlightEntity flightEntity = flightRepository.findById(id).orElseThrow(() -> new FlightNotFoundException("Flight was not found!"));
        log.info("Flight to cancel {}", flightEntity);
        flightRepository.delete(flightEntity);
        log.info("Flight removed!");
    }

    @Override
    public void updateFlight(Long id, FlightDto updatedFlightDto) {
        if (updatedFlightDto == null) throw new NullFlightException("Updated flight cannot be null!");
        if (id == null) throw new NullFlightIdException("Flight Id cannot be null!");
        FlightEntity flightEntity = flightRepository.findById(id).orElseThrow(() -> new FlightNotFoundException("Flight was not found!"));
        log.info("Flight to update {}", flightEntity);
        flightRepository.update(id, FlightMapper.toEntity(updatedFlightDto));
        log.info("Flight updated!");
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
        FlightEntity flightEntity = flightRepository.findAll().
                stream().
                filter(flight -> flight.getDestination().equalsIgnoreCase(destination) && flight.getDepartureTime().isEqual(departureTime)).
                findFirst().orElseThrow(() -> new FlightNotFoundException("Flight was not found!"));
        log.info("Flight searched {}", flightEntity);
        return FlightMapper.toDto(flightEntity);
    }
}
