package runners;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import processors.AbstractProcessor;

public class RunnerImpl<T extends AbstractProcessor> implements Runner<T> {

    @Override
    public void run(T d) {

        // Check task for cancel
        d.toCheckCallback().accept(AbstractProcessor.stop);

        // Process task
        Optional.ofNullable(d.getFile().listFiles())
                .ifPresentOrElse(list -> Arrays.stream(list).filter(Objects::nonNull).forEach(file -> d.process(file)),
                 ()-> d.process(d.getFile()));

        // Try the completion of the task
        d.tryComplete();

    }

}
