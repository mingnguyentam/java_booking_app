public class Room {
    private int roomId;
    private String roomName;
    private String roomType;
    private double pricePerNight;
    private boolean isAvailable;

    public Room(int roomId, String roomName, String roomType, double pricePerNight) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.isAvailable = true;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public String toString() {
        return String.format("ID: %d | Name: %s | Type: %s | Price: %.0f VND/night | Status: %s",
                roomId, roomName, roomType, pricePerNight, isAvailable ? "Available" : "Booked");
    }
}
