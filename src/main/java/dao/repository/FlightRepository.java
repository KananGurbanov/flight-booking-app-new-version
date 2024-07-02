package dao.repository;

import dao.entity.FlightEntity;

import java.util.List;
import java.util.Optional;

public interface FlightRepository extends Repository<FlightEntity> {
    void saveAll(List<FlightEntity> flightEntities);

    void save(FlightEntity flightEntity);

    List<FlightEntity> findAll();

    Optional<FlightEntity> findById(Long id);

    void delete(FlightEntity flight);

    void update(Long id, FlightEntity updatedFlightEntity);
}
