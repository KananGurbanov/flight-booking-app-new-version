package dao.repository.impl;

import dao.entity.FlightEntity;
import dao.repository.FlightRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FlightInMemoryRepository implements FlightRepository {


    private final List<FlightEntity> flightEntities;

    public FlightInMemoryRepository() {
        this.flightEntities = new ArrayList<>();
    }

    @Override
    public void saveAll(List<FlightEntity> flights) {
        flightEntities.addAll(flights);
    }

    @Override
    public void save(FlightEntity flightEntity) {
        flightEntities.add(flightEntity);
    }

    @Override
    public List<FlightEntity> findAll() {
        return new ArrayList<>(flightEntities);
    }

    @Override
    public Optional<FlightEntity> findById(Long id) {
        return flightEntities.stream().filter(flightEntity -> flightEntity.getId().equals(id)).findFirst();
    }

    @Override
    public void delete(FlightEntity flight) {
        flightEntities.remove(flight);
    }

    @Override
    public void update(Long id, FlightEntity updatedFlightEntity) {
        Optional<FlightEntity> flightEntityOptional = findById(id);
        FlightEntity flightEntity = flightEntityOptional.get();
        flightEntity.setOrigin(updatedFlightEntity.getOrigin());
        flightEntity.setDepartureTime(updatedFlightEntity.getDepartureTime());
        flightEntity.setDestination(updatedFlightEntity.getDestination());
        flightEntity.setAvailableSeats(updatedFlightEntity.getAvailableSeats());
    }



}
