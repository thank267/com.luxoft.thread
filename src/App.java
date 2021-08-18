import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutorService;
import java.io.File;
import java.nio.file.Files;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.io.IOException;

public class App {
    public static void main(String[] args) throws Exception {

        final int threadsNumber = 5;

        ExecutorService executorService = Executors.newFixedThreadPool(threadsNumber);
        

        ConcurrentLinkedQueue<File> files = new ConcurrentLinkedQueue<File>();
        ConcurrentLinkedQueue<String> result = new ConcurrentLinkedQueue<String>();

        File startDir = new File("/");
        String toFind = "test";
 

        DirProcessor dirProcessor = new DirProcessor(executorService, files);
        dirProcessor.processFolder(startDir);

        FileProcessor fileProcessor = new FileProcessor(executorService, files, result, toFind);
        fileProcessor.processFile();
    }
    
       
    
    

    
}
