package dao.repository.impl;

import dao.entity.BookingEntity;
import dao.repository.BookingRepository;
import model.Passenger;
import resources.application.properties.PostgresDriver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookingPostgresRepository implements BookingRepository {
    public BookingPostgresRepository() {
        createTables();
    }

    @Override
    public void save(BookingEntity bookingEntity) {
        final String sql1 = "INSERT INTO booking (id, flight_id) VALUES (?, ?)";
        final String sql2 = "INSERT INTO booking_passenger (booking_id, passenger_id, passenger_full_name) VALUES (?, ?, ?)";

        try (Connection connection = new PostgresDriver().getConnection()) {
            try (PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
                 PreparedStatement preparedStatement2 = connection.prepareStatement(sql2)) {

                connection.setAutoCommit(false);

                preparedStatement1.setLong(1, bookingEntity.getId());
                preparedStatement1.setLong(2, bookingEntity.getFlightId());
                preparedStatement1.executeUpdate();

                bookingEntity.getPassengers().forEach(passenger -> {
                    try {
                        preparedStatement2.setLong(1, bookingEntity.getId());
                        preparedStatement2.setLong(2, passenger.getId());
                        preparedStatement2.setString(3, passenger.getFullName());
                        preparedStatement2.executeUpdate();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });

                connection.commit();
            } catch (SQLException e) {
                try {
                    System.err.println("Transaction is being rolled back.");
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(BookingEntity booking) {
        final String sql1 = "DELETE FROM booking WHERE id = ?";
        final String sql2 = "DELETE FROM booking_passenger WHERE booking_id = ?";

        try (Connection connection = new PostgresDriver().getConnection()) {
            try {
                PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
                PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
                connection.setAutoCommit(false);

                preparedStatement2.setLong(1, booking.getId());
                preparedStatement2.executeUpdate();

                preparedStatement1.setLong(1, booking.getId());
                preparedStatement1.executeUpdate();


                connection.commit();
            } catch (SQLException e) {
                try {
                    System.err.println("Transaction is being rolled back.");
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<BookingEntity> findAll() {
        final String sql1 = "SELECT * FROM booking";
        final String sql2 = "SELECT passenger_id, passenger_full_name FROM booking_passenger WHERE booking_id = ?";

        try (Connection connection = new PostgresDriver().getConnection();
             PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
             PreparedStatement preparedStatement2 = connection.prepareStatement(sql2)) {

            List<BookingEntity> bookingEntities = new ArrayList<>();
            ResultSet resultSet = preparedStatement1.executeQuery();

            while (resultSet.next()) {
                Long bookingId = resultSet.getLong("id");
                Long flightId = resultSet.getLong("flight_id");
                List<Passenger> passengers = new ArrayList<>();

                preparedStatement2.setLong(1, bookingId);
                ResultSet resultSet1 = preparedStatement2.executeQuery();

                while (resultSet1.next()) {
                    Passenger passenger = new Passenger();
                    passenger.setId(resultSet1.getLong("passenger_id"));
                    passenger.setFullName(resultSet1.getString("passenger_full_name"));
                    passengers.add(passenger);
                }

                BookingEntity bookingEntity = new BookingEntity(bookingId, flightId, passengers);
                bookingEntities.add(bookingEntity);
            }

            return bookingEntities;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Optional<BookingEntity> findById(Long id) {
        final String sql1 = "SELECT * FROM booking WHERE id = ?";
        final String sql2 = "SELECT passenger_id, passenger_full_name FROM booking_passenger WHERE booking_id = ?";

        try (Connection connection = new PostgresDriver().getConnection();
             PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
             PreparedStatement preparedStatement2 = connection.prepareStatement(sql2)) {

            preparedStatement1.setLong(1, id);
            ResultSet resultSet1 = preparedStatement1.executeQuery();

            if (!resultSet1.next()) {
                return Optional.empty();  // No booking found
            }

            BookingEntity bookingEntity = new BookingEntity(
                    resultSet1.getLong("id"),
                    resultSet1.getLong("flight_id"),
                    new ArrayList<>());

            preparedStatement2.setLong(1, id);
            ResultSet resultSet2 = preparedStatement2.executeQuery();
            while (resultSet2.next()) {
                Passenger passenger = new Passenger(
                        resultSet2.getLong("passenger_id"),
                        resultSet2.getString("passenger_full_name"));
                bookingEntity.getPassengers().add(passenger);
            }

            return Optional.of(bookingEntity);

        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving booking entity", e);
        }


//        final String sql = "SELECT * FROM booking full outer join  booking_passenger on booking.id = booking_passenger.booking_id where id = ?";
//        try (Connection connection = new PostgresDriver().getConnection()) {
//            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//                connection.setAutoCommit(false);
//                preparedStatement.setLong(1, id);
//                ResultSet resultSet = preparedStatement.executeQuery();
//                resultSet.next();
//
//
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }

    }

    @Override
    public void update(Long id, BookingEntity updatedBooking) {
        final String updateBookingSql = "UPDATE booking SET flight_id = ? WHERE id = ?";
        final String deletePassengersSql = "DELETE FROM booking_passenger WHERE booking_id = ?";
        final String insertPassengerSql = "INSERT INTO booking_passenger (booking_id, passenger_id, passenger_full_name) VALUES (?, ?, ?)";

        Connection connection = null;

        try {
            connection = new PostgresDriver().getConnection();
            connection.setAutoCommit(false);
            try (PreparedStatement updateBookingStmt = connection.prepareStatement(updateBookingSql);
                 PreparedStatement deletePassengersStmt = connection.prepareStatement(deletePassengersSql);
                 PreparedStatement insertPassengerStmt = connection.prepareStatement(insertPassengerSql)) {

                updateBookingStmt.setLong(1, updatedBooking.getFlightId());
                updateBookingStmt.setLong(2, id);
                updateBookingStmt.executeUpdate();
                deletePassengersStmt.setLong(1, id);
                deletePassengersStmt.executeUpdate();
                for (Passenger passenger : updatedBooking.getPassengers()) {
                    insertPassengerStmt.setLong(1, id);
                    insertPassengerStmt.setLong(2, passenger.getId());
                    insertPassengerStmt.setString(3, passenger.getFullName());
                    insertPassengerStmt.executeUpdate();
                }

                connection.commit();

            } catch (SQLException e) {
                if (connection != null) {
                    try {
                        System.err.println("Transaction is being rolled back.");
                        connection.rollback();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
                throw new RuntimeException("Error updating booking entity", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error establishing connection", e);
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);  // Reset auto-commit to its default state
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }


    public void createTables() {
        try {
            int i = new PostgresDriver().getConnection().createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS booking(" +
                    " id bigint primary key ," +
                    " flight_id bigint," +
                    " foreign key (flight_id) references flight(id))");
            int i1 = new PostgresDriver().getConnection().createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS booking_passenger(" +
                    " booking_id bigint ," +
                    " passenger_id bigint," +
                    " passenger_full_name varchar(65)," +
                    " foreign key (booking_id) references booking(id))");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
