import java.util.logging.Logger;

import executors.FindStringInFileExecutorService;
import workers.FileSystemBroker;
import workers.MonitorThread;

public class App {

    final static String START_DIR = System.getProperty("user.home");
    final static String TO_FIND = "Getting";

    public static void main(String[] args) throws Exception {
        final Logger log = Logger.getLogger(App.class.getName());

        MonitorThread monitorThread = new MonitorThread();

        monitorThread.start();

        FindStringInFileExecutorService service = new FindStringInFileExecutorService(TO_FIND, START_DIR);

        log.info("Ждем результатов");
        service.submit(new FileSystemBroker(service)).get();

        log.info(String.format("Количество найденных файлов: %d", service.getResult().size()));
        log.info(String.format("Нашли сроку: \"%s\" в следующих файлах: %s", TO_FIND, service.toString()));

    }

}
