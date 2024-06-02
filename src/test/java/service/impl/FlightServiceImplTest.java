package service.impl;

import dao.repository.FlightRepository;
import mapper.FlightMapper;
import model.dto.FlightDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import service.FlightService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


class FlightServiceImplTest {

    private FlightRepository flightRepository;
    private FlightService flightService;

    @BeforeEach
    void setUp() {
        flightRepository = Mockito.mock(FlightRepository.class);
        flightService = new FlightServiceImpl(flightRepository);
    }
    @Test
    void createFlight_Success() {
        FlightDto flightDto = new FlightDto("Baku", "Moscow", LocalDateTime.of(2024, 5, 24, 15, 30), 120);
        flightService.createFlight(flightDto);
        verify(flightRepository, times(1)).save(FlightMapper.toEntity(flightDto));
    }

    @Test
    void createFlight_UnSuccess1() {
        FlightDto flightDto = null;
        assertThrows(RuntimeException.class, () -> flightService.createFlight(flightDto));
        verify(flightRepository, never()).save(any());
    }

    @Test
    void createFlights_Success() {
        List<FlightDto> flightDtos = List.of(new FlightDto("Baku", "Moscow", LocalDateTime.of(2024, 5, 24, 15, 30), 120));
        flightService.createFlights(flightDtos);
        verify(flightRepository, times(1)).saveAll(FlightMapper.toEntityList(flightDtos));
    }

    @Test
    void createFlights_UnSuccess1() {
        List<FlightDto> flightDtos = new ArrayList<>();
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> flightService.createFlights(flightDtos));
        assertEquals("List is null or empty!", runtimeException.getMessage());
        verify(flightRepository, never()).saveAll(FlightMapper.toEntityList(flightDtos));
    }

    @Test
    void createFlights_UnSuccess2() {
        List<FlightDto> flightDtos = null;
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> flightService.createFlights(flightDtos));
        assertEquals("List is null or empty!", runtimeException.getMessage());
        verify(flightRepository, never()).saveAll(anyList());
    }

    @Test
    void retrieveFlight_Success() {
        FlightDto flightDto = new FlightDto(1L, "Baku", "Moscow", LocalDateTime.of(2024, 5, 24, 15, 30), 120);
        when(flightRepository.findById(flightDto.getId())).thenReturn(Optional.of(FlightMapper.toEntity(flightDto)));
        FlightDto flightDto1 = flightService.retrieveFlight(flightDto.getId());
        assertEquals(flightDto, flightDto1);
    }

    @Test
    void retrieveFlight_UnSuccess1() {
        when(flightRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> flightService.retrieveFlight(anyLong()));
    }

    @Test
    void retrieveFlight_UnSuccess2() {
        assertThrows(RuntimeException.class, () -> flightService.retrieveFlight(anyLong()));
    }

    @Test
    void retrieveAllFlights() {
        List<FlightDto> flightDtos = flightService.retrieveAllFlights();
        verify(flightRepository, times(1)).findAll();
    }

    @Test
    void removeFlight_Success() {
        FlightDto flightDto = new FlightDto(1L, "Baku", "Moscow", LocalDateTime.of(2024, 5, 24, 15, 30), 120);
        when(flightRepository.findById(flightDto.getId())).thenReturn(Optional.of(FlightMapper.toEntity(flightDto)));
        flightService.removeFlight(flightDto.getId());
        verify(flightRepository, times(1)).delete(FlightMapper.toEntity(flightDto));
    }

    @Test
    void removeFlight_UnSuccess1() {
        Long id = null;
        assertThrows(RuntimeException.class, () -> flightService.removeFlight(id));
        verify(flightRepository, never()).delete(any());
    }

    @Test
    void removeFlight_UnSuccess2() {
        FlightDto flightDto = new FlightDto(1L, "Baku", "Moscow", LocalDateTime.of(2024, 5, 24, 15, 30), 120);
        when(flightRepository.findById(flightDto.getId())).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> flightService.removeFlight(flightDto.getId()));
        verify(flightRepository, never()).delete(FlightMapper.toEntity(flightDto));
    }

    @Test
    void updateFlight_Success() {
        FlightDto flightDto = new FlightDto(1L, "Baku", "Moscow", LocalDateTime.of(2024, 5, 24, 15, 30), 120);
        when(flightRepository.findById(222L)).thenReturn(Optional.of(FlightMapper.toEntity(flightDto)));
        flightService.updateFlight(222L, flightDto);
        verify(flightRepository, times(1)).update(222L, FlightMapper.toEntity(flightDto));
    }

    @Test
    void updateFlight_UnSuccess1() {
        FlightDto flightDto = new FlightDto(1L, "Baku", "Moscow", LocalDateTime.of(2024, 5, 24, 15, 30), 120);
        when(flightRepository.findById(222L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> flightService.updateFlight(222L, flightDto));
        verify(flightRepository, never()).update(222L, FlightMapper.toEntity(flightDto));
    }

    @Test
    void updateFlight_UnSuccess2() {
        FlightDto flightDto = null;
        assertThrows(RuntimeException.class, () -> flightService.updateFlight(222L, flightDto));
        verify(flightRepository, never()).update(eq(222L), any());
    }

    @Test
    void updateFlight_UnSuccess3() {
        FlightDto flightDto = new FlightDto(1L, "Baku", "Moscow", LocalDateTime.of(2024, 5, 24, 15, 30), 120);
        Long id = null;
        assertThrows(RuntimeException.class, () -> flightService.updateFlight(id, flightDto));
        verify(flightRepository, never()).update(any(), eq(FlightMapper.toEntity(flightDto)));
    }

    @Test
    void displayOnlineBoard_Success() {
        ArrayList<FlightDto> objects = new ArrayList<>();
        objects.add(new FlightDto("Kiev", "Baku", LocalDateTime.of(2024, 6, 2, 15, 30), 120));
        objects.add(new FlightDto("Moscow", "Baku", LocalDateTime.of(2024, 6, 3, 15, 30), 120));
        when(flightRepository.findAll()).thenReturn(FlightMapper.toEntityList(objects));
        assertEquals(List.of(objects.get(0)), flightService.displayOnlineBoard());
    }

    @Test
    void searchFlight_Success() {
        ArrayList<FlightDto> objects = new ArrayList<>();
        objects.add(new FlightDto("Kiev", "Baku", LocalDateTime.of(2024, 5, 31, 15, 30), 120));
        objects.add(new FlightDto("Moscow", "Baku", LocalDateTime.of(2024, 6, 3, 15, 30), 120));
        when(flightRepository.findAll()).thenReturn(FlightMapper.toEntityList(objects));
        assertEquals(objects.get(0), flightService.searchFlight("Baku", LocalDateTime.of(2024, 5, 31, 15, 30)));
    }

    @Test
    void searchFlight_UnSuccess() {
        String destination = null;
        assertThrows(RuntimeException.class, () -> flightService.searchFlight(destination, LocalDateTime.now()));
    }
}