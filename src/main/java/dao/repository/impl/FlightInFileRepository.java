package dao.repository.impl;

import dao.entity.FlightEntity;
import dao.repository.FlightRepository;
import resources.application.properties.Constants;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FlightInFileRepository implements FlightRepository {
    private final List<FlightEntity> flightDatabase;

    public FlightInFileRepository() {
        flightDatabase = loadFromFile();
    }

    @Override
    public void saveAll(List<FlightEntity> flights) {
        flightDatabase.addAll(flights);
        saveToFile();
    }

    @Override
    public void save(FlightEntity flightEntity) {
        flightDatabase.add(flightEntity);
        saveToFile();
    }

    @Override
    public List<FlightEntity> findAll() {
        return new ArrayList<>(flightDatabase);
    }

    @Override
    public Optional<FlightEntity> findById(Long id) {
        return flightDatabase.stream()
                .filter(flightEntity -> flightEntity.getId().equals(id))
                .findFirst();
    }

    @Override
    public void delete(FlightEntity flightEntity) {
        flightDatabase.remove(flightEntity);
        saveToFile();
    }

    @Override
    public void update(Long id, FlightEntity updatedFlightEntity) {
        Optional<FlightEntity> flightEntityOptional = findById(id);
        FlightEntity flightEntity = flightEntityOptional.get();
        flightEntity.setOrigin(updatedFlightEntity.getOrigin());
        flightEntity.setDestination(updatedFlightEntity.getDestination());
        flightEntity.setDepartureTime(updatedFlightEntity.getDepartureTime());
        flightEntity.setAvailableSeats(updatedFlightEntity.getAvailableSeats());
        saveToFile();

    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Constants.FLIGHT_FILE))) {
            oos.writeObject(flightDatabase);
        } catch (IOException e) {
            throw new RuntimeException("Error saving flights to file", e);
        }
    }

    private List<FlightEntity> loadFromFile() {
        File file = new File(Constants.FLIGHT_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Constants.FLIGHT_FILE))) {
            return (List<FlightEntity>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error loading flights from file", e);
        }
    }
}
