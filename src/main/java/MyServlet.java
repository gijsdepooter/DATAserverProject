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

@WebServlet(urlPatterns={"/HR"},loadOnStartup = 1)
public class MyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("text/html");
        out.println("<html><body>");
        String dbUrl = "jdbc:postgresql://localhost:5432/PatientData";
        try
        {
            Class.forName("org.postgresql.Driver");
            Connection conn= DriverManager.getConnection(dbUrl, "postgres","Surfdude04");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from hrlive");
            out.println("<table border=1 width=50% height=50%>");
            out.println("<tr><th>TIME</th><th>HEART RATE</th></tr>");
            while (rs.next())
            {
                Timestamp tm = rs.getTimestamp("timerec");
                Float hr = rs.getFloat("heartrate");
                out.println("<tr><td>"+tm+"</td><td>"+hr+"</td></tr>");


            }
            out.println("</table>");
            out.println("</html></body>");
            conn.close();
        }catch (Exception e){out.println("error");}
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
