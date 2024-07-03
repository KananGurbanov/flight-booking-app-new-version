package service.impl;

import dao.entity.BookingEntity;
import dao.entity.FlightEntity;
import dao.repository.BookingRepository;
import dao.repository.FlightRepository;
import dao.repository.impl.FlightPostgresRepository;
import exceptions.*;
import mapper.BookingMapper;
import mapper.FlightMapper;
import model.dto.BookingDto;
import service.BookingService;

import java.util.List;

public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final FlightRepository flightRepository;

    public BookingServiceImpl(BookingRepository bookingRepository, FlightRepository flightRepository) {
        this.bookingRepository = bookingRepository;
        this.flightRepository = flightRepository;
    }

    @Override
    public void bookFlight(BookingDto bookingDto) {
        if (bookingDto == null) throw new NullBookingException("Booking cannot be null!");
        FlightEntity flightEntity = flightRepository.findById(bookingDto.getFlightId()).orElseThrow(() -> new FlightNotFoundException("Flight not found!"));
        bookingRepository.save(BookingMapper.toEntity(bookingDto));
        flightEntity.setAvailableSeats(flightEntity.getAvailableSeats() - bookingDto.getPassengers().size());
        flightRepository.update(flightEntity.getId(), flightEntity);
    }

    @Override
    public List<BookingDto> retrieveAllBookings() {
        return BookingMapper.toDtoList(bookingRepository.findAll());
    }

    @Override
    public BookingDto retrieveBooking(Long id) {
        if (id == null) throw new NullBookingIdException("Booking id cannot be null!");
        return BookingMapper.toDto(bookingRepository.
                findById(id).
                orElseThrow(() -> new BookingNotFoundException("Booking not found!")));
    }

    @Override
    public void cancelBooking(Long id) {
        if (id == null) throw new NullBookingIdException("Booking id cannot be null!");
        BookingEntity bookingEntity = bookingRepository.
                findById(id).
                orElseThrow(() -> new BookingNotFoundException("Booking not found!"));
        bookingRepository.delete(bookingEntity);
        FlightEntity flightEntity = flightRepository.findById(bookingEntity.getFlightId()).get();
        flightEntity.setAvailableSeats(flightEntity.getAvailableSeats() + bookingEntity.getPassengers().size());
        flightRepository.update(flightEntity.getId(), flightEntity);
    }

    @Override
    public void updateBooking(Long id, BookingDto updatedBooking) {
        if (updatedBooking == null) throw new NullBookingException("Updated booking cannot be null!");
        if (id == null) throw new NullBookingIdException("Booking Id cannot be null!");
        if (bookingRepository.findById(id).isEmpty()) throw new BookingNotFoundException("Booking was not found!");
        bookingRepository.update(id, BookingMapper.toEntity(updatedBooking));
    }

    @Override
    public List<BookingDto> retrieveBookingsByName(String fullName) {
        return BookingMapper.toDtoList(bookingRepository.
                findAll().
                stream().
                filter(booking -> booking.getPassengers().
                        stream().
                        anyMatch(passenger -> passenger.getFullName().equalsIgnoreCase(fullName))).
                toList());
    }

}