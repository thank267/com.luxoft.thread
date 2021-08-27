package processors;

import java.util.logging.Logger;

import callbacks.Callback;
import executors.FindStringInFileExecutorService;

public abstract class AbstractProcessor implements Processor {

    private static Logger log = Logger.getLogger(Processor.class.getName());

    final private FindStringInFileExecutorService service;

    public static boolean stop = false;

    public static Callback stopper = args -> {
        stop = true;
    };

    public AbstractProcessor(FindStringInFileExecutorService service) {
        this.service = service;

    }

    public void toCheckCallback() {
        if (stop) {
            service.shutdownAndAwaitTermination();
        }
    }

    /**
     * @return the log
     */
    public static Logger getLog() {
        return log;
    }

    /**
     * @return the service
     */
    public FindStringInFileExecutorService getService() {
        return service;
    }

}
