import controller.BookingServlet;
import controller.FlightServlet;
import dao.repository.BookingRepository;
import dao.repository.FlightRepository;
import dao.repository.impl.BookingPostgresRepository;
import dao.repository.impl.FlightPostgresRepository;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import service.impl.BookingServiceImpl;
import service.impl.FlightServiceImpl;


public class App {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);
        FlightRepository flightRepository = new FlightPostgresRepository();
        BookingRepository bookingRepository=new BookingPostgresRepository();
        ServletContextHandler handler    = new ServletContextHandler();
        handler.addServlet(new ServletHolder(new FlightServlet(new FlightServiceImpl(flightRepository))), "/flight/*");
        handler.addServlet(new ServletHolder(new BookingServlet(new BookingServiceImpl(bookingRepository,flightRepository))), "/booking/*");

        server.setHandler(handler);

        server.start();
        server.join();
    }
}
