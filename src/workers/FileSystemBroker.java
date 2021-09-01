package workers;

import java.io.File;
import processors.Processor;
import processors.DirProcessor;
import processors.FileProcessor;
import executors.FindStringInFileExecutorService;

public class FileSystemBroker implements Runnable {

    final private FindStringInFileExecutorService service;

    public FileSystemBroker(FindStringInFileExecutorService service) {

        this.service = service;

    }

    @Override
    public void run() {

        while (service.getMessageQueue().isEmpty() == false && !service.isShutdown()) {

            File file = service.getMessageQueue().remove();

            Processor processor = (file.isDirectory() ? new DirProcessor(service) : new FileProcessor(service));

            processor.find(file);
        }

    }

}
