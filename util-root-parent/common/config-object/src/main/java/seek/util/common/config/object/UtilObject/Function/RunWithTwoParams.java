package seek.util.common.config.object.UtilObject.Function;

@FunctionalInterface
public interface RunWithTwoParams <E,T> {
    void function(E param,T param2);
}
