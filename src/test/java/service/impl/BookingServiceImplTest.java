package service.impl;

import dao.repository.BookingRepository;
import dao.repository.FlightRepository;
import exceptions.BookingNotFoundException;
import exceptions.FlightNotFoundException;
import exceptions.NullBookingException;
import exceptions.NullBookingIdException;
import mapper.BookingMapper;
import mapper.FlightMapper;
import model.Passenger;
import model.dto.BookingDto;
import model.dto.FlightDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.BookingService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class BookingServiceImplTest {

    BookingRepository bookingRepository;

    FlightRepository flightRepository;

    BookingService bookingService;

    @BeforeEach
    void setUp() {
        bookingRepository = mock(BookingRepository.class);
        flightRepository = mock(FlightRepository.class);
        bookingService = new BookingServiceImpl(bookingRepository, flightRepository);
    }

    @Test
    void bookFlight_Success() {
        BookingDto bookingDto = new BookingDto(1L, new ArrayList<>());
        FlightDto flightDto = new FlightDto(1L, "Baku", "Moscow", LocalDateTime.of(2024, 5, 24, 15, 30), 120);
        when(flightRepository.findById(bookingDto.getFlightId())).thenReturn(Optional.of(FlightMapper.toEntity(flightDto)));
        bookingService.bookFlight(bookingDto);
        verify(flightRepository, times(3)).findById(bookingDto.getFlightId());
        verify(bookingRepository, times(1)).save(BookingMapper.toEntity(bookingDto));
    }

    @Test
    void bookFlight_UnSuccess1() {
        BookingDto bookingDto = new BookingDto(1L, new ArrayList<>());
        when(flightRepository.findById(bookingDto.getFlightId())).thenReturn(Optional.empty());
        assertThrows(FlightNotFoundException.class, () -> bookingService.bookFlight(bookingDto));
        verify(flightRepository, times(1)).findById(bookingDto.getFlightId());
        verify(bookingRepository, never()).save(BookingMapper.toEntity(bookingDto));
    }

    @Test
    void bookFlight_UnSuccess2() {
        BookingDto bookingDto = null;
        assertThrows(NullBookingException.class, () -> bookingService.bookFlight(null));
        verify(flightRepository, never()).findById(anyLong());
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void retrieveAllBookings_Success() {
        List<BookingDto> bookingDtos = List.of(new BookingDto(1L, 1L, new ArrayList<>()), new BookingDto(2L, 2L, List.of(new Passenger("Kanan"))));
        when(bookingRepository.findAll()).thenReturn(BookingMapper.toEntityList(bookingDtos));
        List<BookingDto> bookingDtos1 = bookingService.retrieveAllBookings();
        assertEquals(bookingDtos1, bookingDtos);
        verify(bookingRepository, times(1)).findAll();
    }

    @Test
    void retrieveBooking_Success() {
        BookingDto bookingDto = new BookingDto(1L, 1L, new ArrayList<>());
        when(bookingRepository.findById(bookingDto.getId())).thenReturn(Optional.of(BookingMapper.toEntity(bookingDto)));
        BookingDto bookingDto1 = bookingService.retrieveBooking(bookingDto.getId());
        assertEquals(bookingDto1, bookingDto);
        verify(bookingRepository, times(1)).findById(bookingDto.getId());
    }

    @Test
    void retrieveBooking_UnSuccess1() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(BookingNotFoundException.class, () -> bookingService.retrieveBooking(anyLong()));
        verify(bookingRepository, times(1)).findById(anyLong());
    }

    @Test
    void retrieveBooking_UnSuccess2() {
        assertThrows(NullBookingIdException.class, () -> bookingService.retrieveBooking(null));
        verify(bookingRepository, never()).findById(null);
    }


    @Test
    void cancelBooking_Success() {
        BookingDto bookingDto1 = new BookingDto(2L, List.of(new Passenger("Kanan")));
        FlightDto flightDto = new FlightDto(2L, "Baku", "Moscow", LocalDateTime.of(2024, 5, 24, 15, 30), 120);
        when(bookingRepository.findById(bookingDto1.getId())).thenReturn(Optional.of(BookingMapper.toEntity(bookingDto1)));
        when(flightRepository.findById(bookingDto1.getFlightId())).thenReturn(Optional.of(FlightMapper.toEntity(flightDto)));
        bookingService.cancelBooking(bookingDto1.getId());
        verify(bookingRepository, times(1)).findById(bookingDto1.getId());
        verify(bookingRepository, times(1)).delete(BookingMapper.toEntity(bookingDto1));
    }

    @Test
    void cancelBooking_UnSuccess1(){
        assertThrows(NullBookingIdException.class, ()->bookingService.cancelBooking(null));
        verify(bookingRepository, never()).delete(any());
    }


    @Test
    void cancelBooking_UnSuccess2(){
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(BookingNotFoundException.class, ()->bookingService.cancelBooking(anyLong()));
        verify(bookingRepository, never()).delete(any());
    }

    @Test
    void updateBooking() {
        BookingDto bookingDto = new BookingDto(1L, new ArrayList<>());
        BookingDto bookingDto1 = new BookingDto(2L, List.of(new Passenger("Kanan")));
        when(bookingRepository.findById(bookingDto.getId())).thenReturn(Optional.of(BookingMapper.toEntity(bookingDto)));
        bookingService.updateBooking(bookingDto.getId(), bookingDto1);
        verify(bookingRepository, times(1)).findById(bookingDto.getId());
        verify(bookingRepository, times(1)).update(bookingDto.getId(), BookingMapper.toEntity(bookingDto1));
    }

    @Test
    void retrieveBookingsByName() {
        String name = "Kanan";
        List<BookingDto> bookingDtos = List.of(new BookingDto(1L, 1L, new ArrayList<>()), new BookingDto(2L, 2L, List.of(new Passenger("Selen"))), new BookingDto(3L, 2L, List.of(new Passenger("Kanan"))));
        when(bookingRepository.findAll()).thenReturn(BookingMapper.toEntityList(bookingDtos));
        List<BookingDto> bookingDtos1 = bookingService.retrieveBookingsByName(name);
        assertEquals(bookingDtos1, List.of(bookingDtos.get(2)));
        verify(bookingRepository, times(1)).findAll();
    }
}