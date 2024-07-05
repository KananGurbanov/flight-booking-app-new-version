package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import exceptions.FlightNotFoundException;
import exceptions.NullFlightException;
import model.dto.FlightDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.FlightService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class FlightServiceHandler {
    private static final Logger log = LoggerFactory.getLogger(FlightServiceHandler.class);
    private final ObjectMapper objectMapper;
    private final FlightService flightService;

    public FlightServiceHandler(FlightService flightService) {
        this.flightService = flightService;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public void retrieve(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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
                objectMapper.writeValue(resp.getWriter(), flights);
            } else {
                String idStr = pathInfo.substring(1); // remove the leading '/'
                Long id = Long.parseLong(idStr);
                FlightDto flightDto = flightService.retrieveFlight(id);
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                objectMapper.writeValue(resp.getWriter(), flightDto);
            }
        } catch (NumberFormatException e) {
            log.error(e.getMessage());
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid id format");
        } catch (FlightNotFoundException e) {
            log.error(e.getMessage());
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid search parameters");
        }
    }

    public void update(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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
            log.error(e.getMessage());
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid id format");
        } catch (FlightNotFoundException | NullFlightException e) {
            log.error(e.getMessage());
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }
    }

    public void create(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            FlightDto flightDto = objectMapper.readValue(req.getReader(), FlightDto.class);
            flightService.createFlight(flightDto);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (NullFlightException e) {
            log.error(e.getMessage());
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    public void cancel(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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
            log.error(e.getMessage());
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid id format");
        } catch (FlightNotFoundException e) {
            log.error(e.getMessage());
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }
    }
}
