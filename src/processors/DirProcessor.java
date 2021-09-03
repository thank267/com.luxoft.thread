package processors;

import java.io.File;
import java.util.concurrent.CountedCompleter;

public class DirProcessor extends AbstractProcessor {

    /**
     * Serial Version of the class. You have to add it because the ForkJoinTask
     * class implements the Serializable interfaces
     */
    private static final long serialVersionUID = -1826436670135695513L;

    /**
     * Constructor of the class
     * 
     * @param completer Completer of the task;
     * @param file      File is going to process
     * @param toFind    String to find in file
     */
    protected DirProcessor(CountedCompleter<?> completer, File file, String toFind) {
        super(completer, file, toFind);
    }

    /**
     * Constructor of the class
     * 
     * @param file   File is going to process
     * @param toFind String to find in file
     */
    public DirProcessor(File file, String toFind) {
        super(file, toFind);

    }

    @Override
    public void onCompletion(CountedCompleter<?> completer) {

        getTasks().forEach(childTask -> 
            getResultList().addAll(childTask.getResultList())
        );

    }

    @Override
    public void process(File file) {

        AbstractProcessor task = file.isDirectory() ?
                new DirProcessor(this, file, getToFind()) :
                new FileProcessor(this, file, getToFind());

        task.fork();
        addToPendingCount(1);
        getTasks().add(task);

    }

}
