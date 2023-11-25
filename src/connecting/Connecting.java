package connecting;

import java.sql.Connection;

public class Connecting {

    public static Connection getConnection(String base) throws Exception {
        try {
            if(base.equalsIgnoreCase("mysql")){
                Class.forName("com.mysql.cj.jdbc.Driver");
                return java.sql.DriverManager.getConnection("jdbc:mysql://localhost:3306/stock", "root", "root");
            } else {
                Class.forName("org.postgresql.Driver");
                return java.sql.DriverManager.getConnection("jdbc:postgresql://localhost:5432/stock", "postgres", "postgres");
            }
        }catch (Exception e){
            throw e;
        }
    }
}
