package processors;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountedCompleter;
import java.util.function.Consumer;

import callbacks.Callback;
import runners.Runner;
import runners.RunnerImpl;

public abstract class AbstractProcessor extends CountedCompleter<List<File>> {

    /**
     * Stop thread semaphore
     */
    public static Boolean stop = false;

    /**
     * Stop thread Callback
     */
    public static Callback stopper = args -> {
        stop = true;
    };

    private static Runner<AbstractProcessor> runner = new RunnerImpl<>();

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
    final private Set<File> resultList = new HashSet<File>();

    /**
     * Child tasks
     */
    final private List<AbstractProcessor> tasks = new ArrayList<>();

    public AbstractProcessor(CountedCompleter<?> completer, File file, String toFind) {
        super(completer);
        this.file = file;
        this.toFind = toFind;

    }

    public AbstractProcessor(File file, String toFind) {
        this.file = file;
        this.toFind = toFind;

    }

    @Override
    public void compute() {

        runner.run(this);

    }

    public abstract void process(File file);

    public Consumer<Boolean> toCheckCallback() {

        return t -> {

            if (t)
                getRoot().tryComplete();
        };

    }

    /**
     * @return the file
     */
    public File getFile() {
        return file;
    }

    /**
     * @return the resultList
     */
    public Set<File> getResultList() {
        return resultList;
    }

    /**
     * @return the tasks
     */
    public List<AbstractProcessor> getTasks() {
        return tasks;
    }

    /**
     * @return the toFind
     */
    public String getToFind() {
        return toFind;
    }

}