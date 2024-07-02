package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import exceptions.FlightNotFoundException;
import exceptions.NullFlightException;
import model.dto.BookingDto;
import model.dto.FlightDto;
import service.BookingService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class BookingServlet extends HttpServlet {
    private final ObjectMapper mapper;
    private final BookingService bookingService;

    public BookingServlet( BookingService bookingService) {
        this.mapper = new ObjectMapper();
        this.bookingService = bookingService;

    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
      try {
          if (pathInfo==null || pathInfo.equals("/")){
              List<BookingDto> bookingDtos=bookingService.retrieveAllBookings();
              resp.setContentType("application/json");
              resp.setCharacterEncoding("UTF-8");
              mapper.writeValue(resp.getWriter(),bookingDtos);
          } else if (pathInfo.equals("/search")) {
              List<BookingDto> bookings =bookingService.retrieveAllBookings();
              resp.setContentType("application/json");
              resp.setCharacterEncoding("UTF-8");
              mapper.writeValue(resp.getWriter(),bookings);
          }
          else {
              String idStr = pathInfo.substring(1); // remove the leading '/'
              Long id = Long.parseLong(idStr);
             BookingDto bookingDto = bookingService.retrieveBooking(id);
              resp.setContentType("application/json");
              resp.setCharacterEncoding("UTF-8");
              mapper.writeValue(resp.getWriter(), bookingDto);
          }

      } catch (IOException e) {
          throw new ServletException(e);
      } catch (NumberFormatException e) {
          throw new RuntimeException(e);
      }


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       try {
           BookingDto bookingDto = mapper.readValue(req.getReader(), BookingDto.class);
           bookingService.bookFlight(bookingDto);
           resp.setStatus(HttpServletResponse.SC_OK);

       } catch (IOException e) {
           throw new ServletException(e);
       } catch (NumberFormatException e) {
           throw new RuntimeException(e);
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
            BookingDto bookingDto = mapper.readValue(req.getReader(), BookingDto.class);
          bookingService.updateBooking(id, bookingDto);
          resp.setStatus(HttpServletResponse.SC_OK);
        }catch (NumberFormatException e) {
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

        }
        String idStr = pathInfo.substring(1);
        try {
            Long id = Long.parseLong(idStr);
            bookingService.cancelBooking(id);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }
}
