package workers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import processors.AbstractProcessor;

public class MonitorThread extends Thread {

    public static volatile BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                if (reader.ready()) {
                    String i = reader.readLine();
                    Thread.currentThread().interrupt();
                    AbstractProcessor.stopper.callback(i);

                }
            } catch (IOException e) {

            }
        }
    }

}
