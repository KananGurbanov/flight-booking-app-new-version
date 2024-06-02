package service;

import model.dto.BookingDto;

import java.util.List;

public interface BookingService {
    void bookFlight(BookingDto bookingDto);

    List<BookingDto> retrieveAllBookings();

    BookingDto retrieveBooking(Long id);

    void cancelBooking(Long id);

    void updateBooking(Long id, BookingDto updatedBookingDto);
    List<BookingDto> retrieveBookingsByName(String fullName);
}
