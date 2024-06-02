import controller.BookingController;
import controller.FlightController;
import dao.repository.impl.BookingPostgresRepository;
import dao.repository.impl.FlightPostgresRepository;
import model.dto.FlightDto;
import service.impl.BookingServiceImpl;
import service.impl.FlightServiceImpl;

import java.time.LocalDateTime;
import java.util.List;

public class App {
    public static void main(String[] args) {
        FlightPostgresRepository flightRepository = new FlightPostgresRepository();
        FlightServiceImpl flightService = new FlightServiceImpl(flightRepository);
        FlightController flightController = new FlightController(flightService);

        BookingPostgresRepository bookingRepository = new BookingPostgresRepository();
        BookingServiceImpl bookingService = new BookingServiceImpl(bookingRepository, flightRepository);
        BookingController bookingController = new BookingController(bookingService);

        console.ConsoleApp consoleApp = new console.ConsoleApp(flightController, bookingController);
        consoleApp.start();


    }
}
