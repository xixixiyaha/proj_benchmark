import Dao.SqlConnection;

public class TestSqlConnection {
    public static void main(String[] args){
        String sql = "INSERT INTO Goods VALUES(?, ?, ?, ?, ?)";
        //PreparedStatement pstmt = con.prepareStatement(sql);
        new SqlConnection().TestSqlConnection();
    }
}
