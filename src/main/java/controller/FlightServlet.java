package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import exceptions.FlightNotFoundException;
import exceptions.NullFlightException;
import model.dto.FlightDto;
import service.FlightService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class FlightServlet extends HttpServlet {
    private final ObjectMapper objectMapper;
    private final FlightService flightService;

    public FlightServlet(FlightService flightService) {
        this.flightService = flightService;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        String destination = req.getParameter("destination");
        String departureTimeStr = req.getParameter("departureTime");

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                if (destination != null && departureTimeStr != null) {
                    LocalDateTime departureTime = LocalDateTime.parse(departureTimeStr);
                    FlightDto flightDto = flightService.searchFlight(destination, departureTime);
                    resp.setContentType("application/json");
                    resp.setCharacterEncoding("UTF-8");
                    objectMapper.writeValue(resp.getWriter(), flightDto);
                } else {
                    List<FlightDto> flights = flightService.retrieveAllFlights();
                    resp.setContentType("application/json");
                    resp.setCharacterEncoding("UTF-8");
                    objectMapper.writeValue(resp.getWriter(), flights);
                }
            } else if (pathInfo.equals("/onlineBoard")) {
                List<FlightDto> flights = flightService.displayOnlineBoard();
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                objectMapper.   writeValue(resp.getWriter(), flights);
            } else {
                String idStr = pathInfo.substring(1); // remove the leading '/'
                Long id = Long.parseLong(idStr);
                FlightDto flightDto = flightService.retrieveFlight(id);
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                objectMapper.writeValue(resp.getWriter(), flightDto);
            }
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid id format");
        } catch (FlightNotFoundException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid search parameters");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            FlightDto flightDto = objectMapper.readValue(req.getReader(), FlightDto.class);
            flightService.createFlight(flightDto);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (NullFlightException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing id");
            return;
        }

        String idStr = pathInfo.substring(1);
        try {
            Long id = Long.parseLong(idStr);
            FlightDto updatedFlightDto = objectMapper.readValue(req.getReader(), FlightDto.class);
            flightService.updateFlight(id, updatedFlightDto);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid id format");
        } catch (FlightNotFoundException | NullFlightException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing id");
            return;
        }

        String idStr = pathInfo.substring(1);
        try {
            Long id = Long.parseLong(idStr);
            flightService.removeFlight(id);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid id format");
        } catch (FlightNotFoundException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }
    }
}
