package dao.repository.impl;

import dao.entity.BookingEntity;
import dao.repository.BookingRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookingInMemoryRepository implements BookingRepository {

    private static List<BookingEntity> bookings;

    public BookingInMemoryRepository() {
        bookings = new ArrayList<>();
    }


    @Override
    public void save(BookingEntity bookingEntity) {
        bookings.add(bookingEntity);
    }

    @Override
    public void delete(BookingEntity booking) {
        bookings.remove(booking);
    }

    @Override
    public List<BookingEntity> findAll() {
        return bookings;
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
    }
}