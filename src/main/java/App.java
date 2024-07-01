import controller.FlightServlet;
import dao.repository.FlightRepository;
import dao.repository.impl.FlightPostgresRepository;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import service.impl.FlightServiceImpl;


public class App {
    public static void main(String[] args) throws Exception {
        Server server = new Server(9000);
        FlightRepository flightRepository = new FlightPostgresRepository();
        ServletContextHandler handler    = new ServletContextHandler();
        handler.addServlet(new ServletHolder(new FlightServlet(new FlightServiceImpl(flightRepository))), "/flight/*");
        server.setHandler(handler);

        server.start();
        server.join();
    }
}
