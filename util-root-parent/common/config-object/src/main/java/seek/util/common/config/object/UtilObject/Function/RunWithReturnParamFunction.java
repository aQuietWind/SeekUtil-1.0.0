package seek.util.common.config.object.UtilObject.Function;

@FunctionalInterface
public interface RunWithReturnParamFunction<T,F> {
    T run(F param);
}
