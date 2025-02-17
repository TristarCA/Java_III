import java.sql.*;
public class MariaDBTest {
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    static final String DB_URL = "jdbc:mariadb://localhost:3300/javatest";
    // Database credentials
    static final String USER = "root";
    static final String PASS = "root";
    public static void main(String args[]){
        Connection conn = null;
        Statement stmt = null;
        try {
//STEP 2: Register JDBC driver
            Class.forName(JDBC_DRIVER);
//STEP 3: Open a connection
            System.out.println("Connecting to a selected database...");
            conn = DriverManager.getConnection(
                    DB_URL, USER, PASS);
            System.out.println("Connected database successfully...");
            stmt=conn.createStatement();
            ResultSet rs=stmt.executeQuery("select * from testing");
            while(rs.next())
                System.out.println(rs.getInt(1)+" "+rs.getString(2));
            conn.close();
        }catch(Exception e){ System.out.println(e);}
    }
}
