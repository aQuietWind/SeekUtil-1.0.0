package com.seek.util.configobject.UtilObject.Function;

@FunctionalInterface
public interface RunWithReturnParamFunction<T,F> {
    T run(F param);
}
