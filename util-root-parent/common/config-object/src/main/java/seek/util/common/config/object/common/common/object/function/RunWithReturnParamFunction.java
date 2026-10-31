package seek.util.common.config.object.common.common.object.function;

@FunctionalInterface
public interface RunWithReturnParamFunction<T,F> {
    T run(F param);
}
