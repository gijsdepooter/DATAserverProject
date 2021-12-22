import java.sql.Timestamp;

public class Patient {
    private int patientID;
    private Timestamp timeRec;
    private int HR;

    public void setPatientID(int patientID){
        this.patientID = patientID;
    }

    public void setTimeRec(Timestamp timeRec){
        this.timeRec = timeRec;
    }

    public void setHR(int HR){
        this.HR = HR;
    }

    public int getPatientID(){
        return patientID;
    }

    public Timestamp getTimeRec(){
        return timeRec;
    }

    public int getHR(){
        return HR;
    }




}
