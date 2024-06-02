package dao.repository.impl;

import dao.entity.FlightEntity;
import dao.repository.FlightRepository;
import resources.application.properties.PostgresDriver;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FlightPostgresRepository implements FlightRepository {
    public FlightPostgresRepository() {
        createTable();
    }
    @Override
    public void saveAll(List<FlightEntity> flightEntities) {

        String insertQuery = "INSERT INTO flight (id, origin, destination, departure_time, available_seats) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = new PostgresDriver().getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                for (FlightEntity flight : flightEntities) {
                    preparedStatement.setLong(1, flight.getId());
                    preparedStatement.setString(2, flight.getOrigin());
                    preparedStatement.setString(3, flight.getDestination());
                    preparedStatement.setTimestamp(4, Timestamp.valueOf(flight.getDepartureTime()));
                    preparedStatement.setInt(5, flight.getAvailableSeats());

                    preparedStatement.executeUpdate();
                }
                System.out.println("All records were inserted successfully!");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    @Override
    public void save(FlightEntity flightEntity) {
        try (PreparedStatement preparedStatement = new PostgresDriver()
                .getConnection()
                .prepareStatement("INSERT INTO flight (id, origin, destination, departure_time, available_seats) " +
                        "VALUES (?, ?, ?, ?, ?)")) {

            preparedStatement.setLong(1, flightEntity.getId());
            preparedStatement.setString(2, flightEntity.getOrigin());
            preparedStatement.setString(3, flightEntity.getDestination());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(flightEntity.getDepartureTime()));
            preparedStatement.setInt(5, flightEntity.getAvailableSeats());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("New record was inserted successfully!");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    @Override
    public List<FlightEntity> findAll() {
        List<FlightEntity> flightEntities = new ArrayList<>();
        String query = "SELECT * FROM flight";

        try (Connection connection = new PostgresDriver().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                flightEntities.add(new FlightEntity(
                        resultSet.getLong("id"),
                        resultSet.getString("origin"),
                        resultSet.getString("destination"),
                        resultSet.getTimestamp("departure_time").toLocalDateTime(),
                        resultSet.getInt("available_seats")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving flight data", e);
        }

        return flightEntities;
    }
    @Override
    public Optional<FlightEntity> findById(Long id) {

        String query = "SELECT * FROM flight WHERE id = ?";

        try (Connection connection = new PostgresDriver().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            return Optional.of(new FlightEntity(resultSet.getLong("id"), resultSet.getString("origin"), resultSet.getString("destination"), resultSet.getTimestamp("departure_time").toLocalDateTime(), resultSet.getInt("available_seats")));


        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving flight data", e);
        }


    }
    @Override
    public void delete(FlightEntity flight) {
        try (PreparedStatement preparedStatement = new PostgresDriver().getConnection().prepareStatement("DELETE FROM flight WHERE id = ?")) {
            preparedStatement.setLong(1, flight.getId());
            int i = preparedStatement.executeUpdate();
            if (i > 0) {
                System.out.println("Record was deleted!");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void update(Long id, FlightEntity updatedFlightEntity) {
        String sql = "UPDATE flight SET " +
                "origin = ?, " +
                "destination = ?, " +
                "departure_time = ?, " +
                "available_seats = ? " +
                "WHERE id = ?";
        try (Connection connection = new PostgresDriver().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1,updatedFlightEntity.getOrigin());
            preparedStatement.setString(2, updatedFlightEntity.getDestination());
            preparedStatement.setTimestamp(3,Timestamp.valueOf(updatedFlightEntity.getDepartureTime()));
            preparedStatement.setInt(4,updatedFlightEntity.getAvailableSeats());
            preparedStatement.setLong(5, id);
            int i = preparedStatement.executeUpdate();
            if(i > 0){
                System.out.println("A record was updated!");
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
    public void createTable() {
        try {
            int i = new PostgresDriver().getConnection().createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS flight(" +
                    " id bigint primary key ," +
                    " origin varchar(255) not null ," +
                    " destination varchar(255) not null ," +
                    " departure_time timestamp , " +
                    " available_seats int)");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
