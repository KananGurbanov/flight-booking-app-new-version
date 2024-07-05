package service.impl;

import dao.entity.BookingEntity;
import dao.entity.FlightEntity;
import dao.repository.BookingRepository;
import dao.repository.FlightRepository;
import exceptions.BookingNotFoundException;
import exceptions.FlightNotFoundException;
import exceptions.NullBookingException;
import exceptions.NullBookingIdException;
import mapper.BookingMapper;
import model.dto.BookingDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.BookingService;

import java.util.List;

public class BookingServiceImpl implements BookingService {
    private static final Logger log = LoggerFactory.getLogger(BookingServiceImpl.class);
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

        log.info("Booked flight {}", flightEntity);
        bookingRepository.save(BookingMapper.toEntity(bookingDto));
        log.info("Booking saved!");

        bookingRepository.save(BookingMapper.toEntity(bookingDto));

        flightEntity.setAvailableSeats(flightEntity.getAvailableSeats() - bookingDto.getPassengers().size());
        flightRepository.update(flightEntity.getId(), flightEntity);
    }

    @Override
    public List<BookingDto> retrieveAllBookings() {
        List<BookingDto> dtoList = BookingMapper.toDtoList(bookingRepository.findAll());
        return dtoList;
    }

    @Override
    public BookingDto retrieveBooking(Long id) {
        if (id == null) throw new NullBookingIdException("Booking id cannot be null!");
        BookingEntity bookingEntity = bookingRepository.
        return BookingMapper.toDto(bookingRepository.

                findById(id).
                orElseThrow(() -> new BookingNotFoundException("Booking not found!"));
        log.info("Booking retrieved {}", bookingEntity);
        BookingDto dto = BookingMapper.toDto(bookingEntity);

        return dto;
    }

    @Override
    public void cancelBooking(Long id) {
        if (id == null) throw new NullBookingIdException("Booking id cannot be null!");
        BookingEntity bookingEntity = bookingRepository.
                findById(id).
                orElseThrow(() -> new BookingNotFoundException("Booking not found!"));
        log.info("Booking to cancel {}", bookingEntity);
        bookingRepository.delete(bookingEntity);

        log.info("Booking deleted!");

        FlightEntity flightEntity = flightRepository.findById(bookingEntity.getFlightId()).get();
        flightEntity.setAvailableSeats(flightEntity.getAvailableSeats() + bookingEntity.getPassengers().size());
        flightRepository.update(flightEntity.getId(), flightEntity);
    }

    @Override
    public void updateBooking(Long id, BookingDto updatedBooking) {
        if (updatedBooking == null) throw new NullBookingException("Updated booking cannot be null!");
        if (id == null) throw new NullBookingIdException("Booking Id cannot be null!");
        BookingEntity bookingEntity = bookingRepository.findById(id).orElseThrow(() -> new BookingNotFoundException("Booking was not found!"));
        log.info("Booking to update {}", bookingEntity);
        bookingRepository.update(id, BookingMapper.toEntity(updatedBooking));
        log.info("Booking updated!");
    }

    @Override
    public List<BookingDto> retrieveBookingsByName(String fullName) {
        List<BookingEntity> list = bookingRepository.
                findAll().
                stream().
                filter(booking -> booking.getPassengers().
                        stream().
                        anyMatch(passenger -> passenger.getFullName().equalsIgnoreCase(fullName))).
                toList();
        log.info("Bookings retrieved {}", list);
        return BookingMapper.toDtoList(list);

    }

}