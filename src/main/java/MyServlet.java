import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        String query = "SELECT * FROM hrlive WHERE id=(SELECT max(id) FROM "+tblName+");";
        return stmt.executeQuery(query);
    }

    public static void createJSON(HttpServletResponse resp, String tblName, String colName) throws Exception{
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        try {
            ResultSet rs = RetrieveData(tblName);
            while (rs.next()){
                Patient p = new Patient();
                p.setPatientID(rs.getInt("patientID"));
                p.setHR(rs.getInt(colName));
                p.setTimeRec(rs.getTimestamp("timeRec"));
                Gson gson = new Gson();
                String json= gson.toJson(p);
                out.println(json);
            }
        } catch (Exception e){}
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

            case "/BP":
                break;

            default:

        }



    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException {
        String reqBody=req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        Gson gson = new Gson();
        resp.setContentType("application/json");
        resp.setContentType("text/html");
        resp.getWriter().write("Thank you client! "+reqBody);
    }


}
