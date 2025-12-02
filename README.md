# Ứng Dụng Quản lý Đặt Phòng - Room Booking App

Một hệ thống quản lý đặt phòng toàn diện được xây dựng bằng Java và SQLite, hỗ trợ quản lý phòng, đặt phòng và theo dõi khách hàng với giao diện đồ họa Swing hiện đại.

## 🌟 Tính năng chính

### 🏨 **Quản lý Phòng**
- **CRUD Hoàn chỉnh**: Thêm, xem, sửa, xóa thông tin phòng
- **Xác thực Tên Phòng**: Ngăn chặn tên phòng trùng lặp (không phân biệt chữ hoa/thường)
- **Bảo vệ Dữ liệu**: Không thể sửa/xóa phòng đang được đặt
- **Sắp xếp Thông minh**: Sắp xếp theo giá (Thấp đến Cao / Cao đến Xuống)
- **Trạng thái Phòng**: Theo dõi phòng Available/Booked tự động

### 💰 **Hệ thống Đặt Phòng**
- **Đặt Phòng Dễ dàng**: Giao diện đơn giản, chọn phòng và nhập thông tin
- **Tính Toán Tự động**: Tính tổng tiền tự động theo số đêm
- **Quản lý Khách hàng**: Theo dõi đặt phòng theo tên khách hàng
- **Lịch sử Đặt phòng**: Xem tất cả đặt phòng của khách hàng hiện tại
- **Định dạng VND**: Hiển thị giá theo đồng Việt Nam


### 🛡️ **Bảo mật & Tính toàn vẹn Dữ liệu**
- **Cơ sở dữ liệu SQLite**: Lưu trữ dữ liệu an toàn, nhẹ nhàng
- **Prepared Statements**: Ngăn chặn SQL Injection
- **Xác thực Dữ liệu**: Kiểm tra dữ liệu đầu vào trước khi lưu
- **Quản lý Kết nối**: Kết nối database singleton được quản lý tốt
- **Foreign Key Constraints**: Đảm bảo tính toàn vẹn dữ liệu

## 🔒 **Xác thực Dữ Liệu**
- **Duplicate Prevention**: Không cho phép tên phòng trùng (case-insensitive)
- **Edit/Delete Protection**: Chỉ phòng trống mới có thể sửa/xóa

## 👥 Thành viên nhóm và phân công công việc

| STT | Họ tên | MSSV | Công việc được giao |
|-----|--------|------|-------------------|
| 1 | Nguyễn Minh Tâm | K24DTCN627 | Thiết kế database structure, triển khai DatabaseManager, RoomManager, BookingManager |
| 2 | Trieu Duc Hoang | K24DTCN598 | Thiết kế GUI Swing, triển khai các Panel, Dialog forms |

## 🚀 Hướng dẫn Cài đặt

### **Yêu cầu Hệ thống**
- Java Development Kit (JDK) 8 hoặc cao hơn
- SQLite JDBC Driver (đã bao gồm trong `lib/`)

#### **Windows**
```bash
# Biên dịch
javac -cp "lib/sqlite-jdbc.jar;." -d bin src/*.java

# Chạy GUI version
java -cp "bin;lib/sqlite-jdbc.jar" MainFrame

# Hoặc chạy Console version
java -cp "bin;lib/sqlite-jdbc.jar" App
```

#### **Linux/Mac**
```bash
# Biên dịch
javac -cp "lib/sqlite-jdbc.jar:." -d bin src/*.java

# Chạy GUI version
java -cp "bin:lib/sqlite-jdbc.jar" MainFrame

# Hoặc chạy Console version
java -cp "bin:lib/sqlite-jdbc.jar" App
```

### 📥 Sử dụng Script có sẵn

#### **Windows (Console)**
```bash
# Chỉ cần double-click file run.bat
run.bat
```

#### **Linux/Mac (Console)**
```bash
# Cấp quyền thực thi
chmod +x run.sh

# Chạy
./run.sh
```
