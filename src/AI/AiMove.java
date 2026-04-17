package AI;

// Lớp dữ liệu (Model/POJO) đại diện cho 1 nước đi của máy (tọa độ Hàng và Cột)
public class AiMove {
    public final int row;
    public final int col;

    public AiMove(int row, int col) {
        this.row = row;
        this.col = col;
    }
}