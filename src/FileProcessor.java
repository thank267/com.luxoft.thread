import java.util.concurrent.ExecutorService;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.io.File;
import java.nio.file.Files;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;

class FileProcessor {
    private final ExecutorService pool;
    private final ConcurrentLinkedQueue<File> files;
    private final ConcurrentLinkedQueue<String> result;
    private final String toFind;

    public FileProcessor(ExecutorService pool, ConcurrentLinkedQueue<File> files, ConcurrentLinkedQueue<String> result,
            final String toFind) {
        this.pool = pool;
        this.files = files;
        this.result = result;
        this.toFind = toFind;
    }

    void processFile() {

        pool.execute(() -> {
            try {
                System.out.println("Thread processFile started: " + Thread.currentThread().getName());
                while (files.isEmpty() == false) {
                    File current = files.remove();

                    if (searchText(current, toFind)) {

                        System.out.println("+++++++++++++++++++++++++++++++++++++++");
                        System.out.println(current.getAbsolutePath());
                        System.out.println("+++++++++++++++++++++++++++++++++++++++");

                        result.add(current.getAbsolutePath());
                    }

                }
            
            } catch (Exception e) {
                
                e.printStackTrace();
            }

        });

    }

    public boolean searchText(File file, String toFind) throws IOException {

        if (!file.canRead() || !file.exists())

        {
            System.out.println("Не можем прочитать: " + file.getAbsolutePath());
            return false;
        }

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            return bufferedReader.lines().filter(line -> line.contains(toFind)).findAny().isPresent();
        }
        
    }
}