/* Reference 1 - taken from https://stackoverflow.com/questions/9418406/running-
two-functions-with-threading */

public class Socket {
    public void startThreads() {
        new Thread(new Th1()).start();
        new Thread(new Th2()).start();

    }

    class Th1 implements Runnable {
        @Override
        public void run() {
            DatabaseReader.Read("patient0hr", "hrlive", "heartrate", 3000, 128937);
            ;
        }
    }

    class Th2 implements Runnable {
        @Override
        public void run() {
            DatabaseReader.Read("patient0ecg", "ecglive", "ecg", 30, 128937);
            ;
        }
    }
}
/* end of reference 1 */