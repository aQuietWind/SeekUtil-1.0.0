package seek.util.common.config.object.UtilObject.Function;

@FunctionalInterface
public interface RunWithReturnTwoParamFunction<R,P1,P2> {
    R run(P1 param,P2 param2);
}
