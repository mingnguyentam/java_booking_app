# Testing Room Edit/Delete Validation

## Test Scenario:

### Step 1: Book a room
1. Go to "Booking Room" menu
2. Select "Booking Room"
3. Book room ID 1 (Deluxe Suite)

### Step 2: Try to Edit the booked room
1. Go to "Manage Room" menu
2. Select "Edit Room"
3. Enter room ID 1
4. **Expected Result:** Error message - "Cannot edit this room! This room is currently booked/rented."

### Step 3: Try to Delete the booked room
1. In "Manage Room" menu
2. Select "Delete Room"
3. Enter room ID 1
4. **Expected Result:** Error message - "Cannot delete this room! This room is currently booked/rented."

### Step 4: Edit/Delete an available room
1. Try to edit or delete room ID 2 or 3 (not booked)
2. **Expected Result:** Edit/Delete works successfully

## What Was Changed:

### editRoom() method:
- Added check: `if (!room.isAvailable())`
- Shows error message if room is booked
- Prevents editing rented rooms

### deleteRoom() method:
- Added check: `if (!room.isAvailable())`
- Shows error message if room is booked
- Prevents deleting rented rooms

## Business Logic:
✅ Available rooms (not booked) → Can be edited or deleted
❌ Booked rooms (is_available = 0) → Cannot be edited or deleted
