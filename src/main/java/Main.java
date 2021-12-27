import java.sql.*;

public class Main {
    public static void main(String[] args){


        DatabaseReader.Read("patient0ecg","ecglive","ECG",1,128937);
        //DatabaseReader.ClearTable("ecglive");
    }
}
