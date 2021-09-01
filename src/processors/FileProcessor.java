package processors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import executors.FindStringInFileExecutorService;

public class FileProcessor extends AbstractProcessor {

    public FileProcessor(FindStringInFileExecutorService service) {
        super(service);

    }

    @Override
    public void find(File file) {

        try {
            toCheckCallback();

            if (searchText(file, getService().getToFind())) {

                getLog().info("Нашли файл: " + file.getAbsolutePath());

                getService().getResult().add(file.getAbsolutePath());

            }

        } catch (Exception e) {

        }

    }

    private boolean searchText(File file, String toFind) {

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

    }

}
