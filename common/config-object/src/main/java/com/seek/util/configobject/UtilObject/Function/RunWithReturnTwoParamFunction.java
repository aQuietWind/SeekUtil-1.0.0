package com.seek.util.configobject.UtilObject.Function;

@FunctionalInterface
public interface RunWithReturnTwoParamFunction<R,P1,P2> {
    R run(P1 param,P2 param2);
}
