package executors;

import java.io.File;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class FindStringInFileExecutorService extends ThreadPoolExecutor {

    final private String toFind;
    final private Queue<String> result;
    final private Queue<File> messageQueue;

    private static Logger log = Logger.getLogger(FindStringInFileExecutorService.class.getName());

    public FindStringInFileExecutorService(String toFind, String startDir) {
        super(Runtime.getRuntime().availableProcessors() / 2, Runtime.getRuntime().availableProcessors(), 10,
                TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(Runtime.getRuntime().availableProcessors()));

        this.toFind = toFind;
        this.result = new ConcurrentLinkedQueue<String>();
        this.messageQueue = new ConcurrentLinkedQueue<File>();
        messageQueue.add(new File(startDir));

    }

    public void shutdownAndAwaitTermination() {
        if (!isShutdown()) {
            shutdown();

            try {
                log.info("Pool is await termination!");
                if (!awaitTermination(00, TimeUnit.SECONDS)) {
                    log.info("Pool is shutdown now!");
                    List<Runnable> result = shutdownNow();

                    log.info(result.toString());

                    if (awaitTermination(00, TimeUnit.SECONDS))
                        log.info("Pool did not terminate");
                }
            } catch (InterruptedException ie) {
                log.info("Pool isInterruptedException: " + ie.getLocalizedMessage());
                List<Runnable> result = shutdownNow();
                log.info(result.toString());
                Thread.currentThread().interrupt();
            }
        }

    }

    @Override
    public String toString() {

        return result.toString();

    }

    /**
     * @return the toFind
     */
    public String getToFind() {
        return toFind;
    }

    /**
     * @return the result
     */
    public Queue<String> getResult() {
        return result;
    }

    /**
     * @return the messageQueue
     */
    public Queue<File> getMessageQueue() {
        return messageQueue;
    }

}
