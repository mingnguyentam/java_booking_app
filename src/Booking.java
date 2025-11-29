public class Booking {
    private int bookingId;
    private int roomId;
    private String customerName;
    private String checkInDate;
    private String checkOutDate;
    private int numberOfNights;
    private double totalPrice;

    public Booking(int bookingId, int roomId, String customerName, String checkInDate, String checkOutDate, int numberOfNights, double totalPrice) {
        this.bookingId = bookingId;
        this.roomId = roomId;
        this.customerName = customerName;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numberOfNights = numberOfNights;
        this.totalPrice = totalPrice;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public int getNumberOfNights() {
        return numberOfNights;
    }

    public void setNumberOfNights(int numberOfNights) {
        this.numberOfNights = numberOfNights;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return String.format("Booking ID: %d | Room ID: %d | Customer: %s | Check-in: %s | Check-out: %s | Nights: %d | Total: $%.2f",
                bookingId, roomId, customerName, checkInDate, checkOutDate, numberOfNights, totalPrice);
    }
}
