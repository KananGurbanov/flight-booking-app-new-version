package controller;

import service.BookingService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BookingServlet extends HttpServlet {
    private final BookingServiceHandler bookingServiceHandler;

    public BookingServlet(BookingService bookingService) {
        bookingServiceHandler = new BookingServiceHandler(bookingService);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        bookingServiceHandler.retrieve(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        bookingServiceHandler.create(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        bookingServiceHandler.update(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        bookingServiceHandler.cancel(req, resp);
    }
}
