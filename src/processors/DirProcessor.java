package processors;

import java.io.File;
import java.util.Arrays;

import executors.FindStringInFileExecutorService;

import workers.FileSystemBroker;

public class DirProcessor extends AbstractProcessor {

    public DirProcessor(FindStringInFileExecutorService service) {
        super(service);

    }

    @Override
    public void find(File dir) {

        try {
            toCheckCallback();

            Arrays.asList(dir.listFiles()).forEach(fl -> {
                getService().getMessageQueue().add(fl);
            });

            getService().submit(new FileSystemBroker(getService()));
        } catch (Exception e) {

        }

    }

}
