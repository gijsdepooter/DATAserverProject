import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Vector;
import java.util.stream.Collectors;



@WebServlet(urlPatterns={"/Login/*"},loadOnStartup = 1)
public class MyServlet extends HttpServlet {

    public static ResultSet RetrieveData(String query) throws Exception{
        String dbUrl = "jdbc:postgresql://ec2-54-73-68-39.eu-west-1.compute.amazonaws.com:5432/dctpppdsoogu5e";
        Class.forName("org.postgresql.Driver");
        Connection conn = DriverManager.getConnection(dbUrl, "wtlubuspzbefzf", "6056c0cef2cfcbf15902982f17d7ba4a19158dd1087ecb110fce1aade0e0629b");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        conn.close();
        return rs;
    }

    public static void createJSON(HttpServletResponse resp, String query, String action) throws Exception{
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        try {
            ResultSet rs = RetrieveData(query);
            Patient p = new Patient();

            while (rs.next()){
                p.setPatientID(rs.getInt("patientID"));
                p.setTimeRec(rs.getTimestamp("timeRec"));

                if(action.equals("heartrate")){
                    p.setHR(rs.getInt(action));
                }
                else if(action.equals("ecg")){
                    p.setECG(rs.getFloat(action));
                }
            }
            p.PrintPatient();
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss.SSS").create();
            String json= gson.toJson(p);
            System.out.println(json);
            out.println(json);

        } catch (Exception e){System.out.println("Exception while making JSON: " + e.getMessage());}
    }

    public static void AbnormalVal(HttpServletResponse resp){
        try {
            String query = "SELECT * FROM hrlive WHERE heartrate>90;";
            createJSON(resp,query,"heartrate");
            String query2 = "SELECT * FROM hrlive WHERE heartrate<60;";
            createJSON(resp,query2,"hearrate");
        }catch (Exception e){}
    }

    public static void AverageValues(HttpServletResponse resp, int sampFreq){
        try{
            int size = 0;

            ResultSet rs1 = RetrieveData("SELECT COUNT(*) FROM hrlive;");
            rs1.next();
            size = rs1.getInt("count");

            String query = "Select * FROM hrlive;";
            ResultSet rs = RetrieveData(query);
            Patient p = new Patient();

            for(int j=0; j< size/(60/sampFreq); j++){
                int sum =0;
                Vector<Integer> hrAv = new Vector<>();

                for(int i=0; i<60/sampFreq; i++){
                        rs.next();
                        int val = rs.getInt("heartrate");
                        hrAv.add(val);
                }
                for(int i=0; i<=hrAv.size()-1; i++) {
                    sum += hrAv.get(i);

                }
                int average= sum/hrAv.size();
                Timestamp ts = rs.getTimestamp("timerec");
                p.setHR(average);
                p.setTimeRec(ts);

            }
            PrintWriter out = resp.getWriter();
            resp.setContentType("application/json");
            p.PrintPatient();
            Gson gson = new Gson();
            String json= gson.toJson(p);
            out.println(json);

        }catch (Exception e){System.out.println(e);}
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
                    String query = "SELECT * FROM hrlive WHERE id=(SELECT max(id) FROM hrlive);";
                    createJSON(resp,query,"heartrate");
                }catch (Exception e){}

                break;

            case "/ECG":
                try{

                    String query = "SELECT * FROM ecglive ORDER BY id DESC LIMIT 500;";
                    createJSON(resp,query,"ecg");
                }catch(Exception e){}

                break;

            case "/RR":
                try{

                    String query = "SELECT * FROM rrlive WHERE id=(SELECT max(id) FROM rrlive);";
                    createJSON(resp,query,"respiratoryrate");
                }catch(Exception e){}

                break;


            case "/ECGlast":
                try{
                    String query = "SELECT * FROM ecglive WHERE id=(SELECT max(id) FROM ecglive);";
                    createJSON(resp,query,"ecg");
                }catch(Exception e){}

                break;

            case "/Abnormal":
                try{
                    AbnormalVal(resp);
                }catch(Exception e){}

                break;

            case "/Average":
                try{
                    AverageValues(resp,3);
                }catch (Exception e){}
                break;


            default:

                break;

        }



    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getRequestURI();
        action = action.substring(action.lastIndexOf("/"));
        System.out.println(action);

        switch (action) {
            case "/Login":
                try {
                    String reqBody = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
                    Gson gson = new Gson();
                    Doctor Doc = gson.fromJson(reqBody, Doctor.class);
                    Doc.Authenticate();
                    Gson gson1 = new Gson();
                    String json= gson1.toJson(Doc);
                    resp.setContentType("application/json");
                    resp.setContentType("text/html");
                    resp.getWriter().write("The user authentication is: " + Doc.getAuthentication()+" For Doctor: "+json);
                } catch (Exception e) {
                }

                break;
        }


    }


}
