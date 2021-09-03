package processors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Optional;
import java.util.concurrent.CountedCompleter;
import java.util.function.Predicate;

public class FileProcessor extends AbstractProcessor {

    /**
     * Serial Version of the class. You have to add it because the ForkJoinTask
     * class implements the Serializable interfaces
     */
    private static final long serialVersionUID = -1826436670135695513L;

    private Optional<File> result = Optional.empty();

    /**
     * Constructor of the class
     * 
     * @param completer Completer of the task;
     * @param file      File is going to process
     * @param toFind    String to find in file
     */
    protected FileProcessor(CountedCompleter<?> completer, File file, String toFind) {
        super(completer, file, toFind);

    }

    @Override
    public void onCompletion(CountedCompleter<?> completer) {

        result.ifPresent(file -> getResultList().add(file));

    }

    @Override
    public void process(File file) {

        if (searchText().test(file)) {
            result = Optional.of(file);
        }

    }

    private Predicate<File> searchText() {

        return file -> {
            if (!file.canRead() || !file.exists())

            {
                return false;
            }

            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
                return bufferedReader.lines().filter(line -> line.contains(getToFind())).findAny().isPresent();
            }

            catch (Exception e) {
                return false;
            }
        };

    }

}
