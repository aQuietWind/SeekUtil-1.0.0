package seek.util.common.common.util.Exception;

import seek.util.common.config.object.common.common.object.exception.BizException;
import seek.util.common.config.object.common.common.object.exception.ErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BizThrowUtil {

    public static void throwBizError(ErrorCodeEnum errorCodeEnum){
        throw new BizException(errorCodeEnum);
    }
}
