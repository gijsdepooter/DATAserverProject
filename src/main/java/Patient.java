
import java.sql.Timestamp;
import java.util.Vector;

public class Patient {
    private int patientID;
    private Timestamp timeRec;
    private int HR;
    private Vector<Float> ECG = new Vector<>(10);;

    public void setPatientID(int patientID){
        this.patientID = patientID;
    }

    public void setTimeRec(Timestamp timeRec){
        this.timeRec = timeRec;
    }

    public void setHR(int HR){
        this.HR = HR;
    }

    public void setECG(Float ecg) throws Exception{
        this.ECG.add(ecg);
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

    public void PrintPatient(){
        System.out.println(patientID);
        System.out.println(timeRec);
        System.out.println(HR);
        System.out.println(ECG);


    }




}
