package controller;

import service.FlightService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FlightServlet extends HttpServlet {
    private final FlightServiceHandler flightServiceHandler;

    public FlightServlet(FlightService flightService) {
        flightServiceHandler = new FlightServiceHandler(flightService);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        flightServiceHandler.retrieve(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        flightServiceHandler.create(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        flightServiceHandler.update(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        flightServiceHandler.cancel(req, resp);
    }
}
