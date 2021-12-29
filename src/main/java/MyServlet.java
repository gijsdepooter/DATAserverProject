import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.stream.Collectors;



@WebServlet(urlPatterns={"/Login/*"},loadOnStartup = 1)
public class MyServlet extends HttpServlet {

    public static ResultSet RetrieveData(String tblName) throws Exception{
        String dbUrl = "jdbc:postgresql://localhost:5432/PatientData";
        Class.forName("org.postgresql.Driver");
        Connection conn = DriverManager.getConnection(dbUrl, "postgres", "Surfdude04");
        Statement stmt = conn.createStatement();

        if (tblName.equals("ecglive")) {
            String query2 = "SELECT * FROM ecglive ORDER BY id DESC LIMIT 10";//Needs to select multiple entries due to high sampling rate of ECG
            System.out.println("ecgtable searched");
            return stmt.executeQuery(query2);
        }
        else {
            String query = "SELECT * FROM " + tblName + " WHERE id=(SELECT max(id) FROM " + tblName + ");";
            return stmt.executeQuery(query);
        }
    }

    public static void createJSON(HttpServletResponse resp, String tblName, String colName) throws Exception{
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        try {
            ResultSet rs = RetrieveData(tblName);
            Patient p = new Patient();

            while (rs.next()){
                p.setPatientID(rs.getInt("patientID"));
                p.setTimeRec(rs.getTimestamp("timeRec"));

                if(colName.equals("heartrate")){
                    p.setHR(rs.getInt(colName));//add methods for getting different numbers for different col names
                }
                else if(colName.equals("ecg")){
                    p.setECG(rs.getFloat("ecg"));
                }
            }
            p.PrintPatient();
            Gson gson = new Gson();
            String json= gson.toJson(p);
            out.println(json);

        } catch (Exception e){System.out.println("Exception while making JSON: " + e.getMessage());}
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Servlet invoked !!");
        String action = req.getRequestURI();
        action = action.substring(action.lastIndexOf("/"));
        System.out.println(action);

        switch (action) {
            case"/HR":
                try{
                    createJSON(resp,"hrlive","heartrate");

                }catch (Exception e){}

                break;

            case "/ECG":
                try{
                    createJSON(resp,"ecglive","ecg");
                }catch(Exception e){}

                break;

            default:

                break;

        }



    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException {
        String reqBody=req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        Gson gson = new Gson();
        Doctor Doc =  gson.fromJson(reqBody,Doctor.class);
        Doc.Authenticate();
        Doc.PrintDoctor();
        resp.setContentType("application/json");
        resp.setContentType("text/html");
        resp.getWriter().write("Thank you client! "+reqBody+" The user authentication is: "+Doc.getAuthentication());
    }


}
