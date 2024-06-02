package dao.repository.impl;

import dao.entity.BookingEntity;
import dao.repository.BookingRepository;
import resources.application.properties.Constants;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookingInFileRepository implements BookingRepository {


    private final List<BookingEntity> bookings;

    public BookingInFileRepository() {
        this.bookings = loadBookingsFromFile();
    }

    @Override
    public void save(BookingEntity bookingEntity) {
        bookings.add(bookingEntity);
        saveBookingsToFile();
    }

    @Override
    public void delete(BookingEntity booking) {
        bookings.remove(booking);
        saveBookingsToFile();
    }

    @Override
    public List<BookingEntity> findAll() {
        return new ArrayList<>(bookings); // Return a copy to prevent modification
    }

    @Override
    public Optional<BookingEntity> findById(Long id) {
        return bookings.stream().filter(x -> x.getId().equals(id)).findFirst();
    }

    @Override
    public void update(Long id, BookingEntity updatedBooking) {
        BookingEntity bookingEntity = findById(id).get();
        bookingEntity.setFlightId(updatedBooking.getFlightId());
        bookingEntity.setPassengers(updatedBooking.getPassengers());
        saveBookingsToFile();
    }

    private List<BookingEntity> loadBookingsFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Constants.BOOKING_FILE))) {
            return (List<BookingEntity>) ois.readObject();
        } catch (FileNotFoundException e) {
            return new ArrayList<>(); // Return empty list if file not found
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to load bookings from file", e);
        }
    }

    private void saveBookingsToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Constants.BOOKING_FILE))) {
            oos.writeObject(bookings);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save bookings to file", e);
        }
    }
}
