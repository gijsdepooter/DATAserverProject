import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Doctor {
    private int doctorID;
    private String userName;
    private String password;
    private Boolean authentication;

    public void setUser(String userName,String password){
        this.userName = userName;
        this.password = password;
    }

    public int getDoctorID(){
        return doctorID;
    }

    public String getUserName(){
        return userName;
    }

    public String getPassword(){
        return password;
    }

    public Boolean getAuthentication(){
        return authentication;
    }

    public void Authenticate(){
        try {
            String dbUrl = "jdbc:postgresql://localhost:5432/PatientData";
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(dbUrl, "postgres", "Surfdude04");
            Statement stmt = conn.createStatement();

            String query = "SELECT * FROM doctors;";
            ResultSet rs = stmt.executeQuery(query);

            while(rs.next()){
                if(rs.getString("username").equals(userName)){
                    if(rs.getString("password").equals(password)){this.authentication = true;}
                }
            }
        }catch (Exception e){}

    }

    public void PrintDoctor(){
        System.out.println(userName);
        System.out.println(password);
        System.out.println(authentication);
    }
}
