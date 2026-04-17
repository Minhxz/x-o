package Logic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Lớp tiện ích cung cấp kết nối đến cơ sở dữ liệu MySQL
public class Database {
    // Cấu hình URL kết nối đến database 'tic_tac_toe_db' qua cổng mặc định 3306
    private static final String URL = "jdbc:mysql://localhost:3306/tic_tac_toe_db";
    // Tên đăng nhập và mật khẩu mặc định của XAMPP
    private static final String USER = "root";
    private static final String PASS = ""; 

    // Phương thức trả về một đối tượng Connection dùng để thao tác với DB
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}