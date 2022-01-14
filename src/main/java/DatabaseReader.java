import java.sql.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class DatabaseReader {
    /*This function takes the values from the raw data tables and puts the physiological
    values along with other metadata in the live data tables */
    public static void Read(String RawData, String LiveData, String Col, int sampFreq, int PatientID){
        String dbUrl = "jdbc:postgresql://ec2-54-73-68-39.eu-west-1.compute.amazonaws.com:5432/dctpppdsoogu5e";
        try {
            // Registers the driver
            Class.forName("org.postgresql.Driver");
            Connection conn= DriverManager.getConnection(dbUrl, "wtlubuspzbefzf","6056c0cef2cfcbf15902982f17d7ba4a19158dd1087ecb110fce1aade0e0629b");

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
        } catch (Exception e) {
            System.out.println(e);

        }

    }

    public static void ClearTable(String TableName){
        /* This function empties the live data tables*/
        String dbUrl = "jdbc:postgresql://ec2-54-73-68-39.eu-west-1.compute.amazonaws.com:5432/dctpppdsoogu5e";
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(dbUrl, "wtlubuspzbefzf", "6056c0cef2cfcbf15902982f17d7ba4a19158dd1087ecb110fce1aade0e0629b");

            String query = "DELETE FROM "+TableName+";";
            Statement st = conn.createStatement();
            st.executeQuery(query);
            conn.close();
        }catch (Exception e){}
    }

}
