package runners;

@FunctionalInterface
public interface Runner<D> {

    public void run(D d);

}
