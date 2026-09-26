package com.seek.util.rocketmqutil;

import com.seek.util.configobject.UtilObject.Function.RunWithReturnFunction;
import com.seek.util.configobject.UtilObject.Function.RunWithReturnParamFunction;

import java.util.concurrent.ConcurrentHashMap;

public class BizStore {
    public static final String splitSign="-";
    public static final int codeIndex=0;
    public static final int keyIndex=1;
    private final ConcurrentHashMap<String , RunWithReturnFunction<Boolean>> bizStore = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String , RunWithReturnParamFunction<Boolean,String>> bizCheckStore = new ConcurrentHashMap<>();

    public RunWithReturnFunction<Boolean> getBiz(String bizCode){
        return bizStore.get(bizCode);
    }

    public Boolean putBiz(String bizCode,RunWithReturnFunction<Boolean> biz){
        return bizStore.putIfAbsent(bizCode, biz) != null;
    }
    public RunWithReturnParamFunction<Boolean,String> getBizCheck(String bizCode){
        return bizCheckStore.get(bizCode);
    }

    public Boolean putBizCheck(String bizCode,RunWithReturnParamFunction<Boolean,String> bizCheck){
        return bizCheckStore.putIfAbsent(bizCode, bizCheck) != null;
    }

    //拼接
    public String codeConcatenateKey(String code,String key) {
        return code + splitSign + key;
    }

    //拆解
    public String[] getCodeAndKey(String codeKey){
        if (codeKey==null) return null;
        return codeKey.split(splitSign,2);
    }
}
