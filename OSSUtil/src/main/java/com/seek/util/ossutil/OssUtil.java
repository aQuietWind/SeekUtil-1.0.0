package com.seek.util.ossutil;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.seek.util.configobject.AliOSSData.OssData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@Lazy
public class OssUtil {

    public static final String endPointPrefix="https://";

    private final ConcurrentHashMap<String,OSS> ossClients =new ConcurrentHashMap<>();

    private OSS buildOSSClient(OssData config){
        config.setEndpoint(config.getEndpoint().replace(endPointPrefix,""));
        return new OSSClientBuilder().build(endPointPrefix+config.getEndpoint(), config.getAccessKeyId(), config.getAccessKeySecret());
    }

    private OSS getOssClient(OssData config){
        if (config==null||config.getBucketName()==null||config.getBucketName().isBlank()){
            return null;
        }
        if(!ossClients.containsKey(config.getBucketName())) {
            ossClients.putIfAbsent(config.getBucketName(), buildOSSClient(config));
        }
        return ossClients.get(config.getBucketName());
    }

    public String putPublicFile(MultipartFile file, OssData config,String fileName){
        OSS ossClient= getOssClient(config);
        if (ossClient==null)return null;
        try{
            ossClient.putObject(config.getBucketName(),fileName,file.getInputStream());
            // 拼接外网访问地址，bucket.endpoint/objectName
            return endPointPrefix+config.getBucketName()+"."+config.getEndpoint()+"/"+fileName;
        } catch (Exception e){
            log.error("AliyunOSS在{}进行存储时,发生异常:",config,e);
            return null;
        }
    }
}
