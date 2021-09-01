import java.io.File;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import processors.DirProcessor;
import workers.MonitorThread;

public class App {

    final static String START_DIR = System.getProperty("user.home");
    final static String TO_FIND = "test";

    public static void main(String[] args) throws Exception {
        final Logger log = Logger.getLogger(App.class.getName());

        MonitorThread monitorThread = new MonitorThread();

        monitorThread.start();

        ForkJoinPool service = new ForkJoinPool();

        DirProcessor proc = new DirProcessor(new File(START_DIR), TO_FIND);

        service.execute(proc);

        log.info("Ждем результатов");
        do {

            try {
                TimeUnit.MILLISECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (!proc.isDone());

        service.shutdown();

        

        if (proc.isCompletedNormally()) {
            log.info("Main: The process has completed normally.\n");
        } else {
            log.info("Main: The process has canceled from keyboard!\n");
        }

        log.info("Нашли");

        log.info(String.format("Количество найденных файлов: %d", proc.getResultList().size()));

        System.out.println(proc.getResultList());

        monitorThread.stop();
    }

}
