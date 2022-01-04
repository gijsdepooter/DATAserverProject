
import java.sql.Timestamp;
import java.util.Vector;

public class Patient {
    private Vector<Integer> patientID = new Vector<>();
    private Vector<Timestamp> timeRec = new Vector<>();
    private Vector<Integer> HR = new Vector<>();
    private Vector<Float> ECG = new Vector<>();;

    public void setPatientID(int patientID){
        this.patientID.add(patientID);
    }

    public void setTimeRec(Timestamp timeRec){
        this.timeRec.add(timeRec);
    }

    public void setHR(int HR){
        this.HR.add(HR);
    }

    public void setECG(Float ecg) throws Exception{
        this.ECG.add(ecg);
    }

    public void PrintPatient(){
        System.out.println(patientID);
        System.out.println(timeRec);
        System.out.println(HR);
        System.out.println(ECG);


    }




}
