import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DatabaseReader {
    public static void Read(){
        String dbUrl = "jdbc:postgresql://localhost:5432/PatientData";
        try {
            // Registers the driver
            Class.forName("org.postgresql.Driver");
            Connection conn= DriverManager.getConnection(dbUrl, "postgres","Surfdude04");

            String query = "SELECT * FROM patient0hr;";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);



            while (rs.next())
            {
                Float hr_t = rs.getFloat("heartrate");
                Date date = new Date();
                Timestamp ts = new Timestamp(date.getTime());

                try {
                    PreparedStatement query2 = conn.prepareStatement("INSERT INTO hrlive (timerec,heartrate) values (?,?);");
                    query2.setTimestamp(1, ts);
                    query2.setFloat(2, hr_t);
                    query2.executeUpdate();
                }catch (SQLException e){
                    System.out.println("Exception while executing update: " + e.getMessage());
                }


                System.out.println(hr_t);
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }


            }
            String query3 = "DELETE FROM hrlive;";
            st.executeQuery(query3);


            st.close();
        } catch (Exception e) {

        }

    }

}
