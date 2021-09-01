package processors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CountedCompleter;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.logging.Logger;

import callbacks.Callback;

public class DirProcessor extends CountedCompleter<List<File>> {

    private static Logger log = Logger.getLogger(DirProcessor.class.getName());

    /**
     * Serial Version of the class. You have to add it because the ForkJoinTask
     * class implements the Serializable interfaces
     */
    private static final long serialVersionUID = -1826436670135695513L;

    /**
     * Child tasks
     */
    private List<DirProcessor> tasks;

    /**
     * Start path
     */
    final private File file;

    /**
     * To find
     */
    final private String toFind;

    /**
     * Result list
     */
    private Set<File> resultList;

    /**
     * Stop thread semaphore
     */
    public static boolean stop = false;

    /**
     * Stop thread Callback
     */
    public static Callback stopper = args -> {
        stop = true;
    };

    /**
     * Constructor of the class
     * 
     * @param completer Completer of the task;
     * @param file      File is going to process
     * @param toFind    String to find in file
     */
    protected DirProcessor(CountedCompleter<?> completer, File file, String toFind) {
        super(completer);
        this.file = file;
        this.toFind = toFind;

    }

    /**
     * Constructor of the class
     * 
     * @param file   File is going to process
     * @param toFind String to find in file
     */
    public DirProcessor(File file, String toFind) {
        this.toFind = toFind;
        this.file = file;

    }

    @Override
    public void compute() {

        toCheckCallback().accept(stop);

        resultList = new HashSet<File>();

        tasks = new ArrayList<>();

        Arrays.stream(file.listFiles()).filter(Objects::nonNull).forEach(file -> {
            if (isDirectory().test(file)) {
                processDir().accept(file);
            } else {
                processFile().accept(file);
            }
        });

        // Try the completion of the task
        tryComplete();
    }

    @Override
    public void onCompletion(CountedCompleter<?> completer) {

        tasks.forEach(childTask -> {
            resultList.addAll(childTask.getResultList());
        });

    }

    public Consumer<File> processDir() {
        return dir -> {

            DirProcessor task = new DirProcessor(this, dir, toFind);
            task.fork();
            addToPendingCount(1);
            tasks.add(task);
        };

    }

    public Consumer<File> processFile() {
        return f -> {

            if (searchText().test(f)) {

                resultList.add(f);
            }
        };

    }

    public Predicate<File> isDirectory() {
        return t -> t.isDirectory();

    }

    public Consumer<Boolean> toCheckCallback() {

        return t -> {
            if (t)
                getRoot().tryComplete();
        };

    }

    public Predicate<File> searchText() {

        return file -> {
            if (!file.canRead() || !file.exists())

            {
                return false;
            }

            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
                return bufferedReader.lines().filter(line -> line.contains(toFind)).findAny().isPresent();
            }

            catch (Exception e) {
                return false;
            }
        };

    }

    /**
     * @return the resultList
     */
    public Set<File> getResultList() {
        return resultList;
    }

    /**
     * @return the log
     */
    public static Logger getLog() {
        return log;
    }

}
