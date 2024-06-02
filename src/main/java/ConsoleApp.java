package console;

import controller.BookingController;
import controller.FlightController;
import exceptions.*;
import model.Passenger;
import model.dto.BookingDto;
import model.dto.FlightDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleApp {
    private final FlightController flightController;

    private final BookingController bookingController;

    public ConsoleApp(FlightController flightController, BookingController bookingController) {
        this.flightController = flightController;
        this.bookingController = bookingController;
    }

    public void start() {
        boolean exit = false;
        while (!exit) {
            printMenu();
            int choice = getUserChoice();
            exit = userHandleInput(choice);
        }
    }

    private void printMenu() {
        System.out.println("Main Menu:");
        System.out.println("1. Online-board");
        System.out.println("2. Show the flight info");
        System.out.println("3. Search and book a flight");
        System.out.println("4. Cancel the booking");
        System.out.println("5. My flights");
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");
    }

    private int getUserChoice() {
        int choice = new Scanner(System.in).nextInt();
        System.out.println();
        return choice;
    }

    private boolean userHandleInput(int choice) {
        switch (choice) {
            case 1:

                System.out.println(flightController.displayOnlineBoard());
                break;
            case 2:
                try {
                    System.out.println("Enter Flight ID: ");
                    Long flightId = new Scanner(System.in).nextLong();
                    FlightDto flightDto = flightController.retrieveFlight(flightId);
                    System.out.println(flightDto);
                } catch (NullFlightIdException | FlightNotFoundException e) {
                    System.out.println("Error showing flight info: " + e.getMessage());
                }
                break;
            case 3:
                try {
                    List<Passenger> passengers = new ArrayList<>();
                    System.out.println("Enter Flight ID: ");
                    Long flightId = new Scanner(System.in).nextLong();
                    System.out.println("Please enter number of the passengers:");
                    int numOfPeople = new Scanner(System.in).nextInt();

                    for (int i = 0; i < numOfPeople; i++) {
                        System.out.println("Enter the full name of passenger " + (i + 1) + " :");
                        String fullName = new Scanner(System.in).nextLine();
                        Passenger passenger = new Passenger(fullName);
                        passengers.add(passenger);
                    }
                    ;
                    BookingDto bookingDto = new BookingDto(flightId, passengers);
                    bookingController.bookFlight(bookingDto);
                    System.out.println("Book successfully flighted");
                } catch (FlightNotFoundException | NullBookingException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case 4:
                try {
                    System.out.println("Enter Booking ID to cancel: ");
                    Long bookingId = new Scanner(System.in).nextLong();
                    bookingController.cancelBooking(bookingId);
                    System.out.println("Booking cancelled successfully!");
                } catch (NullBookingIdException | BookingNotFoundException e) {
                    System.out.println("Error canceling booking: " + e.getMessage());
                }
                break;
            case 5:
                try {
                    System.out.println("Enter Passenger's Full Name: ");
                    Scanner scanner = new Scanner(System.in);
                    String fullName = scanner.nextLine();
                    List<BookingDto> myBookings = bookingController.retrieveBookingsByName(fullName);
                    if (myBookings.isEmpty()) {
                        System.out.println("No bookings found for passenger: " + fullName);
                    } else {
                        System.out.println("My Bookings: " + myBookings);
                    }
                } catch (Exception e) {
                    System.out.println("Error retrieving bookings: " + e.getMessage());
                }
                break;
            case 6:
                System.out.println("Exiting the application...");
                return true;
            default:
                System.out.println("Invalid choice. Please enter a valid option.");
        }
        return false;
    }

}