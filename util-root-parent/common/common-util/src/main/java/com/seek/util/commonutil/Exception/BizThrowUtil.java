package com.seek.util.commonutil.Exception;

import com.seek.util.configobject.UtilObject.Exception.BizException;
import com.seek.util.configobject.UtilObject.Exception.ErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BizThrowUtil {

    public static void throwBizError(ErrorCodeEnum errorCodeEnum){
        throw new BizException(errorCodeEnum);
    }
}
