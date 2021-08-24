import java.io.File;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import processors.Finder;

public class App {

    final static String START_DIR = "/";
    final static String TO_FIND = "test";
    final static int RESULT_COUNT = 10;
    final static int DURATION_TO_EXECUTE = 100;

    public static void main(String[] args) throws Exception {

        final Logger log = Logger.getLogger(App.class.getName());

        final int threadsNumber = Runtime.getRuntime().availableProcessors();

        final ForkJoinPool forkJoinPool = new ForkJoinPool(threadsNumber);

        Queue<String> result = new ConcurrentLinkedQueue<String>();

        log.info("Ждем результатов");

        Finder finder = new Finder(TO_FIND, RESULT_COUNT, new File(START_DIR), result);

        forkJoinPool.execute(finder);

        try {
            finder.get(DURATION_TO_EXECUTE, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            finder.cancel(true);

            log.info("Вышли по timeOut");

        } finally {
            forkJoinPool.shutdownNow();
        }

        log.info(String.format("Количество найденных файлов: %d", result.size()));
        log.info(String.format("Нашли сроку: \"%s\" в следующих файлах: %s", TO_FIND, result.toString()));
    }

}
