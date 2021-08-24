package processors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.RecursiveAction;
import java.util.logging.Logger;

public class Finder extends RecursiveAction {

    private static Logger log = Logger.getLogger(Finder.class.getName());

    private final int numberToFind;
    private final File startPath;
    private final Queue<String> result;
    private final String toFind;

    public Finder(String toFind, int numberToFind, File startPath, Queue<String> result) {
        this.numberToFind = numberToFind;
        this.startPath = startPath;
        this.result = result;
        this.toFind = toFind;
    }

    @Override
    protected void compute() {

        if (result.size() >= numberToFind)
            return;

        try {

            if (startPath.canRead() && startPath.exists()) {

                if (!startPath.isDirectory()) {
                    if (result.size() < numberToFind && searchText(startPath, toFind)) {

                        result.add(startPath.getAbsolutePath());

                        log.info("Нашли файл: " + startPath.getAbsolutePath());

                    }
                }

                else {
                    Arrays.asList(startPath.listFiles()).forEach(file -> {

                        Finder finder = new Finder(toFind, numberToFind, file, result);
                        finder.compute();

                    });

                }

            }

        } catch (Exception e) {

        }

    }

    public boolean searchText(File file, String toFind) {

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
