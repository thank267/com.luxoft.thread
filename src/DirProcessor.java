import java.util.concurrent.ExecutorService;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.io.File;

class DirProcessor {
    private final ExecutorService pool;
    private final ConcurrentLinkedQueue<File> files;

    public DirProcessor(ExecutorService pool, ConcurrentLinkedQueue<File> files) {
        this.pool = pool;
        this.files = files;
    }

    void processFolder(File inputPath) {
       

        try {

            if (inputPath.canRead() && inputPath.exists()) {

                Arrays.asList(inputPath.listFiles()).forEach( file -> {
                    if (file.isDirectory()) {
                        pool.execute(() -> {
                            processFolder(file);
                            //System.out.println("Thread processFolder started: " + Thread.currentThread().getName());
                        });

                    } else {
                        files.add(file);
                    }
                });
             
            }
            else {
                //System.out.println("Не можем прочитать: "+ inputPath);
            }
           
        }
        catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            System.out.println(inputPath);
            e.printStackTrace();
            System.exit(0);
        }

        
    }
}
