import java.sql.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class DatabaseReader {
    public static void Read(String RawData, String LiveData, String Col, int sampFreq, int PatientID){
        String dbUrl = "jdbc:postgresql://localhost:5432/PatientData";
        try {
            // Registers the driver
            Class.forName("org.postgresql.Driver");
            Connection conn= DriverManager.getConnection(dbUrl, "postgres","Surfdude04");

            String query = "SELECT * FROM " +RawData+ ";";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next())
            {
                float data = rs.getFloat(Col);

                Date date = new Date();
                Timestamp ts = new Timestamp(date.getTime());
                System.out.println(ts);

                try {
                    PreparedStatement query2 = conn.prepareStatement("INSERT INTO "+LiveData+" (timerec,patientID,"+Col+") values (?,"+PatientID+",?);");
                    query2.setTimestamp(1, ts);
                    query2.setFloat(2, data);
                    query2.executeUpdate();

                }catch (SQLException e){
                    System.out.println("Exception while executing update: " + e.getMessage());
                }

                try {
                    TimeUnit.MILLISECONDS.sleep(sampFreq);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
            st.close();
            conn.close();
        } catch (Exception e) {

        }

    }

    public static void ClearTable(String TableName){
        String dbUrl = "jdbc:postgresql://localhost:5432/PatientData";
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(dbUrl, "postgres", "Surfdude04");

            String query = "DELETE FROM "+TableName+";";
            Statement st = conn.createStatement();
            st.executeQuery(query);
            conn.close();
        }catch (Exception e){}
    }

}
