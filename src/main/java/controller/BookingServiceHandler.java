package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import exceptions.BookingNotFoundException;
import exceptions.NullBookingIdException;
import model.dto.BookingDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.BookingService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class BookingServiceHandler {
    private static final Logger log = LoggerFactory.getLogger(FlightServiceHandler.class);
    private final ObjectMapper mapper;
    private final BookingService bookingService;

    public BookingServiceHandler(BookingService bookingService) {
        this.mapper = new ObjectMapper();
        this.bookingService = bookingService;
        this.mapper.registerModule(new JavaTimeModule());
    }

    public void retrieve(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        String fullName = req.getParameter("fullName");
        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                List<BookingDto> bookingDtos = bookingService.retrieveAllBookings();
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                mapper.writeValue(resp.getWriter(), bookingDtos);
            } else if (pathInfo.equals("/search") && fullName != null) {
                List<BookingDto> bookings = bookingService.retrieveBookingsByName(fullName);
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                mapper.writeValue(resp.getWriter(), bookings);
            } else {
                String idStr = pathInfo.substring(1);
                Long id = Long.parseLong(idStr);
                BookingDto bookingDto = bookingService.retrieveBooking(id);
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                mapper.writeValue(resp.getWriter(), bookingDto);
            }

        } catch (NumberFormatException e) {
            log.error(e.getMessage());
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid id format");
        } catch (IOException e) {
            log.error(e.getMessage());
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing request");
        } catch (BookingNotFoundException e) {
            log.error(e.getMessage());
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unexpected error occurred");
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
            BookingDto bookingDto = mapper.readValue(req.getReader(), BookingDto.class);
            bookingService.updateBooking(id, bookingDto);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (NumberFormatException e) {
            log.error(e.getMessage());
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid id format");
        } catch (BookingNotFoundException | NullBookingIdException e) {
            log.error(e.getMessage());
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }
    }

    public void create(HttpServletRequest req, HttpServletResponse resp) {
        try {
            BookingDto bookingDto = mapper.readValue(req.getReader(), BookingDto.class);
            bookingService.bookFlight(bookingDto);
            resp.setStatus(HttpServletResponse.SC_OK);

        } catch (IOException e) {
            log.error(e.getMessage());
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST, "Error reading request data");
        } catch (NumberFormatException e) {
            log.error(e.getMessage());
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST, "Invalid id format");
        }
    }

    public void cancel(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing id");
        }
        String idStr = pathInfo.substring(1);
        try {
            Long id = Long.parseLong(idStr);
            bookingService.cancelBooking(id);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (NumberFormatException e) {
            log.error(e.getMessage());
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid id format");
        } catch (BookingNotFoundException e) {
            log.error(e.getMessage());
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unexpected error occurred");
        }
    }
}
