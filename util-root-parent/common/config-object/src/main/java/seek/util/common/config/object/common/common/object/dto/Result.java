package seek.util.common.config.object.common.common.object.dto;


import seek.util.common.config.object.common.common.object.exception.ErrorCodeEnum;
import lombok.Data;

@Data
public class Result<T> {
    private  int code;
    private String msg;
    private T data;
    public Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(200, "success", data);
    }
    public static <T> Result<T> success() {
        return new Result<>(200, "success",null);
    }
    public static <T> Result<T> error(int code,String msg) {
        return new Result<T>(code, msg, null);
    }
    public static <T> Result<T> error(ErrorCodeEnum errorCodeEnum) {
        return new Result(errorCodeEnum.getCode(),errorCodeEnum.getDefaultMsg(),null);
    }
}
