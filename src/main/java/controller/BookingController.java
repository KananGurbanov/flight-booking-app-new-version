package controller;

import model.dto.BookingDto;
import service.BookingService;

import java.util.List;

public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    public void bookFlight(BookingDto bookingDto) {
        bookingService.bookFlight(bookingDto);
    }

    public List<BookingDto> retrieveAllBookings() {
        return bookingService.retrieveAllBookings();
    }

    public BookingDto retrieveBooking(Long id) {
        return bookingService.retrieveBooking(id);
    }

    public void updateBooking(Long id, BookingDto updatedBookingDto) {
        bookingService.updateBooking(id, updatedBookingDto);
    }

    public void cancelBooking(Long id) {
        bookingService.cancelBooking(id);
    }

    public List<BookingDto> retrieveBookingsByName(String fullName) {
        return bookingService.retrieveBookingsByName(fullName);
    }
}