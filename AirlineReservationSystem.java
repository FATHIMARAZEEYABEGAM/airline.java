import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

// --- Abstraction via interface ---
interface Bookable {
    void displayInfo();
}

// --- Flight class ---
class Flight implements Bookable {
    private int flightId;
    private String origin;
    private String destination;
    private int seats;
    private double price;

    public Flight(int flightId, String origin, String destination, int seats, double price) {
        this.flightId = flightId;
        this.origin = origin;
        this.destination = destination;
        this.seats = seats;
        this.price = price;
    }

    public Flight(int flightId, String origin, String destination, int seats) {
        this(flightId, origin, destination, seats, 2999.0);
    }

    public int getFlightId() { return flightId; }
    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    public int getSeats() { return seats; }
    public double getPrice() { return price; }

    public boolean reserveSeat() {
        if (seats > 0) {
            seats--;
            return true;
        }
        return false;
    }

    @Override
    public void displayInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("Flight ID: ").append(flightId)
          .append(" | From: ").append(origin)
          .append(" | To: ").append(destination)
          .append(" | Seats Available: ").append(seats)
          .append(" | Price: ₹").append(price);
        System.out.println(sb.toString());
    }
}

// --- International Flight subclass ---
class InternationalFlight extends Flight {
    private String passportRequired;

    public InternationalFlight(int flightId, String origin, String destination, int seats, double price, String passportRequired) {
        super(flightId, origin, destination, seats, price);
        this.passportRequired = passportRequired;
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("  Note: International flight. Passport needed: " + passportRequired);
    }
}

// --- Booking class ---
class Booking {
    private String passengerName;
    private Flight flight;

    public Booking(String passengerName, Flight flight) {
        this.passengerName = passengerName;
        this.flight = flight;
    }

    public String getPassengerName() { return passengerName; }
    public Flight getFlight() { return flight; }

    public void displayBooking() {
        StringBuilder sb = new StringBuilder();
        sb.append("Passenger: ").append(passengerName)
          .append(" | Flight ID: ").append(flight.getFlightId())
          .append(" (").append(flight.getOrigin()).append(" -> ")
          .append(flight.getDestination()).append(")")
          .append(" | Ticket Price: ₹").append(flight.getPrice());
        System.out.println(sb.toString());
    }
}

// --- Main Airline Reservation System ---
public class AirlineReservationSystem {
    private static String[] airports = {"Chennai", "Mumbai", "Bangalore", "Delhi", "Hyderabad", "Kolkata"};
    private static List<Flight> flights = new ArrayList<>();
    private static List<Booking> bookings = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void preloadFlights() {
        flights.add(new Flight(101, "Chennai", "Delhi", 5, 4500));
        flights.add(new Flight(102, "Mumbai", "Bangalore", 3));
        flights.add(new InternationalFlight(103, "Hyderabad", "Kolkata", 4, 4000, "Yes"));
        flights.add(new Flight(104, "Delhi", "Mumbai", 2, 3200));
    }

    public static void showAirports() {
        System.out.println("Popular Airports: " + Arrays.toString(airports));
    }

    public static void viewFlights() {
        System.out.println("\n--- Available Flights ---");
        for (Flight f : flights) f.displayInfo();
    }

    public static void viewFlights(String originFilter) {
        System.out.println("\n--- Flights from " + originFilter + " ---");
        for (Flight f : flights) {
            if (f.getOrigin().equalsIgnoreCase(originFilter)) f.displayInfo();
        }
    }

    public static void viewFlights(int minSeats) {
        System.out.println("\n--- Flights with at least " + minSeats + " seats ---");
        for (Flight f : flights) {
            if (f.getSeats() >= minSeats) f.displayInfo();
        }
    }

    public static boolean bookTicket(String passengerName, int flightId) {
        for (Flight f : flights) {
            if (f.getFlightId() == flightId) {
                if (f.reserveSeat()) {
                    bookings.add(new Booking(passengerName, f));
                    System.out.println("Ticket booked successfully for " + passengerName + " | Price: ₹" + f.getPrice());
                    return true;
                } else {
                    System.out.println("No seats available on Flight ID: " + flightId);
                    return false;
                }
            }
        }
        System.out.println("Flight not found with ID: " + flightId);
        return false;
    }

    public static boolean bookTicket(Booking booking) {
        Flight f = booking.getFlight();
        if (f.reserveSeat()) {
            bookings.add(booking);
            System.out.println("Ticket booked successfully for " + booking.getPassengerName() + " | Price: ₹" + f.getPrice());
            return true;
        }
        System.out.println("No seats available for booking attempt.");
        return false;
    }

    public static void viewBookings() {
        System.out.println("\n--- Passenger Bookings ---");
        if (bookings.isEmpty()) {
            System.out.println("No bookings yet.");
            return;
        }
        for (Booking b : bookings) b.displayBooking();
    }

    public static void main(String[] args) throws IOException {
        preloadFlights();
        showAirports();

        int choice = -1;
        do {
            System.out.println("\n=== Airline Reservation System (Enhanced) ===");
            System.out.println("1. View All Flights");
            System.out.println("2. View Flights by Origin");
            System.out.println("3. View Flights by Minimum Seats");
            System.out.println("4. Book Ticket (Scanner)");
            System.out.println("5. Book Ticket (BufferedReader)");
            System.out.println("6. View Bookings");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();
            } else {
                scanner.nextLine();
                System.out.println("Please enter a number between 1 and 7.");
                continue;
            }

            switch (choice) {
                case 1: viewFlights(); break;
                case 2:
                    System.out.print("Enter origin to filter: ");
                    String originFilter = scanner.nextLine();
                    viewFlights(originFilter); break;
                case 3:
                    System.out.print("Enter minimum seats required: ");
                    int minSeats = Integer.parseInt(scanner.nextLine());
                    viewFlights(minSeats); break;
                case 4:
                    System.out.print("Enter passenger name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Flight ID to book: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    bookTicket(name, id); break;
                case 5:
                    System.out.print("(BufferedReader) Enter passenger name: ");
                    String brName = reader.readLine();
                    System.out.print("(BufferedReader) Enter Flight ID to book: ");
                    int brId = Integer.parseInt(reader.readLine());
                    bookTicket(brName, brId); break;
                case 6: viewBookings(); break;
                case 7: System.out.println("Thank you for using the Enhanced Airline Reservation System!"); break;
                default: System.out.println("Invalid choice! Please select between 1 and 7.");
            }

        } while (choice != 7);

        scanner.close();
        reader.close();
    }
}