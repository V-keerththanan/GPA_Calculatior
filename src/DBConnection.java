import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public Connection getMyConnection(){
        String path="jdbc:mysql://localhost:3306/GPA_Calculator";
        String user="root";
        String password="root@1999";
        Connection con= null;
        try {
            con = DriverManager.getConnection(path,user,password);
        } catch (SQLException e) {
            System.out.println("There is some problem to connect with database");
        }
        return con;
    }

}
