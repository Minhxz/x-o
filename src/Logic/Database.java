package Logic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    // Đường dẫn kết nối đến cơ sở dữ liệu MySQL thông qua XAMPP (cổng mặc định 3306)
    // Tên cơ sở dữ liệu là: tic_tac_toe_db
    private static final String URL = "jdbc:mysql://localhost:3306/tic_tac_toe_db";
    private static final String USER = "root"; // Tên đăng nhập mặc định
    private static final String PASS = "";     // Mật khẩu mặc định của XAMPP là rỗng

    // Hàm trả về một đối tượng Connection dùng để truy vấn SQL (INSERT, SELECT,...)
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}