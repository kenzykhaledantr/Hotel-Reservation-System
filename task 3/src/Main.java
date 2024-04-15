import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

// Enum for room types
enum RoomType {
    SINGLE,
    DOUBLE,
    SUITE
}

// Class to represent a room
class Room {
    private int roomId;
    private RoomType type;
    private double price;
    private boolean available;

    public Room(int roomId, RoomType type, double price) {
        this.roomId = roomId;
        this.type = type;
        this.price = price;
        this.available = true;
    }

    public int getRoomId() {
        return roomId;
    }

    public RoomType getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}

// Class to represent a reservation
class Reservation {
    private int reservationId;
    private int userId;
    private Room room;
    private Date checkInDate;
    private Date checkOutDate;
    private double totalPrice;

    public Reservation(int reservationId, int userId, Room room, Date checkInDate, Date checkOutDate, double totalPrice) {
        this.reservationId = reservationId;
        this.userId = userId;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalPrice = totalPrice;
    }

    public int getReservationId() {
        return reservationId;
    }

    public int getUserId() {
        return userId;
    }

    public Room getRoom() {
        return room;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}

// Class to represent a user
class User {
    private int userId;
    private String name;
    private String email;

    public User(int userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}

// Main hotel management class
class HotelReservationSystem {
    private List<Room> rooms;
    private List<Reservation> reservations;
    private List<User> users;
    private int nextReservationId;
    private int nextUserId;
    private Scanner scanner;

    public HotelReservationSystem() {
        this.rooms = new ArrayList<>();
        this.reservations = new ArrayList<>();
        this.users = new ArrayList<>();
        this.nextReservationId = 1;
        this.nextUserId = 1;
        this.scanner = new Scanner(System.in);
    }

    // Method to add a room to the hotel
    public void addRoom(Room room) {
        rooms.add(room);
    }

    // Method to search for available rooms
    public List<Room> searchRooms(RoomType type) {
        List<Room> availableRooms = new ArrayList<>();
        for (Room room : rooms) {
            if (room.getType() == type && room.isAvailable()) {
                availableRooms.add(room);
            }
        }
        return availableRooms;
    }

    // Method to make a reservation
    public Reservation makeReservation(User user, Room room, Date checkInDate, Date checkOutDate) {
        long diffInMillies = Math.abs(checkOutDate.getTime() - checkInDate.getTime());
        long diffInDays = (diffInMillies / (1000 * 60 * 60 * 24)) + 1; // Add 1 to include the check-out day
        double totalPrice = diffInDays * room.getPrice();

        Reservation reservation = new Reservation(nextReservationId++, user.getUserId(), room, checkInDate, checkOutDate, totalPrice);
        reservations.add(reservation);
        room.setAvailable(false);
        return reservation;
    }

    // Method to view booking details
    public Reservation viewBookingDetails(int reservationId) {
        for (Reservation reservation : reservations) {
            if (reservation.getReservationId() == reservationId) {
                return reservation;
            }
        }
        return null;
    }

    // Method to process payment
    public void processPayment(Reservation reservation) {
        // Simulate payment processing
        System.out.println("Processing payment for reservation ID: " + reservation.getReservationId());
        System.out.println("Total Price: $" + reservation.getTotalPrice());
        System.out.println("Payment processed successfully.");
    }

    // Method to get user input for creating a user
    public User createUser() {
        System.out.println("Enter your name:");
        String name = scanner.nextLine();
        System.out.println("Enter your email:");
        String email = scanner.nextLine();
        User user = new User(nextUserId++, name, email);
        users.add(user);
        return user;
    }

    // Method to get user input for room type
    public RoomType chooseRoomType() {
        System.out.println("Choose room type:");
        System.out.println("1. Single");
        System.out.println("2. Double");
        System.out.println("3. Suite");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        switch (choice) {
            case 1:
                return RoomType.SINGLE;
            case 2:
                return RoomType.DOUBLE;
            case 3:
                return RoomType.SUITE;
            default:
                System.out.println("Invalid choice. Defaulting to Single.");
                return RoomType.SINGLE;
        }
    }

    public static void main(String[] args) {
        // Sample usage
        HotelReservationSystem hotelSystem = new HotelReservationSystem();

        // Create a user
        User user = hotelSystem.createUser();

        // Add rooms to the hotel with prices
        hotelSystem.addRoom(new Room(101, RoomType.SINGLE, 100.0));
        hotelSystem.addRoom(new Room(102, RoomType.DOUBLE, 150.0));
        hotelSystem.addRoom(new Room(103, RoomType.SINGLE, 100.0));

        // Get user input for room type
        RoomType roomType = hotelSystem.chooseRoomType();

        // Search for available rooms
        List<Room> availableRooms = hotelSystem.searchRooms(roomType);
        System.out.println("Available Rooms:");
        if (availableRooms.isEmpty()) {
            System.out.println("No available rooms of this type.");
        } else {
            for (Room room : availableRooms) {
                System.out.println("Room ID: " + room.getRoomId() + ", Price: $" + room.getPrice());
            }

            // Make a reservation
            System.out.println("Enter the room ID to make a reservation:");
            int roomId = hotelSystem.scanner.nextInt();
            hotelSystem.scanner.nextLine(); // Consume newline
            Room selectedRoom = null;
            for (Room room : availableRooms) {
                if (room.getRoomId() == roomId) {
                    selectedRoom = room;
                    break;
                }
            }
            if (selectedRoom != null) {
                // Get check-in and check-out dates
                System.out.println("Enter check-in date (YYYY-MM-DD):");
                String checkInStr = hotelSystem.scanner.nextLine();
                System.out.println("Enter check-out date (YYYY-MM-DD):");
                String checkOutStr = hotelSystem.scanner.nextLine();
                try {
                    Date checkInDate = new SimpleDateFormat("yyyy-MM-dd").parse(checkInStr);
                    Date checkOutDate = new SimpleDateFormat("yyyy-MM-dd").parse(checkOutStr);
                    Reservation reservation = hotelSystem.makeReservation(user, selectedRoom, checkInDate, checkOutDate);
                    System.out.println("Reservation made. Reservation ID: " + reservation.getReservationId());

                    // Process payment
                    hotelSystem.processPayment(reservation);
                } catch (ParseException e) {
                    System.out.println("Invalid date format. Reservation canceled.");
                }
            } else {
                System.out.println("Invalid room ID.");
            }
        }

        hotelSystem.scanner.close();
    }
}
