package workers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Consumer;
import java.util.function.Predicate;

import java.util.function.BooleanSupplier;

import processors.DirProcessor;

public class MonitorThread {

    public static volatile BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    private Thread thread;

    public void start() {

        thread = new Thread(() -> {
            while (threadNotInterrapted().getAsBoolean()) {

                if (readerIsReady().test(reader))
                    readLine().accept(reader);

            }
        });

        thread.start();
    }

    public BooleanSupplier threadNotInterrapted() {
        return () -> !Thread.currentThread().isInterrupted();
    }

    public Predicate<BufferedReader> readerIsReady() {
        return t -> {
            try {
                return t.ready();
            } catch (IOException e) {
                return false;
            }
        };
    }

    public Consumer<BufferedReader> readLine() {
        return t -> {

            try {
                DirProcessor.stopper.callback(t.readLine());

            } catch (IOException e) {
                DirProcessor.stopper.callback("stop");
            } finally {
                Thread.currentThread().interrupt();
            }

        };
    }

    public void stop() {

        thread.interrupt();

    }

}
